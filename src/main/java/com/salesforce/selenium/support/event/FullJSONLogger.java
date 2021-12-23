/* 
 * Copyright (c) 2018, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.selenium.support.event;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Coordinates;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.salesforce.selenium.support.event.Step.Cmd;

/**
 * Collects information on a given WebDriver command such as click() or getText() and saves this
 * collection to a JSON file.
 * 
 * The {@link EventFiringWebDriver} creates {@link Step} objects before and after each command such as
 * click() or getText(). This class collects these objects and saves this collection to a JSON file.
 * 
 * @author gneumann
 * @since 2.0.0
 */
public class FullJSONLogger extends AbstractWebDriverEventListener {
	private static final int BATCHSIZE = 1000;
	private String fileName = null;
	private List<Step> logEntries = new ArrayList<>();

	/**
	 * Define the file name of the JSON file without the ".json" extension. It will be
	 * written when the running test calls the {@link EventFiringWebDriver#quit()} command.
	 * <p>
	 * The file will be stored under the relative directory
	 * {@link WebDriverEventListener#TESTDROPIN_LOGFILES_DIR}.
	 * 
	 * @param testName name of the JSON file
	 */
	public FullJSONLogger(String testName) {
		this.fileName = TESTDROPIN_LOGFILES_DIR + convertTestname2FileName(testName) + ".json";
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

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to JavascriptExecutor.
	 *--------------------------------------------------------------------*/

	@Override
	public void beforeExecuteAsyncScript(Step step, String script, Object... args) {
		logEntries.add(step);
	}

	@Override
	public void afterExecuteAsyncScript(Step step, String script, Object... args) {
		logEntries.add(step);
	}

	@Override
	public void beforeExecuteScript(Step step, String script, Object... args) {
		logEntries.add(step);
	}

	@Override
	public void afterExecuteScript(Step step, String script, Object... args) {
		logEntries.add(step);
	}

	/*--------------------------------------------------------------------
	 * Section for all commands called directly from WebDriver object
	 * after casting to TakesScreenshot.
	 *--------------------------------------------------------------------*/

	@Override
	public <X> void beforeGetScreenshotAs(Step step, OutputType<X> target) {
		logEntries.add(step);
	}

	@Override
	public <X> void afterGetScreenshotAs(Step step, OutputType<X> target) {
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
	 * Section for all commands called directly from WebDriver.Timeouts object.
	 *---------------------------------------------------------------------------*/

	@Override
	public void beforeImplicitlyWait(Step step, long time, TimeUnit unit) {
		logEntries.add(step);
	}

	@Override
	public void afterImplicitlyWait(Step step, long time, TimeUnit unit) {
		logEntries.add(step);
	}

	@Override
	public void beforePageLoadTimeout(Step step, long time, TimeUnit unit) {
		logEntries.add(step);
	}

	@Override
	public void afterPageLoadTimeout(Step step, long time, TimeUnit unit) {
		logEntries.add(step);
	}

	@Override
	public void beforeSetScriptTimeout(Step step, long time, TimeUnit unit) {
		logEntries.add(step);
	}

	@Override
	public void afterSetScriptTimeout(Step step, long time, TimeUnit unit) {
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
	public void beforeGetSizeByWindow(Step step) {
		logEntries.add(step);
	}

	@Override
	public void afterGetSizeByWindow(Step step, Dimension targetSize) {
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
	public void beforeGetLocation(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterGetLocation(Step step, Point point, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetSizeByElement(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterGetSizeByElement(Step step, Dimension dimension, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeGetRect(Step step, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void afterGetRect(Step step, Rectangle rectangle, WebElement element) {
		logEntries.add(step);
	}

	@Override
	public void beforeSendKeysByElement(Step step, WebElement element, CharSequence... keysToSend) {
		logEntries.add(step);
	}

	@Override
	public void afterSendKeysByElement(Step step, WebElement element, CharSequence... keysToSend) {
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
	public void beforeSendKeysByKeyboard(Step step, CharSequence... keysToSend) {
		logEntries.add(step);
	}

	@Override
	public void afterSendKeysByKeyboard(Step step, CharSequence... keysToSend) {
		logEntries.add(step);
	}

	@Override
	public void beforePressKey(Step step, CharSequence... keyToPress) {
		logEntries.add(step);
	}

	@Override
	public void afterPressKey(Step step, CharSequence... keyToPress) {
		logEntries.add(step);
	}

	@Override
	public void beforeReleaseKey(Step step, CharSequence... keyToPress) {
		logEntries.add(step);
	}

	@Override
	public void afterReleaseKey(Step step, CharSequence... keyToPress) {
		logEntries.add(step);
	}

	@Override
	public void beforeClickByMouse(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void afterClickByMouse(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void beforeDoubleClick(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void afterDoubleClick(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void beforeMouseDown(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void afterMouseDown(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void beforeMouseUp(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void afterMouseUp(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void beforeMouseMove(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void afterMouseMove(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void beforeMouseMove(Step step, Coordinates where, long xOffset, long yOffset) {
		logEntries.add(step);
	}

	@Override
	public void afterMouseMove(Step step, Coordinates where, long xOffset, long yOffset) {
		logEntries.add(step);
	}

	@Override
	public void beforeContextClick(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void afterContextClick(Step step, Coordinates where) {
		logEntries.add(step);
	}

	@Override
	public void onException(Step step, Cmd cmd, Throwable issue) {
		logEntries.add(step);
	}

	@Override
	public void closeListener() {
		// make sure the directory hosting the logfile exists
		new File(TESTDROPIN_LOGFILES_DIR).mkdirs();

		if (logEntries == null || logEntries.size() == 0) {
			System.out.println("Warning: no performance log entries to write to " + fileName);
			return;
		}

		ObjectMapper objectMapper = new ObjectMapper();
    	//Set pretty printing of json
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		SequenceWriter seqWriter = null;
		try {
			seqWriter = objectMapper.writerWithDefaultPrettyPrinter().writeValuesAsArray(new FileWriter(fileName));
			int numOfLogEntries = logEntries.size();
			int numOfBatches = numOfLogEntries / BATCHSIZE;
			int lowerIndex = 0;
			int upperIndex = BATCHSIZE;
			for (int batchNo = 0; batchNo < numOfBatches; batchNo++) {
				List<Step> logEntriesBatch = logEntries.subList(lowerIndex, upperIndex);
				seqWriter.writeAll(logEntriesBatch);
				lowerIndex = lowerIndex + BATCHSIZE;
				upperIndex = upperIndex + BATCHSIZE;
			}
			if (lowerIndex < numOfLogEntries) {
				List<Step> logEntriesBatch = logEntries.subList(lowerIndex, numOfLogEntries);
				seqWriter.writeAll(logEntriesBatch);
			}
			System.out.println("Done writing WebDriver log entries to " + fileName);
		} catch (IOException e) {
			System.err.println("Error while writing WebDriver log entries to " + fileName);
			e.printStackTrace();
		} finally {
			try {
				if (seqWriter != null)
					seqWriter.close();
			} catch (IOException ex) {
				System.err.println("Error while trying to close file writer to " + fileName);
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Convenience method for reading one of the *.json files this logger class has previously
	 * written to disk. It is then possible to process the information and convert into other
	 * formats of your choice.
	 * 
	 * @param fileName relative or absolute path with file name
	 * @return list of {@link Step} objects or null in case of de-serialization problems
	 */
	public static List<Step> readStepsFromFile(String fileName) {
		List<Step> steps = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			steps = objectMapper.readValue(new File(fileName), new TypeReference<List<Step>>() {});
		} catch (IOException e) {
			System.err.println("Error while reading WebDriver log entries from " + fileName);
			e.printStackTrace();
		}
		return steps;
	}
}
