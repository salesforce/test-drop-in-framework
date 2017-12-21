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

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface EnhancedWebDriverEventListener {

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
	 * Called before {@link org.openqa.selenium.WebDriver#get get(String url)}
	 * respectively {@link org.openqa.selenium.WebDriver.Navigation#to
	 * navigate().to(String url)}.
	 *
	 * @param url
	 *            URL
	 * @param driver
	 *            WebDriver
	 */
	void beforeNavigateTo(String url, WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#get get(String url)}
	 * respectively {@link org.openqa.selenium.WebDriver.Navigation#to
	 * navigate().to(String url)}. Not called, if an exception is thrown.
	 *
	 * @param url
	 *            URL
	 * @param driver
	 *            WebDriver
	 */
	void afterNavigateTo(String url, WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#back
	 * navigate().back()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeNavigateBack(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation
	 * navigate().back()}. Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterNavigateBack(WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#forward
	 * navigate().forward()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeNavigateForward(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#forward
	 * navigate().forward()}. Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterNavigateForward(WebDriver driver);

	/**
	 * Called before {@link org.openqa.selenium.WebDriver.Navigation#refresh
	 * navigate().refresh()}.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void beforeNavigateRefresh(WebDriver driver);

	/**
	 * Called after {@link org.openqa.selenium.WebDriver.Navigation#refresh
	 * navigate().refresh()}. Not called, if an exception is thrown.
	 *
	 * @param driver
	 *            WebDriver
	 */
	void afterNavigateRefresh(WebDriver driver);

	/**
	 * Called before {@link WebDriver#findElement WebDriver.findElement(...)}, or
	 * {@link WebDriver#findElements WebDriver.findElements(...)}, or
	 * {@link WebElement#findElement WebElement.findElement(...)}, or
	 * {@link WebElement#findElement WebElement.findElements(...)}.
	 *
	 * @param element
	 *            will be <code>null</code>, if a find method of
	 *            <code>WebDriver</code> is called.
	 * @param by
	 *            locator being used
	 * @param driver
	 *            WebDriver
	 */
	void beforeFindBy(By by, WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebDriver#findElement WebDriver.findElement(...)}, or
	 * {@link WebDriver#findElements WebDriver.findElements(...)}, or
	 * {@link WebElement#findElement WebElement.findElement(...)}, or
	 * {@link WebElement#findElement WebElement.findElements(...)}.
	 *
	 * @param element
	 *            will be <code>null</code>, if a find method of
	 *            <code>WebDriver</code> is called.
	 * @param by
	 *            locator being used
	 * @param driver
	 *            WebDriver
	 */
	void afterFindBy(By by, WebElement element, WebDriver driver);

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
	 * Called before {@link WebElement#clear WebElement.clear()}.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void beforeClear(WebElement element, WebDriver driver);

	/**
	 * Called after {@link WebElement#clear WebElement.clear()}. Not called, if an
	 * exception is thrown.
	 *
	 * @param element
	 *            the WebElement being used for the action
	 * @param driver
	 *            WebDriver
	 */
	void afterClear(WebElement element, WebDriver driver);

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
	 * Called before {@link WebDriver.Timeouts#pageLoadTimeout Timeouts.pageLoadTimeout()}.
	 *
	 * @param time
	 *            amount of time to wait
	 * @param unit
	 *            time unit to use
	 * @param driver
	 *            WebDriver
	 */
	void beforePageLoadTimeout(long time, TimeUnit unit, WebDriver driver);

	/**
	 * Called after {@link WebDriver.Timeouts#pageLoadTimeout Timeouts.pageLoadTimeout()}.
	 * Not called, if an exception is thrown.
	 *
	 * @param time
	 *            amount of time to wait
	 * @param unit
	 *            time unit to use
	 * @param driver
	 *            WebDriver
	 */
	void afterPageLoadTimeout(long time, TimeUnit unit, WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(int) TargetLocator.frame(..)}.
	 *
	 * @param frameIndex
	 *            0-based index of frame on page
	 * @param driver
	 *            WebDriver
	 */
	void beforeFrame(int frameIndex, WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(int) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param frameIndex
	 *            0-based index of frame on page
	 * @param driver
	 *            WebDriver
	 */
	void afterFrame(int frameIndex, WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(..)}.
	 *
	 * @param frameName
	 *            name of frame
	 * @param driver
	 *            WebDriver
	 */
	void beforeFrame(String frameName, WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param frameName
	 *            name of frame
	 * @param driver
	 *            WebDriver
	 */
	void afterFrame(String frameName, WebDriver driver);

	/**
	 * Called before {@link WebDriver.TargetLocator#frame(WebElement) TargetLocator.frame(..)}.
	 *
	 * @param frameElement
	 *            element inside frame
	 * @param driver
	 *            WebDriver
	 */
	void beforeFrame(WebElement frameElement, WebDriver driver);

	/**
	 * Called after {@link WebDriver.TargetLocator#frame(java.lang.String) TargetLocator.frame(..)}.
	 * Not called, if an exception is thrown.
	 *
	 * @param frameElement
	 *            element inside frame
	 * @param driver
	 *            WebDriver
	 */
	void afterFrame(WebElement frameElement, WebDriver driver);

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
	 * Called before
	 * {@link org.openqa.selenium.remote.RemoteWebDriver#executeScript(String, Object[]) }
	 *
	 * @param driver
	 *            WebDriver
	 * @param script
	 *            the script to be executed
	 */
	void beforeScript(String script, WebDriver driver);

	/**
	 * Called after
	 * {@link org.openqa.selenium.remote.RemoteWebDriver#executeScript(java.lang.String, java.lang.Object[]) }.
	 * Not called if an exception is thrown
	 *
	 * @param driver
	 *            WebDriver
	 * @param script
	 *            the script that was executed
	 */
	void afterScript(String script, WebDriver driver);

	/**
	 * Called whenever an exception would be thrown.
	 *
	 * @param driver
	 *            WebDriver
	 * @param throwable
	 *            the exception that will be thrown
	 */
	void onException(Throwable throwable, WebDriver driver);
}
