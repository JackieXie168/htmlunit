/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import org.junit.Test;

/**
 * Tests for {@link NicelyResynchronizingAjaxController}.
 *
 * @version $Revision$
 * @author <a href="mailto:nnk@google.com">Nick Kralevich</a>
 */
public class NicelyResynchronizingAjaxControllerTest extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final NicelyResynchronizingAjaxController controller = new NicelyResynchronizingAjaxController();
        final NicelyResynchronizingAjaxController copy = clone(controller);
        assertTrue(copy.isInOriginalThread());
    }

}
