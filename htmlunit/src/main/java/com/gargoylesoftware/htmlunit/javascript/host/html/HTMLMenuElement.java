/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_MENU_TYPE_EMPTY;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.HtmlMenu;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object "HTMLMenuElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlMenu.class,
    browsers = { @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
public class HTMLMenuElement extends HTMLListElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public HTMLMenuElement() {
    }

    /**
     * Returns the value of the "type" property.
     * @return the value of the "type" property
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(IE) })
    public String getType() {
        if (getBrowserVersion().hasFeature(JS_MENU_TYPE_EMPTY)) {
            return "";
        }

        final String type = getDomNodeOrDie().getAttribute("type");
        if ("context".equalsIgnoreCase(type)) {
            return "context";
        }
        if ("toolbar".equalsIgnoreCase(type)) {
            return "toolbar";
        }
        return "list";
    }

    /**
     * Sets the value of the "type" property.
     * @param type the value of the "type" property
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(IE) })
    public void setType(final String type) {
        if (getBrowserVersion().hasFeature(JS_MENU_TYPE_EMPTY)) {
            if (StringUtils.isEmpty(type)) {
                return;
            }
            throw Context.reportRuntimeError("Cannot set the type property to invalid value: '" + type + "'");
        }

        if ("context".equalsIgnoreCase(type)) {
            getDomNodeOrDie().setAttribute("type", "context");
            return;
        }
        if ("toolbar".equalsIgnoreCase(type)) {
            getDomNodeOrDie().setAttribute("type", "toolbar");
            return;
        }

        getDomNodeOrDie().setAttribute("type", "list");
    }
}
