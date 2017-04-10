/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package interchange.common;

/**
 * The TestContextProvider allows customers and Salesforce CQE team to run
 * the tests provided by the customer in their test environment.
 */
public final class TestContextProvider {
	/**
	 * The value of this system property is used to load the actual implementation
	 * of the customized TestContext interface.
	 * <p>
	 * You can enter this system property along with the run command:
	 * -Dtestcontext.implclassname=interchange.custom.<your company name>.<your impl class name>
	 * e.g.
	 * -Dtestcontext.implclassname=interchange.custom.acme.TestContextImpl
	 * <p>
	 * This mechanism allows customers and Salesforce CQE to use different implementations
	 * to adapt the tests to their test environments.
	 */
	public static final String TEST_CONTEXT_CLASS_NAME = "testcontext.implclassname";
	
	/*
	 * It is not possible to use type T for a static field
	 * and so we use the 2nd best: the super interface
	 */
	private static BaseTestContext context;
	private static String interfaceName;
	
	/**
	 * Gets the test context to be used during test execution.
	 * <p>
	 * By providing an interface extending from interface {@link BaseTestContext}
	 * in the parameter <b>type</b>, customers can define which methods for logging,
	 * reporting, and so on will be used by the tests. The Salesforce CQE team
	 * will then provide its own implementation to adapt the tests to their test
	 * environment.
	 * <p>
	 * The first call of this method will "freeze" the interface type the implementation
	 * has to provide, load the context implementation class by examining the System
	 * property <pre>-Dtestcontext.implclassname=[your test context impl class]</pre>
	 * and cache it. All following calls verify that the same interface is requested
	 * before returning the cached context class.  
	 *   
	 * @param type interface extending from interface {@link BaseTestContext}
	 * @return class implementing the interface defined in param <b>type</b>
	 * @throws IllegalStateException in case class could not be loaded
	 */
	// It is not possible to use type T for a static method.
	@SuppressWarnings("unchecked")
	public <T extends BaseTestContext> T getTestContext(final Class<T> type) {
		assert type.isInterface() : "Type provided has to be an interface, not a class.";

		if (interfaceName == null || "".equals(interfaceName)) {
			// cache full interface name for future reference
			interfaceName = type.getName();
		} else {
			// prohibits loading context with a different interface 
			if (!interfaceName.equals(type.getName())) {
				throw new IllegalArgumentException("TestContextProvider must not be used for a different context");
			}
		}

		if (context != null) {
			// checks above have ensured it's now safe to cast and then return the cached context 
			return (T) context;
		}

		// need to load implementation
		boolean isSystemPropertyDefined = true;
		String implClassName = System.getProperty(TEST_CONTEXT_CLASS_NAME);
		if (implClassName == null || "".equals(implClassName)) {
			isSystemPropertyDefined = false;
			implClassName = type.getName() + "Impl";
			System.out.println("Info: a test context impl class has not been defined via System property -D" + TEST_CONTEXT_CLASS_NAME
					+ "=<your test context impl class>");
			System.out.println("Info: trying to load " + implClassName + " instead.");
		}

		// have to use a variable of type T because field "context" is using the super type interface.
		T tempContext = null;
		try {
	        tempContext = type.cast(Class.forName(implClassName).newInstance());
			System.out.println("Info: successfully loaded " + implClassName);
	        // as seen above, we will cast the "context" field in all following
	        // calls of this method to the proper interface type.
	        context = tempContext;
	    } catch(final InstantiationException|IllegalAccessException|ClassNotFoundException e) {
	    	if (!isSystemPropertyDefined) {
				System.err.println("A test context impl class has not been defined via System property -D" + TEST_CONTEXT_CLASS_NAME
						+ "=<your test context impl class>");
	    	}
	        throw new IllegalStateException(e);
	    }
		return tempContext;
	}
}
