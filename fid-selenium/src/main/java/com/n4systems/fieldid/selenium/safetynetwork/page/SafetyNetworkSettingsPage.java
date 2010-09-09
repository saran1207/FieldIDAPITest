package com.n4systems.fieldid.selenium.safetynetwork.page;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class SafetyNetworkSettingsPage extends FieldIDPage {

	public SafetyNetworkSettingsPage(Selenium selenium) {
		super(selenium);
	}

	public void checkPublishAssetsBox() {
		if (!selenium.isElementPresent("//input[@id='chkAutoPublish']")) {
			fail("Could not find the Auto Publish Checkbox in the Settings page");
		}
		selenium.check("//input[@id='chkAutoPublish']");
	}

	public void checkAutoAcceptBox() {
		if (!selenium.isElementPresent("//input[@id='chkAutoAcceptConnections']")) {
			fail("Could not find the Auto Accept Checkbox in the Settings page");
		}
		selenium.check("//input[@id='chkAutoAcceptConnections']");
	}

	public void unCheckAutoPublishCheckBox() {
		selenium.uncheck("//input[@id='chkAutoPublish']");
	}

	public void unCheckAutoAcceptCheckBox() {
		selenium.uncheck("//input[@id='chkAutoAcceptConnections']");
	}

	public void submitForm() {
		selenium.click("//input[@id='privacySettingsSave_hbutton_save']");
		waitForPageToLoad();
	}

	public boolean isAutoPublishChecked() {
		return selenium.isChecked("//input[@id='chkAutoPublish']");
	}

	public boolean isAutoAcceptChecked() {
		return selenium.isChecked("//input[@id='chkAutoAcceptConnections']");
	}
}
