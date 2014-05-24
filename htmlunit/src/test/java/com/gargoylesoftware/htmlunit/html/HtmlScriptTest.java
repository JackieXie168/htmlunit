/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlScript}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlScriptTest extends SimpleWebTestCase {

    /**
     * Verifies that a failing HTTP status code for a JavaScript file request (like a 404 response)
     * results in a {@link FailingHttpStatusCodeException}, depending on how the client has been
     * configured.
     *
     * @see com.gargoylesoftware.htmlunit.WebClientOptions#isThrowExceptionOnFailingStatusCode()
     * @throws Exception if an error occurs
     */
    @Test
    public void testBadExternalScriptReference() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
                + "<script src='inexistent.js'></script>\n"
                + "</head><body></body></html>";

        final WebClient client = getWebClient();

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

        client.getOptions().setThrowExceptionOnFailingStatusCode(false);

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
    public void asText() throws Exception {
        final String html = "<html><body><script id='s'>var foo = 132;</script></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlScript script = page.getHtmlElementById("s");
        assertEquals("", script.asText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("hello")
    public void asXml() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<script id='script1'>\n"
            + "    alert('hello');\n"
            + "</script></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);

        // asXml() should be reusable
        final String xml = page.asXml();
        loadPageWithAlerts(xml);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void asXml_scriptNestedInCData() throws Exception {
        final String script = "//<![CDATA[\n"
            + "var foo = 132;\n"
            + "//]]>";
        final String html = "<html><body><script id='s'>" + script + "</script></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlScript scriptElement = page.getHtmlElementById("s");
        assertEquals("<script id=\"s\">\r\n" + script + "\r\n</script>\r\n",
                scriptElement.asXml());
    }

    /**
     * Tests the 'Referer' HTTP header.
     * @throws Exception on test failure
     */
    @Test
    public void refererHeader() throws Exception {
        final String firstContent
            = "<html><head><title>Page A</title></head>\n"
            + "<body><script src='" + URL_SECOND + "' id='link'/></body>\n"
            + "</html>";

        final String secondContent = "alert('test')";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);

        final Map<String, String> lastAdditionalHeaders = conn.getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get("Referer"));
    }

    /**
     * Verifies that cloned script nodes do not reload or re-execute their content (bug 1954869).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void testScriptCloneDoesNotReloadScript() throws Exception {
        final String html = "<html><body><script src='" + URL_SECOND + "'></script></body></html>";
        final String js = "alert('loaded')";

        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, js, JAVASCRIPT_MIME_TYPE);
        client.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(2, conn.getRequestCount());

        page.cloneNode(true);
        assertEquals(2, conn.getRequestCount());

        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * Verifies that we're lenient about whitespace before and after URLs in the "src" attribute.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("ok")
    public void testWhitespaceInSrc() throws Exception {
        final String html = "<html><head><script src=' " + URL_SECOND + " '></script></head><body>abc</body></html>";
        final String js = "alert('ok')";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, js);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Verifies that we're lenient about empty "src" attributes.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEmptySrc() throws Exception {
        final String html1 = "<html><head><script src=''></script></head><body>abc</body></html>";
        final String html2 = "<html><head><script src='  '></script></head><body>abc</body></html>";

        final WebClient client = getWebClient();
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
     * Verifies that 204 (No Content) responses for script resources are handled gracefully.
     * @throws Exception on test failure
     * @see <a href="https://sourceforge.net/tracker/?func=detail&atid=448266&aid=2815903&group_id=47038">2815903</a>
     */
    @Test
    public void noContent() throws Exception {
        final String html = "<html><body><script src='" + URL_SECOND + "'/></body></html>";
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        final ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
        conn.setResponse(URL_SECOND, (String) null, HttpStatus.SC_NO_CONTENT, "No Content", JAVASCRIPT_MIME_TYPE,
                headers);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = "f")
    public void addEventListener_error_clientThrows() throws Exception {
        addEventListener_error(true);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = "f")
    public void addEventListener_error_clientDoesNotThrow() throws Exception {
        addEventListener_error(false);
    }

    private void addEventListener_error(final boolean throwOnFailingStatusCode) throws Exception {
        final URL fourOhFour = new URL(URL_FIRST, "/404");
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s1 = document.createElement('script');\n"
            + "    s1.text = 'var foo';\n"
            + "    if(s1.addEventListener) s1.addEventListener('error', function(){alert('a')}, false);\n"
            + "    document.body.insertBefore(s1, document.body.firstChild);\n"
            + "    \n"
            + "    var s2 = document.createElement('script');\n"
            + "    s2.text = 'varrrr foo';\n"
            + "    if(s2.addEventListener) s2.addEventListener('error', function(){alert('b')}, false);\n"
            + "    document.body.insertBefore(s2, document.body.firstChild);\n"
            + "    \n"
            + "    var s3 = document.createElement('script');\n"
            + "    s3.src = '//:';\n"
            + "    if(s3.addEventListener) s3.addEventListener('error', function(){alert('c')}, false);\n"
            + "    document.body.insertBefore(s3, document.body.firstChild);\n"
            + "    \n"
            + "    var s4 = document.createElement('script');\n"
            + "    s4.src = '" + URL_SECOND + "';\n"
            + "    if(s4.addEventListener) s4.addEventListener('error', function(){alert('d')}, false);\n"
            + "    document.body.insertBefore(s4, document.body.firstChild);\n"
            + "    \n"
            + "    var s5 = document.createElement('script');\n"
            + "    s5.src = '" + URL_THIRD + "';\n"
            + "    if(s5.addEventListener) s5.addEventListener('error', function(){alert('e')}, false);\n"
            + "    document.body.insertBefore(s5, document.body.firstChild);\n"
            + "    \n"
            + "    var s6 = document.createElement('script');\n"
            + "    s6.src = '" + fourOhFour + "';\n"
            + "    if(s6.addEventListener) s6.addEventListener('error', function(){alert('f')}, false);\n"
            + "    document.body.insertBefore(s6, document.body.firstChild);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        final WebClient client = getWebClient();
        client.getOptions().setThrowExceptionOnFailingStatusCode(throwOnFailingStatusCode);
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, "var foo;", JAVASCRIPT_MIME_TYPE);
        conn.setResponse(URL_THIRD, "varrrr foo;", JAVASCRIPT_MIME_TYPE);
        conn.setResponse(fourOhFour, "", 404, "Missing", JAVASCRIPT_MIME_TYPE, new ArrayList<NameValuePair>());
        client.setWebConnection(conn);
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html = "<html><head><title>Page A</title></head><body><script>var x = 1;</script></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlScript script = page.getFirstByXPath("//script");
        assertFalse(script.isDisplayed());
    }
}
