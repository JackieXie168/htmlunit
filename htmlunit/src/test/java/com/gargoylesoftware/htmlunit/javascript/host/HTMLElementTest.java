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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for NodeImpl
 * 
 * @author yourgod
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @version $Revision$
 */
public class HTMLElementTest extends WebTestCase {

    /**
     * @param name The name of the test case
     */
    public HTMLElementTest(String name) {
        super(name);
    }

    /**
     * @throws Exception on test failure
     */
    public void testGetAttribute() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>test</title>\n" +
                "    <script>\n" +
                "    function doTest(){\n" +
                "       var myNode = document.getElementById('myNode');\n" +
                "       alert(myNode.title);\n" +
                "       alert(myNode.getAttribute('title'));\n" +
                "       alert(myNode.Title);\n" +
                "   }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>\n" +
                "<p id='myNode' title='a'>\n" +
                "</p>\n" +
                "</body>\n" +
                "</html>\n" +
                "";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[]{
            "a", "a", "undefined"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    public void testSetAttribute() throws Exception {
        final String content = "<html>\n" + 
                "<head>\n" + 
                "    <title>test</title>\n" + 
                "    <script>\n" + 
                "    function doTest(){\n" + 
                "       var myNode = document.getElementById('myNode');\n" + 
                "       alert(myNode.title);\n" + 
                "       myNode.setAttribute('title', 'b');\n" + 
                "       alert(myNode.title);\n" + 
                "       alert(myNode.Title);\n" + 
                "       myNode.Title = 'foo';\n" + 
                "       alert(myNode.Title);\n" + 
                "   }\n" + 
                "    </script>\n" + 
                "</head>\n" + 
                "<body onload='doTest()'>\n" + 
                "<p id='myNode' title='a'>\n" + 
                "</p>\n" + 
                "</body>\n" + 
                "</html>\n" + 
                "";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[]{
            "a", "b", "undefined", "foo"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    public void testGetAttributeNode() throws Exception {
        final String content =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var div = document.getElementById('div2');\n"
            + "      var customAtt = div.getAttributeNode('customAttribute');\n"
            + "      alertAttributeProperties(customAtt);\n"
            + "    }\n"
            + "    function alertAttributeProperties(att) {\n"
            + "      alert('expando=' + att.expando);\n"
            + "      alert('firstChild=' + att.firstChild);\n"
            + "      alert('lastChild=' + att.lastChild);\n"
            + "      alert('name=' + att.name);\n"
            + "      alert('nextSibling=' + att.nextSibling);\n"
            + "      alert('nodeName=' + att.nodeName);\n"
            + "      alert('nodeType=' + att.nodeType);\n"
            + "      alert('nodeValue=' + att.nodeValue);\n"
            + "      alert('(ownerDocument==document)=' + (att.ownerDocument==document));\n"
            + "      alert('parentNode=' + att.parentNode);\n"
            + "      alert('previousSibling=' + att.previousSibling);\n"
            + "      alert('specified=' + att.specified);\n"
            + "      alert('value=' + att.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "  <div id='div2' name='blah' customAttribute='bleh'></div>\n"
            + "  <div id='div3'></div>\n"
            + "</body>\n"
            + "</html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[]{
            "expando=true",
            "firstChild=null",
            "lastChild=null",
            "name=customAttribute",
            "nextSibling=null",
            "nodeName=customAttribute",
            "nodeType=2",
            "nodeValue=bleh",
            "(ownerDocument==document)=true",
            "parentNode=null",
            "previousSibling=null",
            "specified=true",
            "value=bleh",
        });

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    public void testSetAttributeNode() throws Exception {
        final String content =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      // Get the old alignment.\n"
            + "      var div1 = document.getElementById('div1');\n"
            + "      var a1 = div1.getAttributeNode('align');\n"
            + "      alert(a1.value);\n"
            + "      // Set the new alignment.\n"
            + "      var a2 = document.createAttribute('align');\n"
            + "      a2.value = 'right';\n"
            + "      a1 = div1.setAttributeNode(a2);\n"
            + "      alert(a1.value);\n"
            + "      alert(div1.getAttributeNode('align').value);\n"
            + "      alert(div1.getAttribute('align'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' align='left'></div>\n"
            + "</body>\n"
            + "</html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[]{
            "left", "left", "right", "right"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for getElementsByTagName
     * @throws Exception if the test fails
     */
    public void testGetElementsByTagName() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "var a1 = document.getElementsByTagName('td');\n"
            + "alert('all = ' + a1.length);\n"
            + "var firstRow = document.getElementById('r1');\n"
            + "var rowOnly = firstRow.getElementsByTagName('td');\n"
            + "alert('row = ' + rowOnly.length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<table>\n"
            + "<tr id='r1'><td>1</td><td>2</td></tr>\n"
            + "<tr id='r2'><td>3</td><td>4</td></tr>\n"
            + "</table>\n"
            + "</body></html>\n";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "all = 4", "row = 2"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * Test getting the class for the element
     * @throws Exception if the test fails
     */
    public void testGetClassName() throws Exception {
        final String content
            = "<html><head><style>.x {  font: 8pt Arial bold;  }</style>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var ele = document.getElementById('pid');\n"
            + "    var aClass = ele.className;\n"
            + "    alert('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>\n";
          
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "the class is x"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test getting the class for the element
     * @throws Exception if the test fails
     */
    public void testSetClassName() throws Exception {
        final String content
            = "<html><head><style>.x {  font: 8pt Arial bold;  }</style>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var ele = document.getElementById('pid');\n"
            + "    ele.className = 'z';"
            + "    var aClass = ele.className;\n"
            + "    alert('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>\n";
          
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "the class is z"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * 
     * @throws Exception if the test fails
     */
    public void testGetSetInnerHTMLSimple() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>test</title>\n" +
                "    <script>\n" +
                "    function doTest(){\n" +
                "       var myNode = document.getElementById('myNode');\n" +
                "       alert('Old = ' + myNode.innerHTML);\n" +
                "       myNode.innerHTML = 'New  cell value';\n" +
                "       alert('New = ' + myNode.innerHTML);\n" +
                "   }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>\n" +
                "<p id='myNode'><b>Old innerHTML</b></p>\n" +
                "</body>\n" +
                "</html>\n" +
                "";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "Old = <b>Old innerHTML</b>",
            "New = New cell value"
        });
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test the use of innerHTML to set new html code
     * @throws Exception if the test fails
     */
    public void testGetSetInnerHTMLComplex() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>test</title>\n" +
                "    <script>\n" +
                "    function doTest(){\n" +
                "       var myNode = document.getElementById('myNode');\n" +
                "       alert('Old = ' + myNode.innerHTML);\n" +
                "       myNode.innerHTML = ' <b><i id=\"newElt\">New cell value</i></b>';\n" +
                "       alert('New = ' + myNode.innerHTML);\n" +
                "       alert(document.getElementById('newElt').tagName);\n" +
                "   }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>\n" +
                "<p id='myNode'><b>Old innerHTML</b></p>\n" +
                "</body>\n" +
                "</html>\n" +
                "";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "Old = <b>Old innerHTML</b>",
            "New =  <b><i id=\"newElt\">New cell value</i></b>",
            "I"
        });
        assertEquals(expectedAlerts, collectedAlerts);

        final HtmlElement pElt = page.getHtmlElementById("myNode");
        assertEquals("p", pElt.getNodeName());
        final HtmlElement elt = page.getHtmlElementById("newElt");
        assertEquals("New cell value", elt.asText());
    }

