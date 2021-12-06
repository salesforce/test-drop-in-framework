/* 
 * Copyright (c) 2019, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.selenium.support.findby;

import java.lang.reflect.Field;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

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
	 * {@link PageFactory#initElements(org.openqa.selenium.SearchContext, Object)}
	 * by also calling {@link #initElementsJS(ElementLocatorFactory, Object)},
	 * thus enabling support for {@literal @FindByJS} annotations.
	 *
	 * @param searchContext The driver that will be used to look up the elements
	 * @param page   The object with WebElement and List&lt;WebElement&gt; fields
	 *               that should be proxied.
	 */
	public static void initElements(SearchContext searchContext, Object page) {
	    final WebDriver webDriverRef = (WebDriver) searchContext;
	    initElements(new DefaultElementLocatorFactory(searchContext), page);
	    initElementsJS(new JSElementLocatorFactory(webDriverRef), page);
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
		initElementsJS(new JSFieldDecorator(factoryRef), page);
	}


	/**
	 * Similar to the other "initElements" methods, but takes an
	 * {@link FieldDecorator} which is used for decorating each of the fields
	 * if they are annotated by {@literal @FindByJS}.
	 *
	 * @param decorator the decorator to use
	 * @param page      The object to decorate the fields of
	 */
	public static void initElementsJS(FieldDecorator decorator, Object page) {
		Class<?> proxyIn = page.getClass();
		while (proxyIn != Object.class) {
			proxyFieldsJS(decorator, page, proxyIn);
			proxyIn = proxyIn.getSuperclass();
		}
	}

	private static void proxyFieldsJS(FieldDecorator decorator, Object page, Class<?> proxyIn) {
		Field[] fields = proxyIn.getDeclaredFields();
		for (Field field : fields) {
			Object value = decorator.decorate(page.getClass().getClassLoader(), field);
			if (value != null) {
				// if we get here it means it has an annotation @FindByJS
				try {
					field.setAccessible(true);
					field.set(page, value);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	  }
}
