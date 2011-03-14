package com.n4systems.fieldid.selenium.testcase.users;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.ManageEventsPage;
import com.n4systems.fieldid.selenium.pages.reporting.ReportingSearchResultsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fieldid.selenium.persistence.builder.SimpleEventBuilder;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserPermissionsTest extends FieldIDTestCase {

	private static String COMPANY = "test1";
	private static String READ_ONLY_USER = "aReadOnlyUser";
	private static String EMPLOYEE_USER = "anEmployeeUser1";
	private static String EMPLOYEE_USER_WITH_PERMISSIONS = "anEmployeeUser2";

	@Override
	public void setupScenario(Scenario scenario) {
		PrimaryOrg defaultPrimaryOrg = scenario.primaryOrgFor(COMPANY);
		
		defaultPrimaryOrg.setExtendedFeatures(setOf(ExtendedFeature.Projects, ExtendedFeature.ReadOnlyUser));
		
		scenario.save(defaultPrimaryOrg);
		
		scenario.aReadOnlyUser()
		        .withUserId(READ_ONLY_USER)
		        .withPassword(READ_ONLY_USER)
		        .build();
		
		scenario.aUser()
		        .withUserId(EMPLOYEE_USER)
		        .withPassword(EMPLOYEE_USER)
		        .build();
		
		scenario.aUser()
                .withUserId(EMPLOYEE_USER_WITH_PERMISSIONS)
                .withPassword(EMPLOYEE_USER_WITH_PERMISSIONS)
                .withPermissions(511)
                .build();		
		
		SimpleEventBuilder.aSimpleEvent(scenario).createObject();
	}
	
	
	@Test
	public void verify_customer_menu_controls_are_disabled() {
		startAsCompany(COMPANY).login(READ_ONLY_USER, READ_ONLY_USER);

		assertFalse("Shouldn't have identify permission", selenium.isElementPresent("//a[@id='menuIdentify']"));
		assertFalse("Shouldn't have event permission", selenium.isElementPresent("//a[@id='menuEvent']"));
		assertFalse("Shouldn't have web store permission", selenium.isElementPresent("//a[contains(.,'Field ID Store')]"));
		assertFalse("Shouldn't have manage system config permission", selenium.isElementPresent("//a[@id='menuSetup']"));
		assertFalse("Shouldn't have safety network permission", selenium.isElementPresent("//a[@id='menuSafetyNetwork']"));
	}

	@Test
	public void verify_employee_menu_controls_are_disabled() {
		startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER);

		assertFalse("Shouldn't have identify permission", selenium.isElementPresent("//a[@id='menuIdentify']"));
		assertFalse("Shouldn't have event permission", selenium.isElementPresent("//a[@id='menuEvent']"));
		assertFalse("Shouldn't have web store permission", selenium.isElementPresent("//a[contains(.,'Field ID Store')]"));
		assertFalse("Shouldn't have manage system config permission", selenium.isElementPresent("//a[@id='menuSetup']"));
		assertFalse("Shouldn't have safety network permission", selenium.isElementPresent("//a[@id='menuSafetyNetwork']"));
	}

	@Test
	public void verify_employee_user_job_actions_are_disabled() {
		startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER).clickJobsLink().clickJobsLink();
		assertFalse("Shouldn't have job addition permission", selenium.isElementPresent("//a[contains(.,'Add')]"));
	}

	@Test
	public void verify_customer_user_job_actions_are_disabled() {
		startAsCompany(COMPANY).login(READ_ONLY_USER, READ_ONLY_USER).clickJobsLink().clickJobsLink();
		assertFalse("Shouldn't have job addition permission", selenium.isElementPresent("//a[contains(.,'Add')]"));
	}

	@Test
	public void verify_manage_system_users_and_manage_customer_users_is_disabled() {

		startAsCompany(COMPANY).login(EMPLOYEE_USER_WITH_PERMISSIONS, EMPLOYEE_USER_WITH_PERMISSIONS).clickSetupLink();

		assertFalse("Shouldn't have manage employee users permission", selenium.isElementPresent("//a[contains(., 'Manage Users')]"));
		assertFalse("Shouldn't have manage customer users permission", selenium.isElementPresent("//a[contains(., 'Manage Customers')]"));

	}

	@Test
	public void verify_edit_events_is_disabled() {
		ReportingSearchResultsPage resultsPage = startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER).clickReportingLink().clickRunSearchButton();

		assertFalse("Shouldn't be able to see edit link next to view", selenium.isElementPresent("//a[contains(., 'Edit')]"));

		ManageEventsPage manageEventsPage = resultsPage.clickReportLinkForResult(1).clickEventsTab().clickViewEventsByDateGroup();

		assertFalse("Shouldn't be able to see edit link", selenium.isElementPresent("//a[contains(., 'Edit')]"));

		manageEventsPage.clickFirstEventLink();

		assertFalse("Shouldn't be able to see edit tab", selenium.isElementPresent("//a[contains(., 'Edit')]"));

	}
	
	@Test
	public void verify_customers_can_only_edit_four_unrestricted_fields_on_asset(){
		startAsCompany(COMPANY).login(READ_ONLY_USER, READ_ONLY_USER).clickAssetsLink().clickRunSearchButton().clickAssetLinkForResult(1).clickEditTab();
		
		assertEquals(7, selenium.getXpathCount("//div[@class='infoSet']/label"));
		assertTrue("Coudldn't find owner field", selenium.isElementPresent("//label[contains(.,'Owner')]"));
		assertTrue("Coudldn't find Location field", selenium.isElementPresent("//label[contains(.,'Location')]"));
		assertTrue("Coudldn't find Reference Number field", selenium.isElementPresent("//label[contains(.,'Reference Number')]"));
		assertTrue("Coudldn't find Purchase Order field", selenium.isElementPresent("//label[contains(.,'Purchase Order')]"));
	}
	
	@Test
	public void verify_employee_users_with_no_edit_permissions_cannot_view_asset_edit_tab(){
		startAsCompany(COMPANY).login(EMPLOYEE_USER, EMPLOYEE_USER).clickAssetsLink().clickRunSearchButton().clickAssetLinkForResult(1);
	
		assertFalse("Shouldn't be able to see edit tab on asset page", selenium.isElementPresent("//a[contains(., 'Edit')]"));
	}

}
