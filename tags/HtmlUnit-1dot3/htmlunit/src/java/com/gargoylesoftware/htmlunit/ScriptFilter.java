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
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.StringReader;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.cyberneko.html.HTMLConfiguration;
import org.cyberneko.html.filters.DefaultFilter;

/**
 * A filter that will execute javascript and pass the result of any document.write calls back into
 * the input stream.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Jun Chen</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 */
public final class ScriptFilter extends DefaultFilter {

    private final HTMLConfiguration configuration_;

    private HtmlPage htmlPage_;
    private String scriptSource_;
    private String scriptCharset_;
    private StringBuffer scriptBuffer_;
    private StringBuffer newContentBuffer_;
    private String systemId_;

    private String scriptEventHandler_;
    private int eventHandlerId_;

    /**
     *  Create an instance
     *
     * @param  config The html configuration
     */
    public ScriptFilter( final HTMLConfiguration config ) {
        Assert.notNull("config", config);
        configuration_ = config;
    }

    /**
     * set the page. This method is called by the HTML parser as soon as the &lt;html&gt; element
     * has been encountered, and the page has been created
     * @param page the target page
     */
    public void setHtmlPage(HtmlPage page) {
        htmlPage_ = page;
    }

    /**
     * Start document.
     * @param locator The locator
     * @param encoding The encoding.
     * @param augmentations The Augumentations
     * @throws XNIException if a problem occurs.
     */
    public void startDocument(
            final XMLLocator locator,
            final String encoding,
            final Augmentations augmentations )
        throws
            XNIException {

        scriptSource_ = null;
        scriptCharset_ = null;
        scriptBuffer_ = null;
        if( locator == null ) {
            systemId_ = null;
        }
        else {
            systemId_ = locator.getLiteralSystemId();
        }
        scriptEventHandler_ = null;
        eventHandlerId_ = 0;
        super.startDocument( locator, encoding, augmentations );
    }


    /**
     * Start element.
     * @param element The element
     * @param attrs The xml attributes
     * @param augmentations The augmentations.
     * @throws XNIException If a problem occurs
     */
    public void startElement(
            final QName element,
            final XMLAttributes attrs,
            final Augmentations augmentations )
        throws
            XNIException {

        if( element.rawname.equalsIgnoreCase( "script" ) ) {
            final boolean isJavaScript = HtmlPage.isJavaScript( attrs.getValue("type"), attrs.getValue("language") );
            if( isJavaScript ) {
                final String src = attrs.getValue("src");
                final String charset = attrs.getValue("charset");
                if( src != null && src.length() != 0 ) {
                    scriptSource_ = src;
                    scriptCharset_ = charset;
                }
                final String ev = attrs.getValue("event");
                final String obj = attrs.getValue("for");
                if (ev != null && ev.length()>0 && obj != null && obj.length()>0) {
                    scriptEventHandler_ = obj+"."+ev;
                }

                scriptBuffer_ = new StringBuffer();
            }
        }

        super.startElement( element, attrs, augmentations );
    }


    /**
     * Empty element.
     * @param element The element
     * @param attrs The xml attributes.
     * @param augmentations The augmentations
     * @throws XNIException if a problem occurs.
     */
    public void emptyElement(
            final QName element,
            final XMLAttributes attrs,
            final Augmentations augmentations )
        throws
            XNIException {

        super.emptyElement( element, attrs, augmentations );
    }


    /**
     * Characters.
     * @param text The text
     * @param augmentations The augmentations.
     * @throws XNIException If a problem occurs
     */
    public void characters(
            final XMLString text,
            final Augmentations augmentations )
        throws
            XNIException {

        if( scriptBuffer_ == null ) {
            super.characters( text, augmentations );
        }
        else {
            super.characters( text, augmentations );
            scriptBuffer_.append( text.ch, text.offset, text.length );
        }
    }


    /**
     *  End element.
     * @param element The element
     * @param augmentations The augmentations
     * @throws XNIException if a problem occurs.
     */
    public void endElement(
            final QName element,
            final Augmentations augmentations )
        throws
            XNIException {

        super.endElement( element, augmentations );
        if( scriptBuffer_ != null ) {
            if( element.rawname.equalsIgnoreCase( "script" ) == false ) {
                throw new IllegalStateException("Other elements were contained within the script tag");
            }

            try {
                if( scriptSource_ != null ) {
                    final String result = loadScript(scriptSource_, scriptCharset_);
                    pushResult( result );
                }
                String script = scriptBuffer_.toString();

                if (scriptEventHandler_!= null && scriptEventHandler_.length()>0) {
                    final String evhName="htmlunit_evh_JJLL"+String.valueOf(eventHandlerId_);
                    script = "function "+evhName+"()\n{"+script+"}\n"
                            +scriptEventHandler_+"="+evhName+";";
                    eventHandlerId_ ++;
                }
                final String result = executeScript(script);
                pushResult( result );
            }
            finally {
                scriptSource_ = null;
                scriptCharset_ = null;
                scriptBuffer_ = null;
                scriptEventHandler_ = null;
            }
        }
    }


    /**
     * Execute an external loaded script.  Any results from document.write
     * calls are returned.
     * @param scriptSource The URL for the external script to execute.
     * @param scriptCharset The character set to decode the external script.
     * @return The script output result.
     */
    private synchronized String loadScript(final String scriptSource, final String scriptCharset) {
        newContentBuffer_ = null;
        //System.err.println ("Load script " + scriptSource);
        htmlPage_.loadExternalJavaScriptFile(scriptSource, scriptCharset);
        if( newContentBuffer_ != null ) {
            final String result = newContentBuffer_.toString();
            newContentBuffer_ = null;
            return result;
        }
        return "";
    }


    /**
     * Execute an embedded script.  Any results from document.write calls
     * are returned.
     * @param script The embedded script to execute.
     * @return The script output result.
     */
    private synchronized String executeScript( final String script ) {
        newContentBuffer_ = null;
        htmlPage_.executeJavaScriptIfPossible(script, "Embedded script", false, null);
        if( newContentBuffer_ == null ) {
            return "";
        }
        else {
            final String result = newContentBuffer_.toString();
            newContentBuffer_ = null;
            return result;
        }
    }


    /**
     * Push the output result from a script execution as an XML input source.
     * The pushed result is from document.write calls in the script.
     * @param result The script output result.
     */
    private synchronized void pushResult( final String result ) {
        if( result.length() != 0 ) {
            final XMLInputSource xmlInputSource = new XMLInputSource(
                null, systemId_, null, new StringReader( result ), "UTF-8" );
            configuration_.pushInputSource( xmlInputSource );
        }
    }


    /**
     * Write the content back into the stream.
     * @param content The content.
     */
    public synchronized void write( final String content ) {
        if( newContentBuffer_ == null ) {
            newContentBuffer_ = new StringBuffer();
        }
        newContentBuffer_.append(content);
    }
}
