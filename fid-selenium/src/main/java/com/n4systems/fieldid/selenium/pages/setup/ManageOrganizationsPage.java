package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.thoughtworks.selenium.Selenium;

public class ManageOrganizationsPage extends FieldIDPage {
	
	public ManageOrganizationsPage(Selenium selenium) {
		super(selenium);
		if(!selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Manage Organizations')]")) {
			fail("Could not find the header for 'Manage Organizations'.");
		}
	}

	public void clickAdd() {
		clickNavOption("Add");
		waitForTenantSettingsToBeCreated();
	}
	
	public void waitForTenantSettingsToBeCreated() {
		new ConditionWaiter(new Predicate(){
			@Override
			public boolean evaluate() {
				selenium.refresh();
				return !selenium.isElementPresent("//DIV[contains(@class,'limitWarning')]");
			}
		}).run("Secondary Org limits never updated", 5*60, 10*1000);
	}

	public void fillOrganizationForm(Organization o) {
		if(o.getName() != null) {
			selenium.type("//input[@id='organizationCreate_displayName']", o.getName());
		}
		if(o.getNameOnCert() != null) {
			selenium.type("//input[@id='organizationCreate_certificateName']", o.getNameOnCert());
		}
		if(o.getCountry() != null) {
			selenium.select("//SELECT[@id='organizationCreate_countryId']", o.getCountry());
			waitForAjax();
		}
		if(o.getTimeZone() != null) {
			selenium.select("//SELECT[@id='tzlist']", o.getTimeZone());
		}
		if(o.getCompanyStreetAddress() != null) {
			selenium.type("//input[@id='organizationCreate_addressInfo_streetAddress']", o.getCompanyStreetAddress());
		}
		if(o.getCompanyCity() != null) {
			selenium.type("//input[@id='organizationCreate_addressInfo_city']", o.getCompanyCity());
		}
		if(o.getCompanyState() != null) {
			selenium.type("//input[@id='organizationCreate_addressInfo_state']", o.getCompanyState());
		}
		if(o.getCompanyCountry() != null) {
			selenium.type("//input[@id='organizationCreate_addressInfo_country']", o.getCompanyCountry());
		}
		if(o.getCompanyZipCode() != null) {
			selenium.type("//input[@id='organizationCreate_addressInfo_zip']", o.getCompanyZipCode());
		}
		if(o.getCompanyPhoneNumber() != null) {
			selenium.type("//input[@id='organizationCreate_addressInfo_phone1']", o.getCompanyPhoneNumber());
		}
		if(o.getCompanyFaxNumber() != null) {
			selenium.type("//input[@id='organizationCreate_addressInfo_fax1']", o.getCompanyFaxNumber());
		}
	}

	public void clickSave() {
		selenium.click("//input[@id='organizationCreate_label_save']");
		waitForPageToLoad();
	}

	public List<String> getOrganizationNames() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount("//tr[contains(@id,'organization_')]");
		int numRows = n.intValue();
		for(int i = 0; i < numRows; i++) {
			int row = i + 1;	// first row is the column headers so start at + 1
			String cellLocator = "//div[@id='pageContent']/table[@class='list']." + row + ".0";
			String orgName = selenium.getTable(cellLocator);
			result.add(orgName.trim());
		}
		return result;
	}

}
