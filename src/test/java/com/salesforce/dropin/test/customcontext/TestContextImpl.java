/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test.customcontext;

import com.salesforce.dropin.test.defaultcontext.TestContext;
import org.openqa.selenium.WebDriver;

/**
 * Custom test context implementation providing handles to
 * implementation classes in package com.salesforce.dropin.test.customcontext.
 * @author gneumann
 */
public class TestContextImpl implements TestContext {
	private TestData config;
	private TestLogger logger;
	private TestReport report;

	@Override
	public WebDriver wd() {
		// not implemented for test
		return null;
	}

	@Override
	public void setWebDriver(WebDriver driver) { /* not implemented for test */ }

	@Override
	public TestData data() {
		return config;
	}

	@Override
	public TestLogger logger() {
		return logger;
	}

	@Override
	public TestReport report() {
		return report;
	}

	/**
	 * Instantiates implementation classes in package com.salesforce.dropin.test.customcontext.
	 */
	@Override
	public void initialize() {
		config = new TestData();
		logger = new TestLogger();
		report = new TestReport();
	}

}
