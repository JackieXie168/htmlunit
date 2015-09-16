/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for XMLHttpRequest.
 * 
 * @author Daniel Gredler
 * @version $Revision$
 */
public class XMLHttpRequestTest extends WebTestCase {

    private static final String UNINITIALIZED = String.valueOf( XMLHttpRequest.STATE_UNINITIALIZED );
    private static final String LOADING = String.valueOf( XMLHttpRequest.STATE_LOADING );
    private static final String LOADED = String.valueOf( XMLHttpRequest.STATE_LOADED );
    private static final String INTERACTIVE = String.valueOf( XMLHttpRequest.STATE_INTERACTIVE );
    private static final String COMPLETED = String.valueOf( XMLHttpRequest.STATE_COMPLETED );

    /**
     * Creates a new test instance.
     * @param name The name of the new test instance.
     */
    public XMLHttpRequestTest( final String name ) {
        super( name );
    }

    /**
     * Tests synchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception If the test fails.
     */
    public void testSyncUseWithMozillaStyleCreation() throws Exception {

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testSync() {\n"
            + "        request = new XMLHttpRequest();\n"
            + "        alert(request.readyState);\n"
            + "        request.open('GET', '" + URL_SECOND.toExternalForm() + "', false);\n"
            + "        alert(request.readyState);\n"
            + "        request.send();\n"
            + "        alert(request.readyState);\n"
            + "        alert(request.responseText);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testSync()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "  <content>blah</content>\n"
            + "  <content>blah2</content>\n"
            + "</xml>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse( URL_FIRST, html );
        webConnection.setResponse( URL_SECOND, xml, 200, "OK", "text/xml", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );
        client.getPage( URL_FIRST );

        final List alerts = Arrays.asList( new String[] { UNINITIALIZED, LOADING, COMPLETED, xml } );
        assertEquals( alerts, collectedAlerts );
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception If the test fails.
     */
    public void testAsyncUseWithMozillaStyleCreation() throws Exception {

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        request = new XMLHttpRequest();\n"
            + "        request.onreadystatechange = onReadyStateChange;\n"
            + "        alert(request.readyState);\n"
            + "        request.open('GET', '" + URL_SECOND.toExternalForm() + "', true);\n"
            + "        request.send();\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        alert(request.readyState);\n"
            + "        alert(request.responseText);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testAsync()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml2>\n"
            + "  <content2>sdgxsdgx</content2>\n"
            + "  <content2>sdgxsdgx2</content2>\n"
            + "</xml2>";

        final WebClient client = new WebClient();
        final List collectedAlerts = Collections.synchronizedList( new ArrayList() );
        client.setAlertHandler( new CollectingAlertHandler( collectedAlerts ) );
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse( URL_FIRST, html );
        webConnection.setResponse( URL_SECOND, xml, 200, "OK", "text/xml", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );
        client.getPage( URL_FIRST );

        final String[] s = new String[] { UNINITIALIZED, LOADING, "", LOADED, "", INTERACTIVE, xml, COMPLETED, xml };
        final List alerts = Collections.synchronizedList( Arrays.asList( s ) );

        final int waitTime = 50;
        final int maxTime = 1000;
        for( int time = 0; time < maxTime; time += waitTime ) {
            if( alerts.size() == collectedAlerts.size() ) {
                assertEquals( alerts, collectedAlerts );
                return;
            }
            Thread.sleep( waitTime );
        }
        fail( "Unable to collect expected alerts within " + maxTime + "ms; collected alerts: " + collectedAlerts );
    }

}