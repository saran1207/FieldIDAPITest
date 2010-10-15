package com.n4systems.fieldid.selenium.pages.safetynetwork;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class SafetyNetworkSettingsPage extends FieldIDPage {

	public SafetyNetworkSettingsPage(Selenium selenium) {
		super(selenium);
	}

	public void checkPublishAssetsBox() {
		selenium.check("//input[@id='chkAutoPublish']");
	}

	public void checkAutoAcceptBox() {
		selenium.check("//input[@id='chkAutoAcceptConnections']");
	}

    public void checkVisibilityBox() {
        selenium.check("//input[@id='chkSearchable']");
    }

	public void unCheckAutoPublishCheckBox() {
		selenium.uncheck("//input[@id='chkAutoPublish']");
	}

	public void unCheckAutoAcceptCheckBox() {
		selenium.uncheck("//input[@id='chkAutoAcceptConnections']");
	}

    public void unCheckVisibilityBox() {
        selenium.uncheck("//input[@id='chkSearchable']");
    }

	public void saveSettings() {
		selenium.click("//input[@id='privacySettingsSave_hbutton_save']");
		waitForPageToLoad();
	}

	public boolean isAutoPublishChecked() {
		return selenium.isChecked("//input[@id='chkAutoPublish']");
	}

	public boolean isAutoAcceptChecked() {
		return selenium.isChecked("//input[@id='chkAutoAcceptConnections']");
	}

    public boolean isVisibilityChecked() {
        return selenium.isChecked("//input[@id='chkSearchable']");   
    }
}
