/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id$
 */

package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 * @author Erwin Bolwidt <ejb@klomp.org>
 */
final class ParameterRef extends VariableRefBase {

    /**
     * Name of param being referenced.
     */
    QName _name = null;
    
    public ParameterRef(Param param) {
	super(param);
        _name = param._name;
        
    }

    public String toString() {
	return "parameter-ref("+_variable.getName()+'/'+_variable.getType()+')';
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

        /*
         * To fix bug 24518 related to setting parameters of the form
         * {namespaceuri}localName, which will get mapped to an instance 
         * variable in the class.
         */
        final String name = BasisLibrary.mapQNameToJavaName (_name.toString());
	final String signature = _type.toSignature();

	if (_variable.isLocal()) {
	    if (classGen.isExternal()) {
		Closure variableClosure = _closure;
		while (variableClosure != null) {
		    if (variableClosure.inInnerClass()) break;
		    variableClosure = variableClosure.getParentClosure();
		}
	    
		if (variableClosure != null) {
		    il.append(ALOAD_0);
		    il.append(new GETFIELD(
			cpg.addFieldref(variableClosure.getInnerClassName(), 
			    name, signature)));
		}
		else {
		    il.append(_variable.loadInstruction());
		}
	    }
	    else {
		il.append(_variable.loadInstruction());
	    }
	}
	else {
	    final String className = classGen.getClassName();
	    il.append(classGen.loadTranslet());
	    if (classGen.isExternal()) {
		il.append(new CHECKCAST(cpg.addClass(className)));
	    }
	    il.append(new GETFIELD(cpg.addFieldref(className,name,signature)));
	}

	if (_variable.getType() instanceof NodeSetType) {
	    // The method cloneIterator() also does resetting
	    final int clone = cpg.addInterfaceMethodref(NODE_ITERATOR,
						       "cloneIterator",
						       "()" + 
							NODE_ITERATOR_SIG);
	    il.append(new INVOKEINTERFACE(clone, 1));
	}
    }
}
