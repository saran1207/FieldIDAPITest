package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class EULAPage extends WebPage {

    public EULAPage(Selenium selenium) {
        this(selenium, true);
	}

	public EULAPage(Selenium selenium, boolean waitForPageToLoad) {
		super(selenium, waitForPageToLoad);
		if (!selenium.isElementPresent("//h1[contains(text(),'Field ID End User Licence Agreement')]")) {
			fail("Expected to be on Eula Page!");
		}
	}
	
	public void scrollToBottomOfEULA() {
		selenium.runScript("toggleEula();");
	}

	public HomePage clickAcceptEULA() {
		selenium.click("xpath=//INPUT[@id='acceptEula']");
		return new HomePage(selenium);
	}
	
	public AccountSetupWizardPage clickAcceptEULAToWizard() {
		selenium.click("xpath=//INPUT[@id='acceptEula']");
		return new AccountSetupWizardPage(selenium);
	}

}
