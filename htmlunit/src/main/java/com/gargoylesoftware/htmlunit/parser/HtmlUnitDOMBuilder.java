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

import java.io.IOException;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The parser and DOM builder. This class subclasses Xerces's AbstractSAXParser and implements
 * the ContentHandler interface. Thus all parser APIs are kept private. The ContentHandler methods
 * consume SAX events to build the page DOM
 */
public final class HtmlUnitDOMBuilder implements ContentHandler, LexicalHandler {

    private enum HeadParsed { YES, SYNTHESIZED, NO };

//    private final HtmlPage page_;

    private Locator locator_;
    private final Stack<DomNode> stack_ = new Stack<DomNode>();

    private DomNode currentNode_;
    private StringBuilder characters_;
    private HeadParsed headParsed_ = HeadParsed.NO;
    private boolean parsingInnerHead_ = false;
    private HtmlElement head_;
    private HtmlElement body_;
    private boolean lastTagWasSynthesized_;
    private HtmlForm formWaitingForLostChildren_;
    private static final String FEATURE_AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
    private static final String FEATURE_PARSE_NOSCRIPT
        = "http://cyberneko.org/html/features/parse-noscript-content";

    /**
     * Parses and then inserts the specified HTML content into the HTML content currently being parsed.
     * @param html the HTML content to push
     */
    public void pushInputString(final String html) {
//        page_.registerParsingStart();
//        page_.registerInlineSnippetParsingStart();
//        try {
//            final WebResponse webResponse = page_.getWebResponse();
//            final String charset = webResponse.getContentCharset();
//            final String url = webResponse.getWebRequest().getUrl().toString();
//            final XMLInputSource in = new XMLInputSource(null, url, null, new StringReader(html), charset);
//            ((HTMLConfiguration) fConfiguration).evaluateInputSource(in);
//        }
//        finally {
//            page_.registerParsingEnd();
//            page_.registerInlineSnippetParsingEnd();
//        }
    }

//    /**
//     * Creates a new builder for parsing the specified response contents.
//     * @param node the location at which to insert the new content
//     * @param url the page's URL
//     */
//    private HtmlUnitDOMBuilder(final DomNode node, final URL url, final String htmlContent) {
//        super(createConfiguration(node.getPage().getWebClient()));
//        page_ = (HtmlPage) node.getPage();
//
//        currentNode_ = node;
//        for (final Node ancestor : currentNode_.getAncestors(true)) {
//            stack_.push((DomNode) ancestor);
//        }
//
//        final WebClient webClient = page_.getWebClient();
//        final HTMLParserListener listener = webClient.getHTMLParserListener();
//        final boolean reportErrors;
//        if (listener != null) {
//            reportErrors = true;
//            fConfiguration.setErrorHandler(new HTMLErrorHandler(listener, url, htmlContent));
//        }
//        else {
//            reportErrors = false;
//        }
//
//        try {
//            setFeature(FEATURE_AUGMENTATIONS, true);
//            setProperty("http://cyberneko.org/html/properties/names/elems", "default");
//            if (!webClient.getBrowserVersion().hasFeature(HTML_ATTRIBUTE_LOWER_CASE)) {
//                setProperty("http://cyberneko.org/html/properties/names/attrs", "no-change");
//            }
//            setFeature("http://cyberneko.org/html/features/report-errors", reportErrors);
//            setFeature(FEATURE_PARSE_NOSCRIPT, !webClient.getOptions().isJavaScriptEnabled());
//            setFeature(HTMLScanner.ALLOW_SELFCLOSING_IFRAME,
//                !webClient.getBrowserVersion().hasFeature(HTMLIFRAME_IGNORE_SELFCLOSING));
//
//            setContentHandler(this);
//            setLexicalHandler(this); //comments and CDATA
//        }
//        catch (final SAXException e) {
//            throw new ObjectInstantiationException("unable to create HTML parser", e);
//        }
//    }

//    /**
//     * Create the configuration depending on the simulated browser
//     * @param webClient the current WebClient
//     * @return the configuration
//     */
//    private static XMLParserConfiguration createConfiguration(final WebClient webClient) {
//        final BrowserVersion browserVersion = webClient.getBrowserVersion();
//        // for IE we need a special scanner that will be able to understand conditional comments
//        if (browserVersion.hasFeature(HTMLCONDITIONAL_COMMENTS)) {
//            return new HTMLConfiguration() {
//                @Override
//                protected HTMLScanner createDocumentScanner() {
//                    return new HTMLScannerForIE(browserVersion);
//                }
//            };
//        }
//        return new HTMLConfiguration();
//    }

