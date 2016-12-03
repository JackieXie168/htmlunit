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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECT_SET_VALUES_CHECKS_ONLY_VALUE_ATTRIBUTE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.SELECT_DESELECT_ALL_IF_SWITCHING_UNKNOWN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "select".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlSelect extends HtmlElement implements DisabledElement, SubmittableElement, FormFieldWithNameHistory {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "select";

    private final String originalName_;
    private Collection<String> newNames_ = Collections.emptySet();

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlSelect(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
        originalName_ = getNameAttribute();
    }

    /**
     * If we were given an invalid <tt>size</tt> attribute, normalize it.
     * Then set a default selected option if none was specified and the size is 1 or less
     * and this isn't a multiple selection input.
     * @param postponed whether to use {@link com.gargoylesoftware.htmlunit.javascript.PostponedAction} or no
     */
    @Override
    protected void onAllChildrenAddedToPage(final boolean postponed) {
        // Fix the size if necessary.
        int size;
        try {
            size = Integer.parseInt(getSizeAttribute());
            if (size < 0) {
                removeAttribute("size");
                size = 0;
            }
        }
        catch (final NumberFormatException e) {
            removeAttribute("size");
            size = 0;
        }

        // Set a default selected option if necessary.
        if (getSelectedOptions().isEmpty() && size <= 1 && !isMultipleSelectEnabled()) {
            final List<HtmlOption> options = getOptions();
            if (!options.isEmpty()) {
                final HtmlOption first = options.get(0);
                first.setSelectedInternal(true);
            }
        }
    }

    /**
     * <p>Returns all of the currently selected options. The following special
     * conditions can occur if the element is in single select mode:</p>
     * <ul>
     *   <li>if multiple options are erroneously selected, the last one is returned</li>
     *   <li>if no options are selected, the first one is returned</li>
     * </ul>
     *
     * @return the currently selected options
     */
    public List<HtmlOption> getSelectedOptions() {
        final List<HtmlOption> result;
        if (isMultipleSelectEnabled()) {
            // Multiple selections possible.
            result = new ArrayList<>();
            for (final HtmlElement element : getHtmlElementDescendants()) {
                if (element instanceof HtmlOption && ((HtmlOption) element).isSelected()) {
                    result.add((HtmlOption) element);
                }
            }
        }
        else {
            // Only a single selection is possible.
            result = new ArrayList<>(1);
            HtmlOption lastSelected = null;
            for (final HtmlElement element : getHtmlElementDescendants()) {
                if (element instanceof HtmlOption) {
                    final HtmlOption option = (HtmlOption) element;
                    if (option.isSelected()) {
                        lastSelected = option;
                    }
                }
            }
            if (lastSelected != null) {
                result.add(lastSelected);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns all of the options in this select element.
     * @return all of the options in this select element
     */
    public List<HtmlOption> getOptions() {
        return Collections.unmodifiableList(this.<HtmlOption>getElementsByTagNameImpl("option"));
    }

    /**
     * Returns the indexed option.
     *
     * @param index the index
     * @return the option specified by the index
     */
    public HtmlOption getOption(final int index) {
        return this.<HtmlOption>getElementsByTagNameImpl("option").get(index);
    }

    /**
     * Returns the number of options.
     * @return the number of options
     */
    public int getOptionSize() {
        return getElementsByTagName("option").size();
    }

    /**
     * Remove options by reducing the "length" property. This has no
     * effect if the length is set to the same or greater.
     * @param newLength the new length property value
     */
    public void setOptionSize(final int newLength) {
        final List<HtmlElement> elementList = getElementsByTagName("option");

        for (int i = elementList.size() - 1; i >= newLength; i--) {
            elementList.get(i).remove();
        }
    }

    /**
     * Remove an option at the given index.
     * @param index the index of the option to remove
     */
    public void removeOption(final int index) {
        final ChildElementsIterator iterator = new ChildElementsIterator(this);
        for (int i = 0; iterator.hasNext();) {
            final DomElement element = iterator.nextElement();
            if (element instanceof HtmlOption) {
                if (i == index) {
                    element.remove();
                    ensureSelectedIndex();
                    return;
                }
                i++;
            }
        }
    }

    /**
     * Replace an option at the given index with a new option.
     * @param index the index of the option to remove
     * @param newOption the new option to replace to indexed option
     */
    public void replaceOption(final int index, final HtmlOption newOption) {
        final ChildElementsIterator iterator = new ChildElementsIterator(this);
        for (int i = 0; iterator.hasNext();) {
            final DomElement element = iterator.nextElement();
            if (element instanceof HtmlOption) {
                if (i == index) {
                    element.replace(newOption);
                    ensureSelectedIndex();
                    return;
                }
                i++;
            }
        }

        if (newOption.isSelected()) {
            setSelectedAttribute(newOption, true);
        }
    }

    /**
     * Add a new option at the end.
     * @param newOption the new option to add
     */
    public void appendOption(final HtmlOption newOption) {
        appendChild(newOption);

        ensureSelectedIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode appendChild(final Node node) {
        final DomNode response = super.appendChild(node);
        if (node instanceof HtmlOption) {
            final HtmlOption option = (HtmlOption) node;
            if (option.isSelected()) {
                doSelectOption(option, true);
            }
        }
        return response;
    }

    /**
     * Sets the "selected" state of the specified option. If this "select" element
     * is single-select, then calling this method will deselect all other options.
     *
     * Only options that are actually in the document may be selected.
     *
     * @param isSelected true if the option is to become selected
     * @param optionValue the value of the option that is to change
     * @param <P> the page type
     * @return the page contained in the current window as returned
     * by {@link com.gargoylesoftware.htmlunit.WebClient#getCurrentWindow()}
     */
    public <P extends Page> P setSelectedAttribute(final String optionValue, final boolean isSelected) {
        return setSelectedAttribute(optionValue, isSelected, true);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the "selected" state of the specified option. If this "select" element
     * is single-select, then calling this method will deselect all other options.
     *
     * Only options that are actually in the document may be selected.
     *
     * @param isSelected true if the option is to become selected
     * @param optionValue the value of the option that is to change
     * @param invokeOnFocus whether to set focus or not.
     * @param <P> the page type
     * @return the page contained in the current window as returned
     * by {@link com.gargoylesoftware.htmlunit.WebClient#getCurrentWindow()}
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P setSelectedAttribute(final String optionValue,
            final boolean isSelected, final boolean invokeOnFocus) {
        try {
            final boolean attributeOnly = hasFeature(JS_SELECT_SET_VALUES_CHECKS_ONLY_VALUE_ATTRIBUTE);
            final HtmlOption selected;
            if (attributeOnly) {
                selected = getOptionByValueStrict(optionValue);
            }
            else {
                selected = getOptionByValue(optionValue);
            }
            return setSelectedAttribute(selected, isSelected, invokeOnFocus);
        }
        catch (final ElementNotFoundException e) {
            if (hasFeature(SELECT_DESELECT_ALL_IF_SWITCHING_UNKNOWN)) {
                for (final HtmlOption o : getSelectedOptions()) {
                    o.setSelected(false);
                }
            }
            return (P) getPage();
        }
    }

    /**
     * Sets the "selected" state of the specified option. If this "select" element
     * is single-select, then calling this method will deselect all other options.
     *
     * Only options that are actually in the document may be selected.
     *
     * @param isSelected true if the option is to become selected
     * @param selectedOption the value of the option that is to change
     * @param <P> the page type
     * @return the page contained in the current window as returned
     * by {@link com.gargoylesoftware.htmlunit.WebClient#getCurrentWindow()}
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P setSelectedAttribute(final HtmlOption selectedOption, final boolean isSelected) {
        return (P) setSelectedAttribute(selectedOption, isSelected, true);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the "selected" state of the specified option. If this "select" element
     * is single-select, then calling this method will deselect all other options.
     *
     * Only options that are actually in the document may be selected.
     *
     * @param isSelected true if the option is to become selected
     * @param selectedOption the value of the option that is to change
     * @param invokeOnFocus whether to set focus or not.
     * @param <P> the page type
     * @return the page contained in the current window as returned
     * by {@link com.gargoylesoftware.htmlunit.WebClient#getCurrentWindow()}
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P setSelectedAttribute(final HtmlOption selectedOption, final boolean isSelected,
        final boolean invokeOnFocus) {
        if (isSelected && invokeOnFocus) {
            ((HtmlPage) getPage()).setFocusedElement(this);
        }

        final boolean changeSelectedState = selectedOption.isSelected() != isSelected;

        if (changeSelectedState) {
            doSelectOption(selectedOption, isSelected);
            HtmlInput.executeOnChangeHandlerIfAppropriate(this);
        }

        return (P) getPage().getWebClient().getCurrentWindow().getEnclosedPage();
    }

    private void doSelectOption(final HtmlOption selectedOption,
            final boolean isSelected) {
        // caution the HtmlOption may have been created from js and therefore the select now need
        // to "know" that it is selected
        if (isMultipleSelectEnabled()) {
            selectedOption.setSelectedInternal(isSelected);
        }
        else {
            for (final HtmlOption option : getOptions()) {
                option.setSelectedInternal(option == selectedOption && isSelected);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitNameValuePairs() {
        final String name = getNameAttribute();

        final List<HtmlOption> selectedOptions = getSelectedOptions();

        final NameValuePair[] pairs = new NameValuePair[selectedOptions.size()];

        int i = 0;
        for (final HtmlOption option : selectedOptions) {
            pairs[i++] = new NameValuePair(name, option.getValueAttribute());
        }
        return pairs;
    }

    /**
     * Indicates if this select is submittable
     * @return {@code false} if not
     */
    boolean isValidForSubmission() {
        return getOptionSize() > 0;
    }

    /**
     * Returns the value of this element to what it was at the time the page was loaded.
     */
    @Override
    public void reset() {
        for (final HtmlOption option : getOptions()) {
            option.reset();
        }
        onAllChildrenAddedToPage(false);
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        setSelectedAttribute(defaultValue, true);
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public String getDefaultValue() {
        final List<HtmlOption> options = getSelectedOptions();
        if (options.size() > 0) {
            return options.get(0).getValueAttribute();
        }
        return "";
    }

    /**
     * {@inheritDoc}
     * This implementation is empty; only checkboxes and radio buttons
     * really care what the default checked value is.
     * @see SubmittableElement#setDefaultChecked(boolean)
     * @see HtmlRadioButtonInput#setDefaultChecked(boolean)
     * @see HtmlCheckBoxInput#setDefaultChecked(boolean)
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc}
     * This implementation returns {@code false}; only checkboxes and
     * radio buttons really care what the default checked value is.
     * @see SubmittableElement#isDefaultChecked()
     * @see HtmlRadioButtonInput#isDefaultChecked()
     * @see HtmlCheckBoxInput#isDefaultChecked()
     */
    @Override
    public boolean isDefaultChecked() {
        return false;
    }

    /**
     * Returns {@code true} if this select is using "multiple select".
     * @return {@code true} if this select is using "multiple select"
     */
    public boolean isMultipleSelectEnabled() {
        return getAttribute("multiple") != ATTRIBUTE_NOT_DEFINED;
    }

    /**
     * Returns the {@link HtmlOption} object that corresponds to the specified value.
     *
     * @param value the value to search by
     * @return the {@link HtmlOption} object that corresponds to the specified value
     * @exception ElementNotFoundException If a particular element could not be found in the DOM model
     */
    public HtmlOption getOptionByValue(final String value) throws ElementNotFoundException {
        WebAssert.notNull("value", value);
        for (final HtmlOption option : getOptions()) {
            if (option.getValueAttribute().equals(value)) {
                return option;
            }
        }
        throw new ElementNotFoundException("option", "value", value);
    }

    private HtmlOption getOptionByValueStrict(final String value) throws ElementNotFoundException {
        WebAssert.notNull("value", value);
        for (final HtmlOption option : getOptions()) {
            if (option.getAttribute("value").equals(value)) {
                return option;
            }
        }
        throw new ElementNotFoundException("option", "value", value);
    }

    /**
     * Returns the {@link HtmlOption} object that has the specified text.
     *
     * @param text the text to search by
     * @return the {@link HtmlOption} object that has the specified text
     * @exception ElementNotFoundException If a particular element could not be found in the DOM model
     */
    public HtmlOption getOptionByText(final String text) throws ElementNotFoundException {
        WebAssert.notNull("text", text);
        for (final HtmlOption option : getOptions()) {
            if (option.getText().equals(text)) {
                return option;
            }
        }
        throw new ElementNotFoundException("option", "text", text);
    }

    /**
     * Returns a text representation of this element that represents what would
     * be visible to the user if this page was shown in a web browser. If the user
     * can only select one option at a time, this method returns the selected option.
     * If the user can select multiple options, this method returns all options.
     *
     * @return the element as text
     */
    // we need to preserve this method as it is there since many versions with the above documentation.
    @Override
    public String asText() {
        final List<HtmlOption> options;
        if (isMultipleSelectEnabled()) {
            options = getOptions();
        }
        else {
            options = getSelectedOptions();
        }

        final StringBuilder builder = new StringBuilder();
        for (final Iterator<HtmlOption> i = options.iterator(); i.hasNext();) {
            final HtmlOption currentOption = i.next();
            if (currentOption != null) {
                builder.append(currentOption.asText());
            }
            if (i.hasNext()) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute {@code size}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     * details on the use of this attribute.
     *
     * @return the value of the attribute {@code size} or an empty string if that attribute isn't defined
     */
    public final String getSizeAttribute() {
        return getAttribute("size");
    }

    /**
     * Returns the value of the attribute {@code multiple}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code multiple} or an empty string if that attribute isn't defined
     */
    public final String getMultipleAttribute() {
        return getAttribute("multiple");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDisabledAttribute() {
        return getAttribute("disabled");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isDisabled() {
        return hasAttribute("disabled");
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex} or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttribute("tabindex");
    }

    /**
     * Returns the value of the attribute {@code onfocus}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onfocus} or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttribute("onfocus");
    }

    /**
     * Returns the value of the attribute {@code onblur}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onblur} or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttribute("onblur");
    }

    /**
     * Returns the value of the attribute {@code onchange}. Refer to the <a
     * href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onchange} or an empty string if that attribute isn't defined
     */
    public final String getOnChangeAttribute() {
        return getAttribute("onchange");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue) {
        if ("name".equals(qualifiedName)) {
            if (newNames_.isEmpty()) {
                newNames_ = new HashSet<>();
            }
            newNames_.add(attributeValue);
        }
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOriginalName() {
        return originalName_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getNewNames() {
        return newNames_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE_BLOCK;
    }

    /**
     * Returns the value of the {@code selectedIndex} property.
     * @return the selectedIndex property
     */
    public int getSelectedIndex() {
        final List<HtmlOption> selectedOptions = getSelectedOptions();
        if (selectedOptions.isEmpty()) {
            return -1;
        }
        final List<HtmlOption> allOptions = getOptions();
        return allOptions.indexOf(selectedOptions.get(0));
    }

    /**
     * Sets the value of the {@code selectedIndex} property.
     * @param index the new value
     */
    public void setSelectedIndex(final int index) {
        for (final HtmlOption itemToUnSelect : getSelectedOptions()) {
            setSelectedAttribute(itemToUnSelect, false);
        }
        if (index < 0) {
            return;
        }

        final List<HtmlOption> allOptions = getOptions();

        if (index < allOptions.size()) {
            final HtmlOption itemToSelect = allOptions.get(index);
            setSelectedAttribute(itemToSelect, true, false);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Resets the selectedIndex if needed.
     */
    public void ensureSelectedIndex() {
        if (getOptionSize() == 0) {
            setSelectedIndex(-1);
        }
        else if (getSelectedIndex() == -1 && !isMultipleSelectEnabled()) {
            setSelectedIndex(0);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param option the option to search for
     * @return the index of the provided option or zero if not found
     */
    public int indexOf(final HtmlOption option) {
        if (option == null) {
            return 0;
        }

        int index = 0;
        for (final HtmlElement element : getHtmlElementDescendants()) {
            if (option == element) {
                return index;
            }
            index++;
        }
        return 0;
    }
}
