package com.n4systems.fieldid.selenium.pages.search;

import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.fail;

public class SaveSearchPage extends FieldIDPage {

	public SaveSearchPage(Selenium selenium) {
		super(selenium);
		if(!checkOnSaveReportForm()){
			fail("Expected to be on save search page!");
		}
	}

	private boolean checkOnSaveReportForm() {
		return selenium.isElementPresent("//h2[.='Saved Search Details']");
	}
	
	public void setReportName(String name) {
		selenium.type("//input[@name='name']", name);
	}

	public AssetsSearchPage clickSave() {
		selenium.click("//input[@value='Save']");
		return new AssetsSearchPage(selenium);
	}

	public AssetsSearchPage clickCancel() {
		selenium.click("//a[contains(.,'Cancel')]");
		return new AssetsSearchPage(selenium);
	}
}
