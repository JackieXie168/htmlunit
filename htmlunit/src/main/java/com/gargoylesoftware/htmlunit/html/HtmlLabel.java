/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

/**
 * Wrapper for the HTML element "label".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlLabel extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "label";

    /**
     * Creates an instance of HtmlLabel
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlLabel(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute {@code for}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code for}
     * or an empty string if that attribute isn't defined.
     */
    public final String getForAttribute() {
        return getAttribute("for");
    }

    /**
     * Returns the value of the attribute {@code accesskey}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accesskey}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAccessKeyAttribute() {
        return getAttribute("accesskey");
    }

    /**
     * Returns the value of the attribute {@code onfocus}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onfocus}
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnFocusAttribute() {
        return getAttribute("onfocus");
    }

    /**
     * Returns the value of the attribute {@code onblur}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onblur}
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnBlurAttribute() {
        return getAttribute("onblur");
    }

    /**
     * Remove focus from this element.
     */
    @Override
    public void blur() {
        final HtmlElement element = getReferencedElement();
        if (element != null) {
            element.blur();
        }
    }

    /**
     * Sets the focus to this element.
     */
    @Override
    public void focus() {
        final HtmlElement element = getReferencedElement();
        if (element != null) {
            element.focus();
        }
    }

    /**
     * Gets the element referenced by this label. That is the element in the page which id is
     * equal to the value of the for attribute of this label.
     * @return the element, {@code null} if not found
     */
    public HtmlElement getReferencedElement() {
        final String elementId = getForAttribute();
        if (!ATTRIBUTE_NOT_DEFINED.equals(elementId)) {
            try {
                return ((HtmlPage) getPage()).getHtmlElementById(elementId);
            }
            catch (final ElementNotFoundException e) {
                return null;
            }
        }
        for (final DomNode element : getChildren()) {
            if (element instanceof HtmlInput) {
                return (HtmlInput) element;
            }
        }
        return null;
    }

    /**
     * Clicks the label and propagates to the referenced element.
     * {@inheritDoc}
     */
    @Override
    public <P extends Page> P click(Event event, boolean ignoreVisibility) throws IOException {
        // first the click on the label
        final P page = super.click(event, ignoreVisibility);

        // not sure which page we should return
        final P response;

        // then the click on the referenced element
        final HtmlElement element = getReferencedElement();
        if (element != null) {
            response = element.click(false, false, false, false);
        }
        else {
            response = page;
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE;
    }
}
