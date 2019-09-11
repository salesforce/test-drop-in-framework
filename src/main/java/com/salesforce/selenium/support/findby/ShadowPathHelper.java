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
 * @since 2.1
 */
public class ShadowPathHelper {
	public static String getShadowQueryString(String shadowPath) {
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
