/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1997-1999
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU General Public License Version 2 or later (the "GPL"), in which
 * case the provisions of the GPL are applicable instead of those above. If
 * you wish to allow use of your version of this file only under the terms of
 * the GPL and not to allow others to use your version of this file under the
 * MPL, indicate your decision by deleting the provisions above and replacing
 * them with the notice and other provisions required by the GPL. If you do
 * not delete the provisions above, a recipient may use your version of this
 * file under either the MPL or the GPL.
 *
 * ***** END LICENSE BLOCK ***** */

import org.mozilla.javascript.*;

/**
 * An example host object class.
 *
 * Here's a shell session showing the Foo object in action:
 * <pre>
 * js> defineClass("Foo")
 * js> foo = new Foo();         <i>A constructor call, see <a href="#Foo">Foo</a> below.</i>
 * [object Foo]                 <i>The "Foo" here comes from <a href"#getClassName">getClassName</a>.</i>
 * js> foo.counter;             <i>The counter property is defined by the <code>defineProperty</code></i>
 * 0                            <i>call below and implemented by the <a href="#getCounter">getCounter</a></i>
 * js> foo.counter;             <i>method below.</i>
 * 1
 * js> foo.counter;
 * 2
 * js> foo.resetCounter();      <i>Results in a call to <a href="#resetCounter">resetCounter</a>.</i>
 * js> foo.counter;             <i>Now the counter has been reset.</i>
 * 0
 * js> foo.counter;
 * 1
 * js> bar = new Foo(37);       <i>Create a new instance.</i>
 * [object Foo]
 * js> bar.counter;             <i>This instance's counter is distinct from</i>
 * 37                           <i>the other instance's counter.</i>
 * js> foo.varargs(3, "hi");    <i>Calls <a href="#varargs">varargs</a>.</i>
 * this = [object Foo]; args = [3, hi]
 * js> foo[7] = 34;             <i>Since we extended ScriptableObject, we get</i>
 * 34                           <i>all the behavior of a JavaScript object</i>
 * js> foo.a = 23;              <i>for free.</i>
 * 23
 * js> foo.a + foo[7];
 * 57
 * js>
 * </pre>
 *
 * @see org.mozilla.javascript.Context
 * @see org.mozilla.javascript.Scriptable
 * @see org.mozilla.javascript.ScriptableObject
 *
 * @author Norris Boyd
 */

public class Foo extends ScriptableObject {
    private static final long serialVersionUID = -3833489808933339159L;

    /**
     * The zero-parameter constructor.
     *
     * When Context.defineClass is called with this class, it will
     * construct Foo.prototype using this constructor.
     */
    public Foo() {
    }

    /**
     * The Java method defining the JavaScript Foo constructor.
     *
     * Takes an initial value for the counter property.
     * Note that in the example Shell session above, we didn't
     * supply a argument to the Foo constructor. This means that
     * the Undefined value is used as the value of the argument,
     * and when the argument is converted to an integer, Undefined
     * becomes 0.
     */
    public Foo(int counterStart) {
        counter = counterStart;
    }

    /**
     * Returns the name of this JavaScript class, "Foo".
     */
    @Override
    public String getClassName() {
        return "Foo";
    }

    /**
     * The Java method defining the JavaScript resetCounter function.
     *
     * Resets the counter to 0.
     */
    public void jsFunction_resetCounter() {
        counter = 0;
    }

    /**
     * The Java method implementing the getter for the counter property.
     * <p>
     * If "setCounter" had been defined in this class, the runtime would
     * call the setter when the property is assigned to.
     */
    public int jsGet_counter() {
        return counter++;
    }

    /**
     * An example of a variable-arguments method.
     *
     * All variable arguments methods must have the same number and
     * types of parameters, and must be static. <p>
     * @param cx the Context of the current thread
     * @param thisObj the JavaScript 'this' value.
     * @param args the array of arguments for this call
     * @param funObj the function object of the invoked JavaScript function
     *               This value is useful to compute a scope using
     *               Context.getTopLevelScope().
     * @return computes the string values and types of 'this' and
     * of each of the supplied arguments and returns them in a string.
     *
     * @see org.mozilla.javascript.ScriptableObject#getTopLevelScope
     */
    public static Object jsFunction_varargs(Context cx, Scriptable thisObj,
                                            Object[] args, Function funObj)
    {
        StringBuffer buf = new StringBuffer();
        buf.append("this = ");
        buf.append(Context.toString(thisObj));
        buf.append("; args = [");
        for (int i=0; i < args.length; i++) {
            buf.append(Context.toString(args[i]));
            if (i+1 != args.length)
                buf.append(", ");
        }
        buf.append("]");
        return buf.toString();
    }

    /**
     * A piece of private data for this class.
     */
    private int counter;
}

