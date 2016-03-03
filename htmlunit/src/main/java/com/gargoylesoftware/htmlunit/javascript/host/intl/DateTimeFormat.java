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
package com.gargoylesoftware.htmlunit.javascript.host.intl;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DATE_WITH_LEFT_TO_RIGHT_MARK;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code Intl.DateTimeFormat}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class DateTimeFormat extends SimpleScriptable {

    private static Map<String, String> FF_FORMATS_ = new HashMap<>();
    private static Map<String, String> CHROME_FORMATS_ = new HashMap<>();
    private static Map<String, String> IE_FORMATS_ = new HashMap<>();

    private String format_;

    static {
        final String ddSlash = "\u200Edd\u200E/\u200EMM\u200E/\u200EYYYY";
        final String ddDash = "\u200Edd\u200E-\u200EMM\u200E-\u200EYYYY";
        final String mmSlash = "\u200EMM\u200E/\u200Edd\u200E/\u200EYYYY";
        
        FF_FORMATS_.put("", mmSlash);
        FF_FORMATS_.put("en-CA", "YYYY-MM-dd");
        FF_FORMATS_.put("en-NZ", ddSlash);
        FF_FORMATS_.put("en-PA", ddSlash);
        FF_FORMATS_.put("en-PR", ddSlash);

        IE_FORMATS_.putAll(FF_FORMATS_);
        CHROME_FORMATS_.putAll(FF_FORMATS_);

        IE_FORMATS_.put("en-IN", ddDash);
        IE_FORMATS_.put("en-MT", mmSlash);
    }

    /**
     * Default constructor
     */
    public DateTimeFormat() {
    }

    private DateTimeFormat(final String locale, final BrowserVersion browserVersion) {
        final Map<String, String> formats;
        if (browserVersion.isChrome()) {
            formats = CHROME_FORMATS_;
        }
        else if (browserVersion.isIE()) {
            formats = IE_FORMATS_;
        }
        else {
            formats = FF_FORMATS_;
        }
        format_ = formats.get(locale);
        if (format_ == null) {
            format_ = formats.get("");
        }
        if (!browserVersion.hasFeature(JS_DATE_WITH_LEFT_TO_RIGHT_MARK)) {
            format_ = format_.replace("\u200E", "");
        }
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final Object[] args, final Function ctorObj,
            final boolean inNewExpr) {
        String locale;
        if (args.length != 0) {
            locale = Context.toString(args[0]);
        }
        else {
            locale = "";
        }
        final Window window = getWindow(ctorObj);
        final DateTimeFormat format = new DateTimeFormat(locale, window.getBrowserVersion());
        format.setParentScope(window);
        format.setPrototype(window.getPrototype(format.getClass()));
        return format;
    }

    /**
     * Formats a date according to the locale and formatting options of this {@code DateTimeFormat} object.
     * @param object the JavaScript object to convert
     * @return the dated formated
     */
    @JsxFunction
    public String format(final Object object) {
        final Date date = (Date) Context.jsToJava(object, Date.class);
        return new SimpleDateFormat(format_).format(date);
    }

}
