/**
 * 
 */
package com.salesforce.selenium.support.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;

/**
 * @author gneumann
 *
 */
public class StepLogEntry {
	public enum Step { Before, After }
	public enum Cmd { click, clear, close, getText, getWindowHandles, getWindowHandle, navigateTo, quit, type }

	private static long timeMarker;

	private int stepNumber = -1;
	private long timeStamp = -1L; // System.currentTimeMillis()
	private long timeSinceLastStep = -1L; // measured from end of last command to begin of current command
	private long timeElapsedStep = -1L; // measured from begin of current command to end of current command
	private Step typeOfLog;
	private Cmd cmd;
	private String param1;
	private String param2;
	private String returnValue;
	private Throwable issue;

	public StepLogEntry(Step typeOfLog, int stepNumber, Cmd cmd) {
		this(typeOfLog, stepNumber, cmd, null, null, null);
	}
	
	public StepLogEntry(Step typeOfLog, int stepNumber, Cmd cmd, WebElement element) {
		this(typeOfLog, stepNumber, cmd, element, null, null);
	}
	
	public StepLogEntry(Step typeOfLog, int stepNumber, Cmd cmd, WebElement element, String value) {
		this(typeOfLog, stepNumber, cmd, element, value, null);
	}
	
	public StepLogEntry(Step typeOfLog, int stepNumber, Cmd cmd, WebElement element, String sendValue, String returnValue) {
		this.typeOfLog = typeOfLog;
		this.stepNumber = stepNumber;
		this.cmd = cmd;
		this.timeStamp = System.currentTimeMillis();
		
		if (typeOfLog == Step.Before) {
			timeStampsForBegin();
		} else {
			timeStampsForEnd();
		}
		
		switch (cmd) {
		case clear:
		case click:
			this.param1 = getLocatorFromWebElement(element);
			break;
		case getText:
			this.param1 = getLocatorFromWebElement(element);
			this.returnValue = returnValue;
			break;
		case getWindowHandles:
			if (typeOfLog == Step.After) {
				this.returnValue = returnValue;
			}
			break;
		case navigateTo:
			this.param1 = sendValue;
			break;
		case type:
			this.param1 = getLocatorFromWebElement(element);
			this.param2 = sendValue;
			break;
		case close:
		case quit:
		default:
			// do nothing
		}
	}
	
	public int getStepNumber() {
		return stepNumber;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public long getTimeSinceLastStep() {
		return timeSinceLastStep;
	}

	public long getTimeElapsedStep() {
		return timeElapsedStep;
	}

	public Step getTypeOfLog() {
		return typeOfLog;
	}

	public Cmd getCmd() {
		return cmd;
	}

	public String getParam1() {
		return param1;
	}

	public String getParam2() {
		return param2;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public Throwable getIssue() {
		return issue;
	}

	private void timeStampsForBegin() {
		if (stepNumber > 1) {
			timeSinceLastStep = System.nanoTime() - timeMarker;
		}
		// set time marker for timeElapsedStep
		timeMarker = System.nanoTime();
	}

	private void timeStampsForEnd() {
		if (stepNumber > 1) {
			timeElapsedStep = System.nanoTime() - timeMarker;
		}
		// set time marker for timeSinceLastStep
		timeMarker = System.nanoTime();
	}

	private static String getLocatorFromWebElement(WebElement elem) {
		return (elem != null) ? getLocatorFromWebElement(elem.toString()) : null;
	}

	private static String getLocatorFromWebElement(String locator) {
		if (locator == null)
			return null;

		// sample string:
		// "[[RemoteWebDriver: firefox on WINDOWS (a66f78e9668e4aa3b066239459f969fe)] -> xpath: .//*[@id='Country__c_body']/table/tbody/tr[2]/th/a]"
		Pattern outerPattern = Pattern.compile("(\\[\\[.+\\] -> )(.+)\\]");
		Matcher outerMatcher = outerPattern.matcher(locator);
		if (!outerMatcher.matches()) {
			// return toString() as-is
			return locator;
		}

		// try to get the locator
		locator = locator.substring(outerMatcher.start(2), outerMatcher.end(2));
		// sample string:
		// "xpath: .//*[@id='Country__c_body']/table/tbody/tr[2]/th/a]"
		Pattern innerPattern = Pattern.compile("(\\S+): (.+)");
		Matcher innerMatcher = innerPattern.matcher(locator);
		boolean isLinkText = false;
		if (!innerMatcher.matches()) {
			innerPattern = Pattern.compile("(link text): (.+)");
			innerMatcher = innerPattern.matcher(locator);
			if (innerMatcher.matches()) {
				isLinkText = true;
			} else {
				// return what we got with the outer matcher
				return locator;
			}
		}

		// build the @FindBy string
		StringBuilder sb = new StringBuilder();
		sb.append("By.");
		// append locator type: "xpath"
		String locatorType = (isLinkText) ? "linkText" : locator.substring(innerMatcher.start(1), innerMatcher.end(1));
		sb.append(locatorType).append("(\"");
		// append locator itself: ".//*[@id='Country__c_body']/table/tbody/tr[2]/th/a]"
		sb.append(locator.substring(innerMatcher.start(2), innerMatcher.end(2))).append("\")");
		return sb.toString();
	}

	public static void main(String[] args) {
		String s = getLocatorFromWebElement("[[RemoteWebDriver: firefox on WINDOWS (a66f78e9668e4aa3b066239459f969fe)] -> link text: Amazon - Bangalore - Test Account]");
//		String s = getLocatorFromWebElement((WebElement) null);
//		String s = getLocatorFromWebElement((String) null);
		System.out.println(s);
	}
}
