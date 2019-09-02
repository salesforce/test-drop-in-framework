package com.salesforce.selenium.support.findby;

import java.lang.reflect.Field;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class JSElementLocatorFactory implements ElementLocatorFactory {
	private final WebDriver driver;

	public JSElementLocatorFactory(WebDriver driver) {
		this.driver = driver;
	}

	@Override
	public ElementLocator createLocator(Field field) {
		return new JSElementLocator(driver, field);
	}
}
