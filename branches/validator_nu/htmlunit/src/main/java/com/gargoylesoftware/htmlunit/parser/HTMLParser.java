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
package com.gargoylesoftware.htmlunit.parser;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.SVG;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DefaultElementFactory;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.ElementFactory;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.InputElementFactory;
import com.gargoylesoftware.htmlunit.html.UnknownElementFactory;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.svg.SvgElementFactory;


/**
 * <p>SAX parser implementation that uses the NekoHTML {@link org.cyberneko.html.HTMLConfiguration}
 * to parse HTML into a HtmlUnit-specific DOM (HU-DOM) tree.</p>
 *
 * @version $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ethan Glasser-Camp
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
public final class HTMLParser {

    /** XHTML namespace. */
    public static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";

    /** SVG namespace. */
    public static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    private static final Map<String, ElementFactory> ELEMENT_FACTORIES = new HashMap<String, ElementFactory>();

    private static final ElementFactory SVG_FACTORY = new SvgElementFactory();

    static {
        ELEMENT_FACTORIES.put(HtmlInput.TAG_NAME, InputElementFactory.instance);

        final DefaultElementFactory defaultElementFactory = new DefaultElementFactory();
        for (final String tagName : DefaultElementFactory.SUPPORTED_TAGS_) {
            ELEMENT_FACTORIES.put(tagName, defaultElementFactory);
        }
    }

    /**
     * You should never need to create one of these!
     */
    private HTMLParser() {
        // Empty.
    }

    /**
     * Parses the HTML content from the given string into an object tree representation.
     *
     * @param parent the parent for the new nodes
     * @param source the (X)HTML to be parsed
     * @throws SAXException if a SAX error occurs
     * @throws IOException if an IO error occurs
     */
    public static void parseFragment(final DomNode parent, final String source) throws SAXException, IOException {
//        parseFragment(parent, parent, source);
    }

    /**
     * Parses the HTML content from the given string into an object tree representation.
     *
     * @param parent where the new parsed nodes will be added to
     * @param context the context to build the fragment context stack
     * @param source the (X)HTML to be parsed
     * @throws SAXException if a SAX error occurs
     * @throws IOException if an IO error occurs
     */
    public static void parseFragment(final DomNode parent, final DomNode context, final String source)
        throws SAXException, IOException {
        final HtmlPage page = (HtmlPage) parent.getPage();
        final URL url = page.getUrl();
//
//        final HtmlUnitDOMBuilder domBuilder = new HtmlUnitDOMBuilder(parent, url, source);
//        domBuilder.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
//        // build fragment context stack
//        DomNode node = context;
//        final List<QName> ancestors = new ArrayList<QName>();
//        while (node != null && node.getNodeType() != Node.DOCUMENT_NODE) {
//            ancestors.add(0, new QName(null, node.getNodeName(), null, null));
//            node = node.getParentNode();
//        }
//        if (ancestors.isEmpty() || !"html".equals(ancestors.get(0).localpart)) {
//            ancestors.add(0, new QName(null, "html", null, null));
//        }
//        if (ancestors.size() == 1 || !"body".equals(ancestors.get(1).localpart)) {
//            ancestors.add(1, new QName(null, "body", null, null));
//        }
//
//        domBuilder.setFeature(HTMLScanner.ALLOW_SELFCLOSING_TAGS, true);
//        domBuilder.setProperty(HTMLTagBalancer.FRAGMENT_CONTEXT_STACK, ancestors.toArray(new QName[] {}));
//
//        final XMLInputSource in = new XMLInputSource(null, url.toString(), null, new StringReader(source), null);
//
//        page.registerParsingStart();
//        page.registerSnippetParsingStart();
//        try {
//            domBuilder.parse(in);
//        }
//        finally {
//            page.registerParsingEnd();
//            page.registerSnippetParsingEnd();
//        }
    }

    /**
     * Parses the HTML content from the specified <tt>WebResponse</tt> into an object tree representation.
     *
     * @param webResponse the response data
     * @param webWindow the web window into which the page is to be loaded
     * @return the page object which is the root of the DOM tree
     * @throws IOException if there is an IO error
     */
    public static HtmlPage parseHtml(final WebResponse webResponse, final WebWindow webWindow) throws IOException {
        final HtmlPage page = new HtmlPage(webResponse.getWebRequest().getUrl(), webResponse, webWindow);
//        parse(webResponse, webWindow, page, false);
        return page;
    }

    /**
     * Parses the XHTML content from the specified <tt>WebResponse</tt> into an object tree representation.
     *
     * @param webResponse the response data
     * @param webWindow the web window into which the page is to be loaded
     * @return the page object which is the root of the DOM tree
     * @throws IOException if there is an IO error
     */
    public static XHtmlPage parseXHtml(final WebResponse webResponse, final WebWindow webWindow) throws IOException {
        final XHtmlPage page = new XHtmlPage(webResponse.getWebRequest().getUrl(), webResponse, webWindow);
//        parse(webResponse, webWindow, page, true);
        return page;
    }

