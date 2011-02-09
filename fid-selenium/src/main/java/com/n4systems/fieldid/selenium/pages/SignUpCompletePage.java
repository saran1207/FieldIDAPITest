package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class SignUpCompletePage extends WebPage {
	
	public SignUpCompletePage(Selenium selenium) {
		super(selenium, false);
		waitForPageToLoad("30000");
	}
	
	public LoginPage clickSignInNow() {
		selenium.click("//DIV[@id='signIndButton']/A");
		return new LoginPage(selenium);
	}

}
