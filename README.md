# test-drop-in-framework
The ContextProvider helps the owner of a test project and her partner teams,
be them either inside or outside the company, to run the very same tests
provided by the owner in every team's test environment. In essence, it allows
to make sure that the code gets run in a proper context with the proper test
cases without having to modify or rewrite any customer code or test cases, etc.

When the owner runs her tests, the owner leaves the switch in "owner mode" aka
"default mode". If a partner team runs the tests, they flip the switch to their
test context.

## Requirements
To use this project the following requirements have to be met:
- Java 1.8
- TestNG and Selenium WebDriver on the class path

## Recommended
- Maven to resolve dependencies on TestNG and Selenium
- If you are running in Eclipse, you will want the m2e (Maven Integration for Eclipse) plugin.

Please note: if you want to use Microsoft Internet Explorer for test execution, you have to
explicitly add dependencies on JNA and JNA-Platform.

## Development
1. Run "mvn eclipse:eclipse" to generate .project file and download required jars for development in Eclipse.
2. Run "mvn clean" to install any dependencies.
3. Start up eclipse, import project test-drop-in-framework.
4. Run "mvn compile jar:jar" to compile project and create jar file target/test-drop-in-framework-1.0.0.jar
5. Run "mvn install:install-file -Dfile=target/test-drop-in-framework-1.0.0.jar -DpomFile=pom.xml" to deploy jar file to local repository

## Prepare your test project
1. Add the test-drop-in-framework-1.0.0.jar to your build path.
2. In your test project create a package which contains your test context interfaces and the test context implementation class.
   Recommended package name: org.dropin.custom.<company-name> 
3. In this package define an interface which describes the context as per your needs.

	// Example context interface:
	public interface TestContext extends BaseContext {
		IData<String> data();
		ILogger logger();
		IReport report();

		// always provide a custom initialization method
		void initialize(TestCase testCase);
	
		// helper classes
		WebDriverHelper wh();
	}

4. Create the various interfaces, e.g. IData:
	
	// BaseData<T> is provided by test-drop-in-framework
	public interface IData<T> extends BaseData<T> {
		T getCommonData(String key);
		void setCommonData(String key, T value);
	}

5. Create classes implementing the various interfaces.
6. Create the class implementing the test context defined in step 3.

## Initialization of the TestContext

The TestContext interface (see example above) requires implementation of then initialize(testCase) method,
where the objects stored in the context get initialized. A good place is in a method run during
initialization of a test class or in methods annotated with @BeforeClass if you are using TestNG or JUnit.  

## Use the test context in your test project
1. Where-ever in your test project you need to access something stored in the test context, first get the handle
   to the TestContext (see interface example above) this way: 

	TestContext tc = ContextProvider.getTestContext(TestContext.class);

2. Access the data in the context:

	String value = tc.data().getCommonData("mykey");

## How to toggle between Test Context implementations?

The first call of method

	ContextProvider.getTestContext(final Class<T> type)

will "freeze" the interface type and the implementation class.

If the test context implementation is in the same package as the test context interface and the class name
has "Impl" as a postfix to the interface name, the call to getTestContext() will try and instantiate this
so-called "default context" class unless it is overridden by a "custom context" class. 

To use a so-called "custom context" you have to set the system property

	-Dorg.dropin.custom.implclassname=[your test context impl class]

All following calls to that method will return the very same context class instance.

Please note: switching context during test execution may be fatal if information of previous context has
been cached in calling classes. Therefore the getTestContext() method will prohibit any attempts to switch
context.

## Filing bugs

If you find any bugs in *Test Drop-in Framework*, please open a new issue on GitHub and add as many details as possible, but at least the following:

- Expected outcome
- Actual outcome
- Is reproducible? (Always, Randomly, etc.)
- Reproducer scenario
- Your dev environment (Linux, Mac OS, version, etc.)

## Bug Review SLA

After submitting a bug, you can expect to hear back an answer within the next five working days.

