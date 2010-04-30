/*
Copyright 2007-2009 WebDriver committers
Copyright 2007-2009 Google Inc.
Portions copyright 2007 ThoughtWorks, Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.openqa.selenium.htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.Speed;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.internal.ReturnedCookie;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class HtmlUnitDriver implements WebDriver, SearchContext, JavascriptExecutor,
                                       FindsById, FindsByLinkText, FindsByXPath, FindsByName,
                                       FindsByTagName {

  private WebClient webClient;
  private WebWindow currentWindow;

  private boolean enableJavascript;
  private ProxyConfig proxyConfig;
  private AtomicLong windowNamer = new AtomicLong(System.currentTimeMillis());
  private final BrowserVersion version;

  public HtmlUnitDriver(BrowserVersion version) {
    this.version = version;
    webClient = createWebClient(version);
    currentWindow = webClient.getCurrentWindow();

    webClient.addWebWindowListener(new WebWindowListener() {
      public void webWindowOpened(WebWindowEvent webWindowEvent) {
        // Ignore
      }

      public void webWindowContentChanged(WebWindowEvent event) {
        if (event.getWebWindow() != currentWindow) {
          return;
        }

        // Do we need to pick some new default content?
        switchToDefaultContentOfWindow(currentWindow);
      }

      public void webWindowClosed(WebWindowEvent event) {
        // Check if the event window refers to us or one of our parent windows
        // setup the currentWindow appropriately if necessary
        WebWindow curr = currentWindow;
        do {
          // Instance equality is okay in this case
          if (curr == event.getWebWindow()) {
            currentWindow = currentWindow.getTopWindow();
            return;
          }
          curr = curr.getParentWindow();
        } while (curr != currentWindow.getTopWindow());
      }
    });

    // Now put us on the home page, like a real browser
    get(webClient.getHomePage());
  }

  public HtmlUnitDriver() {
    this(false);
  }

  public HtmlUnitDriver(boolean enableJavascript) {
    this(BrowserVersion.getDefault());
    setJavascriptEnabled(enableJavascript);
  }

  private HtmlUnitDriver(boolean enableJavascript, WebWindow currentWindow) {
    this(enableJavascript);
    this.currentWindow = currentWindow;
  }

  private WebClient createWebClient(BrowserVersion version) {
    WebClient client = newWebClient(version);
    client.setHomePage(WebClient.URL_ABOUT_BLANK.toString());
    client.setThrowExceptionOnFailingStatusCode(false);
    client.setPrintContentOnFailingStatusCode(false);
    client.setJavaScriptEnabled(enableJavascript);
    client.setRedirectEnabled(true);
    try {
      client.setUseInsecureSSL(true);
    } catch (GeneralSecurityException e) {
      throw new WebDriverException(e);
    }

    // Ensure that we've set the proxy if necessary
    if (proxyConfig != null) {
      client.setProxyConfig(proxyConfig);
    }

    return modifyWebClient(client);
  }

  /**
   * Create the underlying webclient, but don't set any fields on it.
   *
   * @param version Which browser to emulate
   * @return a new instance of WebClient.
   */
  protected WebClient newWebClient(BrowserVersion version) {
    return new WebClient(version);
  }

  /**
   * Child classes can override this method to customise the webclient that the HtmlUnit driver
   * uses.
   *
   * @param client The client to modify
   * @return The modified client
   */
  protected WebClient modifyWebClient(WebClient client) {
    // Does nothing here to be overridden.
    return client;
  }

  public void setProxy(String host, int port) {
    proxyConfig = new ProxyConfig(host, port);
    webClient.setProxyConfig(proxyConfig);
  }

  public void get(String url) {
    // Prevent the malformed url exception.
    if (WebClient.URL_ABOUT_BLANK.toString().equals(url)) {
      get(WebClient.URL_ABOUT_BLANK);
      return;
    }

    URL fullUrl;
    try {
      fullUrl = new URL(url);
    } catch (Exception e) {
      throw new WebDriverException(e);
    }

    get(fullUrl);
  }

  /**
   * Allows HtmlUnit's about:blank to be loaded in the constructor, and may be useful for other
   * tests?
   * @param fullUrl The URL to visit
   */
  protected void get(URL fullUrl) {
    try {
      // A "get" works over the entire page
      currentWindow = currentWindow.getTopWindow();
      webClient.getPage(fullUrl);
    } catch (UnknownHostException e) {
      // This should be fine
    } catch (ConnectException e) {
      // This might be expected
    } catch (Exception e) {
      throw new WebDriverException(e);
    }

    pickWindow();
  }

  protected void pickWindow() {
    // TODO(simon): HtmlUnit tries to track the current window as the frontmost. We don't
    if (currentWindow == null) {
      currentWindow = webClient.getCurrentWindow();
    }
    Page page = currentWindow.getEnclosedPage();

    if (page == null) {
      return;
    }

    if (!(page instanceof HtmlPage)) {
      return;
    }

    if (((HtmlPage) page).getFrames().size() > 0) {
      FrameWindow frame = ((HtmlPage) page).getFrames().get(0);
      if (!(frame.getFrameElement() instanceof HtmlInlineFrame)) {
        switchTo().frame(0);
      }
    }
  }

  public String getCurrentUrl() {
    // TODO(simon): Blech. I can see this being baaad
    Page page = lastPage();
    if (page == null) {
      return null;
    }

    WebResponse response = page.getWebResponse();
    return response.getRequestUrl().toString();
  }

  public String getTitle() {
    Page page = lastPage();
    if (page == null || !(page instanceof HtmlPage)) {
      return null; // no page so there is no title
    }
    return ((HtmlPage) page).getTitleText();
  }

  public WebElement findElement(By by) {
    return by.findElement((SearchContext) this);
  }

  public List<WebElement> findElements(By by) {
    return by.findElements((SearchContext) this);
  }

  public String getPageSource() {
    Page page = lastPage();
    if (page == null) {
      return null;
    }

    WebResponse response = page.getWebResponse();
    return response.getContentAsString();
  }

  public void close() {
    webClient = createWebClient(version);
  }

  public void quit() {
    if (webClient != null) {
      webClient.closeAllWindows();
      webClient = null;
    }
    currentWindow = null;
  }

  public Set<String> getWindowHandles() {
    Set<String> allHandles = new HashSet<String>();
    List<WebWindow> allWindows = webClient.getWebWindows();
    for (WebWindow window : allWindows) {
      WebWindow top = window.getTopWindow();
      if (top.getName() == null || "".equals(top.getName())) {
        nameWindow(top);
      }
      allHandles.add(top.getName());
    }

    return allHandles;
  }

  public String getWindowHandle() {
    WebWindow window = webClient.getCurrentWindow();
    if (window.getName() == null || "".equals(window.getName())) {
      nameWindow(window);
    }
    return window.getName();
  }

  private String nameWindow(WebWindow window) {
    String windowName = "webdriver" + windowNamer.incrementAndGet();
    window.setName(windowName);
    return windowName;
  }

  public Object executeScript(String script, Object... args) {
    if (!(lastPage() instanceof HtmlPage)) {
      throw new UnsupportedOperationException("Cannot execute JS against a plain text page");
    }

    HtmlPage page = (HtmlPage) lastPage();

    if (!isJavascriptEnabled()) {
      throw new UnsupportedOperationException(
          "Javascript is not enabled for this HtmlUnitDriver instance");
    }

    Object[] parameters = new Object[args.length];

    for (int i = 0; i < args.length; i++) {
      parameters[i] = parseArgumentIntoJavsacriptParameter(args[i]);
    }

    script = "function() {" + script + "};";
    ScriptResult result = page.executeJavaScript(script);
    Function func = (Function) result.getJavaScriptResult();

    result = page.executeJavaScriptFunctionIfPossible(
        func,
        (ScriptableObject) currentWindow.getScriptObject(),
        parameters,
        page.getDocumentElement());

    return parseNativeJavascriptResult(result);
  }

  private Object parseArgumentIntoJavsacriptParameter(Object arg) {
    if (!(arg instanceof HtmlUnitWebElement ||
        arg instanceof HtmlElement || // special case the underlying type
        arg instanceof Number ||
        arg instanceof String ||
        arg instanceof Boolean ||
        arg.getClass().isArray() ||
        arg instanceof Collection<?>)) {
    throw new IllegalArgumentException(
        "Argument must be a string, number, boolean or WebElement: " +
        arg + " (" + arg.getClass() + ")");
    }
  
    if (arg instanceof HtmlUnitWebElement) {
      HtmlElement element = ((HtmlUnitWebElement) arg).getElement();
      return element.getScriptObject();
    } else if (arg instanceof HtmlElement) {
      return ((HtmlElement) arg).getScriptObject();
    } else if (arg instanceof Collection<?>) {
      List<Object> list = new ArrayList<Object>();
      for (Object o : (Collection<?>)arg) {
        list.add(parseArgumentIntoJavsacriptParameter(o));
      }
      return list.toArray();
    } else {
      return arg;
    }
  }

  private Object parseNativeJavascriptResult(Object result) {
    Object value;
    if (result instanceof ScriptResult) {
      value = ((ScriptResult)result).getJavaScriptResult();
    } else {
      value = result;
    }
    if (value instanceof HTMLElement) {
      return newHtmlUnitWebElement(((HTMLElement) value).getDomNodeOrDie());
    }

    if (value instanceof Number) {
      if (value instanceof Float || value instanceof Double) {
        return ((Number) value).doubleValue();
      }
      return ((Number) value).longValue();
    }
    
    if (value instanceof NativeArray) {
      NativeArray array = (NativeArray)value;
      List<Object> list = new ArrayList<Object>((int)array.getLength());
      for (int i = 0; i < array.getLength(); ++i) {
        list.add(parseNativeJavascriptResult(array.get(i)));
      }
      return list;
    }

    if (value instanceof Undefined) {
      return null;
    }

    return value;
  }

  public TargetLocator switchTo() {
    return new HtmlUnitTargetLocator();
  }

  private void switchToDefaultContentOfWindow(WebWindow window) {
    Page page = window.getEnclosedPage();
    if (page instanceof HtmlPage) {
      // Check for frames
      List<FrameWindow> frames = ((HtmlPage) page).getFrames();
      if (frames.size() > 0) {
        FrameWindow frameWindow = frames.get(0);
        if (HtmlFrame.class.isAssignableFrom(frameWindow.getFrameElement().getClass())) {
          currentWindow = frameWindow;
          return;
        }
      }

      // Lovely. We're on a normal page
      currentWindow = window;
    }
  }

  public Navigation navigate() {
    return new HtmlUnitNavigation();
  }

  protected synchronized Page lastPage() {
    return currentWindow.getEnclosedPage();
  }

  public WebElement findElementByLinkText(String selector) {
    int equalsIndex = selector.indexOf('=') + 1;
    String expectedText = selector.substring(equalsIndex).trim();

    if (!(lastPage() instanceof HtmlPage)) {
      throw new IllegalStateException("Cannot find links for " + lastPage());
    }

    List<HtmlAnchor> anchors = ((HtmlPage) lastPage()).getAnchors();
    for (HtmlAnchor anchor : anchors) {
      if (expectedText.equals(anchor.asText())) {
        return newHtmlUnitWebElement(anchor);
      }
    }
    throw new NoSuchElementException("No link found with text: " + expectedText);
  }

  protected WebElement newHtmlUnitWebElement(HtmlElement element) {
    if (isJavascriptEnabled()) {
      return new RenderedHtmlUnitDriverWebElement(this, element);
    }
    return new HtmlUnitWebElement(this, element);
  }

  public List<WebElement> findElementsByLinkText(String selector) {
    List<WebElement> elements = new ArrayList<WebElement>();

    if (!(lastPage() instanceof HtmlPage)) {
      return elements;
    }

    int equalsIndex = selector.indexOf('=') + 1;
    String expectedText = selector.substring(equalsIndex).trim();

    List<HtmlAnchor> anchors = ((HtmlPage) lastPage()).getAnchors();
    for (HtmlAnchor anchor : anchors) {
      if (expectedText.equals(anchor.asText())) {
        elements.add(newHtmlUnitWebElement(anchor));
      }
    }
    return elements;
  }

  public WebElement findElementById(String id) {
    if (!(lastPage() instanceof HtmlPage)) {
      throw new NoSuchElementException("Cannot find element by id for " + lastPage());
    }

    try {
      HtmlElement element = ((HtmlPage) lastPage()).getHtmlElementById(id);
      return newHtmlUnitWebElement(element);
    } catch (ElementNotFoundException e) {
      throw new NoSuchElementException("Cannot find element with ID: " + id);
    }
  }

  public List<WebElement> findElementsById(String id) {
    return findElementsByXPath("//*[@id='" + id + "']");
  }

  public WebElement findElementByName(String name) {
    if (!(lastPage() instanceof HtmlPage)) {
      throw new IllegalStateException("Cannot find element by name for " + lastPage());
    }

    List<HtmlElement> allElements = ((HtmlPage) lastPage()).getElementsByName(name);
    if (allElements.size() > 0) {
      return newHtmlUnitWebElement(allElements.get(0));
    }

    throw new NoSuchElementException("Cannot find element with name: " + name);
  }

  public List<WebElement> findElementsByName(String using) {
    if (!(lastPage() instanceof HtmlPage)) {
      return new ArrayList<WebElement>();
    }

    List<HtmlElement> allElements = ((HtmlPage) lastPage()).getElementsByName(using);
    return convertRawHtmlElementsToWebElements(allElements);
  }

  public WebElement findElementByTagName(String name) {
    if (!(lastPage() instanceof HtmlPage)) {
      throw new IllegalStateException("Cannot find element by name for " + lastPage());
    }

    NodeList allElements = ((HtmlPage) lastPage()).getElementsByTagName(name);
    if (allElements.getLength() > 0) {
      return newHtmlUnitWebElement((HtmlElement) allElements.item(0));
    }

    throw new NoSuchElementException("Cannot find element with name: " + name);
  }

  public List<WebElement> findElementsByTagName(String using) {
    if (!(lastPage() instanceof HtmlPage)) {
      return new ArrayList<WebElement>();
    }

    NodeList allElements = ((HtmlPage) lastPage()).getElementsByTagName(using);
    List<WebElement> toReturn = new ArrayList<WebElement>(allElements.getLength());
    for (int i = 0; i < allElements.getLength(); i++) {
      Node item = allElements.item(i);
      if (item instanceof HtmlElement) {
        toReturn.add(newHtmlUnitWebElement((HtmlElement) item));
      }
    }
    return toReturn;
  }

  public WebElement findElementByXPath(String selector) {
    if (!(lastPage() instanceof HtmlPage)) {
      throw new IllegalStateException("Cannot find element by xpath for " + lastPage());
    }

    Object node = ((HtmlPage) lastPage()).getFirstByXPath(selector);
    if (node == null) {
      throw new NoSuchElementException("Cannot locate a node using " + selector);
    }
    if (node instanceof HtmlElement) {
      return newHtmlUnitWebElement((HtmlElement) node);
    }
    throw new NoSuchElementException(String.format("Cannot find element with xpath %s", selector));
  }

  public List<WebElement> findElementsByXPath(String selector) {
    if (!(lastPage() instanceof HtmlPage)) {
      return new ArrayList<WebElement>();
    }

    List<?> nodes = ((HtmlPage) lastPage()).getByXPath(selector);
    return convertRawHtmlElementsToWebElements(nodes);
  }

  private List<WebElement> convertRawHtmlElementsToWebElements(List<?> nodes) {
    List<WebElement> elements = new ArrayList<WebElement>();

    for (Object node : nodes) {
      if (node instanceof HtmlElement) {
        elements.add(newHtmlUnitWebElement((HtmlElement) node));
      }
    }

    return elements;
  }

  public boolean isJavascriptEnabled() {
    return webClient.isJavaScriptEnabled();
  }

  public void setJavascriptEnabled(boolean enableJavascript) {
    this.enableJavascript = enableJavascript;
    webClient.setJavaScriptEnabled(enableJavascript);
  }

  private class HtmlUnitTargetLocator implements TargetLocator {

    public WebDriver frame(int frameIndex) {
      HtmlPage page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
      try {
        currentWindow = page.getFrames().get(frameIndex);
      } catch (IndexOutOfBoundsException e) {
        throw new NoSuchFrameException("Cannot find frame: " + frameIndex);
      }
      return HtmlUnitDriver.this;
    }

    public WebDriver frame(String name) {
      HtmlPage page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
      WebWindow window = webClient.getCurrentWindow();

      String[] names = name.split("\\.");
      for (String frameName : names) {
        try {
          int index = Integer.parseInt(frameName);
          window = page.getFrames().get(index);
        } catch (NumberFormatException e) {
          window = null;
          for (Object frame : page.getFrames()) {
            FrameWindow frameWindow = (FrameWindow) frame;
            if (frameName.equals(frameWindow.getFrameElement().getId())) {
              window = frameWindow;
              break;
            } else if (frameName.equals(frameWindow.getName())) {
              window = frameWindow;
              break;
            }
          }
          if (window == null) {
            throw new NoSuchFrameException("Cannot find frame: " + name);
          }
        } catch (IndexOutOfBoundsException e) {
          throw new NoSuchFrameException("Cannot find frame: " + name);
        }

        page = (HtmlPage) window.getEnclosedPage();
      }

      currentWindow = window;
      return HtmlUnitDriver.this;
    }

    public WebDriver window(String windowId) {
      WebWindow window;
      try {
        window = webClient.getWebWindowByName(windowId);
      } catch (WebWindowNotFoundException e) {
        throw new NoSuchWindowException("Cannot find window: " + windowId);
      }
      webClient.setCurrentWindow(window);
      currentWindow = window;
      pickWindow();
      return HtmlUnitDriver.this;
    }

    public WebDriver defaultContent() {
      switchToDefaultContentOfWindow(currentWindow.getTopWindow());
      return HtmlUnitDriver.this;
    }

    public WebElement activeElement() {
      Page page = currentWindow.getEnclosedPage();
      if (page instanceof HtmlPage) {
        HtmlElement element = ((HtmlPage) page).getFocusedElement();
        if (element == null) {
          List<? extends HtmlElement> allBodies =
              ((HtmlPage) page).getDocumentElement().getHtmlElementsByTagName("body");
          if (allBodies.size() > 0) {
            return newHtmlUnitWebElement(allBodies.get(0));
          }
        } else {
          return newHtmlUnitWebElement(element);
        }
      }

      throw new NoSuchElementException("Unable to locate element with focus or body tag");
    }

    public Alert alert() {
      return null;
    }
  }

  protected WebDriver findActiveWindow() {
    WebWindow window = webClient.getCurrentWindow();
    HtmlPage page = (HtmlPage) window.getEnclosedPage();

    if (page != null && page.getFrames().size() > 0) {
      FrameWindow frame = page.getFrames().get(0);
      if (!(frame.getFrameElement() instanceof HtmlInlineFrame)) {
        return new HtmlUnitDriver(isJavascriptEnabled(), frame);
      }
    }

    if (currentWindow != null && currentWindow.equals(window)) {
      return this;
    }
    return new HtmlUnitDriver(isJavascriptEnabled(), window);
  }


  protected WebClient getWebClient() {
    return webClient;
  }

  protected WebWindow getCurrentWindow() {
    return currentWindow;
  }

  private class HtmlUnitNavigation implements Navigation {

    public void back() {
      try {
        currentWindow.getHistory().back();
      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }

    public void forward() {
      try {
        currentWindow.getHistory().forward();
      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }


    public void to(String url) {
      get(url);
    }

    public void to(URL url) {
      get(url);
    }

    public void refresh() {
      if (lastPage() instanceof HtmlPage) {
        try {
          ((HtmlPage) lastPage()).refresh();
        } catch (IOException e) {
          throw new WebDriverException(e);
        }
      }
    }
  }

  public Options manage() {
    return new HtmlUnitOptions();
  }

  private class HtmlUnitOptions implements Options {

    public void addCookie(Cookie cookie) {
      Page page = lastPage();
      if (!(page instanceof HtmlPage)) {
        throw new WebDriverException("You may not set cookies on a page that is not HTML");
      }

      String domain = getDomainForCookie();
      verifyDomain(cookie, domain);

      webClient.getCookieManager().addCookie(
          new com.gargoylesoftware.htmlunit.util.Cookie(domain, cookie.getName(), cookie.getValue(),
                                                   cookie.getPath(), cookie.getExpiry(), cookie.isSecure()));
    }

    private void verifyDomain(Cookie cookie, String expectedDomain) {
      String domain = cookie.getDomain();
      if (domain == null) {
        return;
      }

      if ("".equals(domain)) {
        throw new WebDriverException(
            "Domain must not be an empty string. Consider using null instead");
      }

      // Line-noise-tastic
      if (domain.matches(".*[^:]:\\d+$")) {
        domain = domain.replaceFirst(":\\d+$", "");
      }

      expectedDomain = expectedDomain.startsWith(".") ? expectedDomain : "." + expectedDomain;
      domain = domain.startsWith(".") ? domain : "." + domain;

      if (!expectedDomain.endsWith(domain)) {
        throw new WebDriverException(
            String.format(
                "You may only add cookies that would be visible to the current domain: %s => %s",
                domain, expectedDomain));
      }
    }

    public Cookie getCookieNamed(String name) {
      Set<Cookie> allCookies = getCookies();
      for (Cookie cookie : allCookies) {
        if (name.equals(cookie.getName())) {
          return cookie;
        }
      }

      return null;
    }

    public void deleteCookieNamed(String name) {
      CookieManager cookieManager = webClient.getCookieManager();
      URL url = lastPage().getWebResponse().getWebRequest().getUrl();
      Set<com.gargoylesoftware.htmlunit.util.Cookie> rawCookies =
                webClient.getCookieManager().getCookies(url);
      for (com.gargoylesoftware.htmlunit.util.Cookie cookie : rawCookies) {
        if (name.equals(cookie.getName())) {
          cookieManager.removeCookie(cookie);
        }
      }
    }

    public void deleteCookie(Cookie cookie) {
      deleteCookieNamed(cookie.getName());
    }

    public void deleteAllCookies() {
      webClient.getCookieManager().clearCookies();
    }

    public Set<Cookie> getCookies() {
      URL url = lastPage().getWebResponse().getWebRequest().getUrl();
      Set<com.gargoylesoftware.htmlunit.util.Cookie> rawCookies =
               webClient.getCookieManager().getCookies(url);

      Set<Cookie> retCookies = new HashSet<Cookie>();
      for (com.gargoylesoftware.htmlunit.util.Cookie c : rawCookies) {
        if (c.getPath() != null && getPath().startsWith(c.getPath())) {
          retCookies.add(new ReturnedCookie(c.getName(), c.getValue(), c.getDomain(), c.getPath(),
                  c.getExpires(), c.isSecure()));
        }
      }
      return retCookies;
    }

    private String getHostName() {
      return lastPage().getWebResponse().getRequestUrl().getHost().toLowerCase();
    }

    private String getPath() {
      return lastPage().getWebResponse().getRequestUrl().getPath();
    }

    public Speed getSpeed() {
      throw new UnsupportedOperationException();
    }

    public void setSpeed(Speed speed) {
      throw new UnsupportedOperationException();
    }

    private String getDomainForCookie() {
      URL current = lastPage().getWebResponse().getRequestUrl();
      return current.getHost();
    }
  }

  public WebElement findElementByPartialLinkText(String using) {
    if (!(lastPage() instanceof HtmlPage)) {
      throw new IllegalStateException("Cannot find links for " + lastPage());
    }

    List<HtmlAnchor> anchors = ((HtmlPage) lastPage()).getAnchors();
    for (HtmlAnchor anchor : anchors) {
      if (anchor.asText().contains(using)) {
        return newHtmlUnitWebElement(anchor);
      }
    }
    throw new NoSuchElementException("No link found with text: " + using);
  }

  public List<WebElement> findElementsByPartialLinkText(String using) {

    List<HtmlAnchor> anchors = ((HtmlPage) lastPage()).getAnchors();
    Iterator<HtmlAnchor> allAnchors = anchors.iterator();
    List<WebElement> elements = new ArrayList<WebElement>();
    while (allAnchors.hasNext()) {
      HtmlAnchor anchor = allAnchors.next();
      if (anchor.asText().contains(using)) {
        elements.add(newHtmlUnitWebElement(anchor));
      }
    }
    return elements;
  }
}
