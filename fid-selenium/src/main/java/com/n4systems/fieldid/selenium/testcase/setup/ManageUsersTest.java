package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.CustomerUser;
import com.n4systems.fieldid.selenium.datatypes.EmployeeUser;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.SystemUser;
import com.n4systems.fieldid.selenium.pages.admin.AdminOrgPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantLimit;

public class ManageUsersTest extends FieldIDTestCase {

	private ManageUsersPage manageUsersPage;
	
	private static String COMPANY = "test1";
	private static String READ_ONLY_USER = "aReadOnlyUser";
	private static String LITE_USER = "aLiteUser";
	private static String PASSWORD = "password";
	
	@Override
	public void setupScenario(Scenario scenario) {
		PrimaryOrg primaryOrg = scenario.primaryOrgFor(COMPANY);
				
		primaryOrg.setExtendedFeatures(setOf(ExtendedFeature.Projects, ExtendedFeature.ReadOnlyUser));
		
		scenario.updatePrimaryOrg(primaryOrg);
		
		scenario.aReadOnlyUser()
        	 	.withUserId(READ_ONLY_USER)
        	 	.withPassword(READ_ONLY_USER)
        	 	.build();

		scenario.aLiteUser()
        		.withUserId(LITE_USER)
        		.withPassword(LITE_USER)
        		.build();	
	}
	
	@Before
	public void setUp() throws Exception {
		AdminOrgPage adminPage = startAdmin().login().clickEditOrganization(COMPANY);
		adminPage.enterTenantLimits(new TenantLimit(-1L, -1L, -1L, -1L, -1L));
		adminPage.submitOrganizationChanges();
		manageUsersPage = startAsCompany(COMPANY).systemLogin().clickSetupLink().clickManageUsers();
	}
	
	@Test
	public void should_be_able_to_add_an_employee_user() throws Exception {
		manageUsersPage.clickAddUserTab();
		manageUsersPage.clickAddFullUser();
		EmployeeUser user = addAnEmployeeUser(manageUsersPage);
		verifyUserWasAdded(manageUsersPage, user);
	}
	
	@Test
	public void should_be_able_to_add_a_readonly_user() throws Exception {
		manageUsersPage.clickAddUserTab();
		manageUsersPage.clickAddReadOnlyUser();
		CustomerUser user = addAReadonlyUser(manageUsersPage);
		verifyUserWasAdded(manageUsersPage, user);
	}
	
	@Test
	public void should_be_able_to_add_a_lite_user() throws Exception {
		manageUsersPage.clickAddUserTab();
		manageUsersPage.clickAddLiteUser();
		EmployeeUser user = addALiteUser(manageUsersPage);
		verifyUserWasAdded(manageUsersPage, user);
	}
	
	@Test
	public void filter_for_full_users_test() throws Exception {
		manageUsersPage.selectSearchUserType(ManageUsersPage.USER_TYPE_FULL);
		manageUsersPage.clickSearchButton();
		List<String> users = manageUsersPage.getListOfUserIDsOnCurrentPage();
		assertTrue(users.size() > 0);
		assertTrue(users.contains("test_user1"));
	}

	@Test
	public void filter_for_lite_users_test() throws Exception {
		manageUsersPage.selectSearchUserType(ManageUsersPage.USER_TYPE_LITE);
		manageUsersPage.clickSearchButton();
		List<String> users = manageUsersPage.getListOfUserIDsOnCurrentPage();
		assertTrue(users.size() > 0);
		assertTrue(users.contains(LITE_USER));
	}

	@Test
	public void filter_for_readonly_users_test() throws Exception {
		manageUsersPage.selectSearchUserType(ManageUsersPage.USER_TYPE_READONLY);
		manageUsersPage.clickSearchButton();
		List<String> users = manageUsersPage.getListOfUserIDsOnCurrentPage();
		assertTrue(users.size() > 0);
		assertTrue(users.contains(READ_ONLY_USER));
	}
	
