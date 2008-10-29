/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the  "License");
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
/*
 * $Id$
 */

package org.apache.xml.serializer.utils;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * An instance of this class is a ListResourceBundle that
 * has the required getContents() method that returns
 * an array of message-key/message associations.
 * <p>
 * The message keys are defined in {@link MsgKey}. The
 * messages that those keys map to are defined here.
 * <p>
 * The messages in the English version are intended to be
 * translated.
 *
 * This class is not a public API, it is only public because it is
 * used in org.apache.xml.serializer.
 *
 * @xsl.usage internal
 */
public class SerializerMessages_ru extends ListResourceBundle {

    /*
     * This file contains error and warning messages related to
     * Serializer Error Handling.
     *
     *  General notes to translators:

     *  1) A stylesheet is a description of how to transform an input XML document
     *     into a resultant XML document (or HTML document or text).  The
     *     stylesheet itself is described in the form of an XML document.

     *
     *  2) An element is a mark-up tag in an XML document; an attribute is a
     *     modifier on the tag.  For example, in <elem attr='val' attr2='val2'>
     *     "elem" is an element name, "attr" and "attr2" are attribute names with
     *     the values "val" and "val2", respectively.
     *
     *  3) A namespace declaration is a special attribute that is used to associate
     *     a prefix with a URI (the namespace).  The meanings of element names and
     *     attribute names that use that prefix are defined with respect to that
     *     namespace.
     *
     *
     */

