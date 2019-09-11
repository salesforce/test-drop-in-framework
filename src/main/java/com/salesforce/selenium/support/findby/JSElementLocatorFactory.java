/* 
 * Copyright (c) 2019, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.selenium.support.findby;

import java.lang.reflect.Field;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * Allows WebDriver to find an element by using the given JavaScript command.
 *  
 * @author gneumann
 * @since 3.0.0
 */
public class JSElementLocatorFactory implements ElementLocatorFactory {
	private final WebDriver driver;

	public JSElementLocatorFactory(WebDriver driver) {
		this.driver = driver;
	}

	@Override
	public ElementLocator createLocator(Field field) {
		return new JSElementLocator(driver, field);
	}
}
