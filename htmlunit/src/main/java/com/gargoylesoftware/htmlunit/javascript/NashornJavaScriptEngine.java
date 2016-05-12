/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.InteractivePage;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.Element2;
import com.gargoylesoftware.htmlunit.javascript.host.History2;
import com.gargoylesoftware.htmlunit.javascript.host.Location2;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.CharacterData2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Text2;
import com.gargoylesoftware.htmlunit.javascript.host.event.BeforeUnloadEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.api.scripting.NashornScriptEngine;
import com.gargoylesoftware.js.nashorn.api.scripting.NashornScriptEngineFactory;
import com.gargoylesoftware.js.nashorn.api.scripting.ScriptObjectMirror;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Browser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.Property;
import com.gargoylesoftware.js.nashorn.internal.runtime.PropertyMap;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

/**
 * A wrapper for the <a href="http://openjdk.java.net/projects/nashorn/">Nashorn JavaScript engine</a>.
 *
 * @author Ahmed Ashour
 */
public class NashornJavaScriptEngine implements AbstractJavaScriptEngine {

    private static final Log LOG = LogFactory.getLog(NashornJavaScriptEngine.class);

    private final WebClient webClient_;
    final NashornScriptEngine engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine();

    /**
     * Creates an instance for the specified {@link WebClient}.
     *
     * @param webClient the client that will own this engine
     */
    public NashornJavaScriptEngine(final WebClient webClient) {
        webClient_ = webClient;
    }

    private static Browser getBrowser(final BrowserVersion version) {
        BrowserFamily family;
        if (version.isFirefox()) {
            family = BrowserFamily.FF;
        }
        else if (version.isIE()) {
            family = BrowserFamily.IE;
        }
        else if (version.isEdge()) {
            family = BrowserFamily.EDGE;
        }
        else {
            family = BrowserFamily.CHROME;
        }
        return new Browser(family, (int) version.getBrowserVersionNumeric());
    }

