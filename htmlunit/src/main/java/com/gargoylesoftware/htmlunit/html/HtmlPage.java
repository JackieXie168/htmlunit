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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection.JAVASCRIPT_PREFIX;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.OnbeforeunloadHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A representation of an HTML page returned from a server. This class is the
 * DOM Document implementation.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Alex Nikiforoff
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public final class HtmlPage extends SgmlPage implements Cloneable, Document {

    private static final long serialVersionUID = 1779746292119944291L;

    private String originalCharset_;
    private Map<String, List<HtmlElement>> idMap_ = new HashMap<String, List<HtmlElement>>();
    private Map<String, List<HtmlElement>> nameMap_ = new HashMap<String, List<HtmlElement>>();
    private HtmlElement documentElement_;
    private HtmlElement elementWithFocus_;
    private int parserCount_;

    private final transient Log javascriptLog_ = LogFactory.getLog("com.gargoylesoftware.htmlunit.javascript");

    private List<HtmlAttributeChangeListener> attributeListeners_;
    private final transient Object lock_ = new Object(); // used for synchronization

    /**
     * Create an instance of HtmlPage
     *
     * @param originatingUrl The URL that was used to load this page.
     * @param webResponse The web response that was used to create this page
     * @param webWindow The window that this page is being loaded into.
     */
    public HtmlPage(final URL originatingUrl, final WebResponse webResponse, final WebWindow webWindow) {
        super(webResponse, webWindow);
    }

    /**
     * @return this page
     */
    @Override
    public HtmlPage getPage() {
        return this;
    }

    /**
     * Initialize this page.
     * @throws IOException If an IO problem occurs.
     * @throws FailingHttpStatusCodeException If the server returns a failing status code AND the property
     * {@link WebClient#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     */
    @Override
    public void initialize() throws IOException, FailingHttpStatusCodeException {
        loadFrames();
        getDocumentHtmlElement().setReadyState(READY_STATE_COMPLETE);
        if (!getWebClient().getBrowserVersion().isIE()) {
            executeEventHandlersIfNeeded(Event.TYPE_DOM_DOCUMENT_LOADED);
        }
        executeDeferredScriptsIfNeeded();
        executeEventHandlersIfNeeded(Event.TYPE_LOAD);
        setReadyStateOnDeferredScriptsIfNeeded();
        executeRefreshIfNeeded();
    }

    /**
     * Clean up this page.
     * @throws IOException If an IO problem occurs.
     */
    @Override
    public void cleanUp() throws IOException {
        executeEventHandlersIfNeeded(Event.TYPE_UNLOAD);
        deregisterFramesIfNeeded();
    }

    /**
     * {@inheritDoc}
     */
    public Element getDocumentElement() {
        return getDocumentHtmlElement();
    }

    /**
     * Get the root HtmlElement of this document.
     * @return The root element
     */
    public HtmlElement getDocumentHtmlElement() {
        if (documentElement_ == null) {
            DomNode childNode = getFirstDomChild();
            while (childNode != null && !(childNode instanceof HtmlElement)) {
                childNode = childNode.getNextDomSibling();
            }
            documentElement_ = (HtmlElement) childNode;
        }
        return documentElement_;
    }

    /**
     * Returns the <tt>body</tt> element (or <tt>frameset</tt> element), or <tt>null</tt> if it does not yet exist.
     * @return the <tt>body</tt> element (or <tt>frameset</tt> element), or <tt>null</tt> if it does not yet exist
     */
    public HtmlElement getBody() {
        final HtmlElement doc = getDocumentHtmlElement();
        if (doc == null) {
            return null;
        }

        final List<String> tagNames = Arrays.asList(new String[] {"body", "frameset"});
        final List< ? extends HtmlElement> elements = doc.getHtmlElementsByTagNames(tagNames);
        if (elements.isEmpty()) {
            return null;
        }

        return elements.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document getOwnerDocument() {
        return null;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public org.w3c.dom.Node importNode(final org.w3c.dom.Node importedNode, final boolean deep) {
        throw new UnsupportedOperationException("HtmlPage.importNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public NodeList getElementsByTagName(final String tagName) {
        final HTMLCollection collection = new HTMLCollection(this);
        collection.init(this, "//" + tagName.toLowerCase());
        return collection;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public NodeList getElementsByTagNameNS(final String namespaceURI, final String localName) {
        throw new UnsupportedOperationException("HtmlPage.getElementsByTagNameNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public Element getElementById(final String elementId) {
        try {
            return getDocumentHtmlElement().getHtmlElementById(elementId);
        }
        catch (final ElementNotFoundException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getInputEncoding() {
        throw new UnsupportedOperationException("HtmlPage.getInputEncoding is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getXmlEncoding() {
        throw new UnsupportedOperationException("HtmlPage.getXmlEncoding is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean getXmlStandalone() {
        throw new UnsupportedOperationException("HtmlPage.getXmlStandalone is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setXmlStandalone(final boolean xmlStandalone) throws DOMException {
        throw new UnsupportedOperationException("HtmlPage.setXmlStandalone is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getXmlVersion() {
        throw new UnsupportedOperationException("HtmlPage.getXmlVersion is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setXmlVersion(final String xmlVersion) throws DOMException {
        throw new UnsupportedOperationException("HtmlPage.setXmlVersion is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean getStrictErrorChecking() {
        throw new UnsupportedOperationException("HtmlPage.getStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setStrictErrorChecking(final boolean strictErrorChecking) {
        throw new UnsupportedOperationException("HtmlPage.setStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getDocumentURI() {
        throw new UnsupportedOperationException("HtmlPage.getDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setDocumentURI(final String documentURI) {
        throw new UnsupportedOperationException("HtmlPage.setDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public org.w3c.dom.Node adoptNode(final org.w3c.dom.Node source) throws DOMException {
        throw new UnsupportedOperationException("HtmlPage.adoptNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public DOMConfiguration getDomConfig() {
        throw new UnsupportedOperationException("HtmlPage.getDomConfig is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void normalizeDocument() {
        throw new UnsupportedOperationException("HtmlPage.normalizeDocument is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public org.w3c.dom.Node renameNode(final org.w3c.dom.Node newNode, final String namespaceURI,
        final String qualifiedName) throws DOMException {
        throw new UnsupportedOperationException("HtmlPage.renameNode is not yet implemented.");
    }

    /**
     * Return the charset used in the page.
     * The sources of this information are from 1).meta element which
     * http-equiv attribute value is 'content-type', or if not from
     * the response header.
     * @return the value of charset.
     */
    public String getPageEncoding() {
        if (originalCharset_ != null) {
            return originalCharset_;
        }

        for (final HtmlMeta meta : getMetaTags("content-type")) {
            final String contents = meta.getContentAttribute();
            final int pos = contents.toLowerCase().indexOf("charset=");
            if (pos >= 0) {
                originalCharset_ = contents.substring(pos + 8);
                getLog().debug("Page Encoding detected: " + originalCharset_);
                return originalCharset_;
            }
        }
        if (originalCharset_ == null) {
            originalCharset_ = getWebResponse().getContentCharSet();
        }
        return originalCharset_;
    }

    /**
     * {@inheritDoc}
     */
    public Element createElement(final String tagName) {
        return createHtmlElement(tagName);
    }
    
    /**
     * Create a new HTML element with the given tag name.
     *
     * @param tagName The tag name, preferably in lowercase
     * @return the new HTML element.
     */
    public HtmlElement createHtmlElement(final String tagName) {
        final String tagLower = tagName.toLowerCase();
        return HTMLParser.getFactory(tagLower).createElement(this, tagLower, null);
    }

    /**
     * {@inheritDoc}
     */
    public Element createElementNS(final String namespaceURI, final String qualifiedName) {
        return createHtmlElementNS(namespaceURI, qualifiedName);
    }

    /**
     * Create a new HtmlElement with the given namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @return the new HTML element.
     */
    public HtmlElement createHtmlElementNS(final String namespaceURI, final String qualifiedName) {
        final String tagLower = qualifiedName.toLowerCase().substring(qualifiedName.indexOf(':') + 1);
        return HTMLParser.getFactory(tagLower).createElementNS(this, namespaceURI, qualifiedName, null);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr createAttributeNS(final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("HtmlPage.createAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr createAttribute(final String qualifiedName) {
        throw new UnsupportedOperationException("HtmlPage.createAttribute is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public Comment createComment(final String data) {
        return new DomComment(this, data);
    }

    /**
     * {@inheritDoc}
     */
    public Text createTextNode(final String data) {
        return new DomText(this, data);
    }

    /**
     * {@inheritDoc}
     */
    public CDATASection createCDATASection(final String data) {
        return new DomCData(this, data);
    }

    /**
     * {@inheritDoc}
     */
    public DocumentFragment createDocumentFragment() {
        return new DomDocumentFragment(this);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public DOMImplementation getImplementation() {
        throw new UnsupportedOperationException("HtmlPage.getImplementation is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public DocumentType getDoctype() {
        throw new UnsupportedOperationException("HtmlPage.getDoctype is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public EntityReference createEntityReference(final String id) {
        throw new UnsupportedOperationException("HtmlPage.createEntityReference is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public ProcessingInstruction createProcessingInstruction(final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("HtmlPage.createProcessingInstruction is not yet implemented.");
    }

    /**
     * Return the HtmlAnchor with the specified name
     *
     * @param name The name to search by
     * @return See above
     * @throws ElementNotFoundException If the anchor could not be found.
     */
    public HtmlAnchor getAnchorByName(final String name) throws ElementNotFoundException {
        return (HtmlAnchor) getDocumentHtmlElement().getOneHtmlElementByAttribute("a", "name", name);
    }

    /**
    *  Return the {@link HtmlAnchor} with the specified href
     *
     * @param href The string to search by
     * @return The HtmlAnchor
     * @throws ElementNotFoundException If the anchor could not be found.
     */
    public HtmlAnchor getAnchorByHref(final String href) throws ElementNotFoundException {
        return (HtmlAnchor) getDocumentHtmlElement().getOneHtmlElementByAttribute("a", "href", href);
    }

    /**
     * Return a list of all anchors contained in this page.
     * @return the list of {@link HtmlAnchor} in this page.
     */
    @SuppressWarnings("unchecked")
    public List<HtmlAnchor> getAnchors() {
        return (List<HtmlAnchor>) getDocumentHtmlElement().getHtmlElementsByTagName("a");
    }

    /**
     * Return the first anchor that contains the specified text.
     * @param text The text to search for
     * @return The first anchor that was found.
     * @throws ElementNotFoundException If no anchors are found with the specified text
     */
    public HtmlAnchor getFirstAnchorByText(final String text) throws ElementNotFoundException {
        WebAssert.notNull("text", text);

        for (final HtmlAnchor anchor : getAnchors()) {
            if (text.equals(anchor.asText())) {
                return anchor;
            }
        }
        throw new ElementNotFoundException("a", "<text>", text);
    }

    /**
     * Return the first form that matches the specified name
     * @param name The name to search for
     * @return The first form.
     * @exception ElementNotFoundException If no forms match the specified result.
     */
    public HtmlForm getFormByName(final String name) throws ElementNotFoundException {
        final List< ? extends HtmlElement> forms =
            getDocumentHtmlElement().getHtmlElementsByAttribute("form", "name", name);
        if (forms.size() == 0) {
            throw new ElementNotFoundException("form", "name", name);
        }
        else {
            return (HtmlForm) forms.get(0);
        }
    }

    /**
     * Return a list of all the forms in the page.
     * @return All the forms.
     */
    @SuppressWarnings("unchecked")
    public List<HtmlForm> getForms() {
        return (List<HtmlForm>) getDocumentHtmlElement().getHtmlElementsByTagName("form");
    }

    /**
     * Given a relative URL (ie /foo), return a fully qualified URL based on
     * the URL that was used to load this page
     *
     * @param relativeUrl The relative URL
     * @return See above
     * @exception MalformedURLException If an error occurred when creating a URL object
     */
    @SuppressWarnings("unchecked")
    public URL getFullyQualifiedUrl(String relativeUrl)
        throws MalformedURLException {

        final List<HtmlBase> baseElements = (List<HtmlBase>) getDocumentHtmlElement().getHtmlElementsByTagName("base");
        URL baseUrl;
        if (baseElements.isEmpty()) {
            baseUrl = getWebResponse().getUrl();
        }
        else {
            if (baseElements.size() > 1) {
                notifyIncorrectness("Multiple 'base' detected, only the first is used.");
            }
            final HtmlBase htmlBase = (HtmlBase) baseElements.get(0);
            boolean insideHead = false;
            for (DomNode parent = htmlBase.getParentDomNode(); parent != null; parent = parent.getParentDomNode()) {
                if (parent instanceof HtmlHead) {
                    insideHead = true;
                    break;
                }
            }
            
            //http://www.w3.org/TR/1999/REC-html401-19991224/struct/links.html#edef-BASE
            if (!insideHead) {
                notifyIncorrectness("Element 'base' must appear in <head>, it is ignored.");
            }
            
            final String href = htmlBase.getHrefAttribute();
            if (!insideHead || StringUtils.isEmpty(href)) {
                baseUrl = getWebResponse().getUrl();
            }
            else {
                try {
                    baseUrl = new URL(href);
                }
                catch (final MalformedURLException e) {
                    notifyIncorrectness("Invalid base url: \"" + href + "\", ignoring it");
                    baseUrl = getWebResponse().getUrl();
                }
            }
        }

        // to handle http: and http:/ in FF (Bug 1714767)
        if (getWebClient().getBrowserVersion().isNetscape()) {
            boolean incorrectnessNotified = false;
            while (relativeUrl.startsWith("http:") && !relativeUrl.startsWith("http://")) {
                if (!incorrectnessNotified) {
                    notifyIncorrectness("Incorrect URL \"" + relativeUrl + "\" has been corrected");
                    incorrectnessNotified = true;
                }
                relativeUrl = "http:/" + relativeUrl.substring(5);
            }
        }

        return WebClient.expandUrl(baseUrl, relativeUrl);
    }

    /**
     * Given a target attribute value, resolve the target using a base target for the page.
     *
     * @param elementTarget The target specified as an attribute of the element.
     * @return The resolved target to use for the element.
     */
    public String getResolvedTarget(final String elementTarget) {
        final List< ? extends HtmlElement> baseElements = getDocumentHtmlElement().getHtmlElementsByTagName("base");
        final String resolvedTarget;
        if (baseElements.isEmpty()) {
            resolvedTarget = elementTarget;
        }
        else if (elementTarget != null && elementTarget.length() > 0) {
            resolvedTarget = elementTarget;
        }
        else {
            final HtmlBase htmlBase = (HtmlBase) baseElements.get(0);
            resolvedTarget = htmlBase.getTargetAttribute();
        }
        return resolvedTarget;
    }

    /**
     * Return a list of ids (strings) that correspond to the tabbable elements
     * in this page. Return them in the same order specified in {@link #getTabbableElements}
     *
     * @return The list of id's
     */
    public List<String> getTabbableElementIds() {
        final List<String> list = new ArrayList<String>();

        for (final HtmlElement element : getTabbableElements()) {
            list.add(element.getAttributeValue("id"));
        }

        return Collections.unmodifiableList(list);
    }

   /**
    * Returns a list of all elements that are tabbable in the order that will
    * be used for tabbing.<p>
    *
    * The rules for determining tab order are as follows:
    * <ol>
    *   <li>Those elements that support the tabindex attribute and assign a
    *   positive value to it are navigated first. Navigation proceeds from the
    *   element with the lowest tabindex value to the element with the highest
    *   value. Values need not be sequential nor must they begin with any
    *   particular value. Elements that have identical tabindex values should
    *   be navigated in the order they appear in the character stream.
    *   <li>Those elements that do not support the tabindex attribute or
    *   support it and assign it a value of "0" are navigated next. These
    *   elements are navigated in the order they appear in the character
    *   stream.
    *   <li>Elements that are disabled do not participate in the tabbing
    *   order.
    * </ol>
    * Additionally, the value of tabindex must be within 0 and 32767. Any
    * values outside this range will be ignored.<p>
    *
    * The following elements support the <tt>tabindex</tt> attribute: A, AREA, BUTTON,
    * INPUT, OBJECT, SELECT, and TEXTAREA.<p>
    *
    * @return A list containing all the tabbable elements in proper tab order.
    */
    public List<HtmlElement> getTabbableElements() {
        final List<String> tags = Arrays
            .asList(new String[] {"a", "area", "button", "input", "object", "select", "textarea"});
        final List<HtmlElement> tabbableElements = new ArrayList<HtmlElement>();
        for (final HtmlElement element : getAllHtmlChildElements()) {
            final String tagName = element.getTagName();
            if (tags.contains(tagName)) {
                final boolean disabled = element.isAttributeDefined("disabled");
                if (!disabled && element.getTabIndex() != HtmlElement.TAB_INDEX_OUT_OF_BOUNDS) {
                    tabbableElements.add(element);
                }
            }
        }
        Collections.sort(tabbableElements, createTabOrderComparator());
        return Collections.unmodifiableList(tabbableElements);
    }

    private Comparator<HtmlElement> createTabOrderComparator() {
        return new Comparator<HtmlElement>() {
            public int compare(final HtmlElement element1, final HtmlElement element2) {
                final Short i1 = element1.getTabIndex();
                final Short i2 = element2.getTabIndex();

                final short index1;
                if (i1 != null) {
                    index1 = i1.shortValue();
                }
                else {
                    index1 = -1;
                }

                final short index2;
                if (i2 != null) {
                    index2 = i2.shortValue();
                }
                else {
                    index2 = -1;
                }

                final int result;
                if (index1 > 0 && index2 > 0) {
                    result = index1 - index2;
                }
                else if (index1 > 0) {
                    result = -1;
                }
                else if (index2 > 0) {
                    result = +1;
                }
                else if (index1 == index2) {
                    result = 0;
                }
                else {
                    result = index2 - index1;
                }

                return result;
            }
        };
    }

   /**
    * Returns the HTML element that is assigned to the specified access key. An
    * access key (aka mnemonic key) is used for keyboard navigation of the
    * page.<p>
    *
    * Only the following HTML elements may have <tt>accesskey</tt>s defined: A, AREA,
    * BUTTON, INPUT, LABEL, LEGEND, and TEXTAREA.
    *
    * @param accessKey The key to look for
    * @return The HTML element that is assigned to the specified key or null
    *      if no elements can be found that match the specified key.
    */
    public HtmlElement getHtmlElementByAccessKey(final char accessKey) {
        final List<HtmlElement> elements = getHtmlElementsByAccessKey(accessKey);
        if (elements.isEmpty()) {
            return null;
        }
        else {
            return elements.get(0);
        }
    }

   /**
    * Returns all the HTML elements that are assigned to the specified access key. An
    * access key (aka mnemonic key) is used for keyboard navigation of the
    * page.<p>
    *
    * The HTML specification seems to indicate that one accesskey cannot be used
    * for multiple elements however Internet Explorer does seem to support this.
    * It's worth noting that Mozilla does not support multiple elements with one
    * access key so you are making your HTML browser specific if you rely on this
    * feature.<p>
    *
    * Only the following HTML elements may have <tt>accesskey</tt>s defined: A, AREA,
    * BUTTON, INPUT, LABEL, LEGEND, and TEXTAREA.
    *
    * @param accessKey The key to look for
    * @return A list of HTML elements that are assigned to the specified accesskey.
    */
    public List<HtmlElement> getHtmlElementsByAccessKey(final char accessKey) {
        final List<HtmlElement> elements = new ArrayList<HtmlElement>();

        final String searchString = ("" + accessKey).toLowerCase();
        final List<String> acceptableTagNames = Arrays.asList(
                new String[]{"a", "area", "button", "input", "label", "legend", "textarea"});

        for (final HtmlElement element : getAllHtmlChildElements()) {
            if (acceptableTagNames.contains(element.getTagName())) {
                final String accessKeyAttribute = element.getAttributeValue("accesskey");
                if (searchString.equalsIgnoreCase(accessKeyAttribute)) {
                    elements.add(element);
                }
            }
        }

        return elements;
    }

    /**
     * Execute the specified JavaScript within the page.
     * The usage would be similar to what can be achieved to execute JavaScript in the current page
     * by entering a "javascript:...some js code..." in the URL field of a "normal" browser.
     * <p>
     * <b>Note: </b> the provided code won't be executed if JavaScript has been disabled on the WebClient
     * (see {@link WebClient#isJavaScriptEnabled()}.
     * @param sourceCode The JavaScript code to execute.
     * @return A ScriptResult which will contain both the current page (which may be different than
     * the previous page) and a JavaScript result object.
     */
    public ScriptResult executeJavaScript(final String sourceCode) {
        return executeJavaScriptIfPossible(sourceCode, "injected script", 1);
    }

    /**
     * <p>
     * Execute the specified JavaScript if a JavaScript engine was successfully
     * instantiated.  If this JavaScript causes the current page to be reloaded
     * (through location="" or form.submit()) then return the new page.  Otherwise
     * return the current page.
     * </p>
     * <p><b>Please note:</b> Although this method is public, it is not intended for
     * general execution of JavaScript.  Users of HtmlUnit should interact with the pages
     * as a user would by clicking on buttons or links and having the JavaScript event
     * handlers execute as needed..
     * </p>
     *
     * @param sourceCode The JavaScript code to execute.
     * @param sourceName The name for this chunk of code.  This name will be displayed
     * in any error messages.
     * @param htmlElement The HTML element for which this script is being executed.
     * This element will be the context during the JavaScript execution.  If null,
     * the context will default to the window.
     * @return A ScriptResult which will contain both the current page (which may be different than
     * the previous page and a JavaScript result object.
     * @deprecated use {@link #executeJavaScript(String)} instead
     */
    public ScriptResult executeJavaScriptIfPossible(final String sourceCode, final String sourceName,
        final HtmlElement htmlElement) {

        return executeJavaScriptIfPossible(sourceCode, sourceName, 1);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * <p>
     * Execute the specified JavaScript if a JavaScript engine was successfully
     * instantiated.  If this JavaScript causes the current page to be reloaded
     * (through location="" or form.submit()) then return the new page.  Otherwise
     * return the current page.
     * </p>
     * <p><b>Please note:</b> Although this method is public, it is not intended for
     * general execution of JavaScript.  Users of HtmlUnit should interact with the pages
     * as a user would by clicking on buttons or links and having the JavaScript event
     * handlers execute as needed..
     * </p>
     *
     * @param sourceCode The JavaScript code to execute.
     * @param sourceName The name for this chunk of code.  This name will be displayed
     * in any error messages.
     * @param startLine the line at which the script source starts
     * @return A ScriptResult which will contain both the current page (which may be different than
     * the previous page and a JavaScript result object.
     */
    public ScriptResult executeJavaScriptIfPossible(String sourceCode, final String sourceName, final int startLine) {
        if (!getWebClient().isJavaScriptEnabled()) {
            return new ScriptResult(null, this);
        }

        final int prefixLength = JAVASCRIPT_PREFIX.length();

        if (sourceCode.length() > prefixLength
                && sourceCode.substring(0, prefixLength).equalsIgnoreCase(JAVASCRIPT_PREFIX)) {
            sourceCode = sourceCode.substring(prefixLength);
        }

        final WebWindow window = getEnclosingWindow();
        getWebClient().pushClearFirstWindow();
        final Object result = getWebClient().getJavaScriptEngine().execute(this, sourceCode, sourceName, startLine);

        WebWindow firstWindow = getWebClient().popFirstWindow();
        if (firstWindow == null) {
            firstWindow = window;
        }

        return new ScriptResult(result, firstWindow.getEnclosedPage());
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Execute a Function in the given context.
     *
     * @param function The JavaScript Function to call.
     * @param thisObject The "this" object to be used during invocation.
     * @param args The arguments to pass into the call.
     * @param htmlElementScope The HTML element for which this script is being executed.
     * This element will be the context during the JavaScript execution.  If null,
     * the context will default to the page.
     * @return A ScriptResult which will contain both the current page (which may be different than
     * the previous page and a JavaScript result object.
     */
    public ScriptResult executeJavaScriptFunctionIfPossible(final Function function, final Scriptable thisObject,
            final Object[] args, final DomNode htmlElementScope) {

        final WebWindow window = getEnclosingWindow();
        getWebClient().pushClearFirstWindow();

        if (!getWebClient().isJavaScriptEnabled()) {
            return new ScriptResult(null, this);
        }

        final JavaScriptEngine engine = getWebClient().getJavaScriptEngine();
        final Object result = engine.callFunction(this, function, thisObject, args, htmlElementScope);

        WebWindow firstWindow = getWebClient().popFirstWindow();
        if (firstWindow == null) {
            firstWindow = window;
        }
        return new ScriptResult(result, firstWindow.getEnclosedPage());
    }

    /**
     * Return the log object for this element.
     * @return The log object for this element.
     */
    protected Log getJsLog() {
        return javascriptLog_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * @param srcAttribute The source attribute from the script tag.
     * @param charset The charset attribute from the script tag.
     */
    void loadExternalJavaScriptFile(final String srcAttribute, final String charset) {
        if (getWebClient().isJavaScriptEnabled()) {
            final URL scriptURL;
            try {
                scriptURL = getFullyQualifiedUrl(srcAttribute);
                if (scriptURL.getProtocol().equals("javascript")) {
                    getLog().info("Ignoring script src [" + srcAttribute + "]");
                    return;
                }
            }
            catch (final MalformedURLException e) {
                getLog().error("Unable to build URL for script src tag [" + srcAttribute + "]");
                if (getWebClient().isThrowExceptionOnScriptError()) {
                    throw new ScriptException(this, e);
                }
                return;
            }

            final Script script = loadJavaScriptFromUrl(scriptURL, charset);
            if (script != null) {
                getWebClient().getJavaScriptEngine().execute(this, script);
            }
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Return true if a script with the specified type and language attributes
     * is actually JavaScript.
     * According to <a href="http://www.w3.org/TR/REC-html40/types.html#h-6.7">W3C recommendation</a>
     * are content types case insensitive.
     * @param typeAttribute The type attribute specified in the script tag.
     * @param languageAttribute The language attribute specified in the script tag.
     * @return true if the script is JavaScript
     */
    public static boolean isJavaScript(final String typeAttribute, final String languageAttribute) {
        // Unless otherwise specified, we have to assume that any script is JavaScript
        final boolean isJavaScript;
        if (languageAttribute != null && languageAttribute.length() != 0) {
            isJavaScript = TextUtil.startsWithIgnoreCase(languageAttribute, "javascript");
        }
        else if (typeAttribute != null && typeAttribute.length() != 0) {
            isJavaScript = typeAttribute.equalsIgnoreCase("text/javascript");
        }
        else {
            isJavaScript = true;
        }

        return isJavaScript;
    }

    /**
     * Loads JavaScript from the specified URL. This method may return <tt>null</tt> if
     * there is a problem loading the code from the specified URL.
     *
     * @param url the URL of the script
     * @param charset the charset to use to read the text
     * @return the content of the file
     */
    private Script loadJavaScriptFromUrl(final URL url, final String charset) {
        String scriptEncoding = charset;
        getPageEncoding();

        WebResponse webResponse;
        try {
            final WebRequestSettings requestSettings = new WebRequestSettings(url);
            webResponse = getWebClient().loadWebResponse(requestSettings);
        }
        catch (final IOException e) {
            getLog().error("Error loading JavaScript from [" + url.toExternalForm() + "].", e);
            return null;
        }

        final JavaScriptEngine javaScriptEngine = getWebClient().getJavaScriptEngine();
        final Script cachedScript = javaScriptEngine.getCachedScript(webResponse);
        if (cachedScript != null) {
            return cachedScript;
        }

        getWebClient().printContentIfNecessary(webResponse);
        getWebClient().throwFailingHttpStatusCodeExceptionIfNecessary(webResponse);

        final int statusCode = webResponse.getStatusCode();
        final boolean successful = (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES);
        if (!successful) {
            return null;
        }

        final String contentType = webResponse.getContentType();
        if (!contentType.equalsIgnoreCase("text/javascript")
            && !contentType.equalsIgnoreCase("application/x-javascript")) {
            getLog().warn("Expected content type of 'text/javascript' or 'application/x-javascript' for "
                            + "remotely loaded JavaScript element at '" + url + "', "
                            + "but got '" + contentType + "'.");
        }

        if (StringUtils.isEmpty(scriptEncoding)) {
            final String contentCharset = webResponse.getContentCharSet();
            if (!contentCharset.equals(TextUtil.DEFAULT_CHARSET)) {
                scriptEncoding = contentCharset;
            }
            else if (!originalCharset_.equals(TextUtil.DEFAULT_CHARSET)) {
                scriptEncoding = originalCharset_;
            }
            else {
                scriptEncoding = TextUtil.DEFAULT_CHARSET;
            }
        }

        final byte[] data = webResponse.getResponseBody();
        final String scriptCode = EncodingUtil.getString(data, 0, data.length, scriptEncoding);
        
        final Script script = javaScriptEngine.compile(this, scriptCode, url.toExternalForm(), 1);
        javaScriptEngine.cacheScript(webResponse, script);
        return script;
    }

    /**
     * Return the title of this page or an empty string if the title wasn't specified.
     *
     * @return the title of this page or an empty string if the title wasn't specified.
     */
    public String getTitleText() {
        final HtmlTitle titleElement = getTitleElement();
        if (titleElement != null) {
            return titleElement.asText();
        }
        return "";
    }

    /**
     * Set the text for the title of this page.  If there is not a title element
     * on this page, then one has to be generated.
     * @param message The new text
     */
    public void setTitleText(final String message) {
        HtmlTitle titleElement = getTitleElement();
        if (titleElement == null) {
            getLog().debug("No title element, creating one");
            final HtmlHead head = (HtmlHead) getFirstChildElement(getDocumentHtmlElement(), HtmlHead.class);
            if (head == null) {
                // perhaps should we create head too?
                throw new IllegalStateException("Headelement was not defined for this page");
            }
            final Map<String, HtmlAttr> emptyMap = Collections.emptyMap();
            titleElement = new HtmlTitle(null, HtmlTitle.TAG_NAME, this, emptyMap);
            if (head.getFirstDomChild() != null) {
                head.getFirstDomChild().insertBefore(titleElement);
            }
            else {
                head.appendDomChild(titleElement);
            }
        }

        titleElement.setNodeValue(message);
    }

    /**
     * Get the first child of startElement that is an instance of the given class.
     * @param startElement The parent element
     * @param clazz The class to search for.
     * @return <code>null</code> if no child found
     */
    private HtmlElement getFirstChildElement(final HtmlElement startElement, final Class< ? > clazz) {
        for (final HtmlElement element : startElement.getChildElements()) {
            if (clazz.isInstance(element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Get the title element for this page.  Returns null if one is not found.
     *
     * @return the title element for this page or null if this is not one.
     */
    private HtmlTitle getTitleElement() {
        final HtmlHead head = (HtmlHead) getFirstChildElement(getDocumentHtmlElement(), HtmlHead.class);
        if (head != null) {
            return (HtmlTitle) getFirstChildElement(head, HtmlTitle.class);
        }

        return null;
    }

    /**
     * Look for and execute any appropriate event handlers.  Look for body
     * and frame tags.
     * @param eventType either {@link Event#TYPE_LOAD}, {@link Event#TYPE_UNLOAD}, or {@link Event#TYPE_BEFORE_UNLOAD}.
     * @return true if user accepted onbeforeunload (not relevant to other events).
     */
    @SuppressWarnings("unchecked")
    private boolean executeEventHandlersIfNeeded(final String eventType) {
        if (!getWebClient().isJavaScriptEnabled()) {
            return true;
        }

        final Window jsWindow = (Window) getEnclosingWindow().getScriptObject();
        if (jsWindow != null) {
            final HtmlElement element = getDocumentHtmlElement();
            final Event event = new Event(element, eventType);
            element.fireEvent(event);
            if (!isOnbeforeunloadAccepted(this, event)) {
                return false;
            }
        }

        // the event of the contained frames or iframe tags
        final List<BaseFrame> frames = (List<BaseFrame>)
            getDocumentHtmlElement().getHtmlElementsByTagNames(Arrays.asList(new String[]{"frame", "iframe" }));
        for (final BaseFrame frame : frames) {
            final Function frameTagEventHandler = frame.getEventHandler("on" + eventType);
            if (frameTagEventHandler != null) {
                getLog().debug("Executing on" + eventType + " handler for " + frame);
                final Event event = new Event(frame, eventType);
                ((Node) frame.getScriptObject()).executeEvent(event);
                if (!isOnbeforeunloadAccepted(frame.getPage(), event)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isOnbeforeunloadAccepted(final HtmlPage page, final Event event) {
        if (event.jsxGet_type().equals(Event.TYPE_BEFORE_UNLOAD) && event.jsxGet_returnValue() != null) {
            final OnbeforeunloadHandler handler = getWebClient().getOnbeforeunloadHandler();
            if (handler == null) {
                getLog().warn("document.onbeforeunload() returned a string in event.returnValue,"
                        + " but no onbeforeunload handler installed.");
            }
            else {
                final String message = Context.toString(event.jsxGet_returnValue());
                return handler.handleEvent(page, message);
            }
        }
        return true;
    }
    
    /**
     * If a refresh has been specified either through a meta tag or an http
     * response header, then perform that refresh.
     * @throws IOException if an IO problem occurs
     */
    private void executeRefreshIfNeeded() throws IOException {
        // If this page is not in a frame then a refresh has already happened,
        // most likely through the JavaScript onload handler, so we don't do a
        // second refresh.
        final WebWindow window = getEnclosingWindow();
        if (window == null) {
            return;
        }

        final String refreshString = getRefreshStringOrNull();
        if (refreshString == null || refreshString.length() == 0) {
            return;
        }

        final int time;
        final URL url;

        int index = refreshString.indexOf(";");
        final boolean timeOnly = (index == -1);

        if (timeOnly) {
            // Format: <meta http-equiv='refresh' content='10'>
            try {
                time = Integer.parseInt(refreshString);
            }
            catch (final NumberFormatException e) {
                getLog().error("Malformed refresh string (no ';' but not a number): " + refreshString, e);
                return;
            }
            url = getWebResponse().getUrl();
        }
        else {
            // Format: <meta http-equiv='refresh' content='10;url=http://www.blah.com'>
            try {
                time = Integer.parseInt(refreshString.substring(0, index).trim());
            }
            catch (final NumberFormatException e) {
                getLog().error("Malformed refresh string (no valid number before ';') " + refreshString, e);
                return;
            }
            index = refreshString.indexOf("URL=", index);
            if (index == -1) {
                index = refreshString.indexOf("url=", index);
            }
            if (index == -1) {
                getLog().error("Malformed refresh string (found ';' but no 'url='): " + refreshString);
                return;
            }
            final StringBuilder buffer = new StringBuilder(refreshString.substring(index + 4));
            if (buffer.toString().trim().length() == 0) {
                //content='10; URL=' is treated as content='10'
                url = getWebResponse().getUrl();
            }
            else {
                if (buffer.charAt(0) == '"' || buffer.charAt(0) == 0x27) {
                    buffer.deleteCharAt(0);
                }
                if (buffer.charAt(buffer.length() - 1) == '"' || buffer.charAt(buffer.length() - 1) == 0x27) {
                    buffer.deleteCharAt(buffer.length() - 1);
                }
                final String urlString = buffer.toString();
                try {
                    url = getFullyQualifiedUrl(urlString);
                }
                catch (final MalformedURLException e) {
                    getLog().error("Malformed URL in refresh string: " + refreshString, e);
                    throw e;
                }
            }
        }

        getWebClient().getRefreshHandler().handleRefresh(this, url, time);
    }

    /**
     * Return an auto-refresh string if specified.  This will look in both the meta
     * tags (taking care of &lt;noscript&gt; if any) and inside the http response headers.
     * @return the auto-refresh string.
     */
    private String getRefreshStringOrNull() {
        final boolean javaScriptEnabled = getWebClient().isJavaScriptEnabled();
        for (final HtmlMeta meta : getMetaTags("refresh")) {
            if ((!javaScriptEnabled || getFirstParent(meta, HtmlNoScript.TAG_NAME) == null)) {
                return meta.getContentAttribute();
            }
        }
        return getWebResponse().getResponseHeaderValue("Refresh");
    }

    /**
     * Executes any deferred scripts, if necessary.
     */
    @SuppressWarnings("unchecked")
    private void executeDeferredScriptsIfNeeded() {
        if (!getWebClient().isJavaScriptEnabled()) {
            return;
        }
        if (!getWebClient().getBrowserVersion().isIE()) {
            return;
        }
        final HtmlElement doc = getDocumentHtmlElement();
        for (final HtmlScript script : (List<HtmlScript>) doc.getHtmlElementsByTagName("script")) {
            if (script.isDeferred()) {
                script.executeScriptIfNeeded(true);
            }
        }
    }

    /**
     * Sets the ready state on any deferred scripts, if necessary.
     */
    @SuppressWarnings("unchecked")
    private void setReadyStateOnDeferredScriptsIfNeeded() {
        if (!getWebClient().isJavaScriptEnabled()) {
            return;
        }
        if (!getWebClient().getBrowserVersion().isIE()) {
            return;
        }
        final HtmlElement doc = getDocumentHtmlElement();
        for (final HtmlScript script : (List<HtmlScript>) doc.getHtmlElementsByTagName("script")) {
            if (script.isDeferred()) {
                script.setReadyStateComplete();
            }
        }
    }

    /**
     * Returns the first parent with the specified node name, or <tt>null</tt> if no parent
     * with the specified node name can be found.
     *
     * @param node the node to start with
     * @param nodeName the name of the search node
     * @return the first parent with the specified node name
     */
    private DomNode getFirstParent(final DomNode node, final String nodeName) {
        DomNode parent = node.getParentDomNode();
        while (parent != null) {
            if (parent.getNodeName().equals(nodeName)) {
                return parent;
            }
            parent = parent.getParentDomNode();
        }
        return null;
    }

    /**
     * Deregister frames that are no longer in use.
     */
    public void deregisterFramesIfNeeded() {
        for (final WebWindow window : getFrames()) {
            getWebClient().deregisterWebWindow(window);
            if (window.getEnclosedPage() instanceof HtmlPage) {
                final HtmlPage page = (HtmlPage) window.getEnclosedPage();
                if (page != null) {
                    // seems quite silly, but for instance if the src attribute of an iframe is not
                    // set, the error only occurs when leaving the page
                    page.deregisterFramesIfNeeded();
                }
            }
        }
    }

    /**
     * Return a list containing all the frames (from frame and iframe tags) in this page.
     * @return a list of {@link FrameWindow}
     */
    public List<FrameWindow> getFrames() {
        final List<FrameWindow> list = new ArrayList<FrameWindow>();
        final WebWindow enclosingWindow = getEnclosingWindow();
        for (final WebWindow window : getWebClient().getWebWindows()) {
            // quite strange but for a TopLevelWindow parent == self
            if (enclosingWindow == window.getParentWindow()
                    && enclosingWindow != window) {
                list.add((FrameWindow) window);
            }
        }
        return list;
    }

    /**
     * Returns the first frame contained in this page with the specified name.
     * @param name The name to search for
     * @return The first frame found.
     * @exception ElementNotFoundException If no frame exist in this page with the specified name.
     */
    public FrameWindow getFrameByName(final String name) throws ElementNotFoundException {
        for (final FrameWindow frame : getFrames()) {
            if (frame.getName().equals(name)) {
                return frame;
            }
        }

        throw new ElementNotFoundException("frame or iframe", "name", name);
    }

    /**
     * Simulate pressing an access key.  This may change the focus, may click buttons and may invoke
     * JavaScript.
     *
     * @param accessKey The key that will be pressed.
     * @return The element that has the focus after pressing this access key or null if no element
     * has the focus.
     * @throws IOException If an io error occurs during the processing of this access key.  This
     * would only happen if the access key triggered a button which in turn caused a page load.
     */
    public HtmlElement pressAccessKey(final char accessKey) throws IOException {
        final HtmlElement element = getHtmlElementByAccessKey(accessKey);
        if (element != null) {
            element.focus();
            final Page newPage;
            if (element instanceof HtmlAnchor) {
                newPage = ((HtmlAnchor) element).click();
            }
            else if (element instanceof HtmlArea) {
                newPage = ((HtmlArea) element).click();
            }
            else if (element instanceof HtmlButton) {
                newPage = ((HtmlButton) element).click();
            }
            else if (element instanceof HtmlInput) {
                newPage = ((HtmlInput) element).click();
            }
            else if (element instanceof HtmlLabel) {
                newPage = ((HtmlLabel) element).click();
            }
            else if (element instanceof HtmlLegend) {
                newPage = ((HtmlLegend) element).click();
            }
            else if (element instanceof HtmlTextArea) {
                newPage = ((HtmlTextArea) element).click();
            }
            else {
                newPage = this;
            }

            if (newPage != this && getElementWithFocus() == element) {
                // The page was reloaded therefore no element on this page will have the focus.
                getElementWithFocus().blur();
            }
        }

        return getElementWithFocus();
    }

    /**
     * Move the focus to the next element in the tab order.  To determine the specified tab
     * order, refer to {@link HtmlPage#getTabbableElements()}
     *
     * @return The element that has focus after calling this method.
     */
    public HtmlElement tabToNextElement() {
        final List<HtmlElement> elements = getTabbableElements();
        if (elements.isEmpty()) {
            moveFocusToElement(null);
            return null;
        }

        final HtmlElement elementToGiveFocus;
        final HtmlElement elementWithFocus = getElementWithFocus();
        if (elementWithFocus == null) {
            elementToGiveFocus = elements.get(0);
        }
        else {
            final int index = elements.indexOf(elementWithFocus);
            if (index == -1) {
                // The element with focus isn't on this page
                elementToGiveFocus = elements.get(0);
            }
            else {
                if (index == elements.size() - 1) {
                    elementToGiveFocus = elements.get(0);
                }
                else {
                    elementToGiveFocus = elements.get(index + 1);
                }
            }
        }

        moveFocusToElement(elementToGiveFocus);
        return elementToGiveFocus;
    }

    /**
     * Move the focus to the previous element in the tab order.  To determine the specified tab
     * order, refer to {@link HtmlPage#getTabbableElements()}
     *
     * @return The element that has focus after calling this method.
     */
    public HtmlElement tabToPreviousElement() {
        final List<HtmlElement> elements = getTabbableElements();
        if (elements.isEmpty()) {
            moveFocusToElement(null);
            return null;
        }

        final HtmlElement elementToGiveFocus;
        final HtmlElement elementWithFocus = getElementWithFocus();
        if (elementWithFocus == null) {
            elementToGiveFocus = elements.get(elements.size() - 1);
        }
        else {
            final int index = elements.indexOf(elementWithFocus);
            if (index == -1) {
                // The element with focus isn't on this page
                elementToGiveFocus = elements.get(elements.size() - 1);
            }
            else {
                if (index == 0) {
                    elementToGiveFocus = elements.get(elements.size() - 1);
                }
                else {
                    elementToGiveFocus = elements.get(index - 1);
                }
            }
        }

        moveFocusToElement(elementToGiveFocus);
        return elementToGiveFocus;
    }

    /**
     * Returns the HTML element with the specified ID. If more than one element
     * has this ID (not allowed by the HTML spec), then this method returns the
     * first one.
     *
     * @param id the ID value to search by
     * @return the HTML element with the specified ID
     * @throws ElementNotFoundException if no element was found that matches the id
     */
    public HtmlElement getHtmlElementById(final String id) throws ElementNotFoundException {
        final List<HtmlElement> elements = (List<HtmlElement>) idMap_.get(id);
        if (elements != null) {
            return elements.get(0);
        }
        throw new ElementNotFoundException("*", "id", id);
    }

    /**
     * Returns the HTML elements with the specified name attribute. If there are no elements
     * with the specified name, this method returns an empty list. Please note that
     * the lists returned by this method are immutable.
     *
     * @param name the name value to search by
     * @return the HTML elements with the specified name attribute
     */
    public List<HtmlElement> getHtmlElementsByName(final String name) {
        final List<HtmlElement> list = (List<HtmlElement>) nameMap_.get(name);
        if (list != null) {
            return Collections.unmodifiableList(list);
        }
        else {
            return Collections.emptyList();
        }
    }

    /**
     * Returns the HTML elements with the specified string for their name or ID. If there are
     * no elements with the specified name or ID, this method returns an empty list. Please note
     * that lists returned by this method are immutable.
     *
     * @param idAndOrName the value to search for
     * @return the HTML elements with the specified string for their name or ID
     */
    public List<HtmlElement> getHtmlElementsByIdAndOrName(final String idAndOrName) {
        final List<HtmlElement> list1 = (List<HtmlElement>) idMap_.get(idAndOrName);
        final List<HtmlElement> list2 = (List<HtmlElement>) nameMap_.get(idAndOrName);
        final List<HtmlElement> list = new ArrayList<HtmlElement>();
        if (list1 != null) {
            list.addAll(list1);
        }
        if (list2 != null) {
            list.addAll(list2);
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Adds an element to the ID and name maps, if necessary.
     * @param element the element to be added to the ID and name maps
     */
    void addMappedElement(final HtmlElement element) {
        addMappedElement(element, false);
    }

    /**
     * Adds an element to the ID and name maps, if necessary.
     * @param element the element to be added to the ID and name maps
     * @param recurse indicates if children must be added too
     */
    void addMappedElement(final HtmlElement element, final boolean recurse) {
        if (isDescendant(element)) {
            addElement(idMap_, element, "id", recurse);
            addElement(nameMap_, element, "name", recurse);
        }
    }

    /**
     * Checks whether the specified element is descendant of this HtmlPage or not.
     */
    private boolean isDescendant(final HtmlElement element) {
        for (DomNode parent = element; parent != null; parent = parent.getParentDomNode()) {
            if (parent == this) {
                return true;
            }
        }
        return false;
    }
    
    private void addElement(final Map<String, List<HtmlElement>> map, final HtmlElement element,
            final String attribute, final boolean recurse) {
        final String value = element.getAttributeValue(attribute);
        if (!StringUtils.isEmpty(value)) {
            List<HtmlElement> elements = map.get(value);
            if (elements == null) {
                elements = new ArrayList<HtmlElement>();
                elements.add(element);
                map.put(value, elements);
            }
            else if (!elements.contains(element)) {
                elements.add(element);
            }
        }
        if (recurse) {
            for (final HtmlElement child : element.getChildElements()) {
                addElement(map, child, attribute, true);
            }
        }
    }

    /**
     * Removes an element from the ID and name maps, if necessary.
     * @param element the element to be removed from the ID and name maps
     */
    void removeMappedElement(final HtmlElement element) {
        removeMappedElement(element, false, false);
    }

    /**
     * Removes an element and optionally its children from the ID and name maps, if necessary.
     * @param element the element to be removed from the ID and name maps
     * @param recurse indicates if children must be removed too
     * @param descendant indicates of the element was descendant of this HtmlPage, but now its parent might be null.
     */
    void removeMappedElement(final HtmlElement element, final boolean recurse, final boolean descendant) {
        if (descendant || isDescendant(element)) {
            removeElement(idMap_, element, "id", recurse);
            removeElement(nameMap_, element, "name", recurse);
        }
    }

    private void removeElement(final Map<String, List<HtmlElement>> map, final HtmlElement element, final String att,
            final boolean recurse) {
        final String value = element.getAttributeValue(att);
        if (!StringUtils.isEmpty(value)) {
            final List<HtmlElement> elements = map.remove(value);
            if (elements != null && elements.size() != 1) {
                elements.remove(element);
                map.put(value, elements);
            }
        }
        if (recurse) {
            for (final HtmlElement child : element.getChildElements()) {
                removeElement(map, child, att, true);
            }
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * @param node the node that has just been added to the document.
     */
    void notifyNodeAdded(final DomNode node) {
        if (node instanceof HtmlElement) {
            boolean insideNoScript = false;
            if (getWebClient().isJavaScriptEnabled()) {
                for (DomNode parent = node.getParentDomNode(); parent != null; parent = parent.getParentDomNode()) {
                    if (parent instanceof HtmlNoScript) {
                        insideNoScript = true;
                        break;
                    }
                }
            }
            if (!insideNoScript) {
                addMappedElement((HtmlElement) node, true);
            }
        }
        node.onAddedToPage();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * @param node the node that has just been removed from the tree
     */
    void notifyNodeRemoved(final DomNode node) {
        if (node instanceof HtmlElement) {
            removeMappedElement((HtmlElement) node, true, true);
        }
    }

    /**
     * Loads the content of the contained frames. This is done after the page is completely
     * loaded to allow script contained in the frames to reference elements from the
     * page located after the closing </frame> tag.
     * @throws FailingHttpStatusCodeException If the server returns a failing status code AND the property
     * {@link WebClient#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     */
    void loadFrames() throws FailingHttpStatusCodeException {
        final List<String> frameTags = Arrays.asList(new String[] {"frame", "iframe"});
        final List< ? extends HtmlElement> frames = getDocumentHtmlElement().getHtmlElementsByTagNames(frameTags);
        for (final HtmlElement element : frames) {
            final BaseFrame frame = (BaseFrame) element;
            // test if the frame should really be loaded:
            // if a script has already changed its content, it should be skipped
            // use == and not equals(...) to identify initial content (versus URL set to "about:blank")
            if (frame.getEnclosedPage().getWebResponse().getUrl() == WebClient.URL_ABOUT_BLANK) {
                frame.loadInnerPage();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asXml() {
        return getDocumentHtmlElement().asXml();
    }

    /**
     * Gives a basic representation for debugging purposes
     * @return a basic representation
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("HtmlPage(");
        buffer.append(getWebResponse().getUrl());
        buffer.append(")@");
        buffer.append(hashCode());
        return buffer.toString();
    }

    /**
     * Move the focus to the specified component. This will trigger any relevant JavaScript
     * event handlers.
     *
     * @param newElement The element that will receive the focus, use <code>null</code> to remove focus from any element
     * @return true if the specified element now has the focus.
     * @see #getElementWithFocus()
     * @see #tabToNextElement()
     * @see #tabToPreviousElement()
     * @see #pressAccessKey(char)
     * @see WebAssert#assertAllTabIndexAttributesSet(HtmlPage)
     */
    public boolean moveFocusToElement(final HtmlElement newElement) {
        if (elementWithFocus_ == newElement) {
            // nothing to do
            return true;
        }
        else if (newElement != null && newElement.getPage() != this) {
            throw new IllegalArgumentException("Can't move focus to an element from an other page");
        }

        if (elementWithFocus_ != null) {
            elementWithFocus_.fireEvent(Event.TYPE_BLUR);
        }

        elementWithFocus_ = newElement;
        if (newElement != null) {
            newElement.fireEvent(Event.TYPE_FOCUS);
        }

        // If a page reload happened as a result of the focus change then obviously this
        // element will not have the focus because its page has gone away.
        return this == getEnclosingWindow().getEnclosedPage();
    }

    /**
     * Return the element with the focus or null if no element has the focus.
     * @return The element with focus or null.
     * @see #moveFocusToElement(HtmlElement)
     */
    public HtmlElement getElementWithFocus() {
        return elementWithFocus_;
    }

    /**
     * Gets the meta tag for a given http-equiv value.
     * @param httpEquiv the http-equiv value
     * @return a list of {@link HtmlMeta}
     */
    @SuppressWarnings("unchecked")
    protected List<HtmlMeta> getMetaTags(final String httpEquiv) {
        final String nameLC = httpEquiv.toLowerCase();
        final List<HtmlMeta> tags = (List<HtmlMeta>) getDocumentHtmlElement().getHtmlElementsByTagName("meta");
        for (final Iterator<HtmlMeta> iter = tags.iterator(); iter.hasNext();) {
            final HtmlMeta element = iter.next();
            if (!nameLC.equals(element.getHttpEquivAttribute().toLowerCase())) {
                iter.remove();
            }
        }
        return tags;
    }

    /**
     * Select the specified radio button in the page (outside any &lt;form&gt;).
     *
     * @param radioButtonInput The radio Button
     */
    @SuppressWarnings("unchecked")
    void setCheckedRadioButton(final HtmlRadioButtonInput radioButtonInput) {
        //May be done in single xpath search?
        final List<HtmlRadioButtonInput> pageInputs =
            (List<HtmlRadioButtonInput>) getByXPath("//input[lower-case(@type)='radio' "
                + "and @name='" + radioButtonInput.getNameAttribute() + "']");
        final List<HtmlRadioButtonInput> formInputs =
            (List<HtmlRadioButtonInput>) getByXPath("//form//input[lower-case(@type)='radio' "
                + "and @name='" + radioButtonInput.getNameAttribute() + "']");
            
        pageInputs.removeAll(formInputs);

        for (final HtmlRadioButtonInput input : pageInputs) {
            if (input == radioButtonInput) {
                input.setAttributeValue("checked", "checked");
            }
            else {
                input.removeAttribute("checked");
            }
        }
    }

    /**
     * Creates a clone of this instance, and clears cached state
     * to be not shared with the original.
     *
     * @return a clone of this instance.
     */
    @Override
    protected Object clone() {
        try {
            final HtmlPage result = (HtmlPage) super.clone();
            result.documentElement_ = null;
            result.elementWithFocus_ = null;
            result.idMap_ = new HashMap<String, List<HtmlElement>>();
            result.nameMap_ = new HashMap<String, List<HtmlElement>>();
            return result;
        }
        catch (final CloneNotSupportedException e) {
            throw new IllegalStateException("Clone not supported");
        }
    }

    /**
     * {@inheritDoc}
     * Override cloneNode to add cloned elements to the clone, not to the original.
     */
    @Override
    public org.w3c.dom.Node cloneNode(final boolean deep) {
        final HtmlPage result = (HtmlPage) super.cloneDomNode(deep);
        if (deep) {
            // fix up idMap_ and result's idMap_s
            for (final HtmlElement child : result.getAllHtmlChildElements()) {
                removeMappedElement(child);
                result.addMappedElement(child);
            }
        }
        return result;
    }

    /**
     * Adds an HtmlAttributeChangeListener to the listener list.
     * The listener is registered for all attributes of all HtmlElements contained in this page.
     *
     * @param listener the attribute change listener to be added.
     * @see #removeHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void addHtmlAttributeChangeListener(final HtmlAttributeChangeListener listener) {
        WebAssert.notNull("listener", listener);
        synchronized (lock_) {
            if (attributeListeners_ == null) {
                attributeListeners_ = new ArrayList<HtmlAttributeChangeListener>();
            }
            if (!attributeListeners_.contains(listener)) {
                attributeListeners_.add(listener);
            }
        }
    }

    /**
     * Removes an HtmlAttributeChangeListener from the listener list.
     * This method should be used to remove HtmlAttributeChangeListener that were registered
     * for all attributes of all HtmlElements contained in this page.
     *
     * @param listener the attribute change listener to be removed.
     * @see #addHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void removeHtmlAttributeChangeListener(final HtmlAttributeChangeListener listener) {
        WebAssert.notNull("listener", listener);
        synchronized (lock_) {
            if (attributeListeners_ != null) {
                attributeListeners_.remove(listener);
            }
        }
    }
    
    /**
     * Notifies all registered listeners for the given event to add an attribute.
     * @param event the event to fire
     */
    void fireHtmlAttributeAdded(final HtmlAttributeChangeEvent event) {
        final List<HtmlAttributeChangeListener> listeners = safeGetAttributeListeners();
        if (listeners != null) {
            for (final HtmlAttributeChangeListener listener : listeners) {
                listener.attributeAdded(event);
            }
        }
    }

    /**
     * Notifies all registered listeners for the given event to replace an attribute.
     * @param event the event to fire
     */
    void fireHtmlAttributeReplaced(final HtmlAttributeChangeEvent event) {
        final List<HtmlAttributeChangeListener> listeners = safeGetAttributeListeners();
        if (listeners != null) {
            for (final HtmlAttributeChangeListener listener : listeners) {
                listener.attributeReplaced(event);
            }
        }
    }
    
    /**
     * Notifies all registered listeners for the given event to remove an attribute.
     * @param event the event to fire
     */
    void fireHtmlAttributeRemoved(final HtmlAttributeChangeEvent event) {
        final List<HtmlAttributeChangeListener> listeners = safeGetAttributeListeners();
        if (listeners != null) {
            for (final HtmlAttributeChangeListener listener : listeners) {
                listener.attributeRemoved(event);
            }
        }
    }

    private List<HtmlAttributeChangeListener> safeGetAttributeListeners() {
        synchronized (lock_) {
            if (attributeListeners_ != null) {
                return new ArrayList<HtmlAttributeChangeListener>(attributeListeners_);
            }
            else {
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkChildHierarchy(final org.w3c.dom.Node newChild) throws DOMException {
        if (newChild instanceof Element) {
            if (getDocumentElement() != null) {
                throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                    "The Document may only have a single child Element.");
            }
        }
        else if (newChild instanceof DocumentType) {
            if (getDoctype() != null) {
                throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                    "The Document may only have a single child DocumentType.");
            }
        }
        else if (!((newChild instanceof Comment) || (newChild instanceof ProcessingInstruction))) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "The Document may not have a child of this type: " + newChild.getNodeType());
        }
        super.checkChildHierarchy(newChild);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * @return true if the OnbeforeunloadHandler has accepted to change the page.
     */
    public boolean isOnbeforeunloadAccepted() {
        return executeEventHandlersIfNeeded(Event.TYPE_BEFORE_UNLOAD);
    }

    /**
     * Returns <tt>true</tt> if an HTML parser is operating on this page, adding content to it.
     * @return <tt>true</tt> if an HTML parser is operating on this page, adding content to it
     */
    public boolean isBeingParsed() {
        return parserCount_ > 0;
    }

    /**
     * Called by the HTML parser to let the page know that it has started parsing some content for this page.
     */
    void registerParsingStart() {
        parserCount_++;
    }

    /**
     * Called by the HTML parser to let the page know that it has finished parsing some content for this page.
     */
    void registerParsingEnd() {
        parserCount_--;
    }

}
