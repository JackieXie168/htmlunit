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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONHASHCHANGE_BUBBLES_FALSE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

public class HashChangeEvent2 extends Event2 {

    private String oldURL_ = "";
    private String newURL_ = "";

    /**
     * Creates a new event instance.
     */
    public HashChangeEvent2() {
        setEventType("");
    }

    /**
     * Creates a new event instance.
     *
     * @param scriptable the SimpleScriptObject that triggered the event
     * @param type the event type
     * @param oldURL the old URL
     * @param newURL the new URL
     */
    public HashChangeEvent2(final SimpleScriptObject scriptable, final String type,
            final String oldURL, final String newURL) {
        super(scriptable, type);
        oldURL_ = oldURL;
        newURL_ = newURL;

        if (getBrowserVersion().hasFeature(EVENT_ONHASHCHANGE_BUBBLES_FALSE)) {
            setBubbles(false);
        }
        setCancelable(false);
    }

    public static HashChangeEvent2 constructor(final boolean newObj, final Object self) {
        final HashChangeEvent2 host = new HashChangeEvent2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HashChangeEvent2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("HashChangeEvent", 
                    staticHandle("constructor", HashChangeEvent2.class, boolean.class, Object.class),
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
            return "HashChangeEvent";
        }
    }
}
