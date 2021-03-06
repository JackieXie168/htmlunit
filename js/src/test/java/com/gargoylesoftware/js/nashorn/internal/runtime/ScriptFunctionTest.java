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
package com.gargoylesoftware.js.nashorn.internal.runtime;

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;

import org.junit.Test;

import com.gargoylesoftware.js.host.MyEventTarget;
import com.gargoylesoftware.js.host.MyHTMLBodyElement;
import com.gargoylesoftware.js.host.MyHTMLDocument;
import com.gargoylesoftware.js.host.MyWindow;
import com.gargoylesoftware.js.nashorn.api.scripting.NashornScriptEngine;
import com.gargoylesoftware.js.nashorn.api.scripting.NashornScriptEngineFactory;
import com.gargoylesoftware.js.nashorn.api.scripting.ScriptObjectMirror;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Browser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser;

public class ScriptFunctionTest {

    @Test
    public void simple() throws Exception {
        final SupportedBrowser chrome = SupportedBrowser.CHROME;
        final NashornScriptEngine engine = createEngine();
        final Global global = initGlobal(engine, chrome);
        final ScriptContext scriptContext = global.getScriptContext();
        final String script = "return 'a' + 'b'";
        Object value = engine.eval(script, scriptContext);
        assertEquals("ab", value);

        final Source source = Source.sourceFor("some name", script);
        final Global oldGlobal = Context.getGlobal();
        try {
            Context.setGlobal(global);
            final ScriptFunction eventHandler = global.getContext().compileScript(source, global);
            value = ScriptRuntime.apply(eventHandler, global, "hello");
            assertEquals("ab", value);
        }
        finally {
            Context.setGlobal(oldGlobal);
        }
    }

