/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code NodeFilter}.
 *
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html">
 * DOM-Level-2-Traversal-Range</a>
 *
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11),
        @WebBrowser(EDGE) })
public class NodeFilter extends SimpleScriptable {

    /**
     * Accept the node.
     */
    @JsxConstant
    public static final short FILTER_ACCEPT = 1;

    /**
     * Reject the node.
     */
    @JsxConstant
    public static final short FILTER_REJECT = 2;

    /**
     * Skip the node.
     */
    @JsxConstant
    public static final short FILTER_SKIP = 3;

    /** Show all nodes. */
    @JsxConstant
    public static final long SHOW_ALL = 0xFFFFFFFFL;

    /** Show Element nodes. */
    @JsxConstant
    public static final int SHOW_ELEMENT = 0x1;

    /** Show Attr nodes. Only useful when creating a TreeWalker with an
     * attribute node as its root. */
    @JsxConstant
    public static final int SHOW_ATTRIBUTE = 0x2;

    /** Show Text nodes. */
    @JsxConstant
    public static final int SHOW_TEXT = 0x4;

    /** Show CDATASection nodes. */
    @JsxConstant
    public static final int SHOW_CDATA_SECTION = 0x8;

    /** Show EntityReference nodes. */
    @JsxConstant
    public static final int SHOW_ENTITY_REFERENCE = 0x10;

    /** Show Entity nodes. */
    @JsxConstant
    public static final int SHOW_ENTITY = 0x20;

    /** Show ProcessingInstruction nodes. */
    @JsxConstant
    public static final int SHOW_PROCESSING_INSTRUCTION = 0x40;

    /** Show Comment nodes. */
    @JsxConstant
    public static final int SHOW_COMMENT = 0x80;

    /** Show Document nodes. */
    @JsxConstant
    public static final int SHOW_DOCUMENT = 0x100;

    /** Show DocumentType nodes. */
    @JsxConstant
    public static final int SHOW_DOCUMENT_TYPE = 0x200;

    /** Show DocumentFragment nodes. */
    @JsxConstant
    public static final int SHOW_DOCUMENT_FRAGMENT = 0x400;

    /**
     * Show Notation nodes. Only useful when creating a TreeWalker with a
     * Notation node as its root.
     */
    @JsxConstant
    public static final int SHOW_NOTATION = 0x800;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public NodeFilter() {
    }

    /**
     * Test whether a specified node is visible in the logical view of a
     * TreeWalker. This is not normally called directly from user code.
     *
     * @param n The node to check to see if it passes the filter or not.
     * @return a constant to determine whether the node is accepted, rejected,
     *          or skipped.
     */
    public short acceptNode(final Node n) {
        return FILTER_ACCEPT;
    }
}
