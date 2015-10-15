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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Undefined;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * Superclass for all row-containing JavaScript host classes, including tables,
 * table headers, table bodies and table footers.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class RowContainer extends HTMLElement {

    private static final long serialVersionUID = 3258129146093056308L;
    private HTMLCollection rows_; // has to be a member to have equality (==) working

    /**
     * Create an instance.
     */
    public RowContainer() {
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the Rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Returns the rows in the element.
     * @return the rows in the element
     */
    public Object jsxGet_rows() {
        if (rows_ == null) {
            rows_ = new HTMLCollection(this);
            rows_.init(getDomNodeOrDie(), getXPathRows());
        }
        return rows_;
    }

    /**
     * Returns the XPath expression, relative to this node, enabling the retrieval of this container's rows.
     * @return the XPath expression, relative to this node, enabling the retrieval of this container's rows
     */
    protected String getXPathRows() {
        return "./tr";
    }

    /**
     * Deletes the row at the specified index.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536408.aspx">MSDN Documentation</a>
     * @param rowIndex the zero-based index of the row to delete
     */
    public void jsxFunction_deleteRow(int rowIndex) {
        final HTMLCollection rows = (HTMLCollection) jsxGet_rows();
        final int rowCount = rows.jsxGet_length();
        if (rowIndex == -1) {
            rowIndex = rowCount - 1;
        }
        final boolean rowIndexValid = (rowIndex >= 0 && rowIndex < rowCount);
        if (rowIndexValid) {
            final SimpleScriptable row = (SimpleScriptable) rows.jsxFunction_item(new Integer(rowIndex));
            row.getDomNodeOrDie().remove();
        }
    }

    /**
     * Inserts a new row at the specified index in the element's row collection. If the index
     * is -1 or there is no index specified, then the row is appended at the end of the
     * element's row collection.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536457.aspx">MSDN Documentation</a>
     * @param index specifies where to insert the row in the rows collection.
     *        The default value is -1, which appends the new row to the end of the rows collection
     * @return the newly-created row
     */
    public Object jsxFunction_insertRow(final Object index) {
        int rowIndex = -1;
        if (index != Undefined.instance) {
            rowIndex = (int) Context.toNumber(index);
        }
        final HTMLCollection rows = (HTMLCollection) jsxGet_rows();
        final int rowCount = rows.jsxGet_length();
        final int r;
        if (rowIndex == -1 || rowIndex == rowCount) {
            r = Math.max(0, rowCount - 1);
        }
        else {
            r = rowIndex;
        }

        if (r < 0 || r > rowCount) {
            throw Context.reportRuntimeError("Index or size is negative or greater than the allowed amount "
                    + "(index: " + rowIndex + ", " + rowCount + " rows)");
        }

        return insertRow(r);
    }

    /**
     * Inserts a new row at the given position.
     * @param index the index where the row should be inserted (0 <= index < nbRows)
     * @return the inserted row
     */
    protected Object insertRow(final int index) {
        final HTMLCollection rows = (HTMLCollection) jsxGet_rows();
        final int rowCount = rows.jsxGet_length();
        final HtmlElement newRow = ((HtmlPage) getDomNodeOrDie().getPage()).createElement("tr");
        if (rowCount == 0) {
            getDomNodeOrDie().appendChild(newRow);
        }
        else {
            final SimpleScriptable row = (SimpleScriptable) rows.jsxFunction_item(new Integer(index));
            // if at the end, then in the same "sub-container" as the last existing row
            if (index >= rowCount - 1) {
                row.getDomNodeOrDie().getParentNode().appendChild(newRow);
            }
            else {
                row.getDomNodeOrDie().insertBefore(newRow);
            }
        }
        return getScriptableFor(newRow);
    }

    /**
     * Moves the row at the specified source index to the specified target index, returning
     * the row that was moved.
     * @param sourceIndex the index of the row to move
     * @param targetIndex the index to move the row to
     * @return the row that was moved
     */
    public Object jsxFunction_moveRow(final int sourceIndex, final int targetIndex) {
        final HTMLCollection rows = (HTMLCollection) jsxGet_rows();
        final int rowCount = rows.jsxGet_length();
        final boolean sourceIndexValid = (sourceIndex >= 0 && sourceIndex < rowCount);
        final boolean targetIndexValid = (targetIndex >= 0 && targetIndex < rowCount);
        if (sourceIndexValid && targetIndexValid) {
            final SimpleScriptable sourceRow = (SimpleScriptable) rows.jsxFunction_item(new Integer(sourceIndex));
            final SimpleScriptable targetRow = (SimpleScriptable) rows.jsxFunction_item(new Integer(targetIndex));
            targetRow.getDomNodeOrDie().insertBefore(sourceRow.getDomNodeOrDie());
            return sourceRow;
        }
        return null;
    }
}