/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import java.io.IOException;
import org.w3c.dom.Element;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlSubmitInput extends HtmlInput {

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element the xml element that represents this tag
     */
    HtmlSubmitInput( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     *  Submit the form that contains this input
     *
     * @deprecated Use {@link #click()} instead
     * @return  The Page that is the result of submitting this page to the
     *      server
     * @exception  IOException If an io error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page submit()
        throws
            IOException,
            ElementNotFoundException {

        return click();
    }


    /**
     *  Submit the form that contains this input
     *
     * @return  The Page that is the result of submitting this page to the
     *      server
     * @exception  IOException If an io error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page click()
        throws
            IOException,
            ElementNotFoundException {
        return super.click();
    }


    /**
     * This method will be called if there either wasn't an onclick handler or there was
     * but the result of that handler was true.  This is the default behaviour of clicking
     * the element.  In this case, the method will submit the form.
     *
     * @return The page that is currently loaded after execution of this method
     * @throws IOException If an IO error occured
     */
    protected Page doClickAction() throws IOException {
        return getEnclosingFormOrDie().submit(this);
    }


    /**
     * Reset the value of this element to its initial state.  This is a no-op for
     * this component.
     */
    public void reset() {
    }
}

