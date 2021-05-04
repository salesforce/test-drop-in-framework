/**
 * 
 */
package com.salesforce.drillbit.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

import com.salesforce.selenium.support.event.Step;
import com.salesforce.selenium.support.event.Step.Cmd;
import com.salesforce.selenium.support.event.Step.Type;
import com.salesforce.selenium.support.event.EventListener;

/**
 * @author gneumann
 *
 */
public class EventDispatcher {	/**
	 * Properties file name: {@value} This file is expected to reside in the
	 * project's root directory.
	 */
	public static final String PROPERTIES_FILENAME = "eventfiringwebdriver.properties";
	/**
	 * Property key for locator of password field: {@value}
	 * <p>
	 * If this locator is found, the log files will only show '********' for the
	 * password value. The matching expression is using <code>
	 * locator.contains(passwordLocatorValue)
	 * </code>
	 * <p>
	 * If this key is not set, the default value is "password".
	 */
	public static final String CONFIG_PASSWORD_MASK = "password.locator";

	private static Properties properties;

	private final List<EventListener> eventListeners = new ArrayList<>();
	private Step currentStep = null;
	private int stepNumber = 1;

	
	public EventDispatcher() {
		eventListeners.add(new FullJSONLogger());
	}

	public void beforeGet(String url) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		step.setParam1(url);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGet(step, url);
	}

	public void afterGet(String url) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		step.setParam1(url);
		for (EventListener listener : eventListeners)
			listener.afterGet(step, url);
	}

	public void beforeGetTitle() {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getTitle);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetTitle(step);
		
	}

	public void afterGetTitle(String title) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getTitle);
		step.setReturnValue(title);
		for (EventListener listener : eventListeners)
			listener.afterGetTitle(step, title);
		
	}

	public void beforeGetCurrentUrl() {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getCurrentUrl);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetCurrentUrl(step);
	}

	public void afterGetCurrentUrl(String url) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		step.setReturnValue(url);
		for (EventListener listener : eventListeners)
			listener.afterGetCurrentUrl(step, url);
	}

	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
		// TODO Auto-generated method stub
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		step.setParam1(url);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGet(step, url);
		
	}

	public <X> void afterGetScreenshotAs(OutputType<X> target) {
		// TODO Auto-generated method stub
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		step.setParam1(url);
		for (EventListener listener : eventListeners)
			listener.afterGet(step, url);
		
	}

	public void beforeFindElementsByWebDriver(By by) {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.findElementsByWebDriver);
		step.setParam1(Step.getLocatorFromBy(by));
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeFindElementsByWebDriver(step, by);
	}

	public void afterFindElementsByWebDriver(List<WebElement> elements, By by) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.findElementsByWebDriver);
		step.setParam1(Step.getLocatorFromBy(by));
		if (elements.size() > 0) {
			if (elements.size() == 1)
				step.setReturnValue(Step.getLocatorFromWebElement(elements.get(0)));
			else
				step.setReturnValue(Step.getLocatorFromWebElement(elements.get(0)) + " and "
						+ (elements.size() - 1) + " more");				
		}
		step.setReturnObject(elements);
		for (EventListener listener : eventListeners)
			listener.afterFindElementsByWebDriver(step, elements, by);
	}

	public void beforeFindElementByWebDriver(By by) {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.findElementByWebDriver);
		step.setParam1(Step.getLocatorFromBy(by));
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeFindElementByWebDriver(step, by);
	}

	public void afterFindElementByWebDriver(WebElement element, By by) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.findElementByWebDriver);
		step.setParam1(Step.getLocatorFromBy(by));
		step.setReturnValue(Step.getLocatorFromWebElement(element));
		step.setReturnObject(element);
		for (EventListener listener : eventListeners)
			listener.afterFindElementByWebDriver(step, element, by);
	}

	public void beforeGetPageSource() {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getPageSource);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetPageSource(step);
	}

	public void afterGetPageSource(String source) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getPageSource);
		step.setReturnValue(source);
		for (EventListener listener : eventListeners)
			listener.afterGet(step, source);		
	}

	public void beforeClose() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.close);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeClose(step);
	}

	public void afterClose() {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		for (EventListener listener : eventListeners)
			listener.afterClose(step);
	}

	public void beforeQuit() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.quit);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeQuit(step);
	}

	public void afterQuit() {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.quit);
		for (EventListener listener : eventListeners)
			listener.afterQuit(step);
		closeListeners();
	}

	public void beforeGetWindowHandles() {
		// TODO Auto-generated method stub
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetWindowHandles(step);
		
	}

	public void afterGetWindowHandles(Set<String> handles) {
		// TODO Auto-generated method stub
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		step.setReturnValue(url);
		for (EventListener listener : eventListeners)
			listener.afterGet(step, url);
		
	}

	public void beforeGetWindowHandle() {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getWindowHandle);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetWindowHandle(step);
		
	}

	public void afterGetWindowHandle(String handle) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getWindowHandle);
		step.setReturnValue(handle);
		for (EventListener listener : eventListeners)
			listener.afterGetWindowHandle(step, handle);
		
	}

	public void beforeExecuteScript(String script, Object... args) {
		// TODO Auto-generated method stub
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		step.setParam1(url);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGet(step, url);
		
	}

	public void afterExecuteScript(String script, Object... args) {
		// TODO Auto-generated method stub
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		step.setParam1(url);
		for (EventListener listener : eventListeners)
			listener.afterGet(step, url);
		
	}

	public void beforeExecuteAsyncScript(String script, Object... args) {
		// TODO Auto-generated method stub
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		step.setParam1(url);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGet(step, url);
		
	}

	public void afterExecuteAsyncScript(String script, Object... args) {
		// TODO Auto-generated method stub
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		step.setParam1(url);
		for (EventListener listener : eventListeners)
			listener.afterGet(step, url);
		
	}

	// addCookie
	// deleteCookieNamed
	// deleteCookie
	// deleteAllCookies
	// getCookies
	// getCookieNamed

	// getAvailableEngines
	// getActiveEngine
	// isActivated
	// deactivate
	// activateEngine

	public void beforeImplicitlyWait(long time, TimeUnit unit) {
		// TODO Auto-generated method stub
		
	}

	public void afterImplicitlyWait(long time, TimeUnit unit) {
		// TODO Auto-generated method stub
		
	}

	public void beforeSetScriptTimeout(long time, TimeUnit unit) {
		// TODO Auto-generated method stub
		
	}

	public void afterSetScriptTimeout(long time, TimeUnit unit) {
		// TODO Auto-generated method stub
		
	}

	public void beforePageLoadTimeout(long time, TimeUnit unit) {
		// TODO Auto-generated method stub
		
	}

	public void afterPageLoadTimeout(long time, TimeUnit unit) {
		// TODO Auto-generated method stub
		
	}

	public void beforeSetSize(Dimension targetSize) {
		// TODO Auto-generated method stub
		
	}

	public void afterSetSize(Dimension targetSize) {
		// TODO Auto-generated method stub
		
	}

	public void beforeSetPosition(Point targetPosition) {
		// TODO Auto-generated method stub
		
	}

	public void afterSetPosition(Point targetPosition) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetSizeByWindow() {
		// TODO Auto-generated method stub
		
	}

	public void afterGetSizeByWindow(Dimension targetSize) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetPosition() {
		// TODO Auto-generated method stub
		
	}

	public void afterGetPosition(Point targetPosition) {
		// TODO Auto-generated method stub
		
	}

	public void beforeMaximize() {
		// TODO Auto-generated method stub
		
	}

	public void afterMaximize() {
		// TODO Auto-generated method stub
		
	}

	public void beforeFullscreen() {
		// TODO Auto-generated method stub
		
	}

	public void afterFullscreen() {
		// TODO Auto-generated method stub
		
	}

	public void beforeBack() {
		// TODO Auto-generated method stub
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		step.setParam1(url);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGet(step, url);
		
	}

	public void afterBack() {
		// TODO Auto-generated method stub
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		step.setParam1(url);
		for (EventListener listener : eventListeners)
			listener.afterGet(step, url);
		
	}

	public void beforeForward() {
		// TODO Auto-generated method stub
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		step.setParam1(url);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGet(step, url);
		
	}

	public void afterForward() {
		// TODO Auto-generated method stub
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		step.setParam1(url);
		for (EventListener listener : eventListeners)
			listener.afterGet(step, url);
		
	}

	public void beforeTo(String url) {
		// TODO Auto-generated method stub
		
	}

	public void afterTo(String url) {
		// TODO Auto-generated method stub
		
	}

	public void beforeToUrl(URL url) {
		// TODO Auto-generated method stub
		
	}

	public void afterToUrl(URL url) {
		// TODO Auto-generated method stub
		
	}

	public void beforeRefresh() {
		// TODO Auto-generated method stub
		
	}

	public void afterRefresh() {
		// TODO Auto-generated method stub
		
	}

	public void beforeFrameByIndex(int frameIndex) {
		// TODO Auto-generated method stub
		
	}

	public void afterFrameByIndex(int frameIndex) {
		// TODO Auto-generated method stub
		
	}

	public void beforeFrameByName(String frameName) {
		// TODO Auto-generated method stub
		
	}

	public void afterFrameByName(String frameName) {
		// TODO Auto-generated method stub
		
	}

	public void beforeFrameByElement(WebElement frameElement) {
		// TODO Auto-generated method stub
		
	}

	public void afterFrameByElement(WebElement frameElement) {
		// TODO Auto-generated method stub
		
	}

	public void beforeParentFrame() {
		// TODO Auto-generated method stub
		
	}

	public void afterParentFrame() {
		// TODO Auto-generated method stub
		
	}

	public void beforeWindow(String windowName) {
		// TODO Auto-generated method stub
		
	}

	public void afterWindow(String windowName) {
		// TODO Auto-generated method stub
		
	}

	public void beforeDefaultContent() {
		// TODO Auto-generated method stub
		
	}

	public void afterDefaultContent() {
		// TODO Auto-generated method stub
		
	}
	
	public void beforeActiveElement() {
		// TODO Auto-generated method stub
		
	}

	public void afterActiveElement(WebElement activeElement) {
		// TODO Auto-generated method stub
		
	}

	public void beforeAlert() {
		// TODO Auto-generated method stub
		
	}

	public void afterAlert(Alert alert) {
		// TODO Auto-generated method stub
		
	}

	// dismiss
	// accept
	// getTextByAlert
	// sendKeysByAlert

	/* End of methods provided by RemoteWebDriver and its inner classes */

	/* Begin of methods provided by RemoteWebElement class */
	
	public void beforeClick(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterClick(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeSubmit(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterSubmit(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeSendKeysByElement(WebElement element, CharSequence... keysToSend) {
		// TODO Auto-generated method stub
		
	}

	public void afterSendKeysByElement(WebElement element, CharSequence... keysToSend) {
		// TODO Auto-generated method stub
		
	}

	public void beforeClear(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterClear(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetTagName(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterGetTagName(String tagName, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetAttribute(String name, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterGetAttribute(String value, String name, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeIsSelected(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterIsSelected(boolean isSelected, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeIsEnabled(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterIsEnabled(boolean isEnabled, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetText(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterGetText(String text, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetCssValue(String propertyName, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterGetCssValue(String propertyName, String value, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeFindElementsByElement(By by, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterFindElementsByElement(List<WebElement> returnedElements, By by, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeFindElementByElement(By by, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterFindElementByElement(WebElement returnedElement, By by, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeIsDisplayed(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterIsDisplayed(boolean isDisplayed, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetLocation(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterGetLocation(Point point, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetSizeByElement(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterGetSizeByElement(Dimension dimension, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetRect(WebElement element) {
		// TODO Auto-generated method stub
		
	}

	public void afterGetRect(Rectangle rectangle, WebElement element) {
		// TODO Auto-generated method stub
		
	}

	// getCoordinates

	public <X> void beforeGetScreenshotAsByElement(OutputType<X> target) {
		// TODO Auto-generated method stub
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		step.setParam1(url);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGet(step, url);
		
	}

	public <X> void afterGetScreenshotAsByElement(OutputType<X> target) {
		// TODO Auto-generated method stub
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		step.setParam1(url);
		for (EventListener listener : eventListeners)
			listener.afterGet(step, url);
		
	}
	
	/* End of methods provided by RemoteWebElement class */

	/* Begin of methods provided by RemoteKeyboard class */

	public void beforeSendKeysByKeyboard(CharSequence... keysToSend) {
		// TODO Auto-generated method stub
		
	}

	public void afterSendKeysByKeyboard(CharSequence... keysToSend) {
		// TODO Auto-generated method stub
		
	}

	public void beforePressKey(CharSequence... keyToPress) {
		// TODO Auto-generated method stub
		
	}

	public void afterPressKey(CharSequence... keyToPress) {
		// TODO Auto-generated method stub
		
	}

	public void beforeReleaseKey(CharSequence... keyToRelease) {
		// TODO Auto-generated method stub
		
	}

	public void afterReleaseKey(CharSequence... keyToRelease) {
		// TODO Auto-generated method stub
		
	}
	
	/* End of methods provided by RemoteKeyboard class */

	/* Begin of methods provided by RemoteMouse class */

	public void beforeClickByMouse(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void afterClickByMouse(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void beforeContextClick(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void afterContextClick(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void beforeDoubleClick(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void afterDoubleClick(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void beforeMouseDown(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void afterMouseDown(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void beforeMouseUp(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void afterMouseUp(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void beforeMouseMove(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void afterMouseMove(Coordinates where) {
		// TODO Auto-generated method stub
		
	}

	public void beforeMouseMove(Coordinates where, long xOffset, long yOffset) {
		// TODO Auto-generated method stub
		
	}

	public void afterMouseMove(Coordinates where, long xOffset, long yOffset) {
		// TODO Auto-generated method stub
		
	}

	public void onException(Cmd cmd, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

	private void closeListeners() {
		for (EventListener listener : eventListeners)
			listener.closeListener();
	}
}
