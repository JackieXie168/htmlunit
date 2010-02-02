/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1997-1999
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Steve Yegge
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU General Public License Version 2 or later (the "GPL"), in which
 * case the provisions of the GPL are applicable instead of those above. If
 * you wish to allow use of your version of this file only under the terms of
 * the GPL and not to allow others to use your version of this file under the
 * MPL, indicate your decision by deleting the provisions above and replacing
 * them with the notice and other provisions required by the GPL. If you do
 * not delete the provisions above, a recipient may use your version of this
 * file under either the MPL or the GPL.
 *
 * ***** END LICENSE BLOCK ***** */

package org.mozilla.javascript.ast;

import org.mozilla.javascript.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * AST node for a JavaScript 1.7 Array comprehension.
 * Node type is {@link Token#ARRAYCOMP}.<p>
 */
public class ArrayComprehension extends Scope {

    private AstNode result;
    private List<ArrayComprehensionLoop> loops =
        new ArrayList<ArrayComprehensionLoop>();
    private AstNode filter;
    private int ifPosition = -1;
    private int lp = -1;
    private int rp = -1;

    {
        type = Token.ARRAYCOMP;
    }

    public ArrayComprehension() {
    }

    public ArrayComprehension(int pos) {
        super(pos);
    }

    public ArrayComprehension(int pos, int len) {
        super(pos, len);
    }

    /**
     * Returns result expression node (just after opening bracket)
     */
    public AstNode getResult() {
        return result;
    }

    /**
     * Sets result expression, and sets its parent to this node.
     * @throws IllegalArgumentException if result is {@code null}
     */
    public void setResult(AstNode result) {
        assertNotNull(result);
        this.result = result;
        result.setParent(this);
    }

    /**
     * Returns loop list
     */
    public List<ArrayComprehensionLoop> getLoops() {
        return loops;
    }

    /**
     * Sets loop list
     * @throws IllegalArgumentException if loops is {@code null}
     */
    public void setLoops(List<ArrayComprehensionLoop> loops) {
        assertNotNull(loops);
        this.loops.clear();
        for (ArrayComprehensionLoop acl : loops) {
            addLoop(acl);
        }
    }

    /**
     * Adds a child loop node, and sets its parent to this node.
     * @throws IllegalArgumentException if acl is {@code null}
     */
    public void addLoop(ArrayComprehensionLoop acl) {
        assertNotNull(acl);
        loops.add(acl);
        acl.setParent(this);
    }

    /**
     * Returns filter expression, or {@code null} if not present
     */
    public AstNode getFilter() {
        return filter;
    }

    /**
     * Sets filter expression, and sets its parent to this node.
     * Can be {@code null}.
     */
    public void setFilter(AstNode filter) {
        this.filter = filter;
        if (filter != null)
            filter.setParent(this);
    }

    /**
     * Returns position of 'if' keyword, -1 if not present
     */
    public int getIfPosition() {
        return ifPosition;
    }

    /**
     * Sets position of 'if' keyword
     */
    public void setIfPosition(int ifPosition) {
        this.ifPosition = ifPosition;
    }

    /**
     * Returns filter left paren position, or -1 if no filter
     */
    public int getFilterLp() {
        return lp;
    }

    /**
     * Sets filter left paren position, or -1 if no filter
     */
    public void setFilterLp(int lp) {
        this.lp = lp;
    }

    /**
     * Returns filter right paren position, or -1 if no filter
     */
    public int getFilterRp() {
        return rp;
    }

    /**
     * Sets filter right paren position, or -1 if no filter
     */
    public void setFilterRp(int rp) {
        this.rp = rp;
    }

    @Override
    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder(250);
        sb.append("[");
        sb.append(result.toSource(0));
        for (ArrayComprehensionLoop loop : loops) {
            sb.append(loop.toSource(0));
        }
        if (filter != null) {
            sb.append(" if (");
            sb.append(filter.toSource(0));
            sb.append(")");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Visits this node, the result expression, the loops, and the optional
     * filter.
     */
    @Override
    public void visit(NodeVisitor v) {
        if (!v.visit(this)) {
            return;
        }
        result.visit(v);
        for (ArrayComprehensionLoop loop : loops) {
            loop.visit(v);
        }
        if (filter != null) {
            filter.visit(v);
        }
    }
}
