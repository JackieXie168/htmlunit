/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.jelly;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;

/**
 * Jelly tag to load a page from a server.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class GetPageTag extends HtmlUnitTagSupport {
    private String url_ = null;
    private List parameters_ = null;
    private String method_ = "get";

    /**
     * Create an instance
     */
    public GetPageTag() {
    }


    /**
     * Process the tag
     * @param xmlOutput The xml output
     * @throws JellyTagException If a problem occurs
     */
    public void doTag(XMLOutput xmlOutput) throws JellyTagException {
        invokeBody(xmlOutput);
        try {
            final Page page = getWebClient().getPage( getUrl(), getSubmitMethod(), getParameters() );
            getContext().setVariable( getVarOrDie(), page );
            System.out.println("Loaded page: "+page.getClass().getName());
            if( page instanceof HtmlPage ) {
                System.out.println("title=["+((HtmlPage)page).getTitleText()+"]");
            }
        }
        catch( final IOException e ) {
            throw new JellyTagException(e);
        }
    }


    /**
     * Callback from Jelly to set the value of the url attribute.
     * @param url The new value.
     */
    public void setUri( final String url ) {
        url_ = url;
    }


    /**
     * Return the value of the url attribute or throw an exception if it hasn't been set.
     * @return The URL
     * @throws JellyTagException If the attribute hasn't been set or the url is malformed.
     */
    public URL getUrl() throws JellyTagException {
        if( url_ == null ) {
            throw new JellyTagException("url attribute is mandatory");
        }

        try {
            return new URL(url_);
        }
        catch( final MalformedURLException e ) {
            throw new JellyTagException("url attribute is malformed: "+url_);
        }
    }


    /**
     * Add a parameter to the request.
     * @param parameter the new parameter
     */
    public synchronized void addParameter( final String parameter ) {
        if( parameters_ == null ) {
            parameters_ = new ArrayList();
        }
        parameters_.add(parameter);
    }


    /**
     * Return the list of parameters.
     * @return The list of parameters
     */
    public synchronized List getParameters() {
        if( parameters_ == null ) {
            return Collections.EMPTY_LIST;
        }
        else {
            return parameters_;
        }
    }


    /**
     * Return the web client that is currently in use.
     * @return the web client
     * @throws JellyTagException If a web client cannot be found.
     */
    protected final WebClient getWebClient() throws JellyTagException {
        WebClient webClient = (WebClient)getContext().getVariable("webClient");
        if( webClient != null ) {
            return webClient;
        }

        final WebClientTag webClientTag = (WebClientTag) findAncestorWithClass(WebClientTag.class);
        if( webClientTag == null ) {
            throw new JellyTagException("Unable to determine webClient");
        }
        else {
            return webClientTag.getWebClient();
        }
    }


    /**
     * Callback from Jelly to set the value of the method attribute.
     * @param method The new value.
     */
    public void setMethod( final String method ) {
        method_ = method;
    }


    /**
     * Return the submit method to be used when retrieving the page.
     * @return The submit method
     * @throws JellyTagException If the submit method could not be determined.
     */
    public SubmitMethod getSubmitMethod() throws JellyTagException {
        try {
            return SubmitMethod.getInstance(method_);
        }
        catch( final IllegalArgumentException e ) {
            // Provide a nicer error message
            throw new JellyTagException("Value of method attribute is not a valid submit method: "+method_);
        }
    }
}
