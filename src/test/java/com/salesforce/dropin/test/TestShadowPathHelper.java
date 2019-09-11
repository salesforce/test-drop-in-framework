package com.salesforce.dropin.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.salesforce.selenium.support.findby.ShadowPathHelper;

/**
 * Tests for Shadow Path Helper functions
 * 
 * @author ytao
 *
 */
public class TestShadowPathHelper {
	
	@Test
	public void testShadowPath() {
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
	
		Assert.assertEquals(ShadowPathHelper.getShadowQueryString(shadowPath), queryString);
	}
}
