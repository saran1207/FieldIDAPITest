package com.n4systems.fieldid.selenium.pages.admin;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class AdminOrgPage extends FieldIDPage {

    public AdminOrgPage(Selenium selenium) {
        super(selenium);
    }

	public void enterReadOnlyUser(boolean readOnlyUser) {
        changeExtendedFeature("ReadOnlyUser", readOnlyUser);
	}

	public void enterShowPlansAndPricing(boolean showPlansAndPricing) {
        changeFeature("plansAndPricingRow", showPlansAndPricing);
	}

    public void enterSecondaryOrgs(boolean secondaryOrgs) {
        changeFeature("secondaryOrgsRow", secondaryOrgs);
    }

    public void clickEditPlan() {
        selenium.click("//div[@id='orgLimits']//a[.='Edit']");
        waitForAjax();
    }
    
    public void enterTenantLimits(Integer employeeUsers, Integer liteUsers, Integer readonlyUsers) {
        clickEditPlan();
    	if(employeeUsers != null) {
    		selenium.type("//input[@id='planForm_userLimits_maxEmployeeUsers']", employeeUsers.toString());
    	}
    	if(liteUsers != null) {
    		selenium.type("//input[@id='planForm_userLimits_maxLiteUsers']", liteUsers.toString());
    	}
    	if(readonlyUsers != null) {
    		selenium.type("//input[@id='planForm_userLimits_maxReadOnlyUsers']", readonlyUsers.toString());
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
