/**
 * 
 */
package com.salesforce.selenium.support.findby;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * @author gneumann
 *
 */
public class JSPageFactory extends PageFactory {

	/**
	 * As
	 * {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.WebDriver, Class)}
	 * but will only replace the fields of an already instantiated Page Object.
	 *
	 * @param driver The driver that will be used to look up the elements
	 * @param page   The object with WebElement and List&lt;WebElement&gt; fields
	 *               that should be proxied.
	 */
	public static void initElements(WebDriver driver, Object page) {
	    final WebDriver driverRef = driver;
	    initElements(new DefaultElementLocatorFactory(driverRef), page);
	    initElementsJS(new JSElementLocatorFactory(driverRef), page);
	}

	/**
	 * Similar to the other "initElements" methods, but takes an
	 * {@link ElementLocatorFactory} which is used for providing the mechanism for
	 * finding elements. If the ElementLocatorFactory returns null then the field
	 * won't be decorated.
	 *
	 * @param factory The factory to use
	 * @param page    The object to decorate the fields of
	 */
	public static void initElementsJS(ElementLocatorFactory factory, Object page) {
		final ElementLocatorFactory factoryRef = factory;
		initElements(new JSFieldDecorator(factoryRef), page);
	}
}
