/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;


/**
 * A window representing a top level browser window.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 */
public class TopLevelWindow implements WebWindow {

    private String name_;
    private Page enclosedPage_;
    private WebClient webClient_;
    private WebWindow opener_;

    private Object scriptObject_;


    /**
     * Create an instance.
     * @param name The name of the new window
     * @param webClient The web client that "owns" this window.
     */
    public TopLevelWindow( final String name, final WebClient webClient ) {
        Assert.notNull("name", name);
        Assert.notNull("webClient", webClient);

        name_ = name;
        webClient_ = webClient;

        webClient_.registerWebWindow(this);
    }


    /**
     * Return the name of this window.
     *
     * @return The name of this window.
     */
    public String getName() {
        return name_;
    }


    /**
     * Return the currently loaded page or null if no page has been loaded.
     *
     * @return The currently loaded page or null if no page has been loaded.
     */
    public Page getEnclosedPage() {
        return enclosedPage_;
    }


    /**
     * Set the currently loaded page.
     *
     * @param page The new page or null if there is no page (ie empty window)
     */
    public void setEnclosedPage( final Page page ) {
        enclosedPage_ = page;
    }


    /**
     * Return the window that contains this window.  Since this is a top
     * level window, return this window.
     *
     * @return This window since there is no parent.
     */
    public WebWindow getParentWindow() {
        return this;
    }


    /**
     * Return the top level window that contains this window.  Since this
     * is a top level window, return this window.
     *
     * @return This window since it is top level.
     */
    public WebWindow getTopWindow() {
        return this;
    }


    /**
     * Return the web client that owns this window
     * @return The web client
     */
    public WebClient getWebClient() {
        return webClient_;
    }


    /**
     * Return a string representation of this object
     * @return A string representation of this object
     */
    public String toString() {
        return "TopLevelWindow[name=\""+getName()+"\"]";
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Set the javascript object that corresponds to this element.  This is not guarenteed
     * to be set even if there is a javascript object for this html element.
     * @param scriptObject The javascript object.
     */
    public void setScriptObject( final Object scriptObject ) {
        scriptObject_ = scriptObject;
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Return the javascript object that corresponds to this element.
     * @return The javascript object that corresponsd to this element.
     */
    public Object getScriptObject() {
        return scriptObject_;
    }


    /**
     * Set the opener property.  This is the WebWindow that caused this new window to be opened.
     * @param opener The new opener
     */
    public void setOpener( final WebWindow opener ) {
        opener_ = opener;
    }


    /**
     * Return the opener property.  This is the WebWindow that caused this new window to be opened.
     * @return The opener
     */
    public WebWindow getOpener() {
        return opener_;
    }

    /**
     * Close this window.
     */
    public void close() {
        getWebClient().deregisterWebWindow(this);
    }
}
