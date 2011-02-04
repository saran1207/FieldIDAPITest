package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.testcase.EmailNotificationPage;
import com.thoughtworks.selenium.Selenium;

public class EmailNotificationsListPage extends FieldIDPage {

	public EmailNotificationsListPage(Selenium selenium) {
		super(selenium);
		if (!checkOnEmailNotificationsListPage()) {
			fail("Expected to be on email notifications list page!");
		}
	}

	public EmailNotificationPage clickCreateFirstNotification(){
		selenium.click("//div/input");
		return new EmailNotificationPage(selenium);
		
	}
	
	
	private boolean checkOnEmailNotificationsListPage() {
		return selenium.isElementPresent("//div[@class='initialMessage']");
	}
}
