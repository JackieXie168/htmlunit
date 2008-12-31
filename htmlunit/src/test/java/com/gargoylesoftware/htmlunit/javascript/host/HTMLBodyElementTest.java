/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Unit tests for {@link HTMLBodyElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLBodyElementTest extends WebTestCase {

    /**
     * Tests the default body padding and margins.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {",0px,0px,0px,0px", ",,,,", ",8px,8px,8px,8px", ",,,," },
            IE = {"0px,0px,0px,0px,0px", ",,,,", "15px 10px,10px,10px,15px,15px", ",,,," })
    public void testDefaultPaddingAndMargins() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        var s = b.currentStyle ? b.currentStyle : getComputedStyle(b, null);\n"
            + "        alert(s.padding + ',' + s.paddingLeft + ',' + s.paddingRight + ',' + s.paddingTop + ',' + s.paddingBottom);\n"
            + "        alert(b.style.padding + ',' + b.style.paddingLeft + ',' + b.style.paddingRight + ',' + b.style.paddingTop + ',' + b.style.paddingBottom);\n"
            + "        alert(s.margin + ',' + s.marginLeft + ',' + s.marginRight + ',' + s.marginTop + ',' + s.marginBottom);\n"
            + "        alert(b.style.margin + ',' + b.style.marginLeft + ',' + b.style.marginRight + ',' + b.style.marginTop + ',' + b.style.marginBottom);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object]")
    @Browsers(Browser.IE)
    public void attachEvent() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function handler() {\n"
            + "        alert(event);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        document.body.attachEvent('onclick', handler);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <input type='button' id='myInput' value='Test me'>\n"
            + "  </body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        page.<HtmlButtonInput>getHtmlElementById("myInput").click();
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "no",
            IE = "yes")
    public void doScroll() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        if(document.body.doScroll) {\n"
            + "          alert('yes');\n"
            + "          document.body.doScroll();\n"
            + "          document.body.doScroll('down');\n"
            + "        } else {\n"
            + "          alert('no');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }
}
