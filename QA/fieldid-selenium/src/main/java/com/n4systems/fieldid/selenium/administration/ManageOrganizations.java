package com.n4systems.fieldid.selenium.administration;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.PrimaryOrganization;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageOrganizations extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageOrganizationsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Organizations')]";
	private String primaryOrganizationNameLocator = "xpath=//LABEL[@for='name']/../SPAN[@class='fieldHolder']";
	private String secondaryOrganizationsTableRowXpath = "//TR[contains(@id,'organization_')]";
	private String secondaryOrganizationTableLocator = "xpath=//DIV[@id='pageContent']/TABLE[@class='list']";
	private String primaryOrganizationEditLinkLocator = "xpath=//DIV[@id='pageContent']/H2[contains(text(),'Primary Organization')]/A[contains(text(),'Edit')]";
	private String editOrganizationNameTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_displayName']";
	private String editOrganizationNameOnCertTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_certificateName']";
	private String editOrganizationCountryTextFieldLocator = "xpath=//SELECT[@id='organizationUpdate_countryId']";
	private String editOrganizationTimeZoneTextFieldLocator = "xpath=//SELECT[@id='tzlist']";
	private String editOrganizationWebSiteAddressTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_webSite']";
	private String editOrganizationCompanyStreetAddressTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_addressInfo_streetAddress']";
	private String editOrganizationCompanyCityTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_addressInfo_city']";
	private String editOrganizationCompanyStateTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_addressInfo_state']";
	private String editOrganizationCompanyCountryTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_addressInfo_country']";
	private String editOrganizationCompanyZipCodeTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_addressInfo_zip']";
	private String editOrganizationCompanyPhoneNumberTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_addressInfo_phone1']";
	private String editOrganizationCompanyFaxNumberTextFieldLocator = "xpath=//INPUT[@id='organizationUpdate_addressInfo_fax1']";
	private String addOrganizationLinkLocator = "xpath=//LI[contains(@class,'add')]/A[contains(text(),'Add')]";
	
	public ManageOrganizations(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	/**
	 * Verify that there were no errors on the Manage Organizations page and
	 * that the 'Manage Organizations' header is there.
	 */
	public void verifyManageOrganizationsPage() {
		misc.info("Verify going to Manage Organizations page went okay.");
		misc.checkForErrorMessages("verifyManageOrganizationsPage");
		if(!selenium.isElementPresent(manageOrganizationsPageHeaderLocator )) {
			fail("Could not find the header for 'Manage Organizations'.");
		}
	}
	
	/**
	 * Get the Primary organization name from the Manage Organizations page.
	 * It assumes you have already nagivated to the Manage Organizations page.
	 * 
	 * @return Primary Organizations name
	 */
	public String getPrimaryOrganizationName() {
		String result = null;
		misc.info("Note the name of the Primary Organization");
		if(selenium.isElementPresent(primaryOrganizationNameLocator)) {
			result = selenium.getText(primaryOrganizationNameLocator);
		} else {
			fail("Could not find the name of the Primary Organization");
		}
		return result;
	}
	
	/**
	 * Get a list of all the secondary organization names. If there are no 
	 * secondary organizations this will return an empty list.
	 * 
	 * @return list of secondary organization names.
	 */
	public List<String> getSecondaryOrganizationNames() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(secondaryOrganizationsTableRowXpath);
		int numRows = n.intValue();
		for(int i = 0; i < numRows; i++) {
			int row = i + 1;	// first row is the column headers so start at + 1
			String cellLocator = secondaryOrganizationTableLocator + "." + row + ".0";
			String orgName = selenium.getTable(cellLocator);
			result.add(orgName.trim());
		}
		return result;
	}
	
	public void gotoEditPrimaryOrganization() {
		misc.info("Click link to Edit Primary Organization");
		if(selenium.isElementPresent(primaryOrganizationEditLinkLocator)) {
			selenium.click(primaryOrganizationEditLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Edit primary organization");
		}
	}
	
	public void gotoEditSecondaryOrganization(String name) {
		fail("Not implemented yet");
	}
	
	public void gotoAddSecondaryOrganization() {
		misc.info("Click link to add an organizational unit");
		if(selenium.isElementPresent(addOrganizationLinkLocator)) {
			selenium.click(addOrganizationLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
			misc.checkForErrorMessages(null);
		} else {
			fail("Could not find the link to add an organization");
		}
	}
	
	public void gotoViewAll() {
		fail("Not implemented yet");
	}

	public PrimaryOrganization getPrimaryOrganization() {
		PrimaryOrganization p = new PrimaryOrganization(null);
		if(selenium.isElementPresent(editOrganizationNameTextFieldLocator)) {
			p.setName(selenium.getValue(editOrganizationNameTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationNameOnCertTextFieldLocator)) {
			p.setNameOnCert(selenium.getValue(editOrganizationNameOnCertTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationCountryTextFieldLocator)) {
			p.setCountry(selenium.getSelectedLabel(editOrganizationCountryTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationTimeZoneTextFieldLocator)) {
			p.setTimeZone(selenium.getSelectedLabel(editOrganizationTimeZoneTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationWebSiteAddressTextFieldLocator)) {
			p.setWebSiteAddress(selenium.getValue(editOrganizationWebSiteAddressTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationCompanyStreetAddressTextFieldLocator)) {
			p.setCompanyStreetAddress(selenium.getValue(editOrganizationCompanyStreetAddressTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationCompanyCityTextFieldLocator)) {
			p.setCompanyCity(selenium.getValue(editOrganizationCompanyCityTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationCompanyStateTextFieldLocator)) {
			p.setCompanyState(selenium.getValue(editOrganizationCompanyStateTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationCompanyCountryTextFieldLocator)) {
			p.setCompanyCountry(selenium.getValue(editOrganizationCompanyCountryTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationCompanyZipCodeTextFieldLocator)) {
			p.setCompanyZipCode(selenium.getValue(editOrganizationCompanyZipCodeTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationCompanyPhoneNumberTextFieldLocator)) {
			p.setCompanyPhoneNumber(selenium.getValue(editOrganizationCompanyPhoneNumberTextFieldLocator));
		}
		if(selenium.isElementPresent(editOrganizationCompanyFaxNumberTextFieldLocator)) {
			p.setCompanyFaxNumber(selenium.getValue(editOrganizationCompanyFaxNumberTextFieldLocator));
		}
		return p;
	}
}
