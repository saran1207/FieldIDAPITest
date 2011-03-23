package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class RegistrationRequestPage extends FieldIDPage {

	public RegistrationRequestPage(Selenium selenium) {
		super(selenium);
	}

	public LoginPage registerUser(String userId){
		selenium.open("/fieldid/registerUser.action");
		selenium.type("//input[@id='companyName']", "company_name");
		selenium.type("registerUserCreate_firstName", "Our test user");
		selenium.type("registerUserCreate_firstName", "Test");
		selenium.type("registerUserCreate_lastName", "User");
		selenium.type("registerUserCreate_emailAddress", "dev@fieldid.com");
		selenium.type("registerUserCreate_city", "Toronto");
		selenium.select("countryId", "value=CA");
		//selenium.waitForAjax(WebEntity.DEFAULT_TIMEOUT);
		selenium.select("tzlist", "label=Ontario - Toronto");
		selenium.type("registerUserCreate_position", "some position");
		selenium.type("registerUserCreate_phoneNumber", "647-202-2789");
		selenium.type("registerUserCreate_comment", "This is a comment");
		
		selenium.type("registerUserCreate_userId", userId);
		selenium.type("registerUserCreate_password", "makemore$");
		selenium.type("registerUserCreate_passwordConfirmation", "makemore$");
		selenium.click("registerUserCreate_save");
		//selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
		selenium.click("//input");
		
		return new LoginPage(selenium);
	}
	
	public RegistrationRequestPage openRequestFromUser(String userId) {
		selenium.click("css=#viewRequest_" + userId);
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
		return new RegistrationRequestPage(selenium);
	}
}
