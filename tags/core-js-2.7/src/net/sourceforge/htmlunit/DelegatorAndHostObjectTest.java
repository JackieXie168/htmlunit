package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Delegator;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


/**
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=539875">Bug 539875 -
 * Delegator incompatible with host objects with functions</a>.
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
public class DelegatorAndHostObjectTest extends ScriptableObject {

    private int x;

    @Override
    public String getClassName() {
        return getClass().getSimpleName();
    }

    public Delegator jsFunction_createDelegator() {
        return new Delegator(this);
    }

    public int jsFunction_foo() {
        return 42;
    }

    public int jsGet_x() {
        return x;
    }

    public void jsSet_x(int x) {
        this.x = x;
    }

    @Test
    public void delegatorAndHostObjectFunction() {
        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initStandardObjects();
                    ScriptableObject.defineClass(scope, DelegatorAndHostObjectTest.class);
                    final Object o = cx.evaluateString(scope, "new DelegatorAndHostObjectTest().createDelegator().foo()", "test_script", 1, null);
                    Assert.assertEquals(42, o);
                    return o;
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        
        Utils.runWithAllOptimizationLevels(action);
    }

    @Test
    public void delegatorAndHostObjectGetterSetter() {
        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initStandardObjects();
                    ScriptableObject.defineClass(scope, DelegatorAndHostObjectTest.class);
                    final Object o = cx.evaluateString(scope, "var t = new DelegatorAndHostObjectTest().createDelegator(); t.x = 12; t.x;", "test_script", 1, null);
                    Assert.assertEquals(12, o);
                    return o;
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Utils.runWithAllOptimizationLevels(action);
    }

}