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
package com.gargoylesoftware.htmlunit.javascript;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.NOPTransformer;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.saxpath.SAXPathException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.xml.XmlAttr;
import com.gargoylesoftware.htmlunit.xml.XmlElement;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * An array of elements. Used for the element arrays returned by <tt>document.all</tt>,
 * <tt>document.all.tags('x')</tt>, <tt>document.forms</tt>, <tt>window.frames</tt>, etc.
 * Note that this class must not be used for collections that can be modified, for example
 * <tt>map.areas</tt> and <tt>select.options</tt>.
 * <br>
 * This class (like all classes in this package) is specific for the javascript engine.
 * Users of HtmlUnit shouldn't use it directly.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLCollection extends SimpleScriptable implements Function {
    private static final long serialVersionUID = 4049916048017011764L;

    private XPath xpath_;
    private DomNode node_;

    /**
     * The transformer used to get the element to return from the html element.
     * It returns the html element itself except for frames where it returns the nested window.
     */
    private Transformer transformer_;
    
    private List cachedElements_;

    /**
     * Create an instance. Javascript objects must have a default constructor.
     */
    public HTMLCollection() {
        // nothing
    }

    /**
     * Create an instance
     * @param parentScope parent scope
     */
    public HTMLCollection(final SimpleScriptable parentScope) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Init the content of this collection. The elements will be "calculated" at each
     * access using the xpath applied on the node.
     * @param node the node to serve as root for the xpath expression
     * @param xpath the xpath giving the elements of the collection
     */
    public void init(final DomNode node, final XPath xpath) {
        init(node, xpath, NOPTransformer.INSTANCE);
    }

    /**
     * Init the content of this collection. The elements will be "calculated" at each
     * access using the xpath applied on the node and transformed using the transformer.
     * @param node the node to serve as root for the xpath expression
     * @param xpath the xpath giving the elements of the collection
     * @param transformer the transformer allowing to get the expected objects from the xpath
     * evaluation
     */
    public void init(final DomNode node, final XPath xpath, final Transformer transformer) {
        if (node != null) {
            node_ = node;
            xpath_ = xpath;
            try {
                if (node instanceof XmlPage) {
                    final XmlElement documentElement = ((XmlPage) node).getDocumentXmlElement();
                    if (documentElement != null) {
                        addNamespace(documentElement);
                    }
                }
                else if (node instanceof XmlElement) {
                    addNamespace((XmlElement) node);
                }
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Exception adding namespaces: " + e);
            }
            transformer_ = transformer;
            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            node_.addDomChangeListener(listener);
            if (node_ instanceof HtmlElement) {
                ((HtmlElement) node_).addHtmlAttributeChangeListener(listener);
                cachedElements_ = null;
            }
        }
    }

    /**
     * Adds all namespaces defined in the given element and its all descendants to the XPath.
     * @param element root element
     * @throws JaxenException if a <code>NamespaceContext</code>
     *         used by the XPath has been explicitly installed
     */
    private void addNamespace(final XmlElement element) throws JaxenException {
        final Map attributes = element.getAttributes();
        for (final Iterator keys = attributes.keySet().iterator(); keys.hasNext();) {
            final String name = (String) keys.next();
            final String value = (String) ((XmlAttr) attributes.get(name)).getValue();
            if (name.startsWith("xmlns:")) {
                final String prefix = name.substring("xmlns:".length());
                xpath_.addNamespace(prefix, value);
            }
        }
        for (final Iterator children = element.getChildIterator(); children.hasNext();) {
            final DomNode child = (DomNode) children.next();
            if (child instanceof XmlElement) {
                addNamespace((XmlElement) child);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public final Object call(
            final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args)
        throws JavaScriptException {
        if (args.length == 0) {
            throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
        }
        final Object response = get(args[0]);
        if (response == NOT_FOUND) {
            return null;
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    public final Scriptable construct(
            final Context arg0, final Scriptable arg1, final Object[] arg2)
        throws JavaScriptException {
        return null;
    }

    /**
     * Private helper that retrieves the item or items corresponding to the specified
     * index or key.
     * @param o The index or key corresponding to the element or elements to return.
     * @return The element or elements corresponding to the specified index or key.
     */
    private Object get(final Object o) {
        if (o instanceof Number) {
            final Number n = (Number) o;
            final int i = n.intValue();
            return get(i, this);
        }
        else {
            final String key = String.valueOf(o);
            return get(key, this);
        }
    }

    /**
     * Returns the element at the specified index, or <tt>NOT_FOUND</tt> if the
     * index is invalid.
     * {@inheritDoc}
     */
    public final Object get(final int index, final Scriptable start) {
        final HTMLCollection array = (HTMLCollection) start;
        final List elements = array.getElements();

        if (index >= 0 && index < elements.size()) {
            return getScriptableFor(transformer_.transform(elements.get(index)));
        }
        else {
            return NOT_FOUND;
        }
    }

    /**
     * Gets the html elements. Avoid calling it multiple times within a method because the evaluation
     * needs to be performed each time again
     * @return the list of {@link HtmlElement} contained in this collection
     */
    private List getElements() {
        if (cachedElements_ == null) {
            try {
                if (node_ != null) {
                    cachedElements_ = xpath_.selectNodes(node_);
                }
                else {
                    cachedElements_ = new ArrayList();
                }
                boolean isXmlPage = false;

                //TODO: should be replaced by "getPage() instanceof XmlPage"
                for (DomNode parent = node_; parent != null; parent = parent.getParentDomNode()) {
                    if (parent instanceof XmlPage) {
                        isXmlPage = true;
                    }
                }

                final boolean isIE = getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE();

                for (int i = 0; i < cachedElements_.size(); i++) {
                    final DomNode element = (DomNode) cachedElements_.get(i);
                    
                    //IE: XmlPage ignores all empty text nodes
                    if (isIE && isXmlPage && element instanceof DomText
                            && ((DomText) element).getNodeValue().trim().length() == 0) {

                        //and 'xml:space' is 'default'
                        final Boolean xmlSpaceDefault = isXMLSpaceDefault(element.getParentDomNode());
                        if (xmlSpaceDefault != Boolean.FALSE) {
                            cachedElements_.remove(i--);
                            continue;
                        }
                    }
                    for (DomNode parent = element.getParentDomNode(); parent != null;
                        parent = parent.getParentDomNode()) {
                        if (parent instanceof HtmlNoScript) {
                            cachedElements_.remove(i--);
                            break;
                        }
                    }
                }
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Exeption getting elements: " + e.getMessage());
            }
        }
        return cachedElements_;
    }

    /**
     * Recursively checks whether "xml:space" attribute is set to "default".
     * @param node node to start checking from.
     * @return {@link Boolean#TRUE} if "default" is set, {@link Boolean#FALSE} for other value,
     *         or null if nothing is set.
     */
    private static Boolean isXMLSpaceDefault(DomNode node) {
        for ( ; node instanceof XmlElement; node = node.getParentDomNode()) {
            final String value = ((XmlElement) node).getAttributeValue("xml:space");
            if (value.length() != 0) {
                if (value.equals("default")) {
                    return Boolean.TRUE;
                }
                else {
                    return Boolean.FALSE;
                }
            }
        }
        return null;
    }
    
    /**
     * Returns the element or elements that match the specified key. If it is the name
     * of a property, the property value is returned. If it is the id of an element in
     * the array, that element is returned. Finally, if it is the name of an element or
     * elements in the array, then all those elements are returned. Otherwise,
     * {@link #NOT_FOUND} is returned.
     * {@inheritDoc}
     */
    protected Object getWithPreemption(final String name) {
        // Test to see if we are trying to get the length of this collection?
        // If so return NOT_FOUND here to let the property be retrieved using the prototype
        if ("length".equals(name)) {
            return NOT_FOUND;
        }

        final List elements = getElements();
        CollectionUtils.transform(elements, transformer_);

        // See if there is an element in the element array with the specified id.
        for (final Iterator iter = elements.iterator(); iter.hasNext();) {
            final Object next = iter.next();
            if (next instanceof HtmlElement) {
                final HtmlElement element = (HtmlElement) next;
                final String id = element.getId();
                if (id != null && id.equals(name)) {
                    getLog().debug("Property \"" + name + "\" evaluated (by id) to " + element);
                    return getScriptableFor(element);
                }
            }
            else if (next instanceof WebWindow) {
                final WebWindow window = (WebWindow) next;
                final String windowName = window.getName();
                if (windowName != null && windowName.equals(name)) {
                    getLog().debug("Property \"" + name + "\" evaluated (by name) to " + window);
                    return getScriptableFor(window);
                }
            }
            else {
                getLog().debug("Unrecognized type in array: \"" + next.getClass().getName() + "\"");
            }
        }

        // See if there are any elements in the element array with the specified name.
        final HTMLCollection array = new HTMLCollection(this);
        try {
            final String newCondition = "@name = '" + name + "'";
            final String currentXPathExpr = xpath_.toString();
            final String xpathExpr;
            if (currentXPathExpr.endsWith("]")) {
                xpathExpr = currentXPathExpr.substring(0, currentXPathExpr.length() - 1) + " and " + newCondition + "]";
            }
            else {
                xpathExpr = currentXPathExpr + "[" + newCondition + "]";
            }
            final XPath xpathName = xpath_.getNavigator().parseXPath(xpathExpr);
            array.init(node_, xpathName);
        }
        catch (final SAXPathException e) {
            throw Context.reportRuntimeError("Failed getting sub elements by name" + e.getMessage());
        }

        final List subElements = array.getElements();
        if (subElements.size() > 1) {
            getLog().debug("Property \"" + name + "\" evaluated (by name) to " + array + " with "
                    + subElements.size() + " elements");
            return array;
        }
        else if (subElements.size() == 1) {
            final SimpleScriptable singleResult = getScriptableFor(subElements.get(0));
            getLog().debug("Property \"" + name + "\" evaluated (by name) to " + singleResult);
            return singleResult;
        }

        // Nothing was found.
        return NOT_FOUND;
    }

    /**
     * Returns the length of this element array.
     * @return The length of this element array.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/length.asp">MSDN doc</a>
     */
    public final int jsxGet_length() {
        return getElements().size();
    }

    /**
     * Retrieves the item or items corresponding to the specified index or key.
     * @param index The index or key corresponding to the element or elements to return.
     * @return The element or elements corresponding to the specified index or key.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/item.asp">MSDN doc</a>
     */
    public final Object jsxFunction_item(final Object index) {
        return get(index);
    }

    /**
     * Retrieves the item or items corresponding to the specified name (checks ids, and if
     * that does not work, then names).
     * @param name The name or id the element or elements to return.
     * @return The element or elements corresponding to the specified name or id.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/nameditem.asp">MSDN doc</a>
     */
    public final Object jsxFunction_namedItem(final String name) {
        return get(name);
    }

    /**
     * Returns all the elements in this element array that have the specified tag name.
     * This method returns an empty element array if there are no elements with the
     * specified tag name.
     * @param tagName The name of the tag of the elements to return.
     * @return All the elements in this element array that have the specified tag name.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/tags.asp">MSDN doc</a>
     */
    public final Object jsxFunction_tags(final String tagName) {
        final HTMLCollection array = new HTMLCollection(this);
        try {
            final String newXPathExpr = xpath_ + "[name() = '" + tagName.toLowerCase() + "']";
            array.init(node_, xpath_.getNavigator().parseXPath(newXPathExpr));
        }
        catch (final SAXPathException e) {
            // should never occur
            throw Context.reportRuntimeError("Failed call tags: " + e.getMessage());
        }

        return array;
    }

    /**
     * Just for debug purpose.
     * {@inheritDoc}
     */
    public String toString() {
        if (xpath_ != null) {
            return super.toString() + '<' + xpath_ + '>';
        }
        return super.toString();
    }

    /**
     * Called for the js "==".
     * {@inheritDoc}
     */
    protected Object equivalentValues(final Object other) {
        if (other == this) {
            return Boolean.TRUE;
        }
        else if (other instanceof HTMLCollection) {
            final HTMLCollection otherArray = (HTMLCollection) other;
            if (node_ == otherArray.node_
                    && xpath_.toString().equals(otherArray.xpath_.toString())
                    && transformer_.equals(otherArray.transformer_)) {
                return Boolean.TRUE;
            }
            else {
                return NOT_FOUND;
            }
        }

        return super.equivalentValues(other);
    }

    /**
     * {@inheritDoc}
     */
    public boolean has(final String name, final Scriptable start) {
        if (!getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE()) {
            if (name.equals("0") || name.equals("length")) {
                return true;
            }
            final JavaScriptConfiguration jsConfig =
                JavaScriptConfiguration.getInstance(BrowserVersion.FIREFOX_2);
            final Set functionKeys = jsConfig.getClassConfiguration(getClassName()).functionKeys();
            for (final Iterator functionIt = functionKeys.iterator(); functionIt.hasNext();) {
                if (name.equals(functionIt.next())) {
                    return true;
                }
            }
            return false;
        }
        else {
            return name.equals("length") || getWithPreemption(name) != NOT_FOUND;
        }
    }

    /**
     * Returns the element or elements that match the specified key. If it is the name
     * of a property, the property value is returned. If it is the id of an element in
     * the array, that element is returned. Finally, if it is the name of an element or
     * elements in the array, then all those elements are returned. Otherwise,
     * {@inheritDoc}.
     */
    public Object[] getIds() {
        final List idList = new ArrayList();

        if (!getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE()) {
            idList.add("0");
            idList.add("length");
            final JavaScriptConfiguration jsConfig =
                JavaScriptConfiguration.getInstance(BrowserVersion.FIREFOX_2);
            final Set functionKeys = jsConfig.getClassConfiguration(getClassName()).functionKeys();
            for (final Iterator functionIt = functionKeys.iterator(); functionIt.hasNext();) {
                idList.add(functionIt.next());
            }
            //'document.all.tags' is different from 'document.forms.tags'
            //See HTMLCollectionTest.testTags()
            idList.remove("tags");
        }
        else {
            idList.add("length");

            final List elements = getElements();
            CollectionUtils.transform(elements, transformer_);

            // See if there is an element in the element array with the specified id.
            for (final Iterator iter = elements.iterator(); iter.hasNext();) {
                final Object next = iter.next();
                if (next instanceof HtmlElement) {
                    final HtmlElement element = (HtmlElement) next;
                    final String id = element.getId();
                    if (id != HtmlElement.ATTRIBUTE_NOT_DEFINED) {
                        idList.add(id);
                    }
                }
                else if (next instanceof WebWindow) {
                    final WebWindow window = (WebWindow) next;
                    final String windowName = window.getName();
                    if (windowName != null) {
                        idList.add(windowName);
                    }
                }
                else {
                    getLog().debug("Unrecognized type in array: \"" + next.getClass().getName() + "\"");
                }
            }

            if (xpath_ != null) {
                // See if there are any elements in the element array with the specified name.
                final HTMLCollection array = new HTMLCollection(this);
                try {
                    final String newCondition = "@name";
                    final String currentXPathExpr = xpath_.toString();
                    final String xpathExpr;
                    if (currentXPathExpr.endsWith("]")) {
                        xpathExpr =
                            currentXPathExpr.substring(0, currentXPathExpr.length() - 1)
                            + " and " + newCondition + "]";
                    }
                    else {
                        xpathExpr = currentXPathExpr + "[" + newCondition + "]";
                    }
                    final XPath xpathName = xpath_.getNavigator().parseXPath(xpathExpr);
                    array.init(node_, xpathName);
                }
                catch (final SAXPathException e) {
                    throw Context.reportRuntimeError("Failed getting sub elements by name" + e.getMessage());
                }

                final List subElements = array.getElements();
                for (final Iterator it = subElements.iterator(); it.hasNext();) {
                    final DomNode next = (DomNode) it.next();
                    if (next instanceof HtmlElement) {
                        final HtmlElement element = (HtmlElement) next;
                        final String id = element.getAttribute("name");
                        if (id != null) {
                            idList.add(id);
                        }
                    }
                }
            }
        }
        return idList.toArray();
    }


    private class DomHtmlAttributeChangeListenerImpl implements DomChangeListener, HtmlAttributeChangeListener {
        /**
         * {@inheritDoc}
         */
        public void nodeAdded(final DomChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void nodeDeleted(final DomChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }
    }
}