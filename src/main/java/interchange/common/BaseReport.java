/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package interchange.common;

import org.testng.AssertJUnit;

/**
 * Basic interface for mapping test case results to the real
 * report mechanism used by the test environment in use.
 * <p>
 * If test cases tapping into {@link AbstractBaseTestContext} want to call any other
 * report methods, the recommended way to provide those is to extend
 * this interface by the methods needed and provide Salesforce Central QE
 * with this interface in package <pre>interchange.custom.[your-company-name]</pre>
 * 
 * @author gneumann
 */
public interface BaseReport {
	/**
	 * Fails a test with the given message.
	 */
	default void fail(String message) {
		AssertJUnit.fail(message);
	}
	
	/**
	 * Fails a test with no message.
	 */
	default void fail() {
		AssertJUnit.fail();
	}

	/**
	 * Passes a test with the given message.
	 * <p>
	 * Although no such call exists in JUnit, some test environments
	 * are explicitly reporting a <b>PASS</b> for test cases which
	 * have not failed.
	 */
	default void pass(String message) { /* empty default implementation */ }
	
	/**
	 * Passes a test with no message.
	 * <p>
	 * Although no such call exists in JUnit, some test environments
	 * are explicitly reporting a <b>PASS</b> for test cases which
	 * have not failed.
	 */
	default void pass() { /* empty default implementation */ }
}
