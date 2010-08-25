package com.n4systems.fieldid.selenium.components;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.WebEntity;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.thoughtworks.selenium.Selenium;

public class OrgPicker extends WebEntity {
	
	private static final String selectOwnerOrganizationSelectListLocator = "//SELECT[@id='orgList']";
	private static final String orgBrowserLoadingImageLocator = "//DIV[@id='orgBrowserLoading' and not(contains(@style,'display: none'))]";
	private static final String selectOwnerCustomerSelectListLocator = "//SELECT[@id='customerList']";
	private static final String selectOwnerDivisionSelectListLocator = "//SELECT[@id='divisionList']";
	private static final String chooseOwnerLinkLocator = "//A[@class='searchOwner' and contains(text(),'Choose')]";
	private static final String chooseOwnerSelectButtonLocator = "xpath=//INPUT[@id='selectOrg']";

	public OrgPicker(Selenium selenium) {
		super(selenium);
	}
	
	private void waitForOrgPickerLoadingToFinish() {
		new ConditionWaiter(new Predicate() {
			@Override
			public boolean evaluate() {
				return !selenium.isElementPresent(orgBrowserLoadingImageLocator);
			}
		}).run("Org picker loading image never went away");
	}
	
	public void setOwner(Owner owner) {
		if (!owner.specifiesOrg()) {
			selenium.select(selectOwnerOrganizationSelectListLocator, "index=0");
			waitForOrgPickerLoadingToFinish();
		}
		
		String organization = owner.getOrganization();
		if(organization != null) {
			if(isOptionPresent(selectOwnerOrganizationSelectListLocator, organization)) {
				selenium.select(selectOwnerOrganizationSelectListLocator, organization);
				waitForOrgPickerLoadingToFinish();
			} else {
				fail("Could not find the organization '" + organization + "' in choose owner dialog");
			}
		}

		String customer = "";
		if(owner.getCustomer() != null) {
			customer = owner.getCustomer() + " (" + owner.getOrganization() + ")";
			if(isOptionPresent(selectOwnerCustomerSelectListLocator, customer)) {
				selenium.select(selectOwnerCustomerSelectListLocator, customer);
				waitForOrgPickerLoadingToFinish();
			} else {
				fail("Could not find the customer '" + customer + "' in choose owner dialog");
			}
		}

		String division = "";
		if(owner.getDivision() != null) {
			division = owner.getDivision() + ", " + customer;
			if(isOptionPresent(selectOwnerDivisionSelectListLocator, division)) {
				selenium.select(selectOwnerDivisionSelectListLocator, division);
				waitForOrgPickerLoadingToFinish();
			} else {
				fail("Could not find the division '" + division + "' in choose owner dialog");
			}
		}
	}

	public void clickChooseOwner() {
		selenium.click(chooseOwnerLinkLocator);
		waitForOrgPickerLoadingToFinish();
	}

	public void clickSelectOwner() {
		selenium.click(chooseOwnerSelectButtonLocator);
	}

}
