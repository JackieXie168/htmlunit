/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * The javascript object "HTMLElement" which is the base class for all html
 * objects.  This will typically wrap an instance of {@link HtmlElement}.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author David D. Kilzer
 */
public class HTMLElement extends NodeImpl {
    private static final long serialVersionUID = -6864034414262085851L;
    private Style style_;

     /**
      * Create an instance.
      */
     public HTMLElement() {
     }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }


    /**
     * Return the style object for this element.
     *
     * @return The style object
     */
    public Object jsGet_style() {
//        getLog().debug("HTMLElement.jsGet_Style() style=["+style_+"]");
        return style_;
    }


     /**
      * Set the DOM node that corresponds to this javascript object
      * @param domNode The DOM node
      */
     public void setDomNode( final DomNode domNode ) {
         super.setDomNode(domNode);

         style_ = (Style)makeJavaScriptObject("Style");
         style_.initialize(this);
     }


    /**
     * Return the element ID.
     * @return The ID of this element.
     */
    public String jsGet_id() {
        return getHtmlElementOrDie().getId();
    }


    /**
     * Set the identifier this element.
     *
     * @param newId The new identifier of this element.
     */
    public void jsSet_id( final String newId ) {
        getHtmlElementOrDie().setId( newId );
    }


    /**
     * Return true if this element is disabled.
     * @return True if this element is disabled.
     */
    public boolean jsGet_disabled() {
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }


    /**
     * Set whether or not to disable this element
     * @param disabled True if this is to be disabled.
     */
    public void jsSet_disabled( final boolean disabled ) {
        final HtmlElement element = getHtmlElementOrDie();
        if( disabled ) {
            element.setAttributeValue("disabled", "disabled");
        }
        else {
            element.removeAttribute("disabled");
        }
    }


    /**
     * Return the tag name of this element
     * @return The tag name in uppercase
     */
    public String jsGet_tagName() {
        return getHtmlElementOrDie().getTagName().toUpperCase();
    }


    /**
     * Return the value of the named attribute.
     * @param name The name of the variable
     * @param start The scriptable to get the variable from.
     * @return The attribute value
     */
    public Object get( String name, Scriptable start ) {
        Object result = super.get( name, start );
        if ( result == NOT_FOUND ) {
            final HtmlElement htmlElement = getHtmlElementOrNull();
            if( htmlElement != null) {
                final String value = htmlElement.getAttributeValue(name);
                if( value.length() != 0 ) {
                    result = value;
                }
            }
        }
        return result;
    }


    /**
     * Gets the specified property.
     * @param attibuteName attribute name.
     * @return The value of the specified attribute
     */
    public String jsFunction_getAttribute(String attibuteName) {
        return getHtmlElementOrDie().getAttributeValue(attibuteName);
    }


    /**
     * Set an attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-F68F082">
     * the DOM reference</a>
     * 
     * @param name Name of the attribute to set
     * @param value Value to set the attribute to
     */
    public void jsFunction_setAttribute(final String name, final String value) {
        getHtmlElementOrDie().setAttributeValue(name, value);
    }
    
    /**
     * Return all the elements with the specified tag name
     * @param tagName The name to search for
     * @return the list of elements
     */
    public Object jsFunction_getElementsByTagName( final String tagName ) {
        final HtmlElement element = (HtmlElement)getDomNodeOrDie();
        final List list = element.getHtmlElementsByTagNames(
            Collections.singletonList(tagName.toLowerCase()));
        final ListIterator iterator = list.listIterator();
        while(iterator.hasNext()) {
            final HtmlElement htmlElement = (HtmlElement)iterator.next();
            iterator.set( getScriptableFor(htmlElement) );
        }

        return new NativeArray( list.toArray() );
    }
  
    /**
     * Return the class defined for this element
     * @return the class name
     */
    public Object jsGet_className() {
        return getHtmlElementOrDie().getAttributeValue("class");
    }

    /**
     * Set the class attribute for this element.
     * @param className - the new class name
     */
    public void jsSet_className(final String className) {
        getHtmlElementOrDie().setAttributeValue("class", className);
    }
    
    /**
     * Get the innerHTML attribute
     * @return the contents of this node as html
     */
    public String jsGet_innerHTML() {
        final Iterator it = getDomNodeOrDie().getChildIterator();
        StringBuffer buf = new StringBuffer();
        while (it.hasNext()) {
            buf.append(((DomNode) it.next()).asXml());
        }
        removeChar(buf, "\r");
        removeChar(buf, "\n");
        removeChar(buf, "  ");
        return buf.toString();
    }


    /**
     * Remove all occurances of the given string.  Note that if the string
     * is a multi-character string, only the first character is removed.
     * @param buf - StringBuffer to remove the characters from
     * @param str - The string with the first character to be removed.
     */
    private void removeChar(StringBuffer buf, String str) {
        int i;
        while (0 <= (i = buf.indexOf(str))) {
            buf.deleteCharAt(i);
        }
    }
    
    /**
     * Replace all children elements of this element with the supplied value.
     * <b>Currently does not support html as the replacement value.</b>
     * @param value - the new value for the contents of this node
     */
    public void jsSet_innerHTML(final String value) {
        if (value.indexOf('<') >= 0) {
            getLog().debug("Setter not implemented for complex values for innerHTML - simple text string only");
            return;
        }
        final DomNode domNode = getDomNodeOrDie();
        domNode.removeAllChildren();
        domNode.appendChild(new DomText(domNode.getPage(), value));
    }
}
