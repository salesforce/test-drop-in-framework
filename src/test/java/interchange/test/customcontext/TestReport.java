/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package interchange.test.customcontext;

import interchange.common.BaseReport;

/**
 * Custom implementation of {@link BaseReport} interface.
 * @author gneumann
 */
public class TestReport implements BaseReport {
	private String msg;

	/**
	 * Overrides default implementation in interface.
	 */
	@Override
	public void fail(String message) {
		msg = "custom " + message;
	}
	
	public String getFailMsg() {
		return msg;
	}
}
