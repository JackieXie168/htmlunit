/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import org.mozilla.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * JavaScript object representing a Mouse Event.
 * For general information on which properties and functions
 * should be supported, see
 * <a href="http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-MouseEvent">DOM Level 2 Events</a>.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class MouseEvent extends UIEvent {

    private static final long serialVersionUID = 1990705559211878370L;

    /** The click event type, triggered by "onclick" event handlers. */
    public static final String TYPE_CLICK = "click";

    /** The dblclick event type, triggered by "ondblclick" event handlers. */
    public static final String TYPE_DBL_CLICK = "dblclick";

    /** The mouse over event type, triggered by "onmouseover" event handlers. */
    public static final String TYPE_MOUSE_OVER = "mouseover";

    /** The mouse move event type, triggered by "onmousemove" event handlers. */
    public static final String TYPE_MOUSE_MOVE = "mousemove";

    /** The mouse out event type, triggered by "onmouseout" event handlers. */
    public static final String TYPE_MOUSE_OUT = "mouseout";

    /** The mouse down event type, triggered by "onmousedown" event handlers. */
    public static final String TYPE_MOUSE_DOWN = "mousedown";

    /** The mouse up event type, triggered by "onmouseup" event handlers. */
    public static final String TYPE_MOUSE_UP = "mouseup";

    /** The context menu event type, triggered by "oncontextmenu" event handlers. */
    public static final String TYPE_CONTEXT_MENU = "contextmenu";

    /** The code for left mouse button. */
    public static final int BUTTON_LEFT = 0;

    /** The code for middle mouse button. */
    public static final int BUTTON_MIDDLE = 1;

    /** The code for right mouse button. */
    public static final int BUTTON_RIGHT = 2;

    /** The button code for IE (1: left button, 4: middle button, 2: right button). */
    private static final int[] buttonCodeToIE = {1, 4, 2};

    /** The mouse event's horizontal and vertical coordinates. */
    private int screenX_, screenY_;

    /** The button code according to W3C (0: left button, 1: middle button, 2: right button). */
    private int button_;

    /** Whether or not the "meta" key was pressed during the firing of the event. */
    private boolean metaKey_;

    /**
     * Used to build the prototype.
     */
    public MouseEvent() {
        screenX_ = 0;
        screenY_ = 0;
        setDetail(1);
    }

    /**
     * Creates a new event instance.
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link #BUTTON_LEFT}, {@link #BUTTON_MIDDLE} or {@link #BUTTON_RIGHT}
     */
    public MouseEvent(final DomNode domNode, final String type, final boolean shiftKey,
        final boolean ctrlKey, final boolean altKey, final int button) {

        super(domNode, type, shiftKey, ctrlKey, altKey);

        if (button != BUTTON_LEFT && button != BUTTON_MIDDLE && button != BUTTON_RIGHT) {
            throw new IllegalArgumentException("Invalid button code: " + button);
        }
        button_ = button;
        metaKey_ = false;

        if (TYPE_DBL_CLICK.equals(type)) {
            setDetail(2);
        }
        else {
            setDetail(1);
        }

        // compute coordinates from the node
        final HTMLElement target = (HTMLElement) jsxGet_target();
        screenX_ = target.getPosX() + 10;
        screenY_ = target.getPosY() + 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copyPropertiesFrom(final Event event) {
        super.copyPropertiesFrom(event);
        if (event instanceof MouseEvent) {
            final MouseEvent mouseEvent = (MouseEvent) event;
            screenX_ = mouseEvent.screenX_;
            screenY_ = mouseEvent.screenY_;
            button_ = mouseEvent.button_;
            metaKey_ = mouseEvent.metaKey_;
        }
    }

    /**
     * The horizontal coordinate at which the event occurred relative to the DOM implementation's client area.
     * @return the horizontal coordinate (currently the same as {@link #jsxGet_screenX()})
     */
    public int jsxGet_clientX() {
        return screenX_;
    }

    /**
     * The horizontal coordinate at which the event occurred relative to the origin of the screen coordinate system.
     * @return the horizontal coordinate
     */
    public int jsxGet_screenX() {
        return screenX_;
    }

    /**
     * Returns the horizontal coordinate of the event relative to whole document..
     * @return the horizontal coordinate (currently the same as {@link #jsxGet_screenX()})
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:event.pageX">Mozilla doc</a>
     */
    public int jsxGet_pageX() {
        return screenX_;
    }

    /**
     * The vertical coordinate at which the event occurred relative to the DOM implementation's client area.
     * @return the horizontal coordinate (currently the same as {@link #jsxGet_screenY()})
     */
    public int jsxGet_clientY() {
        return screenY_;
    }

    /**
     * The vertical coordinate at which the event occurred relative to the origin of the screen coordinate system.
     * @return the vertical coordinate
     */
    public int jsxGet_screenY() {
        return screenY_;
    }

    /**
     * Returns the vertical coordinate of the event relative to the whole document.
     * @return the horizontal coordinate (currently the same as {@link #jsxGet_screenY()})
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:event.pageY">Mozilla doc</a>
     */
    public int jsxGet_pageY() {
        return screenY_;
    }

    /**
     * Gets the button code.
     * @return the button code
     */
    public int jsxGet_button() {
        if (getBrowserVersion().isIE()) {
            //In IE7: oncontextmenu event.button is 0
            if (jsxGet_type().equals(TYPE_CONTEXT_MENU)) {
                return 0;
            }
            return buttonCodeToIE[button_];
        }
        return button_;
    }

    /**
     * Returns whether or not the "meta" key was pressed during the event firing.
     * @return whether or not the "meta" key was pressed during the event firing
     */
    public boolean jsxGet_metaKey() {
        return metaKey_;
    }

    /**
     * Special for FF (old stuff from Netscape time).
     * @see <a href="http://unixpapa.com/js/mouse.html">Javascript Madness: Mouse Events</a>
     * @return the button code
     */
    public int jsxGet_which() {
        return button_ + 1;
    }

    /**
     * Implementation of the DOM Level 2 Event method for initializing the mouse event.
     *
     * @param type the event type
     * @param bubbles can the event bubble
     * @param cancelable can the event be canceled
     * @param view the view to use for this event
     * @param detail the detail to set for the event
     * @param screenX the initial value of screenX
     * @param screenY the initial value of screenY
     * @param clientX the initial value of clientX
     * @param clientY the initial value of clientY
     * @param ctrlKey is the control key pressed
     * @param altKey is the alt key pressed
     * @param shiftKey is the shift key pressed
     * @param metaKey is the meta key pressed
     * @param button what mouse button is pressed
     * @param relatedTarget is there a related target for the event
     */
    public void jsxFunction_initMouseEvent(
            final String type,
            final boolean bubbles,
            final boolean cancelable,
            final Object view,
            final int detail,
            final int screenX,
            final int screenY,
            final int clientX,
            final int clientY,
            final boolean ctrlKey,
            final boolean altKey,
            final boolean shiftKey,
            final boolean metaKey,
            final int button,
            final Object relatedTarget) {
        jsxFunction_initUIEvent(type, bubbles, cancelable, view, detail);
        screenX_ = screenX;
        screenY_ = screenY;
        // Ignore the clientX parameter; we always use screenX.
        // Ignore the clientY parameter; we always use screenY.
        setCtrlKey(ctrlKey);
        setAltKey(altKey);
        setShiftKey(shiftKey);
        // Ignore the metaKey parameter; we don't support it yet.
        button_ = button;
        // Ignore the relatedTarget parameter; we don't support it yet.
    }

    /**
     * Returns the mouse event currently firing, or <tt>null</tt> if no mouse event is being processed.
     * @return the mouse event currently firing
     */
    static MouseEvent getCurrentMouseEvent() {
        final Event event = (Event) Context.getCurrentContext().getThreadLocal(KEY_CURRENT_EVENT);
        if (event instanceof MouseEvent) {
            return (MouseEvent) event;
        }
        return null;
    }

    /**
     * Returns <tt>true</tt> if the specified event type should be managed as a mouse event.
     * @param type the type of event to check
     * @return <tt>true</tt> if the specified event type should be managed as a mouse event
     */
    static boolean isMouseEvent(final String type) {
        return TYPE_CLICK.equals(type)
            || TYPE_MOUSE_OVER.equals(type)
            || TYPE_MOUSE_MOVE.equals(type)
            || TYPE_MOUSE_OUT.equals(type)
            || TYPE_MOUSE_DOWN.equals(type)
            || TYPE_MOUSE_UP.equals(type)
            || TYPE_CONTEXT_MENU.equals(type);
    }

}