    /**
     * 
     * @throws Exception if the test fails
     */
    public void testGetSetOuterHTMLSimple() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>test</title>\n" +
                "    <script>\n" +
                "    function doTest(){\n" +
                "       var myNode = document.getElementById('myNode');\n" +
                "       var innerNode = document.getElementById('innerNode');\n" +
                "       alert('Old = ' + innerNode.outerHTML);\n" +
                "       innerNode.outerHTML = 'New  cell value';\n" +
                "       alert('New = ' + myNode.innerHTML);\n" +
                "   }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>\n" +
                "<p id='myNode'><b id='innerNode'>Old outerHTML</b></p>\n" +
                "</body>\n" +
                "</html>\n" +
                "";
        final List collectedAlerts = new ArrayList();
        final List expectedAlerts = Arrays.asList(new String[]{
            "Old = <b id=\"innerNode\">Old outerHTML</b>",
            "New = New cell value"
        });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test the use of outerHTML to set new html code
     * @throws Exception if the test fails
     */
    public void testGetSetOuterHTMLComplex() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>test</title>\n" +
                "    <script>\n" +
                "    function doTest(){\n" +
                "       var myNode = document.getElementById('myNode');\n" +
                "       var innerNode = document.getElementById('innerNode');\n" +
                "       alert('Old = ' + innerNode.outerHTML);\n" +
                "       innerNode.outerHTML = ' <b><i id=\"newElt\">New cell value</i></b>';\n" +
                "       alert('New = ' + myNode.innerHTML);\n" +
                "       alert(document.getElementById('newElt').tagName);\n" +
                "   }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>\n" +
                "<p id='myNode'><b id='innerNode'>Old outerHTML</b></p>\n" +
                "</body>\n" +
                "</html>\n" +
                "";

