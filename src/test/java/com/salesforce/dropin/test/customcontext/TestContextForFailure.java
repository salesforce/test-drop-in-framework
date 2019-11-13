/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test.customcontext;

import com.salesforce.dropin.common.BaseContext;

/**
 * Context interface to be used by a negative test case trying
 * to switch context, which is supposed to fail.
 * @author gneumann
 */
public interface TestContextForFailure extends BaseContext {
	// custom initialization method
	void initialize();
}
