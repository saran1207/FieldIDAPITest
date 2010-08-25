package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class SelectPackagePage extends WebPage {
	
	public SelectPackagePage(Selenium selenium) {
		super(selenium);
	}
	
	public SignUpPage clickSignUpNowLink(String packageName) {
		String signUpNowLinkLocator = "//table[@id='packages']/tbody/tr[@class='signUp']/td/a[contains(@href,'" + packageName + "')]";
		selenium.click(signUpNowLinkLocator);
		return new SignUpPage(selenium);
	}
	
}
