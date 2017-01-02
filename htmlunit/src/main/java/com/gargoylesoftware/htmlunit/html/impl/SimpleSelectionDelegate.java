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
package com.gargoylesoftware.htmlunit.html.impl;

/**
 * Contains selection-related functionality without an associated node.
 *
 * @author Ahmed Ashour
 */
public class SimpleSelectionDelegate implements SelectionDelegate {

    private int selectionStart_;
    private int selectionEnd_;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSelectionStart() {
        return selectionStart_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectionStart(final int selectionStart) {
        selectionStart_ = selectionStart;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSelectionEnd() {
        return selectionEnd_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectionEnd(final int selectionEnd) {
        selectionEnd_ = selectionEnd;
    }

}
