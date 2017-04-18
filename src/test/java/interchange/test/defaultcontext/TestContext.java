/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package interchange.test.defaultcontext;

import interchange.common.BaseData;
import interchange.common.BaseReport;
import interchange.common.BaseTestContext;

/**
 * Context interface to be implemented by two different context classes.
 * @author gneumann
 */
public interface TestContext extends BaseTestContext {
	BaseData<String> data();
	ILogger logger();
	BaseReport report();
	// custom initialization method
	void initialize();
}
