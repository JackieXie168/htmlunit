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
package com.gargoylesoftware.htmlunit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link BrowserRunner}.
 *
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class BrowserRunnerTest extends SimpleWebTestCase {
    private static int Counter_ = 0;

    /**
     * Setup.
     */
    @BeforeClass
    public static void setupClass() {
        Counter_++;
    }

    /**
     * Tear down.
     */
    @AfterClass
    public static void tearDownClass() {
        Counter_--;
    }

    /**
     * Methods marked @BeforeClass (and @AfterClass) were wrongly called twice when using {@link BrowserRunner}.
     * @throws Exception if the test fails
     */
    @Test
    public void thatBeforeClassMethodHasBeenCalledOnlyOnce() throws Exception {
        assertEquals(1, Counter_);
    }
}
