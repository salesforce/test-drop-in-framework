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
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.salesforce.selenium.support.event.Step.Cmd;

/**
 * Interface which supports registering of a listener with {@link EventFiringWebDriver}, e.g. for logging
 * purposes.
 * 
 * This is an extended version of org.openqa.selenium.support.events.WebDriverEventListener. See
 * https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/support/events/WebDriverEventListener.html
 * for more information.
 * 
 * @since 2.0.0
 */
public interface WebDriverEventListener {
	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#close close()}.
	 * @param step
	 *            step record
	 */
	void beforeClose(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#close close()}.
	 * @param step
	 *            step record
	 */
	void afterClose(Step step);

	/**
	 * Called before {@link WebDriver#findElement WebDriver.findElement(...)}.
	 * @param step
	 *            step record
	 * @param by
	 *            locator being used
	 */
	void beforeFindElementByWebDriver(Step step, By by);

	/**
	 * Called after {@link WebDriver#findElement WebDriver.findElement(...)}.
	 * @param step
	 *            step record
	 * @param element
	 *            returned element
	 * @param by
	 *            locator being used
	 */
	void afterFindElementByWebDriver(Step step, WebElement element, By by);

	/**
	 * Called before {@link WebDriver#findElements WebDriver.findElements(...)}.
	 * @param step
	 *            step record
	 * @param by
	 *            locator being used
	 */
	void beforeFindElementsByWebDriver(Step step, By by);

	/**
	 * Called after{@link WebDriver#findElements WebDriver.findElements(...)}.
	 * @param step
	 *            step record
	 * @param elements
	 *            returned list of elements
	 * @param by
	 *            locator being used
	 */
	void afterFindElementsByWebDriver(Step step, List<WebElement> elements, By by);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#get get(String url)}.
	 *
	 * @param step
	 *            step record
	 * @param url
	 *            URL
	 */
	void beforeGet(Step step, String url);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#get get(String url)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param url
	 *            URL
	 */
	void afterGet(Step step, String url);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getCurrentUrl getCurrentUrl()}.
	 * @param step
	 *            step record
	 */
	void beforeGetCurrentUrl(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getCurrentUrl getCurrentUrl()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param step
	 *            step record
	 * @param url
	 *            returned URL
	 */
	void afterGetCurrentUrl(Step step, String url);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getPageSource getPageSource()}.
	 * @param step
	 *            step record
	 */
	void beforeGetPageSource(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getPageSource getPageSource()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param step
	 *            step record
	 * @param source
	 *            returned page source
	 */
	void afterGetPageSource(Step step, String source);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getTitle getTitle()}.
	 * @param step
	 *            step record
	 */
	void beforeGetTitle(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getTitle getTitle()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param step
	 *            step record
	 * @param title
	 *            returned page title
	 */
	void afterGetTitle(Step step, String title);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getWindowHandle getWindowHandle()}.
	 * @param step
	 *            step record
	 */
	void beforeGetWindowHandle(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getWindowHandle getWindowHandle()}.
	 *
	 * @param step
	 *            step record
	 * @param handle
	 *            Handle to current window
	 */
	void afterGetWindowHandle(Step step, String handle);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getWindowHandles getWindowHandles()}.
	 * @param step
	 *            step record
	 */
	void beforeGetWindowHandles(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getWindowHandles getWindowHandles()}.
	 * @param step
	 *            step record
	 * @param handles
	 *            Set of handles to windows currently open
	 */
	void afterGetWindowHandles(Step step, Set<String> handles);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#quit quit()}.
	 * @param step
	 *            step record
	 */
	void beforeQuit(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#quit quit()}.
	 * @param step
	 *            step record
	 */
	void afterQuit(Step step);

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to JavascriptExecutor.
	 *--------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.JavascriptExecutor#executeAsyncScript(String, Object...) executingAsyncScript(String, Object...)}.
	 * @param step
	 *            step record
	 */
	void beforeExecuteAsyncScript(Step step, String script, Object... args);

	/**
	 * Called after {@link org.openqa.selenium.JavascriptExecutor#executeAsyncScript(String, Object...) executingAsyncScript(String, Object...)}.
	 * @param step
	 *            step record
	 */
	void afterExecuteAsyncScript(Step step, String script, Object... args);

	/**
	 * Called before {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...) executingScript(String, Object...)}.
	 * @param step
	 *            step record
	 */
	void beforeExecuteScript(Step step, String script, Object... args);

	/**
	 * Called after {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...) executingScript(String, Object...)}.
	 * @param step
	 *            step record
	 */
	void afterExecuteScript(Step step, String script, Object... args);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#back
	 * navigate().back()}.
	 * @param step
	 *            step record
	 */
	void beforeBack(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation
	 * navigate().back()}. Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 */
	void afterBack(Step step);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#forward
	 * navigate().forward()}.
	 * @param step
	 *            step record
	 */
	void beforeForward(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#forward
	 * navigate().forward()}. Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 */
	void afterForward(Step step);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#refresh
	 * navigate().refresh()}.
	 * @param step
	 *            step record
	 */
	void beforeRefresh(Step step);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#refresh
	 * navigate().refresh()}. Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 */
	void afterRefresh(Step step);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#to
	 * navigate().to(String url)}.
	 * @param step
	 *            step record
	 * @param url
	 *            URL
	 */
	void beforeTo(Step step, String url);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#to
	 * navigate().to(String url)}. Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param url
	 *            URL
	 */
	void afterTo(Step step, String url);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.TargetLocator object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebDriver.TargetLocator#activeElement() TargetLocator.activeElement()}.
	 * @param step
	 *            step record
	 */
	void beforeActiveElement(Step step);

	/**
	 * Called after {@link WebDriver.TargetLocator#activeElement() TargetLocator.activeElement()}.
	 * Not called, if an exception is thrown.
	 * @param step step record
	 * @param activeElement the current active WebElement
	 */
	void afterActiveElement(Step step, WebElement activeElement);

	/**
	 * Called before {@link WebDriver.TargetLocator#alert() TargetLocator.alert()}.
	 * @param step
	 *            step record
	 */
	void beforeAlert(Step step);

	/**
	 * Called after {@link WebDriver.TargetLocator#alert() TargetLocator.alert()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param alert
	 *            handle to the Alert
	 */
	void afterAlert(Step step, Alert alert);

	/**
	 * Called before {@link WebDriver.TargetLocator#defaultContent() TargetLocator.defaultContent()}.
	 * @param step
	 *            step record
	 */
	void beforeDefaultContent(Step step);

	/**
	 * Called after {@link WebDriver.TargetLocator#defaultContent() TargetLocator.defaultContent()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 */
	void afterDefaultContent(Step step);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(int) TargetLocator.frame(..)}.
	 * @param step
	 *            step record
	 * @param frameIndex
	 *            0-based index of frame on page
	 */
	void beforeFrameByIndex(Step step, int frameIndex);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(int) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param frameIndex
	 *            0-based index of frame on page
	 */
	void afterFrameByIndex(Step step, int frameIndex);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(..)}.
	 * @param step
	 *            step record
	 * @param frameName
	 *            name of frame
	 */
	void beforeFrameByName(Step step, String frameName);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param frameName
	 *            name of frame
	 */
	void afterFrameByName(Step step, String frameName);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(WebElement) TargetLocator.frame(..)}.
	 * @param step
	 *            step record
	 * @param frameElement
	 *            element inside frame
	 */
	void beforeFrameByElement(Step step, WebElement frameElement);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param frameElement
	 *            element inside frame
	 */
	void afterFrameByElement(Step step, WebElement frameElement);

	/**
	 * Called before {@link WebDriver.TargetLocator#parentFrame() TargetLocator.parentFrame()}.
	 * @param step
	 *            step record
	 */
	void beforeParentFrame(Step step);

	/**
	 * Called after {@link WebDriver.TargetLocator#parentFrame() TargetLocator.parentFrame()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 */
	void afterParentFrame(Step step);

	/**
	 * Called before {@link WebDriver.TargetLocator#window(java.lang.String) TargetLocator.window(..)}.
	 * @param step
	 *            step record
	 * @param windowName
	 *            name of window
	 */
	void beforeWindow(Step step, String windowName);

	/**
	 * Called after {@link WebDriver.TargetLocator#window(java.lang.String) TargetLocator.window(..)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param windowName
	 *            name of window
	 */
	void afterWindow(Step step, String windowName);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Timeouts object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebDriver.Timeouts#implicitlyWait(long, java.util.concurrent.TimeUnit) Timeouts.implicitlyWait(..)}.
	 * @param step
	 *            step record
	 * @param time
	 *            time to wait; to be converted using the given time unit
	 * @param unit
	 *            time unit to use to convert the given time value
	 */
	void beforeImplicitlyWait(Step step, long time, TimeUnit unit);

	/**
	 * Called after {@link WebDriver.Timeouts#implicitlyWait(long, java.util.concurrent.TimeUnit) Timeouts.implicitlyWait(..)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param time
	 *            time to wait; to be converted using the given time unit
	 * @param unit
	 *            time unit to use to convert the given time value
	 */
	void afterImplicitlyWait(Step step, long time, TimeUnit unit);

	/**
	 * Called before {@link WebDriver.Timeouts#pageLoadTimeout(long, java.util.concurrent.TimeUnit) Timeouts.pageLoadTimeout(..)}.
	 * @param step
	 *            step record
	 * @param time
	 *            time to wait; to be converted using the given time unit
	 * @param unit
	 *            time unit to use to convert the given time value
	 */
	void beforePageLoadTimeout(Step step, long time, TimeUnit unit);

	/**
	 * Called after {@link WebDriver.Timeouts#pageLoadTimeout(long, java.util.concurrent.TimeUnit) Timeouts.pageLoadTimeout(..)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param time
	 *            time to wait; to be converted using the given time unit
	 * @param unit
	 *            time unit to use to convert the given time value
	 */
	void afterPageLoadTimeout(Step step, long time, TimeUnit unit);

	/**
	 * Called before {@link WebDriver.Timeouts#setScriptTimeout(long, java.util.concurrent.TimeUnit) Timeouts.setScriptTimeout(..)}.
	 * @param step
	 *            step record
	 * @param time
	 *            time to wait; to be converted using the given time unit
	 * @param unit
	 *            time unit to use to convert the given time value
	 */
	void beforeSetScriptTimeout(Step step, long time, TimeUnit unit);

	/**
	 * Called after {@link WebDriver.Timeouts#setScriptTimeout(long, java.util.concurrent.TimeUnit) Timeouts.setScriptTimeout(..)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param time
	 *            time to wait; to be converted using the given time unit
	 * @param unit
	 *            time unit to use to convert the given time value
	 */
	void afterSetScriptTimeout(Step step, long time, TimeUnit unit);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Window object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebDriver.Window#fullscreen() Window.fullscreen()}.
	 * @param step
	 *            step record
	 */
	void beforeFullscreen(Step step);

	/**
	 * Called after {@link WebDriver.Window#fullscreen() Window.fullscreen()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 */
	void afterFullscreen(Step step);

	/**
	 * Called before {@link WebDriver.Window#getPosition() getPosition()}.
	 * @param step
	 *            step record
	 */
	void beforeGetPosition(Step step);

	/**
	 * Called after {@link WebDriver.Window#getPosition() getPosition()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param targetPosition
	 *            returned location on screen
	 */
	void afterGetPosition(Step step, Point targetPosition);

	/**
	 * Called before {@link WebDriver.Window#getSize() getSize()}.
	 * @param step
	 *            step record
	 */
	void beforeGetSize(Step step);

	/**
	 * Called after {@link WebDriver.Window#getSize() getSize()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param targetSize
	 *            returned window size on screen
	 */
	void afterGetSize(Step step, Dimension targetSize);

	/**
	 * Called before {@link WebDriver.Window#maximize() Window.window()}.
	 * @param step
	 *            step record
	 */
	void beforeMaximize(Step step);

	/**
	 * Called after {@link WebDriver.Window#maximize() Window.window()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 */
	void afterMaximize(Step step);

	/**
	 * Called before {@link WebDriver.Window#setPosition(Point) setPosition(..)}.
	 * @param step
	 *            step record
	 * @param targetPosition
	 *            location on screen
	 */
	void beforeSetPosition(Step step, Point targetPosition);

	/**
	 * Called after {@link WebDriver.Window#setPosition(Point) setPosition(..)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param targetPosition
	 *            location on screen
	 */
	void afterSetPosition(Step step, Point targetPosition);

	/**
	 * Called before {@link WebDriver.Window#setSize(Dimension) setSize(..)}.
	 * @param step
	 *            step record
	 * @param targetSize
	 *            window size on screen
	 */
	void beforeSetSize(Step step, Dimension targetSize);

	/**
	 * Called after {@link WebDriver.Window#setSize(Dimension) setSize(..)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param targetSize
	 *            window size on screen
	 */
	void afterSetSize(Step step, Dimension targetSize);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebElement#clickByElement WebElement.click()}.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeClick(Step step, WebElement element);

	/**
	 * Called after {@link WebElement#clickByElement WebElement.click()}. Not called, if an
	 * exception is thrown.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterClick(Step step, WebElement element);

	/**
	 * Called before {@link WebElement#clear WebElement.clear()}.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeClear(Step step, WebElement element);

	/**
	 * Called after {@link WebElement#clear WebElement.clear()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterClear(Step step, WebElement element);

	/**
	 * Called before {@link WebElement#findElement WebElement.findElement(...)}.
	 * @param step
	 *            step record
	 * @param by
	 *            locator being used
	 * @param element
	 *            use {@link #beforeFindElementByWebDriver(Step, By) beforeFindElement(By, WebDriver)} if a find method of
	 *            <code>WebDriver</code> is called.
	 */
	void beforeFindElementByElement(Step step, By by, WebElement element);

	/**
	 * Called after {@link WebElement#findElement WebElement.findElement(...)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param returnedElement
	 *            returned element
	 * @param by
	 *            locator being used
	 * @param element
	 *            use {@link #afterFindElementByWebDriver(Step, WebElement, By) afterFindElement(By, WebDriver)} if a find method of
	 *            <code>WebDriver</code> is called.
	 */
	void afterFindElementByElement(Step step, WebElement returnedElement, By by, WebElement element);

	/**
	 * Called before {@link WebElement#findElements WebElement.findElements(...)}.
	 * @param step
	 *            step record
	 * @param by
	 *            locator being used
	 * @param element
	 *            use {@link #beforeFindElementByWebDriver(Step, By) beforeFindElement(By, WebDriver)} if a find method of
	 *            <code>WebDriver</code> is called.
	 */
	void beforeFindElementsByElement(Step step, By by, WebElement element);

	/**
	 * Called after {@link WebElement#findElements WebElement.findElements(...)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param returnedElements
	 *            returned list of elements
	 * @param by
	 *            locator being used
	 * @param element
	 *            use {@link #afterFindElementByWebDriver(Step, WebElement, By) afterFindElement(By, WebDriver)} if a find method of
	 *            <code>WebDriver</code> is called.
	 */
	void afterFindElementsByElement(Step step, List<WebElement> returnedElements, By by, WebElement element);

	/**
	 * Called before {@link WebElement#getAttribute WebElement.getAttribute(...)}.
	 * @param step
	 *            step record
	 * @param name
	 *            name of the attribute to get
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeGetAttribute(Step step, String name, WebElement element);

	/**
	 * Called after {@link WebElement#getAttribute WebElement.getAttribute(...)}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param value
	 *            value of the named attribute
	 * @param name
	 *            name of the attribute to get
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterGetAttribute(Step step, String value, String name, WebElement element);

	/**
	 * Called before {@link WebElement#getCssValue WebElement.getCssValue()}.
	 * @param step
	 *            step record
	 * @param propertyName
	 * 			  name of the CSS property to get the value of 
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeGetCssValue(Step step, String propertyName, WebElement element);

	/**
	 * Called after {@link WebElement#getCssValue WebElement.getCssValue()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param propertyName
	 * 			  name of the CSS property to get the value of 
	 * @param value
	 *            the retrieved CSS value
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterGetCssValue(Step step, String propertyName, String value, WebElement element);

	/**
	 * Called before {@link WebElement#getTagName WebElement.getTagName()}.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeGetTagName(Step step, WebElement element);

	/**
	 * Called after {@link WebElement#getTagName WebElement.getTagName()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param tagName
	 *            the retrieved tag name
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterGetTagName(Step step, String tagName, WebElement element);

	/**
	 * Called before {@link WebElement#getText WebElement.getText()}.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeGetText(Step step, WebElement element);

	/**
	 * Called after {@link WebElement#getText WebElement.getText()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param text
	 *            the retrieved text
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterGetText(Step step, String text, WebElement element);

	/**
	 * Called before {@link WebElement#isDisplayed WebElement.isDisplayed()}.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeIsDisplayed(Step step, WebElement element);

	/**
	 * Called after {@link WebElement#isDisplayed WebElement.isDisplayed()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param isDisplayed
	 *            the retrieved value
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterIsDisplayed(Step step, boolean isDisplayed, WebElement element);

	/**
	 * Called before {@link WebElement#isEnabled WebElement.isEnabled()}.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeIsEnabled(Step step, WebElement element);

	/**
	 * Called after {@link WebElement#isEnabled WebElement.isEnabled()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param isEnabled
	 *            the retrieved value
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterIsEnabled(Step step, boolean isEnabled, WebElement element);

	/**
	 * Called before {@link WebElement#isSelected WebElement.isSelected()}.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeIsSelected(Step step, WebElement element);

	/**
	 * Called after {@link WebElement#isSelected WebElement.isSelected()}.
	 * Not called, if an exception is thrown.
	 * @param step
	 *            step record
	 * @param isSelected
	 *            the retrieved value
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterIsSelected(Step step, boolean isSelected, WebElement element);
	
	/**
	 * Called before {@link WebElement#sendKeysByElement WebElement.sendKeys(...)}.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 * @param keysToSend
	 *            text to insert
	 */
	void beforeSendKeys(Step step, WebElement element, CharSequence... keysToSend);

	/**
	 * Called after {@link WebElement#sendKeysByElement WebElement.sendKeys(...)}}. Not called, if an
	 * exception is thrown.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 * @param keysToSend
	 *            text to insert
	 */
	void afterSendKeys(Step step, WebElement element, CharSequence... keysToSend);
	
	/**
	 * Called before {@link WebElement#submit WebElement.submit()}.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeSubmit(Step step, WebElement element);

	/**
	 * Called after {@link WebElement#submit WebElement.submit()}. Not called, if an
	 * exception is thrown.
	 * @param step
	 *            step record
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterSubmit(Step step, WebElement element);

	/**
	 * Called whenever a command throws an exception.
	 * @param step
	 *            step record
	 * @param cmd
	 *            the command which ran into an issue
	 * @param throwable
	 *            the exception that will be thrown
	 */
	void onException(Step step, Cmd cmd, Throwable throwable);

	/**
	 * Allows listener implementation to save itself away before the WebDriver instance has ended.
	 */
	void closeListener();
}
