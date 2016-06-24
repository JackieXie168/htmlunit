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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link DomElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public final class DomElement2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isMouseOver() throws Exception {
        final String html = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <div id='d1'>\n"
                + "    <div id='d2'>\n"
                + "      <div id='d3'>\n"
                + "      </div>\n"
                + "    </div>\n"
                + "    <div id='d4'>\n"
                + "    </div>\n"
                + "  </div>\n"
                + "</body></html>";

        HtmlPage page = loadPage(html);
        assertFalse(page.getElementById("d1").isMouseOver());
        assertFalse(page.getElementById("d2").isMouseOver());
        assertFalse(page.getElementById("d3").isMouseOver());
        assertFalse(page.getElementById("d4").isMouseOver());

        page = loadPage(html);
        page.getElementById("d1").mouseOver();
        assertTrue(page.getElementById("d1").isMouseOver());
        assertFalse(page.getElementById("d2").isMouseOver());
        assertFalse(page.getElementById("d3").isMouseOver());
        assertFalse(page.getElementById("d4").isMouseOver());

        page = loadPage(html);
        page.getElementById("d2").mouseOver();
        assertTrue(page.getElementById("d1").isMouseOver());
        assertTrue(page.getElementById("d2").isMouseOver());
        assertFalse(page.getElementById("d3").isMouseOver());
        assertFalse(page.getElementById("d4").isMouseOver());

        page = loadPage(html);
        page.getElementById("d3").mouseOver();
        assertTrue(page.getElementById("d1").isMouseOver());
        assertTrue(page.getElementById("d2").isMouseOver());
        assertTrue(page.getElementById("d3").isMouseOver());
        assertFalse(page.getElementById("d4").isMouseOver());

        page = loadPage(html);
        page.getElementById("d4").mouseOver();
        assertTrue(page.getElementById("d1").isMouseOver());
        assertFalse(page.getElementById("d2").isMouseOver());
        assertFalse(page.getElementById("d3").isMouseOver());
        assertTrue(page.getElementById("d4").isMouseOver());
    }
}
