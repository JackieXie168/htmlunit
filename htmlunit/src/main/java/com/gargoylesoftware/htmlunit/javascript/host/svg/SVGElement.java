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
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.svg.SvgElement;

/**
 * A JavaScript object for {@code SVGElement}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(domClass = SvgElement.class)
public class SVGElement extends Element {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, FF, EDGE})
    public SVGElement() {
    }

    /**
     * Returns the bounding box, in current user space, of the geometry of all contained graphics elements.
     * @return the bounding box
     */
    @JsxFunction
    public SVGRect getBBox() {
        final SVGRect rect = new SVGRect();
        rect.setParentScope(getParentScope());
        rect.setPrototype(getPrototype(rect.getClass()));
        return rect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public CSSStyleDeclaration getStyle() {
        return super.getStyle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setStyle(final String style) {
        super.setStyle(style);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(IE)
    public String getId() {
        return super.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(IE)
    public void setId(final String newId) {
        super.setId(newId);
    }
}
