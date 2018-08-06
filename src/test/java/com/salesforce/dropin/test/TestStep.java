/* 
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.salesforce.selenium.support.event.Step;
import com.salesforce.selenium.support.event.Step.Cmd;
import com.salesforce.selenium.support.event.Step.Type;

/**
 * Tests using the Step object.
 * @author gneumann
 */
public class TestStep {

	/**
	 * Ensure that the Step object with bare minimum information gets properly created.
	 */
	@Test()
	public void testStepCreation() {
		Step newStepBeforeAction = new Step(Type.BeforeAction, 1, Cmd.clickByElement);
		Assert.assertTrue(newStepBeforeAction.getRecordNumber() == 1);
		Assert.assertTrue(newStepBeforeAction.getTimeStamp() > 1);

		// wait a little so that the time elapsed can be different
		try { Thread.sleep(10L); } catch (InterruptedException e) {	; /* ignore */ }

		Step newStepAfterAction = new Step(Type.AfterAction, 2, Cmd.clickByElement);
		Assert.assertTrue(newStepAfterAction.getRecordNumber() > newStepBeforeAction.getRecordNumber());
		Assert.assertTrue(newStepAfterAction.getTimeStamp() > newStepBeforeAction.getTimeStamp());
		Assert.assertTrue(newStepAfterAction.getTimeElapsedStep() > 1);
	}

	/**
	 * Ensure that the Step object with bare minimum information gets properly created.
	 */
	@Test()
	public void testTimeBetweenActions() {
		Step newStepAfterAction = new Step(Type.AfterAction, 1, Cmd.clickByElement);
		Assert.assertTrue(newStepAfterAction.getTimeSinceLastAction() == -1L);

		// wait a little so that the time between actions can be different
		try { Thread.sleep(10L); } catch (InterruptedException e) {	; /* ignore */ }

		Step newStepBeforeAction = new Step(Type.BeforeAction, 2, Cmd.close);
		Assert.assertTrue(newStepBeforeAction.getTimeSinceLastAction() > 1);
	}

	/**
	 * Ensure that the toString() representation of an By object is properly parsed and turned
	 * into a valid locator string.
	 */
	@Test()
	public void testGetLocatorFromBy() {
		String validXPathLocator = Step.getLocatorFromBy("By.xpath: .//*[@id='thePage:j_id39:searchblock:test:j_id45_lkwgt']/img");
		Assert.assertEquals(validXPathLocator, "By.xpath(\".//*[@id='thePage:j_id39:searchblock:test:j_id45_lkwgt']/img\")");
	}

	/**
	 * Ensure that a null By object returns a null locator string.
	 */
	@Test()
	public void testGetLocatorFromNullBy() {
		String nullBy = Step.getLocatorFromBy((By) null);
		Assert.assertEquals(nullBy, null);
		String nullString = Step.getLocatorFromBy((String) null);
		Assert.assertEquals(nullString, null);
	}

	/**
	 * Ensure that the toString() representation of a WebElement object is properly parsed and turned
	 * into a valid locator string.
	 */
	@Test()
	public void testGetLocatorFromWebElement() {
		String validLinkLocator = Step.getLocatorFromWebElement("[[RemoteWebDriver: firefox on WINDOWS (a66f78e9668e4aa3b066239459f969fe)] -> link text: Amazon - Bangalore - Test Account]");
		Assert.assertEquals(validLinkLocator, "By.linkText(\"Amazon - Bangalore - Test Account\")");
	}

	/**
	 * Ensure that a null WebElement object returns a null locator string.
	 */
	@Test()
	public void testGetLocatorFromNullWebElement() {
		String nullWebElement = Step.getLocatorFromWebElement((WebElement) null);
		Assert.assertEquals(nullWebElement, null);
		String nullString = Step.getLocatorFromWebElement((String) null);
		Assert.assertEquals(nullString, null);
	}
}
