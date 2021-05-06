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

package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;
import com.salesforce.drillbit.client.EventDispatcher;

import org.openqa.selenium.Beta;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.internal.HasIdentity;
import org.openqa.selenium.io.Zip;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RemoteWebElement implements WebElement, FindsByLinkText, FindsById, FindsByName, FindsByTagName,
		FindsByClassName, FindsByCssSelector, FindsByXPath, WrapsDriver, HasIdentity, TakesScreenshot,
		org.openqa.selenium.interactions.Locatable {
	private String foundBy;
	protected String id;
	protected RemoteWebDriver parent;
	protected FileDetector fileDetector;

	private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

	protected void setFoundBy(SearchContext foundFrom, String locator, String term) {
		this.foundBy = String.format("[%s] -> %s: %s", foundFrom, locator, term);
	}

	public void setParent(RemoteWebDriver parent) {
		this.parent = parent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFileDetector(FileDetector detector) {
		fileDetector = detector;
	}

	public void click() {
		eventDispatcher.beforeClick(this);
		execute(DriverCommand.CLICK_ELEMENT, ImmutableMap.of("id", id));
		eventDispatcher.afterClick(this);
	}

	public void submit() {
		eventDispatcher.beforeSubmit(this);
		execute(DriverCommand.SUBMIT_ELEMENT, ImmutableMap.of("id", id));
		eventDispatcher.afterSubmit(this);
	}

	public void sendKeys(CharSequence... keysToSend) {
		if (keysToSend == null || keysToSend.length == 0) {
			throw new IllegalArgumentException("Keys to send should be a not null CharSequence");
		}
		for (CharSequence cs : keysToSend) {
			if (cs == null) {
				throw new IllegalArgumentException("Keys to send should be a not null CharSequence");
			}
		}
		File localFile = fileDetector.getLocalFile(keysToSend);
		if (localFile != null) {
			String remotePath = upload(localFile);
			keysToSend = new CharSequence[] { remotePath };
		}

		eventDispatcher.beforeSendKeysByElement(this, keysToSend);
		execute(DriverCommand.SEND_KEYS_TO_ELEMENT, ImmutableMap.of("id", id, "value", keysToSend));
		eventDispatcher.afterSendKeysByElement(this, keysToSend);
	}

	private String upload(File localFile) {
		if (!localFile.isFile()) {
			throw new WebDriverException("You may only upload files: " + localFile);
		}

		try {
			String zip = Zip.zip(localFile);
			eventDispatcher.beforeUploadFile(this, localFile);
			Response response = execute(DriverCommand.UPLOAD_FILE, ImmutableMap.of("file", zip));
			String uploadResponse = (String) response.getValue();
			eventDispatcher.afterUploadFile(this, localFile, uploadResponse);
			return uploadResponse;
		} catch (IOException e) {
			throw new WebDriverException("Cannot upload " + localFile, e);
		}
	}

	public void clear() {
		eventDispatcher.beforeClear(this);
		execute(DriverCommand.CLEAR_ELEMENT, ImmutableMap.of("id", id));
		eventDispatcher.afterClear(this);
	}

	public String getTagName() {
		eventDispatcher.beforeGetTagName(this);
		String tagName = (String) execute(DriverCommand.GET_ELEMENT_TAG_NAME, ImmutableMap.of("id", id)).getValue();
		eventDispatcher.afterGetTagName(tagName, this);
		return tagName;
	}

	public String getAttribute(String name) {
		eventDispatcher.beforeGetAttribute(name, this);
		String value = stringValueOf(
				execute(DriverCommand.GET_ELEMENT_ATTRIBUTE, ImmutableMap.of("id", id, "name", name)).getValue());
		eventDispatcher.afterGetAttribute(name, value, this);
		return value;
	}

	private static String stringValueOf(Object o) {
		if (o == null) {
			return null;
		}
		return String.valueOf(o);
	}

	public boolean isSelected() {
		eventDispatcher.beforeIsSelected(this);
		Object value = execute(DriverCommand.IS_ELEMENT_SELECTED, ImmutableMap.of("id", id)).getValue();
		try {
			boolean boolValue = (Boolean) value;
			eventDispatcher.afterIsSelected(boolValue, this);
			return boolValue;
		} catch (ClassCastException ex) {
			throw new WebDriverException("Returned value cannot be converted to Boolean: " + value, ex);
		}
	}

	public boolean isEnabled() {
		eventDispatcher.beforeIsEnabled(this);
		Object value = execute(DriverCommand.IS_ELEMENT_ENABLED, ImmutableMap.of("id", id)).getValue();
		try {
			boolean boolValue = (Boolean) value;
			eventDispatcher.afterIsEnabled(boolValue, this);
			return boolValue;
		} catch (ClassCastException ex) {
			throw new WebDriverException("Returned value cannot be converted to Boolean: " + value, ex);
		}
	}

	public String getText() {
		eventDispatcher.beforeGetText(this);
		Response response = execute(DriverCommand.GET_ELEMENT_TEXT, ImmutableMap.of("id", id));
		String text = (String) response.getValue();
		eventDispatcher.afterGetText(text, this);
		return text;
	}

	public String getCssValue(String propertyName) {
		eventDispatcher.beforeGetCssValue(propertyName, this);
		Response response = execute(DriverCommand.GET_ELEMENT_VALUE_OF_CSS_PROPERTY,
				ImmutableMap.of("id", id, "propertyName", propertyName));
		String value = (String) response.getValue();
		eventDispatcher.afterGetText(value, this);
		return value;
	}

	public List<WebElement> findElements(By by) {
		eventDispatcher.beforeFindElementsByElement(by, this);
		List<WebElement> returnedElements = by.findElements(this);
		eventDispatcher.afterFindElementsByElement(returnedElements, by, this);

		for (WebElement element : returnedElements) {
			parent.highlightElement(element);
		}

		return returnedElements;
	}

	public WebElement findElement(By by) {
		eventDispatcher.beforeFindElementByElement(by, this);
		WebElement returnedElement = by.findElement(this);
		eventDispatcher.afterFindElementByElement(returnedElement, by, this);

		parent.highlightElement(returnedElement);

		return returnedElement;
	}

	protected WebElement findElement(String using, String value) {
		Response response = execute(DriverCommand.FIND_CHILD_ELEMENT,
				ImmutableMap.of("id", id, "using", using, "value", value));

		Object responseValue = response.getValue();
		if (responseValue == null) { // see https://github.com/SeleniumHQ/selenium/issues/5809
			throw new NoSuchElementException(String.format("Cannot locate an element using %s=%s", using, value));
		}
		WebElement element;
		try {
			element = (WebElement) responseValue;
		} catch (ClassCastException ex) {
			throw new WebDriverException("Returned value cannot be converted to WebElement: " + value, ex);
		}
		parent.setFoundBy(this, element, using, value);
		return element;
	}

	@SuppressWarnings("unchecked")
	protected List<WebElement> findElements(String using, String value) {
		Response response = execute(DriverCommand.FIND_CHILD_ELEMENTS,
				ImmutableMap.of("id", id, "using", using, "value", value));
		Object responseValue = response.getValue();
		if (responseValue == null) { // see https://github.com/SeleniumHQ/selenium/issues/4555
			return Collections.emptyList();
		}
		List<WebElement> allElements;
		try {
			allElements = (List<WebElement>) responseValue;
		} catch (ClassCastException ex) {
			throw new WebDriverException("Returned value cannot be converted to List<WebElement>: " + responseValue,
					ex);
		}
		allElements.forEach(element -> parent.setFoundBy(this, element, using, value));
		return allElements;
	}

	public WebElement findElementById(String using) {
		return findElement("id", using);
	}

	public List<WebElement> findElementsById(String using) {
		return findElements("id", using);
	}

	public WebElement findElementByLinkText(String using) {
		return findElement("link text", using);
	}

	public List<WebElement> findElementsByLinkText(String using) {
		return findElements("link text", using);
	}

	public WebElement findElementByName(String using) {
		return findElement("name", using);
	}

	public List<WebElement> findElementsByName(String using) {
		return findElements("name", using);
	}

	public WebElement findElementByClassName(String using) {
		return findElement("class name", using);
	}

	public List<WebElement> findElementsByClassName(String using) {
		return findElements("class name", using);
	}

	public WebElement findElementByCssSelector(String using) {
		return findElement("css selector", using);
	}

	public List<WebElement> findElementsByCssSelector(String using) {
		return findElements("css selector", using);
	}

	public WebElement findElementByXPath(String using) {
		return findElement("xpath", using);
	}

	public List<WebElement> findElementsByXPath(String using) {
		return findElements("xpath", using);
	}

	public WebElement findElementByPartialLinkText(String using) {
		return findElement("partial link text", using);
	}

	public List<WebElement> findElementsByPartialLinkText(String using) {
		return findElements("partial link text", using);
	}

	public WebElement findElementByTagName(String using) {
		return findElement("tag name", using);
	}

	public List<WebElement> findElementsByTagName(String using) {
		return findElements("tag name", using);
	}

	protected Response execute(String command, Map<String, ?> parameters) {
		return parent.execute(command, parameters);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WebElement)) {
			return false;
		}

		WebElement other = (WebElement) obj;
		while (other instanceof WrapsElement) {
			other = ((WrapsElement) other).getWrappedElement();
		}

		if (!(other instanceof RemoteWebElement)) {
			return false;
		}

		RemoteWebElement otherRemoteWebElement = (RemoteWebElement) other;

		return id.equals(otherRemoteWebElement.id);
	}

	/**
	 * @return This element's hash code, which is a hash of its internal opaque ID.
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openqa.selenium.internal.WrapsDriver#getWrappedDriver()
	 */
	public WebDriver getWrappedDriver() {
		return parent;
	}

	public boolean isDisplayed() {
		eventDispatcher.beforeIsDisplayed(this);
		Object value = execute(DriverCommand.IS_ELEMENT_DISPLAYED, ImmutableMap.of("id", id)).getValue();
		try {
			boolean boolValue = (Boolean) value;
			eventDispatcher.afterIsDisplayed(boolValue, this);
			return boolValue;
		} catch (ClassCastException ex) {
			throw new WebDriverException("Returned value cannot be converted to Boolean: " + value, ex);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public Point getLocation() {
		eventDispatcher.beforeGetLocation(this);
		Response response = execute(DriverCommand.GET_ELEMENT_LOCATION, ImmutableMap.of("id", id));
		Map<String, Object> rawPoint = (Map<String, Object>) response.getValue();
		int x = ((Number) rawPoint.get("x")).intValue();
		int y = ((Number) rawPoint.get("y")).intValue();
		Point point = new Point(x, y);
		eventDispatcher.afterGetLocation(point, this);
		return point;
	}

	@SuppressWarnings({ "unchecked" })
	public Dimension getSize() {
		eventDispatcher.beforeGetSizeByElement(this);
		Response response = execute(DriverCommand.GET_ELEMENT_SIZE, ImmutableMap.of("id", id));
		Map<String, Object> rawSize = (Map<String, Object>) response.getValue();
		int width = ((Number) rawSize.get("width")).intValue();
		int height = ((Number) rawSize.get("height")).intValue();
		Dimension dimension = new Dimension(width, height);
		eventDispatcher.afterGetSizeByElement(dimension, this);
		return dimension;
	}

	@SuppressWarnings({ "unchecked" })
	public Rectangle getRect() {
		eventDispatcher.beforeGetRect(this);
		Response response = execute(DriverCommand.GET_ELEMENT_RECT, ImmutableMap.of("id", id));
		Map<String, Object> rawRect = (Map<String, Object>) response.getValue();
		int x = ((Number) rawRect.get("x")).intValue();
		int y = ((Number) rawRect.get("y")).intValue();
		int width = ((Number) rawRect.get("width")).intValue();
		int height = ((Number) rawRect.get("height")).intValue();
		Rectangle rect = new Rectangle(x, y, height, width);
		eventDispatcher.afterGetRect(rect, this);
		return rect;
	}

	public Coordinates getCoordinates() {
		eventDispatcher.beforeGetCoordinates(this);
		Coordinates coordinates = new Coordinates() {
			public Point onScreen() {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			public Point inViewPort() {
				Response response = execute(DriverCommand.GET_ELEMENT_LOCATION_ONCE_SCROLLED_INTO_VIEW,
						ImmutableMap.of("id", getId()));

				@SuppressWarnings("unchecked")
				Map<String, Number> mapped = (Map<String, Number>) response.getValue();
				return new Point(mapped.get("x").intValue(), mapped.get("y").intValue());
			}

			public Point onPage() {
				return getLocation();
			}

			public Object getAuxiliary() {
				return getId();
			}
		};
		eventDispatcher.afterGetCoordinates(coordinates, this);
		return coordinates;
	}

	@Beta
	public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
		eventDispatcher.beforeGetScreenshotAsByElement(outputType, this);
		Response response = execute(DriverCommand.ELEMENT_SCREENSHOT, ImmutableMap.of("id", id));
		Object result = response.getValue();
		if (result instanceof String) {
			String base64EncodedPng = (String) result;
			X screenshot = outputType.convertFromBase64Png(base64EncodedPng);
			eventDispatcher.afterGetScreenshotAsByElement(outputType, screenshot, this);
			return screenshot;
		} else if (result instanceof byte[]) {
			String base64EncodedPng = new String((byte[]) result);
			X screenshot = outputType.convertFromBase64Png(base64EncodedPng);
			eventDispatcher.afterGetScreenshotAsByElement(outputType, screenshot, this);
			return screenshot;
		} else {
			eventDispatcher.afterGetScreenshotAsByElement(outputType, null, this);
			throw new RuntimeException(
					String.format("Unexpected result for %s command: %s", DriverCommand.ELEMENT_SCREENSHOT,
							result == null ? "null" : result.getClass().getName() + " instance"));
		}
	}

	public String toString() {
		if (foundBy == null) {
			return String.format("[%s -> unknown locator]", super.toString());
		}
		return String.format("[%s]", foundBy);
	}

	public Map<String, Object> toJson() {
		return ImmutableMap.of(Dialect.OSS.getEncodedElementKey(), getId(), Dialect.W3C.getEncodedElementKey(),
				getId());
	}
}
