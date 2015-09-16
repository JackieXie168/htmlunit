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
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlScript}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HtmlScriptTest extends WebTestCase {

    /**
     * Verifies that a failing HTTP status code for a JavaScript file request (like a 404 response)
     * results in a {@link FailingHttpStatusCodeException}, depending on how the client has been
     * configured.
     *
     * @see WebClient#isThrowExceptionOnFailingStatusCode()
     * @throws Exception if an error occurs
     */
    @Test
    public void testBadExternalScriptReference() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
                + "<script src='inexistent.js'></script>\n"
                + "</head><body></body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse("inexistent", 404, "Not Found", "text/html");
        webConnection.setResponse(URL_FIRST, html);
        client.setWebConnection(webConnection);

        try {
            client.getPage(URL_FIRST);
            fail("Should throw.");
        }
        catch (final FailingHttpStatusCodeException e) {
            final String url = URL_FIRST.toExternalForm();
            assertTrue("exception contains URL of failing script", e.getMessage().indexOf(url) > -1);
            assertEquals(404, e.getStatusCode());
            assertEquals("Not Found", e.getStatusMessage());
        }

        client.setThrowExceptionOnFailingStatusCode(false);

        try {
            client.getPage(URL_FIRST);
        }
        catch (final FailingHttpStatusCodeException e) {
            fail("Should not throw.");
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAsText() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<script id='script1'>\n"
            + "    var foo = 132;\n"
            + "</script></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlScript script = page.getHtmlElementById("script1");
        assertEquals("", script.asText());
    }

    /**
     * Verifies that the weird script src attribute used by the jQuery JavaScript library is
     * ignored silently (bug 1695279).
     * @throws Exception if the test fails
     */
    @Test
    public void testInvalidJQuerySrcAttribute() throws Exception {
        loadPage("<html><body><script src='//:'></script></body></html>");
    }

    /**
     * Verifies that if a script element executes "window.location.href=someotherpage", then subsequent
     * script tags, and any onload handler for the original page do not run.
     * @throws Exception if the test fails
     */
    @Test
    public void testChangingLocationSkipsFurtherScriptsOnPage() throws Exception {
        final String firstPage
            = "<html><head></head>\n"
            + "<body onload='alert(\"body onload executing but should be skipped\")'>\n"
            + "<script>alert('First script executes')</script>\n"
            + "<script>window.location.href='" + URL_SECOND + "'</script>\n"
            + "<script>alert('Third script executing but should be skipped')</script>\n"
            + "</body></html>";

        final String secondPage
            = "<html><head></head><body>\n"
            + "<script>alert('Second page loading')</script>\n"
            + "</body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstPage);
        webConnection.setResponse(URL_SECOND, secondPage);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        final String[] expectedAlerts = {"First script executes", "Second page loading"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that a script element is not run when it is cloned.
     * See bug 1707788.
     * @throws Exception if an error occurs
     */
    @Test
    public void testScriptIsNotRunWhenCloned() throws Exception {
        final String html = "<html><body onload='document.body.cloneNode(true)'>\n"
            + "<script>alert('a')</script></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);

        final String[] expectedAlerts = {"a"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testDefer() throws Exception {
        final String html = "<html><head>\n"
            + "<script defer>alert('deferred')</script>\n"
            + "<script>alert('normal')</script>\n"
            + "</head>\n"
            + "<body onload='alert(\"onload\")'>test</body>\n"
            + "</html>";

        final List<String> actualFF = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, actualFF);
        final String[] expectedFF = new String[] {"deferred", "normal", "onload"};
        assertEquals(expectedFF, actualFF);

        final List<String> actualIE = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7, html, actualIE);
        final String[] expectedIE = new String[] {"normal", "deferred", "onload"};
        assertEquals(expectedIE, actualIE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSimpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <script id='myId'></script>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object HTMLScriptElement]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertTrue(HtmlScript.class.isInstance(page.getHtmlElementById("myId")));
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that setting a script's <tt>src</tt> attribute behaves correctly.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSettingSrcAttribute() throws Exception {
        testSettingSrcAttribute(BrowserVersion.FIREFOX_2, new String[] {"1", "2", "3"});
        testSettingSrcAttribute(BrowserVersion.INTERNET_EXPLORER_6, new String[] {"1", "2", "3", "4", "5"});
        testSettingSrcAttribute(BrowserVersion.INTERNET_EXPLORER_7, new String[] {"1", "2", "3", "4", "5"});
    }

    /**
     * Verifies that setting a script's <tt>src</tt> attribute behaves correctly.
     * @param version the browser version being tested
     * @param expected the expected alerts
     * @throws Exception if an error occurs
     */
    private void testSettingSrcAttribute(final BrowserVersion version, final String[] expected) throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script id='a'></script>\n"
            + "    <script id='b'>alert('1');</script>\n"
            + "    <script id='c' src='script2.js'></script>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        document.getElementById('a').src = 'script3.js';\n"
            + "        document.getElementById('b').src = 'script4.js';\n"
            + "        document.getElementById('c').src = 'script5.js';\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "      test\n"
            + "  </body>\n"
            + "</html>";

        final List<String> actual = new ArrayList<String>();

        final WebClient client = new WebClient(version);
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        webConnection.setResponse(new URL("http://abc/script2.js"), "alert(2);");
        webConnection.setResponse(new URL("http://abc/script3.js"), "alert(3);");
        webConnection.setResponse(new URL("http://abc/script4.js"), "alert(4);");
        webConnection.setResponse(new URL("http://abc/script5.js"), "alert(5);");
        client.setWebConnection(webConnection);

        client.getPage("http://abc/");
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAsXml() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<script id='script1'>\n"
            + "    alert('hello');\n"
            + "</script></body></html>";

        final String[] expectedAlerts = {"hello"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);

        // asXml() should be reusable
        final String xml = page.asXml();
        collectedAlerts.clear();
        loadPage(xml, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that we're lenient about empty "src" attributes.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEmptySrc() throws Exception {
        final String html1 = "<html><head><script src=''></script></head><body>abc</body></html>";
        final String html2 = "<html><head><script src='  '></script></head><body>abc</body></html>";

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html1);
        webConnection.setResponse(URL_SECOND, html2);
        client.setWebConnection(webConnection);

        client.getPage(URL_FIRST);
        assertEquals(1, webConnection.getRequestCount());

        client.getPage(URL_SECOND);
        assertEquals(2, webConnection.getRequestCount());
    }

    /**
     * Verifies that we're lenient about whitespace before and after URLs in the "src" attribute.
     * @throws Exception if an error occurs
     */
    @Test
    public void testWhitespaceInSrc() throws Exception {
        final String html = "<html><head><script src=' " + URL_SECOND + " '></script></head><body>abc</body></html>";
        final String js = "alert('ok')";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, js);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        final String[] expectedAlerts = {"ok"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that cloned script nodes do not reload or re-execute their content (bug 1954869).
     * @throws Exception if an error occurs
     */
    @Test
    public void testScriptCloneDoesNotReloadScript() throws Exception {
        final String html = "<html><body><script src='" + URL_SECOND + "'></script></body></html>";
        final String js = "alert('loaded')";

        final WebClient client = new WebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, js, "text/javascript");
        client.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(2, conn.getRequestCount());

        page.cloneNode(true);
        assertEquals(2, conn.getRequestCount());

        final String[] expected = {"loaded"};
        assertEquals(expected, actual);
    }

    /**
     * Tests the 'Referer' HTTP header.
     * @throws Exception on test failure
     */
    @Test
    public void testRefererHeader() throws Exception {
        final String firstContent
            = "<html><head><title>Page A</title></head>\n"
            + "<body><script src='" + URL_SECOND + "' id='link'/></body>\n"
            + "</html>";

        final String secondContent = "alert('test')";

        final WebClient client = new WebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final Map<String, String> lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get("Referer"));
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void insertBefore() throws Exception {
        final String html
            = "<html><head><title>Page A</title>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var script = document.createElement('script');\n"
            + "    script.text = \"foo = 'myValue';\";\n"
            + "    document.body.insertBefore(script, document.body.firstChild);\n"
            + "    alert(foo);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        final String[] expectedAlerts = {"myValue"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}