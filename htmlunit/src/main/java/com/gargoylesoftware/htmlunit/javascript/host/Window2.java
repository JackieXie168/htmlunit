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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.NashornJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet2;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document2;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.svg.SvgPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.js.internal.dynalink.CallSiteDescriptor;
import com.gargoylesoftware.js.internal.dynalink.linker.GuardedInvocation;
import com.gargoylesoftware.js.internal.dynalink.linker.LinkRequest;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Attribute;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.ECMAErrors;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.Undefined;

public class Window2 extends EventTarget2 {

    private static final Log LOG = LogFactory.getLog(Window2.class);

    private Screen2 screen_;
    private Document2 document_;
    private History2 history_;
    private Location2 location_;
    private HTMLCollection2 frames_; // has to be a member to have equality (==) working
    private Event2 currentEvent_;
    private Object controllers_ = new SimpleScriptObject();
    private Object opener_;
    private Object top_;

    /**
     * Cache computed styles when possible, because their calculation is very expensive.
     * We use a weak hash map because we don't want this cache to be the only reason
     * nodes are kept around in the JVM, if all other references to them are gone.
     */
    private transient WeakHashMap<Element2, Map<String, ComputedCSSStyleDeclaration2>> computedStyles_ = new WeakHashMap<>();

    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR, value = @WebBrowser(CHROME))
    public static final int TEMPORARY = 0;

    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR, value = @WebBrowser(CHROME))
    public static final int PERSISTENT = 1;

    /**
     * Initialize the object.
     * @param enclosedPage the page containing the JavaScript
     */
    public void initialize(final Page enclosedPage) {
        if (enclosedPage instanceof XmlPage || enclosedPage instanceof SvgPage) {
//            document_ = new XMLDocument2();
        }
        else {
            document_ = HTMLDocument2.constructor(true, null);
        }
        document_.setWindow(this);

        final Global global = NashornJavaScriptEngine.getGlobal(enclosedPage.getEnclosingWindow().getScriptContext());

        if (enclosedPage != null && enclosedPage.isHtmlPage()) {
            final HtmlPage htmlPage = (HtmlPage) enclosedPage;

            // Windows don't have corresponding DomNodes so set the domNode
            // variable to be the page. If this isn't set then SimpleScriptable.get()
            // won't work properly
            setDomNode(htmlPage);
//            clearEventListenersContainer();

            document_.setDomNode(htmlPage);

            location_ = Location2.constructor(true, global);
            location_.initialize(this);

            final WebWindow webWindow = getWebWindow();
            if (webWindow instanceof TopLevelWindow) {
                final WebWindow opener = ((TopLevelWindow) webWindow).getOpener();
                if (opener != null) {
                    opener_ = opener.getScriptObject2();
                }
            }
        }
    }

    public static Window2 constructor(final boolean newObj, final Object self) {
        final Window2 host = new Window2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
        return host;
    }

    @Function
    public static void alert(final Object self, final Object o) {
        final AlertHandler handler = getWindow(self).getWebWindow().getWebClient().getAlertHandler();
        if (handler == null) {
            LOG.warn("window.alert(\"" + o + "\") no alert handler installed");
        }
        else {
//            handler.handleAlert(document_.getPage(), stringMessage);
          handler.handleAlert(null, o.toString());
        }
    }

    /**
     * Returns the WebWindow associated with this Window.
     * @return the WebWindow
     */
    public WebWindow getWebWindow() {
        return ((HtmlPage) getDomNodeOrDie()).getEnclosingWindow();
    }

    @Getter
    public static int getInnerHeight(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        return webWindow.getInnerHeight();
    }

    @Getter
    public static int getInnerWidth(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        return webWindow.getInnerWidth();
    }

    @Getter
    public static int getOuterHeight(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        return webWindow.getOuterHeight();
    }

    @Getter
    public static int getOuterWidth(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        return webWindow.getOuterWidth();
    }

    @Getter
    public static Object getTop(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        final WebWindow top = webWindow.getTopWindow();
        return top.getScriptObject2();
    }

    @Getter(@WebBrowser(FF))
    public static Object getControllers(final Object self) {
        return getWindow(self).controllers_;
    }

    @Setter(@WebBrowser(FF))
    public static void setControllers(final Object self, final Object value) {
        getWindow(self).controllers_ = value;
    }

    private Object getHandlerForJavaScript(final String eventName) {
        return getEventListenersContainer().getEventHandlerProp(eventName);
    }

    private void setHandlerForJavaScript(final String eventName, final Object handler) {
        if (handler == null || handler instanceof ScriptFunction) {
            getEventListenersContainer().setEventHandlerProp(eventName, handler);
        }
        // Otherwise, fail silently.
    }

    @Getter
    public static Object getOnload(final Object self) {
        final Window2 window = getWindow(self);
        final Object onload = window.getHandlerForJavaScript("load");
        if (onload == null) {
            // NB: for IE, the onload of window is the one of the body element but not for Mozilla.
            final HtmlPage page = (HtmlPage) window.getWebWindow().getEnclosedPage();
            final HtmlElement body = page.getBody();
            if (body != null) {
                final HTMLBodyElement2 b = (HTMLBodyElement2) body.getScriptObject2();
                return b.getEventHandler("onload");
            }
            return null;
        }
        return onload;

    }

    @Setter
    public static void setOnload(final Object self, final Object newOnload) {
        final Window2 window = getWindow(self);
//        if (window.getBrowserVersion().hasFeature(EVENT_ONLOAD_UNDEFINED_THROWS_ERROR)
//                && Context.getUndefinedValue().equals(newOnload)) {
//                throw Context.reportRuntimeError("Invalid onload value: undefined.");
//            }
        window.getEventListenersContainer().setEventHandlerProp("load", newOnload);
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    @Function({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public static String btoa(final Object self, final String stringToEncode) {
        return new String(Base64.encodeBase64(stringToEncode.getBytes()));
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding..
     * @param encodedData the encoded string
     * @return the decoded value
     */
    @Function({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public static String atob(final Object self, final String encodedData) {
        return new String(Base64.decodeBase64(encodedData.getBytes()));
    }

    /**
     * Executes the specified script code as long as the language is {@code JavaScript} or {@code JScript}.
     * @param script the script code to execute
     * @param language the language of the specified code ({@code JavaScript} or {@code JScript})
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536420.aspx">MSDN documentation</a>
     */
    @Function(@WebBrowser(value = IE, maxVersion = 8))
    public void execScript(final String script, final Object language) {
        final String languageStr = ScriptRuntime.safeToString(language);
        if (language == Undefined.getUndefined()
            || "javascript".equalsIgnoreCase(languageStr) || "jscript".equalsIgnoreCase(languageStr)) {
            final Global global = Context.getGlobal();
            Context.getContext().eval(global, script, global, null);
        }
        else if ("vbscript".equalsIgnoreCase(languageStr)) {
            throw ECMAErrors.typeError("VBScript not supported in Window.execScript().");
        }
        else {
            throw ECMAErrors.typeError("Invalid class string");
        }
    }

    /**
     * An undocumented IE function.
     */
    @Function(@WebBrowser(IE))
    public void CollectGarbage() {
        // Empty.
    }

    /**
     * Returns the JavaScript property {@code document}.
     * @return the document
     */
    @Getter
    public static Document2 getDocument(final Object self) {
        return getWindow(self).document_;
    }

    private static Window2 getWindow(final Object self) {
        if (self instanceof Global) {
            return ((Global) self).getWindow();
        }
        return (Window2) self;
    }
    
    /**
     * Returns the value of the window's {@code name} property.
     * @return the value of the window's {@code name} property
     */
    @Getter
    public String getName() {
        return getWebWindow().getName();
    }

    /**
    * Sets the value of the window's {@code name} property.
    * @param name the value of the window's {@code name} property
    */
   @Setter
   public void setName(final Object name) {
       getWebWindow().setName(name.toString());
   }

    /**
     * Returns the {@code history} property.
     * @return the {@code history} property
     */
    @Getter
    public static History2 getHistory(final Object self) {
        final Window2 window = (Window2) self;
        if (window.history_ == null) {
            window.history_ = History2.constructor(true, self);
        }
        return window.history_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuardedInvocation noSuchProperty(final CallSiteDescriptor desc, final LinkRequest request) {
        final String name = desc.getNameToken(CallSiteDescriptor.NAME_OPERAND);
        final MethodHandle mh = virtualHandle("getArbitraryProperty", Object.class, String.class);
        return new GuardedInvocation(MethodHandles.insertArguments(mh, 1, name));
    }

    @SuppressWarnings("unused")
    private Object getArbitraryProperty(final String name) {
        final HtmlPage page = (HtmlPage) getDomNodeOrDie();
        Object object = getFrameWindowByName(page, name);
        if (object == null) {
            object = Undefined.getUndefined();
        }
        return object;
    }

    private static Object getFrameWindowByName(final HtmlPage page, final String name) {
        try {
            return page.getFrameByName(name).getScriptObject2();
        }
        catch (final ElementNotFoundException e) {
            return null;
        }
    }

    /**
     * Returns the number of frames contained by this window.
     * @return the number of frames contained by this window
     */
    @Getter
    public static int getLength(final Object self) {
        final Window2 window = (Window2) self;
        return (int) window.getFrames2().getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(int key) {
        final HTMLCollection2 frames = getFrames2();
        if (key >= (int) frames.getLength()) {
            return Undefined.getUndefined();
        }
        return frames.item(Integer.valueOf(key));
    }

    /**
     * Stub only at the moment.
     * @param search the text string for which to search
     * @param caseSensitive if true, specifies a case-sensitive search
     * @param backwards if true, specifies a backward search
     * @param wrapAround if true, specifies a wrap around search
     * @param wholeWord if true, specifies a whole word search
     * @param searchInFrames if true, specifies a search in frames
     * @param showDialog if true, specifies a show Dialog.
     * @return false
     */
    @Function({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public boolean find(final String search, final boolean caseSensitive,
            final boolean backwards, final boolean wrapAround,
            final boolean wholeWord, final boolean searchInFrames, final boolean showDialog) {
        return false;
    }

    /**
     * Returns the {@code frames} property.
     * @return the {@code frames} property
     */
    @Getter
    public Window2 getFrames() {
        return this;
    }

    /**
     * Returns the {@code self} property.
     * @return the {@code self} property
     */
    @Getter
    public Window2 getSelf() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchEvent(final Event2 event) {
        event.setTarget(this);
        final ScriptResult result = fireEvent(event);
        return !event.isAborted(result);
    }

    /**
     * Returns the live collection of frames contained by this window.
     * @return the live collection of frames contained by this window
     */
    private HTMLCollection2 getFrames2() {
        if (frames_ == null) {
            final HtmlPage page = (HtmlPage) getDomNodeOrDie();
            frames_ = new HTMLCollectionFrames2(page);
        }
        return frames_;
    }

    /**
     * Returns computed style of the element. Computed style represents the final computed values
     * of all CSS properties for the element. This method's return value is of the same type as
     * that of <tt>element.style</tt>, but the value returned by this method is read-only.
     *
     * @param element the element
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null})
     * @return the computed style
     */
    @Function
    public static ComputedCSSStyleDeclaration2 getComputedStyle(final Object self,
            final Element2 element, final String pseudoElement) {
        final Window2 window2 = getWindow(self);
        synchronized (window2.computedStyles_) {
            final Map<String, ComputedCSSStyleDeclaration2> elementMap = window2.computedStyles_.get(element);
            if (elementMap != null) {
                final ComputedCSSStyleDeclaration2 style = elementMap.get(pseudoElement);
                if (style != null) {
                    return style;
                }
            }
        }

        final CSSStyleDeclaration2 original = element.getStyle();
        final Global global = NashornJavaScriptEngine.getGlobal(window2.getWebWindow().getScriptContext());
        final ComputedCSSStyleDeclaration2 style = new ComputedCSSStyleDeclaration2(global, original);

        final StyleSheetList2 sheets = ((HTMLDocument2) element.getOwnerDocument()).getStyleSheets();
        final boolean trace = LOG.isTraceEnabled();
        for (int i = 0; i < (int) sheets.getLength(); i++) {
            final CSSStyleSheet2 sheet = (CSSStyleSheet2) sheets.item(i);
            if (sheet.isActive() && sheet.isEnabled()) {
                if (trace) {
                    LOG.trace("modifyIfNecessary: " + sheet + ", " + style + ", " + element);
                }
                sheet.modifyIfNecessary(style, element, pseudoElement);
            }
        }

        synchronized (window2.computedStyles_) {
            Map<String, ComputedCSSStyleDeclaration2> elementMap = window2.computedStyles_.get(element);
            if (elementMap == null) {
                elementMap = new WeakHashMap<>();
                window2.computedStyles_.put(element, elementMap);
            }
            elementMap.put(pseudoElement, style);
        }

        return style;
    }

    /**
     * Returns the {@code screen} property.
     * @return the screen property
     */
    @Getter
    public Screen2 getScreen() {
        return screen_;
    }

    /**
     * Returns the {@code location} property.
     * @return the {@code location} property
     */
    @Getter
    public static Location2 getLocation(final Object self) {
        return getWindow(self).location_;
    }

    /**
     * Sets the location property. This will cause a reload of the window.
     * @param self this object
     * @param newLocation the URL of the new content
     * @throws IOException when location loading fails
     */
    @Setter
    public static void setLocation(final Object self, final String newLocation) throws IOException {
        getWindow(self).location_.setHref(newLocation);
    }

    /**
     * Triggers the {@code onerror} handler, if one has been set.
     * @param e the error that needs to be reported
     */
    public void triggerOnError(final ScriptException e) {
        final Object o = getOnerror();
        if (o instanceof ScriptFunction) {
            final ScriptFunction f = (ScriptFunction) o;
            final Global global = NashornJavaScriptEngine.getGlobal(getWebWindow().getScriptContext());

            final String msg = e.getMessage();
            final String url = e.getPage().getUrl().toExternalForm();
            final int line = e.getFailingLineNumber();
            final int column = e.getFailingColumnNumber();

            ScriptRuntime.apply(f, global,
                    msg, url, Integer.valueOf(line), Integer.valueOf(column), e);
        }
    }

    /**
     * Returns the value of the window's {@code onerror} property.
     * @return the value of the window's {@code onerror} property
     */
    @Getter
    public Object getOnerror() {
        return getHandlerForJavaScript(Event2.TYPE_ERROR);
    }

    /**
     * Sets the value of the window's {@code onerror} property.
     * @param onerror the value of the window's {@code onerror} property
     */
    @Setter
    public void setOnerror(final Object onerror) {
        setHandlerForJavaScript(Event2.TYPE_ERROR, onerror);
    }

    /**
     * Sets the value of the {@code onload} event handler.
     * @param onload the new handler
     */
    @Setter
    public void setOnload(final Object onload) {
        getEventListenersContainer().setEventHandlerProp("load", onload);
    }

    /**
     * Returns the {@code onclick} property (not necessary a function if something else has been set).
     * @return the {@code onclick} property
     */
    @Getter
    public Object getOnclick() {
        return getHandlerForJavaScript("click");
    }

    /**
     * Sets the value of the {@code onclick} event handler.
     * @param onclick the new handler
     */
    @Setter
    public void setOnclick(final Object onclick) {
        setHandlerForJavaScript("click", onclick);
    }

    /**
     * Returns the {@code ondblclick} property (not necessary a function if something else has been set).
     * @return the {@code ondblclick} property
     */
    @Getter
    public Object getOndblclick() {
        return getHandlerForJavaScript("dblclick");
    }

    /**
     * Sets the value of the {@code ondblclick} event handler.
     * @param ondblclick the new handler
     */
    @Setter
    public void setOndblclick(final Object ondblclick) {
        setHandlerForJavaScript("dblclick", ondblclick);
    }

    /**
     * Returns the {@code onhashchange} property (not necessary a function if something else has been set).
     * @return the {@code onhashchange} property
     */
    @Getter
    public Object getOnhashchange() {
        return getHandlerForJavaScript(Event2.TYPE_HASH_CHANGE);
    }

    /**
     * Sets the value of the {@code onhashchange} event handler.
     * @param onhashchange the new handler
     */
    @Setter
    public void setOnhashchange(final Object onhashchange) {
        setHandlerForJavaScript(Event2.TYPE_HASH_CHANGE, onhashchange);
    }

    /**
     * Returns the value of the window's {@code onbeforeunload} property.
     * @return the value of the window's {@code onbeforeunload} property
     */
    @Getter
    public Object getOnbeforeunload() {
        return getHandlerForJavaScript(Event2.TYPE_BEFORE_UNLOAD);
    }

    /**
     * Sets the value of the window's {@code onbeforeunload} property.
     * @param onbeforeunload the value of the window's {@code onbeforeunload} property
     */
    @Setter
    public void setOnbeforeunload(final Object onbeforeunload) {
        setHandlerForJavaScript(Event2.TYPE_BEFORE_UNLOAD, onbeforeunload);
    }

    /**
     * Getter for the {@code onchange} event handler.
     * @return the handler
     */
    @Getter
    public Object getOnchange() {
        return getHandlerForJavaScript(Event2.TYPE_CHANGE);
    }

    /**
     * Setter for the {@code onchange} event handler.
     * @param onchange the handler
     */
    @Setter
    public void setOnchange(final Object onchange) {
        setHandlerForJavaScript(Event2.TYPE_CHANGE, onchange);
    }

    /**
     * Getter for the {@code onsubmit} event handler.
     * @return the handler
     */
    @Getter
    public Object getOnsubmit() {
        return getHandlerForJavaScript(Event2.TYPE_SUBMIT);
    }

    /**
     * Setter for the {@code onsubmit} event handler.
     * @param onsubmit the handler
     */
    @Setter
    public void setOnsubmit(final Object onsubmit) {
        setHandlerForJavaScript(Event2.TYPE_SUBMIT, onsubmit);
    }

    /**
     * Returns the current event.
     * @return the current event, or {@code null} if no event is currently available
     */
    @Getter({@WebBrowser(IE), @WebBrowser(CHROME)})
    public Object getEvent() {
        return currentEvent_;
    }

    /**
     * Returns the current event (used internally regardless of the emulation mode).
     * @return the current event, or {@code null} if no event is currently available
     */
    public Event2 getCurrentEvent() {
        return currentEvent_;
    }

    /**
     * Sets the current event.
     * @param event the current event
     */
    public void setCurrentEvent(final Event2 event) {
        currentEvent_ = event;
    }

    /**
     * Opens a new window.
     *
     * @param url when a new document is opened, <i>url</i> is a String that specifies a MIME type for the document.
     *        When a new window is opened, <i>url</i> is a String that specifies the URL to render in the new window
     * @param name the name
     * @param features the features
     * @param replace whether to replace in the history list or no
     * @return the newly opened window, or {@code null} if popup windows have been disabled
     * @see com.gargoylesoftware.htmlunit.WebClientOptions#isPopupBlockerEnabled()
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536651.aspx">MSDN documentation</a>
     */
    @Function
    public ScriptObject open(final Object url, final Object name, final Object features,
            final Object replace) {
        String urlString = null;
        if (url != Undefined.getUndefined()) {
            urlString = url.toString();
        }
        String windowName = "";
        if (name != Undefined.getUndefined()) {
            windowName = name.toString();
        }
        String featuresString = null;
        if (features != Undefined.getUndefined()) {
            featuresString = features.toString();
        }
        final WebClient webClient = getWebWindow().getWebClient();

        if (webClient.getOptions().isPopupBlockerEnabled()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Ignoring window.open() invocation because popups are blocked.");
            }
            return null;
        }

        boolean replaceCurrentEntryInBrowsingHistory = false;
        if (replace != Undefined.getUndefined()) {
            replaceCurrentEntryInBrowsingHistory = (boolean) replace;
        }
        if (featuresString != null || replaceCurrentEntryInBrowsingHistory) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(
                        "window.open: features and replaceCurrentEntryInBrowsingHistory "
                        + "not implemented: url=[" + urlString
                        + "] windowName=[" + windowName
                        + "] features=[" + featuresString
                        + "] replaceCurrentEntry=[" + replaceCurrentEntryInBrowsingHistory
                        + "]");
            }
        }

        // if specified name is the name of an existing window, then hold it
        if (StringUtils.isEmpty(urlString) && !"".equals(windowName)) {
            try {
                final WebWindow webWindow = webClient.getWebWindowByName(windowName);
                return webWindow.getScriptObject2();
            }
            catch (final WebWindowNotFoundException e) {
                // nothing
            }
        }
        final URL newUrl = makeUrlForOpenWindow(urlString);
        final WebWindow newWebWindow = webClient.openWindow(newUrl, windowName, getWebWindow());
        return newWebWindow.getScriptObject2();
    }

    private URL makeUrlForOpenWindow(final String urlString) {
        if (urlString.isEmpty()) {
            return WebClient.URL_ABOUT_BLANK;
        }

        try {
            final Page page = getWebWindow().getEnclosedPage();
            if (page != null && page.isHtmlPage()) {
                return ((HtmlPage) page).getFullyQualifiedUrl(urlString);
            }
            return new URL(urlString);
        }
        catch (final MalformedURLException e) {
            LOG.error("Unable to create URL for openWindow: relativeUrl=[" + urlString + "]", e);
            return null;
        }
    }

    /**
     * Sets the {@code opener} property.
     * @param newValue the new value
     */
    @Setter
    public void setOpener(final Object newValue) {
        if (getBrowserVersion().hasFeature(JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT)
            && newValue != null && newValue != Undefined.getUndefined() && !(newValue instanceof Window2)) {
            throw new RuntimeException("Can't set opener to something other than a window!");
        }
        opener_ = newValue;
    }

    /**
     * Returns the value of the {@code opener} property.
     * @return the value of the {@code opener}, or {@code null} for a top level window
     */
    @Getter
    public Object getOpener() {
        return opener_;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Window2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static MethodHandle virtualHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findVirtual(Window2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("Window", 
                    staticHandle("constructor", Window2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
            ScriptUtils.initialize(this);
        }
 
        public int G$TEMPORARY() {
            return TEMPORARY;
        }

        public int G$PERSISTENT() {
            return PERSISTENT;
        }
    }

    public static final class Prototype extends PrototypeObject {
        public ScriptFunction alert;
        public ScriptFunction atob;
        public ScriptFunction btoa;
        public ScriptFunction CollectGarbage;
        public ScriptFunction find;
        public ScriptFunction getComputedStyle;
        public ScriptFunction open;

        public ScriptFunction G$alert() {
            return alert;
        }

        public void S$alert(final ScriptFunction function) {
            this.alert = function;
        }

        public ScriptFunction G$atob() {
            return atob;
        }

        public void S$atob(final ScriptFunction function) {
            this.atob = function;
        }

        public ScriptFunction G$btoa() {
            return btoa;
        }

        public void S$btoa(final ScriptFunction function) {
            this.btoa = function;
        }

        public ScriptFunction G$CollectGarbage() {
            return CollectGarbage;
        }

        public void S$CollectGarbage(final ScriptFunction function) {
            this.CollectGarbage = function;
        }

        public ScriptFunction G$find() {
            return find;
        }

        public void S$find(final ScriptFunction function) {
            this.find = function;
        }

        public ScriptFunction G$getComputedStyle() {
            return getComputedStyle;
        }

        public void S$getComputedStyle(final ScriptFunction function) {
            this.getComputedStyle = function;
        }

        public ScriptFunction G$open() {
            return open;
        }

        public void S$open(final ScriptFunction function) {
            this.open = function;
        }

        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "Window";
        }
    }

    public static final class ObjectConstructor extends ScriptObject {
        public ScriptFunction alert;
        public ScriptFunction atob;
        public ScriptFunction btoa;
        public ScriptFunction execScript;
        public ScriptFunction CollectGarbage;

        public ScriptFunction G$alert() {
            return this.alert;
        }

        public void S$alert(final ScriptFunction function) {
            this.alert = function;
        }

        public ScriptFunction G$atob() {
            return atob;
        }

        public void S$atob(final ScriptFunction function) {
            this.atob = function;
        }

        public ScriptFunction G$btoa() {
            return btoa;
        }

        public void S$btoa(final ScriptFunction function) {
            this.btoa = function;
        }

        public ScriptFunction G$execScript() {
            return execScript;
        }

        public void S$execScript(final ScriptFunction function) {
            this.execScript = function;
        }

        public ScriptFunction G$CollectGarbage() {
            return CollectGarbage;
        }

        public void S$CollectGarbage(final ScriptFunction function) {
            this.CollectGarbage = function;
        }

        public ObjectConstructor() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "Window";
        }
    }
}

class HTMLCollectionFrames2 extends HTMLCollection2 {
    private static final Log LOG = LogFactory.getLog(HTMLCollectionFrames2.class);

    HTMLCollectionFrames2(final HtmlPage page) {
        super(page, false);
    }

    @Override
    protected boolean isMatching(final DomNode node) {
        return node instanceof BaseFrameElement;
    }

    @Override
    protected ScriptObject getScriptObjectForElement(final Object obj) {
        final WebWindow window;
        if (obj instanceof BaseFrameElement) {
            window = ((BaseFrameElement) obj).getEnclosedWindow();
        }
        else {
            window = ((FrameWindow) obj).getFrameElement().getEnclosedWindow();
        }

        return window.getScriptObject2();
    }

//    @Override
//    protected Object getWithPreemption(final String name) {
//        final List<Object> elements = getElements();
//
//        for (final Object next : elements) {
//            final BaseFrameElement frameElt = (BaseFrameElement) next;
//            final WebWindow window = frameElt.getEnclosedWindow();
//            if (name.equals(window.getName())) {
//                if (LOG.isDebugEnabled()) {
//                    LOG.debug("Property \"" + name + "\" evaluated (by name) to " + window);
//                }
//                return getScriptableForElement(window);
//            }
//            if (getBrowserVersion().hasFeature(JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID)
//                    && frameElt.getAttribute("id").equals(name)) {
//                if (LOG.isDebugEnabled()) {
//                    LOG.debug("Property \"" + name + "\" evaluated (by id) to " + window);
//                }
//                return getScriptableForElement(window);
//            }
//        }
//
//        return NOT_FOUND;
//    }

//    @Override
//    protected void addElementIds(final List<String> idList, final List<Object> elements) {
//        for (final Object next : elements) {
//            final BaseFrameElement frameElt = (BaseFrameElement) next;
//            final WebWindow window = frameElt.getEnclosedWindow();
//            final String windowName = window.getName();
//            if (windowName != null) {
//                idList.add(windowName);
//            }
//        }
//    }
}
