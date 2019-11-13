/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test.defaultcontext;

import com.salesforce.dropin.common.BaseLogger;

/**
 * Custom interface extending {@link BaseLogger} interface. 
 * @author gneumann
 */
public interface ILogger extends BaseLogger {
	String formattedLogging(String msg);
}
