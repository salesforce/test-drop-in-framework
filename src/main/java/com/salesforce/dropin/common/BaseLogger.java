/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.common;

import java.util.logging.Level;

/**
 * Basic interface for mapping logging messages during test runtime
 * to the real logger used by the test environment in use.
 * <p>
 * If test cases tapping into {@link BaseContext} want to call any other
 * logging methods, the recommended way to provide those is to extend
 * this interface by the methods needed and provide your partner team or
 * company with this interface in package
 * <pre>dropin.custom.[your-company-name]</pre>
 * 
 * @author gneumann
 */
public interface BaseLogger {
	/**
	 * Log a message, with no arguments. 
	 * @param level One of the message level identifiers, e.g., SEVERE
     * @param msg The string message
	 */
	public void log(Level level, String msg);

    /**
     * Log a message, with an array of object arguments.
     * @param level One of the message level identifiers, e.g., SEVERE
     * @param msg The string message
     * @param params array of parameters to the message
     */
    public void log(Level level, String msg, Object params[]);

    /**
     * Log a message, with associated Throwable information.
     * @param level One of the message level identifiers, e.g., SEVERE
     * @param msg The string message
     * @param thrown Throwable associated with log message.
     */
    public void log(Level level, String msg, Throwable thrown);
}
