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

/**
 * Allows WebDriver to find an element by using the given JavaScript command
 * in a {@literal @FindByJS} annotation.
 * <p>
 * Example:<p>
 * {@code @FindByJS(script = "return document.querySelector(..) ...")}<p>
 * or<p>
 * {@code @FindByJS(shadowPath = "flexipage-aura-wrapper[0] => lightning-icon ...")}
 * <p>
 * @author gneumann
 * @since 3.0.0
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface FindByJS {
	/**
	 * Name any valid JavaScript which allows to find an element on the current page.
	 * The script has to begin with {@code return document.querySelector}.
	 * <p>
	 * This annotation does not support fields of type List. It can only be used to
	 * locate a single web element.
	 * 
	 * @return JavaScript command as text or empty text if not set
	 */
	String script() default "";
	
	/**
	 * Name any valid shadow path which allows to find an element on the current page.
	 * <p>
	 * A valid shadow path has the format:<p>{@code tag-name ["=>" tag-name]*}
	 * <p>
	 * Example:<p>
	 * {@code flexipage-aura-wrapper[0] => lightning-icon[1] => lightning-primitive-icon}
	 * <p>
	 * This annotation does not support fields of type List. It can only be used to
	 * locate a single web element.
	 * 
	 * @return Shadow path as text or empty text if not set
	 */
	String shadowPath() default "";
}
