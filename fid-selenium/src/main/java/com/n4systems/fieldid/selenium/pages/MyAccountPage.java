package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class MyAccountPage extends FieldIDPage {

	public MyAccountPage(Selenium selenium) {
		super(selenium);
		if(!checkOnMyAccountPage()){
			fail("Expected to be on My Account page!");
		}

	}

	public boolean checkOnMyAccountPage() {
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'My Account')]");
	}
	
	public void deleteReport(String name) {
		selenium.click("//tr/td/a[text()='" + name + "']/../..//a[text()='Delete']");
		waitForPageToLoad();
	}
	

}
