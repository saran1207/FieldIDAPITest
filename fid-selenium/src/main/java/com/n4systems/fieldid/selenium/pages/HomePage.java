package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class HomePage extends FieldIDPage {
	
	private static final String HOME_PAGE_HEADER_XPATH = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Dashboard')]";

	public HomePage(Selenium selenium) {
		this(selenium, true);
	}
	
	public HomePage(Selenium selenium, boolean waitForLoad) {
		super(selenium, waitForLoad);
		if (!checkOnHomePage()) {
			fail("Expected to be on home page!");
		}
	}
	
	private boolean checkOnHomePage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent(HOME_PAGE_HEADER_XPATH);
	}

    public void clickFirstLearningCenterLink() {
        selenium.click("//div[@id='dashboardShortCuts']/div[@id='helpVideos']/ul/li[1]/a");
        waitForPageToLoad();
    }

    public LoginPage clickHomeExpectingSessionBoot() {
        selenium.click("//div[@id='pageNavigation']//a[@id='menuHome']");
        return new LoginPage(selenium);
    }
	
}
