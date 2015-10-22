/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests for {@link HtmlSerializer}.
 *
 * @author Ahmed Ashour
 */
public class HtmlSerializerTest {

    /**
     * Test {@link HtmlSerializer#reduceWhitespace(String)}.
     */
    @Test
    public void reduceWhitespace() {
        final HtmlSerializer serializer = new HtmlSerializer();

        final int length = 80_000;
        final char[] charArray = new char[length];
        Arrays.fill(charArray, ' ');
        charArray[0] = 'a';
        charArray[length - 1] = 'a';
        final String text = new String(charArray);

        final long time = System.currentTimeMillis();
        serializer.reduceWhitespace(text);
        assertTrue("reduceWhitespace() took too much time", System.currentTimeMillis() - time < 3_000);
    }
}
