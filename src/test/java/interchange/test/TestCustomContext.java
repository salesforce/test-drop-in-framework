/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package interchange.test;

import org.testng.annotations.Test;

import interchange.common.TestContextProvider;
import interchange.test.customcontext.TestContextImpl;
import interchange.test.customcontext.TestData;
import interchange.test.customcontext.TestLogger;
import interchange.test.customcontext.TestReport;
import interchange.test.defaultcontext.TestContext;

import org.testng.annotations.BeforeClass;

import java.util.logging.Level;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

/**
 * Tests using the custom context which is injected via the System property "testcontext.implclassname".
 * @author gneumann
 */
public class TestCustomContext {
	@Test(groups = "beforeCustomInstantiation")
	public void testContextWithoutInitializing() {
		TestContext tc = (new TestContextProvider()).getTestContext(TestContext.class);
		Assert.assertNull(tc.data(), "Context is already instantiated");
	}

	@Test(dependsOnGroups = "beforeCustomInstantiation", groups = "afterCustomInstantiation")
	public void testContextAfterInitializing() {
		TestContext tc = (new TestContextProvider()).getTestContext(TestContext.class);
		tc.initialize();
		Assert.assertNotNull(tc.data(), "Context is not yet instantiated");
		
		// prime the context for below test cases
		tc.data().setData("foo", "bar");
		tc.logger().log(Level.FINE, "fine message");
		tc.report().fail("foo");
	}

	@Test(dependsOnGroups = "afterCustomInstantiation")
	public void testTestDataUsingCustomContext() {
		TestContext tc = (new TestContextProvider()).getTestContext(TestContext.class);
		TestData data = (TestData) tc.data();
		Assert.assertNotNull(data.getData("foo"), "Have to get a value for previously set key/value pair");
		Assert.assertEquals(data.getData("foo"), "custom bar");
	}

	@Test(dependsOnGroups = "afterCustomInstantiation")
	public void testLoggerUsingCustomContext() {
		TestContext tc = (new TestContextProvider()).getTestContext(TestContext.class);
		TestLogger logger = (TestLogger) tc.logger();
		Assert.assertEquals(logger.getLastMsg(), "custom message");
		Assert.assertEquals(logger.formattedLogging("blah"), "custom blah");
		logger.log(Level.INFO, "info message");
		Assert.assertEquals(logger.getLastMsg(), "custom message");
		Assert.assertEquals(logger.getLastLogLevel(), Level.ALL);
	}

	@Test(dependsOnGroups = "afterCustomInstantiation")
	public void testReportUsingCustomContext() {
		TestContext tc = (new TestContextProvider()).getTestContext(TestContext.class);
		TestReport report = (TestReport) tc.report();
		Assert.assertEquals(report.getFailMsg(), "custom foo");
	}

	@BeforeClass
	public void beforeClass() {
		// set System property setting to refer to custom context implementation
		System.setProperty("testcontext.implclassname", TestContextImpl.class.getName());
	}

	@AfterClass
	public void afterClass() {
		// remove System property setting
		System.setProperty("testcontext.implclassname", "");
	}
}
