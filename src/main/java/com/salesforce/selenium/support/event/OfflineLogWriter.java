/**
 * 
 */
package com.salesforce.selenium.support.event;

import java.util.List;

import org.openqa.selenium.By;

import com.salesforce.selenium.support.event.Step.Type;

/**
 * @author gneumann
 *
 */
public class OfflineLogWriter {
	public static void processSteps(List<Step> steps, WebDriverEventListener logger) {
		if (steps == null || steps.size() == 0)
			// nothing to be done
			return;
		if (logger == null)
			throw new IllegalArgumentException("logger for producing output must not be null");
		
		for (Step step : steps) {
			switch (step.getCmd()) {
			case close:
				if (step.getTypeOfLog() == Type.BeforeAction)
					logger.beforeClose(step);
				else if (step.getTypeOfLog() == Type.AfterAction)
					logger.afterClose(step);
				else
					System.err.println("Step has unexpected type of command: " + step);
				break;
			case findElementByWebDriver:
				By by = Step.getByFromString(step.getParam1());
				if (by != null) {
					if (step.getTypeOfLog() == Type.BeforeGather)
						logger.beforeFindElementByWebDriver(step, by);
					else if (step.getTypeOfLog() == Type.AfterGather)
						logger.afterFindElementByWebDriver(step, null, by);
					else
						System.err.println("Step has unexpected type of command: " + step);
				}
				break;
			default:
			}
		}
		logger.closeListener();
	}
	
	public static void main(String[] args) {
		List<Step> steps = FullJSONLogger.readStepsFromFile("/Users/gneumann/Downloads/Fonterra__Resume_Complaint_Case_Through_UI_Wizard_-_Investigate_-_Case_Initiator.json");
 		FullJSONLogger logger = new FullJSONLogger("offline");
		processSteps(steps, logger);
	}
}
