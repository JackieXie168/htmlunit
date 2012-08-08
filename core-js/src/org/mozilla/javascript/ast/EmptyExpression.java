/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.ast;

import org.mozilla.javascript.Token;

/**
 * AST node for an empty expression.  Node type is {@link Token#EMPTY}.<p>
 *
 * To create an empty statement, wrap it with an {@link ExpressionStatement}.
 */
public class EmptyExpression extends AstNode {

    {
        type = Token.EMPTY;
    }

    public EmptyExpression() {
    }

    public EmptyExpression(int pos) {
        super(pos);
    }

    public EmptyExpression(int pos, int len) {
        super(pos, len);
    }

    @Override
    public String toSource(int depth) {
        return makeIndent(depth);
    }

    /**
     * Visits this node.  There are no children.
     */
    @Override
    public void visit(NodeVisitor v) {
        v.visit(this);
    }
}
