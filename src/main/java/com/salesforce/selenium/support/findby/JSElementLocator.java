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
 * Finds an element by using the JavaScript command from its {@literal @FindByJS} annotation.
 * Please note that this annotation does not support lists.
 * 
 * @author gneumann
 * @since 2.1
 */
public class JSElementLocator implements ElementLocator {
	private final WebDriver driver;
	private final String script;

	/**
	 * 
	 */
	public JSElementLocator(WebDriver driver, Field field) {
	    this.driver = driver;
	    FindByJS findByJs = field.getAnnotation(FindByJS.class);
	    script = (findByJs != null) ? findByJs.script() : "";
	}

	/**
	 * Finds an element by using the JavaScript command from its {@literal @FindByJS} annotation.
	 * If no object of type {@link WebElement} is found, this method throws an exception.
	 * 
	 * @return object of type {@link WebElement}
	 * @see org.openqa.selenium.support.pagefactory.ElementLocator#findElement()
	 * @throws NoSuchElementException if script is empty or the JavaScript code does not return
	 * an object at all or not an object of type {@link WebElement}.
	 */
	@Override
	public WebElement findElement() {
		if (script.isEmpty()) {
			throw new NoSuchElementException("Cannot find element by calling an empty JavaScript command");
		}
		if (!script.startsWith("return document.querySelector")) {
			throw new NoSuchElementException("Not a valid JavaScript command: " + script + "\nit has to start with \"return document.querySelector\"");
		}
		// Execute JavaScript command and return element found; see Schneider Sales Lightning LexHomePage.getCategory() e.g.
		// script = "return document.querySelector(\"lightning-grouped-combobox.slds-form-element\").shadowRoot.querySelector(\"lightning-base-combobox.slds-combobox_container\").shadowRoot.querySelector(\"input.slds-input.slds-combobox__input\")";
		Object obj = ((JavascriptExecutor) driver).executeScript(script);
		if (!(obj instanceof WebElement)) {
			throw new NoSuchElementException("Cannot find element by calling JavaScript command: " + script);
		}
		return (WebElement) obj;
	}

	/**
	 * The {@literal @FindByJS} annotation is not supported for lists and hence this method
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
