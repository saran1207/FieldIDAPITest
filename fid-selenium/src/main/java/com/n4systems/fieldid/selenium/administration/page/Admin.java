package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class Admin {
	FieldIdSelenium selenium;
	MiscDriver misc;
	
	// Locators
	private String administrationPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Setup')]";
	private String manageOrganizationsLinkLocator = "xpath=//A[contains(text(),'Manage Organizations')]";
	private String manageCustomersLinkLocator = "xpath=//A[contains(text(),'Manage Customers')]";
	private String manageUsersLinkLocator = "xpath=//A[contains(text(),'Manage Users')]";
	private String manageUserRegistrationsLinkLocator = "xpath=//A[contains(text(),'Manage User Registrations')]";
	private String manageSystemSettingsLinkLocator = "xpath=//A[contains(text(),'Manage System Settings')]";
	private String manageAssetTypesLinkLocator = "xpath=//A[contains(text(),'Manage Asset Types')]";
	private String manageAssetTypeGroupsLinkLocator = "xpath=//A[contains(text(),'Manage Asset Type Groups')]";
	private String manageAssetStatusesLinkLocator = "xpath=//A[contains(text(),'Manage Asset Statuses')]";
	private String manageEventTypesLinkLocator = "xpath=//A[contains(text(),'Manage Event Types')]";
	private String manageEventTypeGroupsLinkLocator = "xpath=//A[contains(text(),'Manage Event Type Groups')]";
	private String manageEventBooksLinkLocator = "xpath=//A[contains(text(),'Manage Event Books')]";
	private String autoAttributeWizardLinkLocator = "xpath=//A[contains(text(),'Auto Attribute Wizard')]";
	private String manageCommentTemplatesLinkLocator = "xpath=//A[contains(text(),'Manage Comment Templates')]";
	private String dataLogLinkLocator = "xpath=//A[contains(text(),'Data Log')]";
	private String manageAssetCodeMappingsLinkLocator = "xpath=//A[contains(text(),'Manage Asset Code Mappings')]";

	public Admin(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void gotoManageOrganizations() {
		if(selenium.isElementPresent(manageOrganizationsLinkLocator)) {
			selenium.click(manageOrganizationsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Organizations");
		}
	}
	
	public void gotoManageCustomers() {
		if(selenium.isElementPresent(manageCustomersLinkLocator)) {
			selenium.click(manageCustomersLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Customers");
		}
	}
	
	public void gotoManageUsers() {
		if(selenium.isElementPresent(manageUsersLinkLocator)) {
			selenium.click(manageUsersLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Users");
		}
	}
	
	public void gotoManageUserRegistrations() {
		if(selenium.isElementPresent(manageUserRegistrationsLinkLocator)) {
			selenium.click(manageUserRegistrationsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage User Registration");
		}
	}
	
	public void gotoManageSystemSettings() {
		if(selenium.isElementPresent(manageSystemSettingsLinkLocator)) {
			selenium.click(manageSystemSettingsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage System Settings");
		}
	}
	
	public void gotoManageAssetTypes() {
		if(selenium.isElementPresent(manageAssetTypesLinkLocator)) {
			selenium.click(manageAssetTypesLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Asset Types");
		}
	}
	
	public void gotoManageAssetTypeGroups() {
		if(selenium.isElementPresent(manageAssetTypeGroupsLinkLocator)) {
			selenium.click(manageAssetTypeGroupsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Asset Type Groups");
		}
	}
	
	public void gotoManageAssetStatuses() {
		if(selenium.isElementPresent(manageAssetStatusesLinkLocator)) {
			selenium.click(manageAssetStatusesLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Asset Statuses");
		}
	}
	
	public void gotoManageEventTypes() {
		if(selenium.isElementPresent(manageEventTypesLinkLocator)) {
			selenium.click(manageEventTypesLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Event Types");
		}
	}
	
	public void gotoManageEventTypeGroups() {
		if(selenium.isElementPresent(manageEventTypeGroupsLinkLocator)) {
			selenium.click(manageEventTypeGroupsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Event Type Groups");
		}
	}
	
	public void gotoManageEventBooks() {
		if(selenium.isElementPresent(manageEventBooksLinkLocator)) {
			selenium.click(manageEventBooksLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Event Books");
		}
	}
	
	/**
	 * Click the Auto Attribute Wizard link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Auto Attributes Wizard page. In the
	 * AutoAttributeWizard class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoAutoAttributeWizard() {
		if(selenium.isElementPresent(autoAttributeWizardLinkLocator)) {
			selenium.click(autoAttributeWizardLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Auto Attribute Wizard");
		}
	}
	
	/**
	 * Click the Manage Comment Templates link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Comment Templates page. In the
	 * ManageCommentTemplates class is a method to verify you arrived there 
	 * successfully.
	 */
	public void gotoManageCommentTemplates() {
		if(selenium.isElementPresent(manageCommentTemplatesLinkLocator)) {
			selenium.click(manageCommentTemplatesLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Comment Templates");
		}
	}
	
	/**
	 * Click the Data Log link. Assumes you are already on the Administration
	 * page. Will check for the Oops page but does not validate you arrived at
	 * the Data Log page. In the DataLog class is a method to verify you arrived
	 * there successfully.
	 */
	public void gotoDataLog() {
		if(selenium.isElementPresent(dataLogLinkLocator)) {
			selenium.click(dataLogLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Data Log");
		}
	}

	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageOrganizations() {
		boolean result = selenium.isElementPresent(manageOrganizationsLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageCustomers() {
		boolean result = selenium.isElementPresent(manageCustomersLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageUsers() {
		boolean result = selenium.isElementPresent(manageUsersLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageUserRegistrations() {
		boolean result = selenium.isElementPresent(manageUserRegistrationsLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageSystemSettings() {
		boolean result = selenium.isElementPresent(manageSystemSettingsLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageAssetTypes() {
		boolean result = selenium.isElementPresent(manageAssetTypesLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageAssetTypeGroups() {
		boolean result = selenium.isElementPresent(manageAssetTypeGroupsLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageAssetStatuses() {
		boolean result = selenium.isElementPresent(manageAssetStatusesLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageEventTypes() {
		boolean result = selenium.isElementPresent(manageEventTypesLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageEventTypeGroups() {
		boolean result = selenium.isElementPresent(manageEventTypeGroupsLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageInspectionBooks() {
		boolean result = selenium.isElementPresent(manageEventBooksLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isAutoAttributeWizard() {
		boolean result = selenium.isElementPresent(autoAttributeWizardLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageCommentTemplates() {
		boolean result = selenium.isElementPresent(manageCommentTemplatesLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isDataLog() {
		boolean result = selenium.isElementPresent(dataLogLinkLocator);
		return result;
	}

	/**
	 * Checks to see if there are any error messages on the page and checks
	 * for the header "Administration" on the page.
	 */
	public void verifyAdministrationPage() {
		misc.checkForErrorMessages("VerifyAdministrationPage");
		if(!selenium.isElementPresent(administrationPageHeaderLocator)) {
			fail("Could not find the header for 'Administration'.");
		}
	}

	/**
	 * Clicks the link to Manage Asset Code Mappings. Need to call verify from
	 * ManageAssetCodeMappings class to confirm we arrived properly.
	 */
	public void gotoManageAssetCodeMappings() {
		if(selenium.isElementPresent(manageAssetCodeMappingsLinkLocator)) {
			selenium.click(manageAssetCodeMappingsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Asset Code Mappings");
		}
	}
}
