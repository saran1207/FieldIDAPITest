package com.n4systems.fieldid.selenium.asserts;

import com.thoughtworks.selenium.Selenium;
import static org.junit.Assert.*;


public class FieldIdAssert {
	public static void assertSystemLogoIsUsed(Selenium selenium) {
		assertTrue("This page should display the system logo", selenium.isElementPresent("systemLogo"));
		assertFalse("This page should not display a branded company logo", selenium.isElementPresent("companyLogo"));
	}
}

