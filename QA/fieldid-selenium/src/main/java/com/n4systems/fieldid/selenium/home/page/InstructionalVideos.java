package com.n4systems.fieldid.selenium.home.page;

import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class InstructionalVideos {
	private FieldIdSelenium selenium;
	private MiscDriver misc;
	
	// Locators
	private String instructionalVideosPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Instructional Videos')]";

	public InstructionalVideos(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void assertInstructionalVideosPageHeader() {
		assertTrue("Could not find the header for the Instructional Videos page", selenium.isElementPresent(instructionalVideosPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
}
