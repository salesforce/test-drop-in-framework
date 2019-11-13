/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test;

import org.testng.annotations.Test;

import java.util.logging.Level;

import com.salesforce.dropin.common.ContextProvider;
import com.salesforce.dropin.test.defaultcontext.TestContext;
import com.salesforce.dropin.test.defaultcontext.TestData;
import com.salesforce.dropin.test.defaultcontext.TestLogger;
import com.salesforce.dropin.test.defaultcontext.TestReport;
import org.testng.Assert;

/**
 * Tests using the default context which is found by using the name of the test context interface.
 * @author gneumann
 */
public class TestDefaultContext {
	@Test(groups = "beforeDefaultInstantiation")
	public void testContextWithoutInitializing() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		Assert.assertNull(tc.data(), "Context is already instantiated");
	}

	@Test(dependsOnGroups = "beforeDefaultInstantiation", groups = "afterDefaultInstantiation")
	public void testContextAfterInitializing() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		tc.initialize();
		Assert.assertNotNull(tc.data(), "Context is not yet instantiated");
		
		// prime the context for below test cases
		tc.data().setData("foo", "bar");
		tc.logger().log(Level.FINE, "fine message");
		tc.report().fail("foo");
	}

	@Test(dependsOnGroups = "afterDefaultInstantiation")
	public void testTestDataUsingDefaultContext() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		TestData data = (TestData) tc.data();
		Assert.assertNull(data.getData("blah"), "Must not get a value for unset key/value pair");
		Assert.assertNotNull(data.getData("foo"), "Have to get a value for previously set key/value pair");
		Assert.assertEquals(data.getData("foo"), "bar");
	}

	@Test(dependsOnGroups = "afterDefaultInstantiation")
	public void testLoggerUsingDefaultContext() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		TestLogger logger = (TestLogger) tc.logger();
		Assert.assertEquals(logger.getLastMsg(), "fine message");
		Assert.assertEquals(logger.formattedLogging("blah"), "formatted blah");
		logger.log(Level.INFO, "info message");
		Assert.assertEquals(logger.getLastMsg(), "info message");
		Assert.assertEquals(logger.getLastLogLevel(), Level.INFO);
	}

	@Test(dependsOnGroups = "afterDefaultInstantiation")
	public void testReportUsingDefaultContext() {
		TestContext tc = ContextProvider.getTestContext(TestContext.class);
		TestReport report = (TestReport) tc.report();
		Assert.assertEquals(report.getFailMsg(), "foo");
	}
}
