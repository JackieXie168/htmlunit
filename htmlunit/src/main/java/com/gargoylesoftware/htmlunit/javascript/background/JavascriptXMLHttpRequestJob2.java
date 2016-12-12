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
package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.js.nashorn.internal.objects.Global;

/**
 * A helper class for XMLHttpRequest.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
final class JavascriptXMLHttpRequestJob2 extends BasicJavaScriptJob {
    private final Global global_;
    private final GlobalAction action_;

    JavascriptXMLHttpRequestJob2(final Global global, final GlobalAction action) {
        global_ = global;
        action_ = action;
    }

    @Override
    public void run() {
        action_.run(global_);
    }

    @Override
    public String toString() {
        return "XMLHttpRequest Job " + getId();
    }
}
