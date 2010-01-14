package com.n4systems.fieldid.selenium.administration;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageUsers extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageUsersPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Users')]";
	private String addEmployeeUserLinkLocator = "xpath=//A[contains(text(),'Add Employee User')]";
	private String addEmployeeUserUserIDTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_userId']";
	private String addEmployeeUserEmailAddressTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_emailAddress']";
	private String addEmployeeUserSecurityRFIDNumberTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_securityRfidNumber']";
	private String addEmployeeUserPasswordTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_passwordEntry_password']";
	private String addEmployeeUserVerifyPassworduserIDTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_passwordEntry_passwordVerify']";
	private String addEmployeeUserFirstNameTextFieldLocator = "xpath=//INPUT[@id='firstname']";
	private String addEmployeeUserLastNameTextFieldLocator = "xpath=//INPUT[@id='lastname']";
	private String addEmployeeUserInitialsTextFieldLocator = "xpath=//INPUT[@id='initials']";
	private String addEmployeeUserPositionTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_position']";
	private String addEmployeeUserCountrySelectListLocator = "xpath=//SELECT[@id='employeeUserCreate_countryId']";
	private String addEmployeeUserTimeZoneSelectListLocator = "xpath=//SELECT[@id='tzlist']";
	private String addEmployeeUserIdentifyAssetsOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'1'_true\"]";
	private String addEmployeeUserManageSystemConfigurationOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'2'_true\"]";
	private String addEmployeeUserManageSystemUsersOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'4'_true\"]";
	private String addEmployeeUserManageCustomersOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'8'_true\"]";
	private String addEmployeeUserCreateInspectionsOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'16'_true\"]";
	private String addEmployeeUserEditInspectionsOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'32'_true\"]";
	private String addEmployeeUserManageJobsOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'64'_true\"]";
	private String addEmployeeUserManageSafetyNetworkOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'128'_true\"]";
	private String addEmployeeUserAccessWebStoreOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'256'_true\"]";
	private String addEmployeeUserIdentifyAssetsOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'1'_false\"]";
	private String addEmployeeUserManageSystemConfigurationOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'2'_false\"]";
	private String addEmployeeUserManageSystemUsersOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'4'_false\"]";
	private String addEmployeeUserManageCustomersOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'8'_false\"]";
	private String addEmployeeUserCreateInspectionsOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'16'_false\"]";
	private String addEmployeeUserEditInspectionsOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'32'_false\"]";
	private String addEmployeeUserManageJobsOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'64'_false\"]";
	private String addEmployeeUserManageSafetyNetworkOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'128'_false\"]";
	private String addEmployeeUserAccessWebStoreOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'256'_false\"]";
	private String addEmployeeUserAllOnButtonLocator = "xpath=//INPUT[@value='All On']";
	private String addEmployeeUserAllOffButtonLocator = "xpath=//INPUT[@value='All Off']";
	private String addEmployeeUserSaveButtonLocator = "xpath=//INPUT[@id='employeeUserCreate_save']";
	private String addEmployeeUserCancelLinkLocator = "xpath=//A[contains(text(),'Cancel')]";
	private String permissionTableXpath = "//DIV[@class='infoBlock']/TABLE[@class='list']";
	private String numberOfEmployeePermissionsXpath = permissionTableXpath + "/TBODY/TR[not(@class='titleRow')]";
	private String userTypeSelectListLocator = "xpath=//SELECT[@id='userType']";
	private String filterSearchButtonLocator = "xpath=//INPUT[@id='userList_search']";
	private String userListTableXpath = "//TABLE[@id='userList']";
	private String numberOfUserIDsXpath = userListTableXpath + "/TBODY/TR/TD/A";
	
	public ManageUsers(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageUsersPage() {
		misc.info("Verify going to Manage Users page went okay.");
		misc.checkForErrorMessages("verifyManageUsersPage");
		if(!selenium.isElementPresent(manageUsersPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Users'.");
		}
	}
	
	public void gotoAddEmployeeUser() {
		misc.info("Click Add Employee User");
		if(selenium.isElementPresent(addEmployeeUserLinkLocator)) {
			selenium.click(addEmployeeUserLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to add employee user");
		}
	}
	
	public void verifyAddEmployeeUserPage() {
		assertTrue("Could not find the text field for User ID", selenium.isElementPresent(addEmployeeUserUserIDTextFieldLocator));
		assertTrue("Could not find the text field for Email Address", selenium.isElementPresent(addEmployeeUserEmailAddressTextFieldLocator));
		assertTrue("Could not find the text field for Security RFID Number", selenium.isElementPresent(addEmployeeUserSecurityRFIDNumberTextFieldLocator));
		assertTrue("Could not find the text field for Password", selenium.isElementPresent(addEmployeeUserPasswordTextFieldLocator));
		assertTrue("Could not find the text field for Verify Password", selenium.isElementPresent(addEmployeeUserVerifyPassworduserIDTextFieldLocator));
		assertTrue("Could not find the text field for First Name", selenium.isElementPresent(addEmployeeUserFirstNameTextFieldLocator));
		assertTrue("Could not find the text field for Last Name", selenium.isElementPresent(addEmployeeUserLastNameTextFieldLocator));
		assertTrue("Could not find the text field for Initials", selenium.isElementPresent(addEmployeeUserInitialsTextFieldLocator));
		assertTrue("Could not find the text field for Position", selenium.isElementPresent(addEmployeeUserPositionTextFieldLocator));
		assertTrue("Could not find the select list for Country", selenium.isElementPresent(addEmployeeUserCountrySelectListLocator));
		assertTrue("Could not find the select list for Time Zone", selenium.isElementPresent(addEmployeeUserTimeZoneSelectListLocator));
		verifyPermissions();
		assertTrue("Could not find the Save button", selenium.isElementPresent(addEmployeeUserSaveButtonLocator));
		assertTrue("Could not find the Cancel link", selenium.isElementPresent(addEmployeeUserCancelLinkLocator));
	}

	public void verifyPermissions() {
		assertTrue("Could not find the On radio buttons for Identify Assets", selenium.isElementPresent(addEmployeeUserIdentifyAssetsOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Manage System Configuration", selenium.isElementPresent(addEmployeeUserManageSystemConfigurationOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Manage System Users", selenium.isElementPresent(addEmployeeUserManageSystemUsersOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Manage Customers", selenium.isElementPresent(addEmployeeUserManageCustomersOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Create Inspections", selenium.isElementPresent(addEmployeeUserCreateInspectionsOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Edit Inspections", selenium.isElementPresent(addEmployeeUserEditInspectionsOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Manage Jobs", selenium.isElementPresent(addEmployeeUserManageJobsOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Manage Safety Network", selenium.isElementPresent(addEmployeeUserManageSafetyNetworkOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Access Web Store", selenium.isElementPresent(addEmployeeUserAccessWebStoreOnRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Identify Assets", selenium.isElementPresent(addEmployeeUserIdentifyAssetsOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Manage System Configuration", selenium.isElementPresent(addEmployeeUserManageSystemConfigurationOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Manage System Users", selenium.isElementPresent(addEmployeeUserManageSystemUsersOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Manage Customers", selenium.isElementPresent(addEmployeeUserManageCustomersOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Create Inspections", selenium.isElementPresent(addEmployeeUserCreateInspectionsOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Edit Inspections", selenium.isElementPresent(addEmployeeUserEditInspectionsOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Manage Jobs", selenium.isElementPresent(addEmployeeUserManageJobsOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Manage Safety Network", selenium.isElementPresent(addEmployeeUserManageSafetyNetworkOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Access Web Store", selenium.isElementPresent(addEmployeeUserAccessWebStoreOffRadioButtonLocator));
		assertTrue("Could not find the All On button", selenium.isElementPresent(addEmployeeUserAllOnButtonLocator));
		assertTrue("Could not find the All Off button", selenium.isElementPresent(addEmployeeUserAllOffButtonLocator));
	}

	public List<String> getListOfPermissionsFromAddEmployeeUser() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(numberOfEmployeePermissionsXpath);
		int numberOfPermissions = n.intValue() - 1;	// subtract bottom row, i.e. AllOn and AllOff buttons
		int row = 1;
		String permissionNameCellLocator = "xpath=" + permissionTableXpath + "." + row + ".0";
		for(int i = 0; i < numberOfPermissions; i++, row++) {
			String permission = selenium.getTable(permissionNameCellLocator);
			result.add(permission.trim());
			permissionNameCellLocator = permissionNameCellLocator.replaceFirst("\\." + row, "." + (row+1));
		}
		return result;
	}

	public void setUserType(String s) {
		misc.info("Set the user type to " + s);
		if(selenium.isElementPresent(userTypeSelectListLocator)) {
			if(misc.isOptionPresent(userTypeSelectListLocator, s)) {
				selenium.select(userTypeSelectListLocator, s);
			} else {
				fail("Could not find the User Type '" + s + "'");
			}
		} else {
			fail("Could not find the User Type select list");
		}
	}

	public void gotoFilterSearchButton() {
		misc.info("Click the Search button for filter");
		if(selenium.isElementPresent(filterSearchButtonLocator)) {
			selenium.click(filterSearchButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Search button for the filter");
		}
	}

	public List<String> getListOfUserIDsOnCurrentPage() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(numberOfUserIDsXpath);
		int numberOfUserIDs = n.intValue();
		int row = 2; // first user id starts on row 2 of table, zero-index
		String userIDCellLocator = "xpath=" + userListTableXpath + "." + row + ".0";
		for(int i = 0; i < numberOfUserIDs; i++, row++) {
			String permission = selenium.getTable(userIDCellLocator);
			result.add(permission.trim());
			userIDCellLocator = userIDCellLocator.replaceFirst("\\." + row, "." + (row+1));
		}
		assertTrue("There should be at least one user but the list is empty!", result.size() > 0);
		return result;
	}

	public List<String> getListOfUserNamesOnCurrentPage() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(numberOfUserIDsXpath);
		int numberOfUserIDs = n.intValue();
		int row = 2; // first user id starts on row 2 of table, zero-index
		String userIDCellLocator = "xpath=" + userListTableXpath + "." + row + ".1";
		for(int i = 0; i < numberOfUserIDs; i++, row++) {
			String permission = selenium.getTable(userIDCellLocator);
			result.add(permission.trim());
			userIDCellLocator = userIDCellLocator.replaceFirst("\\." + row, "." + (row+1));
		}
		assertTrue("There should be at least one user but the list is empty!", result.size() > 0);
		return result;
	}

	public void gotoEditUser(String userID) {
		misc.info("Click the user ID '" + userID + "' to edit that user");
		String userIDEditListLocator = "xpath=" + numberOfUserIDsXpath + "[text()='" + userID +"']";
		if(selenium.isElementPresent(userIDEditListLocator)) {
			selenium.click(userIDEditListLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find a link to edit user '" + userID + "'");
		}
	}
}