    /**
     * @return the document locator
     */
    public Locator getLocator() {
        return locator_;
    }

    /** {@inheritDoc ContentHandler#setDocumentLocator} */
    public void setDocumentLocator(final Locator locator) {
        locator_ = locator;
    }

    /** {@inheritDoc ContentHandler#startDocument()} */
    public void startDocument() throws SAXException {
    }

//    /** {@inheritDoc} */
//    @Override
//    public void startElement(final QName element, final XMLAttributes attributes, final Augmentations augs)
//        throws XNIException {
//        // augs might change so we store only the interesting part
//        lastTagWasSynthesized_ = isSynthesized(augs);
//        super.startElement(element, attributes, augs);
//    }

    /** {@inheritDoc ContentHandler#startElement(String,String,String,Attributes)} */
    public void startElement(
            String namespaceURI, final String localName,
            final String qName, final Attributes atts)
        throws SAXException {
//
//        handleCharacters();
//
//        final String tagLower = localName.toLowerCase(Locale.ENGLISH);
//        if (page_.isParsingHtmlSnippet() && ("html".equals(tagLower) || "body".equals(tagLower))) {
//            return;
//        }
//
//        if (parsingInnerHead_ && page_.hasFeature(IGNORE_CONTENTS_OF_INNER_HEAD)) {
//            return;
//        }
//
//        if (namespaceURI != null) {
//            namespaceURI = namespaceURI.trim();
//        }
//        if ("head".equals(tagLower)) {
//            if (headParsed_ == HeadParsed.YES || page_.isParsingHtmlSnippet()) {
//                parsingInnerHead_ = true;
//                return;
//            }
//
//            headParsed_ = lastTagWasSynthesized_ ? HeadParsed.SYNTHESIZED : HeadParsed.YES;
//        }
//        // add a head if none was there
//        else if (headParsed_ == HeadParsed.NO && ("body".equals(tagLower) || "frameset".equals(tagLower))) {
//            final ElementFactory factory = getElementFactory(page_, namespaceURI, "head");
//            final DomElement newElement = factory.createElement(page_, "head", null);
//            currentNode_.appendChild(newElement);
//            headParsed_ = HeadParsed.SYNTHESIZED;
//        }
//
//        // If we're adding a body element, keep track of any temporary synthetic ones
//        // that we may have had to create earlier (for document.write(), for example).
//        HtmlBody oldBody = null;
//        if ("body".equals(qName) && page_.getBody() instanceof HtmlBody) {
//            oldBody = (HtmlBody) page_.getBody();
//        }
//
//        // Add the new node.
//        if (!(page_ instanceof XHtmlPage) && XHTML_NAMESPACE.equals(namespaceURI)) {
//            namespaceURI = null;
//        }
//        final ElementFactory factory = getElementFactory(page_, namespaceURI, qName);
//        final DomElement newElement = factory.createElementNS(page_, namespaceURI, qName, atts, true);
//        newElement.setStartLocation(locator_.getLineNumber(), locator_.getColumnNumber());
//
//        // parse can't replace everything as it does not buffer elements while parsing
//        addNodeToRightParent(currentNode_, newElement);
//
//        // If we had an old synthetic body and we just added a real body element, quietly
//        // remove the old body and move its children to the real body element we just added.
//        if (oldBody != null) {
//            oldBody.quietlyRemoveAndMoveChildrenTo(newElement);
//        }
//
//        if ("body".equals(tagLower)) {
//            body_ = (HtmlElement) newElement;
//        }
//        else if ("head".equals(tagLower)) {
//            head_ = (HtmlElement) newElement;
//        }
//        else if ("html".equals(tagLower)) {
//            if (!page_.hasFeature(JS_DEFINE_GETTER) && page_.isQuirksMode()) {
//                // this is not really correct; a following meta tag may disable the quirks
//                // mode; but at the moment i have no idea for a better place for this
//                removePrototypeProperties((Scriptable) page_.getEnclosingWindow().getScriptObject(), "Array",
//                    "every", "filter", "forEach", "indexOf", "lastIndexOf", "map", "reduce",
//                    "reduceRight", "some");
//            }
//        }
//        else if ("meta".equals(tagLower)) {
//            // i like the IE
//            if (page_.hasFeature(META_X_UA_COMPATIBLE)) {
//                final HtmlMeta meta = (HtmlMeta) newElement;
//                if ("X-UA-Compatible".equals(meta.getHttpEquivAttribute())) {
//                    final String content = meta.getContentAttribute();
//                    if (content.startsWith("IE=")) {
//                        final String mode = content.substring(3).trim();
//                        final int version = (int) page_.getWebClient().getBrowserVersion().
//                                                            getBrowserVersionNumeric();
//                        if ("edge".equals(mode)) {
//                            ((HTMLDocument) page_.getScriptObject()).forceDocumentMode(version);
//                        }
//                        else {
//                            try {
//                                int value = Integer.parseInt(mode);
//                                if (value > version) {
//                                    value = version;
//                                }
//                                ((HTMLDocument) page_.getScriptObject()).forceDocumentMode(value);
//                            }
//                            catch (final Exception e) {
//                                // ignore
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        currentNode_ = newElement;
//        stack_.push(currentNode_);
    }

//    /**
//     * Removes prototype properties.
//     * @param scope the scope
//     * @param className the class for which properties should be removed
//     * @param properties the properties to remove
//     */
//    private void removePrototypeProperties(final Scriptable scope, final String className,
//            final String... properties) {
//        final ScriptableObject prototype = (ScriptableObject) ScriptableObject.getClassPrototype(scope, className);
//        for (final String property : properties) {
//            prototype.delete(property);
//        }
//    }

//    /**
//     * Adds the new node to the right parent that is not necessary the currentNode in case
//     * of malformed HTML code.
//     */
//    private void addNodeToRightParent(final DomNode currentNode, final DomElement newElement) {
//        final String currentNodeName = currentNode.getNodeName();
//        final String newNodeName = newElement.getNodeName();
//
//        // this only fixes bug http://sourceforge.net/support/tracker.php?aid=2767865
//        // TODO: understand in which cases it should be done to generalize it!!!
//        if ("table".equals(currentNodeName) && "div".equals(newNodeName)) {
//            currentNode.insertBefore(newElement);
//        }
//        else if (head_ != null && "title".equals(newNodeName) && !parsingInnerHead_) {
//            head_.appendChild(newElement);
//        }
//        else {
//            currentNode.appendChild(newElement);
//        }
//    }

//    /** {@inheritDoc} */
//    @Override
//    public void endElement(final QName element, final Augmentations augs)
//        throws XNIException {
//        // augs might change so we store only the interesting part
//        lastTagWasSynthesized_ = isSynthesized(augs);
//        super.endElement(element, augs);
//    }

