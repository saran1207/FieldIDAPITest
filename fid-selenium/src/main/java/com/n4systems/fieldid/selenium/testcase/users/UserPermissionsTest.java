package com.n4systems.fieldid.selenium.testcase.users;

import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.pages.ManageEventsPage;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.JobsListPage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
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
	public void verify_employee_user_job_actions_are_disabled() {
		JobsListPage jobsListPage = startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER).clickJobsLink().clickJobsLink();
		assertFalse("Shouldn't have job addition permission", selenium.isElementPresent("//a[contains(.,'Add')]"));
	}

	@Test
	public void verify_customer_user_job_actions_are_disabled() {
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

	@Test
	public void verify_edit_inspections_is_disabled() {
		HomePage homePage = startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER);
		ReportingSearchResultsPage reportingSearchResultsPage = homePage.clickReportingLink().clickRunSearchButton();

		assertFalse("Shouldn't be able to see edit link next to view", selenium.isElementPresent("//a[contains(., 'Edit')]"));

		ManageEventsPage manageEventsPage = reportingSearchResultsPage.clickReportLinkForResult(1).clickEventsTab().clickManageEvents();

		assertFalse("Shouldn't be able to see edit link", selenium.isElementPresent("//a[contains(., 'Edit')]"));

		manageEventsPage.clickFirstEventLink();

		assertFalse("Shouldn't be able to see edit tab", selenium.isElementPresent("//a[contains(., 'Edit')]"));

	}
	
	@Test
	public void verify_customers_can_only_edit_four_unrestricted_fields_on_asset(){
		AssetPage assetPage = startAsCompany(COMPANY).login(CUSTOMER_USER, CUSTOMER_USER).clickAssetsLink().clickRunSearchButton().clickAssetLinkForResult(1).clickEditTab();
		
		//Check for 4 fields + 3 fields inside orgpicker.
		assertEquals(7, selenium.getXpathCount("//div[@class='infoSet']/label"));
		assertTrue("Coudldn't find owner field", selenium.isElementPresent("//label[contains(.,'Owner')]"));
		assertTrue("Coudldn't find Location field", selenium.isElementPresent("//label[contains(.,'Location')]"));
		assertTrue("Coudldn't find Reference Number field", selenium.isElementPresent("//label[contains(.,'Reference Number')]"));
		assertTrue("Coudldn't find Purchase Order field", selenium.isElementPresent("//label[contains(.,'Purchase Order')]"));
	}
	
	@Test
	public void verify_employee_users_with_no_edit_permissions_cannot_view_asset_edit_tab(){
		AssetPage assetPage = startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER).clickAssetsLink().clickRunSearchButton().clickAssetLinkForResult(1);
	
		assertFalse("Shouldn't be able to see edit tab on asset page", selenium.isElementPresent("//a[contains(., 'Edit')]"));
	}

}