    private static NashornScriptEngine createEngine() {
        return (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine();
    }

    private static Global initGlobal(final NashornScriptEngine engine, final SupportedBrowser browser) throws Exception {
        Browser.setCurrent(browser);
        final Global global = engine.createNashornGlobal();
        final ScriptContext scriptContext = new SimpleScriptContext();
        scriptContext.setBindings(new ScriptObjectMirror(global, global), ScriptContext.ENGINE_SCOPE);
        final Global oldGlobal = Context.getGlobal();
        try {
            Context.setGlobal(global);

            if (browser == CHROME) {
                global.put("EventTarget", new MyEventTarget.FunctionConstructor(), true);
                global.put("Window", new MyWindow.FunctionConstructor(), true);
                global.put("HTMLDocument", new MyHTMLDocument.FunctionConstructor(), true);
                global.put("HTMLBodyElement", new MyHTMLBodyElement.FunctionConstructor(), true);
                setProto(global, "Window", "EventTarget");
                final ScriptFunction parentFunction = (ScriptFunction) global.get("EventTarget");
                final PrototypeObject parentPrototype = (PrototypeObject) parentFunction.getPrototype();
                global.setProto(parentPrototype);
            }
            else {
                global.put("Window", new MyWindow.ObjectConstructor(), true);
                global.setProto(new MyEventTarget.ObjectConstructor());
            }
            global.setScriptContext(scriptContext);

            final MyWindow window = new MyWindow();
            ScriptObject windowProto = global.getPrototype(window.getClass());
            for (final Property property : windowProto.getMap().getProperties()) {
                final Object value = property.getObjectValue(windowProto, windowProto);
                global.put(property.getKey(), value, true);
            }

            global.put("window", global, true);
            global.setWindow(window);

            return global;
        }
        finally {
            Context.setGlobal(oldGlobal);
        }
    }

    private static void setProto(final Global global, final String childName, final String parentName) {
        final Object child = global.get(childName);
        if (child instanceof ScriptFunction) {
            final ScriptFunction childFunction = (ScriptFunction) global.get(childName);
            final PrototypeObject childPrototype = (PrototypeObject) childFunction.getPrototype();
            final ScriptFunction parentFunction = (ScriptFunction) global.get(parentName);
            final PrototypeObject parentPrototype = (PrototypeObject) parentFunction.getPrototype();
            childPrototype.setProto(parentPrototype);
            childFunction.setProto(parentFunction);
        }
        else {
            final ScriptObject childObject = (ScriptObject) global.get(childName);
            final ScriptObject parentObject = (ScriptObject) global.get(parentName);
            childObject.setProto(parentObject);
        }
    }

    @Test
    public void caller() throws Exception {
        final String script = ""
                + "function test() {\n"
                + "  return test2();\n"
                + "}\n"
                + "function test2() {\n"
                + "  return arguments.callee.caller;\n"
                + "}\n"
                + "test()";
        final NashornScriptEngine engine = createEngine();
        final Object value = engine.eval(script);
        assertNotNull(value);
    }

    @Test
    public void caller2() throws Exception {
        final String script = ""
                + "function test() {\n"
                + "  return test2('a');\n"
                + "}\n"
                + "function test2(a) {\n"
                + "  return a;\n"
                + "}\n"
                + "test()";
        final NashornScriptEngine engine = createEngine();
        final Object value = engine.eval(script);
        assertEquals("a", value);
    }

    @Test
    public void caller3() throws Exception {
        final String script = ""
                + "function test() {\n"
                + "  return arguments.callee.caller;\n"
                + "}\n"
                + "test()";
        final NashornScriptEngine engine = createEngine();
        final Object value = engine.eval(script);
        assertNull(value);
    }

    @Test
    public void length() throws Exception {
        final String script = ""
                + "function test() {\n"
                + "  return test2('a');\n"
                + "}\n"
                + "function test2(a) {\n"
                + "  return arguments.length;\n"
                + "}\n"
                + "test()";
        final NashornScriptEngine engine = createEngine();
        final Object value = engine.eval(script);
        assertEquals(1, value);
    }

    @Test
    public void length2() throws Exception {
        final String script = ""
                + "function test() {\n"
                + "  return test.length;\n"
                + "}\n"
                + "test()";
        final NashornScriptEngine engine = createEngine();
        final Object value = engine.eval(script);
        assertEquals(0, value);
    }

    @Test
    public void withDeclaration() throws Exception {
        final SupportedBrowser chrome = SupportedBrowser.CHROME;
        final NashornScriptEngine engine = createEngine();
        final Global global = initGlobal(engine, chrome);
        final String script = "function test(event) {return 'hello ' + event}";

        final Source source = Source.sourceFor("some name", script);
        final Global oldGlobal = Context.getGlobal();
        try {
            Context.setGlobal(global);
            final ScriptFunction eventHandler = global.getContext().compileScript(source, global);
            ScriptRuntime.apply(eventHandler, global);

            final ScriptFunction testFunction = (ScriptFunction) global.get("test");
            final Object value = ScriptRuntime.apply(testFunction, global, "there");
            assertEquals("hello there", value.toString());
        }
        finally {
            Context.setGlobal(oldGlobal);
        }
    }

    @Test
    public void useFromGlobal() throws Exception {
        final SupportedBrowser chrome = SupportedBrowser.CHROME;
        final NashornScriptEngine engine = createEngine();
        final Global global = initGlobal(engine, chrome);
        final String code1 = "function test1(name) {return test2(name)}";
        final String code2 = "function test2(event) {return 'hello ' + event}";

        final Source source1 = Source.sourceFor("some name", code1);
        final Source source2 = Source.sourceFor("some name2", code2);
        final Global oldGlobal = Context.getGlobal();
        try {
            Context.setGlobal(global);
            final ScriptFunction event1Handler = global.getContext().compileScript(source1, global);
            ScriptRuntime.apply(event1Handler, global);
            final ScriptFunction event2Handler = global.getContext().compileScript(source2, global);
            ScriptRuntime.apply(event2Handler, global);

            final ScriptFunction test1Function = (ScriptFunction) global.get("test1");
            final Object value = ScriptRuntime.apply(test1Function, global, "there");
            assertEquals("hello there", value.toString());
        }
        finally {
            Context.setGlobal(oldGlobal);
        }
    }

    @Test
    public void differentJsObjects() throws Exception {
        final SupportedBrowser chrome = SupportedBrowser.CHROME;
        final NashornScriptEngine engine = createEngine();
        final Global global = initGlobal(engine, chrome);
        final String code1 = "function test() {"
                + "  var o = {};"
                + "  o.abc = function aaa(event) {"
                + "    return 'hi ' + event + ' ' + test2();"
                + "  };"
                + "  return o.abc('there');"
                + "}"
                + "function test2() {"
                + "  return 'something';"
                + "}";

        final Source source1 = Source.sourceFor("some name", code1);
        final Global oldGlobal = Context.getGlobal();
        try {
            Context.setGlobal(global);
            final ScriptFunction event1Handler = global.getContext().compileScript(source1, global);
            ScriptRuntime.apply(event1Handler, global);

            final ScriptFunction test1Function = (ScriptFunction) global.get("test");
            final Object value = ScriptRuntime.apply(test1Function, global, "there");
            assertEquals("hi there something", value.toString());
        }
        finally {
            Context.setGlobal(oldGlobal);
        }
    }

    @Test
    public void callFromAnotherObject() throws Exception {
        final SupportedBrowser chrome = SupportedBrowser.CHROME;
        final NashornScriptEngine engine = createEngine();
        final Global global = initGlobal(engine, chrome);
        final String code1 = "function test1(name) {return test2(name)}";
        final String code2 = "function test2(event) {return 'hello ' + event}";

        final Source source1 = Source.sourceFor("some name", code1);
        final Source source2 = Source.sourceFor("some name2", code2);
        final Global oldGlobal = Context.getGlobal();
        try {
            Context.setGlobal(global);
            Context context = global.getContext();
            MyHTMLDocument document = MyWindow.getDocument(global);
            MyHTMLBodyElement body = document.getBody();
            final ScriptFunction event2Handler = context.compileScript(source2, global);
            ScriptRuntime.apply(event2Handler, global);
            ScriptFunction event1Handler = context.compileScript(source1, body);
            ScriptRuntime.apply(event1Handler, body);
            final FunctionScope functionScope = new FunctionScope(body.getMap(), global);
            event1Handler = context.compileScript(source1, functionScope);
            ScriptRuntime.apply(event1Handler, body);

            final ScriptFunction test1Function = (ScriptFunction) body.get("test1");
            final Object value = ScriptRuntime.apply(test1Function, global, "there");
            assertEquals("hello there", value.toString());
        }
        finally {
            Context.setGlobal(oldGlobal);
        }
    }

}
