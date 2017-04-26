/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package org.dropin.common;

import org.openqa.selenium.WebDriver;

/**
 * Basic interface for matching logging, reporting, and data providing
 * to their real implementations used by the test environment in use.
 * <p>
 * If test cases tapping into {@link BaseContext} want to call any other
 * environment specific provider, the recommended way to provide those is to extend
 * this interface by the methods needed and provide your partner team or company 
 * with this interface in package <pre>org.dropin.custom.[your-company-name]</pre>
 * and at the same time extend the abstract class {@link AbstractBaseContext}.
 * 
 * @author gneumann
 */
public interface BaseContext {
	WebDriver wd();
	void setWebDriver(WebDriver driver);

	/**
	 * If Salesforce is the designated partner company to uptake your test
	 * project, it needs to be able to explicitly set the proxy server address
	 * during creation of the WebDriver instance.
	 * <p>
	 * If you do not need this information for your WebDriver factory
	 * method then you can accept the default implementation which returns
	 * a null value.
	 * @return name of proxy server or null
	 */
	default String getProxyServerAddress() {
		return null;
	}
}
