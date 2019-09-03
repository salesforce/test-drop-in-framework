/* 
 * Copyright (c) 2019, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.selenium.support.findby;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * Allows WebDriver to find an element by using the given JavaScript command.
 * 
 * @author gneumann
 * @since 2.1
 */
public @interface FindByJS {
	/**
	 * Name any valid JavaScript which allows to find an element on the current page.
	 * The script has to begin with "return document.querySelector"
	 * 
	 * @return JavaScript command as text
	 */
	String script() default "";
}
