/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test.defaultcontext;

import java.util.HashMap;

import com.salesforce.dropin.common.BaseData;

/**
 * Default implementation of {@link BaseData} interface.
 * @author gneumann
 */
public class TestData implements BaseData<String> {
	private static HashMap<String, String> map = new HashMap<>();

	@Override
	public String getData(String key) {
		return map.get(key);
	}


	@Override
	public void setData(String key, String value) {
		map.put(key, value);
	}
}
