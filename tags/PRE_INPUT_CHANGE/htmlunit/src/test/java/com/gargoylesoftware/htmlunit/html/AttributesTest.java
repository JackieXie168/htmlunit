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

import com.gargoylesoftware.htmlunit.FakeWebConnection;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * <p>Tests for all the generated attribute accessors.  This test case will
 * dynamically generate tests for all the various attributes.  The code
 * is fairly complicated but doing it this way is much easier than writing
 * individual tests for all the attributes.</p>
 * 
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class AttributesTest extends TestCase {

    private final Class classUnderTest_;
    private final Method method_;
    private final HtmlPage page_;
    private final String attributeName_;
    
    private static final List EXCLUDED_METHODS = new ArrayList();
    static {
        EXCLUDED_METHODS.add("getHtmlElementsByAttribute");    
        EXCLUDED_METHODS.add("getOneHtmlElementByAttribute");    
    };

    /**
     * Return a test suite containing a seperate test for each attribute
     * on each element.
     * 
     * @return The test suite
     * @throws Exception If the tests cannot be created.
     */
    public static Test suite() throws Exception {
        final HtmlPage page = createDummyPage();
        
        final TestSuite suite = new TestSuite();
        final String[] classesToTest = new String[] {
            "HtmlAddress", "HtmlAnchor", "HtmlApplet", "HtmlArea", 
            "HtmlBase", "HtmlBaseFont", "HtmlBidirectionalOverride", 
            "HtmlBlockQuote", "HtmlBody", "HtmlBreak", "HtmlButton", 
            "HtmlButtonInput", "HtmlCaption", "HtmlCenter", 
            "HtmlCheckBoxInput", "HtmlDefinitionDescription", 
            "HtmlDefinitionList", "HtmlDefinitionTerm", 
            "HtmlDeletedText", "HtmlDivision", "HtmlElement", 
            "HtmlFieldSet", "HtmlFileInput", "HtmlFont", "HtmlForm", 
            "HtmlFrame", "HtmlFrameSet", "HtmlHead", "HtmlHeader1", 
            "HtmlHeader2", "HtmlHeader3", "HtmlHeader4", "HtmlHeader5", 
            "HtmlHeader6", "HtmlHiddenInput", "HtmlHorizontalRule", 
            "HtmlImage", "HtmlImageInput", "HtmlInlineFrame", 
            "HtmlInlineQuotation", 
            "HtmlInsertedText", "HtmlIsIndex", "HtmlLabel", 
            "HtmlLegend", "HtmlLink", "HtmlListItem", "HtmlMap",
            "HtmlMenu", "HtmlMeta", "HtmlNoFrames", "HtmlNoScript", 
            "HtmlObject", "HtmlOption", "HtmlOptionGroup", "HtmlOrderedList", 
            /*"HtmlPage",*/ "HtmlParagraph", "HtmlParameter", "HtmlPasswordInput", 
            "HtmlPreformattedText", "HtmlRadioButtonInput", "HtmlResetInput", 
            "HtmlScript", "HtmlSelect", "HtmlSpan", "HtmlStyle", "HtmlSubmitInput", 
            "HtmlTable", "HtmlTableBody", /*"HtmlTableCell",*/ "HtmlTableColumn", 
            "HtmlTableColumnGroup", "HtmlTableData", "HtmlTableDataCell", 
            "HtmlTableFooter", "HtmlTableHeader", "HtmlTableHeaderCell", 
            "HtmlTableRow", "HtmlTextArea", "HtmlTextDirection", "HtmlTextInput", 
            "HtmlTitle", "HtmlUnorderedList" 
        };
        for( int i=0; i<classesToTest.length; i++ ) {
            final Class clazz = Class.forName(
                "com.gargoylesoftware.htmlunit.html."+classesToTest[i]);
            addTestsForClass( clazz, page, suite );
        }
        return suite;
    }
    
    /**
     * Add all the tests for a given class.
     * 
     * @param clazz The class to create tests for.
     * @param page The HtmlPage that will be passed into the constructor of the
     * objects to be tested.
     * @param suite The suite that all the tests will be placed inside.
     * @throws Exception If the tests cannot be created.
     */
    private static void addTestsForClass( 
            final Class clazz, 
            final HtmlPage page, 
            final TestSuite suite ) 
        throws
            Exception {

        final Method[] methods = clazz.getMethods();
        for( int i=0; i<methods.length; i++ ) {
            final String methodName = methods[i].getName();
            if( methodName.startsWith("get") 
                && methodName.endsWith("Attribute") 
                && EXCLUDED_METHODS.contains(methodName) == false ) {

                String attributeName = methodName.substring(3, methodName.length()-9).toLowerCase();
                if( attributeName.equals("xmllang") ) {
                    attributeName = "xml:lang";
                }
                else if( attributeName.equals("columns") ) {
                    attributeName = "cols";
                }
                else if( attributeName.equals("columnspan") ) {
                    attributeName = "colspan";
                }
                else if( attributeName.equals("textdirection") ) {
                    attributeName = "dir";
                }
                else if( attributeName.equals("httpequiv") ) {
                    attributeName = "http-equiv";
                }
                else if( attributeName.equals("acceptcharset") ) {
                    attributeName = "accept-charset";
                }
                suite.addTest( new AttributesTest(attributeName, clazz, methods[i], page) );                
            }
        }
    }
    
    /**
     * Create an instance of the test.  This will test one specific attribute
     * on one specific class.
     * @param attributeName The name of the attribute to test.
     * @param classUnderTest The class containing the attribute.
     * @param method The "getter" method for the specified attribute.
     * @param page The page that will be passed into the constructor of the object
     * to be tested.
     */
    public AttributesTest (
            final String attributeName,
            final Class classUnderTest,
            final Method method, 
            final HtmlPage page ) {

        super( createTestName(classUnderTest, method) );
        classUnderTest_ = classUnderTest;
        method_ = method;
        page_ = page;
        if( attributeName.equals("TextDirection") ) {
            attributeName_ = "dir";
        }
        else {
           attributeName_ = attributeName;
        }
    }

    /**
     * Create a name for this particular test that reflect the attribute being tested.
     * @param clazz The class containing the attribute.
     * @param method The getter method for the attribute.
     * @return The new name.
     */
    private static String createTestName( final Class clazz, final Method method ) {
        String className = clazz.getName();
        final int index = className.lastIndexOf('.');
        className = className.substring(index+1);
        
        return "testAttributes_"+className+"_"+method.getName();
    }

    /**
     * Run the actual test.
     * @throws Exception If the test fails.
     */
    protected void runTest() throws Exception {
        final String value = new String("value");
        
        final List collectedAttributeRequests = new ArrayList();
        final Element proxyElement = createProxyElement(value, collectedAttributeRequests);
        final Object objectToTest = getNewInstanceForClassUnderTest(proxyElement);
        
        collectedAttributeRequests.clear();
        final Object noObjects[] = new Object[0];
        final Object result = method_.invoke( objectToTest, noObjects );
        assertSame( value, result );
        
        final List expectedAttributeRequests = new ArrayList();
        expectedAttributeRequests.add(attributeName_.toUpperCase());
        assertEquals( expectedAttributeRequests, collectedAttributeRequests);
    }

    /**
     * Create a new instance of the class being tested.
     * @param proxyElement The element that is being wrapped by the class.
     * @return The new instance.
     * @throws Exception If the new object cannot be created.
     */
    private Object getNewInstanceForClassUnderTest(final Element proxyElement ) throws Exception {
        final Object newInstance;
        if( classUnderTest_ == HtmlTableRow.class ) {
            newInstance = new HtmlTableRow( page_, proxyElement, 1 );
        }
        else if( classUnderTest_ == HtmlTableHeaderCell.class ) {
            newInstance = new HtmlTableHeaderCell(page_, proxyElement, 1, 1 );
        }
        else if( classUnderTest_ == HtmlTableDataCell.class ) {
            newInstance = new HtmlTableDataCell(page_, proxyElement, 1, 1 );
        }
        else {
            final Constructor constructor = classUnderTest_.getDeclaredConstructor( 
                new Class[]{ HtmlPage.class, Element.class } );
            newInstance = constructor.newInstance(
                new Object[]{page_, proxyElement});
        }
        
        return newInstance;
    }
    
    /**
     * Create a {@link Proxy} {@link Element}
     * @param value The value that will be returned when the attribute value is 
     * requested.
     * @param collectedAttributeRequests A list into which all the attribute
     * requests will be placed.
     * @return The new proxy.
     */
    private Element createProxyElement( final String value, final List collectedAttributeRequests ) {
        final Class clazz = Element.class;
        final InvocationHandler handler =
            new InvocationHandler() {
                public Object invoke( Object proxy, Method method, Object[] args ) {
                    final String methodName = method.getName();

                    if( methodName.equals( "equals" ) && args.length == 1 ) {
                        return new Boolean( proxy == args[0] );
                    }
                    else if( methodName.equals( "toString" ) ) {
                        return "Proxy(" + clazz.getName() + ")";
                    }
                    else if( methodName.endsWith("getAttributeNode") ) {
                        collectedAttributeRequests.add( args[0] );
                        return createProxyAttributeNode(value);
                    }
                    return null;
                }
            };

        return (Element) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                handler );
    }

    /**
     * Create a {@link Proxy} {@link Attr} object.
     * @param value The object that will be returned by any method call to the proxy.
     * @return The new proxy.
     */
    private Attr createProxyAttributeNode( final String value ) {
        final Class clazz = Attr.class;
        final InvocationHandler handler =
            new InvocationHandler() {
                public Object invoke( Object proxy, Method method, Object[] args ) {
                    return value;
                }
            };

        return (Attr) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                handler );
    }

    /**
     * Create an HtmlPage that will be used through the tests.
     * @return The new page.
     * @throws Exception If the page cannot be created.
     */
    private static HtmlPage createDummyPage() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        return (HtmlPage)client.getPage(
            new URL( "http://first" ),
            SubmitMethod.POST, Collections.EMPTY_LIST );
    }


    public static void main( final String args[] ) {
        junit.textui.TestRunner.main( new String[]{
            "com.gargoylesoftware.htmlunit.html.AttributesTest"
        } );
    }}