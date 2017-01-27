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
package com.gargoylesoftware.htmlunit.html;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "input".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HtmlFileInput extends HtmlInput {

    private static final String FILE_SEPARATOR = "\u00A7";

    private String contentType_;
    private byte[] data_;

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlFileInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        final DomAttr valueAttrib = attributes.get("value");
        if (valueAttrib != null) {
            setDefaultValue(valueAttrib.getNodeValue(), false);
        }
    }

    /**
     * Returns the in-memory data assigned to this file input element, if any.
     * @return {@code null} if {@link #setData(byte[])} hasn't be used
     */
    public final byte[] getData() {
        return data_;
    }

    /**
     * <p>Assigns in-memory data to this file input element. During submission, instead
     * of loading data from a file, the data is read from in-memory byte array.</p>
     *
     * <p>NOTE: Only use this method if you wish to upload in-memory data; if you instead
     * wish to upload the contents of an actual file, use {@link #setValueAttribute(String)},
     * passing in the path to the file.</p>
     *
     * @param data the in-memory data assigned to this file input element
     */
    public final void setData(final byte[] data) {
        data_ = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitNameValuePairs() {
        final String valueAttribute = getValueAttribute();

        if (StringUtils.isEmpty(valueAttribute)) {
            return new NameValuePair[] {new KeyDataPair(getNameAttribute(), null, null, null, null)};
        }

        final List<NameValuePair> list = new ArrayList<>();
        for (File file : splitFiles(valueAttribute)) {
            String contentType;
            if (contentType_ == null) {
                contentType = getPage().getWebClient().getBrowserVersion().getUploadMimeType(file);
                if (StringUtils.isEmpty(contentType)) {
                    contentType = "application/octet-stream";
                }
            }
            else {
                contentType = contentType_;
            }
            final String charset = getPage().getPageEncoding();
            final KeyDataPair keyDataPair = new KeyDataPair(getNameAttribute(), file, null, contentType, charset);
            keyDataPair.setData(data_);
            list.add(keyDataPair);
        }
        return list.toArray(new NameValuePair[list.size()]);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param valueAttribute the string to split
     * @return the list of files
     */
    public static List<File> splitFiles(final String valueAttribute) {
        final List<File> files = new LinkedList<>();
        for (String value : valueAttribute.split(FILE_SEPARATOR)) {
            File file = null;
            // to tolerate file://
            if (value.startsWith("file:/")) {
                if (value.startsWith("file://") && !value.startsWith("file:///")) {
                    value = "file:///" + value.substring(7);
                }
                try {
                    file = new File(new URI(value));
                }
                catch (final URISyntaxException e) {
                    // nothing here
                }
            }

            if (file == null) {
                file = new File(value);
            }

            files.add(file);
        }
        return files;
    }

    /**
     * Sets the content type value that should be sent together with the uploaded file.
     * If content type is not explicitly set, HtmlUnit will try to guess it from the file content.
     * @param contentType the content type ({@code null} resets it)
     */
    public void setContentType(final String contentType) {
        contentType_ = contentType;
    }

    /**
     * Gets the content type that should be sent together with the uploaded file.
     * @return the content type, or {@code null} if this has not been explicitly set
     * and should be guessed from file content
     */
    public String getContentType() {
        return contentType_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asText() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueAttribute(final String newValue) {
        super.setValueAttribute(newValue);
        fireEvent("change");
    }

    /**
     * Used to specify <code>multiple</code> paths to upload.
     *
     * The current implementation splits the value based on '§'.
     * We may follow WebDriver solution, once made,
     * see https://code.google.com/p/selenium/issues/detail?id=2239
     * @param paths the list of paths of the files to upload
     */
    public void setValueAttribute(final String[] paths) {
        if (getAttribute("multiple") == ATTRIBUTE_NOT_DEFINED) {
            throw new IllegalStateException("HtmlFileInput is not 'multiple'.");
        }
        final StringBuilder builder = new StringBuilder();
        for (final String p : paths) {
            if (builder.length() != 0) {
                builder.append(FILE_SEPARATOR);
            }
            builder.append(p);
        }
        setDefaultValue(builder.toString());
        setValueAttribute(builder.toString());
    }

}
