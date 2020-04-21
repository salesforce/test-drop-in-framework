/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test;

import org.testng.annotations.Test;

import com.salesforce.selenium.support.event.FullJSONLogger;
import com.salesforce.selenium.support.event.Step;
import com.salesforce.selenium.support.event.Step.Cmd;
import com.salesforce.selenium.support.event.Step.Type;

/**
 * Tests using the Step object.
 * @author gneumann
 */
public class TestFullJSONLogger {

	/**
	 * Ensure that the logger can write many log entries in batches to the same file.
	 */
	@Test()
	public void testWritingBigLogfile() {
		doWriting(123456);
	}

	/**
	 * Ensure that the logger can write few log entries to the same file.
	 */
	@Test()
	public void testWritingSmallLogfile() {
		doWriting(567);
	}

	/**
	 * Ensure that the logger can write exactly the BATCHSIZE number of log entries to the same file.
	 */
	@Test()
	public void testWritingBatchSizeLogfile() {
		doWriting(1000);
	}

	/**
	 * Ensure that the logger does not write empty list of log entries to file.
	 */
	@Test()
	public void testWritingEmptyLogfile() {
		doWriting(0);
	}

	private void doWriting(int numOfRecords) {
		FullJSONLogger logger = new FullJSONLogger("TestFullJSONLogger" + System.currentTimeMillis());
		// create log entries
		for (int stepNo = 1; stepNo <= numOfRecords; stepNo++) {
			logger.afterBack(new Step(Type.AfterAction, stepNo, Cmd.back));
		}
		logger.closeListener();
	}
}
