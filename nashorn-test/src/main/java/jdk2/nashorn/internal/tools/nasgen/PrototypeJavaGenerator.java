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

package jdk2.nashorn.internal.tools.nasgen;

import static jdk.internal.org.objectweb.asm.Opcodes.ACC_FINAL;
import static jdk.internal.org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static jdk.internal.org.objectweb.asm.Opcodes.ACC_SUPER;
import static jdk.internal.org.objectweb.asm.Opcodes.V1_7;
import static jdk2.nashorn.internal.tools.nasgen.StringConstants.DEFAULT_INIT_DESC;
import static jdk2.nashorn.internal.tools.nasgen.StringConstants.INIT;
import static jdk2.nashorn.internal.tools.nasgen.StringConstants.OBJECT_DESC;
import static jdk2.nashorn.internal.tools.nasgen.StringConstants.PROPERTYMAP_DESC;
import static jdk2.nashorn.internal.tools.nasgen.StringConstants.PROPERTYMAP_FIELD_NAME;
import static jdk2.nashorn.internal.tools.nasgen.StringConstants.PROTOTYPEOBJECT_TYPE;
import static jdk2.nashorn.internal.tools.nasgen.StringConstants.PROTOTYPE_SUFFIX;
import static jdk2.nashorn.internal.tools.nasgen.StringConstants.SCRIPTOBJECT_INIT_DESC;
import static jdk2.nashorn.internal.tools.nasgen.StringConstants.TYPE_SCRIPTFUNCTION;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class generates prototype class for a @ClassInfo annotated class.
 *
 */
public class PrototypeJavaGenerator extends ClassJavaGenerator {
    private final ScriptClassInfo scriptClassInfo;
    private final String className;
    private final int memberCount;

    PrototypeJavaGenerator(final ScriptClassInfo sci) {
        this.scriptClassInfo = sci;
        this.className = scriptClassInfo.getPrototypeClassName();
        this.memberCount = scriptClassInfo.getPrototypeMemberCount();
    }

    String getClassCode() {
        // new class extensing from ScriptObject
        builder.append("    static final class Prototype extends PrototypeObject {" + System.lineSeparator());
        cw.visit(V1_7, ACC_FINAL | ACC_SUPER, className, null, PROTOTYPEOBJECT_TYPE, null);
        if (memberCount > 0) {
            // add fields
            emitFields();
            // add <clinit>
            emitStaticInitializer();
        }

        // add <init>
        emitConstructor();

        // add getClassName()
        emitGetClassName(scriptClassInfo.getName());

        cw.visitEnd();
        builder.append("        private static MethodHandle virtualHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {" + System.lineSeparator());
        builder.append("            try {" + System.lineSeparator());
        builder.append("                return MethodHandles.lookup().findVirtual(Prototype.class, name," + System.lineSeparator());
        builder.append("                        MethodType.methodType(rtype, ptypes));" + System.lineSeparator());
        builder.append("            }" + System.lineSeparator());
        builder.append("            catch (final ReflectiveOperationException e) {" + System.lineSeparator());
        builder.append("                throw new IllegalStateException(e);" + System.lineSeparator());
        builder.append("            }" + System.lineSeparator());
        builder.append("        }" + System.lineSeparator());
        builder.append("    }" + System.lineSeparator());
        return builder.toString();
    }

    // --Internals only below this point
    private void emitFields() {
        // introduce "Function" type instance fields for each
        // prototype @Function in script class info
        for (MemberInfo memInfo : scriptClassInfo.getMembers()) {
            if (memInfo.isPrototypeFunction()) {
                addField(cw, memInfo.getJavaName(), TYPE_SCRIPTFUNCTION.getDescriptor());
            } else if (memInfo.isPrototypeProperty()) {
                if (memInfo.isStaticFinal()) {
                } else {
                    addField(memInfo.getJavaName(), memInfo.getJavaDesc());
                }
            }
        }

        addMapField();
        builder.append(System.lineSeparator());

        for (MemberInfo memInfo : scriptClassInfo.getMembers()) {
            if (memInfo.isPrototypeFunction()) {
                memInfo = (MemberInfo)memInfo.clone();
                memInfo.setJavaDesc(OBJECT_DESC);
                addGetter(className, memInfo);
                addSetter(className, memInfo);
            } else if (memInfo.isPrototypeProperty()) {
                if (memInfo.isStaticFinal()) {
                    addGetter(scriptClassInfo.getJavaName(), memInfo);
                } else {
                    memInfo = (MemberInfo)memInfo.clone();
                    memInfo.setJavaAccess(ACC_PUBLIC);
                    addGetter(className, memInfo);
                    addSetter(className, memInfo);
                }
            }
        }
}

