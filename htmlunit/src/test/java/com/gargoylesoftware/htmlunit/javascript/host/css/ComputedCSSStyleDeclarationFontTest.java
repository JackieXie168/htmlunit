/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@code font} properties of {@link ComputedCSSStyleDeclaration}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ComputedCSSStyleDeclarationFontTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "16px", "2em", "32px", "150%", "24px" })
    public void fontSizeEm() throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(div.style.fontSize);\n"
            + "    alert(style.fontSize);\n"
            + "    div.style.fontSize = '2em';\n"
            + "    alert(div.style.fontSize);\n"
            + "    alert(style.fontSize);\n"
            + "    div.style.fontSize = '150%';\n"
            + "    alert(div.style.fontSize);\n"
            + "    alert(style.fontSize);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "normal normal normal normal 16px / normal 'Times New Roman'",
                "", "normal", "", "normal", "", "normal", "", "16px", "", "normal", "", "'Times New Roman'" },
            FF = { "", "", "", "normal", "", "normal", "", "400", "", "16px", "", "20px", "", "serif",
                "", "", "", "normal", "", "normal", "", "400", "", "16px", "", "20px", "", "serif" },
            IE = { "", "", "", "normal", "", "normal", "", "400", "", "16px", "", "normal", "", "Times New Roman",
                "", "", "", "normal", "", "normal", "", "400", "", "16px", "", "normal", "", "Times New Roman" })
    public void fontInitial() throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    debug(div);\n"
            + "    document.body.appendChild(div);\n"
            + "    debug(div);\n"
            + "  }\n"
            + "  function debug(div) {\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(div.style.font);\n"
            + "    alert(style.font);\n"
            + "    alert(div.style.fontStyle);\n"
            + "    alert(style.fontStyle);\n"
            + "    alert(div.style.fontVariant);\n"
            + "    alert(style.fontVariant);\n"
            + "    alert(div.style.fontWeight);\n"
            + "    alert(style.fontWeight);\n"
            + "    alert(div.style.fontSize);\n"
            + "    alert(style.fontSize);\n"
            + "    alert(div.style.lineHeight);\n"
            + "    alert(style.lineHeight);\n"
            + "    alert(div.style.fontFamily);\n"
            + "    alert(style.fontFamily);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "15px arial, sans-serif", "normal normal normal normal 15px / normal arial, sans-serif",
                "normal", "normal",
                "oblique 15px arial, sans-serif", "oblique normal normal normal 15px / normal arial, sans-serif",
                "oblique", "oblique" },
            FF = { "15px arial,sans-serif", "", "normal", "normal",
                    "oblique 15px arial,sans-serif", "", "oblique", "oblique" },
            IE = { "15px/normal arial, sans-serif", "", "normal", "normal",
                    "oblique 15px/normal arial, sans-serif", "", "oblique", "oblique" })
    @NotYetImplemented
    public void fontStyle() throws Exception {
        font("15px arial, sans-serif", "fontStyle", "oblique");
    }

    private void font(final String fontToSet, final String property, final String value) throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    div.style.font = '" + fontToSet + "';\n"
            + "    debug(div);\n"
            + "    div.style." + property + " = '" + value + "';\n"
            + "    debug(div);\n"
            + "  }\n"
            + "  function debug(div) {\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(div.style.font);\n"
            + "    alert(style.font);\n"
            + "    alert(div.style." + property + ");\n"
            + "    alert(style." + property + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
