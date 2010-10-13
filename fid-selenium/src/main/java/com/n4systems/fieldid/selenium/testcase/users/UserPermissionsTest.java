package com.n4systems.fieldid.selenium.testcase.users;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.JobsListPage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.ManageInspectionsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.reporting.page.ReportingSearchResultsPage;

public class UserPermissionsTest extends FieldIDTestCase {

	private HomePage homePage;
	private static String COMPANY = "illinois";
	private static String CUSTOMER_USER = "aCustomerUser";
	private static String EMPLOYEE_USER = "anEmployeeUser";
	private LoginPage loginPage;
	private ManageUsersPage manageUsersPage;

	@Before
	public void setUp() throws Exception {
		loginPage = startAsCompany(COMPANY);
		homePage = loginPage.systemLogin();

		manageUsersPage = homePage.clickSetupLink().clickManageUsers();
		manageUsersPage.clickUserID(EMPLOYEE_USER);

		manageUsersPage.clickPermissionsAllOff();
		manageUsersPage.clickSaveUserEdit();
	}

	@Test
	public void verify_customer_menu_controls_are_disabled() {
		HomePage homePage = startAsCompany(COMPANY).login(CUSTOMER_USER, CUSTOMER_USER);

		assertFalse("Shouldn't have identify permission", selenium.isElementPresent("//a[@id='menuIdentify']"));
		assertFalse("Shouldn't have inspect permission", selenium.isElementPresent("//a[@id='menuInspect']"));
		assertFalse("Shouldn't have web store permission", selenium.isElementPresent("//a[contains(.,'Field ID Store')]"));
		assertFalse("Shouldn't have manage system config permission", selenium.isElementPresent("//a[@id='menuSetup']"));
		assertFalse("Shouldn't have safety network permission", selenium.isElementPresent("//a[@id='menuSafetyNetwork']"));
	}

	@Test
	public void verify_employee_menu_controls_are_disabled() {
		HomePage homePage = startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER);

		assertFalse("Shouldn't have identify permission", selenium.isElementPresent("//a[@id='menuIdentify']"));
		assertFalse("Shouldn't have inspect permission", selenium.isElementPresent("//a[@id='menuInspect']"));
		assertFalse("Shouldn't have web store permission", selenium.isElementPresent("//a[contains(.,'Field ID Store')]"));
		assertFalse("Shouldn't have manage system config permission", selenium.isElementPresent("//a[@id='menuSetup']"));
		assertFalse("Shouldn't have safety network permission", selenium.isElementPresent("//a[@id='menuSafetyNetwork']"));
	}

	@Test
	public void verify_job_actions_are_disabled_for_employee_user() {
		JobsListPage jobsListPage = startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER).clickJobsLink().clickJobsLink();
		assertFalse("Shouldn't have job addition permission", selenium.isElementPresent("//a[contains(.,'Add')]"));
	}

	@Test
	public void verify_job_actions_are_disabled_for_customer_user() {
		JobsListPage jobsListPage = startAsCompany(COMPANY).login(CUSTOMER_USER, CUSTOMER_USER).clickJobsLink().clickJobsLink();
		assertFalse("Shouldn't have job addition permission", selenium.isElementPresent("//a[contains(.,'Add')]"));
	}

	@Test
	public void verify_manage_system_users_and_manage_customer_users_is_disabled() {

		manageUsersPage.enableManageSystemConfigPermission();
		manageUsersPage.clickSaveUserEdit();

		HomePage homePage = startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER);
		homePage.clickSetupLink();

		assertFalse("Shouldn't have manage employee users permission", selenium.isElementPresent("//a[contains(., 'Manage Users')]"));
		assertFalse("Shouldn't have manage customer users permission", selenium.isElementPresent("//a[contains(., 'Manage Customers')]"));

	}

	public void verify_edit_inspections_is_disabled() {
		HomePage homePage = startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER);
		ReportingSearchResultsPage reportingSearchResultsPage = homePage.clickReportingLink().clickRunSearchButton();

		assertFalse("Shouldn't be able to see edit link next to view", selenium.isElementPresent("//a[contains(., 'Edit')]"));

		ManageInspectionsPage manageInspectionsPage = reportingSearchResultsPage.clickReportLinkForResult(1).clickInspectionsTab().clickManageInspections();

		assertFalse("Shouldn't be able to see edit link", selenium.isElementPresent("//a[contains(., 'Edit')]"));

		manageInspectionsPage.clickFirstInspectionLink();

		assertFalse("Shouldn't be able to see edit tab", selenium.isElementPresent("//a[contains(., 'Edit')]"));

	}

}
