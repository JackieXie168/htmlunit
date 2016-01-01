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
package com.gargoylesoftware.htmlunit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * Tests for {@link SgmlPage}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public final class SgmlPageTest extends WebServerTestCase {

    /**
     * Do not cleanup WebResponse between pages if it is cached.
     * @throws Exception if the test fails
     */
    @Test
    public void onlyCacheToCleanUpWebResponse() throws Exception {
        try (final WebClient webClient = getWebClient()) {
            webClient.getOptions().setMaxInMemory(3);

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Expires", StringUtils.formatHttpDate(DateUtils.addHours(new Date(), 1))));
            getMockWebConnection().setDefaultResponse("something", 200, "Ok", "text/html", headers);
            startWebServer(getMockWebConnection());

            webClient.getPage(URL_FIRST);
            webClient.getPage(URL_FIRST);
        }
    }
}
