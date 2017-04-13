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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_MAP_CONSTRUCTOR_ARGUMENT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.util.LinkedHashMap;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code Map}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class Map extends SimpleScriptable {

    private static Iterator ITERATOR_PROTOTYPE_;
    private java.util.Map<Object, Object> map_ = new LinkedHashMap<>();

    /**
     * Creates an instance.
     */
    public Map() {
    }

    /**
     * Creates an instance.
     * @param iterable an Array or other iterable object
     */
    @JsxConstructor
    public Map(final Object iterable) {
        if (iterable != Undefined.instance) {
            final Window window = (Window) ScriptRuntime.getTopCallScope(Context.getCurrentContext());
            if (window.getBrowserVersion().hasFeature(JS_MAP_CONSTRUCTOR_ARGUMENT)) {
                if (iterable instanceof NativeArray) {
                    final NativeArray array = (NativeArray) iterable;
                    for (int i = 0; i < array.getLength(); i++) {
                        final Object entryObject = array.get(i);
                        if (entryObject instanceof NativeArray) {
                            final Object[] entry = toArray((NativeArray) entryObject);
                            if (entry.length > 0) {
                                final Object key = entry[0];
                                final Object value = entry.length > 1 ? entry[1] : null;
                                set(key, value);
                            }
                        }
                        else {
                            throw Context.reportRuntimeError("TypeError: object is not iterable ("
                                        + entryObject.getClass().getName() + ")");
                        }
                    }
                }
                else if (iterable instanceof Map) {
                    final Map map = (Map) iterable;
                    map_.putAll(map.map_);
                }
                else {
                    throw Context.reportRuntimeError("TypeError: object is not iterable ("
                                + iterable.getClass().getName() + ")");
                }
            }
        }
    }

    /**
     * Replacement of {@link NativeArray#toArray()}.
     */
    private static Object[] toArray(final NativeArray narray) {
        final long longLen = narray.getLength();
        if (longLen > Integer.MAX_VALUE) {
            throw new IllegalStateException();
        }

        final int len = (int) longLen;
        final Object[] arr = new Object[len];
        for (int i = 0; i < len; i++) {
            arr[i] = ScriptableObject.getProperty(narray, i);
        }
        return arr;
    }

    /**
     * Returns the size.
     * @return the size
     */
    @JsxGetter
    public int getSize() {
        return map_.size();
    }

    /**
     * Returns the value of the given key.
     * @param key the key
     * @return the value
     */
    @Override
    @JsxFunction
    public Object get(final Object key) {
        Object o = map_.get(key);
        if (o == null) {
            o = Undefined.instance;
        }
        return o;
    }

    /**
     * Adds the specified pair.
     * @param key the key
     * @param value the value
     * @return the Map object.
     */
    @JsxFunction
    public Map set(Object key, final Object value) {
        if (key instanceof Delegator) {
            key = ((Delegator) key).getDelegee();
        }
        map_.put(key, value);
        return this;
    }

    /**
     * Removes all elements.
     */
    @JsxFunction
    public void clear() {
        map_.clear();
    }

    /**
     * Removed the specified element.
     * @param key the key
     * @return whether the element has been successfully removed
     */
    @JsxFunction
    public boolean delete(final Object key) {
        return map_.remove(key) != null;
    }

    /**
     * Returns whether an element with the specified key exists or not.
     * @param key the key
     * @return whether the element exists or not
     */
    @JsxFunction
    public boolean has(final Object key) {
        return map_.remove(key) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        // A hack to handle Rhino not supporting "get(Object object, Scriptable start)"
        if (name.equals(Symbol.ITERATOR_STRING)) {
            return super.get("entries", start);
        }
        return super.get(name, start);
    }

    /**
     * Returns a new {@code Iterator} object that contains the {@code [key, value]} pairs for each element in the
     * Map object in insertion order.
     * @return a new {@code Iterator} object
     */
    @JsxFunction({@WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE)})
    public Object entries() {
        final SimpleScriptable object = new Iterator("Map Iterator", map_.entrySet().iterator());
        object.setParentScope(getParentScope());
        setIteratorPrototype(object);
        return object;
    }

    /**
     * Returns a new {@code Iterator} object that contains the keys for each element in the Map object
     * in insertion order.
     * @return a new {@code Iterator} object
     */
    @JsxFunction({@WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE)})
    public Object keys() {
        final SimpleScriptable object = new Iterator("Map Iterator", map_.keySet().iterator());
        object.setParentScope(getParentScope());
        setIteratorPrototype(object);
        return object;
    }

    /**
     * Returns a new {@code Iterator} object that contains the values for each element in the Map object
     * in insertion order.
     * @return a new {@code Iterator} object
     */
    @JsxFunction({@WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE)})
    public Object values() {
        final SimpleScriptable object = new Iterator("Map Iterator", map_.values().iterator());
        object.setParentScope(getParentScope());
        setIteratorPrototype(object);
        return object;
    }

    private static void setIteratorPrototype(final Scriptable scriptable) {
        if (ITERATOR_PROTOTYPE_ == null) {
            ITERATOR_PROTOTYPE_ = new Iterator("Map Iterator", null);
        }
        scriptable.setPrototype(ITERATOR_PROTOTYPE_);
    }

    /**
     * Executes a provided function once per each value in the {@link Map} object, in insertion order.
     * @param callback {@link Function} to execute for each element.
     * @param thisArg Value to use as this when executing callback (optional)
     */
    @JsxFunction
    public void forEach(final Function callback, final Object thisArg) {
        if (getBrowserVersion().hasFeature(JS_MAP_CONSTRUCTOR_ARGUMENT)) {
            final Scriptable thisArgument;
            if (thisArg instanceof Scriptable) {
                thisArgument = (Scriptable) thisArg;
            }
            else {
                thisArgument = getWindow();
            }
            for (final java.util.Map.Entry<Object, Object> entry : map_.entrySet()) {
                callback.call(Context.getCurrentContext(), getParentScope(), thisArgument,
                        new Object[] {entry.getValue(), entry.getKey(), this});
            }
        }
    }

}