	@Test
	public void remove_user_test() throws Exception {
		manageUsersPage.removeUser(READ_ONLY_USER, true);
		
		manageUsersPage.selectSearchUserType(ManageUsersPage.USER_TYPE_READONLY);
		manageUsersPage.clickSearchButton();
		List<String> users = manageUsersPage.getListOfUserIDsOnCurrentPage();
		assertTrue(users.size() == 0);
		assertTrue(!users.contains(READ_ONLY_USER));
	}
	
	@Test
	public void add_user_with_existing_userid_should_fail() throws Exception {
		manageUsersPage.clickAddUserTab();
		manageUsersPage.clickAddReadOnlyUser();
		CustomerUser user = addAReadonlyUser(manageUsersPage);
		verifyUserWasAdded(manageUsersPage, user);

		manageUsersPage.clickAddUserTab();
		manageUsersPage.clickAddReadOnlyUser();
		addAReadonlyUser(manageUsersPage);
		assertFalse(manageUsersPage.getFormErrorMessages().isEmpty());
	}
	
	@Test
	public void edit_user_with_sucessful_update() throws Exception {
		manageUsersPage.clickUserID("test_user1");
		manageUsersPage.clickEditTab();
		manageUsersPage.enterName("new name");
		manageUsersPage.clickSave();
		
		assertEquals("User Saved", manageUsersPage.getActionMessages().get(0));
	}

	@Test
	public void edit_user_fail_update_with_missing_required_field() throws Exception {
		manageUsersPage.clickUserID("test_user1");
		manageUsersPage.clickEditTab();
		manageUsersPage.enterName("");
		manageUsersPage.clickSave();
		
		assertEquals("First Name is a required field.", manageUsersPage.getFormErrorMessages().get(0));
	}

	
	private EmployeeUser addAnEmployeeUser(ManageUsersPage manageUsersPage) {
		EmployeeUser employeeUser = new EmployeeUser("TestFullUser", "selenium@fieldid.com", PASSWORD, PASSWORD, new Owner(COMPANY), "Full", "User");
		employeeUser.addPermission(EmployeeUser.create);
		employeeUser.addPermission(EmployeeUser.safety);
		employeeUser.addPermission(EmployeeUser.tag);
		manageUsersPage.setFullUserFormFields(employeeUser);
		manageUsersPage.clickSave();
		return employeeUser;
	}
	
	private EmployeeUser addALiteUser(ManageUsersPage manageUsersPage) {
		EmployeeUser user = new EmployeeUser("TestLiteUser", "selenium@fieldid.com", PASSWORD, PASSWORD, new Owner(COMPANY), "Lite", "User");
		user.addPermission(EmployeeUser.create);
		user.addPermission(EmployeeUser.edit);
		manageUsersPage.setLiteUserFormFields(user);
		manageUsersPage.clickSave();
		return user;
	}
	
	private CustomerUser addAReadonlyUser(ManageUsersPage manageUsersPage) {
		CustomerUser user = new CustomerUser("TestReadOnly", "selenium@fieldid.com", PASSWORD, PASSWORD, new Owner(COMPANY), "ReadOnly", "User");
		manageUsersPage.setReadOnlyUserFormFields(user);
		manageUsersPage.clickSave();
		return user;
	}	

	private void verifyUserWasAdded(ManageUsersPage page, SystemUser user) {
		assertTrue(user != null);
		
		List<String> errors = page.getFormErrorMessages();
		assertTrue("There were errors on the page: " + errors, errors.size() == 0);
		
		assertTrue(page.getActionMessages().contains("User Saved"));
		
		page.clickViewAllTab();
		page.selectSearchUserType(ManageUsersPage.USER_TYPE_ALL);
		page.enterSearchNameFilter(user.getUserid());
		page.clickSearchButton();
		List<String> users = page.getListOfUserIDsOnCurrentPage();
		assertTrue("Did not find the newly created user in the list of users: " + user, users.contains(user.getUserid()));
	}

}
