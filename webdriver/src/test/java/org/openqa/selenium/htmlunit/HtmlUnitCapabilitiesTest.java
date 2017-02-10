// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.openqa.selenium.htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the determineBrowserVersion method.
 */
public class HtmlUnitCapabilitiesTest {
  @Test
  public void configurationViaDirectCapabilities() {
    DesiredCapabilities ieCapabilities =
        new DesiredCapabilities(BrowserType.IE, "", Platform.ANY);

    assertEquals(HtmlUnitDriver.determineBrowserVersion(ieCapabilities),
        BrowserVersion.INTERNET_EXPLORER);

    DesiredCapabilities firefoxCapabilities =
        new DesiredCapabilities(BrowserType.FIREFOX, "", Platform.ANY);

    assertEquals(HtmlUnitDriver.determineBrowserVersion(firefoxCapabilities),
        BrowserVersion.FIREFOX_45);
  }

  @Test
  public void configurationOfFirefoxViaRemote() {
    DesiredCapabilities firefoxCapabilities =
        new DesiredCapabilities(BrowserType.HTMLUNIT, "firefox", Platform.ANY);

    assertEquals(HtmlUnitDriver.determineBrowserVersion(firefoxCapabilities),
        BrowserVersion.FIREFOX_45);
  }

  @Test
  public void configurationOfIEViaRemote() {
    DesiredCapabilities ieCapabilities =
        new DesiredCapabilities(BrowserType.HTMLUNIT, "internet explorer", Platform.ANY);

    assertEquals(HtmlUnitDriver.determineBrowserVersion(ieCapabilities),
        BrowserVersion.INTERNET_EXPLORER);
  }

  @Test
  public void tetsDefautlBrowserVersion() {
    DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();

    assertEquals(HtmlUnitDriver.determineBrowserVersion(capabilities),
        BrowserVersion.getDefault());
  }

  @Test
  public void htmlUnitReportsCapabilities() {
    HtmlUnitDriver driver = new HtmlUnitDriver(true);
    Capabilities jsEnabled = driver.getCapabilities();
    driver.quit();

    driver = new HtmlUnitDriver(false);
    Capabilities jsDisabled = driver.getCapabilities();

    assertTrue(jsEnabled.isJavascriptEnabled());
    assertFalse(jsDisabled.isJavascriptEnabled());
  }

  @Test
  public void configurationOfBrowserLanguage() {
    String browserLanguage = "es-ES";

    DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
    capabilities.setCapability(HtmlUnitDriver.BROWSER_LANGUAGE_CAPABILITY, browserLanguage);

    assertEquals(HtmlUnitDriver.determineBrowserVersion(capabilities).getBrowserLanguage(),
            browserLanguage);
  }

}
