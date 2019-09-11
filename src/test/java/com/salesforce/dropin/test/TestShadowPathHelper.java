/* 
 * Copyright (c) 2019, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license. 
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.dropin.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.salesforce.selenium.support.findby.ShadowPathHelper;

/**
 * Tests for Shadow Path Helper functions
 * 
 * @author ytao
 * @since 3.0.0
 */
public class TestShadowPathHelper {
	
	@Test
	public void testShadowPathLong() {
		String shadowPath = "one-record-home-flexipage2"
							+ "=> forcegenerated-flexipage_v2mom_record_page_v2mom__c"
							+ "=> flexipage-aura-wrapper[0]"
							+ "=> lightning-icon[1]"
							+ "=> lightning-primitive-icon";

		String queryString = "return document.querySelector('one-record-home-flexipage2')"
							+ ".shadowRoot.querySelector('forcegenerated-flexipage_v2mom_record_page_v2mom__c')"
							+ ".shadowRoot.querySelectorAll('flexipage-aura-wrapper')[0]"
							+ ".shadowRoot.querySelectorAll('lightning-icon')[1]"
							+ ".shadowRoot.querySelector('lightning-primitive-icon')";
	
		Assert.assertEquals(ShadowPathHelper.shadowPath2Script(shadowPath), queryString);
	}

	@Test
	public void testShadowPathShort() {
		String shadowPath = "one-record-home-flexipage2";
		String queryString = "return document.querySelector('one-record-home-flexipage2')";
	
		Assert.assertEquals(ShadowPathHelper.shadowPath2Script(shadowPath), queryString);
	}

	@Test
	public void testShadowPathEmpty() {
		String shadowPath = "";
		String queryString = "";
	
		Assert.assertEquals(ShadowPathHelper.shadowPath2Script(shadowPath), queryString);
	}
}