//    private static void parse(final WebResponse webResponse, final WebWindow webWindow, final HtmlPage page,
//            final boolean xhtml)
//        throws IOException {
//
//        webWindow.setEnclosedPage(page);
//
//        final URL url = webResponse.getWebRequest().getUrl();
//        final HtmlUnitDOMBuilder domBuilder = new HtmlUnitDOMBuilder(page, url, null);
//
//        String charset = webResponse.getContentCharsetOrNull();
//        try {
//            // handle charset
//            if (charset != null) {
//                domBuilder.setFeature(HTMLScanner.IGNORE_SPECIFIED_CHARSET, true);
//            }
//            else {
//                final String specifiedCharset = webResponse.getWebRequest().getCharset();
//                if (specifiedCharset != null) {
//                    charset = specifiedCharset;
//                }
//            }
//
//            // xml content is different
//            if (xhtml) {
//                domBuilder.setFeature(HTMLScanner.ALLOW_SELFCLOSING_TAGS, true);
//            }
//        }
//        catch (final Exception e) {
//            throw new ObjectInstantiationException("Error setting HTML parser feature", e);
//        }
//
//        final InputStream content = webResponse.getContentAsStream();
//        final XMLInputSource in = new XMLInputSource(null, url.toString(), null, content, charset);
//
//        page.registerParsingStart();
//        try {
//            domBuilder.parse(in);
//        }
//        catch (final XNIException e) {
//            // extract enclosed exception
//            final Throwable origin = extractNestedException(e);
//            throw new RuntimeException("Failed parsing content from " + url, origin);
//        }
//        finally {
//            IOUtils.closeQuietly(content);
//            page.registerParsingEnd();
//        }
//
//        addBodyToPageIfNecessary(page, true, domBuilder.body_ != null);
//    }
//
//    /**
//     * Adds a body element to the current page, if necessary. Strictly speaking, this should
//     * probably be done by NekoHTML. See the bug linked below. If and when that bug is fixed,
//     * we may be able to get rid of this code.
//     *
//     * http://sourceforge.net/p/nekohtml/bugs/15/
//     * @param page
//     * @param originalCall
//     * @param checkInsideFrameOnly true if the original page had body that was removed by JavaScript
//     */
//    private static void addBodyToPageIfNecessary(
//            final HtmlPage page, final boolean originalCall, final boolean checkInsideFrameOnly) {
//        // IE waits for the whole page to load before initializing bodies for frames.
//        final boolean waitToLoad = page.hasFeature(PAGE_WAIT_LOAD_BEFORE_BODY);
//        if (page.getEnclosingWindow() instanceof FrameWindow && originalCall && waitToLoad) {
//            return;
//        }
//
//        // Find out if the document already has a body element (or frameset).
//        final Element doc = page.getDocumentElement();
//        boolean hasBody = false;
//        for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
//            if (child instanceof HtmlBody || child instanceof HtmlFrameSet) {
//                hasBody = true;
//                break;
//            }
//        }
//
//        // If the document does not have a body, add it.
//        if (!hasBody && !checkInsideFrameOnly) {
//            final HtmlBody body = new HtmlBody("body", page, null, false);
//            doc.appendChild(body);
//        }
//
//        // If this is IE, we need to initialize the bodies of any frames, as well.
//        // This will already have been done when emulating FF (see above).
//        if (waitToLoad) {
//            for (final FrameWindow frame : page.getFrames()) {
//                final Page containedPage = frame.getEnclosedPage();
//                if (containedPage != null && containedPage.isHtmlPage()) {
//                    addBodyToPageIfNecessary((HtmlPage) containedPage, false, false);
//                }
//            }
//        }
//    }
//
//    /**
//     * Extract nested exception within an XNIException (Nekohtml uses reflection and generated
//     * exceptions are wrapped many times within XNIException and InvocationTargetException)
//     *
//     * @param e the original XNIException
//     * @return the cause exception
//     */
//    static Throwable extractNestedException(final Throwable e) {
//        Throwable originalException = e;
//        Throwable cause = ((XNIException) e).getException();
//        while (cause != null) {
//            originalException = cause;
//            if (cause instanceof XNIException) {
//                cause = ((XNIException) cause).getException();
//            }
//            else if (cause instanceof InvocationTargetException) {
//                cause = cause.getCause();
//            }
//            else {
//                cause = null;
//            }
//        }
//        return originalException;
//    }

    /**
     * @param tagName an HTML element tag name
     * @return a factory for creating HtmlElements representing the given tag
     */
    public static ElementFactory getFactory(final String tagName) {
        final ElementFactory result = ELEMENT_FACTORIES.get(tagName);

        if (result != null) {
            return result;
        }
        return UnknownElementFactory.instance;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the pre-registered element factory corresponding to the specified tag, or an UnknownElementFactory.
     * @param page the page
     * @param namespaceURI the namespace URI
     * @param qualifiedName the qualified name
     * @return the pre-registered element factory corresponding to the specified tag, or an UnknownElementFactory
     */
    public static ElementFactory getElementFactory(final HtmlPage page, final String namespaceURI,
            final String qualifiedName) {
        if (namespaceURI == null || namespaceURI.isEmpty()
            || !qualifiedName.contains(":") || namespaceURI.equals(XHTML_NAMESPACE)) {

            if (SVG_NAMESPACE.equals(namespaceURI)
                    && page.hasFeature(SVG)) {
                return SVG_FACTORY;
            }

            String tagName = qualifiedName;
            final int index = tagName.indexOf(':');
            if (index != -1) {
                tagName = tagName.substring(index + 1);
            }
            else {
                tagName = tagName.toLowerCase(Locale.ENGLISH);
            }
            final ElementFactory factory = ELEMENT_FACTORIES.get(tagName);

            if (factory != null) {
                return factory;
            }
        }
        return UnknownElementFactory.instance;
    }
}

