/**
 * 
 */
package com.salesforce.drillbit.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
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
	private int stepNumber = 0;

	
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
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.get);
		step.setReturnValue(url);
		for (EventListener listener : eventListeners)
			listener.afterGetCurrentUrl(step, url);
	}

	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getScreenshotAs);
		step.setParam1(target.toString());
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetScreenshotAs(step, target);
	}

	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getScreenshotAs);
		step.setParam1(target.toString());
		step.setReturnObject(screenshot);
		for (EventListener listener : eventListeners)
			listener.afterGetScreenshotAs(step, target, screenshot);
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
			listener.afterGetPageSource(step, source);		
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
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getWindowHandles);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetWindowHandles(step);
	}

	public void afterGetWindowHandles(Set<String> handles) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getWindowHandles);
		step.setReturnObject(handles);
		for (EventListener listener : eventListeners)
			listener.afterGetWindowHandles(step, handles);
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

	public void beforeExecuteScript(String script, Map<String, ?> params) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.executeScript);
		step.setParam1(script);
		if (!params.isEmpty()) {
			Set<String> arguments = params.keySet();
			StringBuilder b = new StringBuilder();
			for (String arg : arguments)
				b.append(arg).append(",");
			String param2 = b.toString();
			// drop the trailing ','
			step.setParam2(param2.substring(0, param2.length()-1));
		}
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeExecuteScript(step, script, params);
	}

	public void afterExecuteScript(String script, Map<String, ?> params, Object result) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.executeScript);
		step.setParam1(script);
		if (!params.isEmpty()) {
			Set<String> arguments = params.keySet();
			StringBuilder b = new StringBuilder();
			for (String arg : arguments)
				b.append(arg).append(",");
			String param2 = b.toString();
			// drop the trailing ','
			step.setParam2(param2.substring(0, param2.length()-1));
		}
		step.setReturnObject(result);
		for (EventListener listener : eventListeners)
			listener.afterExecuteScript(step, script, params, result);
	}

	public void beforeExecuteAsyncScript(String script, Map<String, ?> params) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.executeAsyncScript);
		step.setParam1(script);
		if (!params.isEmpty()) {
			Set<String> arguments = params.keySet();
			StringBuilder b = new StringBuilder();
			for (String arg : arguments)
				b.append(arg).append(",");
			String param2 = b.toString();
			// drop the trailing ','
			step.setParam2(param2.substring(0, param2.length()-1));
		}
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeExecuteAsyncScript(step, script, params);
	}

	public void afterExecuteAsyncScript(String script, Map<String, ?> params, Object result) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.executeAsyncScript);
		step.setParam1(script);
		if (!params.isEmpty()) {
			Set<String> arguments = params.keySet();
			StringBuilder b = new StringBuilder();
			for (String arg : arguments)
				b.append(arg).append(",");
			String param2 = b.toString();
			// drop the trailing ','
			step.setParam2(param2.substring(0, param2.length()-1));
		}
		step.setReturnObject(result);
		for (EventListener listener : eventListeners)
			listener.afterExecuteAsyncScript(step, script, params, result);
	}

	public void beforeAddCookie(Cookie cookie) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.addCookie);
		step.setParam1(cookie.toString());
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeAddCookie(step, cookie);
	}

	public void afterAddCookie(Cookie cookie) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.addCookie);
		step.setParam1(cookie.toString());
		for (EventListener listener : eventListeners)
			listener.afterAddCookie(step, cookie);
	}

	public void beforeDeleteCookieNamed(String name) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.deleteCookieNamed);
		step.setParam1(name);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeDeleteCookieNamed(step, name);
	}

	public void afterDeleteCookieNamed(String name) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.deleteCookieNamed);
		step.setParam1(name);
		for (EventListener listener : eventListeners)
			listener.afterDeleteCookieNamed(step, name);
	}

	public void beforeDeleteCookie(Cookie cookie) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.deleteCookie);
		step.setParam1(cookie.toString());
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeDeleteCookie(step, cookie);
	}

	public void afterDeleteCookie(Cookie cookie) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.deleteCookie);
		step.setParam1(cookie.toString());
		for (EventListener listener : eventListeners)
			listener.afterDeleteCookie(step, cookie);
	}

	public void beforeDeleteAllCookies() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.deleteAllCookies);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeDeleteAllCookies(step);
	}

	public void afterDeleteAllCookies() {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.deleteAllCookies);
		for (EventListener listener : eventListeners)
			listener.afterDeleteAllCookies(step);
	}

	public void beforeGetCookies() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.getCookies);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetCookies(step);
	}

	public void afterGetCookies(Set<Cookie> cookies) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.getCookies);
		step.setReturnObject(cookies);
		for (EventListener listener : eventListeners)
			listener.afterGetCookies(step, cookies);
	}

	public void beforeGetCookieNamed(String name) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.getCookieNamed);
		step.setParam1(name);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetCookieNamed(step, name);
	}

	public void afterGetCookieNamed(String name, Cookie cookie) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.getCookieNamed);
		step.setParam1(name);
		step.setReturnObject(cookie);
		for (EventListener listener : eventListeners)
			listener.afterGetCookieNamed(step, name, cookie);
	}

	public void beforeGetAvailableEngines() {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getAvailableEngines);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetAvailableEngines(step);
	}

	public void afterGetAvailableEngines(List<String> engines) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getAvailableEngines);
		step.setReturnObject(engines);
		for (EventListener listener : eventListeners)
			listener.afterGetAvailableEngines(step, engines);
	}

	public void beforeGetActiveEngine() {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getActiveEngine);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetActiveEngine(step);
	}

	public void afterGetActiveEngine(String engine) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getActiveEngine);
		step.setReturnValue(engine);
		for (EventListener listener : eventListeners)
			listener.afterGetActiveEngine(step, engine);
	}

	public void beforeIsActivated() {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.isActivated);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeIsActivated(step);
	}

	public void afterIsActivated(boolean isActivated) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.isActivated);
		step.setReturnValue(Boolean.toString(isActivated));
		for (EventListener listener : eventListeners)
			listener.afterIsActivated(step, isActivated);
	}

	public void beforeDeactivate() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.deactivate);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeDeactivate(step);
	}

	public void afterDeactivate() {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.deactivate);
		for (EventListener listener : eventListeners)
			listener.afterDeactivate(step);
	}

	public void beforeActivateEngine(String engine) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.activateEngine);
		step.setParam1(engine);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeActivateEngine(step, engine);
	}

	public void afterActivateEngine(String engine) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.activateEngine);
		step.setParam1(engine);
		for (EventListener listener : eventListeners)
			listener.afterActivateEngine(step, engine);
	}

	public void beforeImplicitlyWait(long time, TimeUnit unit) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.implicitlyWait);
		step.setParam1(Long.toString(time));
		step.setParam2(unit.name());
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeImplicitlyWait(step, time, unit);
	}

	public void afterImplicitlyWait(long time, TimeUnit unit) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.implicitlyWait);
		step.setParam1(Long.toString(time));
		step.setParam2(unit.name());
		for (EventListener listener : eventListeners)
			listener.afterImplicitlyWait(step, time, unit);
	}

	public void beforeSetScriptTimeout(long time, TimeUnit unit) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.setScriptTimeout);
		step.setParam1(Long.toString(time));
		step.setParam2(unit.name());
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeSetScriptTimeout(step, time, unit);
	}

	public void afterSetScriptTimeout(long time, TimeUnit unit) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.setScriptTimeout);
		step.setParam1(Long.toString(time));
		step.setParam2(unit.name());
		for (EventListener listener : eventListeners)
			listener.afterSetScriptTimeout(step, time, unit);
	}

	public void beforePageLoadTimeout(long time, TimeUnit unit) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.pageLoadTimeout);
		step.setParam1(Long.toString(time));
		step.setParam2(unit.name());
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforePageLoadTimeout(step, time, unit);
	}

	public void afterPageLoadTimeout(long time, TimeUnit unit) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.pageLoadTimeout);
		step.setParam1(Long.toString(time));
		step.setParam2(unit.name());
		for (EventListener listener : eventListeners)
			listener.afterPageLoadTimeout(step, time, unit);
	}

	public void beforeSetSizeByWindow(Dimension targetSize) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.setSizeByWindow);
		step.setParam1(targetSize.getHeight() + "x" + targetSize.getWidth());
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeSetSizeByWindow(step, targetSize);
	}

	public void afterSetSizeByWindow(Dimension targetSize) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.setSizeByWindow);
		step.setParam1(targetSize.getHeight() + "x" + targetSize.getWidth());
		for (EventListener listener : eventListeners)
			listener.afterSetSizeByWindow(step, targetSize);
	}

	public void beforeSetPosition(Point targetPosition) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.setPosition);
		step.setParam1("x:" + targetPosition.x + ",y:" + targetPosition.y);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeSetPosition(step, targetPosition);
	}

	public void afterSetPosition(Point targetPosition) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.setPosition);
		step.setParam1("x:" + targetPosition.x + ",y:" + targetPosition.y);
		for (EventListener listener : eventListeners)
			listener.afterSetPosition(step, targetPosition);
	}

	public void beforeGetSizeByWindow() {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getSizeByWindow);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetSizeByWindow(step);
	}

	public void afterGetSizeByWindow(Dimension targetSize) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getSizeByWindow);
		step.setReturnObject(targetSize);
		for (EventListener listener : eventListeners)
			listener.afterGetSizeByWindow(step, targetSize);
	}

	public void beforeGetPosition() {
		Step step = new Step(Type.BeforeGather, stepNumber, Cmd.getPosition);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeGetPosition(step);
	}

	public void afterGetPosition(Point targetPosition) {
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getPosition);
		step.setReturnObject(targetPosition);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.afterGetPosition(step, targetPosition);
	}

	public void beforeMaximize() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.maximize);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeMaximize(step);
	}

	public void afterMaximize() {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.maximize);
		for (EventListener listener : eventListeners)
			listener.afterMaximize(step);
	}

	public void beforeFullscreen() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.fullscreen);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeFullscreen(step);
	}

	public void afterFullscreen() {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.fullscreen);
		for (EventListener listener : eventListeners)
			listener.afterFullscreen(step);
	}

	public void beforeBack() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.back);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeBack(step);
	}

	public void afterBack() {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.back);
		for (EventListener listener : eventListeners)
			listener.afterBack(step);
	}

	public void beforeForward() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.forward);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeForward(step);
	}

	public void afterForward() {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.forward);
		for (EventListener listener : eventListeners)
			listener.afterForward(step);
	}

	public void beforeTo(String url) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.to);
		step.setParam1(url);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeTo(step, url);
	}

	public void afterTo(String url) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.to);
		step.setParam1(url);
		for (EventListener listener : eventListeners)
			listener.afterTo(step, url);
	}

	public void beforeToUrl(URL url) {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.toUrl);
		step.setParam1(url.toString());
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeToUrl(step, url);
	}

	public void afterToUrl(URL url) {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.toUrl);
		step.setParam1(url.toString());
		for (EventListener listener : eventListeners)
			listener.afterToUrl(step, url);
	}

	public void beforeRefresh() {
		Step step = new Step(Type.BeforeAction, stepNumber, Cmd.refresh);
		currentStep = step;
		for (EventListener listener : eventListeners)
			listener.beforeRefresh(step);
	}

	public void afterRefresh() {
		Step step = new Step(Type.AfterAction, stepNumber++, Cmd.refresh);
		for (EventListener listener : eventListeners)
			listener.afterRefresh(step);
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
