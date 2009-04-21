/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

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
        final DefaultPageCreator creator = new DefaultPageCreator();

        assertEquals("html", creator.determinePageType("application/vnd.wap.xhtml+xml"));
        assertEquals("html", creator.determinePageType("application/xhtml+xml"));
        assertEquals("html", creator.determinePageType("text/html"));
        assertEquals("html", creator.determinePageType("text/xhtml"));

        assertEquals("javascript", creator.determinePageType("text/javascript"));
        assertEquals("javascript", creator.determinePageType("application/x-javascript"));

        assertEquals("xml", creator.determinePageType("text/xml"));
        assertEquals("xml", creator.determinePageType("application/xml"));
        assertEquals("xml", creator.determinePageType("text/vnd.wap.wml"));
        assertEquals("xml", creator.determinePageType("application/vnd.mozilla.xul+xml"));
        assertEquals("xml", creator.determinePageType("application/rdf+xml"));
        assertEquals("xml", creator.determinePageType("image/svg+xml"));

        assertEquals("text", creator.determinePageType("text/plain"));
        assertEquals("text", creator.determinePageType("text/csv"));
        assertEquals("text", creator.determinePageType("text/css"));

        assertEquals("unknown", creator.determinePageType("application/pdf"));
        assertEquals("unknown", creator.determinePageType("application/x-shockwave-flash"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
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

        private static final long serialVersionUID = 249364661058883744L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final Writer writer = response.getWriter();
            writer.write("<html><head><meta http-equiv='Content-Type' content='text/html'></head>"
                + "<body>Hello World</body></html>");
            writer.close();
        }
    }
}
