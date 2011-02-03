package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.model.tenant.TenantLimit;
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
    
    public void enterTenantLimits(TenantLimit limits) {
    	if(limits.getDiskSpaceInBytes() != null) {
    		selenium.type("//input[@id='organizationUpdate_diskSpace']", limits.getDiskSpaceInBytes().toString());
    	}
    	if(limits.getAssets() != null) {
    		selenium.type("//input[@id='organizationUpdate_assets']", limits.getAssets().toString());
    	}
    	if(limits.getUsers() != null) {
    		selenium.type("//input[@id='organizationUpdate_users']", limits.getUsers().toString());
    	}
    	if(limits.getLiteUsers() != null) {
    		selenium.type("//input[@id='organizationUpdate_liteUsers']", limits.getLiteUsers().toString());
    	}
    	if(limits.getSecondaryOrgs() != null) {
    		selenium.type("//input[@id='organizationUpdate_secondaryOrgs']", limits.getSecondaryOrgs().toString());
    	}    		
    }
    
    public AdminOrgsListPage submitOrganizationChanges() {
        selenium.click("//input[@value='Submit']");
        return new AdminOrgsListPage(selenium);
    }

}
