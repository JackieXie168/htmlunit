/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.base.testing.BaseTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import junit.framework.AssertionFailedError;

/**
 * Common superclass for HtmlUnit tests
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class WebTestCase extends BaseTestCase {
    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public WebTestCase( final String name ) {
        super( name );
    }


    /**
     * Load a page with the specified html.
     * @param html The html to use.
     * @return The new page.
     * @throws Exception if something goes wrong.
     */
    protected final HtmlPage loadPage( final String html ) throws Exception {
        return loadPage(html, null);
    }


    /**
     * Load a page with the specified html and collect alerts into the list.
     * @param html The HTML to use.
     * @param collectedAlerts The list to hold the alerts.
     * @return The new page.
     * @throws Exception If something goes wrong.
     */
    protected final HtmlPage loadPage( final String html, final List collectedAlerts )
        throws Exception {

        final WebClient client = new WebClient();
        if( collectedAlerts != null ) {
            client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );
        }

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( html );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        return page;
    }


    /**
     * Assert that the specified object is null.
     * @param object The object to check.
     */
    public static void assertNull( final Object object ) {
        if( object != null ) {
            throw new AssertionFailedError("Expected null but found ["+object+"]");
        }
    }
}

