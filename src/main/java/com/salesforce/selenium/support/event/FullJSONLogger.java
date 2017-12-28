package com.salesforce.selenium.support.event;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.salesforce.selenium.support.event.Step.Cmd;

/**
 * Use this class as base class, if you want to implement a
 * {@link WebDriverEventListener} and are only interested in some events. All
 * methods provided by this class have an empty method body.
 */
public class FullJSONLogger implements WebDriverEventListener {
	private String fileName = null;
	private List<Step> logEntries = new ArrayList<>();

	public FullJSONLogger(String testName) {
		this.fileName = "target/" + testName + ".json";
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeClose(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterClose(Step step) {
		logEntries.add(step);
	}

	@Override
	public void beforeFindElementByWebDriver(Step step, By by) {
		logEntries.add(step);
	}

	@Override
	public void afterFindElementByWebDriver(Step step, WebElement returnedElement, By by) {
		logEntries.add(step);
	}

	@Override
	public void beforeFindElementsByWebDriver(Step step, By by) {
		logEntries.add(step);
	}

	@Override
	public void afterFindElementsByWebDriver(Step step, List<WebElement> returnedElements, By by) {
		logEntries.add(step);
	}

	@Override
	public void beforeGet(Step step, String url) {
		logEntries.add(step);
	}

	@Override
	public void afterGet(Step step, String url) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetCurrentUrl(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterGetCurrentUrl(Step step, String url) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetTitle(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterGetTitle(Step step, String title) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetWindowHandle(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterGetWindowHandle(Step step, String handle) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetWindowHandles(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterGetWindowHandles(Step step, Set<String> handles) {
		logEntries.add(step);
	}

	@Override
	public void beforeQuit(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterQuit(Step step) {
		logEntries.add(step);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Navigation object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeBack(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterBack(Step step) {
		logEntries.add(step);
	}

	@Override
	public void beforeForward(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterForward(Step step) {
		logEntries.add(step);
	}

	@Override
	public void beforeRefresh(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterRefresh(Step step) {
		logEntries.add(step);
	}

	@Override
	public void beforeTo(Step step, String url) {
		logEntries.add(step);
	}

	@Override
	public void afterTo(Step step, String url) {
		logEntries.add(step);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.TargetLocator object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeActiveElement(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterActiveElement(Step step, WebElement activeElement) {
		logEntries.add(step);
	}

	@Override
	public void beforeAlert(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterAlert(Step step, Alert alert) {
		logEntries.add(step);
	}

	@Override
	public void beforeDefaultContent(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterDefaultContent(Step step) {
		logEntries.add(step);
	}

	@Override
	public void beforeFrameByIndex(Step step, int frameIndex) {
		logEntries.add(step);
	}

	@Override
	public void afterFrameByIndex(Step step, int frameIndex) {
		logEntries.add(step);
	}

	@Override
	public void beforeFrameByName(Step step, String frameName) {
		logEntries.add(step);
	}

	@Override
	public void afterFrameByName(Step step, String frameName) {
		logEntries.add(step);
	}

	@Override
	public void beforeFrameByElement(Step step, WebElement frameElement) {
		logEntries.add(step);
	}

	@Override
	public void afterFrameByElement(Step step, WebElement frameElement) {
		logEntries.add(step);
	}

	@Override
	public void beforeParentFrame(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterParentFrame(Step step) {
		logEntries.add(step);
	}

	@Override
	public void beforeWindow(Step step, String windowName) {
		logEntries.add(step);
	}

	@Override
	public void afterWindow(Step step, String windowName) {
		logEntries.add(step);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver.Window object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeFullscreen(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterFullscreen(Step step) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetPosition(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterGetPosition(Step step, Point targetPosition) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetSize(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterGetSize(Step step, Dimension targetSize) {
		logEntries.add(step);
	}

	@Override
	public void beforeMaximize(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterMaximize(Step step) {
		logEntries.add(step);
	}

	@Override
	public void beforeSetPosition(Step step, Point targetPosition) {
		logEntries.add(step);
	}

	@Override
	public void afterSetPosition(Step step, Point targetPosition) {
		logEntries.add(step);
	}

	@Override
	public void beforeSetSize(Step step, Dimension targetSize) {
		logEntries.add(step);
	}

	@Override
	public void afterSetSize(Step step, Dimension targetSize) {
		logEntries.add(step);
	}

	/*---------------------------------------------------------------------------
	 * Section for all commands called directly from WebElement object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeClick(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterClick(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeClear(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterClear(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeFindElementByElement(Step step, By by, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterFindElementByElement(Step step, WebElement returnedElement, By by, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeFindElementsByElement(Step step, By by, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterFindElementsByElement(Step step, List<WebElement> returnedElements, By by, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetAttribute(Step step, String name, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterGetAttribute(Step step, String value, String name, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetCssValue(Step step, String propertyName, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterGetCssValue(Step step, String propertyName, String value, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetTagName(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterGetTagName(Step step, String tagName, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetText(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterGetText(Step step, String text, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeIsDisplayed(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterIsDisplayed(Step step, boolean isDisplayed, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeIsEnabled(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterIsEnabled(Step step, boolean isEnabled, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeIsSelected(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterIsSelected(Step step, boolean isSelected, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeSendKeys(Step step, WebElement element, CharSequence... keysToSend) {
		logEntries.add(step);
	}

	@Override
	public void afterSendKeys(Step step, WebElement element, CharSequence... keysToSend) {
		logEntries.add(step);
	}

	@Override
	public void beforeSubmit(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterSubmit(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void onException(Step step, Cmd cmd, Throwable issue) {
		logEntries.add(step);
	}

	@Override
	public void closeListener() {
		if (logEntries == null || logEntries.size() == 0) {
			System.out.println("Warning: no performance log entries to write to " + fileName);
			return;
		}

		ObjectMapper objectMapper = new ObjectMapper();
    	//Set pretty printing of json
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

    	// convert List of Step objects to JSON
    	String logEntriesToJson = null;
		try {
			logEntriesToJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(logEntries);
		} catch (JsonProcessingException e1) {
			System.err.println("Error while converting WebDriver log entries to JSON");
			e1.printStackTrace();
			return;
		}

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);
			fileWriter.write(logEntriesToJson);
			System.out.println("Done writing WebDriver log entries to " + fileName);
		} catch (IOException e) {
			System.err.println("Error while writing WebDriver log entries to " + fileName);
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (IOException ex) {
				System.err.println("Error while trying to close file writer to " + fileName);
				ex.printStackTrace();
			}
		}
	}
}
