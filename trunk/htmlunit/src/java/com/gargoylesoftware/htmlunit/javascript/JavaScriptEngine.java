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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.Map;
import java.lang.ref.WeakReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * A wrapper for the <a href="http://www.mozilla.org/rhino">Rhino javascript engine</a>
 * that provides browser specific features.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author  David K. Taylor
 * @author  Chris Erskine
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 */
public final class JavaScriptEngine extends ScriptEngine {

    /** Information specific to the javascript engine */
    public static final class PageInfo {

        /**
         *  The javascript.getContext()
         *  This is probably wrong to hold a reference to the context: it should be
         *  entered when necessary and exited after, therefore there is some work 
         *  to do in this area. 
         **/
        private Context context_;

        /** The javascript.getScope() */
        private Window scope_;

        /**
         * Return the window scope
         * @return the window
         */
        public Window getScope() {
            return scope_;
        }
    }


    /**
     * Map where keys are {@link HtmlPage}s and values are {@link PageInfo}s
     */
    private final Map pageInfos_ = new WeakHashMap(89);

    private static final ThreadLocal WEB_CLIENTS = new ThreadLocal();
    
    private static final ThreadLocal javaScriptRunning_ = new ThreadLocal();

    /**
     * Create an instance for the specified webclient
     *
     * @param webClient The webClient that will own this engine.
     */
    public JavaScriptEngine( final WebClient webClient ) {
        super( webClient );
    }

    /**
     * perform initialization for the given page
     * @param page the page to initialize for
     */
    public void initialize(final HtmlPage page) {
        //force allocation of the page info.
        try {
            getPageInfo(page);
        }
        catch (final RuntimeException e) {
            // usefull for debugging (else catched Xerces and nested in a XNIException 
            getLog().error("Exception while initializing JavaScript for the page", e);
        }
    }


    private PageInfo getPageInfo( final HtmlPage htmlPage ) {
        Assert.notNull( "htmlPage", htmlPage );

        final WeakReference weakReference = (WeakReference)pageInfos_.get(htmlPage);
        if( weakReference != null ) {
            final PageInfo existingPageInfo = (PageInfo)weakReference.get();
            if( existingPageInfo != null ) {
                return existingPageInfo;
            }
        }
        final WebClient webClient = htmlPage.getWebClient();
        try {
            WEB_CLIENTS.set(webClient);
            
            final Context context = Context.enter();
            context.setOptimizationLevel(-1);
            context.setErrorReporter(new StrictErrorReporter(getScriptEngineLog()));
            final Scriptable parentScope = context.initStandardObjects(null);

            final JavaScriptConfiguration jsConfig = JavaScriptConfiguration.getInstance(webClient.getBrowserVersion());
            final Iterator it = jsConfig.keySet().iterator();
            while (it.hasNext()) {
                final String jsClassName = (String) it.next();
                final ClassConfiguration config = jsConfig.getClassConfiguration(jsClassName);
                if (config.isJsObject()) {
                    final Class jsHostClass = config.getLinkedClass();
                    ScriptableObject.defineClass(parentScope, jsHostClass);
                    final ScriptableObject prototype = (ScriptableObject) ScriptableObject
                        .getClassPrototype(parentScope, jsClassName);
  
                    final Iterator propertiesIterator = config.propertyKeys().iterator();
                    while (propertiesIterator.hasNext()) {
                        final String entryKey = (String) propertiesIterator.next();
                        prototype.defineProperty(entryKey, null, config.getPropertyReadMethod(entryKey),
                            config.getPropertyWriteMethod(entryKey), 0);
                    }

                    final Iterator functionsIterator = config.functionKeys().iterator();
                    while (functionsIterator.hasNext()) {
                        final String entryKey = (String) functionsIterator.next();
                        final FunctionObject functionObject = new FunctionObject(entryKey, 
                                config.getFunctionMethod(entryKey), prototype);
                        prototype.defineProperty(entryKey, functionObject, 0);
                    }
                        
                }
            }
            
            ScriptableObject.defineClass(parentScope, ElementArray.class);
            ScriptableObject.defineClass(parentScope, OptionsArray.class);

            final Window window = (Window) context.newObject(
                parentScope, "Window", new Object[0]);

            final PageInfo newPageInfo = new PageInfo();
            newPageInfo.context_ = context;
            newPageInfo.scope_ = window;
            window.setPageInfo(newPageInfo);
            window.initialize(htmlPage);

            pageInfos_.put( htmlPage, new WeakReference(newPageInfo) );

            return newPageInfo;
        }
        catch( final Exception e ) {
            throw new ScriptException(e);
        }
//        Context.exit();
    }



    /**
     * Return the log object for this class
     * @return The log object
     */
    protected Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * Determine the scope for the page and element.
     * @param pageInfo The page info
     * @param htmlElementScope The element that will be used as context or null if
     * the page should be used as context.
     * @return The JavaScript execution scope.
     */
    private Scriptable getScope(
        final PageInfo pageInfo, final HtmlElement htmlElementScope ) {

        Scriptable scope;
        if( htmlElementScope == null ) {
            scope = pageInfo.getScope();
        }
        else {
            scope = (Scriptable)htmlElementScope.getScriptObject();
            scope.setParentScope(pageInfo.getScope());
        }
        return scope;
    }


