/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "title".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlTitle extends HtmlElement {

    private static final long serialVersionUID = 5463879100333678376L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "title";

    /**
     * Creates an instance of HtmlTitle.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlTitle(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Allows the text value for the title element be replaced.
     * {@inheritDoc}
     */
    @Override
    public void setNodeValue(final String message) {
        final DomNode child = getFirstChild();
        if (child == null) {
            final DomNode textNode = new DomText(getPage() , message);
            appendChild(textNode);
        }
        else if (child instanceof DomText) {
            ((DomText) child).setData(message);
        }
        else {
            throw new IllegalStateException("For title tag, this should be a text node");
        }
    }

    /**
     * Indicates if a node without children should be written in expanded form as XML
     * (i.e. with closing tag rather than with "/&gt;").
     * @return <code>true</code> as required by Microsoft Internet Explorer
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String asTextInternal() {
        return super.asTextInternal() + AS_TEXT_BLOCK_SEPARATOR;
    }
}
