/**
 * 
 */
package com.salesforce.dropin.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.salesforce.selenium.support.event.EventFiringWebDriver;

/**
 * Basic verification of config file handling.
 * @author gneumann
 */
public class TestConfigFile {
	@Test()
	public void verifyPasswordMasking() {
		Assert.assertEquals(EventFiringWebDriver.getProperty("value1", "blah"), "foo",
				"Wrong value retrievd from config file");
		Assert.assertEquals(EventFiringWebDriver.getProperty("value2", "blah"), "Foo",
				"Wrong value retrievd from config file");
		Assert.assertEquals(EventFiringWebDriver.getProperty("nonexisting.key", "blah"), "blah",
				"Wrong value retrievd from config file");
		Assert.assertTrue("By.id(\"password\"".contains(EventFiringWebDriver.getProperty("value3", "blah")));
		Assert.assertTrue("By.id(\"password\"".contains(EventFiringWebDriver.getProperty("value4", "blah")));
	}

	@BeforeClass
	public void createPropertiesFile() {
		Properties tempProps = new Properties();
		try (OutputStream output = new FileOutputStream(EventFiringWebDriver.PROPERTIES_FILENAME)) {

			// set the properties value
			tempProps.setProperty("value1", "foo");
			tempProps.setProperty("value2", "Foo");
			tempProps.setProperty("value3", "By.id(\"password");
			tempProps.setProperty("value4", "By.id(\"password\"");

			// save properties to project root folder
			tempProps.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
			Assert.fail("Cannot create properties file " + EventFiringWebDriver.PROPERTIES_FILENAME);
		}
	}

	@AfterClass
	public void deletePropertiesFile() {
		new File(EventFiringWebDriver.PROPERTIES_FILENAME).delete();
	}
}