    private void initGlobal(final ScriptContext scriptContext, final Browser browser) {
        Browser.setCurrent(browser);
        final Global global = getGlobal(scriptContext);
        final Global oldGlobal = Context.getGlobal();
        try {
            Context.setGlobal(global);

            final BrowserFamily browserFamily = browser.getFamily();
//            boolean isConstructor = false;
//            for (final Method m : enclosingClass.getDeclaredMethods()) {
//                for (final Constructor constructor : m.getAnnotationsByType(Constructor.class)) {
//                    if (isSupported(constructor.browsers(), browserFamily, browserVersion)) {
//                        isConstructor = true;
//                    }
//                }
//            }
//            if ((isConstructor && PrototypeObject.class.isAssignableFrom(scriptObject.getClass()))
//                    || (!isConstructor && !PrototypeObject.class.isAssignableFrom(scriptObject.getClass()))) {
//            
//            }

            if (browserFamily == CHROME) {
                global.put("EventTarget", new EventTarget2.FunctionConstructor(), true);
                global.put("Window", new Window2.FunctionConstructor(), true);
                global.put("Event", new Event2.FunctionConstructor(), true);
                global.put("HTMLBodyElement", new HTMLBodyElement2.FunctionConstructor(), true);
                global.put("HTMLDivElement", new HTMLDivElement2.FunctionConstructor(), true);
                global.put("HTMLHtmlElement", new HTMLHtmlElement2.FunctionConstructor(), true);
                global.put("HTMLElement", new HTMLElement2.FunctionConstructor(), true);
                global.put("HTMLDocument", new HTMLDocument2.FunctionConstructor(), true);
                global.put("Document", new Document2.FunctionConstructor(), true);
                global.put("Element", new Element2.FunctionConstructor(), true);
                global.put("Text", new Text2.FunctionConstructor(), true);
                global.put("CharacterData", new CharacterData2.FunctionConstructor(), true);
                global.put("History", new History2.FunctionConstructor(), true);
                global.put("Node", new Node2.FunctionConstructor(), true);
                global.put("HTMLInputElement", new HTMLInputElement2.FunctionConstructor(), true);
                global.put("ComputedCSSStyleDeclaration", new ComputedCSSStyleDeclaration2.FunctionConstructor(), true);
                global.put("CSSStyleDeclaration", new CSSStyleDeclaration2.FunctionConstructor(), true);
                global.put("Location", new Location2.FunctionConstructor(), true);
                setProto(global, "Window", "EventTarget");
                setProto(global, "HTMLDocument", "Document");
                setProto(global, "HTMLBodyElement", "HTMLElement");
                setProto(global, "HTMLDivElement", "HTMLElement");
                setProto(global, "HTMLElement", "Element");
                setProto(global, "Element", "Node");
                setProto(global, "Text", "CharacterData");
                setProto(global, "CharacterData", "Node");
                setProto(global, "ComputedCSSStyleDeclaration", "CSSStyleDeclaration");
            }
            else {
                global.put("Window", new Window2.ObjectConstructor(), true);
                global.put("Event", new Event2(), true);
                global.put("BeforeUnloadEvent", new BeforeUnloadEvent2(), true);
                setProto(global, "Window", new EventTarget2.ObjectConstructor());
                setProto(global, "BeforeUnloadEvent", "Event");
            }

            final String[] toBeRemoved = {"java", "javax", "javafx", "org", "com", "net", "edu", "JavaAdapter",
                    "JSAdapter", "JavaImporter", "Packages", "arguments", "load", "loadWithNewGlobal", "exit", "quit",
                    "Java", "__noSuchProperty__", "javax.script.filename"};
            for (final String key : toBeRemoved) {
                global.remove(key, true);
            }

            final Window2 window = new Window2();
            ScriptObject windowProto = Context.getGlobal().getPrototype(window.getClass());
            if (windowProto == null) {
                windowProto = (ScriptObject) global.get("Window");
            }
            window.setProto(windowProto);
            ScriptUtils.initialize(window);

            for (final Property p : global.getMap().getProperties()) {
                //TODO: check "JSAdapter"
                final String key = p.getKey();
                window.put(key, global.get(key), true);
            }

            global.put("window", window, true);
            global.setWindow(window);

            try {
                final String[] windowToGlobalFunctions = {"alert", "atob", "btoa", "execScript", "CollectGarbage"};
                for (final String key : windowToGlobalFunctions) {
                    global.put(key, window.get(key), true);
                }

                final String[] globalToWindowFunctions = {"RegExp", "NaN", "isNaN", "Infinity", "isFinite", "eval", "print",
                        "parseInt", "parseFloat", "encodeURI", "encodeURIComponent", "decodeURI", "decodeURIComponent",
                        "escape", "unescape"};
                for (final String key : globalToWindowFunctions) {
                    window.put(key, global.get(key), true);
                }

                final String[] windowProperties = {"top", "controllers", "document", "length", "location"};
                final PropertyMap propertyMap = window.getMap();
                final List<Property> list = new ArrayList<>();
                for (final String key : windowProperties) {
                    final Property property = propertyMap.findProperty(key);
                    if (property != null) {
                        list.add(property);
                    }
                }

                global.setMap(global.getMap().addAll(PropertyMap.newMap(list)));
            }
            catch(final Exception e) {
                throw new RuntimeException(e);
            }
        }
        finally {
            Context.setGlobal(oldGlobal);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T get(final Object o, final String fieldName) {
        try {
            final Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(o);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setProto(final Global global, final String childName, final String parentName) {
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

    private void setProto(final Global global, final String childName, final ScriptObject parentObject) {
        final ScriptObject childObject = (ScriptObject) global.get(childName);
        childObject.setProto(parentObject);
    }

    @Override
    public JavaScriptConfiguration getJavaScriptConfiguration() {
        return null;
    }

    @Override
    public void addPostponedAction(PostponedAction action) {
    }

    @Override
    public void processPostponedActions() {
    }

    @Override
    public Object execute(final InteractivePage page, final String sourceCode, final String sourceName,
            final int startLine) {
        try {
            return engine.eval(sourceCode, page.getEnclosingWindow().getScriptContext());
        }
        catch(final Exception e) {
            handleJavaScriptException(new ScriptException(page, e, sourceCode), true);
            return null;
        }
    }

    /**
     * Returns the web client that this engine is associated with.
     * @return the web client
     */
    public WebClient getWebClient() {
        return webClient_;
    }

    /**
     * Handles an exception that occurred during execution of JavaScript code.
     * @param scriptException the exception
     * @param triggerOnError if true, this triggers the onerror handler
     */
    protected void handleJavaScriptException(final ScriptException scriptException, final boolean triggerOnError) {
        // Trigger window.onerror, if it has been set.
        final InteractivePage page = scriptException.getPage();
        if (triggerOnError && page != null) {
            final WebWindow window = page.getEnclosingWindow();
            if (window != null) {
                final Window2 w = (Window2) window.getScriptObject2();
                if (w != null) {
                    try {
                        w.triggerOnError(scriptException);
                    }
                    catch (final Exception e) {
                        handleJavaScriptException(new ScriptException(page, e, null), false);
                    }
                }
            }
        }
        final JavaScriptErrorListener javaScriptErrorListener = getWebClient().getJavaScriptErrorListener();
        if (javaScriptErrorListener != null) {
            javaScriptErrorListener.scriptException(page, scriptException);
        }
        // Throw a Java exception if the user wants us to.
        if (getWebClient().getOptions().isThrowExceptionOnScriptError()) {
            throw scriptException;
        }
        // Log the error; ScriptException instances provide good debug info.
        LOG.info("Caught script exception", scriptException);
    }

    @Override
    public void registerWindowAndMaybeStartEventLoop(WebWindow webWindow) {
    }

    public static Global getGlobal(final ScriptContext context) {
        return get(context.getBindings(ScriptContext.ENGINE_SCOPE), "sobj");
    }

    @Override
    public void initialize(final WebWindow webWindow) {
        final Global global = engine.createNashornGlobal();
        final ScriptContext scriptContext = webWindow.getScriptContext();
        scriptContext.setBindings(new ScriptObjectMirror(global, global), ScriptContext.ENGINE_SCOPE);
        global.setDomObject(webWindow);
        initGlobal(scriptContext, getBrowser(webClient_.getBrowserVersion()));
        webWindow.setScriptObject(global.<Window2>getWindow());
    }

    @Override
    public void setJavaScriptTimeout(long timeout) {
    }

    @Override
    public long getJavaScriptTimeout() {
        return 0;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public boolean isScriptRunning() {
        return false;
    }

}
