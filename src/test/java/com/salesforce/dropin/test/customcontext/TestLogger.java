/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test.customcontext;

import java.util.logging.Level;

import com.salesforce.dropin.test.defaultcontext.ILogger;

/**
 * Custom implementation of {@link ILogger} interface.
 * @author gneumann
 */
public class TestLogger implements ILogger {
	private Level lastLogLevel;
	private String lastMsg;

	@Override
	public void log(Level level, String msg) {
		lastLogLevel = Level.ALL;
		lastMsg = "custom message";
	}

	@Override
	public void log(Level level, String msg, Object[] params) { /* not implemented for test */ }

	@Override
	public void log(Level level, String msg, Throwable thrown) { /* not implemented for test */ }

	@Override
	public String formattedLogging(String msg) {
		return "custom " + msg;
	}

	public Level getLastLogLevel() {
		return lastLogLevel;
	}
	
	public String getLastMsg() {
		return lastMsg;
	}
}