        final List expectedAlerts = Arrays.asList(new String[]{
            "Old = <b id=\"innerNode\">Old outerHTML</b>",
            "New =  <b><i id=\"newElt\">New cell value</i></b>",
            "I"
        });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);

        final HtmlElement pElt = page.getHtmlElementById("myNode");
        assertEquals("p", pElt.getNodeName());
        final HtmlElement elt = page.getHtmlElementById("newElt");
        assertEquals("New cell value", elt.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testInsertAdjacentHTML() throws Exception {
            final String content = "<html><head><title>First</title>\n"
                + "<script>\n"
                + "function test()\n"
                + "{\n"
                + "  var oDiv = document.getElementById('middle');\n"
                + "  oDiv.insertAdjacentHTML('beforeEnd', ' <div id=3>before end</div> ');\n"
                + "  oDiv.insertAdjacentHTML('afterEnd', ' <div id=4>after end</div> ');\n"
                + "  oDiv.insertAdjacentHTML('beforeBegin', ' <div id=1>before begin</div> ');\n"
                + "  oDiv.insertAdjacentHTML('afterBegin', ' <div id=2>after begin</div> ');\n"
                + "  var coll = document.getElementsByTagName('DIV');\n"
                + "  for (var i=0; i<coll.length; ++i) {\n"
                + "    alert(coll[i].id);\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head>"
                + "<body onload='test()'>\n"
                + "<div id='outside' style='color: #00ff00'>\n"
                + "<div id='middle' style='color: #ff0000'>\n"
                + "inside\n"
                + "</div>\n"
                + "</div>\n"
                + "</body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "outside", "1", "middle", "2", "3", "4"
        });
        assertEquals(expectedAlerts, collectedAlerts);

        final HtmlElement elt = page.getHtmlElementById("outside");
        assertEquals("before begin after begin inside before end after end", elt.asText());
    }

    /**
     * Test the <tt>#default#clientCaps</tt> default IE behavior.
     * 
     * @throws Exception if the test fails
     */
    public void testAddBehaviorDefaultClientCaps() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>Test</title>\n" +
                "    <script>\n" +
                "    function doTest() {\n" +
                "       var body = document.body;\n" +
                "       alert('body.cpuClass = ' + body.cpuClass);\n" +
                "       var id = body.addBehavior('#default#clientCaps');\n" +
                "       alert('body.cpuClass = ' + body.cpuClass);\n" +
                "       body.removeBehavior(id);\n" +
                "       alert('body.cpuClass = ' + body.cpuClass);\n" +
                "    }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>Test</body>\n" +
                "</html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "body.cpuClass = undefined",
            "body.cpuClass = " + page.getWebClient().getBrowserVersion().getCpuClass(),
            "body.cpuClass = undefined"
        });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test the <tt>#default#homePage</tt> default IE behavior.
     * 
     * @throws Exception if the test fails
     */
    public void testAddBehaviorDefaultHomePage() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>Test</title>\n" +
                "    <script>\n" +
                "    function doTest() {\n" +
                "       var body = document.body;\n" +
                "       body.addBehavior('#default#homePage');\n" +
                "       var url = '" + URL_SECOND.toExternalForm() + "';\n" +
                "       alert('isHomePage = ' + body.isHomePage(url));\n" +
                "       body.setHomePage(url);\n" +
                "       alert('isHomePage = ' + body.isHomePage(url));\n" +
                "       body.navigateHomePage();\n" +
                "    }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>Test</body>\n" +
                "</html>";
        final String content2 = "<html></html>";
        
        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, content);
        webConnection.setResponse(URL_SECOND, content2);
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);

        final List expectedAlerts = Arrays.asList(new String[]{
            "isHomePage = false",
            "isHomePage = true"
        });
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals(URL_SECOND.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * Test the removal of behaviors.
     * 
     * @throws Exception if the test fails
     */
    public void testRemoveBehavior() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>Test</title>\n" +
                "    <script>\n" +
                "    function doTest() {\n" +
                "       var body = document.body;\n" +
                "       alert('body.isHomePage = ' + body.isHomePage);\n" +
                "       var id = body.addBehavior('#default#homePage');\n" +
                "       alert('body.isHomePage = ' + body.isHomePage('not the home page'));\n" +
                "       body.removeBehavior(id);\n" +
                "       alert('body.isHomePage = ' + body.isHomePage);\n" +
                "    }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>Test</body>\n" +
                "</html>";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "body.isHomePage = undefined",
            "body.isHomePage = false",
            "body.isHomePage = undefined"
        });
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testChildren() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "    var oDiv = document.getElementById('myDiv');\n"
            + "    for (var i=0; i<oDiv.children.length; ++i) \n"
            + "        alert(oDiv.children(i).tagName);\n"
            + "    var oCol = oDiv.children;\n"
            + "    alert(oCol.length);\n"
            + "    oDiv.insertAdjacentHTML('beforeEnd', '<br>');\n"
            + "    alert(oCol.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'><br/><div><span>test</span></div></div>\n"
            + "</body>\n"
            + "</html>";
        final List collectedAlerts = new ArrayList();
        final List expectedAlerts = Arrays.asList(new String[]{ "BR", "DIV", "2", "3" });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testSetOnclick() throws Exception {
        eventHandlerSetterGetterTest("onclick");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetOndblclick() throws Exception {
        eventHandlerSetterGetterTest("ondblclick");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetOnblur() throws Exception {
        eventHandlerSetterGetterTest("onblur");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetOnfocus() throws Exception {
        eventHandlerSetterGetterTest("onfocus");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetOnkeydown() throws Exception {
        eventHandlerSetterGetterTest("onkeydown");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetOnkeypress() throws Exception {
        eventHandlerSetterGetterTest("onkeypress");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetOnkeyup() throws Exception {
        eventHandlerSetterGetterTest("onkeyup");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetOnmousedown() throws Exception {
        eventHandlerSetterGetterTest("onmousedown");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetOnmouseup() throws Exception {
        eventHandlerSetterGetterTest("onmouseup");
    }
    /**
     * @throws Exception if the test fails
     */
    public void testSetOnmouseover() throws Exception {
        eventHandlerSetterGetterTest("onmouseover");
    }
    /**
     * @throws Exception if the test fails
     */
    public void testSetOnmouseout() throws Exception {
        eventHandlerSetterGetterTest("onmouseout");
    }
    /**
     * @throws Exception if the test fails
     */
    public void testSetOnmousemove() throws Exception {
        eventHandlerSetterGetterTest("onmousemove");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetOnresize() throws Exception {
        eventHandlerSetterGetterTest("onresize");
    }

    /**
     * @param eventName The name of the event
     * @throws Exception if the test fails
     */
    private void eventHandlerSetterGetterTest(String eventName) throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function handler(event) {}"
            + "function test()\n"
            + "{\n"
            + "    var oDiv = document.getElementById('myDiv');\n"
            + "    oDiv." + eventName + " = handler;\n"
            + "    if (oDiv." + eventName + " == handler) {\n"
            + "        alert('success');\n"
            + "    } else {\n"
            + "        alert('fail');\n"
            + "    }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'><br/><div><span>test</span></div></div>\n"
            + "</body>\n"
            + "</html>";
        final List collectedAlerts = new ArrayList();
        final List expectedAlerts = Arrays.asList(new String[]{ "success" });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        HtmlPage page = loadPage(content, collectedAlerts);
        HtmlElement div = page.getHtmlElementById("myDiv");
        
        assertNotNull("Event handler was not set", div.getEventHandler(eventName));
        
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
