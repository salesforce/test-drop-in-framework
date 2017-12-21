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
	protected boolean isDebugMode = false;

	@Override
	public void beforeClose(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeClose n/a");
	}

	@Override
	public void afterClose(WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterClose n/a");
	}

	@Override
	public void beforeQuit(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeQuit n/a");
	}

	@Override
	public void afterQuit(WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterQuit n/a");
	}

	@Override
	public void beforeGetWindowHandles(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeGetWindowHandles n/a");
	}

	@Override
	public void afterGetWindowHandles(Set<String> handles, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterGetWindowHandles n/a");
	}

	@Override
	public void beforeGetWindowHandle(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeGetWindowHandle n/a");
	}

	@Override
	public void afterGetWindowHandle(String handle, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterGetWindowHandle n/a");
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeNavigateTo n/a");
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterNavigateTo n/a");
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeNavigateBack n/a");
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterNavigateBack n/a");
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeNavigateForward n/a");
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterNavigateForward n/a");
	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeNavigateRefresh n/a");
	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterNavigateRefresh n/a");
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeFindBy n/a");
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterFindBy n/a");
	}

	@Override
	public void beforeClick(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeClick n/a");
	}

	@Override
	public void afterClick(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterClick n/a");
	}

	@Override
	public void beforeSendKeys(WebElement element, WebDriver driver, CharSequence... keysToSend) {
		if (isDebugMode)
			System.out.println("beforeSendKeys n/a");
	}

	@Override
	public void afterSendKeys(WebElement element, WebDriver driver, CharSequence... keysToSend) {
		if (isDebugMode)
			System.out.println("afterSendKeys n/a");
	}

	@Override
	public void beforeSubmit(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeSubmit n/a");
	}

	@Override
	public void afterSubmit(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterSubmit n/a");
	}

	@Override
	public void beforeClear(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeClear n/a");
	}

	@Override
	public void afterClear(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterClear n/a");
	}

	@Override
	public void beforeGetTagName(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeGetTagName n/a");
	}

	@Override
	public void afterGetTagName(String tagName, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterGetTagName n/a");
	}

	@Override
	public void beforeGetAttribute(String name, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeGetAttribute n/a");
	}

	@Override
	public void afterGetAttribute(String value, String name, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterGetAttribute n/a");
	}

	@Override
	public void beforeIsSelected(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeIsSelected n/a");
	}

	@Override
	public void afterIsSelected(boolean isSelected, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterIsSelected n/a");
	}

	@Override
	public void beforeIsEnabled(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeIsEnabled n/a");
	}

	@Override
	public void afterIsEnabled(boolean isEnabled, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterIsEnabled n/a");
	}

	@Override
	public void beforeGetText(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeGetText n/a");
	}

	@Override
	public void afterGetText(String text, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterGetText n/a");
	}

	@Override
	public void beforeGetCssValue(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeGetCssValue n/a");
	}

	@Override
	public void afterGetCssValue(String value, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterGetCssValue n/a");
	}

	@Override
	public void beforeIsDisplayed(WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeIsDisplayed n/a");
	}

	@Override
	public void afterIsDisplayed(boolean isDisplayed, WebElement element, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterIsDisplayed n/a");
	}

	@Override
	public void beforePageLoadTimeout(long time, TimeUnit unit, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforePageLoadTimeout n/a");
	}

	@Override
	public void afterPageLoadTimeout(long time, TimeUnit unit, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterPageLoadTimeout n/a");
	}

	@Override
	public void beforeFrame(int frameIndex, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeFrameIndex n/a");
	}

	@Override
	public void afterFrame(int frameIndex, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterFrameIndex n/a");
	}

	@Override
	public void beforeFrame(String frameName, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeFrameName n/a");
	}

	@Override
	public void afterFrame(String frameName, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterFrameName n/a");
	}

	@Override
	public void beforeFrame(WebElement frameElement, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeFrameElement n/a");
	}

	@Override
	public void afterFrame(WebElement frameElement, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterFrameElement n/a");
	}

	@Override
	public void beforeParentFrame(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeParentFrame n/a");
	}

	@Override
	public void afterParentFrame(WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterParentFrame n/a");
	}

	@Override
	public void beforeWindow(String windowName, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeWindow n/a");
	}

	@Override
	public void afterWindow(String windowName, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterWindow n/a");
	}

	@Override
	public void beforeDefaultContent(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeDefaultContent n/a");
	}

	@Override
	public void afterDefaultContent(WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterDefaultContent n/a");
	}

	@Override
	public void beforeActiveElement(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeActiveElement n/a");
	}

	@Override
	public void afterActiveElement(WebElement activeElement, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterActiveElement n/a");
	}

	@Override
	public void beforeAlert(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeAlert n/a");
	}

	@Override
	public void afterAlert(Alert activeElement, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterAlert n/a");
	}

	@Override
	public void beforeMaximize(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeMaximize n/a");
	}

	@Override
	public void afterMaximize(WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterMaximize n/a");
	}

	@Override
	public void beforeFullscreen(WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeFullscreen n/a");
	}

	@Override
	public void afterFullscreen(WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterFullscreen n/a");
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		if (isDebugMode)
			System.out.println("beforeScript n/a");
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		if (isDebugMode)
			System.out.println("afterScript n/a");
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		if (isDebugMode)
			System.out.println("onException n/a");
	}
}
