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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DefaultCredentialsProvider2Test extends WebDriverTestCase {

    private static String XHRInstantiation_ = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

    /**
     * {@inheritDoc}
     */
    protected boolean isBasicAuthentication() {
        return true;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationWrongUserName() throws Exception {
        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (!(getWebDriver() instanceof HtmlUnitDriver)) {
            return;
        }

        getMockWebConnection().setResponse(URL_SECOND, "Hello World");

        // wrong user name
        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("joe", "jetty");

        final WebDriver driver = loadPage2("Hi There");
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationWrongPassword() throws Exception {
        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (!(getWebDriver() instanceof HtmlUnitDriver)) {
            return;
        }

        getMockWebConnection().setResponse(URL_SECOND, "Hello World");

        // wrong user name
        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "secret");

        final WebDriver driver = loadPage2("Hi There");
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthenticationTwice() throws Exception {
        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (!(getWebDriver() instanceof HtmlUnitDriver)) {
            return;
        }

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");

        getMockWebConnection().setResponse(URL_SECOND, "Hello World");
        final WebDriver driver = loadPage2("Hi There");
        assertTrue(driver.getPageSource().contains("Hi There"));
        driver.get(URL_SECOND.toExternalForm());
        assertTrue(driver.getPageSource().contains("Hello World"));
    }

    /**
     * Tests that on calling the website twice, only the first time unauthorized response is returned.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void basicAuthentication_singleAuthenticaiton() throws Exception {
        if (getWebDriver() instanceof HtmlUnitDriver) {
            final Logger logger = Logger.getLogger("org.apache.http.headers");
            final Level oldLevel = logger.getLevel();
            logger.setLevel(Level.DEBUG);

            final InMemoryAppender appender = new InMemoryAppender();
            logger.addAppender(appender);
            try {
                ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");

                final WebDriver driver = loadPage2("Hi There");
                driver.get(URL_FIRST.toExternalForm());
                int unauthorizedCount = 0;
                for (final String message : appender.getMessages()) {
                    if (message.contains("HTTP/1.1 401")) {
                        unauthorizedCount++;
                    }
                }
                assertEquals(1, unauthorizedCount);
            }
            finally {
                logger.removeAppender(appender);
                logger.setLevel(oldLevel);
            }
        }
    }

    /**
     * An in memory appender, used to save all logged messages in memory.
     */
    public static class InMemoryAppender extends AppenderSkeleton {

        private List<String> messages_ = new ArrayList<String>();

        /**
         * {@inheritDoc}
         */
        @Override
        protected void append(final LoggingEvent event) {
            messages_.add(event.getMessage().toString());
        }

        /**
         * Returns the saved messages.
         * @return the saved messages
         */
        public List<String> getMessages() {
            return messages_;
        }

        /**
         * {@inheritDoc}
         */
        public void close() {
        }

        /**
         * {@inheritDoc}
         */
        public boolean requiresLayout() {
            return false;
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "SecRet")
    public void basicAuthenticationUserFromUrl() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (getWebDriver() instanceof HtmlUnitDriver) {
            // no credentials
            final WebDriver driver = loadPage2(html, new URL("http://localhost:" + PORT + "/"));
            assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
        }

        // now a url with credentials
        URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
        loadPageWithAlerts2(url);

        // next step without credentials but the credentials are still known
        url = new URL("http://localhost:" + PORT + "/");
        loadPageWithAlerts2(url);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "SecRet")
    public void basicAuthenticationUserFromUrlUsedForNextSteps() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();

        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (getWebDriver() instanceof HtmlUnitDriver) {
            // no credentials
            final WebDriver driver = loadPage2(html, new URL("http://localhost:" + PORT + "/"));
            assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
        }

        // now a url with credentials
        URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
        loadPageWithAlerts2(url);

        // next step without credentials but the credentials are still known
        url = new URL("http://localhost:" + PORT + "/");
        loadPageWithAlerts2(url);

        // different path
        url = new URL("http://localhost:" + PORT + "/somewhere");
        loadPageWithAlerts2(url);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "SecRet")
    public void basicAuthenticationUserFromUrlOverwrite() throws Exception {
        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();
        WebDriver driver = null;

        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (getWebDriver() instanceof HtmlUnitDriver) {
            // no credentials
            driver = loadPage2(html, new URL("http://localhost:" + PORT + "/"));
            assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
        }

        // now a url with credentials
        URL url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
        driver = loadPageWithAlerts2(url);

        // next step without credentials but the credentials are still known
        url = new URL("http://localhost:" + PORT + "/");
        loadPageWithAlerts2(url);

        if (getWebDriver() instanceof HtmlUnitDriver) {
            // and now with wrong credentials
            url = new URL("http://jetty:wrong@localhost:" + PORT + "/");
            loadPage2(html, url);
            assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SecRet")
    public void basicAuthenticationUserFromUrlOverwriteDefaultCredentials() throws Exception {
        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (!(getWebDriver() instanceof HtmlUnitDriver)) {
            return;
        }

        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");

        // use default credentials
        URL url = new URL("http://localhost:" + PORT + "/");
        final WebDriver driver = loadPageWithAlerts2(url);

        // now a url with wrong credentials
        url = new URL("http://joe:jetty@localhost:" + PORT + "/");
        loadPage2(html, url);

        if (getBrowserVersion().isIE()) {
            assertTrue(driver.getPageSource().contains("SecRet"));
        }
        else {
            assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "SecRet")
    public void basicAuthenticationUserFromUrlOverwriteWrongDefaultCredentials() throws Exception {
        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (!(getWebDriver() instanceof HtmlUnitDriver)) {
            return;
        }

        final String html = "<html><body onload='alert(\"SecRet\")'></body></html>";
        getMockWebConnection().setDefaultResponse(html);

        getWebClient().getCredentialsProvider().clear();
        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("joe", "hack");

        // use default wrong credentials
        URL url = new URL("http://localhost:" + PORT + "/");
        final WebDriver driver = loadPage2(html, url);
        assertTrue(driver.getPageSource().contains("HTTP ERROR 401"));

        // now a url with correct credentials
        url = new URL("http://jetty:jetty@localhost:" + PORT + "/");
        loadPageWithAlerts2(url);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "Hello World")
    public void basicAuthenticationXHR() throws Exception {
        final String html = "<html><head><script>\n"
            + "var xhr = " + XHRInstantiation_ + ";\n"
            + "var handler = function() {\n"
            + "  if (xhr.readyState == 4)\n"
            + "    alert(xhr.responseText);\n"
            + "}\n"
            + "xhr.onreadystatechange = handler;\n"
            + "xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "xhr.send('');\n"
            + "</script></head><body></body></html>";

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");
        getMockWebConnection().setDefaultResponse("Hello World");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("HTTP ERROR 401")
    public void basicAuthenticationXHRWithUsername() throws Exception {
        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (!(getWebDriver() instanceof HtmlUnitDriver)) {
            return;
        }

        final String html = "<html><head><script>\n"
            + "var xhr = " + XHRInstantiation_ + ";\n"
            + "var handler = function() {\n"
            + "  if (xhr.readyState == 4) {\n"
            + "    var s = xhr.responseText.replace(/[\\r\\n]/g, '')"
            + ".replace(/.*(HTTP ERROR \\d+).*/g, '$1');\n"
            + "    alert(s);\n"
            + "  }\n"
            + "}\n"
            + "xhr.onreadystatechange = handler;\n"
            + "xhr.open('GET', '/foo', true, 'joe');\n"
            + "xhr.send('');\n"
            + "</script></head><body></body></html>";

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");
        getMockWebConnection().setDefaultResponse("Hello World");
        loadPageWithAlerts2(html, 1000);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "HTTP ERROR 401")
    public void basicAuthenticationXHRWithUser() throws Exception {
        // this test is not running in real browser because there
        // in no support for basic auth in selenimum
        if (!(getWebDriver() instanceof HtmlUnitDriver)) {
            return;
        }

        final String html = "<html><head><script>\n"
            + "var xhr = " + XHRInstantiation_ + ";\n"
            + "var handler = function() {\n"
            + "  if (xhr.readyState == 4) {\n"
            + "    var s = xhr.responseText.replace(/[\\r\\n]/g, '')"
            + ".replace(/.*(HTTP ERROR \\d+).*/g, '$1');\n"
            + "    alert(s);\n"
            + "  }\n"
            + "}\n"
            + "xhr.onreadystatechange = handler;\n"
            + "xhr.open('GET', '/foo', true, 'joe', 'secret');\n"
            + "xhr.send('');\n"
            + "</script></head><body></body></html>";

        ((DefaultCredentialsProvider) getWebClient().getCredentialsProvider()).addCredentials("jetty", "jetty");
        getMockWebConnection().setDefaultResponse("Hello World");
        loadPageWithAlerts2(html, 1000);
    }
}
