package com.salesforce.selenium.support.findby;

/***
 * 
 * Provide helper function to manage Shadow Path
 * 
 * @author ytao
 * 
 */
public class ShadowPathHelper {
	public static String getShadowQueryString(String shadowPath) {
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
