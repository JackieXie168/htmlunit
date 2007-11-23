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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.StringReader;
import java.util.WeakHashMap;

import org.jaxen.JaxenException;
import org.w3c.css.sac.InputSource;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * An ordered list of stylesheets, accessible via <tt>document.styleSheets</tt>, as specified by the
 * <a href="http://www.w3.org/TR/DOM-Level-2-Style/stylesheets.html#StyleSheets-StyleSheetList">DOM
 * Level 2 Style spec</a> and the <a href="http://developer.mozilla.org/en/docs/DOM:document.styleSheets">Gecko
 * DOM Guide</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class StyleSheetList extends SimpleScriptable {

    private static final long serialVersionUID = -8607630805490604483L;

    /**
     * We back the stylesheet list with an {@link HTMLCollection} of styles/links because this list
     * must be "live".
     */
    private HTMLCollection nodes_;

    /**
     * Cache the stylesheets parsed for each style/link node, but don't keep the garbage collector
     * from releasing the associated nodes if they are removed from the document.
     */
    private WeakHashMap sheets_ = new WeakHashMap();

    /**
     * Rhino requires default constructors.
     */
    public StyleSheetList() {
        // Empty.
    }

    /**
     * Creates a new style sheet list owned by the specified document.
     *
     * @param document the owning document
     */
    public StyleSheetList(final Document document) {
        setParentScope(document);
        setPrototype(getPrototype(getClass()));
        try {
            nodes_ = new HTMLCollection(document);
            nodes_.init(document.getHtmlPage(), new HtmlUnitXPath("//style | //link[lower-case(@rel)='stylesheet']"));
        }
        catch (final JaxenException e) {
            getLog().error(e.getMessage(), e);
        }
    }

    /**
     * Returns the list's length.
     *
     * @return the list's length
     */
    public int jsxGet_length() {
        return nodes_.jsxGet_length();
    }

    /**
     * Returns the style sheet at the specified index.
     *
     * @param index the index of the style sheet to return
     * @return the style sheet at the specified index
     */
    public Stylesheet jsxFunction_item(final int index) {
        HTMLElement element = (HTMLElement) nodes_.jsxFunction_item(new Integer(index));
        final DomNode node = (DomNode) element.getDomNodeOrDie();
        Stylesheet sheet = (Stylesheet) sheets_.get(node);
        if (sheet == null) {
            if (node instanceof HtmlStyle) {
                final HtmlStyle style = (HtmlStyle) node;
                final String s = style.getFirstDomChild().asText();
                sheet = new Stylesheet(new InputSource(new StringReader(s)));
            }
            else {
                final HtmlLink link = (HtmlLink) node;
                sheet = new Stylesheet(new InputSource(link.getHrefAttribute()));
            }
            sheets_.put(node, sheet);
        }
        return sheet;
    }

}
