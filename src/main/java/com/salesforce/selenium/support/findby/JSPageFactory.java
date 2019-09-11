/* 
 * Copyright (c) 2019, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.selenium.support.findby;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * Augments the {@link PageFactory} by supporting element annotation
 * {@link FindByJS}.
 * 
 * @author gneumann
 * @since 3.0.0
 */
public class JSPageFactory extends PageFactory {

	/**
	 * Augments
	 * {@link PageFactory#initElements(org.openqa.selenium.WebDriver, Object)}
	 * by also calling {@link #initElementsJS(ElementLocatorFactory, Object)},
	 * thus enabling support for {@literal @FindByJS} annotations.
	 *
	 * @param driver The driver that will be used to look up the elements
	 * @param page   The object with WebElement and List&lt;WebElement&gt; fields
	 *               that should be proxied.
	 */
	public static void initElements(WebDriver driver, Object page) {
	    final WebDriver driverRef = driver;
	    initElements(new DefaultElementLocatorFactory(driverRef), page);
	    initElementsJS(new JSElementLocatorFactory(driverRef), page);
	}

	/**
	 * Similar to the other "initElements" methods, but takes an
	 * {@link ElementLocatorFactory} which is used for providing the mechanism for
	 * finding elements annotated by {@literal @FindByJS}.
	 *
	 * @param factory The factory to use
	 * @param page    The object to decorate the fields of
	 */
	public static void initElementsJS(ElementLocatorFactory factory, Object page) {
		final ElementLocatorFactory factoryRef = factory;
		initElements(new JSFieldDecorator(factoryRef), page);
	}
}
