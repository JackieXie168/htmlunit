/*
 * Copyright (c) 2016-2017 Gargoyle Software Inc.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (http://www.gnu.org/licenses/).
 */
package com.gargoylesoftware.js.host;

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Browser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

@ScriptClass("ObjectHost1")
public class ObjectHost1 extends ScriptObject {

    @Function(where = Where.CONSTRUCTOR)
    public static String parentMethod(final Object self) {
        return Browser.getCurrent().name();
    }

    @Function(value = CHROME, where = Where.CONSTRUCTOR)
    public static String inChromeOnly(final Object self) {
        return Browser.getCurrent().name();
    }

    @Getter(value = {IE, CHROME}, where = Where.CONSTRUCTOR)
    public static int length(final Object self) {
        return Browser.getCurrent() == CHROME ? 1 : 2;
    }

    public static final class Constructor extends SimpleObjectConstructor {
        public Constructor() {
            super("ObjectHost1");
        }
    }
}
