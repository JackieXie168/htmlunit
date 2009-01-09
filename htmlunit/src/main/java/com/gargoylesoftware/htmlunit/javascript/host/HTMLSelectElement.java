/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

/**
 * The JavaScript object for {@link HtmlSelect}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLSelectElement extends FormField {

    private static final long serialVersionUID = 4332789476842114628L;
    private HTMLOptionsCollection optionsArray_;

    /**
     * Create an instance.
     */
    public HTMLSelectElement() {
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Initialize the object.
     *
     */
    public void initialize() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        htmlSelect.setScriptObject(this);
        if (optionsArray_ == null) {
            optionsArray_ = new HTMLOptionsCollection(this);
            optionsArray_.initialize(htmlSelect);
        }
    }

    /**
     * Removes option at the specified index.
     * @param index the index of the item to remove
     */
    public void jsxFunction_remove(final int index) {
        put(index, null, null);
    }

    /**
     * Adds a new item to the list (optionally) before the specified item.
     * @param newOptionObject the DomNode to insert
     * @param arg2 for Firefox: the DomNode to insert the previous element before (null if at end),
     * for Internet Explorer: the index where the element should be placed (optional).
     */
    public void jsxFunction_add(final HTMLOptionElement newOptionObject, final Object arg2) {
        if (getBrowserVersion().isIE()) {
            add_IE(newOptionObject, arg2);
        }
        else {
            add(newOptionObject, arg2);
        }
        ensureSelectedIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object jsxFunction_appendChild(final Object childObject) {
        final Object object = super.jsxFunction_appendChild(childObject);
        ensureSelectedIndex();
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object jsxFunction_insertBefore(final Object newChildObject, final Object refChildObject) {
        final Object object = super.jsxFunction_insertBefore(newChildObject, refChildObject);
        ensureSelectedIndex();
        return object;
    }

    /**
     * Adds a new item to the list (optionally) at the specified index in IE way.
     * @param newOptionObject the DomNode to insert
     * @param index (optional) the index where the node should be inserted
     */
    protected void add_IE(final HTMLOptionElement newOptionObject, final Object index) {
        final HtmlSelect select = getHtmlSelect();
        final HtmlOption beforeOption;
        if (Context.getUndefinedValue().equals(index)) {
            beforeOption = null;
        }
        else {
            final int intIndex = ((Integer) Context.jsToJava(index, Integer.class)).intValue();
            if (intIndex >= select.getOptionSize()) {
                beforeOption = null;
            }
            else {
                beforeOption = select.getOption(intIndex);
            }
        }

        addBefore(newOptionObject, beforeOption);
    }

    /**
     * Adds a new item to the list (optionally) before the specified item in Mozilla way.
     * @param newOptionObject the DomNode to insert
     * @param beforeOptionObject the DomNode to insert the previous element before (null if at end)
     */
    protected void add(final HTMLOptionElement newOptionObject, final Object beforeOptionObject) {
        final HtmlOption beforeOption;
        if (beforeOptionObject == null) {
            beforeOption = null;
        }
        else if (Context.getUndefinedValue().equals(beforeOptionObject)) {
            throw Context.reportRuntimeError("Not enough arguments [SelectElement.add]");
        }
        else {
            beforeOption = (HtmlOption) ((HTMLOptionElement) beforeOptionObject).getDomNodeOrDie();
        }
        addBefore(newOptionObject, beforeOption);
    }

    /**
     * Adds the option (and create the associated DOM node if needed) before the specified one
     * or at the end if the specified one in null.
     * @param newOptionObject the new option to add
     * @param beforeOption the option that should be after the option to add
     */
    protected void addBefore(final HTMLOptionElement newOptionObject, final HtmlOption beforeOption) {
        final HtmlSelect select = getHtmlSelect();

        HtmlOption htmlOption = newOptionObject.getDomNodeOrNull();
        if (htmlOption == null) {
            htmlOption = (HtmlOption) HTMLParser.getFactory(HtmlOption.TAG_NAME).createElement(
                    select.getPage(), HtmlOption.TAG_NAME, null);
        }

        if (beforeOption == null) {
            select.appendChild(htmlOption);
        }
        else {
            beforeOption.insertBefore(htmlOption);
        }
    }

    /**
     * Returns the type of this input.
     * @return the type
     */
    @Override
    public String jsxGet_type() {
        final String type;
        if (getHtmlSelect().isMultipleSelectEnabled()) {
            type = "select-multiple";
        }
        else {
            type = "select-one";
        }
        return type;
    }

    /**
     * Returns the value of the "options" property.
     * @return the options property
     */
    public HTMLOptionsCollection jsxGet_options() {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_;
    }

    /**
     * Returns the value of the "selectedIndex" property.
     * @return the selectedIndex property
     */
    public int jsxGet_selectedIndex() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        final List<HtmlOption> selectedOptions = htmlSelect.getSelectedOptions();
        if (selectedOptions.isEmpty()) {
            return -1;
        }
        final List<HtmlOption> allOptions = htmlSelect.getOptions();
        return allOptions.indexOf(selectedOptions.get(0));
    }

    /**
     * Sets the value of the "selectedIndex" property.
     * @param index the new value
     */
    public void jsxSet_selectedIndex(final int index) {
        final HtmlSelect htmlSelect = getHtmlSelect();

        for (final HtmlOption itemToUnSelect : htmlSelect.getSelectedOptions()) {
            htmlSelect.setSelectedAttribute(itemToUnSelect, false);
        }
        if (index < 0) {
            return;
        }

        final List<HtmlOption> allOptions = htmlSelect.getOptions();

        if (index < allOptions.size()) {
            final HtmlOption itemToSelect = allOptions.get(index);
            htmlSelect.setSelectedAttribute(itemToSelect, true, false);
        }
    }

    /**
     * Returns the actual value of the selected Option.
     * @return the value
     */
    @Override
    public String jsxGet_value() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        final List<HtmlOption> selectedOptions = htmlSelect.getSelectedOptions();
        if (selectedOptions.isEmpty()) {
            return "";
        }
        return selectedOptions.get(0).getValueAttribute();
    }

    /**
     * Returns the value of the "length" property.
     * @return the length property
     */
    public int jsxGet_length() {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_.jsxGet_length();
    }

    /**
     * Removes options by reducing the "length" property.
     * @param newLength the new length property value
     */
    public void jsxSet_length(final int newLength) {
        if (optionsArray_ == null) {
            initialize();
        }
        optionsArray_.jsxSet_length(newLength);
    }

    /**
     * Returns the specified indexed property.
     * @param index the index of the property
     * @param start the scriptable object that was originally queried for this property
     * @return the property
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_.get(index, start);
    }

    /**
     * Sets the index property.
     * @param index the index
     * @param start the scriptable object that was originally invoked for this property
     * @param newValue the new value
     */
    @Override
    public void put(final int index, final Scriptable start, final Object newValue) {
        if (optionsArray_ == null) {
            initialize();
        }
        optionsArray_.put(index, start, newValue);
    }

    /**
     * Returns the HTML select object.
     * @return the HTML select object
     */
    private HtmlSelect getHtmlSelect() {
        return (HtmlSelect) getDomNodeOrDie();
    }

    /**
     * Selects the option with the specified value.
     * @param newValue the value of the option to select
     */
    @Override
    public void jsxSet_value(final String newValue) {
        getHtmlSelect().setSelectedAttribute(newValue, true);
    }

    /**
     * Returns the <tt>size</tt> attribute.
     * @return the <tt>size</tt> attribute
     */
    public int jsxGet_size() {
        int size = 0;
        final String sizeAttribute = getDomNodeOrDie().getAttribute("size");
        if (sizeAttribute != HtmlSelect.ATTRIBUTE_NOT_DEFINED && sizeAttribute != HtmlSelect.ATTRIBUTE_VALUE_EMPTY) {
            try {
                size = Integer.parseInt(sizeAttribute);
            }
            catch (final Exception e) {
                //silently ignore
            }
        }
        return size;
    }

    /**
     * Sets the <tt>size</tt> attribute.
     * @param size the <tt>size</tt> attribute
     */
    public void jsxSet_size(final String size) {
        getDomNodeOrDie().setAttribute("size", size);
    }

    /**
     * Returns <tt>true</tt> if the <tt>multiple</tt> attribute is set.
     * @return <tt>true</tt> if the <tt>multiple</tt> attribute is set
     */
    public boolean jsxGet_multiple() {
        return getDomNodeOrDie().hasAttribute("multiple");
    }

    /**
     * Sets or clears the <tt>multiple</tt> attribute.
     * @param multiple <tt>true</tt> to set the <tt>multiple</tt> attribute, <tt>false</tt> to clear it
     */
    public void jsxSet_multiple(final boolean multiple) {
        if (multiple) {
            getDomNodeOrDie().setAttribute("multiple", "multiple");
        }
        else {
            getDomNodeOrDie().removeAttribute("multiple");
        }
    }

    private void ensureSelectedIndex() {
        final HtmlSelect select = getHtmlSelect();
        if (select.getOptionSize() == 0) {
            jsxSet_selectedIndex(-1);
        }
        else if (jsxGet_selectedIndex() == -1) {
            jsxSet_selectedIndex(0);
        }
    }

}
