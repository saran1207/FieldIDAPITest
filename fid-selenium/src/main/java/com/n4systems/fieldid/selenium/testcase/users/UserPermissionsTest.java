package com.n4systems.fieldid.selenium.testcase.users;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.reporting.page.ReportingSearchResultsPage;

public class UserPermissionsTest extends FieldIDTestCase {

	private HomePage homePage;
	private static String COMPANY = "illinois";
	private LoginPage loginPage;
	private ManageUsersPage manageUsersPage;

	@Before
	public void setUp() throws Exception {
		loginPage = startAsCompany(COMPANY);
		homePage = loginPage.systemLogin();

		manageUsersPage = homePage.clickSetupLink().clickManageUsers();
		manageUsersPage.clickUserID("anEmployeeUser");

		manageUsersPage.clickPermissionsAllOff();
		manageUsersPage.clickSaveUserEdit();
	}

	public void verify_customer_user_permissions_are_disabled() {

	}

	public void verify_identify_is_disabled() {
		HomePage homePage = startAsCompany(COMPANY).login("anEmployeeUser", "anEmployeeUser");
		assertFalse("Shouldn't have identify permission", selenium.isElementPresent("//a[@id='menuIdentify']"));
	}

	public void verify_manage_system_config_is_disabled() {
		HomePage homePage = startAsCompany(COMPANY).login("anEmployeeUser", "anEmployeeUser");
		assertFalse("Shouldn't have manage system settings permission", selenium.isElementPresent("//a[@id='menuSetup']"));
	}

	public void verify_manage_system_users_and_manage_customer_users_is_disabled() {

		manageUsersPage.enableManageSystemConfigPermission();
		manageUsersPage.clickSaveUserEdit();

		HomePage homePage = startAsCompany(COMPANY).login("anEmployeeUser", "anEmployeeUser");
		homePage.clickSetupLink();

		assertFalse("Shouldn't have manage employee users permission", selenium.isElementPresent("//a[contains(., 'Manage Users')]"));
		assertFalse("Shouldn't have manage customer users permission", selenium.isElementPresent("//a[contains(., 'Manage Customers')]"));
	}


	public void verify_edit_inspections_is_disabled() {
		HomePage homePage = startAsCompany(COMPANY).login("anEmployeeUser", "anEmployeeUser");
		ReportingSearchResultsPage reportingSearchResultsPage= homePage.clickReportingLink().clickRunSearchButton();
		
		assertFalse("Shouldn't edit link next to view", selenium.isElementPresent("//a[contains(., 'Edit')]"));
		
		reportingSearchResultsPage.clickReportLinkForResult(1);
	}

	@Test
	public void verify_employee_user_permissions_are_disabled() {

	}

}
