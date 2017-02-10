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

/**
 * @fileoverview IE specific atoms.
 */

goog.provide('webdriver.ie');

goog.require('bot.dom');
goog.require('bot.Error');
goog.require('bot.ErrorCode');
goog.require('bot.locators');
goog.require('bot.userAgent');
goog.require('goog.dom.TagName');
goog.require('goog.style');


/**
 * Find the first element in the DOM matching the mechanism and critera.
 *
 * @param {!string} mechanism The mechanism to search by.
 * @param {!string} criteria The criteria to search for.
 * @param {(Document|Element)=} opt_root The node from which to start the
 *     search. If not specified, will use {@code document} as the root.
 * @return {!Object} An object containing the status of the find and
 *     the result (success will be the first matching element found in
 *     the DOM, failure will be the associated error message).
 */
webdriver.ie.findElement = function(mechanism, criteria, opt_root) {
  var locator = {};
  var retval = {};
  locator[mechanism] = criteria;
  try {
    retval = bot.locators.findElement(locator, opt_root);
  } catch (e) {
    // The normal error case throws bot.Error, which has a 'code'
    // property. In the case where the locator mechanism is unknown,
    // findElement throws a plain JavaScript Error, which doesn't.
    return {
             'status': e.code || bot.ErrorCode.JAVASCRIPT_ERROR,
             'value': e.message
           };
  }
  if (retval == null) {
    return { 'status': bot.ErrorCode.NO_SUCH_ELEMENT, 'value': retval }
  }
  return { 'status': bot.ErrorCode.SUCCESS, 'value': retval };
};


/**
 * Find all elements in the DOM matching the mechanism and critera.
 *
 * @param {!string} mechanism The mechanism to search by.
 * @param {!string} criteria The criteria to search for.
 * @param {(Document|Element)=} opt_root The node from which to start the
 *     search. If not specified, will use {@code document} as the root.
 * @return {!Object} An object containing the status of the find and
 *     the result (success will be the elements, failure will be the
 *     associated error message).
 */
webdriver.ie.findElements = function(mechanism, criteria, opt_root) {
  // For finding multiple elements by class name in IE, if the document
  // mode is below 8 (which includes quirks mode), the findElements atom
  // drops into a branch of the Closure library that doesn't validate
  // that the class name is valid, and therefore just returns an empty
  // array. Thus, we pre-screen the class name in this special case.
  // Note: the regex used herein may not be entirely correct; judging
  // this to be an acceptable risk due to the (hopefully) limited nature
  // of the bug.
  if (mechanism == 'className' && bot.userAgent.IE_DOC_PRE8) {
    var invalidTokenRegex = /[~!@\$%\^&\*\(\)_\+=,\.\/';:"\?><\[\]\\\{\}\|`#]+/;
    if (invalidTokenRegex.test(criteria)) {
      return { 'status': bot.ErrorCode.INVALID_SELECTOR_ERROR,
               'value': 'Invalid character in class name.' };
    }
  }
  var retval = {};
  var locator = {};
  locator[mechanism] = criteria;
  try {
    retval = bot.locators.findElements(locator, opt_root);
  } catch (e) {
    // The normal error case throws bot.Error, which has a 'code'
    // property. In the case where the locator mechanism is unknown,
    // findElement throws a plain JavaScript Error, which doesn't.
    return {
             'status': e.code || bot.ErrorCode.JAVASCRIPT_ERROR,
             'value': e.message
           };
  }
  return {'status': bot.ErrorCode.SUCCESS, 'value': retval};
};


/**
 * Checks whether the element is currently scrolled into the parent's overflow
 * region, such that the offset given, relative to the top-left corner of the
 * element, is currently in the overflow region.
 *
 * @param {!Element} element The element to check.
 * @return {bot.dom.OverflowState} Whether the coordinates specified, relative to the element,
 *     are scrolled in the parent overflow.
 */
webdriver.ie.isInParentOverflow = function(element) {
  var rect = bot.dom.getClientRect(element);
  var x = Math.round(rect.width / 2);
  var y = Math.round(rect.height / 2);
  var center = new goog.math.Coordinate(x, y);
  return bot.dom.getOverflowState(element, center);
};
