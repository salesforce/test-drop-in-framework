/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package org.dropin.test.defaultcontext;

import org.dropin.common.BaseData;
import org.dropin.common.BaseReport;
import org.dropin.common.BaseContext;

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
