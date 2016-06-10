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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLEmbedElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement2;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.api.scripting.NashornScriptEngine;
import com.gargoylesoftware.js.nashorn.api.scripting.NashornScriptEngineFactory;
import com.gargoylesoftware.js.nashorn.api.scripting.ScriptObjectMirror;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Browser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.Property;
import com.gargoylesoftware.js.nashorn.internal.runtime.PropertyMap;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.Undefined;

/**
 * A wrapper for the <a href="http://openjdk.java.net/projects/nashorn/">Nashorn JavaScript engine</a>.
 *
 * @author Ahmed Ashour
 */
public class NashornJavaScriptEngine implements AbstractJavaScriptEngine {

    private static final Log LOG = LogFactory.getLog(NashornJavaScriptEngine.class);

    @SuppressWarnings("unchecked")
    public static final Class<? extends ScriptObject>[] CLASSES_ = new Class[] {
            CSSStyleDeclaration2.class,
            CharacterData2.class,
            ComputedCSSStyleDeclaration2.class,
            Document2.class,
            Element2.class,
            Event2.class,
            EventTarget2.class,
            History2.class,
            HTMLBodyElement2.class,
            HTMLCollection2.class,
            HTMLDivElement2.class,
            HTMLDocument2.class,
            HTMLElement2.class,
            HTMLEmbedElement2.class,
            HTMLFrameElement2.class,
            HTMLHtmlElement2.class,
            HTMLIFrameElement2.class,
            HTMLImageElement2.class,
            HTMLInputElement2.class,
            HTMLObjectElement2.class,
            Location2.class,
            MessageEvent2.class,
            Node2.class,
            Text2.class,
            Window2.class,
            XMLDocument2.class
        };

    private transient ThreadLocal<Boolean> javaScriptRunning_;
    private transient ThreadLocal<List<PostponedAction>> postponedActions_;
    private transient boolean holdPostponedActions_;

    private final WebClient webClient_;
    final NashornScriptEngine engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine();

    /**
     * Creates an instance for the specified {@link WebClient}.
     *
     * @param webClient the client that will own this engine
     */
    public NashornJavaScriptEngine(final WebClient webClient) {
        webClient_ = webClient;
        initTransientFields();
    }

    private void initTransientFields() {
        javaScriptRunning_ = new ThreadLocal<>();
        postponedActions_ = new ThreadLocal<>();
        holdPostponedActions_ = false;
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

            try {

                final Map<String, String> javaSuperMap = new HashMap<>();
                final Map<String, String> javaJavaScriptMap = new HashMap<>();
                for (final Class<?> klass : CLASSES_) {
                    for (final Class<?> inner : klass.getDeclaredClasses()) {
                        final ClassConstructor constructor = inner.getAnnotation(ClassConstructor.class);
                        if (isSupported(constructor, browser)) {
                            final ScriptObject instance = (ScriptObject) inner.newInstance();
                            String className = instance.getClassName();;
                            if (instance instanceof ScriptFunction) {
                                className = ((ScriptFunction) instance).getName();
                            }
                            global.put(className, instance, false);
                            javaSuperMap.put(klass.getName(), klass.getSuperclass().getName());
                            javaJavaScriptMap.put(klass.getName(), className);
                        }
                    }
                }
                for (final String javaClassName : javaSuperMap.keySet()) {
                    final String javaSuperClassName = javaSuperMap.get(javaClassName);
                    final String superJavaScriptName = javaJavaScriptMap.get(javaSuperClassName);
                    if (superJavaScriptName != null) {
                        setProto(global, javaJavaScriptMap.get(javaClassName), superJavaScriptName);
                    }
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

                final String[] windowToGlobalFunctions = {"alert", "atob", "btoa", "confirm", "execScript", "CollectGarbage", "setTimeout", "clearTimeout",
                        "ScriptEngine", "ScriptEngineBuildVersion", "ScriptEngineMajorVersion", "ScriptEngineMinorVersion", "showModalDialog", "showModelessDialog", "prompt"};
                for (final String key : windowToGlobalFunctions) {
                    final Object function = window.get(key);
                    if (function != Undefined.getUndefined()) {
                        global.put(key, function, true);
                    }
                }

                final String[] globalToWindowFunctions = {"RegExp", "NaN", "isNaN", "Infinity", "isFinite", "eval", "print",
                        "parseInt", "parseFloat", "encodeURI", "encodeURIComponent", "decodeURI", "decodeURIComponent",
                        "escape", "unescape"};
                for (final String key : globalToWindowFunctions) {
                    window.put(key, global.get(key), true);
                }

                final String[] windowProperties = {"controllers", "document", "length", "location", "opener", "parent", "self", "top"};
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
//            Context.setGlobal(oldGlobal);
        }
    }

    private static boolean isSupported(final ClassConstructor constructor, final Browser browser) {
        final int version = browser.getVersion();
        if (constructor != null) {
            for (final WebBrowser webBrowser : constructor.value()) {
                if (webBrowser.value() == browser.getFamily()
                        && webBrowser.minVersion() <= version
                        && webBrowser.maxVersion() >= version) {
                    return true;
                }
            }
        }
        return false;
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

    @Override
    public JavaScriptConfiguration getJavaScriptConfiguration() {
        return null;
    }

    @Override
    public void addPostponedAction(final PostponedAction action) {
        List<PostponedAction> actions = postponedActions_.get();
        if (actions == null) {
            actions = new ArrayList<>();
            postponedActions_.set(actions);
        }
        actions.add(action);
    }

    @Override
    public void processPostponedActions() {
        doProcessPostponedActions();
    }

    private void doProcessPostponedActions() {
        holdPostponedActions_ = false;

        try {
            getWebClient().loadDownloadedResponses();
        }
        catch (final RuntimeException e) {
            throw e;
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }

        final List<PostponedAction> actions = postponedActions_.get();
        if (actions != null) {
            postponedActions_.set(null);
            try {
                for (final PostponedAction action : actions) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Processing PostponedAction " + action);
                    }

                    // verify that the page that registered this PostponedAction is still alive
                    if (action.isStillAlive()) {
                        action.execute();
                    }
                }
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Object execute(final InteractivePage page, final String sourceCode, final String sourceName,
            final int startLine) {
        final Boolean javaScriptAlreadyRunning = javaScriptRunning_.get();
        javaScriptRunning_.set(Boolean.TRUE);
        Object response;
        synchronized (page) { // 2 scripts can't be executed in parallel for one page
            if (page != page.getEnclosingWindow().getEnclosedPage()) {
                return null; // page has been unloaded
            }
            try {
                response = engine.eval(sourceCode, page.getEnclosingWindow().getScriptContext());
            }
            catch(final Exception e) {
                handleJavaScriptException(new ScriptException(page, e, sourceCode), true);
                return null;
            }
        }

        // doProcessPostponedActions is synchronized
        // moved out of the sync block to avoid deadlocks
        if (!holdPostponedActions_) {
            doProcessPostponedActions();
        }
        return response;
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

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * Indicates that no postponed action should be executed.
     */
    public void holdPosponedActions() {
        holdPostponedActions_ = true;
    }

}
