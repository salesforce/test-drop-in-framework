/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test.defaultcontext;

import com.salesforce.dropin.common.BaseData;
import com.salesforce.dropin.common.BaseReport;
import com.salesforce.dropin.common.BaseContext;

/**
 * Context interface to be implemented by two different context classes.
 * @author gneumann
 */
public interface TestContext extends BaseContext {
	BaseData<String> data();
	ILogger logger();
	BaseReport report();
	// custom initialization method
	void initialize();
}
