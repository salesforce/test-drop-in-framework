/**
 * 
 */
package com.salesforce.selenium.support.event;

import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author gneumann
 *
 */
public class Log2TestCase extends AbstractWebDriverEventListener {
	private static final String WEBDRIVER_FIELD = "webDriver";
	private static final String WEBELEM_VAR = "webElem";
	private static final String WEBELEM_TYPE = "WebElement";

	private StringBuffer code = new StringBuffer();
	private Hashtable<String, String> webElementVars = new Hashtable<>();

	@Override
	public void afterFindElementByWebDriver(Step step, WebElement returnedElement, By by) {
		String locator = Step.getLocatorFromWebElement(returnedElement);
		String varName = webElementVars.get(locator);
		if (varName == null) {
			varName = String.format("%s%d", WEBELEM_VAR, webElementVars.size() + 1);
			webElementVars.put(locator, varName);
			// WebElement webElem1
			code.append(WEBELEM_TYPE).append(" ").append(varName);
		} else {
			// webElem2
			code.append(varName);
		}
		// = webDriver.findElement(By.id("foo"));
		code.append(" = ").append(WEBDRIVER_FIELD).append(".findElement(").append(Step.getLocatorFromBy(by)).append(");");
		code.append(System.lineSeparator());
	}

	@Override
	public void afterClick(Step step, WebElement element) {
		String locator = Step.getLocatorFromWebElement(element);
		String varName = webElementVars.get(locator);
		if (varName == null) {
			// new WebElement(By.id("foo")).click();
			code.append("new ").append(WEBELEM_TYPE).append("(").append(locator).append(").").append(step.getCmd()).append("();");
		} else {
			// webElem2.click();
			code.append(varName).append(".").append(step.getCmd()).append("();");
		}
		code.append(System.lineSeparator());
	}

	@Override
	public void afterSendKeys(Step step, WebElement element, CharSequence... keysToSend) {
		StringBuffer textBuffer = new StringBuffer();
		if ((keysToSend != null) && (keysToSend.length > 0)) {
			for (int i = 0; i < keysToSend.length; i++) {
				textBuffer.append(keysToSend[i]);
			}
		}

		String locator = Step.getLocatorFromWebElement(element);
		String varName = webElementVars.get(locator);
		if (varName == null) {
			// new WebElement(By.id("foo")).
			code.append("new ").append(WEBELEM_TYPE).append("(").append(locator).append(").");
			// sendKeys("some text");
			code.append(step.getCmd()).append("(\"").append(textBuffer.toString()).append("\");");
		} else {
			// webElem2.sendKeys("some text");
			code.append(varName).append(".").append(step.getCmd()).append("(\"").append(textBuffer.toString()).append("\");");
		}
		code.append(System.lineSeparator());
	}

	/* (non-Javadoc)
	 * @see com.salesforce.selenium.support.event.WebDriverEventListener#closeListener()
	 */
	@Override
	public void closeListener() {
		System.out.println(code.toString());
	}
}