///**
// * Utility to transmit parsing errors to a {@link HTMLParserListener}.
// */
//class HTMLErrorHandler extends DefaultErrorHandler {
//    private final HTMLParserListener listener_;
//    private final URL url_;
//    private String html_;
//
//    HTMLErrorHandler(final HTMLParserListener listener, final URL url, final String htmlContent) {
//        WebAssert.notNull("listener", listener);
//        WebAssert.notNull("url", url);
//        listener_ = listener;
//        url_ = url;
//        html_ = htmlContent;
//    }
//
//    /** @see DefaultErrorHandler#error(String,String,XMLParseException) */
//    @Override
//    public void error(final String domain, final String key,
//            final XMLParseException exception) throws XNIException {
//        listener_.error(exception.getMessage(),
//                url_,
//                html_,
//                exception.getLineNumber(),
//                exception.getColumnNumber(),
//                key);
//    }
//
//    /** @see DefaultErrorHandler#warning(String,String,XMLParseException) */
//    @Override
//    public void warning(final String domain, final String key,
//            final XMLParseException exception) throws XNIException {
//        listener_.warning(exception.getMessage(),
//                url_,
//                html_,
//                exception.getLineNumber(),
//                exception.getColumnNumber(),
//                key);
//    }
//}
//
//class HTMLScannerForIE extends org.cyberneko.html.HTMLScanner {
//    HTMLScannerForIE(final BrowserVersion browserVersion) {
//        fContentScanner = new ContentScannerForIE(browserVersion);
//    }
//
//    class ContentScannerForIE extends HTMLScanner.ContentScanner {
//        private final BrowserVersion browserVersion_;
//
//        ContentScannerForIE(final BrowserVersion browserVersion) {
//            browserVersion_ = browserVersion;
//        }
//
//        @Override
//        protected void scanComment() throws IOException {
//            final String s = nextContent(30); // [if ...
//            if (s.startsWith("[if ") && s.contains("]>")) {
//                final String condition = StringUtils.substringBefore(s.substring(4), "]>");
//                try {
//                    if (IEConditionalCommentExpressionEvaluator.evaluate(condition, browserVersion_)) {
//                        // skip until ">"
//                        for (int i = 0; i < condition.length() + 6; ++i) {
//                            read();
//                        }
//                        if (s.contains("]><!-->")) {
//                            skip("<!-->", false);
//                        }
//                        else if (s.contains("]>-->")) {
//                            skip("-->", false);
//                        }
//                    }
//                    else {
//                        final StringBuilder builder = new StringBuilder();
//                        while (!builder.toString().endsWith("-->")) {
//                            builder.append((char) read());
//                        }
//                    }
//                    return;
//                }
//                catch (final Exception e) { // incorrect expression => handle it as plain text
//                    // TODO: report it!
//                    final XMLStringBuffer buffer = new XMLStringBuffer("<!--");
//                    scanMarkupContent(buffer, '-');
//                    buffer.append("-->");
//                    fDocumentHandler.characters(buffer, locationAugs());
//                    return;
//                }
//            }
//            // this is a normal comment, not a conditional comment for IE
//            super.scanComment();
//        }
//
//        @Override
//        public String nextContent(final int len) throws IOException {
//            return super.nextContent(len);
//        }
//
//        @Override
//        public boolean scanMarkupContent(final XMLStringBuffer buffer, final char cend) throws IOException {
//            return super.scanMarkupContent(buffer, cend);
//        }
//    }
//
//    @Override
//    protected boolean skipMarkup(final boolean balance) throws IOException {
//        final ContentScannerForIE contentScanner = (ContentScannerForIE) fContentScanner;
//        final String s = contentScanner.nextContent(30);
//        if (s.startsWith("[if ") && s.contains("]>")) {
//            final String condition = StringUtils.substringBefore(s.substring(4), "]>");
//            try {
//                if (IEConditionalCommentExpressionEvaluator.evaluate(condition, contentScanner.browserVersion_)) {
//                    // skip until ">"
//                    for (int i = 0; i < condition.length() + 6; ++i) {
//                        read();
//                    }
//                    return true;
//                }
//
//                final XMLStringBuffer buffer = new XMLStringBuffer();
//                int ch;
//                while ((ch = read()) != -1) {
//                    buffer.append((char) ch);
//                    if (buffer.toString().endsWith("<![endif]>")) {
//                        final XMLStringBuffer trimmedBuffer
//                            = new XMLStringBuffer(buffer.ch, 0, buffer.length - 3);
//                        fDocumentHandler.comment(trimmedBuffer, locationAugs());
//                        return true;
//                    }
//                }
//            }
//            catch (final Exception e) { // incorrect expression => handle it as plain text
//                // TODO: report it!
//                final XMLStringBuffer buffer = new XMLStringBuffer("<!--");
//                contentScanner.scanMarkupContent(buffer, '-');
//                buffer.append("-->");
//                fDocumentHandler.characters(buffer, locationAugs());
//                return true;
//            }
//
//        }
//        return super.skipMarkup(balance);
//    }
// }
