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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLESHEETLIST_ACTIVE_ONLY;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.apache.commons.lang3.StringUtils;
import org.w3c.css.sac.SACMediaList;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.Undefined;
import com.steadystate.css.dom.MediaListImpl;

public class StyleSheetList2 extends SimpleScriptObject {

    /**
     * We back the stylesheet list with an {@link HTMLCollection2} of styles/links because this list must be "live".
     */
    private HTMLCollection2 nodes_;

    /**
     * Verifies if the provided node is a link node pointing to a stylesheet.
     *
     * @param domNode the mode to check
     * @return true if the provided node is a stylesheet link
     */
    public static boolean isStyleSheetLink(final DomNode domNode) {
        return domNode instanceof HtmlLink
                && "stylesheet".equalsIgnoreCase(((HtmlLink) domNode).getRelAttribute());
    }

    /**
     * Verifies if the provided node is a link node pointing to an active stylesheet.
     *
     * @param domNode the mode to check
     * @return true if the provided node is a stylesheet link
     */
    public boolean isActiveStyleSheetLink(final DomNode domNode) {
        if (domNode instanceof HtmlLink) {
            final HtmlLink link = (HtmlLink) domNode;
            if ("stylesheet".equalsIgnoreCase(link.getRelAttribute())) {
                final String media = link.getMediaAttribute();
                if (StringUtils.isBlank(media)) {
                    return true;
                }
                final WebClient webClient = getWindow().getWebWindow().getWebClient();
                final SACMediaList mediaList = CSSStyleSheet.parseMedia(webClient.getCssErrorHandler(), media);
                return CSSStyleSheet2.isActive(this, new MediaListImpl(mediaList));
            }
        }
        return false;
    }

    public StyleSheetList2() {
    }

    /**
     * Creates a new style sheet list owned by the specified document.
     *
     * @param document the owning document
     */
    public StyleSheetList2(final HTMLDocument2 document) {
        final WebClient webClient = document.getDomNodeOrDie().getPage().getWebClient();
        final boolean cssEnabled = webClient.getOptions().isCssEnabled();
        final boolean onlyActive = webClient.getBrowserVersion().hasFeature(JS_STYLESHEETLIST_ACTIVE_ONLY);

        if (cssEnabled) {
            nodes_ = new HTMLCollection2(document.getDomNodeOrDie(), true) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    if (node instanceof HtmlStyle) {
                        return true;
                    }
                    if (onlyActive) {
                        return isActiveStyleSheetLink(node);
                    }
                    return isStyleSheetLink(node);
                }

                @Override
                protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
                    final HtmlElement node = event.getHtmlElement();
                    if (node instanceof HtmlLink && "rel".equalsIgnoreCase(event.getName())) {
                        return EffectOnCache.RESET;
                    }
                    return EffectOnCache.NONE;
                }
            };
        }
        else {
            nodes_ = HTMLCollection2.emptyCollection(getWindow());
        }
    }

    public static StyleSheetList2 constructor(final boolean newObj, final Object self) {
        final StyleSheetList2 host = new StyleSheetList2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Returns the list's length.
     *
     * @return the list's length
     */
    @Getter
    public Object getLength() {
        return nodes_.getLength();
    }

    /**
     * Returns the style sheet at the specified index.
     *
     * @param index the index of the style sheet to return
     * @return the style sheet at the specified index
     */
    @Function
    public Object item(final int index) {
        if (index < 0 || index >= (int) getLength()) {
            return Undefined.getUndefined();
        }

        final HTMLElement2 element = (HTMLElement2) nodes_.item(Integer.valueOf(index));

        final CSSStyleSheet2 sheet;
        // <style type="text/css"> ... </style>
        if (element instanceof HTMLStyleElement2) {
            sheet = ((HTMLStyleElement2) element).getSheet();
        }
        else {
            // <link rel="stylesheet" type="text/css" href="..." />
            sheet = ((HTMLLinkElement2) element).getSheet();
        }

        return sheet;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(StyleSheetList2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("StyleSheetList", 
                    staticHandle("constructor", StyleSheetList2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends PrototypeObject {
        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "StyleSheetList";
        }
    }
}
