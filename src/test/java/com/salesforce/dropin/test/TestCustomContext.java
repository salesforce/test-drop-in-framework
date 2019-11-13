/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test;

import java.util.logging.Level;

import com.salesforce.dropin.common.ContextProvider;
import com.salesforce.dropin.test.customcontext.TestContextForFailure;
import com.salesforce.dropin.test.customcontext.TestContextImpl;
import com.salesforce.dropin.test.customcontext.TestData;
import com.salesforce.dropin.test.customcontext.TestLogger;
import com.salesforce.dropin.test.customcontext.TestReport;
import com.salesforce.dropin.test.defaultcontext.TestContext;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Tests using the custom context which is injected via the
 * System property "dropin.context.implclassname".
 * @author gneumann
 */
public class TestCustomContext {
	@Test(groups = "beforeCustomInstantiation")
	public void testContextWithoutInitializing() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		Assert.assertNull(tc.data(), "Context is already instantiated");
	}

	@Test(dependsOnGroups = "beforeCustomInstantiation", groups = "afterCustomInstantiation")
	public void testContextAfterInitializing() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		tc.initialize();
		Assert.assertNotNull(tc.data(), "Context is not yet instantiated");
		
		// prime the context for below test cases
		tc.data().setData("foo", "bar");
		tc.logger().log(Level.FINE, "fine message");
		tc.report().fail("foo");
	}

	@Test(dependsOnGroups = "beforeCustomInstantiation", groups = "switchingContextProhibited",
		  expectedExceptions = IllegalStateException.class)
	public void testTrySwitchingContext() {
		ContextProvider.getTestContext(TestContextForFailure.class);
	}

	@Test(dependsOnGroups = "beforeCustomInstantiation", groups = "switchingContextProhibited",
		  expectedExceptions = IllegalStateException.class)
	public void testTrySwitchingContextViaSystemProperty() {
		// set System property setting to refer to custom context implementation
		System.setProperty("dropin.context.implclassname", "foo");
		ContextProvider.getTestContext(TestContext.class);
	}

	@Test(dependsOnGroups = "afterCustomInstantiation")
	public void testTestDataUsingCustomContext() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		TestData data = (TestData) tc.data();
		Assert.assertNotNull(data.getData("foo"), "Have to get a value for previously set key/value pair");
		Assert.assertEquals(data.getData("foo"), "custom bar");
	}

	@Test(dependsOnGroups = "afterCustomInstantiation")
	public void testLoggerUsingCustomContext() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		TestLogger logger = (TestLogger) tc.logger();
		Assert.assertEquals(logger.getLastMsg(), "custom message");
		Assert.assertEquals(logger.formattedLogging("blah"), "custom blah");
		logger.log(Level.INFO, "info message");
		Assert.assertEquals(logger.getLastMsg(), "custom message");
		Assert.assertEquals(logger.getLastLogLevel(), Level.ALL);
	}

	@Test(dependsOnGroups = "afterCustomInstantiation")
	public void testReportUsingCustomContext() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		TestReport report = (TestReport) tc.report();
		Assert.assertEquals(report.getFailMsg(), "custom foo");
	}

	@BeforeMethod
	public void beforeMethod() {
		// set System property setting to refer to custom context implementation
		System.setProperty(ContextProvider.CONTEXT_IMPL_CLASS_NAME, TestContextImpl.class.getName());
	}

	@AfterMethod
	public void afterMethod() {
		// remove System property setting
		System.setProperty(ContextProvider.CONTEXT_IMPL_CLASS_NAME, "");
	}
}
