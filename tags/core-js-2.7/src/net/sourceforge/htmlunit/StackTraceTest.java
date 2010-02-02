package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

/**
 * In certain cases stacktrace if lost in case of exception
 * https://bugzilla.mozilla.org/show_bug.cgi?id=376717
 * @author Marc Guillemot
 */
public class StackTraceTest
{
    
	/**
	 * As of CVS head on May, 11. 2009, stacktrace information is lost when a call to some
	 * native function has been made.
	 */
	@Test
    public void testFailureStackTrace() {
        final String source1 = "function f2() { throw 'hello'; }; f2();";
        final String source2 = "function f2() { 'H'.toLowerCase(); throw 'hello'; }; f2();";
        
        runWithExpectedStackTrace(source1, "	at test.js (f2)\n\tat test.js\n"); // works
        runWithExpectedStackTrace(source2, "	at test.js (f2)\n\tat test.js\n"); // fails
    }

	private void runWithExpectedStackTrace(final String _source, final String _expectedStackTrace)
	{
        final ContextAction action = new ContextAction() {
        	public Object run(final Context cx) {
        		final Scriptable scope = cx.initStandardObjects();
        		try {
        			cx.evaluateString(scope, _source, "test.js", 0, null);
        		}
        		catch (final JavaScriptException e)
        		{
        			Assert.assertEquals(_expectedStackTrace, e.getScriptStackTrace());
        			return null;
        		}
        		throw new RuntimeException("Exception expected!");
        	}
        };
        Utils.runWithOptimizationLevel(action, -1);
	}
 }
