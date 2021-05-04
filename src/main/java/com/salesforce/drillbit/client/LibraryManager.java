/**
 * 
 */
package com.salesforce.drillbit.client;

import com.google.common.base.Strings;

/**
 * @author gneumann
 *
 */
public class LibraryManager {
	private static ThreadLocal<String> testCaseName = new ThreadLocal<>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private LibraryManager() {
		
	}
	
	public static String getTestCaseName() {
		if (Strings.isNullOrEmpty(testCaseName.get())) {
			testCaseName.set("not-yet-defined");
		}
		return testCaseName.get();
	}
	
	public static void setTestCaseName(String name) {
		if (Strings.isNullOrEmpty(name))
			throw new IllegalArgumentException("New value for test case name must not be null or empty");
		testCaseName.set(name);
	}
}
