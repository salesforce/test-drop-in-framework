/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.common;

import org.openqa.selenium.WebDriver;

/**
 * Abstract context which assumes that the test is using
 * WebDriver for UI automation.
 * 
 */
public abstract class AbstractBaseContext implements BaseContext {
	private ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
	
	/**
	 * Gets current WebDriver instance or NULL if it has not been
	 * instantiated and set here.
	 * @return driver instance or NULL
	 */
	@Override
	public WebDriver wd() {
		assert webDriver.get() != null : "Test context is not yet initialized";
		return webDriver.get();
	}

	/**
	 * Sets the current WebDriver instance. This instance should
	 * be ready for use by any other piece of code.
	 * @param driver fully initiated instance or NULL if you want
	 * to wipe out any previous instance.
	 */
	@Override
	public void setWebDriver(WebDriver driver) {
		assert driver != null : "Trying to cache NULL as current WebDriver instance";
		webDriver.set(driver);
	}
}
