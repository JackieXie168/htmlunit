/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.MalformedURLException;
import java.net.URL;
import junit.textui.TestRunner;

/**
 *  This class runs live tests against popular webservers just to make sure
 *  nothing obvious has broken. Don't run this very often lest we piss off the
 *  web masters.
 *
 * @version    $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class SanityCheck extends WebTestCase {
    private static final BrowserVersion browserVersion_ = BrowserVersion.MOZILLA_1_0;

    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public SanityCheck( final String name ) {
        super( name );
    }


    /**
     * Main entry point for testing.
     * @param args the arguments.
     */
    public static void main( final String args[] ) {
        com.gargoylesoftware.htmlunit.test.MainTestSuite.enableAllLogging();
        TestRunner.run( SanityCheck.class );
        System.exit( 0 );
    }


    /**
     * Test against a live server: Yahoo mail
     * @throws Exception If something goes wrong.
     */
    public void testYahooMail() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        assertInstanceOf(webClient.getPage( new URL( "http://mail.yahoo.com/" ) ), HtmlPage.class);
    }


    /**
     * Test against a live server: Yahoo
     * @throws Exception If something goes wrong.
     */
    public void testYahoo() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        assertInstanceOf( webClient.getPage( new URL( "http://yahoo.com/" ) ), HtmlPage.class );
    }


    /**
     * Test against a live server: IBM
     * @throws Exception If something goes wrong.
     */
    public void testIBM() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        webClient.setRedirectEnabled( true );
        final HtmlPage page = (HtmlPage)webClient.getPage( new URL( "http://www.ibm.com/" ) );
        assertEquals( "http://www.ibm.com/us/", page.getWebResponse().getUrl().toExternalForm() );
    }


    /**
     * Test against a live server: IBM Alphaworks
     * @throws Exception If something goes wrong.
     */
    public void testAlphaWorks() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        assertInstanceOf(webClient.getPage(new URL( "http://www.alphaworks.ibm.com" ) ), HtmlPage.class);
    }


    /**
     * Test against a live server: CNN
     * @throws Exception If something goes wrong.
     */
    public void testCNN() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        assertInstanceOf( webClient.getPage( new URL( "http://www.cnn.com" ) ), HtmlPage.class);
    }


    /**
     * Test against a live server: Toyota Canada
     * @throws Exception If something goes wrong.
     */
    public void testToyotaCanada() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        assertInstanceOf(webClient.getPage( new URL( "http://www.toyota.ca" ) ), HtmlPage.class);
    }


    /**
     * Test against a live server: HtmlUnit page on sourceforge using https
     * @throws Exception If something goes wrong.
     */
    public void testSourceForge_secure() throws Exception {
        try {
            final WebClient webClient = new WebClient(browserVersion_);
            webClient.setPrintContentOnFailingStatusCode(true);
            assertInstanceOf( webClient.getPage( new URL( "https://sourceforge.net/projects/htmlunit/" ) ), HtmlPage.class );
        }
        catch( final MalformedURLException e ) {
            System.out.println("Skipping https test: "+getName());
        }
    }


    /**
     * Test against a live server: Yahoo secure login
     * @throws Exception If something goes wrong.
     */
    public void testYahooLogin_secure() throws Exception {
        try {
            final WebClient webClient = new WebClient(browserVersion_);
            final HtmlPage page = (HtmlPage)webClient.getPage( new URL( "https://login.yahoo.com/" ) );
            final HtmlForm form = page.getFormByName("login_form");
            assertNotNull(form);
        }
        catch( final MalformedURLException e ) {
            System.out.println("Skipping https test: "+getName());
        }
    }


    /**
     * Test against a live server: Amazon Canada
     * @throws Exception If something goes wrong.
     */
    public void testAmazonCanada() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        assertInstanceOf( webClient.getPage( new URL( "http://www.amazon.ca/" ) ), HtmlPage.class );
    }


    /**
     * Test against a live server: CNN After hours
     * @throws Exception If something goes wrong.
     */
    public void testCnnAfterHours() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        assertInstanceOf( webClient.getPage( new URL( "http://money.cnn.com/markets/afterhours/" ) ), HtmlPage.class );
    }


    /**
     * Print out the name of the test that is running.
     */
    public void setUp() {
        System.out.println();
        System.out.println( "=====================================" );
        System.out.println( "==  Starting test: " + getName() );
        System.out.println( "=====================================" );
    }
}

