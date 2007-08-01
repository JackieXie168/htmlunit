/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
 * @version $Revision: 1624 $
 * @author Marc Guillemot
 */
public class MouseEvent extends Event {
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

    private final int screenX_, screenY_;

    /**
     * Used to build the prototype
     */
    public MouseEvent() {
        screenX_ = 0;
        screenY_ = 0;
    }

    /**
     * Creates a new event instance.
     * @param domNode The DOM node that triggered the event.
     * @param type The event type.
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     */
    public MouseEvent(final DomNode domNode, final String type,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        super(domNode, type, shiftKey, ctrlKey, altKey);

        // compute coordinates from the node
        final HTMLElement target = (HTMLElement) jsxGet_target();
        screenX_ = target.getPosX() + 10;
        screenY_ = target.getPosY() + 10;
    }
    
    /**
     * The horizontal coordinate at which the event occurred relative to the DOM implementation's client area.
     * @return the horizontal coordinate (currently the same than {@link #jsxGet_screenX()).
     */
    public int jsxGet_clientX() {
        return screenX_;
    }

    /**
     * The horizontal coordinate at which the event occurred relative to the origin of the screen coordinate system.
     * @return the horizontal coordinate.
     */
    public int jsxGet_screenX() {
        return screenX_;
    }

    /**
     * Returns the horizontal coordinate of the event relative to whole document..
     * @return the horizontal coordinate (currently the same than {@link #jsxGet_screenX()).
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:event.pageX">Mozilla doc</a>
     */
    public int jsxGet_pageX() {
        return screenX_;
    }

    /**
     * The vertical coordinate at which the event occurred relative to the DOM implementation's client area.
     * @return the horizontal coordinate (currently the same than {@link #jsxGet_screenY()).
     */
    public int jsxGet_clientY() {
        return screenY_;
    }

    /**
     * The vertical coordinate at which the event occurred relative to the origin of the screen coordinate system.
     * @return the vertical coordinate.
     */
    public int jsxGet_screenY() {
        return screenY_;
    }

    /**
     * Returns the vertical coordinate of the event relative to the whole document.
     * @return the horizontal coordinate (currently the same than {@link #jsxGet_screenY()).
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:event.pageY">Mozilla doc</a>
     */
    public int jsxGet_pageY() {
        return screenY_;
    }

    /**
     * Gets the mouse event currently firing
     * @return <code>null</code> if no mouse event is being processed
     */
    static MouseEvent getCurrentMouseEvent() {
        final Event event = (Event) Context.getCurrentContext().getThreadLocal(KEY_CURRENT_EVENT);
        if (event instanceof MouseEvent) {
            return (MouseEvent) event;
        }
        return null;
    }
}
