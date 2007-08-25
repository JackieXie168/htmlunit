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
package com.gargoylesoftware.htmlunit.libraries;

import java.util.Iterator;

/**
 * Tests for compatibility with version 1.1.2 of the <a href="http://jquery.com/">jQuery JavaScript library</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class JQuery112Test extends JQueryTestBase {

    /**
     * Creates an instance.
     *
     * @param name The name of the test.
     */
    public JQuery112Test(final String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    protected String getVersion() {
        return "1.1.2";
    }

    /**
     * {@inheritDoc}
     */
    protected void verify(final Iterator i, final boolean ie) throws Exception {
        ok(i, "core module: Basic requirements", 0, 7);
        ok(i, "core module: $()", 0, 1);
        ok(i, "core module: length", 0, 1);
        ok(i, "core module: size()", 0, 1);
        ok(i, "core module: get()", 0, 1);
        ok(i, "core module: get(Number)", 0, 1);
        ok(i, "core module: add(String|Element|Array)", 0, 7);
        ok(i, "core module: each(Function)", 0, 1);
        ok(i, "core module: index(Object)", 0, 8);
        i.next(); // ok(i, "core module: attr(String)", 2, 13); // TODO: all 15 pass!
        ok(i, "core module: attr(String, Function)", 0, 2);
        ok(i, "core module: attr(Hash)", 0, 1);
        ok(i, "core module: attr(String, Object)", 0, 7);
        ok(i, "core module: css(String|Hash)", 0, 8);
        ok(i, "core module: css(String, Object)", 0, 7);
        ok(i, "core module: text()", 0, 1);
        ok(i, "core module: wrap(String|Element)", 0, 4);
        ok(i, "core module: append(String|Element|Array<Element>|jQuery)", 0, 10);
        ok(i, "core module: appendTo(String|Element|Array<Element>|jQuery)", 0, 5);
        ok(i, "core module: prepend(String|Element|Array<Element>|jQuery)", 0, 5);
        ok(i, "core module: prependTo(String|Element|Array<Element>|jQuery)", 0, 5);
        ok(i, "core module: before(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: insertBefore(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: after(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: insertAfter(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: end()", 0, 3);
        ok(i, "core module: find(String)", 0, 1);
        ok(i, "core module: clone()", 0, 3);
        ok(i, "core module: is(String)", 2, 24);
        ok(i, "core module: $.extend(Object, Object)", 0, 2);
        ok(i, "core module: $.extend(Object, Object, Object, Object)", 0, 4);
        ok(i, "core module: val()", 0, 2);
        ok(i, "core module: val(String)", 0, 2);
        ok(i, "core module: html(String)", 0, 1);
        ok(i, "core module: filter()", 0, 4);
        ok(i, "core module: not()", 0, 3);
        ok(i, "core module: siblings([String])", 0, 4);
        ok(i, "core module: children([String])", 0, 3);
        ok(i, "core module: parent[s]([String])", 0, 8);
        ok(i, "core module: next/prev([String])", 0, 8);
        ok(i, "core module: show()", 0, 1);
        ok(i, "core module: addClass(String)", 0, 1);
        ok(i, "core module: removeClass(String) - simple", 0, 1);
        ok(i, "core module: removeClass(String) - add three classes and remove again", 0, 1);
        ok(i, "core module: toggleClass(String)", 0, 3);
        ok(i, "core module: removeAttr(String", 0, 1);
        ok(i, "core module: text(String)", 0, 1);
        ok(i, "core module: $.each(Object,Function)", 0, 8);
        ok(i, "core module: $.prop", 0, 2);
        ok(i, "core module: $.className", 0, 6);
        ok(i, "core module: remove()", 0, 4);
        ok(i, "core module: empty()", 0, 2);
        ok(i, "core module: eq(), gt(), lt(), contains()", 0, 4);
        ok(i, "core module: click() context", 0, 2);

        ok(i, "selector module: expressions - element", 0, 6);
        ok(i, "selector module: expressions - id", 1, 12);
        ok(i, "selector module: expressions - class", 0, 4);
        ok(i, "selector module: expressions - multiple", 0, 4);
        ok(i, "selector module: expressions - child and adjacent", 0, 14);
        ok(i, "selector module: expressions - attributes", 0, 19);
        ok(i, "selector module: expressions - pseudo (:) selctors", 0, 30);
        ok(i, "selector module: expressions - basic xpath", 1, 14);

        final int failed;
        final int passed;
        if (ie) {
            failed = 3;
            passed = 0;
        }
        else {
            failed = 0;
            passed = 1;
        }

        ok(i, "event module: toggle(Function, Function) - add toggle event and fake a few clicks", failed, passed);
        ok(i, "event module: unbind(event)", 0, 4);
        ok(i, "event module: trigger(event, [data]", 0, 3);
        ok(i, "event module: bind() with data", 0, 2);
        ok(i, "event module: bind() with data and trigger() with data", 0, 4);

        ok(i, "fx module: animate(Hash, Object, Function) - assert that animate doesn't modify its arguments", 0, 1);
        ok(i, "fx module: toggle()", 0, 3);
    }

}
