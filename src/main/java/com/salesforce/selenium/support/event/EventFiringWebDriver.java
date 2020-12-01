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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.logging.Logs;

import com.salesforce.selenium.support.event.Step.Cmd;
import com.salesforce.selenium.support.event.Step.Type;

/**
 * A wrapper around an arbitrary {@link WebDriver} instance which supports
 * registering of a {@link WebDriverEventListener}, e.g. for logging
 * purposes.
 * 
 * This is an extended version of org.openqa.selenium.support.events.EventFiringWebDriver. See
 * https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/support/events/EventFiringWebDriver.html
 * for more information.
 * 
 * @since 2.0.0
 */
public class EventFiringWebDriver
		implements WebDriver, JavascriptExecutor, TakesScreenshot, WrapsDriver,
				   HasInputDevices, HasTouchScreen, Interactive, HasCapabilities {

	/**
	 * Properties file name: {@value}
	 * This file is expected to reside in the project's root directory.
	 */
	public static final String PROPERTIES_FILENAME = "eventfiringwebdriver.properties";
	/**
	 * Property key for locator of password field: {@value}
	 * <p>
	 * If this locator is found, the log files will only show '********' for the password
	 * value. The matching expression is using
	 * <code>
	 * locator.contains(passwordLocatorValue)
	 * </code>
	 * <p>
	 * If this key is not set, the default value is "password".
	 */
	public static final String CONFIG_PASSWORD_MASK = "password.locator";

	private static final String BORDER_COLORING_ENABLED = "border.color.enabled";
	private static final String BORDER_COLORING_PREFIX = "arguments[0].style.border='3px solid ";
	private static final String BORDER_COLORING_POSTFIX = "'";
	private static final String[] BORDER_COLORS = new String[] {"red", "orange", "yellow", "green",	"blue",	"purple", "magenta"};

	private static Properties properties;
	
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

	private Step currentStep = null;
	private int stepNumber = 1;
	private int border_color_index = 0;
	
	private final WebDriverConfigData data;

	public EventFiringWebDriver(final WebDriver driver, String testName) {
		data = new WebDriverConfigData();
		data.setData(WebDriverConfigData.KEY_TESTNAME, testName);

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
							if (currentStep != null) {
								currentStep.setIssue(e);
								dispatcher.onException(currentStep, currentStep.getCmd(), e.getTargetException());
							} else {
								// create a dummy step
								Step step = new Step(Type.Exception, stepNumber, Cmd.quit);
								step.setIssue(e);
								dispatcher.onException(step, Cmd.quit, e.getTargetException());
							}
							throw e.getTargetException();
						}
					}
				});
		// standard listener which writes all events to JSON files
		defaultEventListener = new FullJSONLogger(data.getData("testName"));
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
	
	/**
	 * Gets the cached WebDriver configuration data.
	 * @return data store (non-null)
	 */
	public WebDriverConfigData getDataStore() {
		return data;
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/

	@Override
	public void close() {
		Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.close);
		dispatcher.beforeClose(stepBefore);
		currentStep = stepBefore;

		driver.close();
		Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.close);
		dispatcher.afterClose(stepAfter);
	}

	@Override
	public WebElement findElement(By by) {
		Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.findElementByWebDriver);
		stepBefore.setParam1(Step.getLocatorFromBy(by));
		dispatcher.beforeFindElementByWebDriver(stepBefore, by);
		currentStep = stepBefore;

		WebElement returnedElement = driver.findElement(by);

		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.findElementByWebDriver);
		stepAfter.setParam1(Step.getLocatorFromBy(by));
		stepAfter.setReturnValue(Step.getLocatorFromWebElement(returnedElement));
		stepAfter.setReturnObject(returnedElement);
		dispatcher.afterFindElementByWebDriver(stepAfter, returnedElement, by);
		highlightElement(returnedElement);
		return createWebElement(returnedElement);
	}

	@Override
	public List<WebElement> findElements(By by) {
		Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.findElementsByWebDriver);
		stepBefore.setParam1(Step.getLocatorFromBy(by));
		dispatcher.beforeFindElementByWebDriver(stepBefore, by);
		currentStep = stepBefore;

		List<WebElement> returnedElements = driver.findElements(by);

		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.findElementsByWebDriver);
		stepAfter.setParam1(Step.getLocatorFromBy(by));
		if (returnedElements.size() > 0) {
			if (returnedElements.size() == 1)
				stepAfter.setReturnValue(Step.getLocatorFromWebElement(returnedElements.get(0)));
			else
				stepAfter.setReturnValue(Step.getLocatorFromWebElement(returnedElements.get(0)) + " and "
						+ (returnedElements.size() - 1) + " more");				
		}
		stepAfter.setReturnObject(returnedElements);
		dispatcher.afterFindElementsByWebDriver(stepAfter, returnedElements, by);

		List<WebElement> returnedAndWrappedElements = new ArrayList<>(returnedElements.size());
		for (WebElement element : returnedElements) {
			returnedAndWrappedElements.add(createWebElement(element));
			highlightElement(element);
		}
		return returnedAndWrappedElements;
	}

	@Override
	public void get(String url) {
		Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.get);
		stepBefore.setParam1(url);
		dispatcher.beforeGet(stepBefore, url);
		currentStep = stepBefore;

		driver.get(url);

		Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.get);
		stepAfter.setParam1(url);
		dispatcher.afterGet(stepAfter, url);
	}

	@Override
	public String getCurrentUrl() {
		Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getCurrentUrl);
		dispatcher.beforeGetCurrentUrl(stepBefore);
		currentStep = stepBefore;

		String url = driver.getCurrentUrl();

		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getCurrentUrl);
		stepAfter.setReturnValue(url);
		dispatcher.afterGetCurrentUrl(stepAfter, url);
		return url; 
	}

	@Override
	public String getTitle() {
		Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getTitle);
		dispatcher.beforeGetTitle(stepBefore);
		currentStep = stepBefore;

		String title = driver.getTitle();
		
		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getTitle);
		stepAfter.setReturnValue(title);
		dispatcher.afterGetTitle(stepAfter, title);
		return title;
	}

	@Override
	public String getWindowHandle() {
		Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getWindowHandle);
		dispatcher.beforeGetWindowHandle(stepBefore);
		currentStep = stepBefore;

		String handle = driver.getWindowHandle();

		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getWindowHandle);
		stepAfter.setReturnValue(handle);
		dispatcher.afterGetWindowHandle(stepAfter, handle);
		return handle;
	}

	@Override
	public Set<String> getWindowHandles() {
		Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getWindowHandles);
		dispatcher.beforeGetWindowHandles(stepBefore);
		currentStep = stepBefore;

		Set<String> handles = driver.getWindowHandles();
		
		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getWindowHandles);
		if ((handles != null) && (handles.size() > 0)) {
			StringBuffer sb = new StringBuffer();
			for (String h : handles) {
				sb.append(h).append(",");
			}
			String handlesAsString = sb.toString();
			// remove the trailing ','
			handlesAsString.substring(handlesAsString.length()-1);
			stepAfter.setReturnValue(handlesAsString);
		}

		dispatcher.afterGetWindowHandles(stepAfter, handles);
		return handles;
	}

	/**
	 * In addition to quitting the wrapped WebDriver instance, this call also will call
	 * {@link WebDriverEventListener#closeListener()} on all registered listeners.
	 * 
	 * @see WebDriver#quit()
	 */
	@Override
	public void quit() {
		Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.quit);
		dispatcher.beforeQuit(stepBefore);
		currentStep = stepBefore;

		try {
			// occasionally the driver instance "dies" during quitting,
			// hence throwing a nasty exception which gets TestNG/JUnit into
			// limbo mode.
			driver.quit();
		} catch (Throwable t) {
			// It's benign to catch and swallow here because you can't do anything
			// useful with this driver instance anyway.
			System.err.println("Exception while quitting WebDriver instance: " + t.getMessage());
			t.printStackTrace();
		}

		Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.quit);
		dispatcher.afterQuit(stepAfter);
		closeListeners();
	}

	@Override
	public String getPageSource() {
		Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getPageSource);
		dispatcher.beforeGetPageSource(stepBefore);
		currentStep = stepBefore;

		String source = driver.getPageSource();

		Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getPageSource);
		stepAfter.setReturnValue(source);
		dispatcher.afterGetPageSource(stepAfter, source);
		return source;
	}

	@Override
	public Object executeScript(String script, Object... args) {
		if (driver instanceof JavascriptExecutor) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.executeScript);
			stepBefore.setParam1(script);
			// TODO handle args
			dispatcher.beforeExecuteScript(stepBefore, script, args);
			currentStep = stepBefore;

			Object[] usedArgs = unpackWrappedArgs(args);
			Object result = ((JavascriptExecutor) driver).executeScript(script, usedArgs);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.executeScript);
			stepAfter.setParam1(script);
			// TODO handle args and returned result
			dispatcher.afterExecuteScript(stepAfter, script, args);
			return result;
		}
		throw new UnsupportedOperationException("Underlying driver instance does not support executing javascript");
	}

	@Override
	public Object executeAsyncScript(String script, Object... args) {
		if (driver instanceof JavascriptExecutor) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.executeAsyncScript);
			stepBefore.setParam1(script);
			// TODO handle args
			dispatcher.beforeExecuteAsyncScript(stepBefore, script, args);
			currentStep = stepBefore;

			Object[] usedArgs = unpackWrappedArgs(args);
			Object result = ((JavascriptExecutor) driver).executeAsyncScript(script, usedArgs);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.executeAsyncScript);
			stepAfter.setParam1(script);
			// TODO handle args and returned result
			dispatcher.afterExecuteAsyncScript(stepAfter, script, args);
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

	// TODO add to WebDriverEventListener interface
	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		if (driver instanceof TakesScreenshot) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.getScreenshotAs);
			String targetString = (target != null) ? target.toString() : null;
			stepBefore.setParam1(targetString);
			dispatcher.beforeGetScreenshotAs(stepBefore, target);
			currentStep = stepBefore;

			X result = ((TakesScreenshot) driver).getScreenshotAs(target);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.getScreenshotAs);
			stepAfter.setParam1(targetString);
			dispatcher.afterGetScreenshotAs(stepAfter, target);
			return result;
		}
		throw new UnsupportedOperationException("Underlying driver instance does not support taking screenshots");
	}

	@Override
	public TargetLocator switchTo() {
		return new EventFiringTargetLocator(driver.switchTo());
	}

	@Override
	public Navigation navigate() {
		return new EventFiringNavigation(driver.navigate());
	}

	@Override
	public Options manage() {
		return new EventFiringOptions(driver.manage());
	}

	private WebElement createWebElement(WebElement from) {
		return new EventFiringWebElement(from);
	}

	@Override
	public Keyboard getKeyboard() {
		if (driver instanceof HasInputDevices) {
			return new EventFiringKeyboard();
		} else {
			throw new UnsupportedOperationException(
					"Underlying driver does not implement advanced user interactions yet.");
		}
	}

	@Override
	public Mouse getMouse() {
		if (driver instanceof HasInputDevices) {
			return new EventFiringMouse();
		} else {
			throw new UnsupportedOperationException(
					"Underlying driver does not implement advanced user interactions yet.");
		}
	}

	@Override
	public TouchScreen getTouch() {
		if (driver instanceof HasTouchScreen) {
			// We do not support calls to this interface and hence we simply pass on the instance
			return ((HasTouchScreen) driver).getTouch();
		} else {
			throw new UnsupportedOperationException(
					"Underlying driver does not implement advanced user interactions yet.");
		}
	}

	@Override
	public void perform(Collection<Sequence> actions) {
		if (driver instanceof Interactive) {
			((Interactive) driver).perform(actions);
			return;
		}
		throw new UnsupportedOperationException(
				"Underlying driver does not implement advanced" + " user interactions yet.");

	}

	@Override
	public void resetInputState() {
		if (driver instanceof Interactive) {
			((Interactive) driver).resetInputState();
			return;
		}
		throw new UnsupportedOperationException(
				"Underlying driver does not implement advanced" + " user interactions yet.");

	}

	@Override
	public Capabilities getCapabilities() {
		if (driver instanceof HasCapabilities) {
			return ((HasCapabilities) driver).getCapabilities();
		}
		throw new UnsupportedOperationException("Underlying driver does not implement getting capabilities yet.");
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
								Step step = new Step(Type.Exception, stepNumber, Cmd.quit);
								step.setIssue(e);
								dispatcher.onException(step, Cmd.quit, e.getTargetException());
								throw e.getTargetException();
							}
						}
					});
			this.underlyingElement = element;
		}

		@Override
		public void click() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.clickByElement);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			currentStep = stepBefore;

			dispatcher.beforeClick(stepBefore, element);

			element.click();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.clickByElement);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterClick(stepAfter, element);
		}

		@Override
		public void clear() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.clear);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeClear(stepBefore, element);
			currentStep = stepBefore;

			element.clear();
			
			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.clear);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterClear(stepAfter, element);
		}

		@Override
		public WebElement findElement(By by) {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.findElementByElement);
			stepBefore.setParam1(Step.getLocatorFromBy(by));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeFindElementByElement(stepBefore, by, element);
			currentStep = stepBefore;

			WebElement returnedElement = element.findElement(by);

			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.findElementByElement);
			stepAfter.setParam1(Step.getLocatorFromBy(by));
			stepAfter.setReturnValue(Step.getLocatorFromWebElement(returnedElement));
			stepAfter.setReturnObject(returnedElement);
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterFindElementByElement(stepAfter, returnedElement, by, element);
			highlightElement(element);
			return createWebElement(returnedElement);
		}

		@Override
		public List<WebElement> findElements(By by) {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.findElementsByElement);
			stepBefore.setParam1(Step.getLocatorFromBy(by));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeFindElementByElement(stepBefore, by, element);
			currentStep = stepBefore;

			List<WebElement> returnedElements = element.findElements(by);

			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.findElementsByElement);
			stepAfter.setParam1(Step.getLocatorFromBy(by));
			if (returnedElements.size() > 0) {
				if (returnedElements.size() == 1)
					stepAfter.setReturnValue(Step.getLocatorFromWebElement(returnedElements.get(0)));
				else
					stepAfter.setReturnValue(Step.getLocatorFromWebElement(returnedElements.get(0)) + " and "
							+ (returnedElements.size() - 1) + " more");				
			}
			stepAfter.setReturnObject(returnedElements);
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterFindElementsByElement(stepAfter, returnedElements, by, element);

			List<WebElement> returnedAndWrappedElements = new ArrayList<>(returnedElements.size());
			for (WebElement element : returnedElements) {
				returnedAndWrappedElements.add(createWebElement(element));
				highlightElement(element);
			}
			return returnedAndWrappedElements;
		}

		@Override
		public String getAttribute(String name) {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getAttribute);
			stepBefore.setParam1(name);
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeGetAttribute(stepBefore, name, element);
			currentStep = stepBefore;

			String value = element.getAttribute(name);
			
			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getAttribute);
			stepAfter.setParam1(name);
			stepAfter.setReturnValue(value);
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterGetAttribute(stepAfter, value, name, element);
			return value;
		}

		@Override
		public String getCssValue(String propertyName) {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getCssValue);
			stepBefore.setParam1(propertyName);
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeGetCssValue(stepBefore, propertyName, element);
			currentStep = stepBefore;

			String value = element.getCssValue(propertyName);
			
			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getCssValue);
			stepAfter.setParam1(propertyName);
			stepAfter.setReturnValue(value);
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterGetCssValue(stepAfter, propertyName, value, element);
			return value;
		}

		@Override
		public String getTagName() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getTagName);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeGetTagName(stepBefore, element);
			currentStep = stepBefore;

			String tagName = element.getTagName();

			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getTagName);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue(tagName);
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterGetTagName(stepAfter, tagName, element);
			return tagName;
		}

		@Override
		public String getText() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getText);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeGetText(stepBefore, element);
			currentStep = stepBefore;

			String text = element.getText();
			
			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getText);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue(text);
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterGetText(stepAfter, text, element);
			return text;
		}

		@Override
		public boolean isDisplayed() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.isDisplayed);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeIsDisplayed(stepBefore, element);
			currentStep = stepBefore;

			boolean isDisplayed = element.isDisplayed();

			Step stepAfter = new Step(Type.AfterGather, stepNumber++, Cmd.isDisplayed);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue("" + isDisplayed);
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterIsDisplayed(stepAfter, isDisplayed, element);
			return isDisplayed;
		}

		@Override
		public boolean isEnabled() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.isEnabled);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeIsEnabled(stepBefore, element);
			currentStep = stepBefore;

			boolean isEnabled = element.isEnabled();

			Step stepAfter = new Step(Type.AfterGather, stepNumber++, Cmd.isEnabled);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue("" + isEnabled);
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterIsEnabled(stepAfter, isEnabled, element);
			return isEnabled;
		}

		@Override
		public boolean isSelected() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.isSelected);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeIsSelected(stepBefore, element);
			currentStep = stepBefore;

			boolean isSelected = element.isSelected();

			Step stepAfter = new Step(Type.AfterGather, stepNumber++, Cmd.isSelected);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue("" + isSelected);
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterIsSelected(stepAfter, isSelected, element);
			return isSelected;
		}

		@Override
		public Point getLocation() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getLocation);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeGetLocation(stepBefore, element);
			currentStep = stepBefore;

			Point point = element.getLocation();

			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getLocation);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue(point.toString());
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterGetLocation(stepAfter, point, element);
			return point;
		}

		@Override
		public Dimension getSize() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getSizeByElement);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeGetSizeByElement(stepBefore, element);
			currentStep = stepBefore;

			Dimension dimension = element.getSize();

			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getSizeByElement);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue(dimension.toString());
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterGetSizeByElement(stepAfter, dimension, element);
			return dimension;
		}

		@Override
		public Rectangle getRect() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getRect);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeGetRect(stepBefore, element);
			currentStep = stepBefore;

			Rectangle rect = element.getRect();

			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getRect);
			stepAfter.setParam1(Step.getLocatorFromWebElement(element));
			stepAfter.setReturnValue(rect.toString());
			stepAfter.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.afterGetRect(stepAfter, rect, element);
			return rect;
		}

		@Override
		public void sendKeys(CharSequence... keysToSend) {
			String param1 = Step.getLocatorFromWebElement(element);
			String param2 = maskTextIfPassword(param1, keysToSend);
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.sendKeysByElement);
			stepBefore.setParam1(param1);
			stepBefore.setParam2(param2);
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeSendKeysByElement(stepBefore, element, param2);
			currentStep = stepBefore;

			element.sendKeys(keysToSend);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.sendKeysByElement);
			stepAfter.setParam1(param1);
			stepAfter.setParam2(param2);
			dispatcher.afterSendKeysByElement(stepAfter, element, param2);
		}

		private String maskTextIfPassword(String param1, CharSequence... keysToSend) {
			String param2 = "********";
			if (!param1.contains(getProperty(CONFIG_PASSWORD_MASK, "password"))) {
				StringBuffer buffer = new StringBuffer();
				if ((keysToSend != null) && (keysToSend.length > 0)) {
					for (int i = 0; i < keysToSend.length; i++) {
						buffer.append(keysToSend[i]);
					}
				}
				param2 = buffer.toString();
			}
			return param2;
		}

		@Override
		public void submit() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.submit);
			stepBefore.setParam1(Step.getLocatorFromWebElement(element));
			stepBefore.setElementLocator(Step.getLocatorFromWebElement(element));
			dispatcher.beforeSubmit(stepBefore, element);
			currentStep = stepBefore;

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

		@Override
		public Coordinates getCoordinates() {
			return ((Locatable) underlyingElement).getCoordinates();
		}

		@Override
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

		@Override
		public void back() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.back);
			dispatcher.beforeBack(stepBefore);
			currentStep = stepBefore;

			navigation.back();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.back);
			dispatcher.afterBack(stepAfter);
		}

		@Override
		public void forward() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.forward);
			dispatcher.beforeForward(stepBefore);
			currentStep = stepBefore;

			navigation.forward();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.forward);
			dispatcher.afterForward(stepAfter);
		}

		@Override
		public void refresh() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.refresh);
			dispatcher.beforeRefresh(stepBefore);
			currentStep = stepBefore;

			navigation.refresh();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.refresh);
			dispatcher.afterRefresh(stepAfter);
		}

		@Override
		public void to(URL url) {
			to(String.valueOf(url));
		}

		@Override
		public void to(String url) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.to);
			stepBefore.setParam1(url);
			currentStep = stepBefore;

			navigation.to(url);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.to);
			stepAfter.setParam1(url);
			dispatcher.afterTo(stepAfter, url);
		}
	}

	private class EventFiringOptions implements Options {

		private Options options;

		private EventFiringOptions(Options options) {
			this.options = options;
		}

		@Override
		public Logs logs() {
			return options.logs();
		}

		@Override
		public void addCookie(Cookie cookie) {
			options.addCookie(cookie);
		}

		@Override
		public void deleteCookieNamed(String name) {
			options.deleteCookieNamed(name);
		}

		@Override
		public void deleteCookie(Cookie cookie) {
			options.deleteCookie(cookie);
		}

		@Override
		public void deleteAllCookies() {
			options.deleteAllCookies();
		}

		@Override
		public Set<Cookie> getCookies() {
			Set<Cookie> cookies = options.getCookies(); 
			return cookies;
		}

		@Override
		public Cookie getCookieNamed(String name) {
			Cookie cookie = options.getCookieNamed(name); 
			return cookie;
		}

		@Override
		public Timeouts timeouts() {
			return new EventFiringTimeouts(options.timeouts());
		}

		@Override
		public ImeHandler ime() {
			return options.ime();
		}

		@Override
		public Window window() {
			return new EventFiringWindow(options.window());
		}
	}

	private class EventFiringTimeouts implements Timeouts {

		private final Timeouts timeouts;

		EventFiringTimeouts(Timeouts timeouts) {
			this.timeouts = timeouts;
		}

		@Override
		public Timeouts implicitlyWait(long time, TimeUnit unit) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.implicitlyWait);
			stepBefore.setParam1("" + time);
			stepBefore.setParam2("" + TimeUnit.MILLISECONDS.convert(time, unit));
			dispatcher.beforeImplicitlyWait(stepBefore, time, unit);
			currentStep = stepBefore;

			timeouts.implicitlyWait(time, unit);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.implicitlyWait);
			stepAfter.setParam1("" + time);
			stepAfter.setParam2("" + TimeUnit.MILLISECONDS.convert(time, unit));
			dispatcher.afterImplicitlyWait(stepAfter, time, unit);
			return this;
		}

		@Override
		public Timeouts pageLoadTimeout(long time, TimeUnit unit) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.pageLoadTimeout);
			stepBefore.setParam1("" + time);
			stepBefore.setParam2("" + TimeUnit.MILLISECONDS.convert(time, unit));
			dispatcher.beforePageLoadTimeout(stepBefore, time, unit);
			currentStep = stepBefore;

			timeouts.pageLoadTimeout(time, unit);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.pageLoadTimeout);
			stepAfter.setParam1("" + time);
			stepAfter.setParam2("" + TimeUnit.MILLISECONDS.convert(time, unit));
			dispatcher.afterPageLoadTimeout(stepAfter, time, unit);
			return this;
		}

		@Override
		public Timeouts setScriptTimeout(long time, TimeUnit unit) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.setScriptTimeout);
			stepBefore.setParam1("" + time);
			stepBefore.setParam2("" + TimeUnit.MILLISECONDS.convert(time, unit));
			dispatcher.beforeSetScriptTimeout(stepBefore, time, unit);

			timeouts.setScriptTimeout(time, unit);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.pageLoadTimeout);
			stepAfter.setParam1("" + time);
			stepAfter.setParam2("" + TimeUnit.MILLISECONDS.convert(time, unit));
			dispatcher.afterSetScriptTimeout(stepAfter, time, unit);
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

		@Override
		public WebElement activeElement() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.activeElement);
			dispatcher.beforeActiveElement(stepBefore);
			currentStep = stepBefore;

			WebElement activeElement = targetLocator.activeElement();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.activeElement);
			stepAfter.setReturnValue(Step.getLocatorFromWebElement(activeElement));
			stepAfter.setReturnObject(activeElement);
			dispatcher.afterActiveElement(stepAfter, activeElement);
			return createWebElement(activeElement);
		}

		@Override
		public Alert alert() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.alert);
			dispatcher.beforeAlert(stepBefore);
			currentStep = stepBefore;

			Alert alert = targetLocator.alert();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.alert);
			stepAfter.setReturnValue(alert.toString());
			stepAfter.setReturnObject(alert);
			dispatcher.afterAlert(stepAfter, alert);
			return alert;
		}

		@Override
		public WebDriver defaultContent() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.defaultContent);
			dispatcher.beforeDefaultContent(stepBefore);
			currentStep = stepBefore;

			WebDriver frameDriver = targetLocator.defaultContent();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.defaultContent);
			dispatcher.afterDefaultContent(stepAfter);
			return frameDriver;
		}

		@Override
		public WebDriver frame(int frameIndex) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.frameByIndex);
			stepBefore.setParam1("" + frameIndex);
			dispatcher.beforeFrameByIndex(stepBefore, frameIndex);
			currentStep = stepBefore;

			WebDriver frameDriver = targetLocator.frame(frameIndex);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.frameByIndex);
			stepAfter.setParam1("" + frameIndex);
			dispatcher.afterFrameByIndex(stepAfter, frameIndex);
			return frameDriver;
		}

		@Override
		public WebDriver frame(String frameName) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.frameByName);
			stepBefore.setParam1(frameName);
			dispatcher.beforeFrameByName(stepBefore, frameName);
			currentStep = stepBefore;

			WebDriver frameDriver = targetLocator.frame(frameName);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.frameByName);
			stepAfter.setParam1(frameName);
			dispatcher.afterFrameByName(stepAfter, frameName);
			return frameDriver;
		}

		@Override
		public WebDriver frame(WebElement frameElement) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.frameByElement);
			stepBefore.setParam1(frameElement.toString());
			dispatcher.beforeFrameByElement(stepBefore, frameElement);
			currentStep = stepBefore;

			WebDriver frameDriver = targetLocator.frame(frameElement);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.frameByElement);
			stepAfter.setParam1(frameElement.toString());
			dispatcher.afterFrameByElement(stepAfter, frameElement);
			return frameDriver;
		}

		@Override
		public WebDriver parentFrame() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.parentFrame);
			dispatcher.beforeParentFrame(stepBefore);
			currentStep = stepBefore;

			WebDriver frameDriver = targetLocator.parentFrame();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.parentFrame);
			dispatcher.afterParentFrame(stepAfter);
			return frameDriver;
		}

		@Override
		public WebDriver window(String windowName) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.window);
			stepBefore.setParam1(windowName);
			dispatcher.beforeWindow(stepBefore, windowName);
			currentStep = stepBefore;

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

		@Override
		public void fullscreen() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.fullscreen);
			dispatcher.beforeFullscreen(stepBefore);
			currentStep = stepBefore;
			
			window.fullscreen();
			
			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.fullscreen);
			dispatcher.afterFullscreen(stepAfter);
		}

		@Override
		public Point getPosition() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getPosition);
			dispatcher.beforeGetPosition(stepBefore);
			currentStep = stepBefore;
			
			Point point = window.getPosition();
			
			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getPosition);
			stepAfter.setReturnValue(point.toString());
			stepAfter.setReturnObject(point);
			dispatcher.afterGetPosition(stepAfter, point);
			return point;
		}

		@Override
		public Dimension getSize() {
			Step stepBefore = new Step(Type.BeforeGather, stepNumber, Cmd.getSizeByWindow);
			dispatcher.beforeGetSizeByWindow(stepBefore);
			currentStep = stepBefore;
			
			Dimension size = window.getSize();
			
			Step stepAfter = new Step(Type.AfterGather, stepNumber, Cmd.getSizeByWindow);
			stepAfter.setReturnValue(size.toString());
			stepAfter.setReturnObject(size);
			dispatcher.afterGetSizeByWindow(stepAfter, size);
			return size;
		}

		@Override
		public void maximize() {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.maximize);
			dispatcher.beforeMaximize(stepBefore);
			currentStep = stepBefore;

			window.maximize();

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.maximize);
			dispatcher.afterMaximize(stepAfter);
		}

		@Override
		public void setPosition(Point targetPosition) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.setPosition);
			String targetPositionString = (targetPosition != null) ? targetPosition.toString() : null;
			stepBefore.setParam1(targetPositionString);
			dispatcher.beforeSetPosition(stepBefore, targetPosition);
			currentStep = stepBefore;

			window.setPosition(targetPosition);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.setPosition);
			stepAfter.setParam1(targetPositionString);
			dispatcher.afterSetPosition(stepAfter, targetPosition);
		}

		@Override
		public void setSize(Dimension targetSize) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.setSizeByWindow);
			String targetSizeString = (targetSize != null) ? targetSize.toString() : null;
			stepBefore.setParam1(targetSizeString);
			dispatcher.beforeSetSize(stepBefore, targetSize);
			currentStep = stepBefore;

			window.setSize(targetSize);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.setSizeByWindow);
			stepAfter.setParam1(targetSizeString);
			dispatcher.afterSetSize(stepAfter, targetSize);
		}
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from Keyboard object.
	 *---------------------------------------------------------------------------*/

	private class EventFiringKeyboard implements Keyboard {
		private final Keyboard keyboard;

		private EventFiringKeyboard() {
			this.keyboard = ((HasInputDevices) driver).getKeyboard();
		}

		@Override
		public void sendKeys(CharSequence... keysToSend) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.sendKeysByKeyboard);
			String keysToSendString = (keysToSend != null) ? keysToSend.toString() : null;
			stepBefore.setParam1(keysToSendString);
			dispatcher.beforeSendKeysByKeyboard(stepBefore, keysToSend);
			currentStep = stepBefore;

			keyboard.sendKeys(keysToSend);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.sendKeysByKeyboard);
			stepAfter.setParam1(keysToSendString);
			dispatcher.afterSendKeysByKeyboard(stepBefore, keysToSend);
		}

		@Override
		public void pressKey(CharSequence keyToPress) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.pressKey);
			String keyToPressString = (keyToPress != null) ? keyToPress.toString() : null;
			stepBefore.setParam1(keyToPressString);
			dispatcher.beforePressKey(stepBefore, keyToPress);

			keyboard.pressKey(keyToPress);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.pressKey);
			stepAfter.setParam1(keyToPressString);
			dispatcher.afterPressKey(stepBefore, keyToPress);
		}

		@Override
		public void releaseKey(CharSequence keyToRelease) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.releaseKey);
			String keyToReleaseString = (keyToRelease != null) ? keyToRelease.toString() : null;
			stepBefore.setParam1(keyToReleaseString);
			dispatcher.beforeReleaseKey(stepBefore, keyToRelease);

			keyboard.releaseKey(keyToRelease);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.releaseKey);
			stepAfter.setParam1(keyToReleaseString);
			dispatcher.afterReleaseKey(stepBefore, keyToRelease);
		}
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from Keyboard object.
	 *---------------------------------------------------------------------------*/

	private class EventFiringMouse implements Mouse {
		private final Mouse mouse;

		private EventFiringMouse() {
			this.mouse = ((HasInputDevices) driver).getMouse();
		}

		@Override
		public void click(Coordinates where) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.clickByMouse);
			String whereString = (where != null) ? where.toString() : null;
			stepBefore.setParam1(whereString);
			dispatcher.beforeClickByMouse(stepBefore, where);

			mouse.click(where);
			
			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.clickByMouse);
			stepAfter.setParam1(whereString);
			dispatcher.afterClickByMouse(stepBefore, where);
		}

		@Override
		public void doubleClick(Coordinates where) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.doubleClick);
			String whereString = (where != null) ? where.toString() : null;
			stepBefore.setParam1(whereString);
			dispatcher.beforeDoubleClick(stepBefore, where);

			mouse.doubleClick(where);

			Step stepAfter = new Step(Type.AfterAction, stepNumber, Cmd.doubleClick);
			stepAfter.setParam1(whereString);
			dispatcher.afterDoubleClick(stepAfter, where);
		}

		@Override
		public void mouseDown(Coordinates where) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.mouseDown);
			String whereString = (where != null) ? where.toString() : null;
			stepBefore.setParam1(whereString);
			dispatcher.beforeMouseDown(stepBefore, where);

			mouse.mouseDown(where);

			Step stepAfter = new Step(Type.AfterAction, stepNumber, Cmd.mouseDown);
			stepAfter.setParam1(whereString);
			dispatcher.afterMouseDown(stepAfter, where);
		}

		@Override
		public void mouseUp(Coordinates where) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.mouseUp);
			String whereString = (where != null) ? where.toString() : null;
			stepBefore.setParam1(whereString);
			dispatcher.beforeMouseUp(stepBefore, where);

			mouse.mouseUp(where);

			Step stepAfter = new Step(Type.AfterAction, stepNumber, Cmd.mouseUp);
			stepAfter.setParam1(whereString);
			dispatcher.afterMouseUp(stepAfter, where);
		}

		@Override
		public void mouseMove(Coordinates where) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.mouseMove);
			String whereString = (where != null) ? where.toString() : null;
			stepBefore.setParam1(whereString);
			dispatcher.beforeMouseMove(stepBefore, where);

			mouse.mouseMove(where);

			Step stepAfter = new Step(Type.AfterAction, stepNumber, Cmd.mouseMove);
			stepAfter.setParam1(whereString);
			dispatcher.afterMouseMove(stepAfter, where);
		}

		@Override
		public void mouseMove(Coordinates where, long xOffset, long yOffset) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.mouseMoveWithOffset);
			String whereString = (where != null) ? where.toString() : null;
			stepBefore.setParam1(whereString);
			stepBefore.setParam2(xOffset + ", " + yOffset);
			dispatcher.beforeMouseMove(stepBefore, where, xOffset, yOffset);

			mouse.mouseMove(where, xOffset, yOffset);

			Step stepAfter = new Step(Type.AfterAction, stepNumber, Cmd.mouseMoveWithOffset);
			stepAfter.setParam1(whereString);
			stepAfter.setParam2(xOffset + ", " + yOffset);
			dispatcher.afterMouseMove(stepAfter, where, xOffset, yOffset);
		}

		@Override
		public void contextClick(Coordinates where) {
			Step stepBefore = new Step(Type.BeforeAction, stepNumber, Cmd.contextClick);
			String whereString = (where != null) ? where.toString() : null;
			stepBefore.setParam1(whereString);
			dispatcher.beforeContextClick(stepBefore, where);

			mouse.contextClick(where);

			Step stepAfter = new Step(Type.AfterAction, stepNumber++, Cmd.contextClick);
			stepAfter.setParam1(whereString);
			dispatcher.afterContextClick(stepBefore, where);
		}
	}

	private void closeListeners() {
		defaultEventListener.closeListener();
		for (WebDriverEventListener eventListener : eventListeners) {
			eventListener.closeListener();
		}
	}

	private void highlightElement(WebElement element) {
		// only draw a border if the system property "border.color.enabled" is set to TRUE
		if (!"true".equalsIgnoreCase(System.getProperty(BORDER_COLORING_ENABLED, "false")))
			return;

		// draw a border around the element
	    if (driver instanceof JavascriptExecutor) {
	    	// choose from seven border colors for each call
			String color = BORDER_COLORS[border_color_index % BORDER_COLORS.length];
			border_color_index = border_color_index + 1;
			// decorate element with a border
	        ((JavascriptExecutor)driver).executeScript(BORDER_COLORING_PREFIX + color + BORDER_COLORING_POSTFIX, element);
	    }
	}

	/**
	 * Gets the value of the given property by looking up system properties first
	 * and then anything defined in the property file {@link #PROPERTIES_FILENAME}.
	 * If there is still nothing found, it will return the default value.
	 * 
	 * This allows to define proper values in a file but to (occasionally) override
	 * them by setting a system property.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return value found or default value, if property is not defined in either system or
	 * property file {@link #PROPERTIES_FILENAME}
	 */
	public static String getProperty(String key, String defaultValue) {
		String value = System.getProperty(key);
		return (value != null) ? value : getProperties().getProperty(key, defaultValue);
	}
	
	private static Properties getProperties() {
		if (properties != null)
			return properties;
		
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream(PROPERTIES_FILENAME)) {
			prop.load(input);
		} catch (IOException ex) {
			// Warn only one time that config file is not present (or corrupt?)
			System.out.println("Unable to load config file " + PROPERTIES_FILENAME);
		}
		return prop;
	}
	
	/**
	 * Data store for WebDriver specific configuration information.
	 * @author gneumann
	 */
	public static class WebDriverConfigData {
		/**
		 * Key to get the current test name from cache.
		 */
		public static final String KEY_TESTNAME = "testName";
		private final Map<String, String> map = new HashMap<>();

		/**
		 * Gets the data stored for this particular key or NULL
		 * @param key
		 * @return data if present or NULL
		 */
		public String getData(String key) {
			return map.get(key);
		}
		
		/**
		 * Sets the value for the given key in a map. NULL values are permitted, but
		 * make no sense, of course.
		 * @param key
		 * @param value
		 */
		public void setData(String key, String value) {
			map.put(key, value);
		}
	}
}
