/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.DefaultPageCreator.PageType;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Tests for {@link DefaultPageCreator}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DefaultPageCreatorTest extends WebServerTestCase {

    /**
     * Test for {@link DefaultPageCreator#determinePageType(String)}.
     */
    @Test
    @Browsers(Browser.NONE)
    public void determinePageType() {
        assertEquals(PageType.HTML, DefaultPageCreator.determinePageType("text/html"));

        assertEquals(PageType.JAVASCRIPT, DefaultPageCreator.determinePageType("text/javascript"));
        assertEquals(PageType.JAVASCRIPT, DefaultPageCreator.determinePageType("application/x-javascript"));

        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("text/xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/xhtml+xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("text/vnd.wap.wml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/vnd.mozilla.xul+xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/vnd.wap.xhtml+xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/rdf+xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("image/svg+xml"));

        assertEquals(PageType.TEXT, DefaultPageCreator.determinePageType("text/plain"));
        assertEquals(PageType.TEXT, DefaultPageCreator.determinePageType("text/csv"));
        assertEquals(PageType.TEXT, DefaultPageCreator.determinePageType("text/css"));
        assertEquals(PageType.TEXT, DefaultPageCreator.determinePageType("text/xhtml"));

        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType(null));
        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType(""));
        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType(" \t"));
        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType("application/pdf"));
        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType("application/x-shockwave-flash"));
    }

    /**
     * Verifies page types generated by various combinations of content types, doctypes and namespaces.
     * Uses a real web server so that results can be easily verified against real browsers.
     * @throws Exception if the test fails
     */
    @Test
    public void contentTypes() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/x", ContentTypeServlet.class);
        startWebServer("./", null, servlets);

        final WebClient c = getWebClient();
        final String base = "http://localhost:" + PORT + "/x?";

        assertTrue(c.<Page>getPage(base + "type=text%2Fhtml") instanceof HtmlPage);
        assertTrue(c.<Page>getPage(base + "type=text%2Fhtml&doctype=1") instanceof HtmlPage);
        assertTrue(c.<Page>getPage(base + "type=text%2Fhtml&ns=1") instanceof HtmlPage);
        assertTrue(c.<Page>getPage(base + "type=text%2Fhtml&doctype=1&ns=1") instanceof HtmlPage);

        assertTrue(c.<Page>getPage(base + "type=text%2Fxhtml") instanceof TextPage);
        assertTrue(c.<Page>getPage(base + "type=text%2Fxhtml&doctype=1") instanceof TextPage);
        assertTrue(c.<Page>getPage(base + "type=text%2Fxhtml&ns=1") instanceof TextPage);
        assertTrue(c.<Page>getPage(base + "type=text%2Fxhtml&doctype=1&ns=1") instanceof TextPage);

        assertTrue(c.<Page>getPage(base + "type=text%2Fxml") instanceof XmlPage);
        assertTrue(c.<Page>getPage(base + "type=text%2Fxml&doctype=1") instanceof XmlPage);
        assertTrue(c.<Page>getPage(base + "type=text%2Fxml&ns=1") instanceof XHtmlPage);
        assertTrue(c.<Page>getPage(base + "type=text%2Fxml&doctype=1&ns=1") instanceof XHtmlPage);

        assertTrue(c.<Page>getPage(base + "type=application%2Fxhtml%2Bxml") instanceof XmlPage);
        assertTrue(c.<Page>getPage(base + "type=application%2Fxhtml%2Bxml&doctype=1") instanceof XmlPage);
        assertTrue(c.<Page>getPage(base + "type=application%2Fxhtml%2Bxml&ns=1") instanceof XHtmlPage);
        assertTrue(c.<Page>getPage(base + "type=application%2Fxhtml%2Bxml&doctype=1&ns=1") instanceof XHtmlPage);
    }

    /**
     * Servlet for {@link #contentTypes()}.
     */
    public static class ContentTypeServlet extends HttpServlet {
        private static final String XHTML_DOCTYPE =
              "<!DOCTYPE html PUBLIC\n"
            + "\"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n"
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        /** {@inheritDoc} */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType(request.getParameter("type"));
            final Writer writer = response.getWriter();
            final boolean doctype = (request.getParameter("doctype") != null);
            if (doctype) {
                writer.write(XHTML_DOCTYPE);
            }
            writer.write("<html");
            final boolean ns = (request.getParameter("ns") != null);
            if (ns) {
                writer.write(" xmlns='http://www.w3.org/1999/xhtml'");
            }
            writer.write("><body>foo</body></html>");
            writer.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void noContentType() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", NoContentTypeServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/test");
        assertNotNull(page);
    }

    /**
     * Servlet for {@link #noContentType()}.
     */
    public static class NoContentTypeServlet extends HttpServlet {
        /** {@inheritDoc} */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final Writer writer = response.getWriter();
            writer.write("<html><head><meta http-equiv='Content-Type' content='text/html'></head>"
                + "<body>Hello World</body></html>");
            writer.close();
        }
    }

}
