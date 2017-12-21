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

import com.salesforce.selenium.support.event.internal.EnhancedEventFiringKeyboard;
import com.salesforce.selenium.support.event.internal.EnhancedEventFiringMouse;
import com.salesforce.selenium.support.event.internal.EnhancedEventFiringTouch;

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
 * registering of a {@link EnhancedWebDriverEventListener}, e.g. for logging
 * purposes.
 */
public class EnhancedEventFiringWebDriver
		implements WebDriver, JavascriptExecutor, TakesScreenshot, WrapsDriver, HasInputDevices, HasTouchScreen {

	private final WebDriver driver;

	private final List<EnhancedWebDriverEventListener> eventListeners = new ArrayList<>();
	private final EnhancedWebDriverEventListener dispatcher = (EnhancedWebDriverEventListener) Proxy.newProxyInstance(
			EnhancedWebDriverEventListener.class.getClassLoader(), new Class[] { EnhancedWebDriverEventListener.class },
			new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					try {
						for (EnhancedWebDriverEventListener eventListener : eventListeners) {
							method.invoke(eventListener, args);
						}
						return null;
					} catch (InvocationTargetException e) {
						throw e.getTargetException();
					}
				}
			});

	public EnhancedEventFiringWebDriver(final WebDriver driver) {
		Class<?>[] allInterfaces = extractInterfaces(driver);

		this.driver = (WebDriver) Proxy.newProxyInstance(EnhancedWebDriverEventListener.class.getClassLoader(), allInterfaces,
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("getWrappedDriver".equals(method.getName())) {
							return driver;
						}

						try {
							return method.invoke(driver, args);
						} catch (InvocationTargetException e) {
							dispatcher.onException(e.getTargetException(), driver);
							throw e.getTargetException();
						}
					}
				});
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
	public EnhancedEventFiringWebDriver register(EnhancedWebDriverEventListener eventListener) {
		eventListeners.add(eventListener);
		return this;
	}

	/**
	 * @param eventListener
	 *            the event listener to unregister
	 * @return this for method chaining.
	 */
	public EnhancedEventFiringWebDriver unregister(EnhancedWebDriverEventListener eventListener) {
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

	public void get(String url) {
		dispatcher.beforeNavigateTo(url, driver);
		driver.get(url);
		dispatcher.afterNavigateTo(url, driver);
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public List<WebElement> findElements(By by) {
		dispatcher.beforeFindBy(by, null, driver);
		List<WebElement> temp = driver.findElements(by);
		dispatcher.afterFindBy(by, null, driver);
		List<WebElement> result = new ArrayList<>(temp.size());
		for (WebElement element : temp) {
			result.add(createWebElement(element));
		}
		return result;
	}

	public WebElement findElement(By by) {
		dispatcher.beforeFindBy(by, null, driver);
		WebElement temp = driver.findElement(by);
		dispatcher.afterFindBy(by, null, driver);
		return createWebElement(temp);
	}

	public String getPageSource() {
		return driver.getPageSource();
	}

	public void close() {
		dispatcher.beforeClose(driver);
		driver.close();
		dispatcher.afterClose(driver);
	}

	public void quit() {
		dispatcher.beforeQuit(driver);
		driver.quit();
		dispatcher.afterQuit(driver);
	}

	public Set<String> getWindowHandles() {
		dispatcher.beforeGetWindowHandles(driver);
		Set<String> handles = driver.getWindowHandles();
		dispatcher.afterGetWindowHandles(handles, driver);
		return handles;
	}

	public String getWindowHandle() {
		dispatcher.beforeGetWindowHandle(driver);
		String handle = driver.getWindowHandle();
		dispatcher.afterGetWindowHandle(handle, driver);
		return handle;
	}

	public Object executeScript(String script, Object... args) {
		if (driver instanceof JavascriptExecutor) {
			dispatcher.beforeScript(script, driver);
			Object[] usedArgs = unpackWrappedArgs(args);
			Object result = ((JavascriptExecutor) driver).executeScript(script, usedArgs);
			dispatcher.afterScript(script, driver);
			return result;
		}
		throw new UnsupportedOperationException("Underlying driver instance does not support executing javascript");
	}

	public Object executeAsyncScript(String script, Object... args) {
		if (driver instanceof JavascriptExecutor) {
			dispatcher.beforeScript(script, driver);
			Object[] usedArgs = unpackWrappedArgs(args);
			Object result = ((JavascriptExecutor) driver).executeAsyncScript(script, usedArgs);
			dispatcher.afterScript(script, driver);
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
		} else if (arg instanceof EnhancedEventFiringWebElement) {
			return ((EnhancedEventFiringWebElement) arg).getWrappedElement();
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
		return new EnhancedEventFiringTargetLocator(driver.switchTo());
	}

	public Navigation navigate() {
		return new EnhancedEventFiringNavigation(driver.navigate());
	}

	public Options manage() {
		return new EnhancedEventFiringOptions(driver.manage());
	}

	private WebElement createWebElement(WebElement from) {
		return new EnhancedEventFiringWebElement(from);
	}

	public Keyboard getKeyboard() {
		if (driver instanceof HasInputDevices) {
			return new EnhancedEventFiringKeyboard(driver, dispatcher);
		} else {
			throw new UnsupportedOperationException(
					"Underlying driver does not implement advanced" + " user interactions yet.");
		}
	}

	public Mouse getMouse() {
		if (driver instanceof HasInputDevices) {
			return new EnhancedEventFiringMouse(driver, dispatcher);
		} else {
			throw new UnsupportedOperationException(
					"Underlying driver does not implement advanced" + " user interactions yet.");
		}
	}

	public TouchScreen getTouch() {
		if (driver instanceof HasTouchScreen) {
			return new EnhancedEventFiringTouch(driver, dispatcher);
		} else {
			throw new UnsupportedOperationException(
					"Underlying driver does not implement advanced" + " user interactions yet.");
		}
	}

	private class EnhancedEventFiringWebElement implements WebElement, WrapsElement, WrapsDriver, Locatable {

		private final WebElement element;
		private final WebElement underlyingElement;

		private EnhancedEventFiringWebElement(final WebElement element) {
			this.element = (WebElement) Proxy.newProxyInstance(EnhancedWebDriverEventListener.class.getClassLoader(),
					extractInterfaces(element), new InvocationHandler() {
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							if (method.getName().equals("getWrappedElement")) {
								return element;
							}
							try {
								return method.invoke(element, args);
							} catch (InvocationTargetException e) {
								dispatcher.onException(e.getTargetException(), driver);
								throw e.getTargetException();
							}
						}
					});
			this.underlyingElement = element;
		}

		public void click() {
			dispatcher.beforeClick(element, driver);
			element.click();
			dispatcher.afterClick(element, driver);
		}

		public void submit() {
			dispatcher.beforeSubmit(element, driver);
			element.submit();
			dispatcher.afterSubmit(element, driver);
		}

		public void sendKeys(CharSequence... keysToSend) {
			dispatcher.beforeSendKeys(element, driver, keysToSend);
			element.sendKeys(keysToSend);
			dispatcher.afterSendKeys(element, driver, keysToSend);
		}

		public void clear() {
			dispatcher.beforeClear(element, driver);
			element.clear();
			dispatcher.afterClear(element, driver);
		}

		public String getTagName() {
			dispatcher.beforeGetTagName(element, driver);
			String tagName = element.getTagName();
			dispatcher.afterGetTagName(tagName, element, driver);
			return tagName;
		}

		public String getAttribute(String name) {
			dispatcher.beforeGetAttribute(name, element, driver);
			String value = element.getAttribute(name);
			dispatcher.afterGetAttribute(value, name, element, driver);
			return value;
		}

		public boolean isSelected() {
			dispatcher.beforeIsSelected(element, driver);
			boolean isSelected = element.isSelected();
			dispatcher.afterIsSelected(isSelected, element, driver);
			return isSelected;
		}

		public boolean isEnabled() {
			dispatcher.beforeIsEnabled(element, driver);
			boolean isEnabled = element.isEnabled();
			dispatcher.afterIsEnabled(isEnabled, element, driver);
			return isEnabled;
		}

		public String getText() {
			dispatcher.beforeGetText(element, driver);
			String text = element.getText();
			dispatcher.afterGetText(text, element, driver);
			return text;
		}

		public boolean isDisplayed() {
			dispatcher.beforeIsDisplayed(element, driver);
			boolean isDisplayed = element.isDisplayed();
			dispatcher.afterIsDisplayed(isDisplayed, element, driver);
			return isDisplayed;
		}

		public Point getLocation() {
			Point point = element.getLocation();
			return point;
		}

		public Dimension getSize() {
			Dimension dimension = element.getSize();
			return dimension;
		}

		public Rectangle getRect() {
			Rectangle rect = element.getRect();
			return rect;
		}

		public String getCssValue(String propertyName) {
			dispatcher.beforeGetCssValue(element, driver);
			String value = element.getCssValue(propertyName);
			dispatcher.afterGetCssValue(value, element, driver);
			return value;
		}

		public WebElement findElement(By by) {
			dispatcher.beforeFindBy(by, element, driver);
			WebElement temp = element.findElement(by);
			dispatcher.afterFindBy(by, element, driver);
			return createWebElement(temp);
		}

		public List<WebElement> findElements(By by) {
			dispatcher.beforeFindBy(by, element, driver);
			List<WebElement> temp = element.findElements(by);
			dispatcher.afterFindBy(by, element, driver);
			List<WebElement> result = new ArrayList<>(temp.size());
			for (WebElement element : temp) {
				result.add(createWebElement(element));
			}
			return result;
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

	private class EnhancedEventFiringNavigation implements Navigation {

		private final WebDriver.Navigation navigation;

		EnhancedEventFiringNavigation(Navigation navigation) {
			this.navigation = navigation;
		}

		public void to(String url) {
			dispatcher.beforeNavigateTo(url, driver);
			navigation.to(url);
			dispatcher.afterNavigateTo(url, driver);
		}

		public void to(URL url) {
			to(String.valueOf(url));
		}

		public void back() {
			dispatcher.beforeNavigateBack(driver);
			navigation.back();
			dispatcher.afterNavigateBack(driver);
		}

		public void forward() {
			dispatcher.beforeNavigateForward(driver);
			navigation.forward();
			dispatcher.afterNavigateForward(driver);
		}

		public void refresh() {
			dispatcher.beforeNavigateRefresh(driver);
			navigation.refresh();
			dispatcher.afterNavigateRefresh(driver);
		}
	}

	private class EnhancedEventFiringOptions implements Options {

		private Options options;

		private EnhancedEventFiringOptions(Options options) {
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
			return new EnhancedEventFiringTimeouts(options.timeouts());
		}

		public ImeHandler ime() {
			return options.ime();
		}

		@Beta
		public Window window() {
			return new EnhancedEventFiringWindow(options.window());
		}
	}

	private class EnhancedEventFiringTimeouts implements Timeouts {

		private final Timeouts timeouts;

		EnhancedEventFiringTimeouts(Timeouts timeouts) {
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
			dispatcher.beforePageLoadTimeout(time, unit, driver);
			timeouts.pageLoadTimeout(time, unit);
			dispatcher.afterPageLoadTimeout(time, unit, driver);
			return this;
		}
	}

	private class EnhancedEventFiringTargetLocator implements TargetLocator {

		private TargetLocator targetLocator;

		private EnhancedEventFiringTargetLocator(TargetLocator targetLocator) {
			this.targetLocator = targetLocator;
		}

		public WebDriver frame(int frameIndex) {
			dispatcher.beforeFrame(frameIndex, driver);
			WebDriver frameDriver = targetLocator.frame(frameIndex);
			dispatcher.afterFrame(frameIndex, driver);
			return frameDriver;
		}

		public WebDriver frame(String frameName) {
			dispatcher.beforeFrame(frameName, driver);
			WebDriver frameDriver = targetLocator.frame(frameName);
			dispatcher.afterFrame(frameName, driver);
			return frameDriver;
		}

		public WebDriver frame(WebElement frameElement) {
			dispatcher.beforeFrame(frameElement, driver);
			WebDriver frameDriver = targetLocator.frame(frameElement);
			dispatcher.afterFrame(frameElement, driver);
			return frameDriver;
		}

		public WebDriver parentFrame() {
			dispatcher.beforeParentFrame(driver);
			WebDriver frameDriver = targetLocator.parentFrame();
			dispatcher.afterParentFrame(driver);
			return frameDriver;
		}

		public WebDriver window(String windowName) {
			dispatcher.beforeWindow(windowName, driver);
			WebDriver windowDriver =  targetLocator.window(windowName);
			dispatcher.afterWindow(windowName, driver);
			return windowDriver;
		}

		public WebDriver defaultContent() {
			dispatcher.beforeDefaultContent(driver);
			WebDriver frameDriver =  targetLocator.defaultContent();
			dispatcher.afterDefaultContent(driver);
			return frameDriver;
		}

		public WebElement activeElement() {
			dispatcher.beforeActiveElement(driver);
			WebElement activeElement = targetLocator.activeElement();
			dispatcher.afterActiveElement(activeElement, driver);
			return createWebElement(activeElement);
		}

		public Alert alert() {
			dispatcher.beforeAlert(driver);
			Alert alert = targetLocator.alert();
			dispatcher.afterAlert(alert, driver);
			return alert;
		}
	}

	@Beta
	private class EnhancedEventFiringWindow implements Window {
		private final Window window;

		EnhancedEventFiringWindow(Window window) {
			this.window = window;
		}

		public void setSize(Dimension targetSize) {
			window.setSize(targetSize);
		}

		public void setPosition(Point targetLocation) {
			window.setPosition(targetLocation);
		}

		public Dimension getSize() {
			Dimension size = window.getSize();
			return size;
		}

		public Point getPosition() {
			Point point = window.getPosition();
			return point;
		}

		public void maximize() {
			dispatcher.beforeMaximize(driver);
			window.maximize();
			dispatcher.afterMaximize(driver);
		}

		public void fullscreen() {
			dispatcher.beforeFullscreen(driver);
			window.fullscreen();
			dispatcher.afterFullscreen(driver);
		}
	}
}
