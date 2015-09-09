/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.gargoylesoftware.js.nashorn.internal.codegen;

import static com.gargoylesoftware.js.nashorn.internal.codegen.CompilerConstants.SCOPE;

import java.util.List;
import com.gargoylesoftware.js.nashorn.internal.codegen.types.Type;
import com.gargoylesoftware.js.nashorn.internal.runtime.PropertyMap;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

/**
 * Base class for object creation code generation.
 * @param <T> value type
 */
public abstract class ObjectCreator<T> {

    /** List of keys & symbols to initiate in this ObjectCreator */
    final List<MapTuple<T>> tuples;

    /** Code generator */
    final CodeGenerator codegen;

    /** Property map */
    protected PropertyMap   propertyMap;

    private final boolean       isScope;
    private final boolean       hasArguments;

    /**
     * Constructor
     *
     * @param codegen      the code generator
     * @param tuples       key,symbol,value (optional) tuples
     * @param isScope      is this object scope
     * @param hasArguments does the created object have an "arguments" property
     */
    ObjectCreator(final CodeGenerator codegen, final List<MapTuple<T>> tuples, final boolean isScope, final boolean hasArguments) {
        this.codegen       = codegen;
        this.tuples        = tuples;
        this.isScope       = isScope;
        this.hasArguments  = hasArguments;
    }

    /**
     * Generate code for making the object.
     * @param method Script method.
     */
    protected abstract void makeObject(final MethodEmitter method);

    /**
     * Construct the property map appropriate for the object.
     * @return the newly created property map
     */
    protected abstract PropertyMap makeMap();

    /**
     * Create a new MapCreator
     * @param clazz type of MapCreator
     * @return map creator instantiated by type
     */
    protected MapCreator<?> newMapCreator(final Class<? extends ScriptObject> clazz) {
        return new MapCreator<>(clazz, tuples);
    }

    /**
     * Loads the scope on the stack through the passed method emitter.
     * @param method the method emitter to use
     */
    protected void loadScope(final MethodEmitter method) {
        method.loadCompilerConstant(SCOPE);
    }

    /**
     * Emit the correct map for the object.
     * @param method method emitter
     * @return the method emitter
     */
    protected MethodEmitter loadMap(final MethodEmitter method) {
        codegen.loadConstant(propertyMap);
        return method;
    }

    PropertyMap getMap() {
        return propertyMap;
    }

    /**
     * Is this a scope object
     * @return true if scope
     */
    protected boolean isScope() {
        return isScope;
    }

    /**
     * Does the created object have an "arguments" property
     * @return true if has an "arguments" property
     */
    protected boolean hasArguments() {
        return hasArguments;
    }

    /**
     * Technique for loading an initial value. Defined by anonymous subclasses in code gen.
     *
     * @param value Value to load.
     * @param type the type of the value to load
     */
    protected abstract void loadValue(T value, Type type);

    MethodEmitter loadTuple(final MethodEmitter method, final MapTuple<T> tuple, final boolean pack) {
        loadValue(tuple.value, tuple.type);
        if (pack && codegen.useDualFields() && tuple.isPrimitive()) {
            method.pack();
        } else {
            method.convert(Type.OBJECT);
        }
        return method;
    }

    MethodEmitter loadTuple(final MethodEmitter method, final MapTuple<T> tuple) {
        return loadTuple(method, tuple, true);
    }

    /**
     * If using optimistic typing, let the code generator realize that the newly created object on the stack
     * when DUP-ed will be the same value. Basically: {NEW, DUP, INVOKESPECIAL init, DUP} will leave a stack
     * load specification {unknown, unknown} on stack (that is "there's two values on the stack, but neither
     * comes from a known local load"). If there's an optimistic operation in the literal initializer,
     * OptimisticOperation.storeStack will allocate two temporary locals for it and store them as
     * {ASTORE 4, ASTORE 3}. If we instead do {NEW, DUP, INVOKESPECIAL init, ASTORE 3, ALOAD 3, DUP} we end up
     * with stack load specification {ALOAD 3, ALOAD 3} (as DUP can track that the value it duplicated came
     * from a local load), so if/when a continuation needs to be recreated from it, it'll be
     * able to emit ALOAD 3, ALOAD 3 to recreate the stack. If we didn't do this, deoptimization within an
     * object literal initialization could in rare cases cause an incompatible change in the shape of the
     * local variable table for the temporaries, e.g. in the following snippet where a variable is reassigned
     * to a wider type in an object initializer:
     * <code>var m = 1; var obj = {p0: m, p1: m = "foo", p2: m}</code>
     * @param method the current method emitter.
     */
    void helpOptimisticRecognizeDuplicateIdentity(final MethodEmitter method) {
        if (codegen.useOptimisticTypes()) {
            final Type objectType = method.peekType();
            final int tempSlot = method.defineTemporaryLocalVariable(objectType.getSlots());
            method.storeHidden(objectType, tempSlot);
            method.load(objectType, tempSlot);
        }
    }
}
