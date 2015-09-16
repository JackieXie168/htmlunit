/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.PageCreator;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowAdapter;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  Tests for WebClient
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class WebClientTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public WebClientTest( final String name ) {
        super( name );
    }


    /**
     * @exception  Exception
     */
    public void testCredentialProvider_NoCredentials()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "No access</body></html>";
        final WebClient client = new WebClient();
        client.setPrintContentOnFailingStatusCode( false );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setDefaultResponse(
            htmlContent, 401, "Credentials missing or just plain wrong", "text/plain" );
        client.setWebConnection( webConnection );

        try {
            client.getPage(
                    new URL( "http://www.gargoylesoftware.com" ),
                    SubmitMethod.POST, Collections.EMPTY_LIST );
            fail( "Expected FailingHttpStatusCodeException" );
        }
        catch( final FailingHttpStatusCodeException e ) {
            assertEquals( 401, e.getStatusCode() );
        }
    }


    public void testAssertionFailed() {
        final WebClient client = new WebClient();

        try {
            client.assertionFailed( "foobar" );
            fail( "Expected AssertionFailedError" );
        }
        catch( junit.framework.AssertionFailedError e ) {
            assertEquals( "foobar", e.getMessage() );
        }
    }


    public void testHtmlWindowEvents_changed() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>"
                 + "</body></html>";
        final WebClient client = new WebClient();
        final List collectedChangeEvents = new ArrayList();
        final List collectedOpenEvents = new ArrayList();
        client.addWebWindowListener( new WebWindowAdapter() {
            public void webWindowOpened( final WebWindowEvent event ) {
                collectedOpenEvents.add(event);
            }
            public void webWindowContentChanged( final WebWindowEvent event ) {
                collectedChangeEvents.add(event);
            }
        } );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlAnchor anchor = ( HtmlAnchor )page.getHtmlElementById( "a2" );

        assertEquals( "Open event after first page",
            Collections.EMPTY_LIST,
            collectedOpenEvents );
        assertEquals( "Change event after first page",
            Collections.singletonList(new WebWindowEvent(client.getCurrentWindow(), null, page)),
            collectedChangeEvents );

        collectedOpenEvents.clear();
        collectedChangeEvents.clear();

        final HtmlPage secondPage = ( HtmlPage )anchor.click();

        assertEquals( "Open event after second page",
            Collections.EMPTY_LIST,
            collectedOpenEvents );
        assertEquals( "Change event after second page",
            Collections.singletonList(
                new WebWindowEvent(client.getCurrentWindow(), page, secondPage)),
            collectedChangeEvents );
    }


    public void testHtmlWindowEvents_opened() throws Exception {
        final String page1Content
                 = "<html><head><title>foo</title>"
                 + "<script>window.open('http://page2', 'myNewWindow')</script>"
                 + "</head><body>"
                 + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>"
                 + "</body></html>";
        final String page2Content
                 = "<html><head><title>foo</title></head><body></body></html>";
        final WebClient client = new WebClient();
        final List collectedChangeEvents = new ArrayList();
        final List collectedOpenEvents = new ArrayList();
        client.addWebWindowListener( new WebWindowAdapter() {
            public void webWindowOpened( final WebWindowEvent event ) {
                collectedOpenEvents.add(event);
            }
            public void webWindowContentChanged( final WebWindowEvent event ) {
                collectedChangeEvents.add(event);
            }
        } );

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://page1"), page1Content, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://page2"), page2Content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://page1" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("foo", page.getTitleText());

        assertEquals( 1, collectedOpenEvents.size() );
        final WebWindowEvent openEvent = (WebWindowEvent)collectedOpenEvents.get(0);
        final WebWindow newWindow = (WebWindow)openEvent.getSource();
        assertEquals("myNewWindow", newWindow.getName());
    }


    public void testRedirection301_MovedPermanently_GetMethod() throws Exception {
        final int statusCode = 301;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    /**
     * From the http spec:  If the 301 status code is received in response
     * to a request other than GET or HEAD, the user agent MUST NOT automatically
     * redirect the request unless it can be confirmed by the user, since this
     * might change the conditions under which the request was issued.
     */
    public void testRedirection301_MovedPermanently_PostMethod() throws Exception {
        final int statusCode = 301;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = null;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    /**
     * From the http spec:  Note: RFC 1945 and RFC 2068 specify that the client
     * is not allowed to change the method on the redirected request.  However,
     * most existing user agent implementations treat 302 as if it were a 303
     * response, performing a GET on the Location field-value regardless
     * of the original request method. The status codes 303 and 307 have
     * been added for servers that wish to make unambiguously clear which
     * kind of reaction is expected of the client.
     */
    public void testRedirection302_MovedTemporarily_PostMethod() throws Exception {
        final int statusCode = 302;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    public void testRedirection302_MovedTemporarily_GetMethod() throws Exception {
        final int statusCode = 302;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    // Should be same as 302
    public void testRedirection303_SeeOther_GetMethod() throws Exception {
        final int statusCode = 303;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    // Should be same as 302
    public void testRedirection303_SeeOther_PostMethod() throws Exception {
        final int statusCode = 303;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    public void testRedirection307_TemporaryRedirect_GetMethod() throws Exception {
        final int statusCode = 307;
        final SubmitMethod initialRequestMethod = SubmitMethod.GET;
        final SubmitMethod expectedRedirectedRequestMethod = SubmitMethod.GET;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    public void testRedirection307_TemporaryRedirect_PostMethod() throws Exception {
        final int statusCode = 307;
        final SubmitMethod initialRequestMethod = SubmitMethod.POST;
        final SubmitMethod expectedRedirectedRequestMethod = null;

        doTestRedirection( statusCode, initialRequestMethod, expectedRedirectedRequestMethod );
    }


    /**
     *
     * @param statusCode The code to return from the initial request
     * @param The submit method of the second (redirected) request.  If a redirect
     * is not expected to happen then this must be null
     */
    private void doTestRedirection(
            final int statusCode,
            final SubmitMethod initialRequestMethod,
            final SubmitMethod expectedRedirectedRequestMethod )
        throws
            Exception{

        final String firstContent = "<html><head><title>First</title></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setPrintContentOnFailingStatusCode(false);

        final List headers = Collections.singletonList(
            new KeyValuePair("Location", "http://second") );
        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        webConnection.setResponse(
            new URL("http://first"), firstContent, statusCode,
            "Some error", "text/html", headers );
        webConnection.setResponse(
            new URL("http://second"), secondContent, 200,
            "OK", "text/html", Collections.EMPTY_LIST );

        webClient.setWebConnection( webConnection );

        final URL url = new URL("http://first");

        HtmlPage page;
        WebResponse webResponse;

        //
        // Second time redirection is turned on (default setting)
        //
        page = (HtmlPage)webClient.getPage(url, initialRequestMethod, Collections.EMPTY_LIST);
        webResponse = page.getWebResponse();
        if( expectedRedirectedRequestMethod == null ) {
            // No redirect should have happened
            assertEquals( statusCode, webResponse.getStatusCode() );
            assertEquals( initialRequestMethod, webConnection.getLastMethod() );
        }
        else {
            // A redirect should have happened
            assertEquals( 200, webResponse.getStatusCode() );
            assertEquals( "Second", page.getTitleText() );
            assertEquals( expectedRedirectedRequestMethod, webConnection.getLastMethod() );
        }

        //
        // Second time redirection is turned off
        //
        webClient.setRedirectEnabled(false);
        page = (HtmlPage)webClient.getPage(url, initialRequestMethod, Collections.EMPTY_LIST);
        webResponse = page.getWebResponse();
        assertEquals( statusCode, webResponse.getStatusCode() );
        assertEquals( initialRequestMethod, webConnection.getLastMethod() );

    }


    public void testSetPageCreator_null() {
        final WebClient webClient = new WebClient();
        try {
            webClient.setPageCreator(null);
            fail("Expected NullPointerException");
        }
        catch( final NullPointerException e ) {
            // expected path
        }
    }


    public void testSetPageCreator() throws Exception {
        final String page1Content
                 = "<html><head><title>foo</title>"
                 + "</head><body>"
                 + "<a href='http://www.foo2.com' id='a2'>link to foo2</a>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setResponse(
            new URL("http://page1"), page1Content, 200, "OK", "text/html", Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );
        class CollectingPageCreator implements PageCreator {
            private final List list;
            public CollectingPageCreator( final List list ) {
                this.list = list;
            }
            public Page createPage(
                final WebClient webClient,
                final WebResponse webResponse,
                final WebWindow webWindow )
                throws IOException {

                final Page page = new TextPage(webResponse, webWindow);
                list.add(page);
                return page;
            }
        };
        final List collectedPageCreationItems = new ArrayList();
        client.setPageCreator( new CollectingPageCreator(collectedPageCreationItems) );

        final Page page = client.getPage(
                new URL( "http://page1" ), SubmitMethod.POST, Collections.EMPTY_LIST );
        assertTrue( "instanceof TextPage", page instanceof TextPage );

        final List expectedPageCreationItems = Arrays.asList( new Object[] {
            page
        } );

        assertEquals( expectedPageCreationItems, collectedPageCreationItems );
    }


    public void testLoadPage_PostWithParameters() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
            new URL( "http://first?a=b" ), SubmitMethod.POST, Collections.EMPTY_LIST );

        assertEquals(
            "http://first?a=b",
            page.getWebResponse().getUrl().toExternalForm() );
    }


    public void testRedirectViaJavaScriptDuringInitialPageLoad() throws Exception {
        final String firstContent = "<html><head><title>First</title><script>"
            + "location.href='http://second'"
            + "</script></head><body></body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://second"), secondContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        webClient.setWebConnection( webConnection );

        final URL url = new URL("http://first");

        final HtmlPage page = (HtmlPage)webClient.getPage(
            url, SubmitMethod.GET, Collections.EMPTY_LIST);
        assertEquals("Second", page.getTitleText());
    }


    public void testKeyboard_NoTabbableElements() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = getPageForKeyboardTest(webClient, new String[0]);
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        assertNull( "original", webClient.getElementWithFocus() );
        assertNull( "next", page.tabToNextElement() );
        assertNull( "previous", page.tabToPreviousElement() );
        assertNull( "accesskey", page.pressAccessKey('a') );

        final List expectedAlerts = Collections.EMPTY_LIST;
        assertEquals(expectedAlerts, collectedAlerts);
    }


    public void testKeyboard_OneTabbableElement() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ null });
        final HtmlElement element = page.getHtmlElementById("submit0");

        assertNull( "original", webClient.getElementWithFocus() );
        assertNull( "accesskey", page.pressAccessKey('x') );

        assertEquals( "next", element, page.tabToNextElement() );
        assertEquals( "nextAgain", element, page.tabToNextElement() );

        webClient.moveFocusToElement(null);
        assertNull( "original", webClient.getElementWithFocus() );

        assertEquals( "previous", element, page.tabToPreviousElement() );
        assertEquals( "previousAgain", element, page.tabToPreviousElement() );

        assertEquals( "accesskey", element, page.pressAccessKey('z') );

        final List expectedAlerts = Arrays.asList( new String[]{"focus-0", "blur-0", "focus-0"} );
        assertEquals(expectedAlerts, collectedAlerts);
    }


    public void testAccessKeys() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ "1", "2", "3" });

        assertEquals( "submit0", page.pressAccessKey('a').getAttributeValue("name") );
        assertEquals( "submit2", page.pressAccessKey('c').getAttributeValue("name") );
        assertEquals( "submit1", page.pressAccessKey('b').getAttributeValue("name") );

        final List expectedAlerts = Arrays.asList( new String[]{
            "focus-0", "blur-0", "focus-2", "blur-2", "focus-1"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testTabNext() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ "1", "2", "3" });

        assertEquals( "submit0", page.tabToNextElement().getAttributeValue("name") );
        assertEquals( "submit1", page.tabToNextElement().getAttributeValue("name") );
        assertEquals( "submit2", page.tabToNextElement().getAttributeValue("name") );

        final List expectedAlerts = Arrays.asList( new String[]{
            "focus-0", "blur-0", "focus-1", "blur-1", "focus-2"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testTabPrevious() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ "1", "2", "3" });

        assertEquals( "submit2", page.tabToPreviousElement().getAttributeValue("name") );
        assertEquals( "submit1", page.tabToPreviousElement().getAttributeValue("name") );
        assertEquals( "submit0", page.tabToPreviousElement().getAttributeValue("name") );

        final List expectedAlerts = Arrays.asList( new String[]{
            "focus-2", "blur-2", "focus-1", "blur-1", "focus-0"
        } );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testMoveFocusToElement_NotTabbableElement() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ "1", "2", "3" });
        final HtmlElement element = page.getHtmlElementById("div1");

        assertFalse( webClient.moveFocusToElement(element) );
        assertEquals( Collections.EMPTY_LIST, collectedAlerts );

        final List expectedAlerts = Arrays.asList( new String[]{} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testPressAccessKey_Button() throws Exception {
        final WebClient webClient = new WebClient();
        final List collectedAlerts = new ArrayList();
        webClient.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = getPageForKeyboardTest(webClient, new String[]{ "1", "2", "3" });
        final HtmlElement button = (HtmlElement)page.getHtmlElementById("button1");

        final List expectedAlerts = Collections.singletonList("buttonPushed");
        collectedAlerts.clear();

        button.getElement().removeAttribute("disabled");
        page.pressAccessKey('1');

        assertEquals( expectedAlerts, collectedAlerts );
    }


    private HtmlPage getPageForKeyboardTest(
        final WebClient webClient, final String[] tabIndexValues ) throws Exception {

        final StringBuffer buffer = new StringBuffer();
        buffer.append(
            "<html><head><title>First</title></head><body><form name='form1' method='post' onsubmit='return false;'>");

        for( int i=0; i<tabIndexValues.length; i++ ) {
            buffer.append( "<input type='submit' name='submit");
            buffer.append(i);
            buffer.append("' id='submit");
            buffer.append(i);
            buffer.append("'");
            if( tabIndexValues[i] != null ) {
                buffer.append(" tabindex='");
                buffer.append(tabIndexValues[i]);
                buffer.append("'");
            }
            buffer.append(" onblur='alert(\"blur-"+i+"\")'");
            buffer.append(" onfocus='alert(\"focus-"+i+"\")'");
            buffer.append(" accesskey='"+(char)('a'+i)+"'");
            buffer.append(">");
        }
        buffer.append("<div id='div1'>foo</div>"); // something that isn't tabbable

        // Elements that are tabbable but are disabled
        buffer.append("<button name='button1' id='button1' disabled onclick='alert(\"buttonPushed\")' accesskey='1'>foo</button>");

        buffer.append("</form></body></html>");

        final FakeWebConnection webConnection = new FakeWebConnection( webClient );
        webConnection.setResponse(
            new URL("http://first/"), buffer.toString(), 200, "OK", "text/html", Collections.EMPTY_LIST );
        webClient.setWebConnection( webConnection );

        final URL url = new URL("http://first/");
        return (HtmlPage)webClient.getPage(url, SubmitMethod.GET, Collections.EMPTY_LIST);
    }
}
