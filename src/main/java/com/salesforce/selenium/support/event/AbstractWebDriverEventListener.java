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
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Coordinates;

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
	public void beforeGetPageSource(Step step) {
	}

	@Override
	public void afterGetPageSource(Step step, String source) {
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

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to JavascriptExecutor.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeExecuteAsyncScript(Step step, String script, Object... args) {
	}

	@Override
	public void afterExecuteAsyncScript(Step step, String script, Object... args) {
	}

	@Override
	public void beforeExecuteScript(Step step, String script, Object... args) {
	}

	@Override
	public void afterExecuteScript(Step step, String script, Object... args) {
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to TakesScreenshot.
	 *--------------------------------------------------------------------*/

	@Override
	public <X> void beforeGetScreenshotAs(Step step, OutputType<X> target) {
	}

	@Override
	public <X> void afterGetScreenshotAs(Step step, OutputType<X> target) {
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
	 * Section for all commands called directly from WebDriver.Timeouts object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeImplicitlyWait(Step step, long time, TimeUnit unit) {
	}

	@Override
	public void afterImplicitlyWait(Step step, long time, TimeUnit unit) {
	}

	@Override
	public void beforePageLoadTimeout(Step step, long time, TimeUnit unit) {
	}

	@Override
	public void afterPageLoadTimeout(Step step, long time, TimeUnit unit) {
	}

	@Override
	public void beforeSetScriptTimeout(Step step, long time, TimeUnit unit) {
	}

	@Override
	public void afterSetScriptTimeout(Step step, long time, TimeUnit unit) {
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
	public void beforeGetSizeByWindow(Step step) {
	}

	@Override
	public void afterGetSizeByWindow(Step step, Dimension targetSize) {
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
	public void beforeGetLocation(Step step, WebElement element) {
	}

	@Override
	public void afterGetLocation(Step step, Point point, WebElement element) {
	}

	@Override
	public void beforeGetSizeByElement(Step step, WebElement element) {
	}

	@Override
	public void afterGetSizeByElement(Step step, Dimension dimension, WebElement element) {
	}

	@Override
	public void beforeGetRect(Step step, WebElement element) {
	}

	@Override
	public void afterGetRect(Step step, Rectangle rectangle, WebElement element) {
	}

	@Override
	public void beforeSendKeysByElement(Step step, WebElement element, CharSequence... keysToSend) {
	}

	@Override
	public void afterSendKeysByElement(Step step, WebElement element, CharSequence... keysToSend) {
	}

	@Override
	public void beforeSubmit(Step step, WebElement element) {
	}

	@Override
	public void afterSubmit(Step step, WebElement element) {
	}

	@Override
	public void beforeSendKeysByKeyboard(Step step, CharSequence... keysToSend) {
	}

	@Override
	public void afterSendKeysByKeyboard(Step step, CharSequence... keysToSend) {
	}

	@Override
	public void beforePressKey(Step step, CharSequence... keyToPress) {
	}

	@Override
	public void afterPressKey(Step step, CharSequence... keyToPress) {
	}

	@Override
	public void beforeReleaseKey(Step step, CharSequence... keyToPress) {
	}

	@Override
	public void afterReleaseKey(Step step, CharSequence... keyToPress) {
	}

	@Override
	public void beforeClickByMouse(Step step, Coordinates where) {
	}

	@Override
	public void afterClickByMouse(Step step, Coordinates where) {
	}

	@Override
	public void beforeDoubleClick(Step step, Coordinates where) {
	}

	@Override
	public void afterDoubleClick(Step step, Coordinates where) {
	}

	@Override
	public void beforeMouseDown(Step step, Coordinates where) {
	}

	@Override
	public void afterMouseDown(Step step, Coordinates where) {
	}

	@Override
	public void beforeMouseUp(Step step, Coordinates where) {
	}

	@Override
	public void afterMouseUp(Step step, Coordinates where) {
	}

	@Override
	public void beforeMouseMove(Step step, Coordinates where) {
	}

	@Override
	public void afterMouseMove(Step step, Coordinates where) {
	}

	@Override
	public void beforeMouseMove(Step step, Coordinates where, long xOffset, long yOffset) {
	}

	@Override
	public void afterMouseMove(Step step, Coordinates where, long xOffset, long yOffset) {
	}

	@Override
	public void beforeContextClick(Step step, Coordinates where) {
	}

	@Override
	public void afterContextClick(Step step, Coordinates where) {
	}

	@Override
	public void onException(Step step, Cmd cmd, Throwable issue) {
	}

	/**
	 * The test name can contain characters which are not supported in a file name. This
	 * convenience method replaces such characters with underscores.
	 * @param testName test name
	 * @return file name using only characters supported by the OS
	 */
	public static String convertTestname2FileName(final String testName) {
		return testName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
	}
}
