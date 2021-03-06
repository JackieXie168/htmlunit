/* 
 * Copyright Marc Guillemot
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.htmlunit.cyberneko.xercesbridge;

import org.apache.xerces.xni.NamespaceContext;

/**
 * Xerces bridge for use with Xerces 2.3 and higher
 * @author Marc Guillemot
 */
public class XercesBridge_2_3 extends XercesBridge_2_2
{
    /**
     * Should fail for Xerces version less than 2.3 
     * @throws InstantiationException if instantiation failed 
     */
    public XercesBridge_2_3() throws InstantiationException {
        try {
            NamespaceContext.class.getMethod("declarePrefix", new Class[] {String.class, String.class});
        }
        catch (final NoSuchMethodException e) {
            // means that we're not using Xerces 2.3 or higher
            throw new InstantiationException(e.getMessage());
        }
    }

    @Override
    public void NamespaceContext_declarePrefix(final NamespaceContext namespaceContext, 
            final String ns, String avalue) {
        namespaceContext.declarePrefix(ns, avalue);
    }
}
