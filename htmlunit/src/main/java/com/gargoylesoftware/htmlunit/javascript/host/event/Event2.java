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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_FOCUS_FOCUS_IN_BLUR_OUT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONLOAD_CANCELABLE_FALSE;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Attribute;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.runtime.AccessorProperty;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;

// When this is renamed to Event, change the name in initEvent()
@ScriptClass
public class Event2 extends SimpleScriptObject {

    /**
     * Key to place the event's target in the Context's scope during event processing
     * to compute node coordinates compatible with those of the event.
     */
    protected static final String KEY_CURRENT_EVENT = "Event#current";

    /** The submit event type, triggered by {@code onsubmit} event handlers. */
    public static final String TYPE_SUBMIT = "submit";

    /** The change event type, triggered by {@code onchange} event handlers. */
    public static final String TYPE_CHANGE = "change";

    /** The load event type, triggered by {@code onload} event handlers. */
    public static final String TYPE_LOAD = "load";

    /** The unload event type, triggered by {@code onunload} event handlers. */
    public static final String TYPE_UNLOAD = "unload";

    /** The popstate event type, triggered by {@code onpopstate} event handlers. */
    public static final String TYPE_POPSTATE = "popstate";

    /** The focus event type, triggered by {@code onfocus} event handlers. */
    public static final String TYPE_FOCUS = "focus";

    /** The focus in event type, triggered by {@code onfocusin} event handlers. */
    public static final String TYPE_FOCUS_IN = "focusin";

    /** The focus out event type, triggered by {@code onfocusout} event handlers. */
    public static final String TYPE_FOCUS_OUT = "focusout";

    /** The blur event type, triggered by {@code onblur} event handlers. */
    public static final String TYPE_BLUR = "blur";

    /** The key down event type, triggered by {@code onkeydown} event handlers. */
    public static final String TYPE_KEY_DOWN = "keydown";

    /** The key down event type, triggered by {@code onkeypress} event handlers. */
    public static final String TYPE_KEY_PRESS = "keypress";

    /** The input event type, triggered by {@code oninput} event handlers. */
    public static final String TYPE_INPUT = "input";

    /** The key down event type, triggered by {@code onkeyup} event handlers. */
    public static final String TYPE_KEY_UP = "keyup";

    /** The submit event type, triggered by {@code onreset} event handlers. */
    public static final String TYPE_RESET = "reset";

    /** The beforeunload event type, triggered by {@code onbeforeunload} event handlers. */
    public static final String TYPE_BEFORE_UNLOAD = "beforeunload";

    /** Triggered after the DOM has loaded but before images etc. */
    public static final String TYPE_DOM_DOCUMENT_LOADED = "DOMContentLoaded";

    /** The event type triggered by {@code onpropertychange} event handlers. */
    public static final String TYPE_PROPERTY_CHANGE = "propertychange";

    /** The event type triggered by {@code onhashchange} event handlers. */
    public static final String TYPE_HASH_CHANGE = "hashchange";

    /** The event type triggered by {@code onreadystatechange} event handlers. */
    public static final String TYPE_READY_STATE_CHANGE = "readystatechange";

    /** The event type triggered by {@code onerror} event handlers. */
    public static final String TYPE_ERROR = "error";

    /** The message event type, triggered by postMessage. */
    public static final String TYPE_MESSAGE = "message";

    /** The close event type, triggered by {@code onclose} event handlers. */
    public static final String TYPE_CLOSE = "close";

    /** The open event type, triggered by {@code onopen} event handlers. */
    public static final String TYPE_OPEN = "open";

    /** No event phase. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = {@WebBrowser(CHROME), @WebBrowser(FF)},
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final short NONE = 0;

    /** The first event phase: the capturing phase. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final short CAPTURING_PHASE = 1;

    /** The second event phase: at the event target. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final short AT_TARGET = 2;

    /** The third (and final) event phase: the bubbling phase. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final short BUBBLING_PHASE = 3;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int BLUR = 0x2000;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int CHANGE = 0x8000;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int CLICK = 0x40;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int DBLCLICK = 0x80;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int DRAGDROP = 0x800;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int FOCUS = 0x1000;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int KEYDOWN = 0x100;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int KEYPRESS = 0x400;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int KEYUP = 0x200;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int MOUSEDOWN = 0x1;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int MOUSEDRAG = 0x20;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int MOUSEMOVE = 0x10;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int MOUSEOUT = 0x8;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int MOUSEOVER = 0x4;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int MOUSEUP = 0x2;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(CHROME),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int SELECT = 0x4000;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(FF),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int ALT_MASK = 0x1;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(FF),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int CONTROL_MASK = 0x2;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(FF),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int SHIFT_MASK = 0x4;

    /** Constant. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(
            value = @WebBrowser(FF),
            attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int META_MASK = 0x8;

    private Object srcElement_;        // IE-only writable equivalent of target.
    private Object target_;            // W3C standard read-only equivalent of srcElement.
    private ScriptObject currentTarget_; // Changes during event capturing and bubbling.
    private String type_ = "";         // The event type.
    private Object keyCode_;           // Key code for a keypress
    private boolean shiftKey_;         // Exposed here in IE, only in mouse events in FF.
    private boolean ctrlKey_;          // Exposed here in IE, only in mouse events in FF.
    private boolean altKey_;           // Exposed here in IE, only in mouse events in FF.
    private String propertyName_;
    private boolean stopPropagation_;
    private Object returnValue_;
    private boolean preventDefault_;

    /**
     * The current event phase. This is a W3C standard attribute. One of {@link #NONE},
     * {@link #CAPTURING_PHASE}, {@link #AT_TARGET} or {@link #BUBBLING_PHASE}.
     */
    private short eventPhase_;

