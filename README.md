# test-drop-in-framework

This repository contains these major deliverables:
a) the Test Drop-in Framework,
b) a WebDriver implementation offering enhanced logging capabilities, and
c) the @FindByJS annotation support

## Test Drop-in Framework
The ContextProvider helps the owner of a test project and her partner teams, be
them either inside or outside the company, to run the very same tests
provided by the owner in every team's test environment. In essence, it allows
to make sure that the code gets run in a proper context with the proper test
cases without having to modify or rewrite any customer code or test cases, etc.

When the owner runs her tests, the owner leaves the switch in "owner mode" aka
"default mode". If a partner team runs the tests, they flip the switch to their
test context.

The main classes are:
* com.salesforce.dropin.common.BaseContext
* com.salesforce.dropin.common.ContextProvider

## Enhanced WebDriver

This WebDriver is basically a wrapper around an arbitrary WebDriver instance. It supports
registering WebDriverEventListener implementations, e.g. for logging purposes.

This is an extended version of org.openqa.selenium.support.events.EventFiringWebDriver.

The main class and interface are:
* com.salesforce.selenium.support.event.EventFiringWebDriver
* com.salesforce.selenium.support.event.WebDriverEventListener

## @FindByJS Annotation Support

WebDriver supports three annotations @FindBy, @FindBys, @FindAll which facilitate
the use of the Page Object model.

Occasionally there is the need to look up WebElement objects using JavaScript, e.g.
when dealing with UI elements inside shadowDOM. This is particularly the case when
writing UI test automation for applications using Salesforce Lightning Web Components
LWC.

Until WebDriver comes up with a solution how to deal with shadowDOM, we recommend to
use the @FindByJS annotation.

The main class and interface are:
* com.salesforce.selenium.support.findby.FindByJS
* com.salesforce.selenium.support.findby.JSPageFactory

## Requirements
To use this project the following requirements have to be met:
- Java 1.8
- TestNG and Selenium WebDriver on the class path

## Recommended
- Maven to resolve dependencies on TestNG and Selenium
- If you are running in Eclipse, you will want the m2e (Maven Integration for
Eclipse) plugin.

Please note: if you want to use Microsoft Internet Explorer for test execution,
you have to explicitly add dependencies on JNA and JNA-Platform.

## Development
0. Pull the right branch of test-drop-in-framework:
 a. When your project is based on WebDriver 3.x or newer, pull from branch 'master'
 b. When your project is based on WebDriver 2.x, pull from branch 'selenium2'
1. Run `mvn eclipse:eclipse` to generate .project file and download required jars
for development in Eclipse.
2. Run `mvn clean` to install any dependencies.
3. Start up eclipse, import project test-drop-in-framework.
4. Compile project and create jar file:
 a. When your project is based on WebDriver 3.x or newer, run
`mvn compile jar:jar` which creates jar file target/test-drop-in-framework-2.0.0.jar
 b. When your project is based on WebDriver 2.x, run
`mvn compile jar:jar -f pom.xml` which creates jar file target/test-drop-in-framework-selenium2-2.0.0.jar
5. Deploy jar file to local repository:
 a. When your project is based on WebDriver 3.x or newer, run
`mvn install:install-file -Dfile=target/test-drop-in-framework-2.0.0.jar -DpomFile=pom.xml`
b. When your project is based on WebDriver 2.x, run
`mvn install:install-file -Dfile=target/test-drop-in-framework-selenium2-2.0.0.jar -DpomFile=pom.xml`

Please note: the framework has to prohibit context switching during runtime.
Therefore it is not possible to run both test classes via "mvn test" and have all
tests passing!

6. Run `mvn -Dtest=TestDefaultContext test` to run all tests provided for testing
default context.
7. Run `mvn -Dtest=TestCustomContext test` to run all tests provided for testing
custom context.

## How to use the Test Drop-in Framework
### Prepare your test project
1. Add the test drop-in framework jar created in Development step 5 to your build path.
2. In your test project create a package which contains your test context interfaces
and the test context implementation class. Recommended package name:
`dropin.custom.<company-name>`
3. In this package define an interface which describes the context as per your needs.

```java
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
```

4. Create the various interfaces, e.g. IData:

```java
// BaseData<T> is provided by test-drop-in-framework
public interface IData<T> extends BaseData<T> {
	T getCommonData(String key);
	void setCommonData(String key, T value);
}
```

5. Create classes implementing the various interfaces.
6. Create the class implementing the test context defined in step 3.

### Initialization of the TestContext

The TestContext interface (see example above) requires implementation of then
initialize(testCase) method, where the objects stored in the context get initialized.
A good place is in a method run during initialization of a test class or in methods
annotated with @BeforeClass if you are using TestNG or JUnit.  

### Use the test context in your test project
1. Where-ever in your test project you need to access something stored in the test
context, first get the handle to the TestContext (see interface example above) this way: 

```java
TestContext tc = ContextProvider.getTestContext(TestContext.class);
```

2. Access the data in the context:

```java
String value = tc.data().getCommonData("mykey");
```

### How to toggle between Test Context implementations?

The first call of method

```java
ContextProvider.getTestContext(final Class<T> type)
```

will "freeze" the interface type and the implementation class.

If the test context implementation is in the same package as the test context
interface and the class name has "Impl" as a postfix to the interface name, the
call to getTestContext() will try and instantiate this so-called "default context"
class unless it is overridden by a "custom context" class. 

To use a so-called "custom context" you have to set the system property

```
-Ddropin.context.implclassname=[your test context impl class]
```

All following calls to that method will return the very same context class instance.

Please note: switching context during test execution may be fatal if information
of previous context has been cached in calling classes. Therefore the
getTestContext() method will prohibit any attempts to switch context.

## Filing bugs

If you find any bugs in *Test Drop-in Framework*, please open a new issue on GitHub
and add as many details as possible, but at least the following:

- Expected outcome
- Actual outcome
- Is reproducible? (Always, Randomly, etc.)
- Reproducer scenario
- Your dev environment (Linux, Mac OS, version, etc.)

## Bug Review SLA

After submitting a bug, you can expect to hear back an answer within the next
five working days.

## Documenting a new release version
1. Go to the Releases [section](https://github.com/salesforce/test-drop-in-framework/releases)
2. Click on the "Draft a new release" button
3. Enter the "tag version" and "Release title" value as "test-drop-in-framework-v\<new-version\>"
4. Write one or more sentences about the changes pushed ([example](https://github.com/salesforce/test-drop-in-framework/releases/tag/test-drop-in-framework-v3.0.17)) and publish a release.
