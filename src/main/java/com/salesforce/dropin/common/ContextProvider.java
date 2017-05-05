/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.common;

import com.salesforce.dropin.common.BaseContext;

/**
 * The ContextProvider helps the owner of the test project and partner teams,
 * be them either inside or outside the company, to run the very same tests
 * provided by the owner in every team's test environment.
 */
public final class ContextProvider {
	/**
	 * The value of this system property is used to load the actual implementation
	 * of the context interface provided by the owner of the test project.
	 * <p>
	 * You can enter this system property along with the run command:
	 * -Ddropin.context.implclassname=dropin.custom.[your company name].[your impl class name]
	 * e.g.
	 * -Ddropin.context.implclassname=dropin.custom.acme.TestContextImpl
	 * <p>
	 * This mechanism allows you and your partner team(s) inside or outside your company
	 * to use different implementations to adapt the tests to their test environments.
	 */
	public static final String CONTEXT_IMPL_CLASS_NAME = "dropin.context.implclassname";
	
	/*
	 * It is not possible to use type T for a static field
	 * and so we use the 2nd best: the super interface
	 */
	private static BaseContext context;
	private static String interfaceName;
	private static String implClassName;
	private static boolean isDefinedBySystemProperty;

	/**
	 * Gets the test context to be used during test execution.
	 * <p>
	 * By providing an interface extending from interface {@link BaseContext}
	 * in the parameter <b>type</b>, one can define which methods for logging,
	 * reporting, and so on will be used by the tests. Your partner, be it another team
	 * or company or even Salesforce, will then provide its own implementation to adapt
	 * the tests to their test environment.
	 * <p>
	 * The first call of this method will "freeze" the interface type the implementation
	 * has to provide, load the context implementation class by examining the System
	 * property <pre>-Ddropin.context.implclassname=[your test context impl class]</pre>
	 * and cache it. All following calls verify that the same interface and implementation
	 * is requested before returning the cached context class.
	 * <p>
	 * Please note: switching context during test execution may be fatal if information
	 * of previous context has been cached in calling classes. Therefore this method will
	 * prohibit any attempts to switch context.
	 *
	 * @param <T> type provided has to extend from interface {@link BaseContext}
	 * @param type interface defined by the owner of the test project
	 * @return class implementing the interface defined in parameter <b>type</b>
	 * @throws IllegalStateException an attempt has been detected to switch to
	 * a different interface or implementation during the lifetime of this class.
	 */
	@SuppressWarnings("unchecked")
	public static synchronized <T extends BaseContext> T getTestContext(final Class<T> type) {
		assert type.isInterface() : "Type provided has to be an interface, not a class.";

		String implClsName = System.getProperty(CONTEXT_IMPL_CLASS_NAME, "");
		if (context != null) {
			// prohibits loading context with a different interface 
			if (!interfaceName.equals(type.getName())) {
				// switching context during test execution may be fatal if information of previous context has been cached
				throw new IllegalStateException(ContextProvider.class.getName() + " must not be used for a different context");
			}

			// prohibit has the system property setting changed since the last call?
			boolean tempIsDefinedBySystemProperty = !"".equals(implClsName);
			if (isDefinedBySystemProperty != tempIsDefinedBySystemProperty) {
				// switching context during test execution may be fatal if information of previous context has been cached
				throw new IllegalStateException("Must not switch to a different context impl class during test execution");
			}
			
			if (!implClsName.equals("") && !implClsName.equals(implClassName)) {
				// switching context during test execution may be fatal if information of previous context has been cached
				throw new IllegalStateException("Must not switch to a different context impl class during test execution");
			}
			// checks above have ensured it's now safe to cast and then return the cached context 
			return (T) context;
		}

		// cache full interface name for future reference
		interfaceName = type.getName();

		if (!"".equals(implClsName)) {
			isDefinedBySystemProperty = true;
			implClassName = implClsName;
		} else {
			isDefinedBySystemProperty = false;
			implClassName = type.getName() + "Impl";
			System.out.println("Info: a test context impl class has not been defined via System property -D"
					+ CONTEXT_IMPL_CLASS_NAME + "=<your context impl class>");
		}
		System.out.println("Info: trying to load " + implClassName + ".");

		// have to use a variable of type T because field "context" is using the super type interface.
		T tempContext = null;
		try {
	        tempContext = type.cast(Class.forName(implClassName).newInstance());
			System.out.println("Info: successfully loaded " + implClassName);
	        // as seen above, we will cast the "context" field in all following
	        // calls of this method to the proper interface type.
	        context = tempContext;
	    } catch(final InstantiationException|IllegalAccessException|ClassNotFoundException e) {
	        throw new IllegalStateException(e);
	    }
		return tempContext;
	}
}
