package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;
import static org.junit.Assert.*;

public class Admin {
	Selenium selenium;
	Misc misc;
	
	// Locators
	private String administrationPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Setup')]";
	private String manageOrganizationsLinkLocator = "xpath=//A[contains(text(),'Manage Organizations')]";
	private String manageCustomersLinkLocator = "xpath=//A[contains(text(),'Manage Customers')]";
	private String manageUsersLinkLocator = "xpath=//A[contains(text(),'Manage Users')]";
	private String manageUserRegistrationsLinkLocator = "xpath=//A[contains(text(),'Manage User Registrations')]";
	private String manageSystemSettingsLinkLocator = "xpath=//A[contains(text(),'Manage System Settings')]";
	private String manageProductTypesLinkLocator = "xpath=//A[contains(text(),'Manage Product Types')]";
	private String manageProductTypeGroupsLinkLocator = "xpath=//A[contains(text(),'Manage Product Type Groups')]";
	private String manageProductStatusesLinkLocator = "xpath=//A[contains(text(),'Manage Product Statuses')]";
	private String manageInspectionTypesLinkLocator = "xpath=//A[contains(text(),'Manage Inspection Types')]";
	private String manageEventTypeGroupsLinkLocator = "xpath=//A[contains(text(),'Manage Event Type Groups')]";
	private String manageInspectionBooksLinkLocator = "xpath=//A[contains(text(),'Manage Inspection Books')]";
	private String autoAttributeWizardLinkLocator = "xpath=//A[contains(text(),'Auto Attribute Wizard')]";
	private String manageCommentTemplatesLinkLocator = "xpath=//A[contains(text(),'Manage Comment Templates')]";
	private String dataLogLinkLocator = "xpath=//A[contains(text(),'Data Log')]";
	private String manageProductCodeMappingsLinkLocator = "xpath=//A[contains(text(),'Manage Product Code Mappings')]";

	public Admin(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Clicks the link to Manage Organizations. Need to call verify from
	 * ManageOrganizations class to confirm we arrived properly.
	 */
	public void gotoManageOrganizations() {
		misc.info("Click link to go to Manage Organizations");
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
		misc.info("Click link to go to Manage Customers");
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
		misc.info("Click link to go to Manage Users");
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
		misc.info("Click link to go to Manage User Registrations");
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
		misc.info("Click link to go to Manage System Settings");
		if(selenium.isElementPresent(manageSystemSettingsLinkLocator)) {
			selenium.click(manageSystemSettingsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage System Settings");
		}
	}
	
	/**
	 * Click the Manage Product Types link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Product Types page. In the ManageProductTypes
	 * class is a method to verify you arrived there successfully.
	 */
	public void gotoManageProductTypes() {
		misc.info("Click link to go to Manage Product Types");
		if(selenium.isElementPresent(manageProductTypesLinkLocator)) {
			selenium.click(manageProductTypesLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Product Types");
		}
	}
	
	/**
	 * Click the Manage Product Type Groups link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Product Type Groups page. In the
	 * ManageProductTypeGroups class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoManageProductTypeGroups() {
		misc.info("Click link to go to Manage Product Type Groups");
		if(selenium.isElementPresent(manageProductTypeGroupsLinkLocator)) {
			selenium.click(manageProductTypeGroupsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Product Type Groups");
		}
	}
	
	/**
	 * Click the Manage Product Statuses link. Assumes you are already on the
	 * Administration page. Will check for the Oops page but does not validate
	 * you arrived at the Manage Product Statuses page. In the
	 * ManageProductStatuses class is a method to verify you arrived there
	 * successfully.
	 */
	public void gotoManageProductStatuses() {
		misc.info("Click link to go to Manage Product Statuses");
		if(selenium.isElementPresent(manageProductStatusesLinkLocator)) {
			selenium.click(manageProductStatusesLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Product Statuses");
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
		misc.info("Click link to go to Manage Inspection Types");
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
		misc.info("Click link to go to Manage Event Type Groups");
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
		misc.info("Click link to go to Manage Inspection Books");
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
		misc.info("Click link to go to Auto Attribute Wizard");
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
		misc.info("Click link to go to Manage Comment Templates");
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
		misc.info("Click link to go to Data Log");
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
	public boolean isManageProductTypes() {
		boolean result = selenium.isElementPresent(manageProductTypesLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageProductTypeGroups() {
		boolean result = selenium.isElementPresent(manageProductTypeGroupsLinkLocator);
		return result;
	}
	
	/**
	 * Checks to see if the link to the page exist on the Administration page.
	 * 
	 * @return true if the link exists, otherwise false.
	 */
	public boolean isManageProductStatuses() {
		boolean result = selenium.isElementPresent(manageProductStatusesLinkLocator);
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
		misc.info("Verify going to Administration page went okay.");
		misc.checkForErrorMessages("VerifyAdministrationPage");
		if(!selenium.isElementPresent(administrationPageHeaderLocator)) {
			fail("Could not find the header for 'Administration'.");
		}
	}

	/**
	 * Clicks the link to Manage Product Code Mappings. Need to call verify from
	 * ManageProductCodeMappings class to confirm we arrived properly.
	 */
	public void gotoManageProductCodeMappings() {
		misc.info("Click link to go to Manage Product Code Mappings");
		if(selenium.isElementPresent(manageProductCodeMappingsLinkLocator)) {
			selenium.click(manageProductCodeMappingsLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Manage Product Code Mappings");
		}
	}
}
