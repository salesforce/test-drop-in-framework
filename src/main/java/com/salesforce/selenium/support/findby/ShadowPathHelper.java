/* 
 * Copyright (c) 2019, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.selenium.support.findby;

/**
 * Provide helper function to manage Shadow Path information
 * in {@link FindByJS}.
 * 
 * @author ytao
 * @since 3.0.0
 */
public class ShadowPathHelper {
	/**
	 * Convert a shadowPath to proper JavaScript code.
	 * <p>
	 * A valid shadow path has the format:<p>{@code tag-name ["=>" tag-name]*}<p>
	 * Please note: this method does not perform any format validation check.
	 * <p>
	 * Example:<p>
	 * {@code flexipage-aura-wrapper[0] => lightning-icon[1] => lightning-primitive-icon}
	 * <p>
	 * This gets converted into this JavaScript code:<p>
	 * {@code return document.querySelectorAll('flexipage-aura-wrapper')[0].shadowRoot.querySelectorAll('lightning-icon')[1].shadowRoot.querySelector('lightning-primitive-icon');}
	 * <p>
	 * which can then be send to {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...)}.
	 * 
	 * @param shadowPath a valid shadow path
	 * @return JavaScript code or empty string
	 */
	public static String shadowPath2Script(String shadowPath) {
		if (shadowPath == null || shadowPath.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder("return document");
		for(String shadow : shadowPath.split("=>")) {
			shadow = shadow.trim();
			
			if(shadow.endsWith("]")) {
				sb.append(".querySelectorAll('")
					.append(shadow.substring(0,shadow.indexOf('[')))
					.append("')")
					.append(shadow.substring(shadow.indexOf('['),shadow.length()));
			}
			else {
				sb.append(".querySelector('").append(shadow).append("')");
			}
			sb.append(".shadowRoot");
		}
		sb.setLength(sb.lastIndexOf(".shadowRoot"));
		return sb.toString();
	}
}
