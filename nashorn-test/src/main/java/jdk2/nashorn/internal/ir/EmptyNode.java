/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jdk2.nashorn.internal.ir;

import jdk2.nashorn.internal.ir.annotations.Immutable;
import jdk2.nashorn.internal.ir.visitor.NodeVisitor;

/**
 * IR representation for an empty statement.
 */
@Immutable
public final class EmptyNode extends Statement {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     *
     * @param node node to wrap
     */
    public EmptyNode(final Statement node) {
        super(node);
    }

    /**
     * Constructor
     *
     * @param lineNumber line number
     * @param token      token
     * @param finish     finish
     */
    public EmptyNode(final int lineNumber, final long token, final int finish) {
        super(lineNumber, token, finish);
    }


    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterEmptyNode(this)) {
            return visitor.leaveEmptyNode(this);
        }
        return this;
    }

    @Override
    public void toString(final StringBuilder sb, final boolean printTypes) {
        sb.append(';');
    }

}