    private void emitStaticInitializer() {
        final MethodGenerator mi = makeStaticInitializer();
        builder.append("        static {" + System.lineSeparator());
        emitStaticInitPrefix(mi, className, memberCount);

        for (final MemberInfo memInfo : scriptClassInfo.getMembers()) {
            if (memInfo.isPrototypeFunction() || memInfo.isPrototypeProperty()) {
                linkerAddGetterSetter(mi, className, memInfo);
            } else if (memInfo.isPrototypeGetter()) {
                final MemberInfo setter = scriptClassInfo.findSetter(memInfo);
                linkerAddGetterSetter(mi, scriptClassInfo.getJavaName(), memInfo, setter);
            }
        }
        emitStaticInitSuffix(mi, className);
        builder.append("        }" + System.lineSeparator());
        builder.append(System.lineSeparator());
    }

    private void emitConstructor() {
        final MethodGenerator mi = makeConstructor();
        builder.append("        Prototype() {" + System.lineSeparator());
        mi.visitCode();
        mi.loadThis();
        if (memberCount > 0) {
            // call "super(map$)"
            mi.getStatic(className, PROPERTYMAP_FIELD_NAME, PROPERTYMAP_DESC);
            builder.append("            super(" + PROPERTYMAP_FIELD_NAME + ");" + System.lineSeparator());
            // make sure we use duplicated PropertyMap so that original map
            // stays intact and so can be used for many global.
            mi.invokeSpecial(PROTOTYPEOBJECT_TYPE, INIT, SCRIPTOBJECT_INIT_DESC);
            // initialize Function type fields
            initFunctionFields(mi);
        } else {
            // call "super()"
            mi.invokeSpecial(PROTOTYPEOBJECT_TYPE, INIT, DEFAULT_INIT_DESC);
        }
        mi.returnVoid();
        mi.computeMaxs();
        mi.visitEnd();
        builder.append("        }" + System.lineSeparator());
        builder.append(System.lineSeparator());
    }

    private void initFunctionFields(final MethodGenerator mi) {
        for (final MemberInfo memInfo : scriptClassInfo.getMembers()) {
            if (! memInfo.isPrototypeFunction()) {
                continue;
            }
            mi.loadThis();
            builder.append("            " + memInfo.getJavaName() + " = ");
            newFunction(mi, scriptClassInfo.getJavaName(), memInfo, scriptClassInfo.findSpecializations(memInfo.getJavaName()));
            mi.putField(className, memInfo.getJavaName(), OBJECT_DESC);
        }
    }

    /**
     * External entry point for PrototypeGenerator if called from the command line
     *
     * @param args arguments, takes 1 argument which is the class to process
     * @throws IOException if class cannot be read
     */
    public static void main(final String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: " + PrototypeJavaGenerator.class.getName() + " <class>");
            System.exit(1);
        }

        final String className = args[0].replace('.', '/') + ".class";
    }
    public static String getString(final String className) throws IOException {
        final ScriptClassInfo sci = getScriptClassInfo(className);
        if (sci == null) {
            System.err.println("No @ScriptClass in " + className);
            System.exit(2);
            throw new AssertionError(); //guard against warning that sci is null below
        }
        try {
            sci.verify();
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            System.exit(3);
        }
        final PrototypeJavaGenerator gen = new PrototypeJavaGenerator(sci);
        return gen.getClassCode();
    }
}
