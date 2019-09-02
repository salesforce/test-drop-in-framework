/**
 * 
 */
package com.salesforce.selenium.support.findby;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * @author gneumann
 *
 */
public class JSFieldDecorator extends DefaultFieldDecorator {

	/**
	 * @param factory
	 */
	public JSFieldDecorator(ElementLocatorFactory factory) {
		super(factory);
	}

	@Override
	protected boolean isDecoratableList(Field field) {
		if (!List.class.isAssignableFrom(field.getType())) {
			return false;
		}

		// Type erasure in Java isn't complete. Attempt to discover the generic
		// type of the list.
		Type genericType = field.getGenericType();
		if (!(genericType instanceof ParameterizedType)) {
			return false;
		}

		Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

		if (!WebElement.class.equals(listType)) {
			return false;
		}

		if (field.getAnnotation(FindByJS.class) == null) {
			return false;
		}

		return true;
	}
}
