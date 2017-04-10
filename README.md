# test-drop-in-framework
By using this project a customer can define a test context and SFDC can define a second context. In essence,
it allows SFDC to make sure that the code gets run in a proper context with the proper test cases without
having to modify or rewrite any customer code or test cases, etc.

When the customer runs her tests, the customer leaves the switch in "customer mode". If SFDC runs the tests,
they flip the switch to their test context.

## Requirements
To use this project the following requirements have to be met:
- Java 1.8
- TestNG and Selenium 2.x on the class path

## Recommended
- Maven to resolve dependencies on TestNG and Selenium
- If you are running in Eclipse, you will want the m2e (Maven Integration for Eclipse) plugin.

Please note: if you want to use Microsoft Internet Explorer for test execution, you have to
explicitly add dependencies on JNA and JNA-Platform.

## Development
1. Run "mvn eclipse:eclipse" to generate .project file and download required jars for development in Eclipse.
2. Run "mvn clean" to install any dependencies.
3. Start up eclipse, import project cqe-test-container.
4. Run "mvn compile jar:jar" to compile project and create jar file target/cqe-test-container-1.0.0.jar
5. Run "mvn install:install-file -Dfile=target/cqe-test-container-1.0.0.jar -DpomFile=pom.xml" to deploy jar file to local repository

## Prepare your test project
1. Add the cqe-test-container-1.0.0.jar to your build path.
2. In your test project create a package which contains your test context interfaces and the test context implementation class.
   Recommended package name: interchange.custom.<company-name> 
3. In this package define an interface which describes the context as per your needs.

	// Example context interface:
	public interface TestContext extends BaseTestContext {
		IData<String> data();
		ILogger logger();
		IReport report();

		// always provide a custom initialization method
		void initialize(TestCase testCase);
	
		// helper classes
		WebDriverHelper wh();
	}

4. Create the various interfaces, e.g. IData:
	
	// BaseData<T> is provided by cqe-test-container
	public interface IData<T> extends BaseData<T> {
		T getCommonData(String key);
		void setCommonData(String key, T value);
	}

5. Create classes implementing the various interfaces.
6. Create the class implementing the test context defined in step 3.

## Initialization of the TestContext

The TestContext interface requires implementation of then initialize(testCase) method, where the objects
stored in the context get initialized. A good place is in a method run during initialization of a test class
or in methods annotated with @BeforeClass if you are using TestNG or JUnit.  

## Use the test context in your test project
1. Where-ever in your test project you need to access something stored in the test context, first get the handle
   to the TestContext this way: 

	TestContext tc = (new TestContextProvider()).getTestContext(TestContext.class);

2. Access the data in the context:

	String value = tc.data().getCommonData("mykey");

## How to toggle between Test Context implementations?

The first call of method

	TestContextProvider.getTestContext(final Class<T> type)

will "freeze" the interface type the implementation has to provide, load the context implementation class by
examining the system property

	-Dtestcontext.implclassname=[your test context impl class]

and cache it. All following calls to that method will verify that the same interface is requested before
returning the cached context class.

If the test context implementation is in the same package as the test context interface and the class name
has "Impl" as a postfix to the interface name, the system property does not have to be set.
