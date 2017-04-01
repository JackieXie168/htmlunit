/*
 * Copyright (c) 2016-2017 Gargoyle Software Inc.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (http://www.gnu.org/licenses/).
 */
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

package com.gargoylesoftware.js.nashorn.internal.tools.nasgen;

import static com.gargoylesoftware.js.nashorn.internal.tools.nasgen.StringConstants.$CLINIT$;
import static com.gargoylesoftware.js.nashorn.internal.tools.nasgen.StringConstants.CLINIT;
import static com.gargoylesoftware.js.nashorn.internal.tools.nasgen.StringConstants.DEFAULT_INIT_DESC;
import static com.gargoylesoftware.js.nashorn.internal.tools.nasgen.StringConstants.INIT;
import static com.gargoylesoftware.js.nashorn.internal.tools.nasgen.StringConstants.OBJECT_DESC;
import static com.gargoylesoftware.js.nashorn.internal.tools.nasgen.StringConstants.SCRIPTOBJECT_TYPE;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckClassAdapter;

import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.tools.nasgen.MemberInfo.Kind;

/**
 * This class instruments the java class annotated with @ScriptClass.
 *
 * Changes done are:
 *
 * 1) remove all jdk2.nashorn.internal.objects.annotations.* annotations.
 * 2) static final @Property fields stay here. Other @Property fields moved to
 *    respective classes depending on 'where' value of annotation.
 * 2) add "Map" type static field named "$map".
 * 3) add static initializer block to initialize map.
 */

public class ScriptClassJavaInstrumentor extends ClassVisitor {
    private final ScriptClassInfo scriptClassInfo;
    private final int memberCount;
    private boolean staticInitFound;

    ScriptClassJavaInstrumentor(final ClassVisitor visitor, final ScriptClassInfo sci) {
        super(Opcodes.ASM4, visitor);
        if (sci == null) {
            throw new IllegalArgumentException("Null ScriptClassInfo, is the class annotated?");
        }
        this.scriptClassInfo = sci;
        this.memberCount = scriptClassInfo.getInstancePropertyCount();
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        if (ScriptClassInfo.annotations.containsKey(desc)) {
            // ignore @ScriptClass
            return null;
        }

        return super.visitAnnotation(desc, visible);
    }

