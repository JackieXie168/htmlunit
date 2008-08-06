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

import java.util.Map;

import org.xml.sax.Attributes;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Element factory which creates elements by calling the constructor on a
 * given {@link com.gargoylesoftware.htmlunit.html.HtmlElement} subclass.
 * The constructor is expected to take 2 arguments of type
 * {@link com.gargoylesoftware.htmlunit.html.HtmlPage} and {@link java.util.Map}
 * where the first one is the owning page of the element, the second one is a map
 * holding the initial attributes for the element.
 *
 * @version $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author David K. Taylor
 */
class DefaultElementFactory implements IElementFactory {

    /**
     * @param page the owning page
     * @param tagName the HTML tag name
     * @param attributes initial attributes, possibly <code>null</code>
     * @return the newly created element
     */
    public HtmlElement createElement(final SgmlPage page, final String tagName, final Attributes attributes) {
        return createElementNS(page, null, tagName, attributes);
    }

    /**
     * @param page the owning page
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param attributes initial attributes, possibly <code>null</code>
     * @return the newly created element
     */
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes) {
        final Map<String, DomAttr> attributeMap = setAttributes(page, attributes);

        final HtmlElement element;
        final String tagName;
        final int colonIndex = qualifiedName.indexOf(':');
        if (colonIndex == -1) {
            tagName = qualifiedName;
        }
        else {
            tagName = qualifiedName.substring(colonIndex + 1).toLowerCase();
        }
        if (tagName.equals(HtmlAddress.TAG_NAME)) {
            element = new HtmlAddress(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlAnchor.TAG_NAME)) {
            element = new HtmlAnchor(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlApplet.TAG_NAME)) {
            element = new HtmlApplet(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlArea.TAG_NAME)) {
            element = new HtmlArea(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBase.TAG_NAME)) {
            element = new HtmlBase(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBaseFont.TAG_NAME)) {
            element = new HtmlBaseFont(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBidirectionalOverride.TAG_NAME)) {
            element = new HtmlBidirectionalOverride(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBlockQuote.TAG_NAME)) {
            element = new HtmlBlockQuote(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBody.TAG_NAME)) {
            element = new HtmlBody(namespaceURI, qualifiedName, page, attributeMap, false);
        }
        else if (tagName.equals(HtmlBreak.TAG_NAME)) {
            element = new HtmlBreak(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlButton.TAG_NAME)) {
            element = new HtmlButton(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlButtonInput.TAG_NAME)) {
            element = new HtmlButtonInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlCaption.TAG_NAME)) {
            element = new HtmlCaption(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlCenter.TAG_NAME)) {
            element = new HtmlCenter(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlCheckBoxInput.TAG_NAME)) {
            element = new HtmlCheckBoxInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDefinitionDescription.TAG_NAME)) {
            element = new HtmlDefinitionDescription(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDefinitionList.TAG_NAME)) {
            element = new HtmlDefinitionList(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDefinitionTerm.TAG_NAME)) {
            element = new HtmlDefinitionTerm(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDeletedText.TAG_NAME)) {
            element = new HtmlDeletedText(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDivision.TAG_NAME)) {
            element = new HtmlDivision(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlFieldSet.TAG_NAME)) {
            element = new HtmlFieldSet(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlFileInput.TAG_NAME)) {
            element = new HtmlFileInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlFont.TAG_NAME)) {
            element = new HtmlFont(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlForm.TAG_NAME)) {
            element = new HtmlForm(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlFrame.TAG_NAME)) {
            if (attributeMap != null) {
                final DomAttr srcAttribute = attributeMap.get("src");
                if (srcAttribute != null) {
                    srcAttribute.setValue(srcAttribute.getValue().trim());
                }
            }
            element = new HtmlFrame(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlFrameSet.TAG_NAME)) {
            element = new HtmlFrameSet(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHead.TAG_NAME)) {
            element = new HtmlHead(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading1.TAG_NAME)) {
            element = new HtmlHeading1(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading2.TAG_NAME)) {
            element = new HtmlHeading2(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading3.TAG_NAME)) {
            element = new HtmlHeading3(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading4.TAG_NAME)) {
            element = new HtmlHeading4(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading5.TAG_NAME)) {
            element = new HtmlHeading5(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading6.TAG_NAME)) {
            element = new HtmlHeading6(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHiddenInput.TAG_NAME)) {
            element = new HtmlHiddenInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHorizontalRule.TAG_NAME)) {
            element = new HtmlHorizontalRule(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHtml.TAG_NAME)) {
            element = new HtmlHtml(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlImage.TAG_NAME)) {
            element = new HtmlImage(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlImageInput.TAG_NAME)) {
            element = new HtmlImageInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlInlineFrame.TAG_NAME)) {
            if (attributeMap != null) {
                final DomAttr srcAttribute = attributeMap.get("src");
                if (srcAttribute != null) {
                    srcAttribute.setValue(srcAttribute.getValue().trim());
                }
            }
            element = new HtmlInlineFrame(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlInlineQuotation.TAG_NAME)) {
            element = new HtmlInlineQuotation(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlInsertedText.TAG_NAME)) {
            element = new HtmlInsertedText(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlIsIndex.TAG_NAME)) {
            element = new HtmlIsIndex(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlLabel.TAG_NAME)) {
            element = new HtmlLabel(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlLegend.TAG_NAME)) {
            element = new HtmlLegend(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlLink.TAG_NAME)) {
            element = new HtmlLink(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlListItem.TAG_NAME)) {
            element = new HtmlListItem(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlMap.TAG_NAME)) {
            element = new HtmlMap(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlMenu.TAG_NAME)) {
            element = new HtmlMenu(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlMeta.TAG_NAME)) {
            element = new HtmlMeta(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlNoFrames.TAG_NAME)) {
            element = new HtmlNoFrames(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlNoScript.TAG_NAME)) {
            element = new HtmlNoScript(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlObject.TAG_NAME)) {
            element = new HtmlObject(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlOption.TAG_NAME)) {
            element = new HtmlOption(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlOptionGroup.TAG_NAME)) {
            element = new HtmlOptionGroup(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlOrderedList.TAG_NAME)) {
            element = new HtmlOrderedList(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlParagraph.TAG_NAME)) {
            element = new HtmlParagraph(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlParameter.TAG_NAME)) {
            element = new HtmlParameter(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlPasswordInput.TAG_NAME)) {
            element = new HtmlPasswordInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlPreformattedText.TAG_NAME)) {
            element = new HtmlPreformattedText(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlRadioButtonInput.TAG_NAME)) {
            element = new HtmlRadioButtonInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlResetInput.TAG_NAME)) {
            element = new HtmlResetInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlScript.TAG_NAME)) {
            element = new HtmlScript(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlSelect.TAG_NAME)) {
            element = new HtmlSelect(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlSpan.TAG_NAME)) {
            element = new HtmlSpan(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlStyle.TAG_NAME)) {
            element = new HtmlStyle(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlSubmitInput.TAG_NAME)) {
            element = new HtmlSubmitInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTable.TAG_NAME)) {
            element = new HtmlTable(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableBody.TAG_NAME)) {
            element = new HtmlTableBody(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableColumn.TAG_NAME)) {
            element = new HtmlTableColumn(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableColumnGroup.TAG_NAME)) {
            element = new HtmlTableColumnGroup(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableDataCell.TAG_NAME)) {
            element = new HtmlTableDataCell(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableFooter.TAG_NAME)) {
            element = new HtmlTableFooter(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableHeader.TAG_NAME)) {
            element = new HtmlTableHeader(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableHeaderCell.TAG_NAME)) {
            element = new HtmlTableHeaderCell(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableRow.TAG_NAME)) {
            element = new HtmlTableRow(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTextArea.TAG_NAME)) {
            element = new HtmlTextArea(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDirectory.TAG_NAME)) {
            element = new HtmlDirectory(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTextInput.TAG_NAME)) {
            element = new HtmlTextInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTitle.TAG_NAME)) {
            element = new HtmlTitle(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlUnorderedList.TAG_NAME)) {
            element = new HtmlUnorderedList(namespaceURI, qualifiedName, page, attributeMap);
        }
        else {
            throw new IllegalStateException("Cannot find HtmlElement for " + qualifiedName);
        }
        return element;
    }

    /**
     * Converts {@link Attributes} into the map needed by {@link HtmlElement}s.
     *
     * @param page the page which contains the specified attributes
     * @param attributes the SAX attributes
     * @return the map of attribute values for {@link HtmlElement}s
     */
    static Map<String, DomAttr> setAttributes(final Page page, final Attributes attributes) {
        Map<String, DomAttr> attributeMap = null;
        if (attributes != null) {
            attributeMap = HtmlElement.createAttributeMap(attributes.getLength());
            for (int i = 0; i < attributes.getLength(); i++) {
                final String qName = attributes.getQName(i);
                // browsers consider only first attribute (ex: <div id='foo' id='something'>...</div>)
                if (!attributeMap.containsKey(qName)) {
                    HtmlElement.addAttributeToMap(page, attributeMap, attributes.getURI(i),
                        qName, attributes.getValue(i));
                }
            }
        }
        return attributeMap;
    }
}
