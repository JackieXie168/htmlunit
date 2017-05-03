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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_ITEM_SUPPORTS_DOUBLE_INDEX_ALSO;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_ITEM_SUPPORTS_ID_SEARCH_ALSO;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_SUPPORTS_PARANTHESES;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.arrays.ObjectArrayData;

/**
 * An array of elements. Used for the element arrays returned by <tt>document.all</tt>,
 * <tt>document.all.tags('x')</tt>, <tt>document.forms</tt>, <tt>window.frames</tt>, etc.
 * Note that this class must not be used for collections that can be modified, for example
 * <tt>map.areas</tt> and <tt>select.options</tt>.
 * <br>
 * This class (like all classes in this package) is specific for the JavaScript engine.
 * Users of HtmlUnit shouldn't use it directly.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@ScriptClass
public class HTMLCollection2 extends AbstractList2 {

    /**
     * Gets an empty collection.
     * @param window the current scope
     * @return an empty collection
     */
    public static HTMLCollection2 emptyCollection(final Window2 window) {
        return null;
//        final List<Object> list = Collections.emptyList();
//        return new HTMLCollection2(window) {
//            @Override
//            public List<Object> getElements() {
//                return list;
//            }
//        };
    }

    /**
     * Creates an instance.
     */
    public HTMLCollection2() {
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     */
    public HTMLCollection2(final DomNode parentScope, final boolean attributeChangeSensitive) {
        super(parentScope, attributeChangeSensitive);
        final List<Object> list = new ArrayList<>();
        for (final Object o : computeElements()) {
            list.add(getScriptObjectForElement(o));
        }

        setArray(new ObjectArrayData(list.toArray(new Object[list.size()]), list.size()));
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param domNode the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    HTMLCollection2(final DomNode domNode, final List<DomNode> initialElements) {
        super(domNode, initialElements);
    }

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static HTMLCollection2 constructor(final boolean newObj, final Object self) {
        final HTMLCollection2 host = new HTMLCollection2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * {@inheritDoc}
     */
    @Getter
    @Override
    public final Object getLength() {
        return getElements().size();
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536460.aspx">MSDN doc</a>
     */
    @Override
    public Object item(final Object index) {
        if (index instanceof String && getBrowserVersion().hasFeature(HTMLCOLLECTION_ITEM_SUPPORTS_ID_SEARCH_ALSO)) {
            final String name = (String) index;
            return namedItem(name);
        }

        final Object object = get(((Number) index).intValue());

//        if (object == NOT_FOUND) {
//            return null;
//        }
        return object;
    }

    /**
     * Retrieves the item or items corresponding to the specified name (checks ids, and if
     * that does not work, then names).
     * @param name the name or id the element or elements to return
     * @return the element or elements corresponding to the specified name or id
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536634.aspx">MSDN doc</a>
     */
    @Function
    public Object namedItem(final String name) {
        final List<DomNode> elements = getElements();
        for (final DomNode next : elements) {
            if (next instanceof DomElement) {
                final DomElement elem = (DomElement) next;
                final String nodeName = elem.getAttribute("name");
                if (name.equals(nodeName)) {
                    return getScriptObjectForElement(elem);
                }

                final String id = elem.getAttribute("id");
                if (name.equals(id)) {
                    return getScriptObjectForElement(elem);
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getWithPreemptionByName(final String name, final List<DomNode> elements) {
        final List<DomNode> matchingElements = new ArrayList<>();
        final boolean searchName = isGetWithPreemptionSearchName();
        for (final DomNode next : elements) {
            if (next instanceof DomElement
                    && (searchName || next instanceof HtmlInput || next instanceof HtmlForm)) {
                final String nodeName = ((DomElement) next).getAttribute("name");
                if (name.equals(nodeName)) {
                    matchingElements.add(next);
                }
            }
        }

        if (matchingElements.isEmpty()) {
            if (getBrowserVersion().hasFeature(HTMLCOLLECTION_ITEM_SUPPORTS_DOUBLE_INDEX_ALSO)) {
//                final Double doubleValue = Context.toNumber(name);
//                if (!doubleValue.isNaN()) {
//                    final Object object = get(doubleValue.intValue(), this);
//                    if (object != null) {
//                        return object;
//                    }
//                }
            }
            return ScriptRuntime.UNDEFINED;
        }
        else if (matchingElements.size() == 1) {
            return getScriptObjectForElement(matchingElements.get(0));
        }

        // many elements => build a sub collection
        final DomNode domNode = getDomNodeOrNull();
        final HTMLCollection collection = new HTMLCollection(domNode, matchingElements);
        collection.setAvoidObjectDetection(true);
        return collection;
    }

    /**
     * Returns whether {@link #getWithPreemption(String)} should search by name or not.
     * @return whether {@link #getWithPreemption(String)} should search by name or not
     */
    protected boolean isGetWithPreemptionSearchName() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Object index) {
        if (supportsParanteses()) {
            return super.call(index);
        }

        throw new RuntimeException("TypeError - HTMLCollection does nont support function like access");
    }

    /**
     * Is parentheses supported.
     *
     * @return true or false
     */
    protected boolean supportsParanteses() {
        return getBrowserVersion().hasFeature(HTMLCOLLECTION_SUPPORTS_PARANTHESES);
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLCollection2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("HTMLCollection",
                    staticHandle("constructor", HTMLCollection2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("HTMLCollection");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("HTMLCollection");
        }
    }
}
