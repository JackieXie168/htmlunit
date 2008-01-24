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
package com.gargoylesoftware.htmlunit;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WebAssert}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class WebAssertTest extends WebTestCase2 {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertTitleEquals() throws Exception {

        final String html = "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleEquals(page, "foo");

        boolean caught = false;
        try {
            WebAssert.assertTitleEquals(page, "bar");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertTitleContains() throws Exception {

        final String html = "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleContains(page, "o");

        boolean caught = false;
        try {
            WebAssert.assertTitleContains(page, "a");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertTitleMatches() throws Exception {

        final String html = "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleMatches(page, "f..");

        boolean caught = false;
        try {
            WebAssert.assertTitleMatches(page, "b..");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertElementPresent() throws Exception {

        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementPresent(page, "a");

        boolean caught = false;
        try {
            WebAssert.assertElementPresent(page, "b");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertElementPresentByXPath() throws Exception {

        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementPresentByXPath(page, "html/body/div");

        boolean caught = false;
        try {
            WebAssert.assertElementPresentByXPath(page, "ul");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertElementNotPresent() throws Exception {

        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementNotPresent(page, "b");

        boolean caught = false;
        try {
            WebAssert.assertElementNotPresent(page, "a");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertElementNotPresentByXPath() throws Exception {

        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementNotPresentByXPath(page, "ul");

        boolean caught = false;
        try {
            WebAssert.assertElementNotPresentByXPath(page, "html/body/div");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertTextPresent() throws Exception {

        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextPresent(page, "bar");

        boolean caught = false;
        try {
            WebAssert.assertTextPresent(page, "baz");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertTextPresentInElement() throws Exception {

        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextPresentInElement(page, "bar", "a");

        boolean caught = false;
        try {
            WebAssert.assertTextPresentInElement(page, "baz", "a");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
            WebAssert.assertTextPresentInElement(page, "bar", "b");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertTextNotPresent() throws Exception {

        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextNotPresent(page, "baz");

        boolean caught = false;
        try {
            WebAssert.assertTextNotPresent(page, "bar");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertTextNotPresentInElement() throws Exception {

        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextNotPresentInElement(page, "baz", "a");

        boolean caught = false;
        try {
            WebAssert.assertTextNotPresentInElement(page, "bar", "a");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
            WebAssert.assertTextNotPresentInElement(page, "bar", "b");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertLinkPresent() throws Exception {

        final String html = "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkPresent(page, "x");

        boolean caught = false;
        try {
            WebAssert.assertLinkPresent(page, "z");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertLinkNotPresent() throws Exception {

        final String html = "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkNotPresent(page, "z");

        boolean caught = false;
        try {
            WebAssert.assertLinkNotPresent(page, "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertLinkPresentWithText() throws Exception {

        final String html = "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkPresentWithText(page, "r");

        boolean caught = false;
        try {
            WebAssert.assertLinkPresentWithText(page, "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertLinkNotPresentWithText() throws Exception {

        final String html = "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkNotPresentWithText(page, "x");

        boolean caught = false;
        try {
            WebAssert.assertLinkNotPresentWithText(page, "r");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertFormPresent() throws Exception {

        final String html = "<html><body><form name='f'>bar</form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertFormPresent(page, "f");

        boolean caught = false;
        try {
            WebAssert.assertFormPresent(page, "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertFormNotPresent() throws Exception {

        final String html = "<html><body><form name='f'>bar</form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertFormNotPresent(page, "x");

        boolean caught = false;
        try {
            WebAssert.assertFormNotPresent(page, "f");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertInputPresent() throws Exception {

        final String html = "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputPresent(page, "i");

        boolean caught = false;
        try {
            WebAssert.assertInputPresent(page, "q");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertInputNotPresent() throws Exception {

        final String html = "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputNotPresent(page, "q");

        boolean caught = false;
        try {
            WebAssert.assertInputNotPresent(page, "i");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertInputContainsValue() throws Exception {

        final String html = "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputContainsValue(page, "i", "x");

        boolean caught = false;
        try {
            WebAssert.assertInputContainsValue(page, "i", "z");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
            WebAssert.assertInputContainsValue(page, "q", "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertInputDoesNotContainValue() throws Exception {

        final String html = "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputDoesNotContainValue(page, "i", "z");

        boolean caught = false;
        try {
            WebAssert.assertInputDoesNotContainValue(page, "i", "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
            WebAssert.assertInputDoesNotContainValue(page, "q", "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertAllTabIndexAttributesSet() throws Exception {

        final String html1 = "<html><body><a href='#' tabindex='1'>foo</a></body></html>";
        final HtmlPage page1 = loadPage(html1);

        WebAssert.assertAllTabIndexAttributesSet(page1);

        final String html2 = "<html><body><a href='#'>foo</a></body></html>";
        final HtmlPage page2 = loadPage(html2);

        boolean caught = false;
        try {
            WebAssert.assertAllTabIndexAttributesSet(page2);
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        final String html3 = "<html><body><a href='#' tabindex='x'>foo</a></body></html>";
        final HtmlPage page3 = loadPage(html3);

        caught = false;
        try {
            WebAssert.assertAllTabIndexAttributesSet(page3);
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertAllAccessKeyAttributesUnique() throws Exception {

        final String html1 = "<html><body><a accesskey='k'>foo</a></body></html>";
        final HtmlPage page1 = loadPage(html1);

        WebAssert.assertAllAccessKeyAttributesUnique(page1);

        final String html2 = "<html><body><a accesskey='k'>foo</a><a accesskey='k'>bar</a></body></html>";
        final HtmlPage page2 = loadPage(html2);

        boolean caught = false;
        try {
            WebAssert.assertAllAccessKeyAttributesUnique(page2);
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testAssertAllIdAttributesUnique() throws Exception {

        final String html1 = "<html><body><a id='k'>foo</a></body></html>";
        final HtmlPage page1 = loadPage(html1);

        WebAssert.assertAllIdAttributesUnique(page1);

        final String html2 = "<html><body><a id='k'>foo</a><a id='k'>bar</a></body></html>";
        final HtmlPage page2 = loadPage(html2);

        boolean caught = false;
        try {
            WebAssert.assertAllIdAttributesUnique(page2);
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

}
