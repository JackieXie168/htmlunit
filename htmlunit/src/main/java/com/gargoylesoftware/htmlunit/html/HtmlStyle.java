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
 * Wrapper for the HTML element "style".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlStyle extends HtmlElement {

    private static final long serialVersionUID = 8908548603811742885L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "style";

    /**
     * Create an instance of HtmlStyle
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlStyle(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute "lang". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "lang" or an empty string if that attribute isn't defined
     */
    public final String getLangAttribute() {
        return getAttributeValue("lang");
    }

    /**
     * Returns the value of the attribute "xml:lang". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "xml:lang" or an empty string if that attribute isn't defined
     */
    public final String getXmlLangAttribute() {
        return getAttributeValue("xml:lang");
    }

    /**
     * Returns the value of the attribute "dir". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "dir" or an empty string if that attribute isn't defined
     */
    public final String getTextDirectionAttribute() {
        return getAttributeValue("dir");
    }

    /**
     * Returns the value of the attribute "type". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "type" or an empty string if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        return getAttributeValue("type");
    }

    /**
     * Returns the value of the attribute "media". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "media" or an empty string if that attribute isn't defined
     */
    public final String getMediaAttribute() {
        return getAttributeValue("media");
    }

    /**
     * Returns the value of the attribute "title". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "title" or an empty string if that attribute isn't defined
     */
    public final String getTitleAttribute() {
        return getAttributeValue("title");
    }

    /**
     * @see com.gargoylesoftware.htmlunit.html.HtmlInput#asText()
     * @return an empty string as the content of style is not visible by itself
     */
    // we need to preserve this method as it is there since many versions with the above documentation.
    // This doesn't mean that asTextInternal() has to return the same.
    @Override
    public String asText() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String asTextInternal() {
        return "";
    }
}
