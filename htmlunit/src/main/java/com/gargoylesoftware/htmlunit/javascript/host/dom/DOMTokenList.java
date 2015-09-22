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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOMTOKENLIST_ENHANCED_WHITESPACE_CHARS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_EDIT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * A JavaScript object for {@code DOMTokenList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11),
        @WebBrowser(EDGE) })
public class DOMTokenList extends SimpleScriptable {

    private static final String WHITESPACE_CHARS = " \t\r\n\u000C";
    private static final String WHITESPACE_CHARS_IE_11 = WHITESPACE_CHARS + "\u000B";

    private String attributeName_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public DOMTokenList() {
    }

    /**
     * Creates an instance.
     * @param node the node which contains the underlying string
     * @param attributeName the attribute name of the DomElement of the specified node
     */
    public DOMTokenList(final Node node, final String attributeName) {
        setDomNode(node.getDomNodeOrDie(), false);
        setParentScope(node.getParentScope());
        setPrototype(getPrototype(getClass()));
        attributeName_ = attributeName;
    }

    /**
     * Returns the length property.
     * @return the length
     */
    @JsxGetter
    public int getLength() {
        final String value = getDefaultValue(null);
        return StringUtils.split(value, whitespaceChars()).length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue(final Class<?> hint) {
        if (getPrototype() == null) {
            return (String) super.getDefaultValue(hint);
        }
        final DomAttr attr = (DomAttr) getDomNodeOrDie().getAttributes().getNamedItem(attributeName_);
        if (attr != null) {
            String value = attr.getValue();
            if (getBrowserVersion().hasFeature(JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_EDIT)) {
                value = StringUtils.join(StringUtils.split(value, whitespaceChars()), ' ');
            }
            return value;
        }
        return "";
    }

    /**
     * Adds the specified token to the underlying string.
     * @param token the token to add
     */
    @JsxFunction
    public void add(final String token) {
        if (!contains(token)) {
            String value = getDefaultValue(null);
            if (value.length() != 0 && !isWhitespache(value.charAt(value.length() - 1))) {
                value = value + " ";
            }
            value = value + token;
            updateAttribute(value);
        }
    }

    /**
     * Removes the specified token from the underlying string.
     * @param token the token to remove
     */
    @JsxFunction
    public void remove(final String token) {
        if (StringUtils.isEmpty(token)) {
            throw Context.reportRuntimeError("Empty imput not allowed");
        }
        if (StringUtils.containsAny(token, whitespaceChars())) {
            throw Context.reportRuntimeError("Empty imput not allowed");
        }
        String value = getDefaultValue(null);
        int pos = position(value, token);
        while (pos != -1) {
            int from = pos;
            int to = pos + token.length();

            while (from > 0 && isWhitespache(value.charAt(from - 1))) {
                from = from - 1;
            }
            while (to < value.length() - 1 && isWhitespache(value.charAt(to))) {
                to = to + 1;
            }

            final StringBuilder result = new StringBuilder();
            if (from > 0) {
                result.append(value, 0, from);
                if (to < value.length()) {
                    result.append(" ");
                }
            }
            result.append(value, to, value.length());

            value = result.toString();
            updateAttribute(value);

            pos = position(value, token);
        }
    }

    /**
     * Toggle the token, by adding or removing.
     * @param token the token to add or remove
     * @return whether the string now contains the token or not
     */
    @JsxFunction
    public boolean toggle(final String token) {
        if (contains(token)) {
            remove(token);
            return false;
        }
        add(token);
        return true;
    }

    /**
     * Checks if the specified token is contained in the underlying string.
     * @param token the token to add
     * @return true if the underlying string contains token, otherwise false
     */
    @JsxFunction
    public boolean contains(final String token) {
        if (StringUtils.isEmpty(token)) {
            throw Context.reportRuntimeError("Empty imput not allowed");
        }
        if (StringUtils.containsAny(token, whitespaceChars())) {
            throw Context.reportRuntimeError("Empty imput not allowed");
        }
        return position(getDefaultValue(null), token) > -1;
    }

    /**
     * Returns the item at the specified index.
     * @param index the index of the item
     * @return the item
     */
    @JsxFunction
    public Object item(final int index) {
        if (index < 0) {
            return null;
        }
        final String value = getDefaultValue(null);
        final List<String> values = Arrays.asList(StringUtils.split(value, whitespaceChars()));
        if (index < values.size()) {
            return values.get(index);
        }
        return null;
    }

    private void updateAttribute(final String value) {
        final HtmlElement domNode = (HtmlElement) getDomNodeOrDie();
        DomAttr attr = (DomAttr) domNode.getAttributes().getNamedItem(attributeName_);
        if (null == attr) {
            attr = domNode.getPage().createAttribute(attributeName_);
            domNode.setAttributeNode(attr);
        }
        attr.setValue(value);
    }

    private int position(final String value, final String token) {
        final int pos = value.indexOf(token);
        if (pos < 0) {
            return -1;
        }

        // whitespace before
        if (pos != 0 && !isWhitespache(value.charAt(pos - 1))) {
            return -1;
        }

        // whitespace after
        final int end = pos + token.length();
        if (end != value.length() && !isWhitespache(value.charAt(end))) {
            return -1;
        }
        return pos;
    }

    private String whitespaceChars() {
        if (getBrowserVersion().hasFeature(JS_DOMTOKENLIST_ENHANCED_WHITESPACE_CHARS)) {
            return WHITESPACE_CHARS_IE_11;
        }
        return WHITESPACE_CHARS;
    }

    private boolean isWhitespache(final int ch) {
        return whitespaceChars().indexOf(ch) > -1;
    }
}
