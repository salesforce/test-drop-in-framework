/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package org.dropin.common;

/**
 * Basic interface for mapping test case input data to the real
 * data provider mechanism used by the test environment in use.
 * <p>
 * If test cases tapping into {@link AbstractBaseContext} want to call any other
 * data import methods, the recommended way to provide those is to extend
 * this interface by the methods needed and provide your partner team or company
 * with this interface in package <pre>org.dropin.custom.[your-company-name]</pre>
 * 
 * @author gneumann
 */
public interface BaseData<T> {
	T getData(String key);
	void setData(String key, T value);
}
