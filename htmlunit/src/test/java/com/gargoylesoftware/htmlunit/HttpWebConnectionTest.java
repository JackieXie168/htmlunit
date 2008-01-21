/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HttpWebConnectionTest {
    
    /**
     * The listener port for the web server.
     */
    public static final int PORT = 12345;
    
    private Server server_;

    /**
     * Assert that the two byte arrays are equal
     * @param expected The expected value
     * @param actual The actual value
     */
    public static void assertEquals(final byte[] expected, final byte[] actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * Assert that the two byte arrays are equal
     * @param message The message to display on failure
     * @param expected The expected value
     * @param actual The actual value
     */
    public static void assertEquals(
            final String message, final byte[] expected, final byte[] actual) {
        assertEquals(message, expected, actual, expected.length);
    }

    /**
     * Assert that the two byte arrays are equal
     * @param message The message to display on failure
     * @param expected The expected value
     * @param actual The actual value
     * @param length How many characters at the beginning of each byte array will be compared.
     */
    public static void assertEquals(
            final String message, final byte[] expected, final byte[] actual,
            final int length) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            Assert.fail(message);
        }
        if (expected.length < length || actual.length < length) {
            Assert.fail(message);
        }
        for (int i = 0; i < length; i++) {
            Assert.assertEquals(message, expected[i], actual[i]);
        }
    }

    /**
     * Assert that the two input streams are the same.
     * @param expected The expected value
     * @param actual The actual value
     * @throws IOException If an IO problem occurs during comparison
     */
    public static void assertEquals(final InputStream expected, final InputStream actual) throws IOException {
        assertEquals(null, expected, actual);
    }

    /**
     * Assert that the two input streams are the same.
     * @param message The message to display on failure
     * @param expected The expected value
     * @param actual The actual value
     * @throws IOException If an IO problem occurs during comparison
     */
    public static void assertEquals(
            final String message, final InputStream expected,
            final InputStream actual)
        throws IOException {

        if (expected == null && actual == null) {
            return;
        }

        if (expected == null || actual == null) {
            try {
                Assert.fail(message);
            }
            finally {
                try {
                    if (expected != null) {
                        expected.close();
                    }
                }
                finally {
                    if (actual != null) {
                        actual.close();
                    }
                }
            }
        }

        InputStream expectedBuf = null;
        InputStream actualBuf = null;
        try {
            expectedBuf = new BufferedInputStream(expected);
            actualBuf = new BufferedInputStream(actual);

            final byte[] expectedArray = new byte[2048];
            final byte[] actualArray = new byte[2048];

            int expectedLength = expectedBuf.read(expectedArray);
            while (true) {

                final int actualLength = actualBuf.read(actualArray);
                Assert.assertEquals(message, expectedLength, actualLength);

                if (expectedLength == -1) {
                    break;
                }

                assertEquals(message, expectedArray, actualArray, expectedLength);
                expectedLength = expectedBuf.read(expectedArray);
            }
        }
        finally {
            try {
                if (expectedBuf != null) {
                    expectedBuf.close();
                }
            }
            finally {
                if (actualBuf != null) {
                    actualBuf.close();
                }
            }
        }
    }

    /**
     * Test creation of a web response
     * @throws Exception If the test fails
     */
    @Test
    public void testMakeWebResponse() throws Exception {
        final URL url = new URL("http://htmlunit.sourceforge.net/");
        final String content = "<html><head></head><body></body></html>";
        final int httpStatus = HttpStatus.SC_OK;
        final long loadTime = 500L;

        final HttpMethodBase httpMethod = new GetMethod(url.toString());
        final Field responseBodyField = HttpMethodBase.class.getDeclaredField("responseBody");
        responseBodyField.setAccessible(true);
        responseBodyField.set(httpMethod, content.getBytes());

        final StatusLine statusLine = new StatusLine("HTTP/1.0 200 OK");
        final Field statusLineField = HttpMethodBase.class.getDeclaredField("statusLine");
        statusLineField.setAccessible(true);
        statusLineField.set(httpMethod, statusLine);

        final HttpWebConnection connection = new HttpWebConnection(new WebClient());
        final Method method =
                connection.getClass().getDeclaredMethod("makeWebResponse", new Class[]{
                    int.class, HttpMethodBase.class, URL.class, long.class, String.class});
        method.setAccessible(true);

        final WebResponse response =
                (WebResponse) method.invoke(connection, new Object[]{
                    new Integer(httpStatus), httpMethod, url, new Long(loadTime), TextUtil.DEFAULT_CHARSET});

        Assert.assertEquals(httpStatus, response.getStatusCode());
        Assert.assertEquals(url, response.getUrl());
        Assert.assertEquals(loadTime, response.getLoadTimeInMilliSeconds());
        Assert.assertEquals(content, response.getContentAsString());
        assertEquals(content.getBytes(), response.getResponseBody());
        assertEquals(new ByteArrayInputStream(content.getBytes()), response.getContentAsStream());
    }

    /**
     * Testing Jetty
     * @throws Exception on failure
     */
    @Test
    public void testJettyProofOfConcept() throws Exception {
        server_ = startWebServer("./");

        final WebClient client = new WebClient();
        Page page = client.getPage("http://localhost:" + PORT + "/src/test/resources/event_coordinates.html");
        final WebConnection defaultConnection = client.getWebConnection();
        Assert.assertTrue(
                "HttpWebConnection should be the default",
                HttpWebConnection.class.isInstance(defaultConnection));
        Assert.assertTrue("Response should be valid HTML", HtmlPage.class.isInstance(page));

        // test that // is escaped
        final URL url = new URL("http://localhost:" + PORT + "//src/test/resources/event_coordinates.html");
        page = client.getPage(url);
        Assert.assertEquals(url.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resouceBase the base of resources for the default context.
     * @return the started web server.
     * @throws Exception If the test fails.
     */
    public static Server startWebServer(final String resouceBase) throws Exception {
        final Server server = new Server(PORT);

        final Context context = new Context();
        context.setContextPath("/");
        context.setResourceBase(resouceBase);
        
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(resouceBase);

        final HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});
        server.setHandler(handlers);
        server.setHandler(resourceHandler);
        
        server.start();
        return server;
    }

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resouceBase the base of resources for the default context.
     * @param classpath additional classpath entries to add (may be null).
     * @return the started web server.
     * @throws Exception If the test fails.
     */
    public static Server startWebServer(final String resouceBase, final String[] classpath) throws Exception {
        final Server server = new Server(PORT);

        final WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase(resouceBase);
        final WebAppClassLoader loader = new WebAppClassLoader(context);
        if (classpath != null) {
            for (int i = 0; i < classpath.length; i++) {
                loader.addClassPath(classpath[i]);
            }
        }
        context.setClassLoader(loader);
        server.setHandler(context);
        server.start();
        return server;
    }

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resouceBase the base of resources for the default context.
     * @param classpath additional classpath entries to add (may be null).
     * @param servlets Map of <Class,String>, Class is the class, while String is the path spec.
     * @return the started web server.
     * @throws Exception If the test fails.
     */
    public static Server startWebServer(final String resouceBase, final String[] classpath,
            final Map<Class< ? extends Servlet>, String> servlets)
        throws Exception {
        final Server server = new Server(PORT);

        final WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase(resouceBase);
        
        for (final Class< ? extends Servlet> servlet : servlets.keySet()) {
            final String pathSpec = servlets.get(servlet);
            context.addServlet(servlet, pathSpec);
            
        }
        final WebAppClassLoader loader = new WebAppClassLoader(context);
        if (classpath != null) {
            for (int i = 0; i < classpath.length; i++) {
                loader.addClassPath(classpath[i]);
            }
        }
        context.setClassLoader(loader);
        server.setHandler(context);
        server.start();
        return server;
    }

    /**
     * Stops the web server.
     *
     * @param httpServer the web server.
     * @throws Exception If the test fails.
     */
    public static void stopWebServer(final Server httpServer) throws Exception {
        if (httpServer != null) {
            httpServer.stop();
        }
    }

    /**
     * {@inheritDoc}
     * Stops the web server if it has been started.
     */
    @After
    public void tearDown() throws Exception {
        stopWebServer(server_);
        server_ = null;
    }

    /**
     * Test for feature request 1438216: HttpWebConnection should allow extension to create the HttpClient
     * @throws Exception if the test fails
     */
    @Test
    public void testDesignedForExtension() throws Exception {
        server_ = startWebServer("./");

        final WebClient webClient = new WebClient();
        final boolean[] tabCalled = {false};
        final WebConnection myWebConnection = new HttpWebConnection(webClient) {
            protected HttpClient createHttpClient() {
                tabCalled[0] = true;
                return new HttpClient();
            }
        };

        webClient.setWebConnection(myWebConnection);
        webClient.getPage("http://localhost:" + PORT + "/README");
        Assert.assertTrue("createHttpClient has not been called", tabCalled[0]);
    }

    /**
     * Was throwing a NPE on 14.04.06
     * @throws Exception if the test fails
     */
    @Test
    public void testStateAccess() throws Exception {
        final WebClient webClient = new WebClient();
        webClient.getWebConnection().getState();
    }

    /**
     * Test that the right file part is built for a file that doesn't exist
     * @throws Exception if the test fails
     */
    @Test
    public void testBuildFilePart() throws Exception {
        final String encoding = "ISO8859-1";
        final KeyDataPair pair = new KeyDataPair("myFile", new File("this/doesnt_exist.txt"), "text/plain", encoding);
        final FilePart part = new HttpWebConnection(new WebClient()).buildFilePart(pair, encoding);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        part.send(baos);
        
        final String expected = "------------------314159265358979323846\r\n"
            + "Content-Disposition: form-data; name=\"myFile\"; filename=\"doesnt_exist.txt\"\r\n"
            + "Content-Type: text/plain\r\n"
            + "Content-Transfer-Encoding: binary\r\n"
            + "\r\n"
            + "\r\n";
        Assert.assertEquals(expected, baos.toString(encoding));
    }
}
