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

/**
 * Use this class as base class, if you want to implement a
 * {@link WebDriverEventListener} and are only interested in some events. All
 * methods provided by this class have an empty method body.
 */
public abstract class AbstractEnhancedWebDriverEventListener implements EnhancedWebDriverEventListener {

	@Override
	public void beforeClose(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterClose(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeQuit(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterQuit(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeGetWindowHandles(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterGetWindowHandles(Set<String> handles, WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeGetWindowHandle(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterGetWindowHandle(String handle, WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeClick(WebElement element, WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void afterClick(WebElement element, WebDriver driver) {
		// Do nothing.
	}

	@Override
	public void beforeSendKeys(WebElement element, WebDriver driver, CharSequence... keysToSend) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterSendKeys(WebElement element, WebDriver driver, CharSequence... keysToSend) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSubmit(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterSubmit(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeClear(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterClear(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeGetTagName(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterGetTagName(String tagName, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeGetAttribute(String name, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterGetAttribute(String value, String name, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeIsSelected(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterIsSelected(boolean isSelected, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeIsEnabled(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterIsEnabled(boolean isEnabled, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeGetText(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterGetText(String text, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeGetCssValue(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterGetCssValue(String value, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeIsDisplayed(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterIsDisplayed(boolean isDisplayed, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforePageLoadTimeout(long time, TimeUnit unit, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterPageLoadTimeout(long time, TimeUnit unit, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeFrame(int frameIndex, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterFrame(int frameIndex, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeFrame(String frameName, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterFrame(String frameName, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeFrame(WebElement frameElement, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterFrame(WebElement frameElement, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeParentFrame(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterParentFrame(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeDefaultContent(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterDefaultContent(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeActiveElement(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterActiveElement(WebElement activeElement, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeAlert(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterAlert(Alert activeElement, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeMaximize(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterMaximize(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeFullscreen(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterFullscreen(WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		// Do nothing
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		// Do nothing
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		// Do nothing
	}
}
