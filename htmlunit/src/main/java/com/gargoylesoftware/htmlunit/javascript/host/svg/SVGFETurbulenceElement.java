/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgFeTurbulence;

/**
 * A JavaScript object for {@code SVGFETurbulenceElement}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(domClass = SvgFeTurbulence.class)
public class SVGFETurbulenceElement extends SVGElement {

    /** The constant {@code SVG_STITCHTYPE_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_STITCHTYPE_UNKNOWN = 0;
    /** The constant {@code SVG_TURBULENCE_TYPE_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_TURBULENCE_TYPE_UNKNOWN = 0;
    /** The constant {@code SVG_STITCHTYPE_STITCH}. */
    @JsxConstant
    public static final int SVG_STITCHTYPE_STITCH = 1;
    /** The constant {@code SVG_TURBULENCE_TYPE_FRACTALNOISE}. */
    @JsxConstant
    public static final int SVG_TURBULENCE_TYPE_FRACTALNOISE = 1;
    /** The constant {@code SVG_STITCHTYPE_NOSTITCH}. */
    @JsxConstant
    public static final int SVG_STITCHTYPE_NOSTITCH = 2;
    /** The constant {@code SVG_TURBULENCE_TYPE_TURBULENCE}. */
    @JsxConstant
    public static final int SVG_TURBULENCE_TYPE_TURBULENCE = 2;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, FF, EDGE})
    public SVGFETurbulenceElement() {
    }
}
