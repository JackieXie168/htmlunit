/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.io.PrintWriter;
import org.w3c.dom.CDATASection;

import com.gargoylesoftware.htmlunit.Page;

/**
 * Representation of a CDATA node in the Html DOM.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author David K. Taylor
 */
public class DomCData extends DomText implements CDATASection {

    private static final long serialVersionUID = 4941214369614888520L;

    /** The symbolic node name. */
    public static final String NODE_NAME = "#cdata-section";

    /**
     * Creates an instance of DomText.
     *
     * @param page the Page that contains this element
     * @param data the string data held by this node
     */
    public DomCData(final Page page, final String data) {
        super(page, data);
    }

    /**
     * @return the node type constant, in this case {@link org.w3c.dom.Node#CDATA_SECTION_NODE}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.CDATA_SECTION_NODE;
    }

    /**
     * @return the node name, in this case {@link #NODE_NAME}
     */
    @Override
    public String getNodeName() {
        return NODE_NAME;
    }

    /**
     * Recursively writes the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    @Override
    protected void printXml(final String indent, final PrintWriter printWriter) {
        printWriter.print("<![CDATA[");
        printWriter.print(getData());
        printWriter.print("]]>");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DomText createSplitTextNode(final int offset) {
        return new DomCData(getPage(), getData().substring(offset));
    }
}
