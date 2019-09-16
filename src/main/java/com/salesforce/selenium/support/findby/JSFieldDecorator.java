/* 
 * Copyright (c) 2019, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.selenium.support.findby;

import java.lang.reflect.Field;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * Allows WebDriver to find an element by using the given JavaScript command
 * in a {@link FindByJS} annotation.
 * 
 * @author gneumann
 * @since 3.0.0
 */
public class JSFieldDecorator extends DefaultFieldDecorator {

	/**
	 * CTOR only delegates to super class.
	 * @param factory selected ElementLocator factory
	 */
	public JSFieldDecorator(ElementLocatorFactory factory) {
		super(factory);
	}

	/**
	 * Create proxy for field if it has the annotation {@link FindByJS}.
	 * 
	 * @param loader this class' loader
	 * @param field class member to process
	 * @return proxy for locator or NULL
	 */
	@Override
	public Object decorate(ClassLoader loader, Field field) {
		// only proceed if it is decorated by a @FindByJS
		if (field.getAnnotation(FindByJS.class) == null)
			return null;

		if (!(WebElement.class.isAssignableFrom(field.getType()) || isDecoratableList(field))) {
			return null;
		}

		ElementLocator locator = factory.createLocator(field);
		if (locator == null) {
			return null;
		}

		if (WebElement.class.isAssignableFrom(field.getType())) {
			return proxyForLocator(loader, locator);
		} else if (List.class.isAssignableFrom(field.getType())) {
			return proxyForListLocator(loader, locator);
		} else {
			return null;
		}
	}

	/**
	 * The {@link FindByJS} annotation on fields of type List is
	 * not supported.
	 * @param field currently processed class or instance member
	 * @return false always
	 */
	@Override
	protected boolean isDecoratableList(Field field) {
		return false;
	}
}
