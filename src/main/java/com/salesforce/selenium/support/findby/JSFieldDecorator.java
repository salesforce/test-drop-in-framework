/* 
 * Copyright (c) 2019, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.selenium.support.findby;

import java.lang.reflect.Field;

import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * Allows WebDriver to find an element by using the given JavaScript command
 * in a {@link FindByJS} annotation.
 * 
 * @author gneumann
 * @since 2.1
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
