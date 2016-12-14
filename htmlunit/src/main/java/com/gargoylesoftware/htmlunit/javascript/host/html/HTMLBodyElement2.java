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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BODY_MARGINS_8;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Locale;

import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

@ScriptClass
public class HTMLBodyElement2 extends HTMLElement2 {

    public static HTMLBodyElement2 constructor(final boolean newObj, final Object self) {
        final HTMLBodyElement2 host = new HTMLBodyElement2();
        ScriptUtils.initialize(host);
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Forwards the events to window.
     *
     * {@inheritDoc}
     *
     * @see HTMLFrameSetElement2#getEventListenersContainer()
     */
    public EventListenersContainer2 getEventListenersContainer() {
        return getWindow().getEventListenersContainer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(final ComputedCSSStyleDeclaration2 style) {
        if (getBrowserVersion().hasFeature(JS_BODY_MARGINS_8)) {
            style.setDefaultLocalStyleAttribute("margin", "8px");
            style.setDefaultLocalStyleAttribute("padding", "0px");
        }
        else {
            style.setDefaultLocalStyleAttribute("margin-left", "8px");
            style.setDefaultLocalStyleAttribute("margin-right", "8px");
            style.setDefaultLocalStyleAttribute("margin-top", "8px");
            style.setDefaultLocalStyleAttribute("margin-bottom", "8px");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getClientWidth() {
        return super.getClientWidth() + 16;
    }

    /**
     * Creates the event handler from the attribute value. This has to be done no matter which browser
     * is simulated to handle ill-formed HTML code with many body (possibly generated) elements.
     * @param attributeName the attribute name
     * @param value the value
     */
    public void createEventHandlerFromAttribute(final String attributeName, final String value) {
        // when many body tags are found while parsing, attributes of
        // different tags are added and should create an event handler when needed
        if (attributeName.toLowerCase(Locale.ROOT).startsWith("on")) {
            createEventHandler(attributeName, value);
        }
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLBodyElement2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @ClassConstructor({@WebBrowser(CHROME), @WebBrowser(FF)})
    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("HTMLBodyElement", 
                    staticHandle("constructor", HTMLBodyElement2.class, boolean.class, Object.class),
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
            return "HTMLBodyElement";
        }
    }

    @ClassConstructor(@WebBrowser(IE))
    public static final class ObjectConstructor extends ScriptObject {
        public ObjectConstructor() {
            ScriptUtils.initialize(this);
        }

        public Object getDefaultValue(final Class<?> typeHint) {
            return "[object HTMLBodyElement]";
        }

        public String getClassName() {
            return "HTMLBodyElement";
        }
    }
}
