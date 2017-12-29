//Licensed to the Software Freedom Conservancy (SFC) under one
//or more contributor license agreements.  See the NOTICE file
//distributed with this work for additional information
//regarding copyright ownership.  The SFC licenses this file
//to you under the Apache License, Version 2.0 (the
//"License"); you may not use this file except in compliance
//with the License.  You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing,
//software distributed under the License is distributed on an
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//KIND, either express or implied.  See the License for the
//specific language governing permissions and limitations
//under the License.
package com.salesforce.selenium.support.event;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.salesforce.selenium.support.event.Step.Cmd;

/**
 * Use this class as base class, if you want to implement a
 * {@link WebDriverEventListener} and are only interested in some events. All
 * methods provided by this class have an empty method body.
 * 
 * This is an extended version of org.openqa.selenium.support.events.AbstractWebDriverEventListener. See
 * https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/support/events/AbstractWebDriverEventListener.html
 * for more information.
 * 
 * @since 2.0.0
 */
public abstract class AbstractWebDriverEventListener implements WebDriverEventListener {

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeClose(Step step) {
	}

	@Override
	public void afterClose(Step step) {
	}

	@Override
	public void beforeFindElementByWebDriver(Step step, By by) {
	}

	@Override
	public void afterFindElementByWebDriver(Step step, WebElement returnedElement, By by) {
	}

	@Override
	public void beforeFindElementsByWebDriver(Step step, By by) {
	}

	@Override
	public void afterFindElementsByWebDriver(Step step, List<WebElement> returnedElements, By by) {
	}

	@Override
	public void beforeGet(Step step, String url) {
	}

	@Override
	public void afterGet(Step step, String url) {
	}

	@Override
	public void beforeGetCurrentUrl(Step step) {
	}

	@Override
	public void afterGetCurrentUrl(Step step, String url) {
	}

	@Override
	public void beforeGetTitle(Step step) {
	}

	@Override
	public void afterGetTitle(Step step, String title) {
	}

	@Override
	public void beforeGetWindowHandle(Step step) {
	}

	@Override
	public void afterGetWindowHandle(Step step, String handle) {
	}

	@Override
	public void beforeGetWindowHandles(Step step) {
	}

	@Override
	public void afterGetWindowHandles(Step step, Set<String> handles) {
	}

	@Override
	public void beforeQuit(Step step) {
	}

	@Override
	public void afterQuit(Step step) {
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeBack(Step step) {
	}

	@Override
	public void afterBack(Step step) {
	}

	@Override
	public void beforeForward(Step step) {
	}

	@Override
	public void afterForward(Step step) {
	}

	@Override
	public void beforeRefresh(Step step) {
	}

	@Override
	public void afterRefresh(Step step) {
	}

	@Override
	public void beforeTo(Step step, String url) {
	}

	@Override
	public void afterTo(Step step, String url) {
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.TargetLocator object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeActiveElement(Step step) {
	}

	@Override
	public void afterActiveElement(Step step, WebElement activeElement) {
	}

	@Override
	public void beforeAlert(Step step) {
	}

	@Override
	public void afterAlert(Step step, Alert alert) {
	}

	@Override
	public void beforeDefaultContent(Step step) {
	}

	@Override
	public void afterDefaultContent(Step step) {
	}

	@Override
	public void beforeFrameByIndex(Step step, int frameIndex) {
	}

	@Override
	public void afterFrameByIndex(Step step, int frameIndex) {
	}

	@Override
	public void beforeFrameByName(Step step, String frameName) {
	}

	@Override
	public void afterFrameByName(Step step, String frameName) {
	}

	@Override
	public void beforeFrameByElement(Step step, WebElement frameElement) {
	}

	@Override
	public void afterFrameByElement(Step step, WebElement frameElement) {
	}

	@Override
	public void beforeParentFrame(Step step) {
	}

	@Override
	public void afterParentFrame(Step step) {
	}

	@Override
	public void beforeWindow(Step step, String windowName) {
	}

	@Override
	public void afterWindow(Step step, String windowName) {
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Window object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeFullscreen(Step step) {
	}

	@Override
	public void afterFullscreen(Step step) {
	}

	@Override
	public void beforeGetPosition(Step step) {
	}

	@Override
	public void afterGetPosition(Step step, Point targetPosition) {
	}

	@Override
	public void beforeGetSize(Step step) {
	}

	@Override
	public void afterGetSize(Step step, Dimension targetSize) {
	}

	@Override
	public void beforeMaximize(Step step) {
	}

	@Override
	public void afterMaximize(Step step) {
	}

	@Override
	public void beforeSetPosition(Step step, Point targetPosition) {
	}

	@Override
	public void afterSetPosition(Step step, Point targetPosition) {
	}

	@Override
	public void beforeSetSize(Step step, Dimension targetSize) {
	}

	@Override
	public void afterSetSize(Step step, Dimension targetSize) {
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeClick(Step step, WebElement element) {
	}

	@Override
	public void afterClick(Step step, WebElement element) {
	}

	@Override
	public void beforeClear(Step step, WebElement element) {
	}

	@Override
	public void afterClear(Step step, WebElement element) {
	}

	@Override
	public void beforeFindElementByElement(Step step, By by, WebElement element) {
	}

	@Override
	public void afterFindElementByElement(Step step, WebElement returnedElement, By by, WebElement element) {
	}

	@Override
	public void beforeFindElementsByElement(Step step, By by, WebElement element) {
	}

	@Override
	public void afterFindElementsByElement(Step step, List<WebElement> returnedElements, By by, WebElement element) {
	}

	@Override
	public void beforeGetAttribute(Step step, String name, WebElement element) {
	}

	@Override
	public void afterGetAttribute(Step step, String value, String name, WebElement element) {
	}

	@Override
	public void beforeGetCssValue(Step step, String propertyName, WebElement element) {
	}

	@Override
	public void afterGetCssValue(Step step, String propertyName, String value, WebElement element) {
	}

	@Override
	public void beforeGetTagName(Step step, WebElement element) {
	}

	@Override
	public void afterGetTagName(Step step, String tagName, WebElement element) {
	}

	@Override
	public void beforeGetText(Step step, WebElement element) {
	}

	@Override
	public void afterGetText(Step step, String text, WebElement element) {
	}

	@Override
	public void beforeIsDisplayed(Step step, WebElement element) {
	}

	@Override
	public void afterIsDisplayed(Step step, boolean isDisplayed, WebElement element) {
	}

	@Override
	public void beforeIsEnabled(Step step, WebElement element) {
	}

	@Override
	public void afterIsEnabled(Step step, boolean isEnabled, WebElement element) {
	}

	@Override
	public void beforeIsSelected(Step step, WebElement element) {
	}

	@Override
	public void afterIsSelected(Step step, boolean isSelected, WebElement element) {
	}

	@Override
	public void beforeSendKeys(Step step, WebElement element, CharSequence... keysToSend) {
	}

	@Override
	public void afterSendKeys(Step step, WebElement element, CharSequence... keysToSend) {
	}

	@Override
	public void beforeSubmit(Step step, WebElement element) {
	}

	@Override
	public void afterSubmit(Step step, WebElement element) {
	}

	@Override
	public void onException(Step step, Cmd cmd, Throwable issue) {
	}
}
