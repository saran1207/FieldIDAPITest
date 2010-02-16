package com.n4systems.fieldid.selenium.asserts;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;


public class FieldIdAssert {
	public static void assertSystemLogoIsUsed(FieldIdSelenium selenium) {
		assertTrue("This page should display the system logo", selenium.isElementPresent("systemLogo"));
		assertFalse("This page should not display a branded company logo", selenium.isElementPresent("companyLogo"));
	}
}