    /** {@inheritDoc ContentHandler@endElement(String,String,String)} */
    public void endElement(final String namespaceURI, final String localName, final String qName)
        throws SAXException {
//
//        handleCharacters();
//
//        final String tagLower = localName.toLowerCase(Locale.ENGLISH);
//
//        if (page_.isParsingHtmlSnippet() && ("html".equals(tagLower) || "body".equals(tagLower))) {
//            return;
//        }
//
//        if (parsingInnerHead_) {
//            if ("head".equals(tagLower)) {
//                parsingInnerHead_ = false;
//            }
//            if ("head".equals(tagLower) || page_.hasFeature(IGNORE_CONTENTS_OF_INNER_HEAD)) {
//                return;
//            }
//        }
//
//        final DomNode previousNode = stack_.pop(); //remove currentElement from stack
//        previousNode.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());
//
//        // special handling for form lost children (malformed HTML code where </form> is synthesized)
//        if (previousNode instanceof HtmlForm && lastTagWasSynthesized_) {
//            formWaitingForLostChildren_ = (HtmlForm) previousNode;
//        }
//        else if (formWaitingForLostChildren_ != null && previousNode instanceof SubmittableElement) {
//            formWaitingForLostChildren_.addLostChild((HtmlElement) previousNode);
//        }
//
//        if (!stack_.isEmpty()) {
//            currentNode_ = stack_.peek();
//        }
//
//        final boolean postponed = page_.isParsingInlineHtmlSnippet();
//        previousNode.onAllChildrenAddedToPage(postponed);
    }

