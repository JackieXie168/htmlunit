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

import static com.gargoylesoftware.js.nashorn.internal.runtime.linker.NashornCallSiteDescriptor.CALLSITE_OPTIMISTIC;

import java.util.Arrays;
import java.util.EnumSet;
import com.gargoylesoftware.js.nashorn.internal.codegen.types.Type;
import com.gargoylesoftware.js.nashorn.internal.ir.Symbol;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

/**
 * A scope call or get operation that can be shared by several callsites. This generates a static
 * method that wraps the invokedynamic instructions to get or call scope variables.
 * The rationale for this is that initial linking of invokedynamic callsites is expensive,
 * so by sharing them we can reduce startup overhead and allow very large scripts to run that otherwise wouldn't.
 *
 * <p>Static methods generated by this class expect two parameters in addition to the parameters of the
 * function call: The current scope object and the depth of the target scope relative to the scope argument
 * for when this is known at compile-time (fast-scope access).</p>
 *
 * <p>The second argument may be -1 for non-fast-scope symbols, in which case the scope chain is checked
 * for each call. This may cause callsite invalidation when the shared method is used from different
 * scopes, but such sharing of non-fast scope calls may still be necessary for very large scripts.</p>
 *
 * <p>Scope calls must not be shared between normal callsites and callsites contained in a <tt>with</tt>
 * statement as this condition is not handled by current guards and will cause a runtime error.</p>
 */
class SharedScopeCall {

    /** Threshold for using shared scope calls with fast scope access. */
    public static final int FAST_SCOPE_CALL_THRESHOLD = 4;
    /** Threshold for using shared scope calls with slow scope access. */
    public static final int SLOW_SCOPE_CALL_THRESHOLD = 500;
    /** Threshold for using shared scope gets with fast scope access. */
    public static final int FAST_SCOPE_GET_THRESHOLD  = 200;

    final Type valueType;
    final Symbol symbol;
    final Type returnType;
    final Type[] paramTypes;
    final int flags;
    final boolean isCall;
    private CompileUnit compileUnit;
    private String methodName;
    private String staticSignature;

    /**
     * Constructor.
     *
     * @param symbol the symbol
     * @param valueType the type of the value
     * @param returnType the return type
     * @param paramTypes the function parameter types
     * @param flags the callsite flags
     */
    SharedScopeCall(final Symbol symbol, final Type valueType, final Type returnType, final Type[] paramTypes, final int flags) {
        this.symbol = symbol;
        this.valueType = valueType;
        this.returnType = returnType;
        this.paramTypes = paramTypes;
        assert (flags & CALLSITE_OPTIMISTIC) == 0;
        this.flags = flags;
        // If paramTypes is not null this is a call, otherwise it's just a get.
        this.isCall = paramTypes != null;
    }

    @Override
    public int hashCode() {
        return symbol.hashCode() ^ returnType.hashCode() ^ Arrays.hashCode(paramTypes) ^ flags;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SharedScopeCall) {
            final SharedScopeCall c = (SharedScopeCall) obj;
            return symbol.equals(c.symbol)
                    && flags == c.flags
                    && returnType.equals(c.returnType)
                    && Arrays.equals(paramTypes, c.paramTypes);
        }
        return false;
    }

    /**
     * Set the compile unit and method name.
     * @param compileUnit the compile unit
     * @param methodName the method name
     */
    protected void setClassAndName(final CompileUnit compileUnit, final String methodName) {
        this.compileUnit = compileUnit;
        this.methodName  = methodName;
    }

    /**
     * Generate the invoke instruction for this shared scope call.
     * @param method the method emitter
     * @return the method emitter
     */
    public MethodEmitter generateInvoke(final MethodEmitter method) {
        return method.invokestatic(compileUnit.getUnitClassName(), methodName, getStaticSignature());
    }

    /**
     * Generate the method that implements the scope get or call.
     */
    protected void generateScopeCall() {
        final ClassEmitter classEmitter = compileUnit.getClassEmitter();
        final EnumSet<ClassEmitter.Flag> methodFlags = EnumSet.of(ClassEmitter.Flag.STATIC);

        // This method expects two fixed parameters in addition to any parameters that may be
        // passed on to the function: A ScriptObject representing the caller's current scope object,
        // and an int specifying the distance to the target scope containing the symbol we want to
        // access, or -1 if this is not known at compile time (e.g. because of a "with" or "eval").

        final MethodEmitter method = classEmitter.method(methodFlags, methodName, getStaticSignature());
        method.begin();

        // Load correct scope by calling getProto() on the scope argument as often as specified
        // by the second argument.
        final Label parentLoopStart = new Label("parent_loop_start");
        final Label parentLoopDone  = new Label("parent_loop_done");
        method.load(Type.OBJECT, 0);
        method.label(parentLoopStart);
        method.load(Type.INT, 1);
        method.iinc(1, -1);
        method.ifle(parentLoopDone);
        method.invoke(ScriptObject.GET_PROTO);
        method._goto(parentLoopStart);
        method.label(parentLoopDone);

        assert !isCall || valueType.isObject(); // Callables are always objects
        // If flags are optimistic, but we're doing a call, remove optimistic flags from the getter, as they obviously
        // only apply to the call.
        method.dynamicGet(valueType, symbol.getName(), isCall ? CodeGenerator.nonOptimisticFlags(flags) : flags, isCall, false);

        // If this is a get we're done, otherwise call the value as function.
        if (isCall) {
            method.convert(Type.OBJECT);
            // ScriptFunction will see CALLSITE_SCOPE and will bind scope accordingly.
            method.loadUndefined(Type.OBJECT);
            int slot = 2;
            for (final Type type : paramTypes) {
                method.load(type, slot);
                slot += type.getSlots();
            }
            // Shared scope calls disabled in optimistic world. TODO is this right?
            method.dynamicCall(returnType, 2 + paramTypes.length, flags, symbol.getName());
        }

        method._return(returnType);
        method.end();
    }

    private String getStaticSignature() {
        if (staticSignature == null) {
            if (paramTypes == null) {
                staticSignature = Type.getMethodDescriptor(returnType, Type.typeFor(ScriptObject.class), Type.INT);
            } else {
                final Type[] params = new Type[paramTypes.length + 2];
                params[0] = Type.typeFor(ScriptObject.class);
                params[1] = Type.INT;
                System.arraycopy(paramTypes, 0, params, 2, paramTypes.length);
                staticSignature = Type.getMethodDescriptor(returnType, params);
            }
        }
        return staticSignature;
    }

    @Override
    public String toString() {
        return methodName + " " + staticSignature;
    }

}
