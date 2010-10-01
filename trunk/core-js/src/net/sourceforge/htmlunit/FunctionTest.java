package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Scriptable;

/**
 * Unit tests for Function.
 * @author Marc Guillemot
 */
public class FunctionTest {
    
	/**
	 * Test for bug #600479
	 * https://bugzilla.mozilla.org/show_bug.cgi?id=600479
	 * Syntax of function built from Function's constructor string parameter was not correct
	 * when this string contained "//".  
	 */
	@Test
    public void testFunctionWithSlashSlash() {
        assertEvaluates(true, "new Function('return true//;').call()");
    }

    private void assertEvaluates(final Object expected, final String source) {
        final ContextAction action = new ContextAction() {
            public Object run(Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object rep = cx.evaluateString(scope, source, "test.js",
                        0, null);
                Assert.assertEquals(expected, rep);
                return null;
            }
        };
        Utils.runWithAllOptimizationLevels(action);
    }
 }
