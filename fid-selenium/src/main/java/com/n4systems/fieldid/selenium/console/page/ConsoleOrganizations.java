package com.n4systems.fieldid.selenium.console.page;

import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import com.n4systems.fieldid.selenium.datatypes.ConsoleTenant;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ConsoleOrganizations {
	FieldIdSelenium selenium;
	MiscDriver misc;
	
	// Locators
	private String organizationsTableXpath = "//DIV[@id='content']/TABLE";
	private String numberOfTenantsXpath = organizationsTableXpath + "/TBODY/TR";
	private String totalTenantsTextLocator = "xpath=//DIV[@id='content']";
	private String tenantIDColumn = "2";
	private String editTenantColumn = "3";
	private String partnerCenterCheckBoxLocator = "xpath=//INPUT[contains(@id,'PartnerCenter')]";
	private String submitButtonLocator = "xpath=//INPUT[@id='organizationUpdate_0']";
	private String cancelButtonLocator = "xpath=//INPUT[@id='organizationUpdate_redirectAction:organizations']";
	private String showPlansAndPricingCheckBoxLocator = "xpath=//INPUT[@id='organizationUpdate_primaryOrg_plansAndPricingAvailable']";

	public ConsoleOrganizations(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Assumes you are on the Organizations tab. Gets the list of tenant IDs.
	 * 
	 * @return
	 */
	public List<String> getListOfTenantIDs() {
		List<String> tenantIds = new ArrayList<String>();
		Number n = selenium.getXpathCount(numberOfTenantsXpath);
		int numTenants = n.intValue();
		String tenantIDCellLocator = "xpath=" + organizationsTableXpath + ".0.1";
		for(int i = 0; i < numTenants; i++) {
			String tenant = selenium.getTable(tenantIDCellLocator);
			tenantIds.add(tenant);
			tenantIDCellLocator = tenantIDCellLocator.replaceFirst("\\." + i, "." + (i+1));
		}
		return tenantIds;
	}
	
	public void gotoEditTenant(String tenantID) {
		String editLinkLocator = "xpath=" + organizationsTableXpath + 
			"/TBODY/TR/TD[position()=" + tenantIDColumn + " and contains(text(),'" + tenantID +
			"')]/../TD[" + editTenantColumn + "]/A[contains(text(),'Edit')]";
		if(selenium.isElementPresent(editLinkLocator)) {
			selenium.click(editLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Edit '" + tenantID + "'");
		}
	}
	
	public void gotoSubmitTenantInformation() {
		if(selenium.isElementPresent(submitButtonLocator)) {
			selenium.click(submitButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Submit button");
		}
	}
	
	public void gotoCancelTenantInformation() {
		if(selenium.isElementPresent(cancelButtonLocator)) {
			selenium.click(cancelButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Cancel button");
		}
	}
	
	public void setPartnerCenter(boolean partnerCenter) {
		if(selenium.isElementPresent(partnerCenterCheckBoxLocator)) {
			if(partnerCenter) {
				selenium.check(partnerCenterCheckBoxLocator);
			} else {
				selenium.uncheck(partnerCenterCheckBoxLocator);
			}
		} else {
			fail("Could not find the check box for PartnerCenter");
		}
	}

	public void setShowPlansAndPricing(boolean showPlansAndPricing) {
		if(selenium.isElementPresent(showPlansAndPricingCheckBoxLocator)) {
			if(showPlansAndPricing) {
				selenium.check(showPlansAndPricingCheckBoxLocator);
			} else {
				selenium.uncheck(showPlansAndPricingCheckBoxLocator);
			}
		} else {
			fail("Could not find the check box for Set Show Plans and pricing when Partner Center enabled");
		}
	}
}

