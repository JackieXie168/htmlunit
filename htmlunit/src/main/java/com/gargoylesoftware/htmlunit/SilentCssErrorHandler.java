/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

/**
 * Implementation of {@link ErrorHandler} which ignores all CSS problems.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @see DefaultCssErrorHandler
 */
public class SilentCssErrorHandler implements ErrorHandler, Serializable {

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final CSSParseException exception) {
        // Ignore.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatalError(final CSSParseException exception) {
        // Ignore.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(final CSSParseException exception) {
        // Ignore.
    }

}
