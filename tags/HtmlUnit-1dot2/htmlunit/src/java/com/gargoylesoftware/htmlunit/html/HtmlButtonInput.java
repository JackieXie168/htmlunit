/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlButtonInput extends HtmlInput {

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element the xml element that represents this tag
     */
    HtmlButtonInput( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     * Reset this element to its original values.  This is a no-op for a button.
     */
    public void reset() {
    }
}