    /** {@inheritDoc} */
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
//        if ((characters_ == null || characters_.length() == 0)
//                && page_.hasFeature(HTMLPARSER_REMOVE_EMPTY_CONTENT)
//                && StringUtils.isBlank(new String(ch, start, length))) {
//
//            DomNode node = currentNode_.getLastChild();
//            if (currentNode_ instanceof HTMLElement.ProxyDomNode) {
//                final HTMLElement.ProxyDomNode proxyNode = (HTMLElement.ProxyDomNode) currentNode_;
//                node = proxyNode.getDomNode();
//                if (!proxyNode.isAppend()) {
//                    node = node.getPreviousSibling();
//                    if (node == null) {
//                        node = proxyNode.getDomNode().getParentNode();
//                    }
//                }
//            }
//            if (removeEmptyCharacters(node)) {
//                return;
//            }
//        }
//        if (characters_ == null) {
//            characters_ = new StringBuilder();
//        }
//        characters_.append(ch, start, length);
    }

//    private boolean removeEmptyCharacters(final DomNode node) {
//        if (node != null) {
//            if (node instanceof HtmlInput) {
//                return false;
//            }
//            if (node.getFirstChild() != null
//                && (node instanceof HtmlAnchor || node instanceof HtmlSpan
//                    || node instanceof HtmlFont
//                    || node instanceof HtmlStrong || node instanceof HtmlBold
//                    || node instanceof HtmlItalic || node instanceof HtmlUnderlined
//                    || node instanceof HtmlEmphasis
//                    || node instanceof HtmlAbbreviated || node instanceof HtmlAcronym
//                    || node instanceof HtmlBaseFont || node instanceof HtmlBidirectionalOverride
//                    || node instanceof HtmlBig || node instanceof HtmlBlink
//                    || node instanceof HtmlCitation || node instanceof HtmlCode
//                    || node instanceof HtmlDeletedText || node instanceof HtmlDefinition
//                    || node instanceof HtmlInsertedText || node instanceof HtmlKeyboard
//                    || node instanceof HtmlLabel || node instanceof HtmlMap
//                    || node instanceof HtmlNoBreak || node instanceof HtmlInlineQuotation
//                    || node instanceof HtmlS || node instanceof HtmlSample
//                    || node instanceof HtmlSmall || node instanceof HtmlStrike
//                    || node instanceof HtmlSubscript || node instanceof HtmlSuperscript
//                    || node instanceof HtmlTeletype || node instanceof HtmlVariable
//                    )) {
//                return false;
//            }
//        }
//        else {
//            if (currentNode_ instanceof HtmlFont) {
//                return false;
//            }
//        }
//        return true;
//    }

    /** {@inheritDoc} */
    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
//        if (characters_ == null) {
//            characters_ = new StringBuilder();
//        }
//        characters_.append(ch, start, length);
    }

