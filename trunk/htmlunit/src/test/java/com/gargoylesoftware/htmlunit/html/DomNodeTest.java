/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode.DescendantElementsIterator;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Tests for {@link DomNode}.
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class DomNodeTest extends WebTestCase {

    /**
     * Test hasAttributes() on an element with attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementHasAttributesWith() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");
        Assert.assertEquals("Element should have attribute", true, node.hasAttributes());
    }

    /**
     * Test hasAttributes() on an element with no attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementHasAttributesNone() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");
        final DomNode parent = node.getParentNode();
        Assert.assertEquals("Element should not have attribute", false, parent.hasAttributes());
    }

    /**
     * Test hasAttributes on a node that is not defined to have attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testNonElementHasAttributes() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");
        final DomNode child = node.getFirstDomChild();
        Assert.assertEquals("Text should not have attribute", false, child.hasAttributes());
    }

    /**
     * Test getPrefix on a node that is not defined to have a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void testNonElementGetPrefix() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");
        final DomNode child = node.getFirstDomChild();
        Assert.assertEquals("Text should not have a prefix", null, child.getPrefix());
    }

    /**
     * Test getNamespaceURI on a node that is not defined to have a namespace.
     * @throws Exception if the test fails
     */
    @Test
    public void testNonElementGetNamespaceURI() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");
        final DomNode child = node.getFirstDomChild();
        Assert.assertEquals("Text should not have a prefix", null, child.getNamespaceURI());
    }

    /**
     * Test getLocalName on a node that is not defined to have a local name.
     * @throws Exception if the test fails
     */
    @Test
    public void testNonElementGetLocalName() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");
        final DomNode child = node.getFirstDomChild();
        Assert.assertEquals("Text should not have a prefix", null, child.getLocalName());
    }

    /**
     * Test setPrefix on a node that is not defined to have a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void testNonElementSetPrefix() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");
        final DomNode child = node.getFirstDomChild();
        child.setPrefix("bar"); // This does nothing.
        Assert.assertEquals("Text should not have a prefix", null, child.getPrefix());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testRemoveAllChildren() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<p id='tag'><table>\n"
            + "<tr><td>row 1</td></tr>\n"
            + "<tr><td>row 2</td></tr>\n"
            + "</table></p></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");
        node.removeAllChildren();
        Assert.assertEquals("Did not remove all nodes", null, node.getFirstDomChild());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReplace() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'/><br><div id='tag2'/></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");

        final DomNode previousSibling = node.getPreviousDomSibling();
        final DomNode nextSibling = node.getNextDomSibling();
        final DomNode parent = node.getParentNode();

        // position among parent's children
        final int position = readPositionAmongParentChildren(node);

        final DomNode newNode = new DomText(page, "test");
        node.replace(newNode);
        assertSame("previous sibling", previousSibling, newNode.getPreviousDomSibling());
        assertSame("next sibling", nextSibling, newNode.getNextDomSibling());
        assertSame("parent", parent, newNode.getParentNode());
        assertSame(newNode, previousSibling.getNextDomSibling());
        assertSame(newNode, nextSibling.getPreviousDomSibling());
        assertEquals(position, readPositionAmongParentChildren(newNode));

        final AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, "id", "id", null, "tag2"); // with the same id as the node to replace
        final DomNode node2 = page.getHtmlElementById("tag2");
        assertEquals("div", node2.getNodeName());
        
        final DomNode node3 = HTMLParser.getFactory(HtmlSpan.TAG_NAME).createElement(
                page, HtmlSpan.TAG_NAME, attributes);
        node2.replace(node3);
        assertEquals("span", page.getHtmlElementById("tag2").getTagName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetNewNodeById() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'/></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");

        final AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, "id", "id", null, "newElt");
        final DomNode newNode = HTMLParser.getFactory(HtmlDivision.TAG_NAME).createElement(
                page, HtmlDivision.TAG_NAME, attributes);
        try {
            page.getHtmlElementById("newElt");
            fail("Element should not exist yet");
        }
        catch (final ElementNotFoundException e) {
            // nothing to do, it's ok
        }

        node.replace(newNode);

        page.getHtmlElementById("newElt");
        try {
            page.getHtmlElementById("tag");
            fail("Element should not exist anymore");
        }
        catch (final ElementNotFoundException e) {
            // nothing to do, it's ok
        }

        newNode.insertBefore(node);
        page.getHtmlElementById("tag");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAppendChild() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div><div id='tag'/></div><br></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");

        final DomNode parent = node.getParentNode();

        // position among parent's children
        final int position = readPositionAmongParentChildren(node);

        final DomNode newNode = new DomText(page, "test");
        parent.appendDomChild(newNode);
        assertSame("new node previous sibling", node, newNode.getPreviousDomSibling());
        assertSame("new node next sibling", null, newNode.getNextDomSibling());
        assertSame("next sibling", newNode, node.getNextDomSibling());
        assertSame("parent", parent, newNode.getParentNode());
        assertEquals(position + 1, readPositionAmongParentChildren(newNode));

        final DomNode newNode2 = new DomText(page, "test2");
        parent.appendDomChild(newNode2);
        page.getHtmlElementById("tag");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testInsertBefore() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'/><br></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getDocumentHtmlElement().getHtmlElementById("tag");

        final DomNode previousSibling = node.getPreviousDomSibling();
        final DomNode nextSibling = node.getNextDomSibling();
        final DomNode parent = node.getParentNode();

        // position among parent's children
        final int position = readPositionAmongParentChildren(node);

        final DomNode newNode = new DomText(page, "test");
        node.insertBefore(newNode);
        assertSame("new node previous sibling", previousSibling, newNode.getPreviousDomSibling());
        assertSame("previous sibling", newNode, node.getPreviousDomSibling());
        assertSame("new node next sibling", node, newNode.getNextDomSibling());
        assertSame("next sibling", nextSibling, node.getNextDomSibling());
        assertSame("parent", parent, newNode.getParentNode());
        assertSame(newNode, previousSibling.getNextDomSibling());
        assertSame(node, nextSibling.getPreviousDomSibling());
        assertEquals(position, readPositionAmongParentChildren(newNode));
    }

    /**
     * Reads the position of the node among the children of its parent
     * @param node the node to look at
     * @return the position
     */
    private int readPositionAmongParentChildren(final DomNode node) {
        int i = 0;
        for (final DomNode child : node.getParentNode().getChildren()) {
            if (child == node) {
                return i;
            }
            i++;
        }

        return -1;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetByXPath() throws Exception {
        final String htmlContent
            = "<html><head><title>my title</title></head><body>\n"
            + "<p id='p1'><ul><li>foo 1</li><li>foo 2</li></li></p>\n"
            + "<div><span>bla</span></div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final List< ? > results = page.getByXPath("//title");
        assertEquals(1, results.size());
        final HtmlTitle title = (HtmlTitle) results.get(0);
        assertEquals("my title", title.asText());

        final HtmlHead head = (HtmlHead) title.getParentNode();
        assertEquals(results, head.getByXPath("//title"));
        assertEquals(results, head.getByXPath("./title"));
        assertEquals(0, head.getByXPath("/title").size());
        assertEquals(results, head.getByXPath("title"));

        final HtmlParagraph p = (HtmlParagraph) page.getFirstByXPath("//p");
        assertSame(p, page.getHtmlElementById("p1"));
        final List< ? > lis = p.getByXPath("ul/li");
        assertEquals(2, lis.size());
        assertEquals(lis, page.getByXPath("//ul/li"));

        assertEquals(2, ((Number) p.getFirstByXPath("count(//li)")).intValue());
    }

    /**
     * Test that element.selectNodes("/tagName") searches from root of the tree, not from that specific element.
     * @throws Exception if the test fails
     */
    @Test
    public void selectNodes() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    doc.load('" + URL_SECOND + "');\n"
            + "    var child = doc.documentElement.firstChild;\n"
            + "    alert(child.tagName);\n"
            + "    alert(child.selectNodes('/title').length);\n"
            + "    alert(child.selectNodes('title').length);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<books><book><title>Immortality</title><author>John Smith</author></book></books>";

        final String[] expectedAlerts = {"book", "0", "1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetFirstByXPath() throws Exception {
        final String htmlContent
            = "<html><head><title>my title</title></head><body>\n"
            + "<p id='p1'><ul><li>foo 1</li><li>foo 2</li></li></p>\n"
            + "<div><span>bla</span></div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTitle title = (HtmlTitle) page.getFirstByXPath("//title");
        assertEquals("my title", title.asText());

        final HtmlHead head = (HtmlHead) title.getParentNode();
        assertSame(title, head.getFirstByXPath("//title"));
        assertSame(title, head.getFirstByXPath("./title"));
        assertNull(head.getFirstByXPath("/title"));
        assertSame(title, head.getFirstByXPath("title"));

        final HtmlParagraph p = (HtmlParagraph) page.getFirstByXPath("//p");
        assertSame(p, page.getHtmlElementById("p1"));
        final HtmlListItem listItem = (HtmlListItem) p.getFirstByXPath("ul/li");
        assertSame(listItem, page.getFirstByXPath("//ul/li"));

        assertEquals(2, ((Number) p.getFirstByXPath("count(//li)")).intValue());
    }

    /**
     * Verifies that {@link DomNode#getAllHtmlChildElements()} returns descendant elements in the correct order.
     * @throws Exception If an error occurs.
     */
    @Test
    public void testGetAllHtmlChildElementsOrder() throws Exception {
        final String html = "<html><body id='0'>\n"
            + "<span id='I'><span id='I.1'><span id='I.1.a'/><span id='I.1.b'/><span id='I.1.c'/></span>\n"
            + "<span id='I.2'><span id='I.2.a'/></span></span>\n"
            + "<span id='II'/>\n"
            + "<span id='III'><span id='III.1'><span id='III.1.a'/></span></span>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final DescendantElementsIterator iterator = (DescendantElementsIterator)
            page.getDocumentHtmlElement().getAllHtmlChildElements().iterator();
        assertEquals("", iterator.nextElement().getId());
        assertEquals("0", iterator.nextElement().getId());
        assertEquals("I", iterator.nextElement().getId());
        assertEquals("I.1", iterator.nextElement().getId());
        assertEquals("I.1.a", iterator.nextElement().getId());
        assertEquals("I.1.b", iterator.nextElement().getId());
        assertEquals("I.1.c", iterator.nextElement().getId());
        assertEquals("I.2", iterator.nextElement().getId());
        assertEquals("I.2.a", iterator.nextElement().getId());
        assertEquals("II", iterator.nextElement().getId());
        assertEquals("III", iterator.nextElement().getId());
        assertEquals("III.1", iterator.nextElement().getId());
        assertEquals("III.1.a", iterator.nextElement().getId());
        assertFalse(iterator.hasNext());
    }

    static class DomChangeListenerTestImpl implements DomChangeListener {
        private final List<String> collectedValues_ = new ArrayList<String>();
        @Test
        public void nodeAdded(final DomChangeEvent event) {
            collectedValues_.add("nodeAdded: " + event.getParentNode().getNodeName() + ','
                    + event.getChangedNode().getNodeName());
        }
        @Test
        public void nodeDeleted(final DomChangeEvent event) {
            collectedValues_.add("nodeDeleted: " + event.getParentNode().getNodeName() + ','
                    + event.getChangedNode().getNodeName());
        }
        List<String> getCollectedValues() {
            return collectedValues_;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDomChangeListenerTestImpl_insertBefore() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.createElement('DIV');\n"
            + "    p1.insertBefore(div);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";
        
        final String[] expectedValues = {"nodeAdded: p,div", "nodeAdded: p,div"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final DomChangeListenerTestImpl listenerImpl = new DomChangeListenerTestImpl();
        p1.addDomChangeListener(listenerImpl);
        page.addDomChangeListener(listenerImpl);
        final HtmlButtonInput myButton = (HtmlButtonInput) page.getHtmlElementById("myButton");
        
        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDomChangeListenerTestImpl_appendChild() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.createElement('DIV');\n"
            + "    p1.appendChild(div);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";
        
        final String[] expectedValues = {"nodeAdded: p,div", "nodeAdded: p,div"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final DomChangeListenerTestImpl listenerImpl = new DomChangeListenerTestImpl();
        p1.addDomChangeListener(listenerImpl);
        page.addDomChangeListener(listenerImpl);
        final HtmlButtonInput myButton = (HtmlButtonInput) page.getHtmlElementById("myButton");
        
        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDomChangeListenerTestImpl_removeChild() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    div.removeChild(p1);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='myDiv'><p id='p1' title='myTitle'></p></div>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";
        
        final String[] expectedValues = {"nodeDeleted: div,p", "nodeDeleted: div,p"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final DomChangeListenerTestImpl listenerImpl = new DomChangeListenerTestImpl();
        p1.addDomChangeListener(listenerImpl);
        page.addDomChangeListener(listenerImpl);
        final HtmlButtonInput myButton = (HtmlButtonInput) page.getHtmlElementById("myButton");
        
        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDomChangeListenerRegisterNewListener() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.createElement('DIV');\n"
            + "    p1.appendChild(div);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final List<String> l = new ArrayList<String>();
        final DomChangeListener listener2 = new DomChangeListenerTestImpl() {
            @Test
            @Override
            public void nodeAdded(final DomChangeEvent event) {
                l.add("in listener 2");
            }
        };
        final DomChangeListener listener1 = new DomChangeListenerTestImpl() {
            @Test
            @Override
            public void nodeAdded(final DomChangeEvent event) {
                l.add("in listener 1");
                page.addDomChangeListener(listener2);
            }
        };

        page.addDomChangeListener(listener1);

        final HtmlButtonInput myButton = (HtmlButtonInput) page.getHtmlElementById("myButton");
        myButton.click();

        final String[] expectedValues = {"in listener 1"};
        assertEquals(expectedValues, l);
        l.clear();

        myButton.click();
        final String[] expectedValues2 = {"in listener 1", "in listener 2"};
        assertEquals(expectedValues2, l);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetByXPath_XML() throws Exception {
        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, xml, "text/xml");
        client.setWebConnection(webConnection);
        final XmlPage page = (XmlPage) client.getPage(URL_FIRST);

        final List< ? > results = page.getByXPath("//title");
        assertEquals(1, results.size());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void testOwnerDocument() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "    alert(document == document.body.ownerDocument);\n"
            + "    alert(document == document.getElementById('foo').ownerDocument);\n"
            + "    alert(document == document.body.firstChild.ownerDocument);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "<div id='foo'>bla</div>\n"
            + "</body>\n"
            + "</html>";

        final String[] expectedAlerts = {"true", "true", "true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getCanonicalXPath() throws Exception {
        final String content = "<html><head></head><body><div id='div1'/><div id='div2'/></body></html>";
        final HtmlPage page = loadPage(content);
        for (final HtmlElement element : page.getAllHtmlChildElements()) {
            final List< ? extends Object> foundElements = page.getByXPath(element.getCanonicalXPath());
            assertEquals(1, foundElements.size());
            assertSame(element, foundElements.get(0));
        }
    }
}
