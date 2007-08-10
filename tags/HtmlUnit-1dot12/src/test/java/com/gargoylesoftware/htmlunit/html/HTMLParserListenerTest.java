/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Test class for {@link HTMLParserListener}.<br/>
 * We probably don't need to check the details of the messages generated by the
 * parser but just that we catch and "transmit" them.
 * @version $Revision$
 * @author Marc Guillemot
 */
public class HTMLParserListenerTest extends WebTestCase {
    static class MessageInfo {
        private boolean error_; // versus warning
        private String message_;
        private URL url_;
        private int line_;
        private int column_;

        /**
         * Utility class to hold data.
         * @param error The error
         * @param message The message
         * @param url The url
         * @param line The line number
         * @param column The column number
         * @param key Ignored value
         */
        MessageInfo(final boolean error, final String message, final URL url,
                final int line, final int column, final String key) {
            error_ = error;
            message_ = message;
            url_ = url;
            line_ = line;
            column_ = column;
            // ignore key
        }

        /** @see Object#toString() */
        public String toString() {
            return message_ + " (" + url_ + " " + line_ + ":" + column_ + ")";
        }

        /**
         * Compares according to error, message, url and line.
         * @see Object#equals(Object)
         */
        public boolean equals(final Object obj) {
            if (!(obj instanceof MessageInfo)) {
                return false;
            }
            final MessageInfo other = (MessageInfo) obj;
            final EqualsBuilder builder = new EqualsBuilder();
            builder.append(error_, other.error_);
            builder.append(message_, other.message_);
            builder.append(url_.toExternalForm(), other.url_.toExternalForm());
            builder.append(line_, other.line_);
            return builder.isEquals();
        }
        
        /**
         * Stub to fix Eclipse warning
         * @see Object#hashCode()
         */
        public int hashCode() {
            return super.hashCode();
        }
    }

    /**
     * Create an instance
     * @param name The name of the test
     */
    public HTMLParserListenerTest(final String name) {
        super(name);
    }

    /**
     * @exception Exception If the test fails
     */
    public void testSimple() throws Exception {
        final String htmlContent = "<html>\n" + "<head>\n<title>foo\n</head>\n"
                + "<body>\nfoo\n</body>\n</html>";

        final WebClient webClient = new WebClient();
        assertNull(webClient.getHTMLParserListener());
        WebClient.setIgnoreOutsideContent(true);

        final List messages = new ArrayList();
        final HTMLParserListener collecter = new HTMLParserListener() {
            public void error(final String message, final URL url,
                    final int line, final int column, final String key) {
                messages.add(new MessageInfo(true, message, url, line, column,
                        key));
            }

            public void warning(final String message, final URL url,
                    final int line, final int column, final String key) {
                messages.add(new MessageInfo(false, message, url, line, column,
                        key));
            }
        };
        webClient.setHTMLParserListener(collecter);

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setDefaultResponse(htmlContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("foo", page.getTitleText());
        
        // ignore column and key
        final MessageInfo expectedError = new MessageInfo(false,
                "End element <head> automatically closes element <title>.",
                URL_FIRST, 4, -1, null);
        assertEquals(Collections.singletonList(expectedError), messages);
    }
}
