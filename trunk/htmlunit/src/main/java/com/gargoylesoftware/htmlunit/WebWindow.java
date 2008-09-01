/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

/**
 * An interface that represents one window in a browser. It could be a top level window or a frame.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author David D. Kilzer
 */
public interface WebWindow {

    /**
     * Returns the name of this window.
     *
     * @return the name of this window
     */
    String getName();

    /**
     * Sets the name of this window.
     *
     * @param name the new window name
     */
    void setName(final String name);

    /**
     * Returns the currently loaded page or null if no page has been loaded.
     *
     * @param <P> the page
     * @return the currently loaded page or null if no page has been loaded
     */
    <P extends Page> P getEnclosedPage();

    /**
     * Sets the currently loaded page.
     *
     * @param page the new page or null if there is no page (ie empty window)
     */
    void setEnclosedPage(final Page page);

    /**
     * Returns the window that contains this window. If this is a top
     * level window, then return this window.
     *
     * @return the parent window or this window if there is no parent
     */
    WebWindow getParentWindow();

    /**
     * Returns the top level window that contains this window. If this
     * is a top level window, then return this window.
     *
     * @return the top level window that contains this window or this
     * window if there is no parent.
     */
    WebWindow getTopWindow();

    /**
     * Returns the web client that "owns" this window.
     *
     * @return the web client or null if this window has been closed
     */
    WebClient getWebClient();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Set the JavaScript object that corresponds to this element. This is not guaranteed
     * to be set even if there is a JavaScript object for this HTML element.
     * @param scriptObject the JavaScript object
     */
    void setScriptObject(final Object scriptObject);

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Return the JavaScript object that corresponds to this element.
     * @return the JavaScript object that corresponds to this element
     */
    Object getScriptObject();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Currently exposed here for testing purposes, a better API will be added in the
     * future and this will become a completely internal class.
     *
     * @return the ThreadManager for this WebWindow
     */
    ThreadManager getThreadManager();
}