//    /**
//     * Picks up the character data accumulated so far and add it to the current element as a text node.
//     */
//    private void handleCharacters() {
//        if (characters_ != null && characters_.length() > 0) {
//            if (currentNode_ instanceof HtmlHtml) {
//                // In HTML, the <html> node only has two possible children:
//                // the <head> and the <body>; any text is ignored.
//                characters_.setLength(0);
//            }
//            else {
//                // Use the normal behavior: append a text node for the accumulated text.
//                final String textValue = characters_.toString();
//                final DomText text = new DomText(page_, textValue);
//                characters_.setLength(0);
//
//                // malformed HTML: </td>some text</tr> => text comes before the table
//                if (currentNode_ instanceof HtmlTableRow && StringUtils.isNotBlank(textValue)) {
//                    final HtmlTableRow row = (HtmlTableRow) currentNode_;
//                    final HtmlTable enclosingTable = row.getEnclosingTable();
//                    if (enclosingTable != null) { // may be null when called from Range.createContextualFragment
//                        enclosingTable.insertBefore(text);
//                    }
//                }
//                else {
//                    currentNode_.appendChild(text);
//                }
//            }
//        }
//    }

    /** {@inheritDoc} */
    public void endDocument() throws SAXException {
//        handleCharacters();
//        final DomNode currentPage = page_;
//        currentPage.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());
    }

    /** {@inheritDoc} */
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
    }

    /** {@inheritDoc} */
    public void endPrefixMapping(final String prefix) throws SAXException {
    }

    /** {@inheritDoc} */
    public void processingInstruction(final String target, final String data) throws SAXException {
    }

    /** {@inheritDoc} */
    public void skippedEntity(final String name) throws SAXException {
    }

    // LexicalHandler methods

    /** {@inheritDoc} */
    public void comment(final char[] ch, final int start, final int length) {
//        handleCharacters();
//        final String data = new String(ch, start, length);
//        if (!data.startsWith("[CDATA")
//                || page_.hasFeature(HTML_CDATA_AS_COMMENT)) {
//            final DomComment comment = new DomComment(page_, data);
//            currentNode_.appendChild(comment);
//        }
    }

    /** {@inheritDoc} */
    public void endCDATA() {
    }

    /** {@inheritDoc} */
    public void endDTD() {
    }

    /** {@inheritDoc} */
    public void endEntity(final String name) {
    }

    /** {@inheritDoc} */
    public void startCDATA() {
    }

    /** {@inheritDoc} */
    public void startDTD(final String name, final String publicId, final String systemId) {
//        final DomDocumentType type = new DomDocumentType(page_, name, publicId, systemId);
//        page_.setDocumentType(type);
//
//        final Node child;
//        if (page_.hasFeature(DOCTYPE_IS_COMMENT)) {
//            child = new DomComment(page_, "DOCTYPE " + name + " PUBLIC \""
//                    + publicId + "\"      \"" + systemId + '"');
//        }
//        else {
//            child = type;
//        }
//        page_.appendChild(child);
    }

    /** {@inheritDoc} */
    public void startEntity(final String name) {
    }

//    /**
//     * {@inheritDoc}
//     */
//    public void ignoredEndElement(final QName element, final Augmentations augs) {
//        // if real </form> is reached, don't accept fields anymore as lost children
//        if (formWaitingForLostChildren_ != null && "form".equals(element.localpart)) {
//            formWaitingForLostChildren_ = null;
//        }
//
//        if (parsingInnerHead_ && "head".equalsIgnoreCase(element.localpart)) {
//            parsingInnerHead_ = false;
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public void ignoredStartElement(final QName elem, final XMLAttributes attrs, final Augmentations augs) {
//        // when multiple body elements are encountered, the attributes of the discarded
//        // elements are used when not previously defined
//        if (body_ != null && "body".equalsIgnoreCase(elem.localpart) && attrs != null) {
//            // add the attributes that don't already exist
//            final int length = attrs.getLength();
//            for (int i = 0; i < length; ++i) {
//                final String attrName = attrs.getLocalName(i).toLowerCase(Locale.ENGLISH);
//                if (body_.getAttributes().getNamedItem(attrName) == null) {
//                    body_.setAttribute(attrName, attrs.getValue(i));
//                    if (attrName.startsWith("on") && body_.getScriptObject() != null) {
//                        final HTMLBodyElement jsBody = (HTMLBodyElement) body_.getScriptObject();
//                        jsBody.createEventHandlerFromAttribute(attrName, attrs.getValue(i));
//                    }
//                }
//            }
//        }
//
//        if (headParsed_ == HeadParsed.YES && "head".equalsIgnoreCase(elem.localpart)) {
//            parsingInnerHead_ = true;
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void parse(final XMLInputSource inputSource) throws XNIException, IOException {
//        final HtmlUnitDOMBuilder oldBuilder = page_.getBuilder();
//        page_.setBuilder(this);
//        try {
//            super.parse(inputSource);
//        }
//        finally {
//            page_.setBuilder(oldBuilder);
//        }
//    }

//    private boolean isSynthesized(final Augmentations augs) {
//        final HTMLEventInfo info = (augs == null) ? null
//                : (HTMLEventInfo) augs.getItem(FEATURE_AUGMENTATIONS);
//        return info != null ? info.isSynthesized() : false;
//    }
}
