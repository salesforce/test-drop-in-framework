package com.salesforce.selenium.support.event;

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

import org.openqa.selenium.Alert;
import org.openqa.selenium.Beta;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.logging.Logs;

import com.salesforce.selenium.support.event.Step.Cmd;
import com.salesforce.selenium.support.event.Step.Type;
import com.salesforce.selenium.support.event.internal.EventFiringKeyboard;
import com.salesforce.selenium.support.event.internal.EventFiringMouse;
import com.salesforce.selenium.support.event.internal.EventFiringTouch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * A wrapper around an arbitrary {@link WebDriver} instance which supports
 * registering of a {@link WebDriverEventListener}, e.g. for logging
 * purposes.
 */
public class EventFiringWebDriver
		implements WebDriver, JavascriptExecutor, TakesScreenshot, WrapsDriver, HasInputDevices, HasTouchScreen {

	private final WebDriver driver;
	private final WebDriverEventListener defaultEventListener;
	private final List<WebDriverEventListener> eventListeners = new ArrayList<>();
	private final WebDriverEventListener dispatcher = (WebDriverEventListener) Proxy.newProxyInstance(
			WebDriverEventListener.class.getClassLoader(), new Class[] { WebDriverEventListener.class },
			new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					try {
						method.invoke(defaultEventListener, args);
						for (WebDriverEventListener eventListener : eventListeners) {
							method.invoke(eventListener, args);
						}
						return null;
					} catch (InvocationTargetException e) {
						throw e.getTargetException();
					}
				}
			});

	private int stepNumber = 1;

	public EventFiringWebDriver(final WebDriver driver, String testName) {
		Class<?>[] allInterfaces = extractInterfaces(driver);

		this.driver = (WebDriver) Proxy.newProxyInstance(WebDriverEventListener.class.getClassLoader(), allInterfaces,
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("getWrappedDriver".equals(method.getName())) {
							return driver;
						}

						try {
							return method.invoke(driver, args);
						} catch (InvocationTargetException e) {
							Step step = new Step(Type.Exception, stepNumber, Cmd.testFailure);
							step.setIssue(e);
							dispatcher.onException(step, Cmd.testFailure, e.getTargetException());
							throw e.getTargetException();
						}
					}
				});
		// standard listener which writes all events to JSON files
		defaultEventListener = new FullJSONLogger(testName);
	}

	private Class<?>[] extractInterfaces(Object object) {
		Set<Class<?>> allInterfaces = new HashSet<>();
		allInterfaces.add(WrapsDriver.class);
		if (object instanceof WebElement) {
			allInterfaces.add(WrapsElement.class);
		}
		extractInterfaces(allInterfaces, object.getClass());

		return allInterfaces.toArray(new Class<?>[allInterfaces.size()]);
	}

	private void extractInterfaces(Set<Class<?>> addTo, Class<?> clazz) {
		if (Object.class.equals(clazz)) {
			return; // Done
		}

		Class<?>[] classes = clazz.getInterfaces();
		addTo.addAll(Arrays.asList(classes));
		extractInterfaces(addTo, clazz.getSuperclass());
	}

	/**
	 * @param eventListener
	 *            the event listener to register
	 * @return this for method chaining.
	 */
	public EventFiringWebDriver register(WebDriverEventListener eventListener) {
		eventListeners.add(eventListener);
		return this;
	}

	/**
	 * @param eventListener
	 *            the event listener to unregister
	 * @return this for method chaining.
	 */
	public EventFiringWebDriver unregister(WebDriverEventListener eventListener) {
		eventListeners.remove(eventListener);
		return this;
	}

	public WebDriver getWrappedDriver() {
		if (driver instanceof WrapsDriver) {
			return ((WrapsDriver) driver).getWrappedDriver();
		} else {
			return driver;
		}
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/

	public void close() {
		dispatcher.beforeClose(new Step(Type.BeforeAction, stepNumber, Cmd.close));
		driver.close();
		dispatcher.afterClose(new Step(Type.AfterAction, stepNumber++, Cmd.close));
	}

	public WebElement findElement(By by) {
		Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.findElementByWebDriver);
		stepBefore.setParam1(Step.getLocatorFromBy(by));
		dispatcher.beforeFindElementByWebDriver(stepBefore, by);

		WebElement returnedElement = driver.findElement(by);

		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.findElementByWebDriver);
		stepAfter.setParam1(Step.getLocatorFromBy(by));
		stepAfter.setReturnObject(returnedElement);
		dispatcher.afterFindElementByWebDriver(stepAfter, returnedElement, by);
		return createWebElement(returnedElement);
	}

	public List<WebElement> findElements(By by) {
		Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.findElementsByWebDriver);
		stepBefore.setParam1(Step.getLocatorFromBy(by));
		dispatcher.beforeFindElementByWebDriver(stepBefore, by);

		List<WebElement> returnedElements = driver.findElements(by);

		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.findElementsByWebDriver);
		stepAfter.setParam1(Step.getLocatorFromBy(by));
		stepAfter.setReturnObject(returnedElements);
		dispatcher.afterFindElementsByWebDriver(stepAfter, returnedElements, by);

		List<WebElement> returnedAndWrappedElements = new ArrayList<>(returnedElements.size());
		for (WebElement element : returnedElements) {
			returnedAndWrappedElements.add(createWebElement(element));
		}
		return returnedAndWrappedElements;
	}

	public void get(String url) {
		Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		stepBefore.setParam1(url);
		dispatcher.beforeGet(stepBefore, url);

		driver.get(url);

		Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		stepAfter.setParam1(url);
		dispatcher.afterGet(stepAfter, url);
	}

	public String getCurrentUrl() {
		dispatcher.beforeGetCurrentUrl(new Step(Type.BeforeGather, stepNumber, Cmd.getCurrentUrl));

		String url = driver.getCurrentUrl();

		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getCurrentUrl);
		stepAfter.setReturnValue(url);
		dispatcher.afterGetCurrentUrl(stepAfter, url);
		return url; 
	}

	public String getTitle() {
		dispatcher.beforeGetTitle(new Step(Type.BeforeGather, stepNumber, Cmd.getTitle));

		String title = driver.getTitle();
		
		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getTitle);
		step.setReturnValue(title);
		dispatcher.afterGetTitle(step, title);
		return title;
	}

	public String getWindowHandle() {
		dispatcher.beforeGetWindowHandle(new Step(Type.BeforeGather, stepNumber, Cmd.getWindowHandle));

		String handle = driver.getWindowHandle();

		Step step = new Step(Type.AfterGather, stepNumber, Cmd.getWindowHandle);
		step.setReturnValue(handle);
		dispatcher.afterGetWindowHandle(step, handle);
		return handle;
	}

	public Set<String> getWindowHandles() {
		dispatcher.beforeGetWindowHandles(new Step(Type.BeforeGather, stepNumber, Cmd.getWindowHandles));

		Set<String> handles = driver.getWindowHandles();

		String handlesAsOneString = "n/a";
		StringBuffer sb = new StringBuffer();
		if (handles != null) {
			for (String h : handles) {
				sb.append(h).append(" ");
			}
			handlesAsOneString = sb.toString();
		}

		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getWindowHandles);
		stepAfter.setReturnValue(handlesAsOneString);
		dispatcher.afterGetWindowHandles(stepAfter, handles);
		return handles;
	}

	public void quit() {
		dispatcher.beforeQuit(new Step(Type.BeforeAction, stepNumber, Cmd.quit));
		driver.quit();
		dispatcher.afterQuit(new Step(Type.AfterAction, stepNumber++, Cmd.quit));
		closeListeners();
	}

	// TODO
	public String getPageSource() {
		String source = driver.getPageSource();
		return source;
	}

	// TODO
	public Object executeScript(String script, Object... args) {
		if (driver instanceof JavascriptExecutor) {
			Object[] usedArgs = unpackWrappedArgs(args);
			Object result = ((JavascriptExecutor) driver).executeScript(script, usedArgs);
			return result;
		}
		throw new UnsupportedOperationException("Underlying driver instance does not support executing javascript");
	}

	public Object executeAsyncScript(String script, Object... args) {
		if (driver instanceof JavascriptExecutor) {
			Object[] usedArgs = unpackWrappedArgs(args);
			Object result = ((JavascriptExecutor) driver).executeAsyncScript(script, usedArgs);
			return result;
		}
		throw new UnsupportedOperationException("Underlying driver instance does not support executing javascript");
	}

	private Object[] unpackWrappedArgs(Object... args) {
		// Walk the args: the various drivers expect unpacked versions of the elements
		Object[] usedArgs = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			usedArgs[i] = unpackWrappedElement(args[i]);
		}
		return usedArgs;
	}

	private Object unpackWrappedElement(Object arg) {
		if (arg instanceof List<?>) {
			List<?> aList = (List<?>) arg;
			List<Object> toReturn = new ArrayList<>();
			for (Object anAList : aList) {
				toReturn.add(unpackWrappedElement(anAList));
			}
			return toReturn;
		} else if (arg instanceof Map<?, ?>) {
			Map<?, ?> aMap = (Map<?, ?>) arg;
			Map<Object, Object> toReturn = new HashMap<>();
			for (Object key : aMap.keySet()) {
				toReturn.put(key, unpackWrappedElement(aMap.get(key)));
			}
			return toReturn;
		} else if (arg instanceof EventFiringWebElement) {
			return ((EventFiringWebElement) arg).getWrappedElement();
		} else {
			return arg;
		}
	}

	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		if (driver instanceof TakesScreenshot) {
			return ((TakesScreenshot) driver).getScreenshotAs(target);
		}

		throw new UnsupportedOperationException("Underlying driver instance does not support taking screenshots");
	}

	public TargetLocator switchTo() {
		return new EventFiringTargetLocator(driver.switchTo());
	}

	public Navigation navigate() {
		return new EventFiringNavigation(driver.navigate());
	}

	public Options manage() {
		return new EventFiringOptions(driver.manage());
	}

	private WebElement createWebElement(WebElement from) {
		return new EventFiringWebElement(from);
	}

	public Keyboard getKeyboard() {
		if (driver instanceof HasInputDevices) {
			return new EventFiringKeyboard(driver, dispatcher);
		} else {
			throw new UnsupportedOperationException(
					"Underlying driver does not implement advanced" + " user interactions yet.");
		}
	}

	public Mouse getMouse() {
		if (driver instanceof HasInputDevices) {
			return new EventFiringMouse(driver, dispatcher);
		} else {
			throw new UnsupportedOperationException(
					"Underlying driver does not implement advanced" + " user interactions yet.");
		}
	}

	public TouchScreen getTouch() {
		if (driver instanceof HasTouchScreen) {
			return new EventFiringTouch(driver, dispatcher);
		} else {
			throw new UnsupportedOperationException(
					"Underlying driver does not implement advanced" + " user interactions yet.");
		}
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	private class EventFiringWebElement implements WebElement, WrapsElement, WrapsDriver, Locatable {

		private final WebElement element;
		private final WebElement underlyingElement;

		private EventFiringWebElement(final WebElement element) {
			this.element = (WebElement) Proxy.newProxyInstance(WebDriverEventListener.class.getClassLoader(),
					extractInterfaces(element), new InvocationHandler() {
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							if (method.getName().equals("getWrappedElement")) {
								return element;
							}
							try {
								return method.invoke(element, args);
							} catch (InvocationTargetException e) {
								Step step = new Step(Type.Exception, stepNumber, Cmd.testFailure);
								step.setIssue(e);
								dispatcher.onException(step, Cmd.testFailure, e.getTargetException());
								throw e.getTargetException();
							}
						}
					});
			this.underlyingElement = element;
		}

		public void click() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.click);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));

			dispatcher.beforeClick(stepBefore, element);

			element.click();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.click);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.afterClick(stepAfter, element);
		}

		public void clear() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.clear);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.beforeClear(stepBefore, element);

			element.clear();
			
			Step step = new Step(Type.AfterAction, stepNumber++, Cmd.clear);
			step.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.afterClear(step, element);
		}

		public WebElement findElement(By by) {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.findElementByElement);
			stepBefore.setParam1(Step.getLocatorFromBy(by));
			dispatcher.beforeFindElementByElement(stepBefore, by, element);

			WebElement returnedElement = element.findElement(by);

			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.findElementByElement);
			stepAfter.setParam1(Step.getLocatorFromBy(by));
			stepAfter.setReturnObject(returnedElement);
			dispatcher.afterFindElementByElement(stepAfter, returnedElement, by, element);
			return createWebElement(returnedElement);
		}

		public List<WebElement> findElements(By by) {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.findElementsByElement);
			stepBefore.setParam1(Step.getLocatorFromBy(by));
			dispatcher.beforeFindElementByElement(stepBefore, by, element);

			List<WebElement> returnedElements = element.findElements(by);

			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.findElementsByElement);
			stepAfter.setParam1(Step.getLocatorFromBy(by));
			stepAfter.setReturnObject(returnedElements);
			dispatcher.afterFindElementsByElement(stepAfter, returnedElements, by, element);

			List<WebElement> returnedAndWrappedElements = new ArrayList<>(returnedElements.size());
			for (WebElement element : returnedElements) {
				returnedAndWrappedElements.add(createWebElement(element));
			}
			return returnedAndWrappedElements;
		}

		public String getAttribute(String name) {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getAttribute);
			stepBefore.setParam1(name);
			dispatcher.beforeGetAttribute(stepBefore, name, element);

			String value = element.getAttribute(name);
			
			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getAttribute);
			stepAfter.setParam1(name);
			stepAfter.setReturnValue(value);
			dispatcher.afterGetAttribute(stepAfter, value, name, element);
			return value;
		}

		public String getCssValue(String propertyName) {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getCssValue);
			stepBefore.setParam1(propertyName);
			dispatcher.beforeGetCssValue(stepBefore, propertyName, element);

			String value = element.getCssValue(propertyName);
			
			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getCssValue);
			stepAfter.setParam1(propertyName);
			stepAfter.setReturnValue(value);
			dispatcher.afterGetCssValue(stepAfter, propertyName, value, element);
			return value;
		}

		public String getTagName() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getTagName);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.beforeGetTagName(stepBefore, element);

			String tagName = element.getTagName();

			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getTagName);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue(tagName);
			dispatcher.afterGetTagName(stepAfter, tagName, element);
			return tagName;
		}

		public String getText() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getText);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.beforeGetText(stepBefore, element);

			String text = element.getText();
			
			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getText);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue(text);
			dispatcher.afterGetText(stepAfter, text, element);
			return text;
		}

		public boolean isDisplayed() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.isDisplayed);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.beforeIsDisplayed(stepBefore, element);

			boolean isDisplayed = element.isDisplayed();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.isDisplayed);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue("" + isDisplayed);
			dispatcher.afterIsDisplayed(stepAfter, isDisplayed, element);
			return isDisplayed;
		}

		public boolean isEnabled() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.isEnabled);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.beforeIsEnabled(stepBefore, element);

			boolean isEnabled = element.isEnabled();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.isEnabled);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue("" + isEnabled);
			dispatcher.afterIsEnabled(stepAfter, isEnabled, element);
			return isEnabled;
		}

		public boolean isSelected() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.isSelected);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.beforeIsSelected(stepBefore, element);

			boolean isSelected = element.isSelected();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.isSelected);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue("" + isSelected);
			dispatcher.afterIsSelected(stepAfter, isSelected, element);
			return isSelected;
		}

		// TODO add to WebDriverEventListener interface
		public Point getLocation() {
			Point point = element.getLocation();
			return point;
		}

		// TODO add to WebDriverEventListener interface
		public Dimension getSize() {
			Dimension dimension = element.getSize();
			return dimension;
		}

		// TODO add to WebDriverEventListener interface
		public Rectangle getRect() {
			Rectangle rect = element.getRect();
			return rect;
		}

		public void sendKeys(CharSequence... keysToSend) {
			StringBuffer buffer = new StringBuffer();
			if ((keysToSend != null) && (keysToSend.length > 0)) {
				for (int i = 0; i < keysToSend.length; i++) {
					buffer.append(keysToSend[i]);
				}
			}
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.sendKeys);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setParam2(buffer.toString());
			dispatcher.beforeSendKeys(stepBefore, element, keysToSend);

			element.sendKeys(keysToSend);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.sendKeys);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setParam2(buffer.toString());
			dispatcher.afterSendKeys(stepAfter, element, keysToSend);
		}

		public void submit() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.submit);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.beforeSubmit(stepBefore, element);

			element.submit();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.submit);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			dispatcher.afterSubmit(stepAfter, element);
		}

		public WebElement getWrappedElement() {
			return underlyingElement;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof WebElement)) {
				return false;
			}

			WebElement other = (WebElement) obj;
			if (other instanceof WrapsElement) {
				other = ((WrapsElement) other).getWrappedElement();
			}

			return underlyingElement.equals(other);
		}

		@Override
		public int hashCode() {
			return underlyingElement.hashCode();
		}

		@Override
		public String toString() {
			return underlyingElement.toString();
		}

		public WebDriver getWrappedDriver() {
			return driver;
		}

		public Coordinates getCoordinates() {
			return ((Locatable) underlyingElement).getCoordinates();
		}

		public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
			return element.getScreenshotAs(outputType);
		}
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/

	private class EventFiringNavigation implements Navigation {

		private final WebDriver.Navigation navigation;

		EventFiringNavigation(Navigation navigation) {
			this.navigation = navigation;
		}

		public void back() {
			dispatcher.beforeBack(new Step(Type.BeforeAction, stepNumber, Cmd.back));

			navigation.back();

			dispatcher.afterBack(new Step(Type.AfterAction, stepNumber++, Cmd.back));
		}

		public void forward() {
			dispatcher.beforeForward(new Step(Type.BeforeAction, stepNumber, Cmd.forward));

			navigation.forward();

			dispatcher.afterForward(new Step(Type.AfterAction, stepNumber++, Cmd.forward));
		}

		public void refresh() {
			dispatcher.beforeRefresh(new Step(Type.BeforeAction, stepNumber, Cmd.refresh));

			navigation.refresh();

			dispatcher.afterRefresh(new Step(Type.AfterAction, stepNumber++, Cmd.refresh));
		}

		public void to(URL url) {
			to(String.valueOf(url));
		}

		public void to(String url) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.to);
			stepBefore.setParam1(url);

			navigation.to(url);

			Step step = new Step(Type.AfterAction, stepNumber++, Cmd.to);
			step.setParam1(url);
			dispatcher.afterTo(step, url);
		}
	}

	private class EventFiringOptions implements Options {

		private Options options;

		private EventFiringOptions(Options options) {
			this.options = options;
		}

		public Logs logs() {
			return options.logs();
		}

		public void addCookie(Cookie cookie) {
			options.addCookie(cookie);
		}

		public void deleteCookieNamed(String name) {
			options.deleteCookieNamed(name);
		}

		public void deleteCookie(Cookie cookie) {
			options.deleteCookie(cookie);
		}

		public void deleteAllCookies() {
			options.deleteAllCookies();
		}

		public Set<Cookie> getCookies() {
			Set<Cookie> cookies = options.getCookies(); 
			return cookies;
		}

		public Cookie getCookieNamed(String name) {
			Cookie cookie = options.getCookieNamed(name); 
			return cookie;
		}

		public Timeouts timeouts() {
			return new EventFiringTimeouts(options.timeouts());
		}

		public ImeHandler ime() {
			return options.ime();
		}

		@Beta
		public Window window() {
			return new EventFiringWindow(options.window());
		}
	}

	private class EventFiringTimeouts implements Timeouts {

		private final Timeouts timeouts;

		EventFiringTimeouts(Timeouts timeouts) {
			this.timeouts = timeouts;
		}

		public Timeouts implicitlyWait(long time, TimeUnit unit) {
			timeouts.implicitlyWait(time, unit);
			return this;
		}

		public Timeouts setScriptTimeout(long time, TimeUnit unit) {
			timeouts.setScriptTimeout(time, unit);
			return this;
		}

		public Timeouts pageLoadTimeout(long time, TimeUnit unit) {
			timeouts.pageLoadTimeout(time, unit);
			return this;
		}
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.TargetLocator object.
	 *---------------------------------------------------------------------------*/

	private class EventFiringTargetLocator implements TargetLocator {

		private TargetLocator targetLocator;

		private EventFiringTargetLocator(TargetLocator targetLocator) {
			this.targetLocator = targetLocator;
		}

		public WebElement activeElement() {
			dispatcher.beforeActiveElement(new Step(Type.BeforeAction, stepNumber, Cmd.activeElement));

			WebElement activeElement = targetLocator.activeElement();

			Step step = new Step(Type.AfterAction, stepNumber++, Cmd.activeElement);
			step.setReturnObject(activeElement);
			dispatcher.afterActiveElement(step, activeElement);
			return createWebElement(activeElement);
		}

		public Alert alert() {
			dispatcher.beforeAlert(new Step(Type.BeforeAction, stepNumber, Cmd.alert));

			Alert alert = targetLocator.alert();

			Step step = new Step(Type.AfterAction, stepNumber++, Cmd.alert);
			step.setReturnObject(alert);
			dispatcher.afterAlert(step, alert);
			return alert;
		}

		public WebDriver defaultContent() {
			dispatcher.beforeDefaultContent(new Step(Type.BeforeAction, stepNumber, Cmd.defaultContent));

			// TODO is this an EventFiringWebDriver instance?
			WebDriver frameDriver = targetLocator.defaultContent();

			dispatcher.afterDefaultContent(new Step(Type.AfterAction, stepNumber++, Cmd.defaultContent));
			return frameDriver;
		}

		public WebDriver frame(int frameIndex) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.frameByIndex);
			stepBefore.setParam1("" + frameIndex);
			dispatcher.beforeFrameByIndex(stepBefore, frameIndex);

			// TODO is this an EventFiringWebDriver instance?
			WebDriver frameDriver = targetLocator.frame(frameIndex);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.frameByIndex);
			stepAfter.setParam1("" + frameIndex);
			dispatcher.afterFrameByIndex(stepAfter, frameIndex);
			return frameDriver;
		}

		public WebDriver frame(String frameName) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.frameByName);
			stepBefore.setParam1(frameName);
			dispatcher.beforeFrameByName(stepBefore, frameName);

			// TODO is this an EventFiringWebDriver instance?
			WebDriver frameDriver = targetLocator.frame(frameName);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.frameByName);
			stepAfter.setParam1(frameName);
			dispatcher.afterFrameByName(stepAfter, frameName);
			return frameDriver;
		}

		public WebDriver frame(WebElement frameElement) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.frameByElement);
			stepBefore.setParam1(frameElement.toString());
			dispatcher.beforeFrameByElement(stepBefore, frameElement);

			// TODO is this an EventFiringWebDriver instance?
			WebDriver frameDriver = targetLocator.frame(frameElement);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.frameByElement);
			stepAfter.setParam1(frameElement.toString());
			dispatcher.afterFrameByElement(stepAfter, frameElement);
			return frameDriver;
		}

		public WebDriver parentFrame() {
			dispatcher.beforeParentFrame(new Step(Type.BeforeAction, stepNumber, Cmd.parentFrame));

			// TODO is this an EventFiringWebDriver instance?
			WebDriver frameDriver = targetLocator.parentFrame();

			dispatcher.afterParentFrame(new Step(Type.AfterAction, stepNumber++, Cmd.parentFrame));
			return frameDriver;
		}

		public WebDriver window(String windowName) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.window);
			stepBefore.setParam1(windowName);
			dispatcher.beforeWindow(stepBefore, windowName);

			// TODO is this an EventFiringWebDriver instance?
			WebDriver windowDriver = targetLocator.window(windowName);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.window);
			stepAfter.setParam1(windowName);
			dispatcher.afterWindow(stepAfter, windowName);
			return windowDriver;
		}
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Window object.
	 *---------------------------------------------------------------------------*/

	private class EventFiringWindow implements Window {
		private final Window window;

		EventFiringWindow(Window window) {
			this.window = window;
		}

		public void fullscreen() {
			dispatcher.beforeFullscreen(new Step(Type.BeforeAction, stepNumber, Cmd.fullscreen));
			
			window.fullscreen();
			
			dispatcher.afterFullscreen(new Step(Type.AfterAction, stepNumber++, Cmd.fullscreen));
		}

		public Point getPosition() {
			dispatcher.beforeGetPosition(new Step(Type.BeforeGather, stepNumber, Cmd.getPosition));
			
			Point point = window.getPosition();
			
			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getPosition);
			stepAfter.setReturnObject(point);
			dispatcher.afterGetPosition(stepAfter, point);
			return point;
		}

		public Dimension getSize() {
			dispatcher.beforeGetSize(new Step(Type.BeforeGather, stepNumber, Cmd.getSize));
			
			Dimension size = window.getSize();
			
			Step step = new Step(Type.AfterGather, stepNumber, Cmd.getSize);
			step.setReturnObject(size);
			dispatcher.afterGetSize(step, size);
			return size;
		}

		public void maximize() {
			dispatcher.beforeMaximize(new Step(Type.BeforeAction, stepNumber, Cmd.maximize));

			window.maximize();

			dispatcher.afterMaximize(new Step(Type.AfterAction, stepNumber++, Cmd.maximize));
		}

		public void setPosition(Point targetPosition) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.setPosition);
			stepBefore.setParam1(targetPosition.toString());
			dispatcher.beforeSetPosition(stepBefore, targetPosition);

			window.setPosition(targetPosition);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.setPosition);
			stepAfter.setParam1(targetPosition.toString());
			dispatcher.afterSetPosition(stepAfter, targetPosition);
		}

		public void setSize(Dimension targetSize) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.setSize);
			stepBefore.setParam1(targetSize.toString());
			dispatcher.beforeSetSize(stepBefore, targetSize);

			window.setSize(targetSize);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.setSize);
			stepAfter.setParam1(targetSize.toString());
			dispatcher.afterSetSize(stepAfter, targetSize);
		}
	}
	
	private void closeListeners() {
		defaultEventListener.closeListener();
		for (WebDriverEventListener eventListener : eventListeners) {
			eventListener.closeListener();
		}
	}
}
