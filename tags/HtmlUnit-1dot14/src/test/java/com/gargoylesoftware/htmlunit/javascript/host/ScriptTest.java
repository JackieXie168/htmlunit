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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link Script}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class ScriptTest extends WebTestCase {

    /**
     * Creates an instance.
     * @param name The name of the test.
     */
    public ScriptTest(final String name) {
        super(name);
    }

    /**
     * Verifies that the <tt>onreadystatechange</tt> handler is invoked correctly.
     * @throws Exception If an error occurs.
     */
    public void testOnReadyStateChangeHandler() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script id='a'>\n"
            + "      var script = document.createElement('script');\n"
            + "      script.id = 'b';\n"
            + "      script.type = 'text/javascript';\n"
            + "      script.onreadystatechange = null;"
            + "      script.onreadystatechange = function() {\n"
            + "        alert(script.id + '=' + script.readyState);\n"
            + "      }\n"
            + "      alert('1');\n"
            + "      script.src = '" + URL_SECOND + "';\n"
            + "      alert('2');\n"
            + "      document.getElementsByTagName('head')[0].appendChild(script);\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body>abc</body>\n"
            + "</html>";

        final String js = "alert('3')";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, js, "text/javascript");
        client.setWebConnection(webConnection);

        client.getPage(URL_FIRST);
        final String[] expectedAlerts = {"1", "2", "b=complete", "3" };
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for bug
     * https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1782719&group_id=47038
     * @throws Exception if the test fails
     */
    public void testSrcWithJavaScriptProtocol() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        testSrcWithJavaScriptProtocol(BrowserVersion.INTERNET_EXPLORER_6_0, new String[] {"1"});
        testSrcWithJavaScriptProtocol(BrowserVersion.INTERNET_EXPLORER_7_0, new String[0]);
        testSrcWithJavaScriptProtocol(BrowserVersion.FIREFOX_2, new String[] {"1"});
    }

    private void testSrcWithJavaScriptProtocol(final BrowserVersion browserVersion,
            final String[] expectedAlerts) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var script=document.createElement('script');\n"
            + "    script.src=\"javascript:'alert(1)'\";\n"
            + "    document.getElementsByTagName('head')[0].appendChild(script);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        
        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testScriptForEvent() throws Exception {
        // IE accepts it with () or without
        testScriptForEvent("onload");
        testScriptForEvent("onload()");
    }

    private void testScriptForEvent(final String eventName) throws Exception {
        final String content
            = "<html><head><title>foo</title>\n"
            + "<script FOR='window' EVENT='" + eventName + "' LANGUAGE='javascript'>\n"
            + " document.form1.txt.value='hello';\n"
            + " alert(document.form1.txt.value);\n"
            + "</script></head><body>\n"
            + "<form name='form1'><input type=text name='txt'></form></body></html>";
        final List collectedAlerts = new ArrayList();

        final String[] expectedAlerts = {"hello"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}