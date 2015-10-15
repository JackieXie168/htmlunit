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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;


/**
 * Tests for Inputs
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 */
public class InputTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test.
     */
    public InputTest( final String name ) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testStandardProperties_Text() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest(){\n"
                 + "    alert(document.form1.textfield1.value)\n"
                 + "    alert(document.form1.textfield1.type)\n"
                 + "    alert(document.form1.textfield1.name)\n"
                 + "    alert(document.form1.textfield1.form.name)\n"
                 + "    document.form1.textfield1.value='cat'\n"
                 + "    alert(document.form1.textfield1.value)\n"
                 +"}\n"
                 + "</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='text' name='textfield1' value='foo' />"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "foo", "text", "textfield1", "form1", "cat"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testTextProperties() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest(){\n"
                 + "    alert(document.form1.button1.type)\n"
                 + "    alert(document.form1.button2.type)\n"
                 + "    alert(document.form1.checkbox1.type)\n"
                 + "    alert(document.form1.fileupload1.type)\n"
                 + "    alert(document.form1.hidden1.type)\n"
                 + "    alert(document.form1.select1.type)\n"
                 + "    alert(document.form1.select2.type)\n"
                 + "    alert(document.form1.password1.type)\n"
                 + "    alert(document.form1.reset1.type)\n"
                 + "    alert(document.form1.reset2.type)\n"
                 + "    alert(document.form1.submit1.type)\n"
                 + "    alert(document.form1.submit2.type)\n"
                 + "    alert(document.form1.textInput1.type)\n"
                 + "    alert(document.form1.textarea1.type)\n"
                 + "}\n"
                 + "</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='button' name='button1' />"
                 + "    <button type='button' name='button2' />"
                 + "    <input type='checkbox' name='checkbox1' />"
                 + "    <input type='file' name='fileupload1' />"
                 + "    <input type='hidden' name='hidden1' />"
                 + "    <select name='select1'>"
                 + "        <option>foo</option>"
                 + "    </select>"
                 + "    <select multiple='multiple' name='select2'>"
                 + "        <option>foo</option>"
                 + "    </select>"
                 + "    <input type='password' name='password1' />"
                 + "    <input type='radio' name='radio1' />"
                 + "    <input type='reset' name='reset1' />"
                 + "    <button type='reset' name='reset2' />"
                 + "    <input type='submit' name='submit1' />"
                 + "    <button type='submit' name='submit2' />"
                 + "    <input type='text' name='textInput1' />"
                 + "    <textarea name='textarea1'>foo</textarea>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "button", "button", "checkbox", "file", "hidden", "select-one",
             "select-multiple", "password", "reset", "reset", "submit",
             "submit", "text", "textarea"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testCheckedAttribute_Checkbox() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function test() {"
                 + "    alert(document.form1.checkbox1.checked)\n"
                 + "    document.form1.checkbox1.checked=true\n"
                 + "    alert(document.form1.checkbox1.checked)\n"
                 + "}"
                 + "</script></head><body>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='cheCKbox' name='checkbox1' id='checkbox1' value='foo' />"
                 + "</form>"
                 + "<a href='javascript:test()' id='clickme'>click me</a>\n"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         final HtmlCheckBoxInput checkBox = (HtmlCheckBoxInput)page.getHtmlElementById("checkbox1");
         assertFalse( checkBox.isChecked() );
         ((HtmlAnchor)page.getHtmlElementById("clickme")).click();
         assertTrue( checkBox.isChecked() );

         final List expectedAlerts = Arrays.asList( new String[]{
             "false", "true"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testCheckedAttribute_Radio() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function test() {"
                 + "    alert(document.form1.radio1[0].checked)\n"
                 + "    alert(document.form1.radio1[1].checked)\n"
                 + "    alert(document.form1.radio1[2].checked)\n"
                 + "    document.form1.radio1[1].checked=true\n"
                 + "    alert(document.form1.radio1[0].checked)\n"
                 + "    alert(document.form1.radio1[1].checked)\n"
                 + "    alert(document.form1.radio1[2].checked)\n"
                 + "}"
                 + "</script></head><body>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='radio' name='radio1' id='radioA' value='a' checked='checked'/>"
                 + "    <input type='RADIO' name='radio1' id='radioB' value='b' />"
                 + "    <input type='radio' name='radio1' id='radioC' value='c' />"
                 + "</form>"
                 + "<a href='javascript:test()' id='clickme'>click me</a>\n"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         final HtmlRadioButtonInput radioA
            = (HtmlRadioButtonInput)page.getHtmlElementById("radioA");
         final HtmlRadioButtonInput radioB
            = (HtmlRadioButtonInput)page.getHtmlElementById("radioB");
         final HtmlRadioButtonInput radioC
            = (HtmlRadioButtonInput)page.getHtmlElementById("radioC");
         assertTrue( radioA.isChecked() );
         assertFalse( radioB.isChecked() );
         assertFalse( radioC.isChecked() );
         ((HtmlAnchor)page.getHtmlElementById("clickme")).click();
         assertFalse( radioA.isChecked() );
         assertTrue( radioB.isChecked() );
         assertFalse( radioC.isChecked() );

         final List expectedAlerts = Arrays.asList( new String[]{
             "true", "false", "false", "false", "true", "false"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testDisabledAttribute() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function test() {"
                 + "    alert(document.form1.button1.disabled)\n"
                 + "    alert(document.form1.button2.disabled)\n"
                 + "    alert(document.form1.button3.disabled)\n"
                 + "    document.form1.button1.disabled=true\n"
                 + "    document.form1.button2.disabled=false\n"
                 + "    document.form1.button3.disabled=true\n"
                 + "    alert(document.form1.button1.disabled)\n"
                 + "    alert(document.form1.button2.disabled)\n"
                 + "    alert(document.form1.button3.disabled)\n"
                 + "}"
                 + "</script></head><body>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <input type='submit' name='button1' value='1'/>"
                 + "    <input type='submit' name='button2' value='2' disabled/>"
                 + "    <input type='submit' name='button3' value='3'/>"
                 + "</form>"
                 + "<a href='javascript:test()' id='clickme'>click me</a>\n"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);
         final HtmlForm form = page.getFormByName("form1");

         final HtmlSubmitInput button1
            = (HtmlSubmitInput)form.getInputByName("button1");
         final HtmlSubmitInput button2
            = (HtmlSubmitInput)form.getInputByName("button2");
         final HtmlSubmitInput button3
            = (HtmlSubmitInput)form.getInputByName("button3");
         assertFalse( button1.isDisabled() );
         assertTrue ( button2.isDisabled() );
         assertFalse( button3.isDisabled() );
         ((HtmlAnchor)page.getHtmlElementById("clickme")).click();
         assertTrue ( button1.isDisabled() );
         assertFalse( button2.isDisabled() );
         assertTrue ( button3.isDisabled() );

         final List expectedAlerts = Arrays.asList( new String[]{
             "false", "true", "false", "true", "false", "true"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testInputValue() throws Exception {
        final String htmlContent =
            "<html><head><title>foo</title><script>"
                + "function doTest(){\n"
                + " document.form1.textfield1.value = 'blue';"
                + "}\n"
                + "</script></head><body>"
                + "<p>hello world</p>"
                + "<form name='form1' method='post' onsubmit='doTest()'>"
                + " <input type='text' name='textfield1' id='textfield1' value='foo' />"
                + "</form>"
                + "</body></html>";

        final WebClient client = new WebClient(BrowserVersion.MOZILLA_1_0);

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final URL url = URL_GARGOYLE;
        final HtmlPage page = (HtmlPage) client.getPage(url);

        final HtmlForm form = page.getFormByName("form1");
        form.submit();
    }
    /**
     * @throws Exception if the test fails
     */
    public void testInputSelect_NotDefinedAsPropertyAndFunction() throws Exception {
        final String htmlContent =
            "<html><head><title>foo</title><script>"
            + "function doTest(){\n"
            + " document.form1.textfield1.select();"
            + "}\n"
            + "</script></head><body>"
            + "<p>hello world</p>"
            + "<form name='form1' method='post' onsubmit='doTest()'>"
            + " <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "</form>"
            + "</body></html>";

        final WebClient client = new WebClient(BrowserVersion.MOZILLA_1_0);

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final URL url = URL_GARGOYLE;
        final HtmlPage page = (HtmlPage) client.getPage(url);

        final HtmlForm form = page.getFormByName("form1");
        form.submit();
    }    
    /**
     * @throws Exception if the test fails
     */
    public void testThisDotFormInOnClick() throws Exception {
        final String htmlContent = "<html>"
                + "<head><title>First</title></head>"
                + "<body>"
                + "<form name=\"form1\">"
                + "<input type=\"submit\" name=\"button1\" onClick=\"this.form.target='_blank'; return false;\">"
                + "</form>"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        assertEquals("First", page.getTitleText());

        assertEquals("", page.getFormByName("form1").getTargetAttribute());

        ((HtmlSubmitInput) page.getFormByName("form1").getInputByName("button1")).click();

        assertEquals("_blank", page.getFormByName("form1").getTargetAttribute());
    }
    
    /**
     * @throws Exception if the test fails
     */
    public void testInputNameChange() throws Exception {
        final String htmlContent = "<html><head><title>foo</title><script>"
            + "function doTest(){\n"
            + " document.form1.textfield1.name = 'changed';"
            + " alert(document.form1.changed.name);"
            + "}\n"
            + "</script></head><body>"
            + "<p>hello world</p>"
            + "<form name='form1' method='post' onsubmit='doTest()'>"
            + " <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + " <input type='submit' name='button1' value='pushme' />"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final MockWebConnection connection = (MockWebConnection) page.getWebClient()
                .getWebConnection();

        final HtmlForm form = page.getFormByName("form1");
        form.getInputByName("button1").click();

        final List expectedAlerts = Arrays.asList(new String[] {"changed"});
        assertEquals(expectedAlerts, collectedAlerts);

        final List expectedParameters = Arrays.asList(new Object[] {
            new KeyValuePair("changed", "foo"),
            new KeyValuePair("button1", "pushme")
        });
        assertEquals(expectedParameters, connection.getLastParameters()); 
    }    

    /**
     * @throws Exception if the test fails
     */
    public void testOnChange() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>"
            + "</head><body>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + " <input type='text' name='text1' onchange='alert(this.value)'>"
            + "<input name='myButton' type='button' onclick='document.form1.text1.value=\"from button\"'>"
            + "</form>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlForm form = page.getFormByName("form1");
        final HtmlTextInput textinput = (HtmlTextInput) form.getInputByName("text1");
        textinput.setValueAttribute("foo");
        final HtmlButtonInput button = (HtmlButtonInput) form.getInputByName("myButton");
        button.click();
        assertEquals("from button", textinput.getValueAttribute());

        final List expectedAlerts = Arrays.asList(new String[] {"foo"});
        assertEquals(expectedAlerts, collectedAlerts);
    }    

    /**
     * @throws Exception if the test fails
     */
    public void testOnChangeSetByJavaScript() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>"
            + "</head><body>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + " <input type='text' name='text1' id='text1'>"
            + "<input name='myButton' type='button' onclick='document.form1.text1.value=\"from button\"'>"
            + "</form>"
            + "<script>"
            + "document.getElementById('text1').onchange= function(event) { alert(this.value) };"
            + "</script>"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlForm form = page.getFormByName("form1");
        final HtmlTextInput textinput = (HtmlTextInput) form.getInputByName("text1");
        textinput.setValueAttribute("foo");
        final HtmlButtonInput button = (HtmlButtonInput) form.getInputByName("myButton");
        button.click();
        assertEquals("from button", textinput.getValueAttribute());

        final List expectedAlerts = Arrays.asList(new String[] {"foo"});
        assertEquals(expectedAlerts, collectedAlerts);
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);
    }    

    /**
     * Test the default value of a radio and checkbox buttons.
     * @throws Exception if the test fails
     */
    public void testDefautValue() throws Exception {
        final String content
             = "<html><head><title>First</title><script>\n"
             + "function doTest() {\n"
             + "    alert(document.myForm.myRadio.value);\n"
             + "    alert(document.myForm.myCheckbox.value);\n"
             + "}\n</script></head>"
             + "<body onload='doTest()'>\n"
             + "<form name='myForm' action='foo'>\n"
             + "<input type='radio' name='myRadio'/>"
             + "<input type='checkbox' name='myCheckbox'/>"
             + "</form></body></html>";

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        
        final List expectedAlerts = Arrays.asList( new String[]{"on", "on"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }
}