    /**
     * Whether or not the event bubbles. The value of this attribute depends on the event type. To
     * determine if a certain event type bubbles, see http://www.w3.org/TR/DOM-Level-2-Events/events.html
     * Most event types do bubble, so this is true by default; event types which do not bubble should
     * overwrite this value in their constructors.
     */
    private boolean bubbles_ = true;

    /**
     * Whether or not the event can be canceled. The value of this attribute depends on the event type. To
     * determine if a certain event type can be canceled, see http://www.w3.org/TR/DOM-Level-2-Events/events.html
     * The more common event types are cancelable, so this is true by default; event types which cannot be
     * canceled should overwrite this value in their constructors.
     */
    private boolean cancelable_ = true;

    /**
     * The time at which the event was created.
     */
    private final long timeStamp_ = System.currentTimeMillis();

    public static Event2 constructor(final boolean newObj, final Object self) {
        final Event2 host = new Event2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    public Event2() {
    }

    /**
     * Creates a new event instance.
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     */
    public Event2(final DomNode domNode, final String type) {
        this((SimpleScriptObject) domNode.getScriptObject2(), type);
        setDomNode(domNode, false);
    }

    /**
     * Creates a new event instance.
     * @param scriptable the SimpleScriptable that triggered the event
     * @param type the event type
     */
    public Event2(final SimpleScriptObject scriptable, final String type) {
        srcElement_ = scriptable;
        target_ = scriptable;
        currentTarget_ = scriptable;
        type_ = type;

        if (TYPE_CHANGE.equals(type)) {
            cancelable_ = false;
        }
        else if (TYPE_LOAD.equals(type)) {
            bubbles_ = false;
            if (scriptable.getBrowserVersion().hasFeature(EVENT_ONLOAD_CANCELABLE_FALSE)) {
                cancelable_ = false;
            }
        }
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
//    @JsxConstructor({@WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE)})
    public void jsConstructor(final String type, final ScriptObject details) {
        boolean bubbles = false;
        boolean cancelable = false;

        if (details != null /*&& !ScriptRuntime.UNDEFINED.equals(details)*/) {
            final Boolean detailBubbles = (Boolean) details.get("bubbles");
            if (detailBubbles != null) {
                bubbles = detailBubbles.booleanValue();
            }

            final Boolean detailCancelable = (Boolean) details.get("cancelable");
            if (detailCancelable != null) {
                cancelable = detailCancelable.booleanValue();
            }
        }
        initEvent(type, bubbles, cancelable);
    }

    @Getter
    public static String getType(final Object self) {
        return ((Event2) self).type_;
    }

    @Setter
    public static void settype(final Object self, final String value) {
        ((Event2) self).type_ = value;
    }

    /**
     * Called whenever an event is created using <code>Document.createEvent(..)</code>.
     * This method is called after the parent scope was set so you are able to access the browser version.
     */
    public void eventCreated() {
        setBubbles(false);
        setCancelable(false);
    }

    /**
     * @return whether or not this event bubbles
     */
    @Getter
    public Boolean getBubbles() {
        return bubbles_;
    }

    /**
     * @param bubbles the bubbles to set
     */
    protected void setBubbles(final boolean bubbles) {
        bubbles_ = bubbles;
    }

    /**
     * @return whether or not this event can be canceled
     */
    @Getter
    public Boolean getCancelable() {
        return cancelable_;
    }

    /**
     * @param cancelable the cancelable to set
     */
    protected void setCancelable(final boolean cancelable) {
        cancelable_ = cancelable;
    }

    /**
     * Returns the return value associated with the event.
     * @return the return value associated with the event
     */
    public Object getReturnValue() {
        return returnValue_;
    }

    /**
     * Sets the return value associated with the event.
     * @param returnValue the return value associated with the event
     */
    public void setReturnValue(final Object returnValue) {
        returnValue_ = returnValue;
    }

    /**
     * Initializes this event.
     * @param type the event type
     * @param bubbles whether or not the event should bubble
     * @param cancelable whether or not the event the event should be cancelable
     */
    @Function
    public void initEvent(final String type, final boolean bubbles, final boolean cancelable) {
        type_ = type;
        bubbles_ = bubbles;
        cancelable_ = cancelable;
        if (TYPE_BEFORE_UNLOAD.equals(type) && getBrowserVersion().hasFeature(EVENT_FOCUS_FOCUS_IN_BLUR_OUT)) {
            try {
                final MethodHandle getter = virtualHandle("getReturnValue", Object.class);
                final MethodHandle setter = virtualHandle("setReturnValue", void.class, Object.class);
                final AccessorProperty property = AccessorProperty.create("returnValue", Attribute.DEFAULT_ATTRIBUTES, 
                        getter, setter);

                addBoundProperties((Object) this, new AccessorProperty[] {property});
                if ("Event2".equals(getClass().getSimpleName())) {
                    setReturnValue(Boolean.TRUE);
                }
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns the event target to which the event was originally dispatched.
     * @return the event target to which the event was originally dispatched
     */
    @Getter
    public Object getTarget() {
        return target_;
    }

    /**
     * Sets the event target.
     * @param target the event target
     */
    public void setTarget(final Object target) {
        target_ = target;
    }

    /**
     * Returns the event type.
     * @return the event type
     */
    @Getter
    public String getType() {
        return type_;
    }

    /**
     * Sets the event type.
     * @param type the event type
     */
    @Setter
    public void setType(final String type) {
        type_ = type;
    }

    /**
     * Returns the event target whose event listeners are currently being processed. This
     * is useful during event capturing and event bubbling.
     * @return the current event target
     */
    @Getter
    public ScriptObject getCurrentTarget() {
        return currentTarget_;
    }

    /**
     * Sets the current target.
     * @param target the new value
     */
    public void setCurrentTarget(final ScriptObject target) {
        currentTarget_ = target;
    }

    /**
     * Stops the event from propagating.
     */
    @Function
    public void stopPropagation() {
        stopPropagation_ = true;
    }

    /**
     * Indicates if event propagation is stopped.
     * @return the status
     */
    public boolean isPropagationStopped() {
        return stopPropagation_;
    }

    /**
     * Returns {@code true} if this event has been aborted via <tt>preventDefault()</tt> in
     * standards-compliant browsers, or via the event's <tt>returnValue</tt> property in IE, or
     * by the event handler returning {@code false}.
     *
     * @param result the event handler result (if {@code false}, the event is considered aborted)
     * @return {@code true} if this event has been aborted
     */
    public boolean isAborted(final ScriptResult result) {
        return ScriptResult.isFalse(result) || preventDefault_;
    }

    /**
     * Returns the current event phase for the event.
     * @return the current event phase for the event
     */
    @Getter
    public int getEventPhase() {
        return eventPhase_;
    }

    /**
     * Sets the current event phase. Must be one of {@link #CAPTURING_PHASE}, {@link #AT_TARGET} or
     * {@link #BUBBLING_PHASE}.
     *
     * @param phase the phase the event is in
     */
    public void setEventPhase(final short phase) {
        if (phase != CAPTURING_PHASE && phase != AT_TARGET && phase != BUBBLING_PHASE) {
            throw new IllegalArgumentException("Illegal phase specified: " + phase);
        }
        eventPhase_ = phase;
    }

    /**
     * Sets the event type.
     * @param eventType the event type
     */
    public void setEventType(final String eventType) {
        type_ = eventType;
    }

    /**
     * Returns whether {@code SHIFT} has been pressed during this event or not.
     * @return whether {@code SHIFT} has been pressed during this event or not
     */
    public boolean getShiftKey() {
        return shiftKey_;
    }

    /**
     * Sets whether {@code SHIFT} key is pressed on not.
     * @param shiftKey whether {@code SHIFT} has been pressed during this event or not
     */
    protected void setShiftKey(final boolean shiftKey) {
        shiftKey_ = shiftKey;
    }

    /**
     * Returns whether {@code CTRL} has been pressed during this event or not.
     * @return whether {@code CTRL} has been pressed during this event or not
     */
    public boolean getCtrlKey() {
        return ctrlKey_;
    }

    /**
     * Sets whether {@code CTRL} key is pressed on not.
     * @param ctrlKey whether {@code CTRL} has been pressed during this event or not
     */
    protected void setCtrlKey(final boolean ctrlKey) {
        ctrlKey_ = ctrlKey;
    }

    /**
     * Returns whether {@code ALT} has been pressed during this event or not.
     * @return whether {@code ALT} has been pressed during this event or not
     */
    public boolean getAltKey() {
        return altKey_;
    }

    /**
     * Sets whether {@code ALT} key is pressed on not.
     * @param altKey whether {@code ALT} has been pressed during this event or not
     */
    protected void setAltKey(final boolean altKey) {
        altKey_ = altKey;
    }

    /**
     * Sets the key code.
     * @param keyCode the virtual key code value of the key which was depressed, otherwise zero
     */
    protected void setKeyCode(final Object keyCode) {
        keyCode_ = keyCode;
    }

    /**
     * Returns the key code associated with the event.
     * @return the key code associated with the event
     */
    public Object getKeyCode() {
        if (keyCode_ == null) {
            return Integer.valueOf(0);
        }
        return keyCode_;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Event2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static MethodHandle virtualHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findVirtual(Event2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @ClassConstructor({@WebBrowser(CHROME), @WebBrowser(FF)})
    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("Event", 
                    staticHandle("constructor", Event2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }

        public int G$NONE() {
            return NONE;
        }

        public int G$CAPTURING_PHASE() {
            return CAPTURING_PHASE;
        }

        public int G$AT_TARGET() {
            return AT_TARGET;
        }

        public int G$BUBBLING_PHASE() {
            return BUBBLING_PHASE;
        }

        public int G$BLUR() {
            return BLUR;
        }

        public int G$CHANGE() {
            return CHANGE;
        }

        public int G$CLICK() {
            return CLICK;
        }

        public int G$DBLCLICK() {
            return DBLCLICK;
        }

        public int G$DRAGDROP() {
            return DRAGDROP;
        }

        public int G$FOCUS() {
            return FOCUS;
        }

        public int G$KEYDOWN() {
            return KEYDOWN;
        }

        public int G$KEYPRESS() {
            return KEYPRESS;
        }

        public int G$KEYUP() {
            return KEYUP;
        }

        public int G$MOUSEDOWN() {
            return MOUSEDOWN;
        }

        public int G$MOUSEDRAG() {
            return MOUSEDRAG;
        }

        public int G$MOUSEMOVE() {
            return MOUSEMOVE;
        }

        public int G$MOUSEOUT() {
            return MOUSEOUT;
        }

        public int G$MOUSEOVER() {
            return MOUSEOVER;
        }

        public int G$MOUSEUP() {
            return MOUSEUP;
        }

        public int G$SELECT() {
            return SELECT;
        }

        public int G$ALT_MASK() {
            return ALT_MASK;
        }

        public int G$CONTROL_MASK() {
            return CONTROL_MASK;
        }

        public int G$SHIFT_MASK() {
            return SHIFT_MASK;
        }

        public int G$META_MASK() {
            return META_MASK;
        }
    }

    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("Event");
        }
    }

    @ClassConstructor(@WebBrowser(IE))
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        public int G$NONE() {
            return NONE;
        }

        public int G$CAPTURING_PHASE() {
            return CAPTURING_PHASE;
        }

        public int G$AT_TARGET() {
            return AT_TARGET;
        }

        public int G$BUBBLING_PHASE() {
            return BUBBLING_PHASE;
        }

        public int G$BLUR() {
            return BLUR;
        }

        public int G$CHANGE() {
            return CHANGE;
        }

        public int G$CLICK() {
            return CLICK;
        }

        public int G$DBLCLICK() {
            return DBLCLICK;
        }

        public int G$DRAGDROP() {
            return DRAGDROP;
        }

        public int G$FOCUS() {
            return FOCUS;
        }

        public int G$KEYDOWN() {
            return KEYDOWN;
        }

        public int G$KEYPRESS() {
            return KEYPRESS;
        }

        public int G$KEYUP() {
            return KEYUP;
        }

        public int G$MOUSEDOWN() {
            return MOUSEDOWN;
        }

        public int G$MOUSEDRAG() {
            return MOUSEDRAG;
        }

        public int G$MOUSEMOVE() {
            return MOUSEMOVE;
        }

        public int G$MOUSEOUT() {
            return MOUSEOUT;
        }

        public int G$MOUSEOVER() {
            return MOUSEOVER;
        }

        public int G$MOUSEUP() {
            return MOUSEUP;
        }

        public int G$SELECT() {
            return SELECT;
        }

        public int G$ALT_MASK() {
            return ALT_MASK;
        }

        public int G$CONTROL_MASK() {
            return CONTROL_MASK;
        }

        public int G$SHIFT_MASK() {
            return SHIFT_MASK;
        }

        public int G$META_MASK() {
            return META_MASK;
        }

        public ObjectConstructor() {
            super("Event");
        }
    }
}
