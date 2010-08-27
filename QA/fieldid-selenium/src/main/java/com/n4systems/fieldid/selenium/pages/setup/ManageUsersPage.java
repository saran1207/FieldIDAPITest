package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.CustomerUser;
import com.n4systems.fieldid.selenium.datatypes.EmployeeUser;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageUsersPage extends FieldIDPage {
	
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

	
	public ManageUsersPage(Selenium selenium) {
		super(selenium);
	}
	
	public void clickAddCustomerUserTab() {
		clickNavOption("Add Customer User");
	}
	
	public void clickAddEmployeeUserTab() {
		clickNavOption("Add Employee User");
	}
	
	public void clickViewAllTab() {
		clickNavOption("View All");
	}
	
	public void setCustomerFormFields(CustomerUser cu) {
		assertNotNull(cu);
		if(cu.getUserid() != null) {
			selenium.type("//INPUT[@id='customerUserCreate_userId']", cu.getUserid());
		}
		if(cu.getEmail() != null) {
			selenium.type("//INPUT[@id='customerUserCreate_emailAddress']", cu.getEmail());
		}
		if(cu.getSecurityRFIDNumber() != null) {
			selenium.type("//INPUT[@id='customerUserCreate_securityRfidNumber']", cu.getSecurityRFIDNumber());
		}
		if(cu.getPassword() != null) {
			selenium.type("//INPUT[@id='customerUserCreate_passwordEntry_password']", cu.getPassword());
		}
		if(cu.getVerifyPassword() != null) {
			selenium.type("//INPUT[@id='customerUserCreate_passwordEntry_passwordVerify']", cu.getVerifyPassword());
		}
		if(cu.getOwner() != null) {
			OrgPicker orgPicker = getOrgPicker();
			orgPicker.clickChooseOwner();
			orgPicker.setOwner(cu.getOwner());
			orgPicker.clickSelectOwner();
		}
		if(cu.getFirstName() != null) {
			selenium.type("//INPUT[@id='firstname']", cu.getFirstName());
		}
		if(cu.getLastName() != null) {
			selenium.type("//INPUT[@id='lastname']", cu.getLastName());
		}
		if(cu.getInitials() != null) {
			selenium.type("//INPUT[@id='initials']", cu.getInitials());
		}
		if(cu.getPosition() != null) {
			selenium.type("//INPUT[@id='customerUserCreate_position']", cu.getPosition());
		}
		if(cu.getCountry() != null) {
			if(isOptionPresent("//SELECT[@id='customerUserCreate_countryId']", cu.getCountry())) {
				selenium.select("//SELECT[@id='customerUserCreate_countryId']", cu.getCountry());
				waitForAjax();
			} else {
				fail("The country '" + cu.getCountry() + "' does not exist on the select list");
			}
		}
		if(cu.getTimeZone() != null) {
			if(isOptionPresent("//SELECT[@id='tzlist']", cu.getTimeZone())) {
				selenium.select("//SELECT[@id='tzlist']", cu.getTimeZone());
			} else {
				fail("The time zone '" + cu.getTimeZone() + "' does not exist on the select list");
			}
		}
	}
	
	public void setAddEmployeeUser(EmployeeUser employeeUser) {
		assertNotNull(employeeUser);
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
			OrgPicker orgPicker = getOrgPicker();
			orgPicker.clickChooseOwner();
			orgPicker.setOwner(employeeUser.getOwner());
			orgPicker.clickSelectOwner();
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
			if(isOptionPresent(addEmployeeUserCountrySelectListLocator, employeeUser.getCountry())) {
				selenium.select(addEmployeeUserCountrySelectListLocator, employeeUser.getCountry());
				waitForAjax();
			} else {
				fail("The country '" + employeeUser.getCountry() + "' does not exist on the select list");
			}
		}
		if(employeeUser.getTimeZone() != null) {
			if(isOptionPresent(addEmployeeUserTimeZoneSelectListLocator, employeeUser.getTimeZone())) {
				selenium.select(addEmployeeUserTimeZoneSelectListLocator, employeeUser.getTimeZone());
			} else {
				fail("The time zone '" + employeeUser.getTimeZone() + "' does not exist on the select list");
			}
		}
		if(employeeUser.getPermissions() != null && employeeUser.getPermissions().size() > 0) {
			clickPermissionsAllOff();
			setPermissionsAddEmployeeUser(employeeUser.getPermissions());
		}
	}
	
	public void setPermissionsAddEmployeeUser(List<String> permissions) {
		String permissionTableLocator1 = "css=.permissions";
		for(String permission : permissions) {
			String permissionOnButtonLocator = permissionTableLocator1 + " td:contains('" + permission + "') ~ td input[value='true']";
			selenium.check(permissionOnButtonLocator);
		}
	}
	
	public void clickSaveCustomerUser() {
		selenium.click("//INPUT[@id='customerUserCreate_save']");
		waitForPageToLoad();
	}
	
	public void clickSaveEmployeeUser() {
		selenium.click("//INPUT[@id='employeeUserCreate_save']");
		waitForPageToLoad();
	}
	
	public void selectSearchUserType(String type) {
		selenium.select("//SELECT[@id='userType']", type);
	}
	
	public void enterSearchNameFilter(String filter) {
		selenium.type("//INPUT[@id='nameFilter']", filter);
	}
	
	public void clickSearchButton() {
		selenium.click("//INPUT[@id='userList_search']");
		waitForPageToLoad();
	}
	
	public void clickPermissionsAllOff() {
		selenium.click("//INPUT[@value='All Off']");
	}
	
	public List<String> getListOfUserIDsOnCurrentPage() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount("//table[@id='userList']/tbody/tr/td/a");
		int numberOfUserIDs = n.intValue();
		int row = 2; // first user id starts on row 2 of table, zero-index
		String userIDCellLocator = "//table[@id='userList']" + "." + row + ".0";
		for(int i = 0; i < numberOfUserIDs; i++, row++) {
			String permission = selenium.getTable(userIDCellLocator);
			result.add(permission.trim());
			userIDCellLocator = userIDCellLocator.replaceFirst("\\." + row, "." + (row+1));
		}
		assertTrue("There should be at least one user but the list is empty!", result.size() > 0);
		return result;
	}

	public String clickFirstSearchResult() {
		String locator = "xpath=//table[@id='userList']/tbody/tr[3]/td[1]/a";
		String userName = selenium.getText(locator);
		selenium.click(locator);
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		return userName;
	}

	public void clickChangePasswordTab() {
		clickNavOption("Change Password");
	}

	public void clickCancelChangePassword() {
		selenium.click("label.cancel");
		waitForPageToLoad();
	}

	public String getUserId() {
		return selenium.getValue("//div[@class='infoBlock']//input[@name='userId']").trim();
	}
	
}