    /** The lookup table for error messages.   */
    public Object[][] getContents() {
        Object[][] contents = new Object[][] {
            {   MsgKey.BAD_MSGKEY,
                "\u041a\u043b\u044e\u0447 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f ''{0}'' \u043d\u0435 \u043e\u0442\u043d\u043e\u0441\u0438\u0442\u0441\u044f \u043a \u043a\u043b\u0430\u0441\u0441\u0443 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f ''{1}''" },

            {   MsgKey.BAD_MSGFORMAT,
                "\u0424\u043e\u0440\u043c\u0430\u0442 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f ''{0}'' \u0432 \u043a\u043b\u0430\u0441\u0441\u0435 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f ''{1}'' \u0432\u044b\u0437\u0432\u0430\u043b \u043e\u0448\u0438\u0431\u043a\u0443. " },

            {   MsgKey.ER_SERIALIZER_NOT_CONTENTHANDLER,
                "\u041a\u043b\u0430\u0441\u0441 \u0441\u0435\u0440\u0438\u0430\u043b\u0438\u0437\u0430\u0442\u043e\u0440\u0430 ''{0}'' \u043d\u0435 \u043f\u0440\u0438\u043c\u0435\u043d\u044f\u0435\u0442 org.xml.sax.ContentHandler. " },

            {   MsgKey.ER_RESOURCE_COULD_NOT_FIND,
                    "\u0420\u0435\u0441\u0443\u0440\u0441 [ {0} ] \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d.\n {1}" },

            {   MsgKey.ER_RESOURCE_COULD_NOT_LOAD,
                    "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u0440\u0435\u0441\u0443\u0440\u0441 [ {0} ]: {1} \n {2} \t {3}" },

            {   MsgKey.ER_BUFFER_SIZE_LESSTHAN_ZERO,
                    "\u0420\u0430\u0437\u043c\u0435\u0440 \u0431\u0443\u0444\u0435\u0440\u0430 <=0" },

            {   MsgKey.ER_INVALID_UTF16_SURROGATE,
                    "\u041e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d\u043e \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u043e\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0435 UTF-16: {0} ?" },

            {   MsgKey.ER_OIERROR,
                "\u041e\u0448\u0438\u0431\u043a\u0430 \u0432\u0432\u043e\u0434\u0430-\u0432\u044b\u0432\u043e\u0434\u0430" },

            {   MsgKey.ER_ILLEGAL_ATTRIBUTE_POSITION,
                "\u0410\u0442\u0440\u0438\u0431\u0443\u0442 {0} \u043d\u0435\u043b\u044c\u0437\u044f \u0434\u043e\u0431\u0430\u0432\u043b\u044f\u0442\u044c \u043f\u043e\u0441\u043b\u0435 \u0434\u043e\u0447\u0435\u0440\u043d\u0438\u0445 \u0443\u0437\u043b\u043e\u0432 \u0438 \u0434\u043e \u0441\u043e\u0437\u0434\u0430\u043d\u0438\u044f \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u0430. \u0410\u0442\u0440\u0438\u0431\u0443\u0442 \u0431\u0443\u0434\u0435\u0442 \u043f\u0440\u043e\u0438\u0433\u043d\u043e\u0440\u0438\u0440\u043e\u0432\u0430\u043d. " },

            /*
             * Note to translators:  The stylesheet contained a reference to a
             * namespace prefix that was undefined.  The value of the substitution
             * text is the name of the prefix.
             */
            {   MsgKey.ER_NAMESPACE_PREFIX,
                "\u041f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0441\u0442\u0432\u043e \u0438\u043c\u0435\u043d \u0434\u043b\u044f \u043f\u0440\u0435\u0444\u0438\u043a\u0441\u0430 ''{0}'' \u043d\u0435 \u043e\u0431\u044a\u044f\u0432\u043b\u0435\u043d\u043e. " },

            /*
             * Note to translators:  This message is reported if the stylesheet
             * being processed attempted to construct an XML document with an
             * attribute in a place other than on an element.  The substitution text
             * specifies the name of the attribute.
             */
            {   MsgKey.ER_STRAY_ATTRIBUTE,
                "\u0410\u0442\u0440\u0438\u0431\u0443\u0442 ''{0}'' \u0432\u043d\u0435 \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u0430. " },

            /*
             * Note to translators:  As with the preceding message, a namespace
             * declaration has the form of an attribute and is only permitted to
             * appear on an element.  The substitution text {0} is the namespace
             * prefix and {1} is the URI that was being used in the erroneous
             * namespace declaration.
             */
            {   MsgKey.ER_STRAY_NAMESPACE,
                "\u041e\u0431\u044a\u044f\u0432\u043b\u0435\u043d\u0438\u0435 \u043f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0441\u0442\u0432\u0430 \u0438\u043c\u0435\u043d ''{0}''=''{1}'' \u0432\u043d\u0435 \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u0430. " },

            {   MsgKey.ER_COULD_NOT_LOAD_RESOURCE,
                "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c ''{0}'' (\u043f\u0440\u043e\u0432\u0435\u0440\u044c\u0442\u0435 CLASSPATH), \u043f\u0440\u0438\u043c\u0435\u043d\u044f\u044e\u0442\u0441\u044f \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u044f \u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e" },

            {   MsgKey.ER_ILLEGAL_CHARACTER,
                "\u041f\u043e\u043f\u044b\u0442\u043a\u0430 \u0432\u044b\u0432\u043e\u0434\u0430 \u0441\u0438\u043c\u0432\u043e\u043b\u0430, \u0438\u043d\u0442\u0435\u0433\u0440\u0430\u043b\u044c\u043d\u043e\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0435 {0} \u043a\u043e\u0442\u043e\u0440\u043e\u0433\u043e \u043d\u0435 \u043f\u0440\u0435\u0434\u0441\u0442\u0430\u0432\u043b\u0435\u043d\u043e \u0432 \u0443\u043a\u0430\u0437\u0430\u043d\u043d\u043e\u0439 \u043a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0435 \u0432\u044b\u0432\u043e\u0434\u0430 {1}. " },

            {   MsgKey.ER_COULD_NOT_LOAD_METHOD_PROPERTY,
                "\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u0444\u0430\u0439\u043b \u0441\u0432\u043e\u0439\u0441\u0442\u0432 ''{0}'' \u0434\u043b\u044f \u043c\u0435\u0442\u043e\u0434\u0430 \u0432\u044b\u0432\u043e\u0434\u0430 ''{1}'' (\u043f\u0440\u043e\u0432\u0435\u0440\u044c\u0442\u0435 CLASSPATH)" },

            {   MsgKey.ER_INVALID_PORT,
                "\u041d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0439 \u043d\u043e\u043c\u0435\u0440 \u043f\u043e\u0440\u0442\u0430" },

            {   MsgKey.ER_PORT_WHEN_HOST_NULL,
                "\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0437\u0430\u0434\u0430\u0442\u044c \u043f\u043e\u0440\u0442 \u0434\u043b\u044f \u043f\u0443\u0441\u0442\u043e\u0433\u043e \u0430\u0434\u0440\u0435\u0441\u0430 \u0445\u043e\u0441\u0442\u0430" },

            {   MsgKey.ER_HOST_ADDRESS_NOT_WELLFORMED,
                "\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e \u0441\u0444\u043e\u0440\u043c\u0438\u0440\u043e\u0432\u0430\u043d \u0430\u0434\u0440\u0435\u0441 \u0445\u043e\u0441\u0442\u0430" },

            {   MsgKey.ER_SCHEME_NOT_CONFORMANT,
                "\u0421\u0445\u0435\u043c\u0430 \u043d\u0435 \u043a\u043e\u043d\u0444\u043e\u0440\u043c\u0430\u0442\u0438\u0432\u043d\u0430." },

            {   MsgKey.ER_SCHEME_FROM_NULL_STRING,
                "\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0437\u0430\u0434\u0430\u0442\u044c \u0441\u0445\u0435\u043c\u0443 \u0434\u043b\u044f \u043f\u0443\u0441\u0442\u043e\u0439 \u0441\u0442\u0440\u043e\u043a\u0438" },

            {   MsgKey.ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
                "\u0412 \u0438\u043c\u0435\u043d\u0438 \u043f\u0443\u0442\u0438 \u0432\u0441\u0442\u0440\u0435\u0447\u0430\u0435\u0442\u0441\u044f \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u0430\u044f Esc-\u043f\u043e\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c" },

            {   MsgKey.ER_PATH_INVALID_CHAR,
                "\u0412 \u0438\u043c\u0435\u043d\u0438 \u043f\u0443\u0442\u0438 \u043e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0439 \u0441\u0438\u043c\u0432\u043e\u043b: {0}" },

            {   MsgKey.ER_FRAG_INVALID_CHAR,
                "\u0424\u0440\u0430\u0433\u043c\u0435\u043d\u0442 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u0442 \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0439 \u0441\u0438\u043c\u0432\u043e\u043b" },

            {   MsgKey.ER_FRAG_WHEN_PATH_NULL,
                "\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0437\u0430\u0434\u0430\u0442\u044c \u0444\u0440\u0430\u0433\u043c\u0435\u043d\u0442 \u0434\u043b\u044f \u043f\u0443\u0441\u0442\u043e\u0433\u043e \u043f\u0443\u0442\u0438" },

            {   MsgKey.ER_FRAG_FOR_GENERIC_URI,
                "\u0424\u0440\u0430\u0433\u043c\u0435\u043d\u0442 \u043c\u043e\u0436\u043d\u043e \u0437\u0430\u0434\u0430\u0442\u044c \u0442\u043e\u043b\u044c\u043a\u043e \u0434\u043b\u044f \u0448\u0430\u0431\u043b\u043e\u043d\u0430 URI" },

            {   MsgKey.ER_NO_SCHEME_IN_URI,
                "\u0412 URI \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430 \u0441\u0445\u0435\u043c\u0430" },

            {   MsgKey.ER_CANNOT_INIT_URI_EMPTY_PARMS,
                "\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0438\u043d\u0438\u0446\u0438\u0430\u043b\u0438\u0437\u0438\u0440\u043e\u0432\u0430\u0442\u044c URI \u0441 \u043f\u0443\u0441\u0442\u044b\u043c\u0438 \u043f\u0430\u0440\u0430\u043c\u0435\u0442\u0440\u0430\u043c\u0438" },

            {   MsgKey.ER_NO_FRAGMENT_STRING_IN_PATH,
                "\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0437\u0430\u0434\u0430\u0442\u044c \u0444\u0440\u0430\u0433\u043c\u0435\u043d\u0442 \u043e\u0434\u043d\u043e\u0432\u0440\u0435\u043c\u0435\u043d\u043d\u043e \u0434\u043b\u044f \u043f\u0443\u0442\u0438 \u0438 \u0444\u0440\u0430\u0433\u043c\u0435\u043d\u0442\u0430" },

            {   MsgKey.ER_NO_QUERY_STRING_IN_PATH,
                "\u041d\u0435\u043b\u044c\u0437\u044f \u0443\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c \u0441\u0442\u0440\u043e\u043a\u0443 \u0437\u0430\u043f\u0440\u043e\u0441\u0430 \u0432 \u0441\u0442\u0440\u043e\u043a\u0435 \u043f\u0443\u0442\u0438 \u0438 \u0437\u0430\u043f\u0440\u043e\u0441\u0430" },

            {   MsgKey.ER_NO_PORT_IF_NO_HOST,
                "\u041d\u0435\u043b\u044c\u0437\u044f \u0443\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c \u043f\u043e\u0440\u0442, \u0435\u0441\u043b\u0438 \u043d\u0435 \u0437\u0430\u0434\u0430\u043d \u0445\u043e\u0441\u0442" },

            {   MsgKey.ER_NO_USERINFO_IF_NO_HOST,
                "\u041d\u0435\u043b\u044c\u0437\u044f \u0443\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043e \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u0435, \u0435\u0441\u043b\u0438 \u043d\u0435 \u0437\u0430\u0434\u0430\u043d \u0445\u043e\u0441\u0442" },
            {   MsgKey.ER_XML_VERSION_NOT_SUPPORTED,
                "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435: \u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u0430 \u0432\u0435\u0440\u0441\u0438\u044f \u0434\u043e\u043a\u0443\u043c\u0435\u043d\u0442\u0430 \u0432\u044b\u0432\u043e\u0434\u0430 ''{0}''. \u042d\u0442\u0430 \u0432\u0435\u0440\u0441\u0438\u044f XML \u043d\u0435 \u043f\u043e\u0434\u0434\u0435\u0440\u0436\u0438\u0432\u0430\u0435\u0442\u0441\u044f. \u0412\u0435\u0440\u0441\u0438\u0435\u0439 \u0434\u043e\u043a\u0443\u043c\u0435\u043d\u0442\u0430 \u0432\u044b\u0432\u043e\u0434\u0430 \u0431\u0443\u0434\u0435\u0442 ''1.0''. " },

            {   MsgKey.ER_SCHEME_REQUIRED,
                "\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u0430 \u0441\u0445\u0435\u043c\u0430!" },

            /*
             * Note to translators:  The words 'Properties' and
             * 'SerializerFactory' in this message are Java class names
             * and should not be translated.
             */
            {   MsgKey.ER_FACTORY_PROPERTY_MISSING,
                "\u041e\u0431\u044a\u0435\u043a\u0442 \u0441\u0432\u043e\u0439\u0441\u0442\u0432, \u043f\u0435\u0440\u0435\u0434\u0430\u043d\u043d\u044b\u0439 \u0432 SerializerFactory, \u043d\u0435 \u043e\u0431\u043b\u0430\u0434\u0430\u0435\u0442 \u0441\u0432\u043e\u0439\u0441\u0442\u0432\u043e\u043c ''{0}''. " },

            {   MsgKey.ER_ENCODING_NOT_SUPPORTED,
                "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435:  \u041a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430 ''{0}'' \u043d\u0435 \u043f\u043e\u0434\u0434\u0435\u0440\u0436\u0438\u0432\u0430\u0435\u0442\u0441\u044f \u0441\u0440\u0435\u0434\u043e\u0439 \u0432\u044b\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u044f Java." },

             {MsgKey.ER_FEATURE_NOT_FOUND,
             "\u041f\u0430\u0440\u0430\u043c\u0435\u0442\u0440 ''{0}'' \u043d\u0435 \u0440\u0430\u0441\u043f\u043e\u0437\u043d\u0430\u043d. "},

             {MsgKey.ER_FEATURE_NOT_SUPPORTED,
             "\u041f\u0430\u0440\u0430\u043c\u0435\u0442\u0440 ''{0}'' \u0440\u0430\u0441\u043f\u043e\u0437\u043d\u0430\u043d, \u043d\u043e \u0437\u0430\u043f\u0440\u043e\u0448\u0435\u043d\u043d\u043e\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0435 \u0437\u0430\u0434\u0430\u0442\u044c \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c. "},

             {MsgKey.ER_STRING_TOO_LONG,
             "\u0421\u0442\u0440\u043e\u043a\u0430 \u0440\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442\u0430 \u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0434\u043b\u0438\u043d\u043d\u0430\u044f \u0434\u043b\u044f \u0440\u0430\u0437\u043c\u0435\u0449\u0435\u043d\u0438\u044f \u0432 DOMString: ''{0}''. "},

             {MsgKey.ER_TYPE_MISMATCH_ERR,
             "\u0422\u0438\u043f \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u044f \u0434\u043b\u044f \u043f\u0430\u0440\u0430\u043c\u0435\u0442\u0440\u0430 \u0441 \u044d\u0442\u0438 \u0438\u043c\u0435\u043d\u0435\u043c \u043d\u0435\u0441\u043e\u0432\u043c\u0435\u0441\u0442\u0438\u043c \u0441 \u043e\u0436\u0438\u0434\u0430\u0435\u043c\u044b\u043c \u0442\u0438\u043f\u043e\u043c \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u044f. "},

             {MsgKey.ER_NO_OUTPUT_SPECIFIED,
             "\u041d\u0435 \u0443\u043a\u0430\u0437\u0430\u043d \u0446\u0435\u043b\u0435\u0432\u043e\u0439 \u043a\u0430\u0442\u0430\u043b\u043e\u0433 \u0434\u043b\u044f \u0432\u044b\u0432\u043e\u0434\u0430 \u0434\u0430\u043d\u043d\u044b\u0445. "},

             {MsgKey.ER_UNSUPPORTED_ENCODING,
             "\u041e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d\u0430 \u043d\u0435\u043f\u043e\u0434\u0434\u0435\u0440\u0436\u0438\u0432\u0430\u0435\u043c\u0430\u044f \u043a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430. "},

             {MsgKey.ER_UNABLE_TO_SERIALIZE_NODE,
             "\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0441\u0435\u0440\u0438\u0430\u043b\u0438\u0437\u043e\u0432\u0430\u0442\u044c \u0443\u0437\u0435\u043b. "},

             {MsgKey.ER_CDATA_SECTIONS_SPLIT,
             "\u0420\u0430\u0437\u0434\u0435\u043b CDATA \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u0442 \u043e\u0434\u0438\u043d \u0438\u043b\u0438 \u043d\u0435\u0441\u043a\u043e\u043b\u044c\u043a\u043e \u043c\u0430\u0440\u043a\u0435\u0440\u043e\u0432 \u0440\u0430\u0437\u0434\u0435\u043b\u0438\u0442\u0435\u043b\u0435\u0439 ']]>'. "},

             {MsgKey.ER_WARNING_WF_NOT_CHECKED,
                 "\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0441\u043e\u0437\u0434\u0430\u0442\u044c \u044d\u043a\u0437\u0435\u043c\u043f\u043b\u044f\u0440 \u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0438 \u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u043e\u0441\u0442\u0438. \u0414\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0439 \u043f\u0430\u0440\u0430\u043c\u0435\u0442\u0440 \u0438\u043c\u0435\u0435\u0442 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0435 true, \u043d\u043e \u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0443 \u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u043e\u0441\u0442\u0438 \u0432\u044b\u043f\u043e\u043b\u043d\u0438\u0442\u044c \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c. "
             },

             {MsgKey.ER_WF_INVALID_CHARACTER,
                 "\u0423\u0437\u0435\u043b ''{0}'' \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u0442 \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0435 \u0441\u0438\u043c\u0432\u043e\u043b\u044b XML. "
             },

             { MsgKey.ER_WF_INVALID_CHARACTER_IN_COMMENT,
                 "\u0412 \u043a\u043e\u043c\u043c\u0435\u043d\u0442\u0430\u0440\u0438\u0438 \u043e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0439 \u0441\u0438\u043c\u0432\u043e\u043b XML (\u042e\u043d\u0438\u043a\u043e\u0434: 0x{0})."
             },

             { MsgKey.ER_WF_INVALID_CHARACTER_IN_PI,
                 "\u041f\u0440\u0438 \u043e\u0431\u0440\u0430\u0431\u043e\u0442\u043a\u0435 instructiondata \u0431\u044b\u043b \u043e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0439 \u0441\u0438\u043c\u0432\u043e\u043b XML (\u042e\u043d\u0438\u043a\u043e\u0434: 0x{0}). "
             },

             { MsgKey.ER_WF_INVALID_CHARACTER_IN_CDATA,
                 "\u0412 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u043c CDATASection \u043e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0439 \u0441\u0438\u043c\u0432\u043e\u043b XML (\u042e\u043d\u0438\u043a\u043e\u0434: 0x{0})."
             },

             { MsgKey.ER_WF_INVALID_CHARACTER_IN_TEXT,
                 "\u0412 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u043c \u0441\u0438\u043c\u0432\u043e\u043b\u044c\u043d\u044b\u0445 \u0434\u0430\u043d\u043d\u044b\u0445 \u0443\u0437\u043b\u0430 \u043e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0439 \u0441\u0438\u043c\u0432\u043e\u043b XML (\u042e\u043d\u0438\u043a\u043e\u0434: 0x{0})."
             },

             { MsgKey.ER_WF_INVALID_CHARACTER_IN_NODE_NAME,
                 "\u041d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0435 \u0441\u0438\u043c\u0432\u043e\u043b\u044b XML \u043e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d\u044b \u0432 \u0443\u0437\u043b\u0435 {0} \u0441 \u0438\u043c\u0435\u043d\u0435\u043c ''{1}''. "
             },

             { MsgKey.ER_WF_DASH_IN_COMMENT,
                 "\u0421\u0442\u0440\u043e\u043a\u0430 \"--\" \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u0430 \u0432 \u043a\u043e\u043c\u043c\u0435\u043d\u0442\u0430\u0440\u0438\u0438. "
             },

             {MsgKey.ER_WF_LT_IN_ATTVAL,
                 "\u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435 \u0430\u0442\u0440\u0438\u0431\u0443\u0442\u0430 \"{1}\", \u0441\u0432\u044f\u0437\u0430\u043d\u043d\u043e\u0433\u043e \u0441 \u0442\u0438\u043f\u043e\u043c \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u0430 \"{0}\", \u043d\u0435 \u0434\u043e\u043b\u0436\u043d\u043e \u0441\u043e\u0434\u0435\u0440\u0436\u0430\u0442\u044c \u0441\u0438\u043c\u0432\u043e\u043b ''<''. "
             },

             {MsgKey.ER_WF_REF_TO_UNPARSED_ENT,
                 "\u041d\u0435\u043e\u0431\u0440\u0430\u0431\u043e\u0442\u0430\u043d\u043d\u0430\u044f \u0441\u0441\u044b\u043b\u043a\u0430 \u043d\u0430 \u044d\u043b\u0435\u043c\u0435\u043d\u0442 \"&{0};\" \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u0430. "
             },

             {MsgKey.ER_WF_REF_TO_EXTERNAL_ENT,
                 "\u0412\u043d\u0435\u0448\u043d\u044f\u044f \u0441\u0441\u044b\u043b\u043a\u0430 \u043d\u0430 \u044d\u043b\u0435\u043c\u0435\u043d\u0442 \"&{0};\" \u043d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u0430 \u0432 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0438 \u0430\u0442\u0440\u0438\u0431\u0443\u0442\u0430. "
             },

             {MsgKey.ER_NS_PREFIX_CANNOT_BE_BOUND,
                 "\u041f\u0440\u0435\u0444\u0438\u043a\u0441 \"{0}\" \u043d\u0435 \u043c\u043e\u0436\u0435\u0442 \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u044c\u0441\u044f \u0432 \u043f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0441\u0442\u0432\u0435  \u0438\u043c\u0435\u043d \"{1}\". "
             },

             {MsgKey.ER_NULL_LOCAL_ELEMENT_NAME,
                 "\u041b\u043e\u043a\u0430\u043b\u044c\u043d\u043e\u0435 \u0438\u043c\u044f \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u0430 \"{0}\" \u043f\u0443\u0441\u0442\u043e. "
             },

             {MsgKey.ER_NULL_LOCAL_ATTR_NAME,
                 "\u041b\u043e\u043a\u0430\u043b\u044c\u043d\u043e\u0435 \u0438\u043c\u044f \u0430\u0442\u0440\u0438\u0431\u0443\u0442\u0430 \"{0}\" \u043f\u0443\u0441\u0442\u043e.  "
             },

             { MsgKey.ER_ELEM_UNBOUND_PREFIX_IN_ENTREF,
                 "\u0422\u0435\u043a\u0441\u0442 \u0437\u0430\u043c\u0435\u043d\u044b \u0434\u043b\u044f \u0443\u0437\u043b\u0430 \u0437\u0430\u043f\u0438\u0441\u0438 \"{0}\" \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u0442 \u0443\u0437\u0435\u043b \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u043e\u0432 \"{1}\" \u0441 \u043d\u0435\u0441\u0432\u044f\u0437\u0430\u043d\u043d\u044b\u043c \u043f\u0440\u0435\u0444\u0438\u043a\u0441\u043e\u043c \"{2}\". "
             },

             { MsgKey.ER_ATTR_UNBOUND_PREFIX_IN_ENTREF,
                 "\u0422\u0435\u043a\u0441\u0442 \u0437\u0430\u043c\u0435\u043d\u044b \u0434\u043b\u044f \u0443\u0437\u043b\u0430 \u0437\u0430\u043f\u0438\u0441\u0438 \"{0}\" \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u0442 \u0443\u0437\u0435\u043b \u0430\u0442\u0440\u0438\u0431\u0443\u0442\u043e\u0432 \"{1}\" \u0441 \u043d\u0435\u0441\u0432\u044f\u0437\u0430\u043d\u043d\u044b\u043c \u043f\u0440\u0435\u0444\u0438\u043a\u0441\u043e\u043c \"{2}\". "
             },

        };

        return contents;
    }
}
