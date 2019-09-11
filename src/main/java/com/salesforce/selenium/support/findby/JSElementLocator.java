/* 
 * Copyright (c) 2019, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.selenium.support.findby;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

/**
 * Finds an element by using the JavaScript command from its {@link FindByJS} annotation.
 * Please note that this annotation does not support lists.
 * 
 * @author gneumann
 * @since 2.1
 */
public class JSElementLocator implements ElementLocator {
	private final WebDriver driver;
	private final String script;
	private final String shadowPath;

	/**
	 * CTOR gets the locator command from {@link FindByJS} annotation if
	 * defined for the given field.
	 * 
	 * @param driver currently active WebDriver instance
	 * @param field currently processed class or instance member
	 */
	public JSElementLocator(WebDriver driver, Field field) {
	    this.driver = driver;
	    FindByJS findByJs = field.getAnnotation(FindByJS.class);
	    script = (findByJs != null) ? findByJs.script() : "";
	    shadowPath = (findByJs != null) ? findByJs.shadowPath() : "";
	}

	/**
	 * Finds an element by using either the JavaScript command or the shadow path information
	 * from its {@link FindByJS} annotation.
	 * <p>
	 * If no object of type {@link WebElement} is found, this method throws an exception.
	 * <p>
	 * The shadow path information will be converted into an equivalent JavaScript command
	 * by calling {@link ShadowPathHelper#getShadowQueryString(String)}.
	 * <p>
	 * If the JavaScript command does not start with {@code return document.querySelector}
	 * this method throws an exception.
	 * 
	 * @return object of type {@link WebElement}
	 * @see org.openqa.selenium.support.pagefactory.ElementLocator#findElement()
	 * @throws NoSuchElementException if
	 * <ul>
	 * <li>script and shadowPath values are empty</li>
	 * <li>script does not start with "return document.querySelector"</li>
	 * <li>the JavaScript code or the shadow path converted into JavaScript code does not return an object at all</li>
	 * <li>the JavaScript code or the shadow path converted into JavaScript code does not return an object of type {@link WebElement}</li>
	 * </ul>
	 */
	@Override
	public WebElement findElement() {
		if (script.isEmpty() && shadowPath.isEmpty()) {
			String errMsg = (script.isEmpty()) ? "Cannot find element by calling an empty shadow path"
					: "Cannot find element by calling an empty JavaScript command";
			throw new NoSuchElementException(errMsg);
		}

		// Example script:
		// "return document.querySelector('lightning-grouped-combobox.slds-form-element').shadowRoot.querySelector('lightning-base-combobox.slds-combobox_container').shadowRoot.querySelector('input.slds-input.slds-combobox__input')";

		String command = script;
		if (script.isEmpty()) {
			// user did not set "script" attribute but "shadowPath"; convert it to proper JavaScript
			command = ShadowPathHelper.getShadowQueryString(shadowPath);
		} else {
			if (!script.startsWith("return document.querySelector")) {
				// user did set "script" attribute but it's not the supported command
				throw new NoSuchElementException("Not a valid JavaScript command: " + script + "\nit has to start with \"return document.querySelector\"");
			}
		}
		// Execute JavaScript command and return element found
		Object obj = ((JavascriptExecutor) driver).executeScript(command);
		if (!(obj instanceof WebElement)) {
			String errMsg = (script.isEmpty()) ? "Cannot find element by calling shadow path: " + shadowPath
					: "Cannot find element by calling JavaScript command: " + script;
			throw new NoSuchElementException(errMsg);
		}
		return (WebElement) obj;
	}

	/**
	 * The {@link FindByJS} annotation is not supported for lists and hence this method
	 * will return an empty list.
	 * 
	 * @return empty list
	 * @see org.openqa.selenium.support.pagefactory.ElementLocator#findElements()
	 */
	@Override
	public List<WebElement> findElements() {
		// @FindByJS does not support lists, hence return an empty list
		return Collections.emptyList();
	}
}
