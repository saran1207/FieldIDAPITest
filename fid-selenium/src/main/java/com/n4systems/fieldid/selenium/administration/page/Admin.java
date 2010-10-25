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
	private String manageInspectionTypesLinkLocator = "xpath=//A[contains(text(),'Manage Inspection Types')]";
	private String manageEventTypeGroupsLinkLocator = "xpath=//A[contains(text(),'Manage Event Type Groups')]";
	private String manageInspectionBooksLinkLocator = "xpath=//A[contains(text(),'Manage Inspection Books')]";
	private String autoAttributeWizardLinkLocator = "xpath=//A[contains(text(),'Auto Attribute Wizard')]";
	private String manageCommentTemplatesLinkLocator = "xpath=//A[contains(text(),'Manage Comment Templates')]";
	private String dataLogLinkLocator = "xpath=//A[contains(text(),'Data Log')]";
	private String manageAssetCodeMappingsLinkLocator = "xpath=//A[contains(text(),'Manage Asset Code Mappings')]";

	public Admin(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Clicks the link to Manage Organizations. Need to call verify from
	 * ManageOrganizations class to confirm we arrived properly.
	 */
	public void gotoManageOrganizations() {
		if(selenium.isElementPresent(manageOrganizationsLinkLocator)) {
			selenium.click(manageOrganizationsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Organizations");
		}
	}
	
	/**
	 * Click the Manage Customers link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Customers page. In the ManageCustomers class
	 * is a method to verify you arrived there successfully.
	 */
	public void gotoManageCustomers() {
		if(selenium.isElementPresent(manageCustomersLinkLocator)) {
			selenium.click(manageCustomersLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Customers");
		}
	}
	
	/**
	 * Click the Manage Users link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Users page. In the ManageUsers class
	 * is a method to verify you arrived there successfully.
	 */
	public void gotoManageUsers() {
		if(selenium.isElementPresent(manageUsersLinkLocator)) {
			selenium.click(manageUsersLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Users");
		}
	}
	
	/**
	 * Click the Manage User Registration link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage User Registration page. In the
	 * ManageUserRegistration class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoManageUserRegistrations() {
		if(selenium.isElementPresent(manageUserRegistrationsLinkLocator)) {
			selenium.click(manageUserRegistrationsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage User Registration");
		}
	}
	
	/**
	 * Click the Manage System Settings link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage System Settings page. In the
	 * ManageSystemSettings class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoManageSystemSettings() {
		if(selenium.isElementPresent(manageSystemSettingsLinkLocator)) {
			selenium.click(manageSystemSettingsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage System Settings");
		}
	}
	
	/**
	 * Click the Manage Asset Types link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Asset Types page. In the ManageAssetTypes
	 * class is a method to verify you arrived there successfully.
	 */
	public void gotoManageAssetTypes() {
		if(selenium.isElementPresent(manageAssetTypesLinkLocator)) {
			selenium.click(manageAssetTypesLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Asset Types");
		}
	}
	
	/**
	 * Click the Manage Asset Type Groups link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Asset Type Groups page. In the
	 * ManageAssetTypeGroups class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoManageAssetTypeGroups() {
		if(selenium.isElementPresent(manageAssetTypeGroupsLinkLocator)) {
			selenium.click(manageAssetTypeGroupsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Asset Type Groups");
		}
	}
	
	/**
	 * Click the Manage Asset Statuses link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Asset Statuses page. In the
	 * ManageAssetStatuses class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoManageAssetStatuses() {
		if(selenium.isElementPresent(manageAssetStatusesLinkLocator)) {
			selenium.click(manageAssetStatusesLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Asset Statuses");
		}
	}
	
	/**
	 * Click the Manage Inspection Types link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Inspection Types page. In the
	 * ManageInspectionTypes class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoManageInspectionTypes() {
		if(selenium.isElementPresent(manageInspectionTypesLinkLocator)) {
			selenium.click(manageInspectionTypesLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Inspection Types");
		}
	}
	
	/**
	 * Click the Manage Event Type Groups link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Event Type Groups page. In the
	 * ManageEventTypeGroups class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoManageEventTypeGroups() {
		if(selenium.isElementPresent(manageEventTypeGroupsLinkLocator)) {
			selenium.click(manageEventTypeGroupsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Event Type Groups");
		}
	}
	
	/**
	 * Click the Manage Inspection Books link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Inspection Books page. In the
	 * ManageInspectionBooks class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoManageInspectionBooks() {
		if(selenium.isElementPresent(manageInspectionBooksLinkLocator)) {
			selenium.click(manageInspectionBooksLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Inspection Books");
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
	public boolean isManageInspectionTypes() {
		boolean result = selenium.isElementPresent(manageInspectionTypesLinkLocator);
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
		boolean result = selenium.isElementPresent(manageInspectionBooksLinkLocator);
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
