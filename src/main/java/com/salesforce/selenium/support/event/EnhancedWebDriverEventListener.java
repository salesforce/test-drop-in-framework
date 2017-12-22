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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface EnhancedWebDriverEventListener {
	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#close close()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeClose(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#close close()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterClose(WebDriver driver);

	/**
	 * Called before {@link WebDriver#findElement WebDriver.findElement(...)}.
	 *
	 * @param by
	 *            locator being used
	 * @param driver
	 *            WebDriver
	 */
	void beforeFindElementByWebDriver(By by, WebDriver driver);

	/**
	 * Called after {@link WebDriver#findElement WebDriver.findElement(...)}.
	 * @param element
	 *            returned element
	 * @param by
	 *            locator being used
	 * @param driver
	 *            WebDriver
	 */
	void afterFindElementByWebDriver(WebElement element, By by, WebDriver driver);

	/**
	 * Called before {@link WebDriver#findElements WebDriver.findElements(...)}.
	 *
	 * @param by
	 *            locator being used
	 * @param driver
	 *            WebDriver
	 */
	void beforeFindElementsByWebDriver(By by, WebDriver driver);

	/**
	 * Called after{@link WebDriver#findElements WebDriver.findElements(...)}.
	 * @param elements
	 *            returned list of elements
	 * @param by
	 *            locator being used
	 * @param driver
	 *            WebDriver
	 */
	void afterFindElementsByWebDriver(List<WebElement> elements, By by, WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#get get(String url)}.
	 *
	 * @param url
	 *            URL
	 * @param driver
	 *            WebDriver
	 */
	void beforeGet(String url, WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#get get(String url)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param url
	 *            URL
	 * @param driver
	 *            WebDriver
	 */
	void afterGet(String url, WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getCurrentUrl getCurrentUrl()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetCurrentUrl(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getCurrentUrl getCurrentUrl()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param url
	 *            returned URL
	 * @param driver
	 *            WebDriver
	 */
	void afterGetCurrentUrl(String url, WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getTitle getTitle()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetTitle(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getTitle getTitle()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param title
	 *            returned page title
	 * @param driver
	 *            WebDriver
	 */
	void afterGetTitle(String title, WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getWindowHandle getWindowHandle()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetWindowHandle(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getWindowHandle getWindowHandle()}.
	 *
	 * @param handle
	 *            Handle to current window
	 * @param driver
	 *            WebDriver
	 */
	void afterGetWindowHandle(String handle, WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#getWindowHandles getWindowHandles()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetWindowHandles(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#getWindowHandles getWindowHandles()}.
	 *
	 * @param handles
	 *            Set of handles to windows currently open
	 * @param driver
	 *            WebDriver
	 */
	void afterGetWindowHandles(Set<String> handles, WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#quit quit()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeQuit(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#quit quit()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterQuit(WebDriver driver);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#back
	 * navigate().back()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeBack(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation
	 * navigate().back()}. Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterBack(WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#forward
	 * navigate().forward()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeForward(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#forward
	 * navigate().forward()}. Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterForward(WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#refresh
	 * navigate().refresh()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeRefresh(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#refresh
	 * navigate().refresh()}. Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterRefresh(WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#to
	 * navigate().to(String url)}.
	 *
	 * @param url
	 *            URL
	 * @param driver
	 *            WebDriver
	 */
	void beforeTo(String url, WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#to
	 * navigate().to(String url)}. Not called, if an exception is thrown.
	 *
	 * @param url
	 *            URL
	 * @param driver
	 *            WebDriver
	 */
	void afterTo(String url, WebDriver driver);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.TargetLocator object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebDriver.TargetLocator#activeElement() TargetLocator.activeElement()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeActiveElement(WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#activeElement() TargetLocator.activeElement()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param element
	 *            the current active WebElement
	 * @param driver
	 *            WebDriver
	 */
	void afterActiveElement(WebElement activeElement, WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#alert() TargetLocator.alert()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeAlert(WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#alert() TargetLocator.alert()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param alert
	 *            handle to the Alert
	 * @param driver
	 *            WebDriver
	 */
	void afterAlert(Alert activeElement, WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#defaultContent() TargetLocator.defaultContent()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeDefaultContent(WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#defaultContent() TargetLocator.defaultContent()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterDefaultContent(WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(int) TargetLocator.frame(..)}.
	 *
	 * @param frameIndex
	 *            0-based index of frame on page
	 * @param driver
	 *            WebDriver
	 */
	void beforeFrameByIndex(int frameIndex, WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(int) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param frameIndex
	 *            0-based index of frame on page
	 * @param driver
	 *            WebDriver
	 */
	void afterFrameByIndex(int frameIndex, WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(..)}.
	 *
	 * @param frameName
	 *            name of frame
	 * @param driver
	 *            WebDriver
	 */
	void beforeFrameByName(String frameName, WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param frameName
	 *            name of frame
	 * @param driver
	 *            WebDriver
	 */
	void afterFrameByName(String frameName, WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(WebElement) TargetLocator.frame(..)}.
	 *
	 * @param frameElement
	 *            element inside frame
	 * @param driver
	 *            WebDriver
	 */
	void beforeFrameByElement(WebElement frameElement, WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param frameElement
	 *            element inside frame
	 * @param driver
	 *            WebDriver
	 */
	void afterFrameByElement(WebElement frameElement, WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#parentFrame() TargetLocator.parentFrame()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeParentFrame(WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#parentFrame() TargetLocator.parentFrame()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterParentFrame(WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#window(java.lang.String) TargetLocator.window(..)}.
	 *
	 * @param windowName
	 *            name of window
	 * @param driver
	 *            WebDriver
	 */
	void beforeWindow(String windowName, WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#window(java.lang.String) TargetLocator.window(..)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param windowName
	 *            name of window
	 * @param driver
	 *            WebDriver
	 */
	void afterWindow(String windowName, WebDriver driver);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Window object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebDriver.Window#fullscreen() Window.fullscreen()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeFullscreen(WebDriver driver);

	/**
	 * Called after {@link WebDriver.Window#fullscreen() Window.fullscreen()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterFullscreen(WebDriver driver);

	/**
	 * Called before {@link WebDriver.Window#getPosition() getPosition()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetPosition(WebDriver driver);

	/**
	 * Called after {@link WebDriver.Window#getPosition() getPosition()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param targetPosition
	 *            returned location on screen
	 * @param driver
	 *            WebDriver
	 */
	void afterGetPosition(Point targetPosition, WebDriver driver);

	/**
	 * Called before {@link WebDriver.Window#getSize() getSize()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetSize(WebDriver driver);

	/**
	 * Called after {@link WebDriver.Window#getSize() getSize()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param targetSize
	 *            returned window size on screen
	 * @param driver
	 *            WebDriver
	 */
	void afterGetSize(Dimension targetSize, WebDriver driver);

	/**
	 * Called before {@link WebDriver.Window#maximize() Window.window()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeMaximize(WebDriver driver);

	/**
	 * Called after {@link WebDriver.Window#maximize() Window.window()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterMaximize(WebDriver driver);

	/**
	 * Called before {@link WebDriver.Window#setPosition(Point) setPosition(..)}.
	 *
	 * @param targetPosition
	 *            location on screen
	 * @param driver
	 *            WebDriver
	 */
	void beforeSetPosition(Point targetPosition, WebDriver driver);

	/**
	 * Called after {@link WebDriver.Window#setPosition(Point) setPosition(..)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param targetPosition
	 *            location on screen
	 * @param driver
	 *            WebDriver
	 */
	void afterSetPosition(Point targetPosition, WebDriver driver);

	/**
	 * Called before {@link WebDriver.Window#setSize(Dimension) setSize(..)}.
	 *
	 * @param targetSize
	 *            window size on screen
	 * @param driver
	 *            WebDriver
	 */
	void beforeSetSize(Dimension targetSize, WebDriver driver);

	/**
	 * Called after {@link WebDriver.Window#setSize(Dimension) setSize(..)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param targetSize
	 *            window size on screen
	 * @param driver
	 *            WebDriver
	 */
	void afterSetSize(Dimension targetSize, WebDriver driver);

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	/**
	 * Called before {@link WebElement#click WebElement.click()}.
	 *
	 * @param driver
	 *            WebDriver
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeClick(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#click WebElement.click()}. Not called, if an
	 * exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterClick(WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#clear WebElement.clear()}.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void beforeClear(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#clear WebElement.clear()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void afterClear(WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#findElement WebElement.findElement(...)}.
	 * @param by
	 *            locator being used
	 * @param element
	 *            use {@link #beforeFindElementByWebDriver(By, WebDriver) beforeFindElement(By, WebDriver)} if a find method of
	 *            <code>WebDriver</code> is called.
	 * @param driver
	 *            WebDriver
	 */
	void beforeFindElementByElement(By by, WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#findElement WebElement.findElement(...)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param returnedElement
	 *            returned element
	 * @param by
	 *            locator being used
	 * @param element
	 *            use {@link #afterFindElementByWebDriver(WebElement, By, WebDriver) afterFindElement(By, WebDriver)} if a find method of
	 *            <code>WebDriver</code> is called.
	 * @param driver
	 *            WebDriver
	 */
	void afterFindElementByElement(WebElement returnedElement, By by, WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#findElements WebElement.findElements(...)}.
	 * @param by
	 *            locator being used
	 * @param element
	 *            use {@link #beforeFindElementByWebDriver(By, WebDriver) beforeFindElement(By, WebDriver)} if a find method of
	 *            <code>WebDriver</code> is called.
	 * @param driver
	 *            WebDriver
	 */
	void beforeFindElementsByElement(By by, WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#findElements WebElement.findElements(...)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param returnedElements
	 *            returned list of elements
	 * @param by
	 *            locator being used
	 * @param element
	 *            use {@link #afterFindElementByWebDriver(WebElement, By, WebDriver) afterFindElement(By, WebDriver)} if a find method of
	 *            <code>WebDriver</code> is called.
	 * @param driver
	 *            WebDriver
	 */
	void afterFindElementsByElement(List<WebElement> returnedElements, By by, WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#getAttribute WebElement.getAttribute(...)}.
	 *
	 * @param name
	 *            name of the attribute to get
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetAttribute(String name, WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#getAttribute WebElement.getAttribute(...)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param value
	 *            value of the named attribute
	 * @param name
	 *            name of the attribute to get
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void afterGetAttribute(String value, String name, WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#getCssValue WebElement.getCssValue()}.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetCssValue(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#getCssValue WebElement.getCssValue()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param value
	 *            the retrieved CSS value
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void afterGetCssValue(String value, WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#getTagName WebElement.getTagName()}.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetTagName(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#getTagName WebElement.getTagName()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param tagName
	 *            the retrieved tag name
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void afterGetTagName(String tagName, WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#getText WebElement.getText()}.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void beforeGetText(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#getText WebElement.getText()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param text
	 *            the retrieved text
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void afterGetText(String text, WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#isDisplayed WebElement.isDisplayed()}.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void beforeIsDisplayed(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#isDisplayed WebElement.isDisplayed()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param isDisplayed
	 *            the retrieved value
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void afterIsDisplayed(boolean isDisplayed, WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#isEnabled WebElement.isEnabled()}.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void beforeIsEnabled(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#isEnabled WebElement.isEnabled()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param isEnabled
	 *            the retrieved value
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void afterIsEnabled(boolean isEnabled, WebElement element, WebDriver driver);

	/**
	 * Called before {@link WebElement#isSelected WebElement.isSelected()}.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void beforeIsSelected(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#isSelected WebElement.isSelected()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param isSelected
	 *            the retrieved value
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void afterIsSelected(boolean isSelected, WebElement element, WebDriver driver);
	
	/**
	 * Called before {@link WebElement#sendKeys WebElement.sendKeys(...)}.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 * @param keysToSend
	 *            text to insert
	 */
	void beforeSendKeys(WebElement element, WebDriver driver, CharSequence... keysToSend);

	/**
	 * Called after {@link WebElement#sendKeys WebElement.sendKeys(...)}}. Not called, if an
	 * exception is thrown.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 * @param keysToSend
	 *            text to insert
	 */
	void afterSendKeys(WebElement element, WebDriver driver, CharSequence... keysToSend);
	
	/**
	 * Called before {@link WebElement#submit WebElement.submit()}.
	 *
	 * @param driver
	 *            WebDriver
	 * @param element
	 *            the WebElement being used for the action
	 */
	void beforeSubmit(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#submit WebElement.submit()}. Not called, if an
	 * exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 * @param element
	 *            the WebElement being used for the action
	 */
	void afterSubmit(WebElement element, WebDriver driver);

	/**
	 * Called whenever an exception would be thrown.
	 *
	 * @param driver
	 *            WebDriver
	 * @param throwable
	 *            the exception that will be thrown
	 */
	void onException(Throwable throwable, WebDriver driver);

	/**
	 * Allows listener implementation to save itself away before the WebDriver instance has ended.
	 */
	void closeListener();
}
