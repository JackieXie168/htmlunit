/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Test class for {@link HTMLParserListener}.<br/>
 * We probably don't need to check the details of the messages generated by the
 * parser but just that we catch and "transmit" them.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLParserListenerTest extends SimpleWebTestCase {
    static class MessageInfo {
        private boolean error_; // versus warning
        private String message_;
        private URL url_;
        private String html_;
        private int line_;
        private int column_;

        /**
         * Utility class to hold data.
         * @param error the error
         * @param message the message
         * @param url the URL
         * @param line the line number
         * @param column the column number
         * @param key Ignored value
         */
        MessageInfo(final boolean error, final String message, final URL url, final String html,
                final int line, final int column, final String key) {
            error_ = error;
            message_ = message;
            url_ = url;
            html_ = html;
            line_ = line;
            column_ = column;
            // ignore key
        }

        /** @see Object#toString() */
        @Override
        public String toString() {
            return message_ + " (" + url_ + " " + line_ + ":" + column_ + ")";
        }

        /**
         * Compares according to error, message, URL and line.
         * @see Object#equals(Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof MessageInfo)) {
                return false;
            }
            final MessageInfo other = (MessageInfo) obj;
            final EqualsBuilder builder = new EqualsBuilder();
            builder.append(error_, other.error_);
            builder.append(message_, other.message_);
            builder.append(url_.toExternalForm(), other.url_.toExternalForm());
            builder.append(html_, other.html_);
            builder.append(line_, other.line_);
            builder.append(column_, other.column_);
            return builder.isEquals();
        }

        /**
         * @see Object#hashCode()
         */
        @Override
        public int hashCode() {
            final HashCodeBuilder builder = new HashCodeBuilder();
            builder.append(error_);
            builder.append(message_);
            builder.append(url_);
            builder.append(html_);
            builder.append(line_);
            builder.append(column_);
            return builder.hashCode();
        }
    }

    /**
     * This is the right test as we had before HtmlUnit-2.6 but due
     * to an (accepted - at least in a first time -) regression in
     * NekoHTML, it doesn't work anymore.
     * testSimple_withWrongLineCol ensures that no other regression occurs here.
     * @exception Exception If the test fails
     */
    @Test
    public void testSimple() throws Exception {
        testSimple(5, 7);
    }

    /**
     * Currently, NekoHtml doesn't deliver right information about the line
     * for the warning. Let this test run with wrong expectation
     * on line and col to avoid that other regression occur.
     * @exception Exception If the test fails
     */
    @Test
    public void testSimple_withWrongLineCol() throws Exception {
        testSimple(5, 7);
    }

    private void testSimple(final int line, final int col) throws Exception {
        final String htmlContent = "<html>\n" + "<head>\n<title>foo\n</head>\n"
                + "<body>\nfoo\n</body>\n</html>";

        final WebClient webClient = getWebClient();
        assertNull(webClient.getHTMLParserListener());

        final List<MessageInfo> messages = new ArrayList<MessageInfo>();
        final HTMLParserListener collecter = new HTMLParserListener() {

            public void error(final String message, final URL url, final String html,
                    final int line, final int column, final String key) {
                messages.add(new MessageInfo(true, message, url, html, line, column, key));
            }

            public void warning(final String message, final URL url, final String html,
                    final int line, final int column, final String key) {
                messages.add(new MessageInfo(false, message, url, html, line, column, key));
            }
        };
        webClient.setHTMLParserListener(collecter);

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("foo", page.getTitleText());

        // ignore column and key
        final MessageInfo expectedError = new MessageInfo(false,
                "End element <head> automatically closes element <title>.",
                URL_FIRST, null, line, col, null);

        assertEquals(1, messages.size());
        assertEquals(expectedError, messages.get(0));
    }

    /**
     * Test parsing of an fragment.
     * @exception Exception If the test fails
     */
    @Test
    public void parseFragment() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  var oNode = document.getElementById('middle');\n"
                + "  oNode.insertAdjacentHTML('afterEnd', '<div><span>before end</div>');\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <span id='middle' style='color: #ff0000'>\n"
                + "    inside\n"
                + "  </span>\n"
                + "</body></html>";

        final WebClient webClient = getWebClient();
        assertNull(webClient.getHTMLParserListener());

        final List<MessageInfo> messages = new ArrayList<MessageInfo>();
        final HTMLParserListener collecter = new HTMLParserListener() {

            public void error(final String message, final URL url, final String html,
                    final int line, final int column, final String key) {
                messages.add(new MessageInfo(true, message, url, html, line, column, key));
            }

            public void warning(final String message, final URL url, final String html,
                    final int line, final int column, final String key) {
                messages.add(new MessageInfo(false, message, url, html, line, column, key));
            }
        };
        webClient.setHTMLParserListener(collecter);

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("foo", page.getTitleText());

        // ignore key
        final MessageInfo expectedError = new MessageInfo(false,
                "End element <div> automatically closes element <span>.",
                URL_FIRST, "<div><span>before end</div>", 1, 28, null);

        assertEquals(1, messages.size());
        assertEquals(expectedError, messages.get(0));
    }
}
