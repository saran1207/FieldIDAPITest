package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.model.tenant.TenantLimit;
import com.thoughtworks.selenium.Selenium;

public class AdminOrgPage extends FieldIDPage {

    public AdminOrgPage(Selenium selenium) {
        super(selenium);
    }

	public void enterReadOnlyUser(boolean readOnlyUser) {
        changeExtendedFeature("ReadOnlyUser", readOnlyUser);
	}

	public void enterShowPlansAndPricing(boolean showPlansAndPricing) {
        String rowId = "plansAndPricingRow";
        changeFeature(rowId, showPlansAndPricing);
	}

    public void clickEditPlan() {
        selenium.click("//div[@id='orgLimits']//a[.='Edit']");
        waitForAjax();
    }
    
    public void enterTenantLimits(TenantLimit limits) {
        clickEditPlan();
    	if(limits.getDiskSpaceInBytes() != null) {
    		selenium.type("//input[@id='planForm_diskSpace']", limits.getDiskSpaceInBytes().toString());
    	}
    	if(limits.getAssets() != null) {
    		selenium.type("//input[@id='planForm_assets']", limits.getAssets().toString());
    	}
    	if(limits.getUsers() != null) {
    		selenium.type("//input[@id='planForm_users']", limits.getUsers().toString());
    	}
    	if(limits.getLiteUsers() != null) {
    		selenium.type("//input[@id='planForm_liteUsers']", limits.getLiteUsers().toString());
    	}
    	if(limits.getSecondaryOrgs() != null) {
    		selenium.type("//input[@id='planForm_secondaryOrgs']", limits.getSecondaryOrgs().toString());
    	}
        clickSavePlan();
    }

    public void clickSavePlan() {
        selenium.click("//div[@id='orgLimits']//input[@type='button' and @value='Save']");
        waitForAjax("30000");
    }
    
    public AdminOrgsListPage submitOrganizationChanges() {
        selenium.click("//input[@value='Submit']");
        return new AdminOrgsListPage(selenium);
    }

    private void changeExtendedFeature(String extendedFeatureId, boolean enable) {
        String rowId = "extendedFeatureRow_"+extendedFeatureId;
        changeFeature(rowId, enable);
    }

    private void changeFeature(String rowId, boolean enable) {
        String currentLinkText = selenium.getText("//tr[@id='"+rowId+"']//a").trim();
        boolean currentlyOn = currentLinkText.equals("on");
        if (enable && !currentlyOn) {
            selenium.click("//tr[@id='"+rowId+"']//a[contains(text(), 'off')]");
        } else if (!enable && currentlyOn) {
            selenium.click("//tr[@id='"+rowId+"']//a[contains(text(), 'on')]");
        }
        waitForAjax();
    }

}
