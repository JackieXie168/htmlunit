/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  Wrapper for the html element "select"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 */
public class HtmlSelect
         extends ClickableElement
         implements SubmittableElement {

    private String[] fakeSelectedValues_;


    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element The xml element that corresponds to this html element
     */
    HtmlSelect( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     *  Return a List containing all of the currently selected options
     *
     * @return  See above
     */
    public List getSelectedOptions() {
        final List allOptions = getAllOptions();
        final List selectedOptions = new ArrayList( allOptions.size() );

        final Iterator iterator = allOptions.iterator();
        while( iterator.hasNext() ) {
            final HtmlOption option = ( HtmlOption )iterator.next();

            if( option.isSelected() ) {
                selectedOptions.add( option );
            }
        }

        return Collections.unmodifiableList( selectedOptions );
    }


    /**
     *  Return a List containing all the options
     *
     * @return  See above
     */
    public List getAllOptions() {

        final NodeList nodeList = getElement().getElementsByTagName( "option" );
        final int nodeCount = nodeList.getLength();
        final List allOptions = new ArrayList( nodeCount );
        final HtmlPage page = getPage();

        int i;
        for( i = 0; i < nodeCount; i++ ) {
            allOptions.add( page.getHtmlElement( nodeList.item( i ) ) );
        }

        return Collections.unmodifiableList( allOptions );
    }


    /**
     *  Return the indexed option.
     *
     * @param index The index
     * @return The option specified by the index
     */
    public HtmlOption getOption( final int index ) {

        final NodeList nodeList =
            getElement().getElementsByTagName( "option" );

        return (HtmlOption) getPage().getHtmlElement( nodeList.item( index ) );
    }


    /**
     * Return the number of options
     * @return The number of options
     */
    public int getOptionSize() {

        final NodeList nodeList = getElement().getElementsByTagName( "option" );
        return nodeList.getLength();
    }


    /**
     * Remove options by reducing the "length" property.  This has no
     * effect if the length is set to the same or greater.
     * @param newLength The new length property value
     */
    public void setOptionSize( final int newLength ) {
        final NodeList nodeList = getElement().getElementsByTagName( "option" );

        while( nodeList.getLength() > newLength ) {
            final Node optionNode = nodeList.item( nodeList.getLength() - 1 ) ;
            getNode().removeChild( optionNode ) ;
        }
    }


    /**
     * Remove an option at the given index.
     * @param index The index of the option to remove
     */
    public void removeOption( final int index ) {

        final NodeList nodeList = getElement().getElementsByTagName( "option" );

        final Node optionNode = nodeList.item( index ) ;
        optionNode.getParentNode().removeChild( optionNode ) ;
    }


    /**
     * Replace an option at the given index with a new option.
     * @param index The index of the option to remove
     * @param newOption The new option to replace to indexed option
     */
    public void replaceOption( final int index, final HtmlOption newOption ) {

        final NodeList nodeList = getElement().getElementsByTagName( "option" );

        final Node optionNode = nodeList.item( index ) ;
        optionNode.getParentNode().replaceChild( newOption.getNode(), optionNode ) ;
    }


    /**
     * Add a new option at the end.
     * @param newOption The new option to add
     */
    public void appendOption( final HtmlOption newOption ) {

        final Node selectNode = getNode() ;
        selectNode.appendChild( newOption.getNode() ) ;
    }


    /**
     *  Set the "selected" state of the specified option. If this "select" is
     *  single select then calling this will deselect all other options <p>
     *
     *  Only options that are actually in the document may be selected. If you
     *  need to select an option that really isn't there (ie testing error
     *  cases) then use {@link #fakeSelectedAttribute(String)} or {@link
     *  #fakeSelectedAttribute(String[])} instead.
     *
     * @param  isSelected true if the option is to become selected
     * @param  optionValue The value of the option that is to change
     * @return  The page that occupies this window after this change is made.  It
     * may be the same window or it may be a freshly loaded one.
     */
    public Page setSelectedAttribute( final String optionValue, final boolean isSelected ) {
        try {
            return setSelectedAttribute( getOptionByValue(optionValue), isSelected );
        }
        catch( final ElementNotFoundException e ) {
            throw new IllegalArgumentException("optionValue");
        }
    }

    /**
     *  Set the "selected" state of the specified option. If this "select" is
     *  single select then calling this will deselect all other options <p>
     *
     *  Only options that are actually in the document may be selected. If you
     *  need to select an option that really isn't there (ie testing error
     *  cases) then use {@link #fakeSelectedAttribute(String)} or {@link
     *  #fakeSelectedAttribute(String[])} instead.
     *
     * @param  isSelected true if the option is to become selected
     * @param  selectedOption The value of the option that is to change
     * @return  The page that occupies this window after this change is made.  It
     * may be the same window or it may be a freshly loaded one.
     */
    public Page setSelectedAttribute( final HtmlOption selectedOption, final boolean isSelected ) {

        if( isMultipleSelectEnabled() ) {
            setSelected( selectedOption.getElement(), isSelected );
        }
        else {
            final Iterator iterator = getAllOptions().iterator();
            while( iterator.hasNext() ) {
                final HtmlOption option = ( HtmlOption )iterator.next();
                setSelected(
                    option.getElement(),
                    option == selectedOption && isSelected );
            }
        }

        final HtmlPage page = getPage();
        final String onChange = getOnChangeAttribute();

        if( onChange.length() != 0 && page.getWebClient().isJavaScriptEnabled() ) {
            final ScriptResult scriptResult
                = page.executeJavaScriptIfPossible( onChange, "onChange handler", true, this );
            return scriptResult.getNewPage();
        }
        return page;
    }


    /**
     *  Set the selected value to be something that was not originally contained
     *  in the document.
     *
     * @param  optionValue The value of the new "selected" option
     */
    public void fakeSelectedAttribute( final String optionValue ) {
        Assert.notNull( "optionValue", optionValue );
        fakeSelectedAttribute( new String[]{optionValue} );
    }


    /**
     *  Set the selected values to be something that were not originally
     *  contained in the document.
     *
     * @param  optionValues The values of the new "selected" options
     */
    public void fakeSelectedAttribute( final String optionValues[] ) {
        Assert.notNull( "optionValues", optionValues );
        fakeSelectedValues_ = optionValues;
    }


    private void setSelected( final Element element, final boolean isSelected ) {
        if( isSelected ) {
            element.setAttribute( "selected", "selected" );
        }
        else {
            element.removeAttribute( "selected" );
        }
    }


    /**
     *  Return an array of KeyValuePairs that are the values that will be sent
     *  back to the server whenever the current form is submitted.<p>
     *
     *  THIS METHOD IS INTENDED FOR THE USE OF THE FRAMEWORK ONLY AND SHOULD NOT
     *  BE USED BY CONSUMERS OF HTMLUNIT. USE AT YOUR OWN RISK.
     *
     * @return  See above
     */
    public KeyValuePair[] getSubmitKeyValuePairs() {
        final String name = getNameAttribute();
        final KeyValuePair[] pairs;

        if( fakeSelectedValues_ == null ) {
            final List selectedOptions = getSelectedOptions();
            final int optionCount = selectedOptions.size();

            pairs = new KeyValuePair[optionCount];

            for( int i = 0; i < optionCount; i++ ) {
                final HtmlOption option = ( HtmlOption )selectedOptions.get( i );
                pairs[i] = new KeyValuePair( name, option.getValueAttribute() );
            }
        }
        else {
            pairs = new KeyValuePair[fakeSelectedValues_.length];
            for( int i = 0; i < pairs.length; i++ ) {
                pairs[i] = new KeyValuePair( name, fakeSelectedValues_[i] );
            }
        }
        return pairs;
    }


    /**
     * Return the value of this element to what it was at the time the page was loaded.
     */
    public void reset() {
        final Iterator iterator = getAllOptions().iterator();
        while( iterator.hasNext() ) {
            final HtmlOption option = (HtmlOption)iterator.next();
            option.reset();
        }
    }


    /**
     *  Return true if this select is using "multiple select"
     *
     * @return  See above
     */
    public boolean isMultipleSelectEnabled() {
        return getAttributeValue( "multiple" ) != ATTRIBUTE_NOT_DEFINED;
    }


    /**
     *  Return the HtmlOption object that corresponds to the specified value
     *
     * @param  value The value to search by
     * @return  See above
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public HtmlOption getOptionByValue( final String value )
        throws ElementNotFoundException {
        Assert.notNull("value", value);

        return ( HtmlOption )getOneHtmlElementByAttribute( "option", "value", value );
    }


    /**
     *  Return a text representation of this element that represents what would
     *  be visible to the user if this page was shown in a web browser. For
     *  example, a select element would return the currently selected value as
     *  text
     *
     * @return  The element as text
     */
    public String asText() {

        final List options;
        if( isMultipleSelectEnabled() ) {
            options = getAllOptions();
        }
        else {
            options = getSelectedOptions();
        }

        boolean isFirstTimeThrough = true;
        final StringBuffer buffer = new StringBuffer();

        final Iterator iterator = options.iterator();
        while( iterator.hasNext() ) {
            if( isFirstTimeThrough == true ) {
                isFirstTimeThrough = false;
            }
            else {
                buffer.append( "\n" );
            }

            final HtmlOption currentOption = ( HtmlOption )iterator.next();
            buffer.append( currentOption.asText() );
        }

        return buffer.toString();
    }


    /**
     *  Return the value of the attribute "name". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "name" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue( "name" );
    }


    /**
     *  Return the value of the attribute "size". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "size" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getSizeAttribute() {
        return getAttributeValue( "size" );
    }


    /**
     *  Return the value of the attribute "multiple". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "multiple" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getMultipleAttribute() {
        return getAttributeValue( "multiple" );
    }


    /**
     *  Return the value of the attribute "disabled". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "disabled" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getDisabledAttribute() {
        return getAttributeValue( "disabled" );
    }


    /**
     *  Return the value of the attribute "tabindex". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "tabindex" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue( "tabindex" );
    }


    /**
     *  Return the value of the attribute "onfocus". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onfocus" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue( "onfocus" );
    }


    /**
     *  Return the value of the attribute "onblur". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onblur" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue( "onblur" );
    }


    /**
     *  Return the value of the attribute "onchange". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onchange" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnChangeAttribute() {
        return getAttributeValue( "onchange" );
    }
}