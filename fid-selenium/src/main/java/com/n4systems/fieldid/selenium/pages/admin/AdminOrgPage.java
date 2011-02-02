package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class AdminOrgPage extends FieldIDPage {

    public AdminOrgPage(Selenium selenium) {
        super(selenium);
    }

	public void enterReadOnlyUser(boolean readOnlyUser) {
        if(readOnlyUser) {
            selenium.check("xpath=//INPUT[contains(@id,'ReadOnlyUser')]");
        } else {
            selenium.uncheck("xpath=//INPUT[contains(@id,'ReadOnlyUser')]");
        }
	}

	public void enterShowPlansAndPricing(boolean showPlansAndPricing) {
        if(showPlansAndPricing) {
            selenium.check("xpath=//INPUT[@id='organizationUpdate_primaryOrg_plansAndPricingAvailable']");
        } else {
            selenium.uncheck("xpath=//INPUT[@id='organizationUpdate_primaryOrg_plansAndPricingAvailable']");
        }
	}

    public AdminOrgsListPage submitOrganizationChanges() {
        selenium.click("//input[@value='Submit']");
        return new AdminOrgsListPage(selenium);
    }

}