    @Override
    public FieldVisitor visitField(final int fieldAccess, final String fieldName,
            final String fieldDesc, final String signature, final Object value) {
        final MemberInfo memInfo = scriptClassInfo.find(fieldName, fieldDesc, fieldAccess);
        if (memInfo != null && memInfo.getKind() == Kind.PROPERTY &&
                memInfo.getWhere() != Where.INSTANCE && !memInfo.isStaticFinal()) {
            // non-instance @Property fields - these have to go elsewhere unless 'static final'
            return null;
        }

        final FieldVisitor delegateFV = super.visitField(fieldAccess, fieldName, fieldDesc,
                signature, value);
        return new FieldVisitor(Opcodes.ASM4, delegateFV) {
            @Override
            public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
                if (ScriptClassInfo.annotations.containsKey(desc)) {
                    // ignore script field annotations
                    return null;
                }

                return fv.visitAnnotation(desc, visible);
            }

            @Override
            public void visitAttribute(final Attribute attr) {
                fv.visitAttribute(attr);
            }

            @Override
            public void visitEnd() {
                fv.visitEnd();
            }
        };
    }

    @Override
    public MethodVisitor visitMethod(final int methodAccess, final String methodName,
            final String methodDesc, final String signature, final String[] exceptions) {

        final boolean isConstructor = INIT.equals(methodName);
        final boolean isStaticInit  = CLINIT.equals(methodName);

        if (isStaticInit) {
            staticInitFound = true;
        }

        final MethodGenerator delegateMV = new MethodGenerator(super.visitMethod(methodAccess, methodName, methodDesc,
                signature, exceptions), methodAccess, methodName, methodDesc);

        return new MethodVisitor(Opcodes.ASM4, delegateMV) {
            @Override
            public void visitInsn(final int opcode) {
                // call $clinit$ just before return from <clinit>
                if (isStaticInit && opcode == RETURN) {
                    super.visitMethodInsn(INVOKESTATIC, scriptClassInfo.getJavaName(),
                            $CLINIT$, DEFAULT_INIT_DESC, false);
                }
                super.visitInsn(opcode);
            }

            @Override
            public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                if (isConstructor && opcode == INVOKESPECIAL &&
                        INIT.equals(name) && SCRIPTOBJECT_TYPE.equals(owner)) {
                    super.visitMethodInsn(opcode, owner, name, desc, false);

                    if (memberCount > 0) {
                        // initialize @Property fields if needed
                        for (final MemberInfo memInfo : scriptClassInfo.getMembers()) {
                            if (memInfo.isInstanceProperty() && !memInfo.getInitClass().isEmpty()) {
                                final String clazz = memInfo.getInitClass();
                                super.visitVarInsn(ALOAD, 0);
                                super.visitTypeInsn(NEW, clazz);
                                super.visitInsn(DUP);
                                super.visitMethodInsn(INVOKESPECIAL, clazz,
                                    INIT, DEFAULT_INIT_DESC, false);
                                super.visitFieldInsn(PUTFIELD, scriptClassInfo.getJavaName(),
                                    memInfo.getJavaName(), memInfo.getJavaDesc());
                            }

                            if (memInfo.isInstanceFunction()) {
                                super.visitVarInsn(ALOAD, 0);
                                ClassJavaGenerator.newFunction(delegateMV, scriptClassInfo.getJavaName(), memInfo, scriptClassInfo.findSpecializations(memInfo.getJavaName()));
                                super.visitFieldInsn(PUTFIELD, scriptClassInfo.getJavaName(),
                                    memInfo.getJavaName(), OBJECT_DESC);
                            }
                        }
                    }
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }
            }

            @Override
            public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
                if (ScriptClassInfo.annotations.containsKey(desc)) {
                    // ignore script method annotations
                    return null;
                }
                return super.visitAnnotation(desc, visible);
            }
        };
    }

    @Override
    public void visitEnd() {
        emitFields();
        emitStaticInitializer();
        emitGettersSetters();
        super.visitEnd();
    }

    private void emitFields() {
        // introduce "Function" type instance fields for each
        // instance @Function in script class info
        final String className = scriptClassInfo.getJavaName();
        for (MemberInfo memInfo : scriptClassInfo.getMembers()) {
            if (memInfo.isInstanceFunction()) {
                ClassJavaGenerator.addFunctionField(cv, memInfo.getJavaName());
                memInfo = (MemberInfo)memInfo.clone();
                memInfo.setJavaDesc(OBJECT_DESC);
                ClassJavaGenerator.addGetter(cv, className, memInfo);
                ClassJavaGenerator.addSetter(cv, className, memInfo);
            }
        }
        // omit addMapField() since instance classes already define a static PropertyMap field
    }

    void emitGettersSetters() {
        if (memberCount > 0) {
            for (final MemberInfo memInfo : scriptClassInfo.getMembers()) {
                final String className = scriptClassInfo.getJavaName();
                if (memInfo.isInstanceProperty()) {
                    ClassJavaGenerator.addGetter(cv, className, memInfo);
                    if (! memInfo.isFinal()) {
                        ClassJavaGenerator.addSetter(cv, className, memInfo);
                    }
                }
            }
        }
    }

    private void emitStaticInitializer() {
        final String className = scriptClassInfo.getJavaName();
        if (! staticInitFound) {
            // no user written <clinit> and so create one
            final MethodVisitor mv = ClassJavaGenerator.makeStaticInitializer(this);
            mv.visitCode();
            mv.visitInsn(RETURN);
            mv.visitMaxs(Short.MAX_VALUE, 0);
            mv.visitEnd();
        }
        // Now generate $clinit$
        final MethodGenerator mi = ClassJavaGenerator.makeStaticInitializer(this, $CLINIT$);
        ClassJavaGenerator.emitStaticInitPrefix(mi, className, memberCount);
        if (memberCount > 0) {
            for (final MemberInfo memInfo : scriptClassInfo.getMembers()) {
                if (memInfo.isInstanceProperty() || memInfo.isInstanceFunction()) {
                    ClassJavaGenerator.linkerAddGetterSetter(mi, className, memInfo);
                } else if (memInfo.isInstanceGetter()) {
                    final MemberInfo setter = scriptClassInfo.findSetter(memInfo);
                    ClassJavaGenerator.linkerAddGetterSetter(mi, className, memInfo, setter);
                }
            }
        }
        ClassJavaGenerator.emitStaticInitSuffix(mi, className);
    }

    /**
     * External entry point for ScriptClassInfoCollector if run from the command line
     *
     * @param args arguments - one argument is needed, the name of the class to collect info from
     *
     * @throws IOException if there are problems reading class
     */
    public static void main(final String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: " + ScriptClassJavaInstrumentor.class.getName() + " <class>");
            System.exit(1);
        }
        final String fileName = args[0].replace('.', '/');
        System.out.println(getString(fileName + ".class"));
    }

    public static String getString(final String fileName) throws IOException {
        final ScriptClassInfo sci = ClassJavaGenerator.getScriptClassInfo(fileName);
        if (sci == null) {
            System.err.println("No @ScriptClass in " + fileName);
            System.exit(2);
            throw new AssertionError(); //guard against warning that sci is null below
        }

        try {
            sci.verify();
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            System.exit(3);
        }

        final ClassWriter writer = ClassJavaGenerator.makeClassWriter();
        try (final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName))) {
            final ClassReader reader = new ClassReader(bis);
            final CheckClassAdapter checker = new CheckClassAdapter(writer);
            final ScriptClassJavaInstrumentor instr = new ScriptClassJavaInstrumentor(checker, sci);
            reader.accept(instr, 0);
        }

        String name = sci.getJavaName();
        name = name.substring(name.lastIndexOf('/') + 1);
        return ClassJavaGenerator.getCode(name);
    }
}
