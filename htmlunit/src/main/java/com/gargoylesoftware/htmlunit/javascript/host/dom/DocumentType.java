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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCTYPE_ENTITIES_NULL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCTYPE_NOTATIONS_NULL;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF45;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code DocumentType}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762752.aspx">MSDN documentation</a>
 * @see <a href="http://www.xulplanet.com/references/objref/DocumentType.html">XUL Planet</a>
 */
@JsxClass(domClass = DomDocumentType.class)
public class DocumentType extends Node {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, FF, EDGE})
    public DocumentType() {
    }

    /**
     * Returns the name.
     * @return the name
     */
    @JsxGetter
    public String getName() {
        return ((DomDocumentType) getDomNodeOrDie()).getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return getName();
    }

    /**
     * Returns the publicId.
     * @return the publicId
     */
    @JsxGetter
    public String getPublicId() {
        return ((DomDocumentType) getDomNodeOrDie()).getPublicId();
    }

    /**
     * Returns the systemId.
     * @return the systemId
     */
    @JsxGetter
    public String getSystemId() {
        return ((DomDocumentType) getDomNodeOrDie()).getSystemId();
    }

    /**
     * Returns the internal subset.
     * @return the internal subset
     */
    @JsxGetter(IE)
    public String getInternalSubset() {
        final String subset = ((DomDocumentType) getDomNodeOrDie()).getInternalSubset();
        if (StringUtils.isNotEmpty(subset)) {
            return subset;
        }

        return null;
    }

    /**
     * Returns entities.
     * @return entities
     */
    @JsxGetter
    public Object getEntities() {
        final NamedNodeMap entities = ((DomDocumentType) getDomNodeOrDie()).getEntities();
        if (null != entities) {
            return entities;
        }

        if (getBrowserVersion().hasFeature(JS_DOCTYPE_ENTITIES_NULL)) {
            return null;
        }
        return Undefined.instance;
    }

    /**
     * Returns notations.
     * @return notations
     */
    @JsxGetter
    public Object getNotations() {
        final NamedNodeMap notations = ((DomDocumentType) getDomNodeOrDie()).getNotations();
        if (null != notations) {
            return notations;
        }

        if (getBrowserVersion().hasFeature(JS_DOCTYPE_NOTATIONS_NULL)) {
            return null;
        }
        return Undefined.instance;
    }

    /**
     * Returns the namespace prefix.
     * @return the namespace prefix
     */
    @Override
    @JsxGetter(FF45)
    public Object getPrefix() {
        return super.getPrefix();
    }

    /**
     * Returns the local name of this element.
     * @return the local name of this element
     */
    @Override
    @JsxGetter(FF45)
    public Object getLocalName() {
        return super.getLocalName();
    }

    /**
     * Returns the URI that identifies an XML namespace.
     * @return the URI that identifies an XML namespace
     */
    @Override
    @JsxGetter(IE)
    public Object getNamespaceURI() {
        return super.getNamespaceURI();
    }

}
