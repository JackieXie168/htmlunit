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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser.IE;

import com.gargoylesoftware.htmlunit.html.HtmlFrameSet;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * The JavaScript object {@code HTMLFrameSetElement}.
 *
 * @author Bruce Chapman
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlFrameSet.class)
public class HTMLFrameSetElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, FF, EDGE})
    public HTMLFrameSetElement() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEventHandlerOnWindow() {
        return true;
    }

    /**
     * Sets the rows property.
     *
     * @param rows the rows attribute value
     */
    @JsxSetter
    public void setRows(final String rows) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("rows", rows);
        }
    }

    /**
     * Gets the rows property.
     *
     * @return the rows attribute value
     */

    @JsxGetter
    public String getRows() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getRowsAttribute();
    }

    /**
     * Sets the cols property.
     *
     * @param cols the cols attribute value
     */
    @JsxSetter
    public void setCols(final String cols) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("cols", cols);
        }
    }

    /**
     * Gets the cols property.
     *
     * @return the cols attribute value
     */
    @JsxGetter
    public String getCols() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getColsAttribute();
    }

    /**
     * Gets the {@code border} attribute.
     * @return the {@code border} attribute
     */
    @JsxGetter(IE)
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the {@code border} attribute.
     * @param border the {@code border} attribute
     */
    @JsxSetter(IE)
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Overwritten to throw an exception.
     * @param value the new value for replacing this node
     */
    @JsxSetter
    @Override
    public void setOuterHTML(final Object value) {
        throw Context.reportRuntimeError("outerHTML is read-only for tag 'frameset'");
    }
}
