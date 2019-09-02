/**
 * 
 */
package com.salesforce.selenium.support.findby;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

/**
 * @author gneumann
 *
 */
public class JSElementLocator implements ElementLocator {
	private final WebDriver driver;
	/**
	 * 
	 */
	public JSElementLocator(WebDriver driver, Field field) {
	    this.driver = driver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.support.pagefactory.ElementLocator#findElement()
	 */
	@Override
	public WebElement findElement() {
		// Execute JavaScript command and return element found; see Schneider Sales Lightning LexHomePage.getCategory() e.g.
		// String queryString = "return document.querySelector(\"lightning-grouped-combobox.slds-form-element\").shadowRoot.querySelector(\"lightning-base-combobox.slds-combobox_container\").shadowRoot.querySelector(\"input.slds-input.slds-combobox__input\")";
		// WebElement elem = (WebElement)((JavascriptExecutor) driver).executeScript(queryString);
		return (WebElement)((JavascriptExecutor) driver).executeScript("queryString");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.support.pagefactory.ElementLocator#findElements()
	 */
	@Override
	public List<WebElement> findElements() {
		// TODO
		return Collections.emptyList();
	}
}
