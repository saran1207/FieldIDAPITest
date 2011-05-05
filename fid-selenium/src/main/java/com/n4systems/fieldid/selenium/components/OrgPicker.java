package com.n4systems.fieldid.selenium.components;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.WebEntity;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.thoughtworks.selenium.Selenium;

public class OrgPicker extends WebEntity {
	
	private static final String selectOwnerOrganizationSelectListLocator = "//select[@id='orgList']";
	private static final String selectOwnerCustomerSelectListLocator = "//select[@id='customerList']";
	private static final String selectOwnerDivisionSelectListLocator = "//select[@id='divisionList']";
	
	public OrgPicker(Selenium selenium) {
		super(selenium);
	}
	
	private void waitForOrgPickerLoadingToFinish() {
		new ConditionWaiter(new Predicate() {
			@Override
			public boolean evaluate() {
				return !selenium.isElementPresent("//div[@id='orgBrowserLoading' and not(contains(@style,'display: none'))]");
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
		selenium.click("//a[@class='searchOwner' and contains(text(),'Choose')]");
		waitForOrgPickerLoadingToFinish();
	}

	public void clickSelectOwner() {
		selenium.click("xpath=//input[@id='selectOrg']");
	}

	public Owner getOwner() {
		String organization = selenium.getSelectedLabel(selectOwnerOrganizationSelectListLocator);
		String customer = null;
		String division = null;
		
		String[] s = selenium.getSelectOptions(selectOwnerCustomerSelectListLocator);
		if(s.length > 0 && !s.equals("")) {
			String customerOrganization = selenium.getSelectedLabel(selectOwnerCustomerSelectListLocator);
			customer = customerOrganization.replace(" (" + organization + ")", "").trim();
		}
		s = selenium.getSelectOptions(selectOwnerDivisionSelectListLocator);
		if(s.length > 0 && !s[0].equals("")) {
			String divisionCustomerOrganization = selenium.getSelectedLabel(selectOwnerDivisionSelectListLocator);
			division = divisionCustomerOrganization.replace(", " + customer, "").replace(" (" + organization + ")", "").trim();
		}
		return new Owner(organization.trim(), customer, division);
	}

	public void clickCancelOwner() {
		selenium.click("//a[@id='cancelOrgSelect']");
	}

}
