package com.n4systems.fieldid.selenium.pages.setup;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class SystemSettingsPage extends FieldIDPage {
	
	public SystemSettingsPage(Selenium selenium) {
		super(selenium);
	}

    public boolean getEnableAssetAssignment() {
        return selenium.isChecked("//input[@id='systemSettingsUpdate_assignedTo']");
    }

    public String getPreferredDateFormat() {
        return selenium.getSelectedValue("//select[@name='dateFormat']");
    }

}
