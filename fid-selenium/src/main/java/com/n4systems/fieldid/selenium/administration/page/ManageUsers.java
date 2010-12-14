package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import com.n4systems.fieldid.selenium.datatypes.CustomerUser;
import com.n4systems.fieldid.selenium.datatypes.EmployeeUser;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.util.UserType;

public class ManageUsers {
	FieldIdSelenium selenium;
	MiscDriver misc;

	public final static String USER_TYPE_ALL = UserType.ALL.getLabel();
	public final static String USER_TYPE_READONLY = UserType.READONLY.getLabel();
	public final static String USER_TYPE_EMPLOYEE = UserType.FULL.getLabel();
	
	private String manageUsersPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Users')]";
	private String addEmployeeUserLinkLocator = "xpath=//A[contains(text(),'Add Employee User')]";
	private String addEmployeeUserUserIDTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_userId']";
	private String addEmployeeUserEmailAddressTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_emailAddress']";
	private String addEmployeeUserSecurityRFIDNumberTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_securityRfidNumber']";
	private String addEmployeeUserPasswordTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_passwordEntry_password']";
	private String addEmployeeUserVerifyPasswordTextFieldLocator = "xpath=//INPUT[@id='employeeUserCreate_passwordEntry_passwordVerify']";
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
	private String addEmployeeUserCreateEventsOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'16'_true\"]";
	private String addEmployeeUserEditEventsOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'32'_true\"]";
	private String addEmployeeUserManageJobsOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'64'_true\"]";
	private String addEmployeeUserManageSafetyNetworkOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'128'_true\"]";
	private String addEmployeeUserAccessWebStoreOnRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'256'_true\"]";
	private String addEmployeeUserIdentifyAssetsOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'1'_false\"]";
	private String addEmployeeUserManageSystemConfigurationOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'2'_false\"]";
	private String addEmployeeUserManageSystemUsersOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'4'_false\"]";
	private String addEmployeeUserManageCustomersOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'8'_false\"]";
	private String addEmployeeUserCreateEventsOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'16'_false\"]";
	private String addEmployeeUserEditEventsOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'32'_false\"]";
	private String addEmployeeUserManageJobsOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'64'_false\"]";
	private String addEmployeeUserManageSafetyNetworkOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'128'_false\"]";
	private String addEmployeeUserAccessWebStoreOffRadioButtonLocator = "xpath=//INPUT[@id=\"employeeUserCreate_userPermissions_'256'_false\"]";
	private String addEmployeeUserAllOnButtonLocator = "xpath=//INPUT[@value='All On']";
	private String addEmployeeUserAllOffButtonLocator = "xpath=//INPUT[@value='All Off']";
	private String addEmployeeUserSaveButtonLocator = "xpath=//INPUT[@id='employeeUserCreate_save']";
	private String addEmployeeUserCancelLinkLocator = "xpath=//A[contains(text(),'Cancel')]";
	private String permissionTableLocator = ".permissions";
	private String permissionTableXpath = "//TABLE[contains(concat(' ',normalize-space(@class),' '),' permissions ')]";
	private String numberOfEmployeePermissionsXpath = permissionTableXpath + "/TBODY/TR[not(@class='titleRow')]";
	private String userTypeSelectListLocator = "xpath=//SELECT[@id='userType']";
	private String filterSearchButtonLocator = "xpath=//INPUT[@id='userList_search']";
	private String userListTableXpath = "//TABLE[@id='userList']";
	private String numberOfUserIDsXpath = userListTableXpath + "/TBODY/TR/TD/A";
	private String addCustomerUserLinkLocator = "xpath=//A[contains(text(),'Add Customer User')]";
	private String addCustomerUserUserIDTextFieldLocator = "xpath=//INPUT[@id='customerUserCreate_userId']";
	private String addCustomerUserEmailAddressTextFieldLocator = "xpath=//INPUT[@id='customerUserCreate_emailAddress']";
	private String addCustomerUserSecurityRFIDNumberTextFieldLocator = "xpath=//INPUT[@id='customerUserCreate_securityRfidNumber']";
	private String addCustomerPasswordTextFieldLocator = "xpath=//INPUT[@id='customerUserCreate_passwordEntry_password']";
	private String addCustomerVerifyPasswordTextFieldLocator = "xpath=//INPUT[@id='customerUserCreate_passwordEntry_passwordVerify']";
	private String addCustomerFirstNameTextFieldLocator = "xpath=//INPUT[@id='firstname']";
	private String addCustomerLastNameTextFieldLocator = "xpath=//INPUT[@id='lastname']";
	private String addCustomerInitialsTextFieldLocator = "xpath=//INPUT[@id='initials']";
	private String addCustomerPositionTextFieldLocator = "xpath=//INPUT[@id='customerUserCreate_position']";
	private String addCustomerCountrySelectListLocator = "xpath=//SELECT[@id='customerUserCreate_countryId']";
	private String addCustomerTimeZoneSelectListLocator = "xpath=//SELECT[@id='tzlist']";
	private String addCustomerUserSaveButtonLocator = "xpath=//INPUT[@id='customerUserCreate_save']";
	private String addCustomerUserCancelLinkLocator = "xpath=//A[contains(text(),'Cancel')]";
	private String manageUsersViewAllLinkLocator = "xpath=//A[contains(text(),'View All')]";
	private String filterNameTextFieldLocator = "xpath=//INPUT[@id='nameFilter']";
	