    /**
     * Execute the specified javascript code in the context of a given html page.
     *
     * @param htmlPage The page that the code will execute within
     * @param sourceCode The javascript code to execute.
     * @param sourceName The name that will be displayed on error conditions
     * @param htmlElementScope The element that will be used as context or null if
     * the page should be used as context.
     * @return The result of executing the specified code.
     */
    public Object execute(
        final HtmlPage htmlPage, String sourceCode, final String sourceName, final HtmlElement htmlElementScope ) {

        Assert.notNull( "sourceCode", sourceCode );

        // Pre process the source code
        sourceCode = preProcess(htmlPage, sourceCode, sourceName, htmlElementScope);

        // Remove html comments around the source if needed
        sourceCode = sourceCode.trim();
        if( sourceCode.startsWith("<!--") ) {
            int startIndex = 4;

            final int endIndex;
            if( sourceCode.endsWith("-->") ) {
                endIndex = sourceCode.length()-3;
            }
            else {
                endIndex = sourceCode.length();
            }

            // Anything on the same line as the opening comment should be ignored
            char eachChar = sourceCode.charAt(startIndex);
            while( startIndex < endIndex && eachChar != '\n' && eachChar != '\r' ) {
                eachChar = sourceCode.charAt( ++startIndex );
            }

            sourceCode = sourceCode.substring(startIndex, endIndex);
        }

        final PageInfo pageInfo = getPageInfo(htmlPage);

        final int lineNumber = 1;
        final Object securityDomain = null;

        final Scriptable scope = getScope( pageInfo, htmlElementScope );

        final Boolean javaScriptAlreadyRunning = (Boolean) javaScriptRunning_.get();
        javaScriptRunning_.set(Boolean.TRUE);
        try {
            final Object result = pageInfo.context_.evaluateString(
                    scope, sourceCode, sourceName, lineNumber, securityDomain );
            return result;
        }
        catch (final Exception e ) {
            final ScriptException scriptException = new ScriptException( e, sourceCode );  
            if (getWebClient().isThrowExceptionOnScriptError()) {
                throw scriptException;
            }
            else {
                // use a ScriptException to log it because it provides good information 
                // on the source code
                getLog().info("Catched script exception", scriptException);
                return null;
            }
        }
        finally {
            javaScriptRunning_.set(javaScriptAlreadyRunning);
        }
    }


    /**
     * Call a JavaScript function and return the result.
     * @param htmlPage The page
     * @param javaScriptFunction The function to call.
     * @param thisObject The this object for class method calls.
     * @param args The list of arguments to pass to the function.
     * @param htmlElementScope The html element that will act as the context.
     * @return The result of the function call.
     */
    public Object callFunction(
            final HtmlPage htmlPage,
            final Object javaScriptFunction,
            final Object thisObject,
            final Object [] args,
            final HtmlElement htmlElementScope ) {

        final PageInfo pageInfo = getPageInfo(htmlPage);

        final Scriptable scope = getScope( pageInfo, htmlElementScope );
        // some js code (like onchange handlers) should not be triggered from JS code: 
        // => keep trace of JS running or not
        final Boolean javaScriptAlreadyRunning = (Boolean) javaScriptRunning_.get();
        javaScriptRunning_.set(Boolean.TRUE);
        final Function function = (Function) javaScriptFunction;
        try {
            final Object result = function.call( pageInfo.context_,
                scope, (Scriptable) thisObject, args );
            return result;
        }
        catch (final Exception e ) {
            final ScriptException scriptException = new ScriptException( e, 
                    pageInfo.context_.decompileFunction(function, 2));  
            if (getWebClient().isThrowExceptionOnScriptError()) {
                throw scriptException;
            }
            else {
                // use a ScriptException to log it because it provides good information 
                // on the source code
                getLog().info("Catched script exception", scriptException);
                return null;
            }
        }
        finally {
            javaScriptRunning_.set(javaScriptAlreadyRunning);
        }
    }


    /**
     * Return the string representation of the JavaScript object in the context of the given page.
     * @param htmlPage The page
     * @param javaScriptObject The object to represent at a string.
     * @return The result string.
     */
    public String toString( final HtmlPage htmlPage, final Object javaScriptObject ) {

        getPageInfo(htmlPage);

        final String result = Context.toString( javaScriptObject );
        return result;
    }

    /** 
     * Return the WebClient that is executing on the current thread.  If no client
     * can be found then return null.
     * @return The web client.
     */
    public static WebClient getWebClientForCurrentThread() {
        return (WebClient)WEB_CLIENTS.get();
    }
    
    /**
     * Indicates if JavaScript is running in current thread. <br/>
     * This allows code to know if there own evaluation is has been  triggered by some JS code.
     * @return <code>true</code> if JavaScript is running.
     */
    public boolean isScriptRunning() {
        return Boolean.TRUE.equals(javaScriptRunning_.get());
    }
}

