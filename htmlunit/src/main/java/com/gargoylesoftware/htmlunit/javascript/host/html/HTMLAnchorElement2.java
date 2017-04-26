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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PATHNAME_NONE_FOR_BROKEN_URL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PATHNAME_NONE_FOR_NONE_HTTP_URL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PROTOCOL_COLON_FOR_BROKEN_URL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PROTOCOL_COLON_UPPER_CASE_DRIVE_LETTERS;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * The JavaScript object that represents an anchor.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gousseff@netscape.net">Alexei Goussev</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@ScriptClass
public class HTMLAnchorElement2 extends HTMLElement2 {

    private static final List<String> REFERRER_POLICIES = Arrays.asList("no-referrer", "origin", "unsafe-url");

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static HTMLAnchorElement2 constructor(final boolean newObj, final Object self) {
        final HTMLAnchorElement2 host = new HTMLAnchorElement2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Sets the {@code href} property.
     * @param href the {@code href} property value
     */
    @Setter
    public void setHref(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of this link's {@code href} property.
     * @return the value of this link's {@code href} property
     */
    @Getter
    public String getHref() {
        final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
        final String hrefAttr = anchor.getHrefAttribute();

        if (hrefAttr == DomElement.ATTRIBUTE_NOT_DEFINED) {
            return "";
        }

        try {
            return getUrl().toString();
        }
        catch (final MalformedURLException e) {
            return hrefAttr;
        }
    }

    /**
     * Sets the focus to this element.
     */
    @Override
    public void focus() {
        final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
        final String hrefAttr = anchor.getHrefAttribute();

        if (hrefAttr != DomElement.ATTRIBUTE_NOT_DEFINED) {
            anchor.focus();
        }
    }

    /**
     * Sets the name property.
     * @param name name attribute value
     */
    @Setter
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute("name", name);
    }

    /**
     * Returns the value of the name property of this link.
     * @return the name property
     */
    @Getter
    public String getName() {
        return getDomNodeOrDie().getAttribute("name");
    }

    /**
     * Sets the target property of this link.
     * @param target target attribute value
     */
    @Setter
    public void setTarget(final String target) {
        getDomNodeOrDie().setAttribute("target", target);
    }

    /**
     * Returns the value of the target property of this link.
     * @return the href property
     */
    @Getter
    public String getTarget() {
        return getDomNodeOrDie().getAttribute("target");
    }

    /**
     * Returns this link's current URL.
     * @return this link's current URL
     * @throws MalformedURLException if an error occurs
     */
    private URL getUrl() throws MalformedURLException {
        final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
        return ((HtmlPage) anchor.getPage()).getFullyQualifiedUrl(anchor.getHrefAttribute());
    }

    /**
     * Sets the {@code href} attribute of this link to the specified URL.
     * @param url the new value of the {@code href} attribute
     */
    private void setUrl(final URL url) {
        getDomNodeOrDie().setAttribute("href", url.toString());
    }

    /**
     * Sets the rel property.
     * @param rel rel attribute value
     */
    @Setter
    public void setRel(final String rel) {
        getDomNodeOrDie().setAttribute("rel", rel);
    }

    /**
     * Returns the value of the rel property.
     * @return the rel property
     */
    @Getter
    public String getRel() {
        return ((HtmlAnchor) getDomNodeOrDie()).getRelAttribute();
    }

    /**
     * Returns the value of the rev property.
     * @return the rev property
     */
    @Getter
    public String getRev() {
        return ((HtmlAnchor) getDomNodeOrDie()).getRevAttribute();
    }

    /**
     * Sets the rev property.
     * @param rel rev attribute value
     */
    @Setter
    public void setRev(final String rel) {
        getDomNodeOrDie().setAttribute("rev", rel);
    }

    /**
     * Returns the value of the rev property.
     * @return the referrerPolicy property
     */
    @Getter(@WebBrowser(CHROME))
    public String getReferrerPolicy() {
        String attrib = ((HtmlAnchor) getDomNodeOrDie()).getAttribute("referrerPolicy");
        if (StringUtils.isEmpty(attrib)) {
            return "";
        }
        attrib = attrib.toLowerCase(Locale.ROOT);
        if (REFERRER_POLICIES.contains(attrib)) {
            return attrib;
        }
        return "";
    }

    /**
     * Sets the rev property.
     * @param referrerPolicy referrerPolicy attribute value
     */
    @Setter(@WebBrowser(CHROME))
    public void setReferrerPolicy(final String referrerPolicy) {
        getDomNodeOrDie().setAttribute("referrerPolicy", referrerPolicy);
    }

    /**
     * Returns the search portion of the link's URL (the portion starting with
     * '?' and up to but not including any '#').
     * @return the search portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534620.aspx">MSDN Documentation</a>
     */
    @Getter
    public String getSearch() {
        try {
            final String query = getUrl().getQuery();
            if (query == null) {
                return "";
            }
            return "?" + query;
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the search portion of the link's URL (the portion starting with '?'
     * and up to but not including any '#')..
     * @param search the new search portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534620.aspx">MSDN Documentation</a>
     */
    @Setter
    public void setSearch(final String search) throws Exception {
        final String query;
        if (search == null || "?".equals(search) || "".equals(search)) {
            query = null;
        }
        else if (search.charAt(0) == '?') {
            query = search.substring(1);
        }
        else {
            query = search;
        }

        setUrl(UrlUtils.getUrlWithNewQuery(getUrl(), query));
    }

    /**
     * Returns the hash portion of the link's URL (the portion following the '#', including the '#').
     * @return the hash portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    @Getter
    public String getHash() {
        try {
            final String hash = getUrl().getRef();
            if (hash == null) {
                return "";
            }
            return "#" + hash;
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the hash portion of the link's URL (the portion following the '#').
     * @param hash the new hash portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    @Setter
    public void setHash(final String hash) throws Exception {
        setUrl(UrlUtils.getUrlWithNewRef(getUrl(), hash));
    }

    /**
     * Returns the host portion of the link's URL (the '[hostname]:[port]' portion).
     * @return the host portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533784.aspx">MSDN Documentation</a>
     */
    @Getter
    public String getHost() {
        try {
            final URL url = getUrl();
            final int port = url.getPort();
            final String host = url.getHost();

            if (port == -1) {
                return host;
            }
            return host + ":" + port;
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the host portion of the link's URL (the '[hostname]:[port]' portion).
     * @param host the new host portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533784.aspx">MSDN Documentation</a>
     */
    @Setter
    public void setHost(final String host) throws Exception {
        final String hostname;
        final int port;
        final int index = host.indexOf(':');
        if (index != -1) {
            hostname = host.substring(0, index);
            port = Integer.parseInt(host.substring(index + 1));
        }
        else {
            hostname = host;
            port = -1;
        }
        final URL url = UrlUtils.getUrlWithNewHostAndPort(getUrl(), hostname, port);
        setUrl(url);
    }

    /**
     * Returns the hostname portion of the link's URL.
     * @return the hostname portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533785.aspx">MSDN Documentation</a>
     */
    @Getter
    public String getHostname() {
        try {
            return getUrl().getHost();
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the hostname portion of the link's URL.
     * @param hostname the new hostname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533785.aspx">MSDN Documentation</a>
     */
    @Setter
    public void setHostname(final String hostname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
    }

    /**
     * Returns the pathname portion of the link's URL.
     * @return the pathname portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534332.aspx">MSDN Documentation</a>
     */
    @Getter
    public String getPathname() {
        try {
            final URL url = getUrl();
            if (!url.getProtocol().startsWith("http")
                    && getBrowserVersion().hasFeature(JS_ANCHOR_PATHNAME_NONE_FOR_NONE_HTTP_URL)) {
                return "";
            }
            if (getBrowserVersion().hasFeature(JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL)) {
                final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
                String href = anchor.getHrefAttribute();
                if (href.length() > 1 && Character.isLetter(href.charAt(0)) && ':' == href.charAt(1)) {
                    if (getBrowserVersion().hasFeature(JS_ANCHOR_PROTOCOL_COLON_UPPER_CASE_DRIVE_LETTERS)) {
                        href = StringUtils.capitalize(href);
                    }
                    if (getBrowserVersion().hasFeature(JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL)) {
                        href = "/" + href;
                    }
                    return href;
                }
            }
            return url.getPath();
        }
        catch (final MalformedURLException e) {
            final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
            if (anchor.getHrefAttribute().startsWith("http")
                    && getBrowserVersion().hasFeature(JS_ANCHOR_PATHNAME_NONE_FOR_BROKEN_URL)) {
                return "";
            }
            return "/";
        }
    }

    /**
     * Sets the pathname portion of the link's URL.
     * @param pathname the new pathname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534332.aspx">MSDN Documentation</a>
     */
    @Setter
    public void setPathname(final String pathname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPath(getUrl(), pathname));
    }

    /**
     * Returns the port portion of the link's URL.
     * @return the port portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534342.aspx">MSDN Documentation</a>
     */
    @Getter
    public String getPort() {
        try {
            final int port = getUrl().getPort();
            if (port == -1) {
                return "";
            }
            return Integer.toString(port);
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the port portion of the link's URL.
     * @param port the new port portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534342.aspx">MSDN Documentation</a>
     */
    @Setter
    public void setPort(final String port) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPort(getUrl(), Integer.parseInt(port)));
    }

    /**
     * Returns the protocol portion of the link's URL, including the trailing ':'.
     * @return the protocol portion of the link's URL, including the trailing ':'
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    @Getter
    public String getProtocol() {
        try {
            if (getBrowserVersion().hasFeature(JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL)) {
                final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
                final String href = anchor.getHrefAttribute().toLowerCase(Locale.ROOT);
                if (href.length() > 1 && Character.isLetter(href.charAt(0)) && ':' == href.charAt(1)) {
                    return "file:";
                }
            }

            return getUrl().getProtocol() + ":";
        }
        catch (final MalformedURLException e) {
            final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
            if (anchor.getHrefAttribute().startsWith("http")
                    && getBrowserVersion().hasFeature(JS_ANCHOR_PROTOCOL_COLON_FOR_BROKEN_URL)) {
                return ":";
            }
            return StringUtils.substringBefore(anchor.getHrefAttribute(), "/");
        }
    }

    /**
     * Sets the protocol portion of the link's URL.
     * @param protocol the new protocol portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    @Setter
    public void setProtocol(final String protocol) throws Exception {
        final String bareProtocol = StringUtils.substringBefore(protocol, ":");
        setUrl(UrlUtils.getUrlWithNewProtocol(getUrl(), bareProtocol));
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        final HtmlElement element = getDomNodeOrNull();
        if (element == null) {
            return super.getDefaultValue(null);
        }
        return getDefaultValue(element);
    }

    static String getDefaultValue(final HtmlElement element) {
        String href = element.getAttribute("href");

        if (DomElement.ATTRIBUTE_NOT_DEFINED == href) {
            return ""; // for example for named anchors
        }

        href = href.trim();

        final SgmlPage page = element.getPage();
        if (page == null || !page.isHtmlPage()) {
            return href;
        }

        try {
            return HtmlAnchor.getTargetUrl(href, (HtmlPage) page).toExternalForm();
        }
        catch (final MalformedURLException e) {
            return href;
        }
    }

    /**
     * Returns the {@code text} attribute.
     * @return the {@code text} attribute
     */
    @Getter
    public String getText() {
        final DomNode htmlElement = getDomNodeOrDie();
        return htmlElement.asText();
    }

    /**
     * Sets the {@code text} attribute.
     * @param text the {@code text} attribute
     */
    @Setter
    public void setText(final String text) {
        final DomNode htmlElement = getDomNodeOrDie();
        htmlElement.setTextContent(text);
    }

    /**
     * Returns the {@code charset} attribute.
     * @return the {@code charset} attribute
     */
    @Getter
    public String getCharset() {
        return getDomNodeOrDie().getAttribute("charset");
    }

    /**
     * Sets the {@code charset} attribute.
     * @param charset the {@code charset} attribute
     */
    @Setter
    public void setCharset(final String charset) {
        getDomNodeOrDie().setAttribute("charset", charset);
    }

    /**
     * Returns the {@code coords} attribute.
     * @return the {@code coords} attribute
     */
    @Getter
    public String getCoords() {
        return getDomNodeOrDie().getAttribute("coords");
    }

    /**
     * Sets the {@code coords} attribute.
     * @param coords {@code coords} attribute
     */
    @Setter
    public void setCoords(final String coords) {
        getDomNodeOrDie().setAttribute("coords", coords);
    }

    /**
     * Returns the {@code hreflang} attribute.
     * @return the {@code hreflang} attribute
     */
    @Getter
    public String getHreflang() {
        return getDomNodeOrDie().getAttribute("hreflang");
    }

    /**
     * Sets the {@code hreflang} attribute.
     * @param hreflang {@code hreflang} attribute
     */
    @Setter
    public void setHreflang(final String hreflang) {
        getDomNodeOrDie().setAttribute("hreflang", hreflang);
    }

    /**
     * Returns the {@code origin} attribute.
     * @return the {@code origin} attribute
     */
    @Getter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public String getOrigin() {
        if (!getDomNodeOrDie().hasAttribute("href")) {
            return "";
        }

        try {
            return getUrl().getProtocol() + "://" + getHost();
        }
        catch (final Exception e) {
            return "";
        }
    }

    /**
     * Sets the {@code origin} attribute.
     * @param origin {@code origin} attribute
     */
    @Setter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public void setOrigin(final String origin) {
        // ignore
    }

    /**
     * Returns the {@code username} attribute.
     * @return the {@code username} attribute
     */
    @Getter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public String getUsername() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code username} attribute.
     * @param username {@code username} attribute
     */
    @Setter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public void setUsername(final String username) {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code password} attribute.
     * @return the {@code password} attribute
     */
    @Getter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public String getPassword() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code password} attribute.
     * @param password {@code password} attribute
     */
    @Setter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public void setPassword(final String password) {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code download} attribute.
     * @return the {@code download} attribute
     */
    @Getter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public String getDownload() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code download} attribute.
     * @param download {@code download} attribute
     */
    @Setter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public void setDownload(final String download) {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code ping} attribute.
     * @return the {@code ping} attribute
     */
    @Getter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public String getPing() {
        return ((HtmlAnchor) getDomNodeOrDie()).getPingAttribute();
    }

    /**
     * Sets the {@code ping} attribute.
     * @param ping {@code ping} attribute
     */
    @Setter({@WebBrowser(CHROME), @WebBrowser(FF)})
    public void setPing(final String ping) {
        getDomNodeOrDie().setAttribute("ping", ping);
    }

    /**
     * Returns the {@code shape} attribute.
     * @return the {@code shape} attribute
     */
    @Getter
    public String getShape() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code shape} attribute.
     * @param shape {@code shape} attribute
     */
    @Setter
    public void setShape(final String shape) {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code type} attribute.
     * @return the {@code type} attribute
     */
    @Getter
    public String getType() {
        return getDomNodeOrDie().getAttribute("type");
    }

    /**
     * Sets the {@code type} attribute.
     * @param type {@code type} attribute
     */
    @Setter
    public void setType(final String type) {
        getDomNodeOrDie().setAttribute("type", type);
    }

    /**
     * Returns the {@code relList} attribute.
     * @return the {@code relList} attribute
     */
    @Getter(@WebBrowser(FF))
    public DOMTokenList getRelList() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code protocolLong} attribute.
     * @return the {@code protocolLong} attribute
     */
    @Getter(@WebBrowser(IE))
    public String getProtocolLong() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code Methods} attribute.
     * @return the {@code Methods} attribute
     */
    @Getter(name = "Methods", value = @WebBrowser(IE))
    public String getMethods() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code Methods} attribute.
     * @param methods {@code Methods} attribute
     */
    @Setter(name = "Methods", value = @WebBrowser(IE))
    public void setMethods(final String methods) {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code mimeType} attribute.
     * @return the {@code mimeType} attribute
     */
    @Getter(@WebBrowser(IE))
    public String getMimeType() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code mimeType} attribute.
     * @param mimeType {@code mimeType} attribute
     */
    @Setter(@WebBrowser(IE))
    public void setMimeType(final String mimeType) {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code nameProp} attribute.
     * @return the {@code nameProp} attribute
     */
    @Getter(@WebBrowser(IE))
    public String getNameProp() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code urn} attribute.
     * @return the {@code urn} attribute
     */
    @Getter(@WebBrowser(IE))
    public String getUrn() {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code urn} attribute.
     * @param urn {@code urn} attribute
     */
    @Setter(@WebBrowser(IE))
    public void setUrn(final String urn) {
        throw new RuntimeException(new UnsupportedOperationException());
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLAnchorElement2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("HTMLAnchorElement",
                    staticHandle("constructor", HTMLAnchorElement2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends PrototypeObject {
        Prototype() {
            ScriptUtils.initialize(this);
        }

        @Override
        public String getClassName() {
            return "HTMLAnchorElement";
        }
    }
}