	public ManageUsers(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageUsersPage() {
		misc.checkForErrorMessages("verifyManageUsersPage");
		if(!selenium.isElementPresent(manageUsersPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Users'.");
		}
	}
	
	public void gotoAddEmployeeUser() {
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
		assertTrue("Could not find the text field for Verify Password", selenium.isElementPresent(addEmployeeUserVerifyPasswordTextFieldLocator));
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
		assertTrue("Could not find the On radio buttons for Create Events", selenium.isElementPresent(addEmployeeUserCreateEventsOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Edit Events", selenium.isElementPresent(addEmployeeUserEditEventsOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Manage Jobs", selenium.isElementPresent(addEmployeeUserManageJobsOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Manage Safety Network", selenium.isElementPresent(addEmployeeUserManageSafetyNetworkOnRadioButtonLocator));
		assertTrue("Could not find the On radio buttons for Access Web Store", selenium.isElementPresent(addEmployeeUserAccessWebStoreOnRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Identify Assets", selenium.isElementPresent(addEmployeeUserIdentifyAssetsOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Manage System Configuration", selenium.isElementPresent(addEmployeeUserManageSystemConfigurationOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Manage System Users", selenium.isElementPresent(addEmployeeUserManageSystemUsersOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Manage Customers", selenium.isElementPresent(addEmployeeUserManageCustomersOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Create Events", selenium.isElementPresent(addEmployeeUserCreateEventsOffRadioButtonLocator));
		assertTrue("Could not find the Off radio buttons for Edit Events", selenium.isElementPresent(addEmployeeUserEditEventsOffRadioButtonLocator));
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
		String userIDEditListLocator = "xpath=" + numberOfUserIDsXpath + "[text()='" + userID +"']";
		if(selenium.isElementPresent(userIDEditListLocator)) {
			selenium.click(userIDEditListLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find a link to edit user '" + userID + "'");
		}
	}

	/**
	 * Click the "All On" button in the Permissions section.
	 */
	public void setPermissionsAllOn() {
		selenium.click(addEmployeeUserAllOnButtonLocator);
	}

	public void setAddEmployeeUser(EmployeeUser employeeUser) {
		assertNotNull(employeeUser);
		verifyAddEmployeeUserPage();
		if(employeeUser.getUserid() != null) {
			selenium.type(addEmployeeUserUserIDTextFieldLocator, employeeUser.getUserid());
		}
		if(employeeUser.getEmail() != null) {
			selenium.type(addEmployeeUserEmailAddressTextFieldLocator, employeeUser.getEmail());
		}
		if(employeeUser.getSecurityRFIDNumber() != null) {
			selenium.type(addEmployeeUserSecurityRFIDNumberTextFieldLocator, employeeUser.getSecurityRFIDNumber());
		}
		if(employeeUser.getPassword() != null) {
			selenium.type(addEmployeeUserPasswordTextFieldLocator, employeeUser.getPassword());
		}
		if(employeeUser.getVerifyPassword() != null) {
			selenium.type(addEmployeeUserVerifyPasswordTextFieldLocator, employeeUser.getVerifyPassword());
		}
		if(employeeUser.getOwner() != null) {
			misc.gotoChooseOwner();
			misc.setOwner(employeeUser.getOwner());
			misc.gotoSelectOwner();
		}
		if(employeeUser.getFirstName() != null) {
			selenium.type(addEmployeeUserFirstNameTextFieldLocator, employeeUser.getFirstName());
		}
		if(employeeUser.getLastName() != null) {
			selenium.type(addEmployeeUserLastNameTextFieldLocator, employeeUser.getLastName());
		}
		if(employeeUser.getInitials() != null) {
			selenium.type(addEmployeeUserInitialsTextFieldLocator, employeeUser.getInitials());
		}
		if(employeeUser.getPosition() != null) {
			selenium.type(addEmployeeUserPositionTextFieldLocator, employeeUser.getPosition());
		}
		if(employeeUser.getCountry() != null) {
			if(misc.isOptionPresent(addEmployeeUserCountrySelectListLocator, employeeUser.getCountry())) {
				selenium.select(addEmployeeUserCountrySelectListLocator, employeeUser.getCountry());
				misc.waitForTimeZoneToUpdate();
			} else {
				fail("The country '" + employeeUser.getCountry() + "' does not exist on the select list");
			}
		}
		if(employeeUser.getTimeZone() != null) {
			if(misc.isOptionPresent(addEmployeeUserTimeZoneSelectListLocator, employeeUser.getTimeZone())) {
				selenium.select(addEmployeeUserTimeZoneSelectListLocator, employeeUser.getTimeZone());
			} else {
				fail("The time zone '" + employeeUser.getTimeZone() + "' does not exist on the select list");
			}
		}
		if(employeeUser.getPermissions() != null && employeeUser.getPermissions().size() > 0) {
			setPermissionsAllOff();
			setPermissionsAddEmployeeUser(employeeUser.getPermissions());
		}
	}

	public void setPermissionsAddEmployeeUser(List<String> permissions) {
		verifyPermissions();
		String permissionTableLocator1 = "css=" + permissionTableLocator;
		for(String permission : permissions) {
			String permissionOnButtonLocator = permissionTableLocator1 + " td:contains('" + permission + "') ~ td input[value='true']";
			selenium.check(permissionOnButtonLocator);
		}
	}

	/**
	 * Click the "All Off" button in the Permissions section.
	 */
	public void setPermissionsAllOff() {
		selenium.click(addEmployeeUserAllOffButtonLocator);
	}

	public void gotoSaveEmployeeUser() {
		selenium.click(addEmployeeUserSaveButtonLocator);
		misc.waitForPageToLoadAndCheckForOopsPage();
	}

	public void gotoAddCustomerUser() {
		if(selenium.isElementPresent(addCustomerUserLinkLocator)) {
			selenium.click(addCustomerUserLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to add customer user");
		}
	}

	public void setAddCustomerUser(CustomerUser cu) {
		assertNotNull(cu);
		verifyAddCustomerUserPage();
		if(cu.getUserid() != null) {
			selenium.type(addCustomerUserUserIDTextFieldLocator, cu.getUserid());
		}
		if(cu.getEmail() != null) {
			selenium.type(addCustomerUserEmailAddressTextFieldLocator, cu.getEmail());
		}
		if(cu.getSecurityRFIDNumber() != null) {
			selenium.type(addCustomerUserSecurityRFIDNumberTextFieldLocator, cu.getSecurityRFIDNumber());
		}
		if(cu.getPassword() != null) {
			selenium.type(addCustomerPasswordTextFieldLocator, cu.getPassword());
		}
		if(cu.getVerifyPassword() != null) {
			selenium.type(addCustomerVerifyPasswordTextFieldLocator, cu.getVerifyPassword());
		}
		if(cu.getOwner() != null) {
			misc.gotoChooseOwner();
			misc.setOwner(cu.getOwner());
			misc.gotoSelectOwner();
		}
		if(cu.getFirstName() != null) {
			selenium.type(addCustomerFirstNameTextFieldLocator, cu.getFirstName());
		}
		if(cu.getLastName() != null) {
			selenium.type(addCustomerLastNameTextFieldLocator, cu.getLastName());
		}
		if(cu.getInitials() != null) {
			selenium.type(addCustomerInitialsTextFieldLocator, cu.getInitials());
		}
		if(cu.getPosition() != null) {
			selenium.type(addCustomerPositionTextFieldLocator, cu.getPosition());
		}
		if(cu.getCountry() != null) {
			if(misc.isOptionPresent(addCustomerCountrySelectListLocator, cu.getCountry())) {
				selenium.select(addCustomerCountrySelectListLocator, cu.getCountry());
				misc.waitForTimeZoneToUpdate();
			} else {
				fail("The country '" + cu.getCountry() + "' does not exist on the select list");
			}
		}
		if(cu.getTimeZone() != null) {
			if(misc.isOptionPresent(addCustomerTimeZoneSelectListLocator, cu.getTimeZone())) {
				selenium.select(addCustomerTimeZoneSelectListLocator, cu.getTimeZone());
			} else {
				fail("The time zone '" + cu.getTimeZone() + "' does not exist on the select list");
			}
		}
	}

	private void verifyAddCustomerUserPage() {
		assertTrue("Could not find the text field for User ID", selenium.isElementPresent(addCustomerUserUserIDTextFieldLocator));
		assertTrue("Could not find the text field for Email Address", selenium.isElementPresent(addCustomerUserEmailAddressTextFieldLocator));
		assertTrue("Could not find the text field for Security RFID Number", selenium.isElementPresent(addCustomerUserSecurityRFIDNumberTextFieldLocator));
		assertTrue("Could not find the text field for Password", selenium.isElementPresent(addCustomerPasswordTextFieldLocator));
		assertTrue("Could not find the text field for Verify Password", selenium.isElementPresent(addCustomerVerifyPasswordTextFieldLocator));
		assertTrue("Could not find the text field for First Name", selenium.isElementPresent(addCustomerFirstNameTextFieldLocator));
		assertTrue("Could not find the text field for Last Name", selenium.isElementPresent(addCustomerLastNameTextFieldLocator));
		assertTrue("Could not find the text field for Initials", selenium.isElementPresent(addCustomerInitialsTextFieldLocator));
		assertTrue("Could not find the text field for Position", selenium.isElementPresent(addCustomerPositionTextFieldLocator));
		assertTrue("Could not find the select list for Country", selenium.isElementPresent(addCustomerCountrySelectListLocator));
		assertTrue("Could not find the select list for Time Zone", selenium.isElementPresent(addCustomerTimeZoneSelectListLocator));
		assertTrue("Could not find the Save button", selenium.isElementPresent(addCustomerUserSaveButtonLocator));
		assertTrue("Could not find the Cancel link", selenium.isElementPresent(addCustomerUserCancelLinkLocator));
	}

	public void gotoSaveCustomerUser() {
		selenium.click(addCustomerUserSaveButtonLocator);
		misc.waitForPageToLoadAndCheckForOopsPage();
	}

	public void gotoViewAll() {
		if(selenium.isElementPresent(manageUsersViewAllLinkLocator)) {
			selenium.click(manageUsersViewAllLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not locate the link 'View All'");
		}
	}

	public void setNameFilter(String userid) {
		assertNotNull(userid);
		if(selenium.isElementPresent(filterNameTextFieldLocator)) {
			selenium.type(filterNameTextFieldLocator, userid);
		} else {
			fail("Could not find the Name text field for the Filter");
		}
	}

	public void gotoSearchFilter() {
		if(selenium.isElementPresent(filterSearchButtonLocator)) {
			selenium.click(filterSearchButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Search button");
		}
	}
}
