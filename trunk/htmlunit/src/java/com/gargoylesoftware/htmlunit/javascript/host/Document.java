/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;
import org.jaxen.JaxenException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.UniqueTag;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.ElementArray;

/**
 * A JavaScript object for a Document.
 * 
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_document.asp">
 * MSDN documentation</a>
 */
public final class Document extends NodeImpl {

    private static final long serialVersionUID = -7646789903352066465L;
    private String status_ = "";

    /** The buffer that will be used for calls to document.write() */
    private StringBuffer writeBuffer_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public Document() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }


    /**
     * Return the html page that this document is modeling..
     * @return The page.
     */
    public HtmlPage getHtmlPage() {
        return (HtmlPage)getDomNodeOrDie();
    }


    /**
     * Return the value of the javascript attribute "forms".
     * @return The value of this attribute.
     */
    public NativeArray jsGet_forms() {
        // TODO: Once the page has been fully loaded, there's no need to
        // rebuild this array every time.  It could be cached at that point.

        final List jsForms = new ArrayList();

        final List formElements
            = getHtmlPage().getDocumentElement().getHtmlElementsByTagNames( Collections.singletonList("form") );
        final Iterator iterator = formElements.iterator();
        while( iterator.hasNext() ) {
            final HtmlForm htmlForm = (HtmlForm)iterator.next();
            Form jsForm = (Form)htmlForm.getScriptObject();
            if( jsForm == null ) {
                jsForm = (Form)makeJavaScriptObject("Form");
                jsForm.setHtmlElement( htmlForm );
            }
            jsForms.add(jsForm);
        }

        final int attributes = READONLY;
        final Form[] array = new Form[jsForms.size()];
        jsForms.toArray(array);
        final NativeArray allForms = new NativeArray(array);
        for( int i=0; i<array.length; i++ ) {
            final String name = array[i].getHtmlElementOrDie().getAttributeValue("name");
            if( name.length() != 0 ) {
                allForms.defineProperty(name, array[i], attributes);
            }
        }

        return allForms;
    }


    /**
     * javascript function "write".
     * @param context The javascript context
     * @param scriptable The scriptable
     * @param args The arguments passed into the method.
     * @param function The function.
     * @return Nothing
     */
    public static Object jsFunction_write(
        final Context context, final Scriptable scriptable, final Object[] args,  final Function function ) {

        final Document document = (Document)scriptable;
        final String content = getStringArg(0, args, "");

        if( document.writeBuffer_ == null ) {
            // open() hasn't been called
            final HtmlPage page = (HtmlPage)document.getDomNodeOrDie();
            page.getScriptFilter().write(content);
        }
        else {
            document.writeBuffer_.append(content);
        }
        return null;
    }


    /**
     * javascript function "writeln".
     * @param context The javascript context
     * @param scriptable The scriptable
     * @param args The arguments passed into the method.
     * @param function The function.
     * @return Nothing
     */
    public static Object jsFunction_writeln(
        final Context context, final Scriptable scriptable, final Object[] args,  final Function function ) {

        final String content = getStringArg(0, args, "");
        return jsFunction_write(context, scriptable, new Object[]{ content+"\n" }, function);
    }


    private HttpState getHttpState() {
        final HtmlPage htmlPage = getHtmlPage();
        final WebConnection connection = htmlPage.getWebClient().getWebConnection();
        final URL url = htmlPage.getWebResponse().getUrl();

        return connection.getStateForUrl( url );
    }

    /**
     * Return the cookie attribute.
     * @return The cookie attribute
     */
    public String jsGet_cookie() {
        final HttpState state = getHttpState();
        final Cookie[] cookies = state.getCookies();
        if( cookies == null ) {
            return "";
        }

        final StringBuffer buffer = new StringBuffer();

        for( int i=0; i<cookies.length; i++ ) {
            if( i != 0 ) {
                buffer.append(";");
            }
            buffer.append( cookies[i].getName() );
            buffer.append( "=" );
            buffer.append( cookies[i].getValue() );
        }
        return buffer.toString();
    }


    /**
     * Return the value of the "location" property.
     * @return The value of the "location" property
     */
    public Location jsGet_location() {
        final WebWindow webWindow = ((HtmlPage)getDomNodeOrDie()).getEnclosingWindow();
        return ((Window)webWindow.getScriptObject()).jsGet_location();
    }


    /**
     * Return the value of the "images" property.
     * @return The value of the "images" property
     */
    public NativeArray jsGet_images() {
        final List jsImages = new ArrayList();

        final List imageElements
            = getHtmlPage().getDocumentElement().getHtmlElementsByTagNames( Collections.singletonList("img") );
        final Iterator iterator = imageElements.iterator();
        while( iterator.hasNext() ) {
            final HtmlImage htmlImage = (HtmlImage)iterator.next();
            Image jsImage = (Image)htmlImage.getScriptObject();
            if( jsImage == null ) {
                jsImage = (Image)makeJavaScriptObject("Image");
                jsImage.setHtmlElement( htmlImage );
            }
            jsImages.add(jsImage);
        }

        final int attributes = READONLY;
        final Image[] array = new Image[jsImages.size()];
        jsImages.toArray(array);
        final NativeArray allImages = new NativeArray(array);
        for( int i=0; i<array.length; i++ ) {
            final String name = array[i].getHtmlElementOrDie().getAttributeValue("name");
            if( name.length() != 0 ) {
                allImages.defineProperty(name, array[i], attributes);
            }
        }

        return allImages;
    }


    /**
     * Return the value of the "referrer" property.
     * @return The value of the "referrer" property
     */
    public String jsGet_referrer() {
        final String referrer = getHtmlPage().getWebResponse().getResponseHeaderValue("referrer");
        if( referrer == null ) {
            return "";
        }
        else {
            return referrer;
        }
    }


    /**
     * Return the value of the "URL" property.
     * @return The value of the "URL" property
     */
    public String jsGet_URL() {
        return getHtmlPage().getWebResponse().getUrl().toExternalForm();
    }


    /**
     * Return the value of the "all" property.
     * @return The value of the "all" property
     */
    public ElementArray jsGet_all() {
        final ElementArray all = (ElementArray) makeJavaScriptObject("ElementArray");
        final List elements = new ArrayList();
        CollectionUtils.addAll( elements, getHtmlPage().getAllHtmlChildElements() );
        all.setElements( elements );
        return all;
    }


    /**
     * javascript function "open".
     * @param context The javascript context
     * @param scriptable The scriptable
     * @param args The arguments passed into the method.
     * @param function The function.
     * @return Nothing
     */
    public static Object jsFunction_open(
        final Context context, final Scriptable scriptable, final Object[] args,  final Function function ) {

        final Document document = (Document)scriptable;
        if( document.writeBuffer_ == null ) {
            document.writeBuffer_ = new StringBuffer();
        }
        else {
            document.getLog().warn("open() called when document is already open.");
        }
        return null;
    }


    /**
     * javascript function "close".
     * @param context The javascript context
     * @param scriptable The scriptable
     * @param args The arguments passed into the method.
     * @param function The function.
     * @return Nothing
     * @throws IOException If an IO problem occurs.
     */
    public static Object jsFunction_close(
            final Context context,
            final Scriptable scriptable,
            final Object[] args,
            final Function function )
        throws
            IOException {

        final Document document = (Document)scriptable;
        if( document.writeBuffer_  == null ) {
            document.getLog().warn("close() called when document is not open.");
        }
        else {
            final WebResponse webResponse
                = new StringWebResponse(document.writeBuffer_.toString());
            final HtmlPage page = document.getDomNodeOrDie().getPage();
            final WebClient webClient = page.getWebClient();
            final WebWindow window = page.getEnclosingWindow();

            webClient.loadWebResponseInto(webResponse, window);

            document.writeBuffer_ = null;
        }
        return null;
    }

    /**
     * Get the JavaScript property "documentElement" for the document.
     * @return The root node for the document.
     */
    public Object jsGet_documentElement() {
        return getScriptableFor(((HtmlPage)getDomNodeOrDie()).getDocumentElement());
    }


    /**
     * Create a new HTML element with the given tag name.
     *
     * @param tagName The tag name
     * @return the new HTML element, or NOT_FOUND if the tag is not supported.
     */
    public Object jsFunction_createElement( final String tagName ) {
        Object result = NOT_FOUND;
        try {
            final HtmlElement htmlElement = getDomNodeOrDie().getPage().createElement(tagName);
            final Object jsElement = getScriptableFor(htmlElement);

            if( jsElement == NOT_FOUND ) {
                getLog().debug("createElement("+tagName
                    +") cannot return a result as there isn't a javascript object for the html element "
                    + htmlElement.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch( final ElementNotFoundException e ) {
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }


    /**
     * Create a new DOM text node with the given data.
     *
     * @param newData The string value for the text node.
     * @return the new text node or NOT_FOUND if there is an error.
     */
    public Object jsFunction_createTextNode( final String newData ) {
        Object result = NOT_FOUND;
        try {
            final DomNode domNode = new DomText(getDomNodeOrDie().getPage(), newData);
            final Object jsElement = getScriptableFor(domNode);

            if( jsElement == NOT_FOUND ) {
                getLog().debug("createTextNode("+newData
                    +") cannot return a result as there isn't a javascript object for the DOM node "
                    + domNode.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch( final ElementNotFoundException e ) {
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }


    /**
     * Return the element with the specified id or null if that element could
     * not be found
     * @param id The ID to search for
     * @return the element or null
     */
    public Object jsFunction_getElementById( final String id ) {
        Object result = null;
        try {
            final HtmlElement htmlElement = ((HtmlPage)getDomNodeOrDie()).getDocumentElement().getHtmlElementById(id);
            final Object jsElement = getScriptableFor(htmlElement);

            if( jsElement == NOT_FOUND ) {
                getLog().debug("getElementById("+id
                    +") cannot return a result as there isn't a javascript object for the html element "
                    + htmlElement.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to null
            
            final BrowserVersion browser = this.getHtmlPage().getEnclosingWindow().getWebClient().getBrowserVersion();
            if (BrowserVersion.INTERNET_EXPLORER.equals(browser.getApplicationName())) {
                NativeArray elements = (NativeArray) jsFunction_getElementsByName(id);
                result = elements.get(0, this);
                if (result instanceof UniqueTag) {
                    return null;
                }
                getLog().warn("getElementById(" + id + ") did a getElementByName for Internet Explorer");
                return result;
            }
            getLog().warn("getElementById(" + id 
                + ") cannot return a result as there isn't a javascript object for the html element ");
        }
        return result;
    }


    /**
     * Return all the elements with the specified tag name
     * @param tagName The name to search for
     * @return the list of elements
     */
    public Object jsFunction_getElementsByTagName( final String tagName ) {
        final HtmlPage page = (HtmlPage)getDomNodeOrDie();
        final List list = page.getDocumentElement().getHtmlElementsByTagNames(
            Collections.singletonList(tagName.toLowerCase()));
        final ListIterator iterator = list.listIterator();
        while(iterator.hasNext()) {
            final HtmlElement htmlElement = (HtmlElement)iterator.next();
            iterator.set( getScriptableFor(htmlElement) );
        }

        return new NativeArray( list.toArray() );
    }
    /**
     * Returns all HTML elements that have a "name" attribute with the given value
     * 
     * Refer to <a href="http://www.w3.org/TR/DOM-Level-2-HTML/html.html#ID-71555259">
     * The DOM spec</a> for details.
     * 
     * @param elementName - value of the "name" attribute to look for
     * @return NodeList of elements
     */
    public Object jsFunction_getElementsByName( final String elementName ) {
        final HtmlPage page = (HtmlPage)getDomNodeOrDie();
        final String exp = "//*[@name='" + elementName + "']";
        final List list;
        try {
            final HtmlUnitXPath xpath = new HtmlUnitXPath(exp);
            list = xpath.selectNodes(page);
        } 
        catch (JaxenException e) {
            return new NativeArray(0);
        }
        final ListIterator iterator = list.listIterator();
        while(iterator.hasNext()) {
            final HtmlElement htmlElement = (HtmlElement)iterator.next();
            iterator.set( getScriptableFor(htmlElement) );
        }        
        return new NativeArray( list.toArray() );
    }


    /**
     * Return the specified property or NOT_FOUND if it could not be found. This is
     * overridden to check to see if the name belongs to a form within this document.
     * @param name The name of the property
     * @param start The scriptable object that was originally queried for this property
     * @return The property.
     */
    public Object get( final String name, final Scriptable start ) {
        // Some calls to get will happen during the initialization of the superclass.
        // At this point, we don't have enough information to do our own initialization
        // so we have to just pass this call through to the superclass.
        final HtmlPage htmlPage = (HtmlPage)getDomNodeOrNull();
        if( htmlPage == null ) {
            return super.get(name, start);
        }

        try {
            final HtmlForm htmlForm = htmlPage.getFormByName(name);
            Form jsForm = (Form)htmlForm.getScriptObject();
            if( jsForm == null ) {
                // Create new one here.
                jsForm = (Form)makeJavaScriptObject("Form");
                jsForm.setHtmlElement( htmlForm );
                return jsForm;
            }
            else {
                return jsForm;
            }
        }
        catch( final ElementNotFoundException e ) {
            // There are no forms with the specified name so pass the request
            // up to the superclass.
            return super.get(name, start);
        }
    }

    /**
     * Return the text from the status line.
     * @return the status line text
     */
    public Object jsGet_status() {
        return status_;
    }

    /**
     * Set the text from the status line.
     * @param message the status line text
     */
    public void jsSet_status( final String message ) {
        status_ = message;

        final HtmlPage page = getHtmlPage();
        final StatusHandler statusHandler = page.getWebClient().getStatusHandler();
        if( statusHandler != null ) {
            statusHandler.statusMessageChanged(page, message);
        }
    }

    /**
     * Gets the body element this document
     * @return body element
     */
    public Object jsGet_body() {
        final List list = getHtmlPage().getDocumentElement().getHtmlElementsByTagName("body");
        if( list.size() == 0 ) {
            return NOT_FOUND;
        }
        else {
            final DomNode bodyElement = (DomNode)list.get(0);
            return getScriptableFor(bodyElement);
        }
    }
    

    /**
     * Gets the title of this document
     * @return body element
     */
    public String jsGet_title() {
        return getHtmlPage().getTitleText();
    }
    
    /**
     * Set the title.
     * @param message The new title
     */
    public void jsSet_title(final String message ) {
        getHtmlPage().setTitleText(message);
    }
    
    /**
     * Get the readyState of this document.  This is an IE only function
     * @return the state - uninitilized, loading or complete - The interactive state is not returned
     * 
     */
    public String jsGet_readyState() {
        final DomNode node = getDomNodeOrDie();
        if (node instanceof HtmlPage) {
            return ((HtmlPage) node).getDocumentElement().getReadyState();
        }
        return getDomNodeOrDie().getReadyState();
    }
}

