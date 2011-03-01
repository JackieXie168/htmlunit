/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.background;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * <p>Default implementation of {@link JavaScriptJobManager}.</p>
 *
 * <p>This job manager class is guaranteed not to keep old windows in memory (no window memory leaks).</p>
 *
 * <p>This job manager is serializable, but any running jobs are transient and are not serialized.</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Katharina Probst
 * @author Amit Manjhi
 * @author Ronald Brill
 * @see MemoryLeakTest
 */
public class JavaScriptJobManagerImpl implements JavaScriptJobManager {

    /**
     * The window to which this job manager belongs (weakly referenced, so as not
     * to leak memory).
     */
    private final transient WeakReference<WebWindow> window_;

    /**
     * Queue of jobs that are scheduled to run. This is a priority queue, sorted
     * by closest target execution time.
     */
    private transient PriorityQueue<JavaScriptJob> scheduledJobsQ_ = new PriorityQueue<JavaScriptJob>();

    private transient ArrayList<Integer> cancelledJobs_ = new ArrayList<Integer>();

    private transient JavaScriptJob currentlyRunningJob_ = null;

    /** A counter used to generate the IDs assigned to {@link JavaScriptJob}s. */
    private static final AtomicInteger NEXT_JOB_ID_ = new AtomicInteger(1);

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptJobManagerImpl.class);

    /**
     * Creates a new instance.
     *
     * @param window the window associated with the new job manager
     */
    public JavaScriptJobManagerImpl(final WebWindow window) {
        window_ = new WeakReference<WebWindow>(window);
    }

    /** {@inheritDoc} */
    public synchronized int getJobCount() {
        return scheduledJobsQ_.size() + (currentlyRunningJob_ != null ? 1 : 0);
    }

    /** {@inheritDoc} */
    public int addJob(final JavaScriptJob job, final Page page) {
        final WebWindow w = getWindow();
        if (w == null) {
            /*
             * The window to which this job manager belongs has been garbage
             * collected. Don't spawn any more jobs for it.
             */
            return 0;
        }
        if (w.getEnclosedPage() != page) {
            /*
             * The page requesting the addition of the job is no longer contained by
             * our owner window. Don't let it spawn any more jobs.
             */
            return 0;
        }
        final int id = NEXT_JOB_ID_.getAndIncrement();
        job.setId(id);

        synchronized (this) {
            scheduledJobsQ_.add(job);

            if (LOG.isDebugEnabled()) {
                LOG.debug("\twindow is: " + getWindow());
                LOG.debug("\tadded job: " + job.toString());
                LOG.debug("after adding job to the queue, the queue is: ");
                printQueue();
            }

            notify();
        }

        return id;
    }

    /** {@inheritDoc} */
    public synchronized void removeJob(final int id) {
        for (JavaScriptJob job : scheduledJobsQ_) {
            if (job.getId() == id) {
                scheduledJobsQ_.remove(job);
                break;
            }
        }
        cancelledJobs_.add(id);
        notify();
    }

    /** {@inheritDoc} */
    public synchronized void stopJob(final int id) {
        for (JavaScriptJob job : scheduledJobsQ_) {
            if (job.getId() == id) {
                scheduledJobsQ_.remove(job);
                // TODO: should we try to interrupt the job if it is running?
                break;
            }
        }
        cancelledJobs_.add(id);
        notify();
    }

    /** {@inheritDoc} */
    public synchronized void removeAllJobs() {
        if (currentlyRunningJob_ != null) {
            cancelledJobs_.add(currentlyRunningJob_.getId());
        }
        for (JavaScriptJob job : scheduledJobsQ_) {
            cancelledJobs_.add(job.getId());
        }
        scheduledJobsQ_.clear();
        notify();
    }

    /** {@inheritDoc} */
    public int waitForJobs(final long timeoutMillis) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Waiting for all jobs to finish (will wait max " + timeoutMillis + " millis).");
        }
        if (timeoutMillis > 0) {
            long now = System.currentTimeMillis();
            final long end = now + timeoutMillis;
            while (getJobCount() > 0 && now < end) {
                try {
                    synchronized (this) {
                        wait(end - now);
                    }

                    // maybe a change triggers the wakup; we have to recalculate the
                    // wait time
                    now = System.currentTimeMillis();
                }
                catch (final InterruptedException e) {
                    LOG.error("InterruptedException while in waitForJobs", e);
                }
            }
        }
        final int jobs = getJobCount();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finished waiting for all jobs to finish (final job count is " + jobs + ").");
        }
        return jobs;
    }

    /** {@inheritDoc} */
    public int waitForJobsStartingBefore(final long delayMillis) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Waiting for all jobs that have execution time before " + delayMillis + " to finish");
        }
        final long targetExecutionTime = System.currentTimeMillis() + delayMillis;
        JavaScriptJob earliestJob = getEarliestJob();

        boolean currentJob;
        synchronized (this) {
            currentJob = currentlyRunningJob_ != null
                && currentlyRunningJob_.getTargetExecutionTime() < targetExecutionTime;
        }

        final long interval = Math.max(40, delayMillis);
        while (currentJob
            || (earliestJob != null && earliestJob.getTargetExecutionTime() < targetExecutionTime)) {
            try {
                synchronized (this) {
                    wait(interval);
                }
            }
            catch (final InterruptedException e) {
                LOG.error("InterruptedException while in waitForJobsStartingBefore", e);
            }
            earliestJob = getEarliestJob();
            synchronized (this) {
                currentJob = currentlyRunningJob_ != null
                    && currentlyRunningJob_.getTargetExecutionTime() < targetExecutionTime;
            }
        }
        final int jobs = getJobCount();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finished waiting for all jobs that have target execution time earlier than "
                + targetExecutionTime + ", final job count is " + jobs);
        }
        return jobs;
    }

    /** {@inheritDoc} */
    public synchronized void shutdown() {
        scheduledJobsQ_.clear();
        notify();
    }

    /**
     * Returns the window to which this job manager belongs, or <tt>null</tt> if
     * it has been garbage collected.
     *
     * @return the window to which this job manager belongs, or <tt>null</tt> if
     *         it has been garbage collected
     */
    private WebWindow getWindow() {
        return window_.get();
    }

    /**
     * Utility method to print current queue.
     */
    private void printQueue() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("------ printing JavaScript job queue -----");
            LOG.debug("number of jobs on the queue: " + scheduledJobsQ_.size());
            for (JavaScriptJob job : scheduledJobsQ_) {
                LOG.debug("\tJob target execution time:" + job.getTargetExecutionTime());
                LOG.debug("\tjob to string: " + job.toString());
                LOG.debug("\tjob id: " + job.getId());
            }
            LOG.debug("------------------------------------------");
        }
    }

    /**
     * {@inheritDoc}
     */
    public JavaScriptJob getEarliestJob() {
        return scheduledJobsQ_.peek();
    }

    /**
     * {@inheritDoc}
     */
    public boolean runSingleJob(final JavaScriptJob givenJob) {
        assert givenJob != null;
        final JavaScriptJob job = scheduledJobsQ_.peek();
        if (job != givenJob) {
            return false;
        }

        final long currentTime = System.currentTimeMillis();
        if (job.getTargetExecutionTime() > currentTime) {
            return false;
        }
        synchronized (this) {
            if (scheduledJobsQ_.remove(job)) {
                currentlyRunningJob_ = job;
            }
            // no need to notify if processing is started
        }

        final boolean isPeriodicJob = job.isPeriodic();
        if (isPeriodicJob) {
            // reference: http://ejohn.org/blog/how-javascript-timers-work/
            long timeDifference = currentTime - job.getTargetExecutionTime();
            timeDifference = (timeDifference / job.getPeriod()) * job.getPeriod()
                    + job.getPeriod();
            job.setTargetExecutionTime(job.getTargetExecutionTime() + timeDifference);

            // queue
            synchronized (this) {
                if (!cancelledJobs_.contains(job.getId())) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Reschedulling job " + job);
                    }
                    scheduledJobsQ_.add(job);
                    notify();
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            final String periodicJob = isPeriodicJob ? "interval " : "";
            LOG.debug("Starting " + periodicJob + "job " + job);
        }
        try {
            job.run();
        }
        catch (final RuntimeException e) {
            LOG.error("Job run failed with unexpected RuntimeException: " + e.getMessage(), e);
        }
        finally {
            synchronized (this) {
                if (job == currentlyRunningJob_) {
                    currentlyRunningJob_ = null;
                }
                notify();
            }
        }
        if (LOG.isDebugEnabled()) {
            final String periodicJob = isPeriodicJob ? "interval " : "";
            LOG.debug("Finished " + periodicJob + "job " + job);
        }
        return true;
    }

    /**
     * Our own serialization (to handle the weak reference)
     * @param out the stream to write to
     * @throws IOException in case of error
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * Our own serialization (to handle the weak reference)
     * @param in the stream to read form
     * @throws IOException in case of error
     * @throws ClassNotFoundException in case of error
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // we do not store the jobs (at the moment)
        scheduledJobsQ_ = new PriorityQueue<JavaScriptJob>();
        cancelledJobs_ = new ArrayList<Integer>();
        currentlyRunningJob_ = null;
    }
}
