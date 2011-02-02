package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.CustomerUser;
import com.n4systems.fieldid.selenium.datatypes.EmployeeUser;
import com.n4systems.fieldid.selenium.datatypes.SystemUser;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.util.UserType;
import com.thoughtworks.selenium.Selenium;

public class ManageUsersPage extends FieldIDPage {
	
	public final static String USER_TYPE_ALL = UserType.ALL.getLabel();
	public final static String USER_TYPE_READONLY = UserType.READONLY.getLabel();
	public final static String USER_TYPE_LITE = UserType.LITE.getLabel();
	public final static String USER_TYPE_FULL = UserType.FULL.getLabel();

	public ManageUsersPage(Selenium selenium) {
		super(selenium);
	}
	
	public void clickAddUserTab() {
	    clickNavOption("Add User");
    }
	
	public void clickAddFullUser() {
		selenium.click("//input[@id='addFullUser']");
		waitForPageToLoad();
	}

	public void clickAddLiteUser() {
		selenium.click("//input[@id='addLiteUser']");
		waitForPageToLoad();
	}

	public void clickAddReadOnlyUser() {
		selenium.click("//input[@id='addReadOnlyUser']");
		waitForPageToLoad();
	}

	public void clickViewAllTab() {
		clickNavOption("View All");
	}

	public void setReadOnlyUserFormFields(CustomerUser user) {
		assertNotNull(user);
		setCommonUserFields(user, "readOnly");
	}
	
	public void setLiteUserFormFields(EmployeeUser user) {
		assertNotNull(user);
		setCommonUserFields(user, "lite");
		if (user.getPermissions() != null && user.getPermissions().size() > 0) {
			clickPermissionsAllOff();
			setUserPermissions(user.getPermissions());
		}
	}

	public void setFullUserFormFields(EmployeeUser user) {
		assertNotNull(user);
		setCommonUserFields(user, "employee");
		if (user.getPermissions() != null && user.getPermissions().size() > 0) {
			clickPermissionsAllOff();
			setUserPermissions(user.getPermissions());
		}
	}

	private void setCommonUserFields(SystemUser user, String prefix) {
		if (user.getUserid() != null) {
			selenium.type("//INPUT[@id='" + prefix + "UserCreate_userId']", user.getUserid());
		}
		if (user.getEmail() != null) {
			selenium.type("//INPUT[@id='" + prefix + "UserCreate_emailAddress']", user.getEmail());
		}
		if (user.getSecurityRFIDNumber() != null) {
			selenium.type("//INPUT[@id='" + prefix + "UserCreate_securityRfidNumber']", user.getSecurityRFIDNumber());
		}
		if (user.getPassword() != null) {
			selenium.type("//INPUT[@id='" + prefix + "UserCreate_passwordEntry_password']", user.getPassword());
		}
		if (user.getVerifyPassword() != null) {
			selenium.type("//INPUT[@id='" + prefix + "UserCreate_passwordEntry_passwordVerify']", user.getVerifyPassword());
		}
		if (user.getOwner() != null) {
			OrgPicker orgPicker = getOrgPicker();
			orgPicker.clickChooseOwner();
			orgPicker.setOwner(user.getOwner());
			orgPicker.clickSelectOwner();
		}
		if (user.getFirstName() != null) {
			selenium.type("//INPUT[@id='firstname']", user.getFirstName());
		}
		if (user.getLastName() != null) {
			selenium.type("//INPUT[@id='lastname']", user.getLastName());
		}
		if (user.getInitials() != null) {
			selenium.type("//INPUT[@id='initials']", user.getInitials());
		}
		if (user.getPosition() != null) {
			selenium.type("//INPUT[@id='" + prefix + "UserCreate_position']", user.getPosition());
		}
		if (user.getCountry() != null) {
			if (isOptionPresent("//SELECT[@id='" + prefix + "UserCreate_countryId']", user.getCountry())) {
				selenium.select("//SELECT[@id='" + prefix + "UserCreate_countryId']", user.getCountry());
				waitForAjax();
			} else {
				fail("The country '" + user.getCountry() + "' does not exist on the select list");
			}
		}
		if (user.getTimeZone() != null) {
			if (isOptionPresent("//SELECT[@id='tzlist']", user.getTimeZone())) {
				selenium.select("//SELECT[@id='tzlist']", user.getTimeZone());
			} else {
				fail("The time zone '" + user.getTimeZone() + "' does not exist on the select list");
			}
		}
	}

	private void setUserPermissions(List<String> permissions) {
		String permissionTableLocator1 = "css=.permissions";
		for (String permission : permissions) {
			String permissionOnButtonLocator = permissionTableLocator1 + " td:contains('" + permission + "') ~ td input[value='true']";
			selenium.check(permissionOnButtonLocator);
		}
	}

	public void clickSaveUser() {
		selenium.click("//INPUT[@name='save']");
		waitForPageToLoad();
	}
	
	public void clickSaveUserEdit() {
		selenium.click("//INPUT[@id='employeeUserUpdate_save']");
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
		selenium.click("//input[@name='allOff']");
	}
	
	public void clickPermissionsAllOn() {
		selenium.click("//input[@name='allOn']");
	}

	public List<String> getListOfUserIDsOnCurrentPage() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount("//table[@id='userList']/tbody/tr/td/a");
		int numberOfUserIDs = n.intValue();
		int row = 2; // first user id starts on row 2 of table, zero-index
		String userIDCellLocator = "//table[@id='userList']" + "." + row + ".0";
		for (int i = 0; i < numberOfUserIDs; i++, row++) {
			String permission = selenium.getTable(userIDCellLocator);
			result.add(permission.trim());
			userIDCellLocator = userIDCellLocator.replaceFirst("\\." + row, "." + (row + 1));
		}
		return result;
	}

	public String clickFirstSearchResult() {
		String locator = "xpath=//table[@id='userList']/tbody/tr[3]/td[1]/a";
		String userName = selenium.getText(locator);
		selenium.click(locator);
		waitForPageToLoad();
		return userName;
	}

	public void clickChangePasswordTab() {
		clickNavOption("Change Password");
	}

	public void clickCancelChangePassword() {
		selenium.click("//a[contains(.,'Cancel')]");
		waitForPageToLoad();
	}

	public String getUserId() {
		return selenium.getValue("//input[@name='userId']").trim();
	}

	public void clickUserID(String userID){
		selenium.click("//a[contains(text(), '" + userID + "')]");
		waitForPageToLoad();
	}
	
	public void enableManageSystemConfigPermission(){
		selenium.click("//tr[3]/td[2]/input");
	}

	public void clickEditTab() {
		clickNavOption("Edit");
	}

	public void clickChangeAccountType() {
		selenium.click("//a[text()='Change Account Type']");
		waitForPageToLoad();
	}

	public void clickChangeToReadOnlyUser() {
		selenium.click("//input[@value='Change to a Read-Only User']");
		waitForPageToLoad();
	}
	
	public void clickChangeToLiteUser() {
		selenium.click("//input[@value='Change to a Lite User']");
		waitForPageToLoad();
	}

	public void clickChangeToFullUser() {
		selenium.click("//input[@value='Change to a Full User']");
		waitForPageToLoad();
	}

	public void removeUser(String userid, boolean confirm) {
		confirmNextDialog(confirm);
		selenium.click("//a[contains(text(),'" + userid + "')]/../..//a[text()='Remove']");
		selenium.getConfirmation();
//		if(confirm) {
//			waitForPageToLoad();
//		}
	}

}
