package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.CustomerUser;
import com.n4systems.fieldid.selenium.datatypes.EmployeeUser;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.SystemUser;
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
		
		Set<ExtendedFeature> extendedFeatures = new HashSet<ExtendedFeature>(
				Arrays.asList(ExtendedFeature.Projects, ExtendedFeature.ReadOnlyUser));
		
		TenantLimit limits = new TenantLimit(-1L, -1L, -1L, -1L, -1L); 
		
		PrimaryOrg primaryOrg = scenario.primaryOrgFor(COMPANY);
		
		primaryOrg.setExtendedFeatures(extendedFeatures);
		primaryOrg.setLimits(limits);
		
		scenario.save(primaryOrg);
		
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
	public void test_filter_for_full_users() throws Exception {
		manageUsersPage.selectSearchUserType(ManageUsersPage.USER_TYPE_FULL);
		manageUsersPage.clickSearchButton();
		List<String> users = manageUsersPage.getListOfUserIDsOnCurrentPage();
		assertTrue(users.size() > 0);
		assertTrue(users.contains("test_user1"));
	}

	@Test
	public void test_filter_for_lite_users() throws Exception {
		manageUsersPage.selectSearchUserType(ManageUsersPage.USER_TYPE_LITE);
		manageUsersPage.clickSearchButton();
		List<String> users = manageUsersPage.getListOfUserIDsOnCurrentPage();
		assertTrue(users.size() > 0);
		assertTrue(users.contains(LITE_USER));
	}

	@Test
	public void test_filter_for_readonly_users() throws Exception {
		manageUsersPage.selectSearchUserType(ManageUsersPage.USER_TYPE_READONLY);
		manageUsersPage.clickSearchButton();
		List<String> users = manageUsersPage.getListOfUserIDsOnCurrentPage();
		assertTrue(users.size() > 0);
		assertTrue(users.contains(READ_ONLY_USER));
	}

	private EmployeeUser addAnEmployeeUser(ManageUsersPage manageUsersPage) {
		EmployeeUser employeeUser = new EmployeeUser("TestFullUser", "selenium@fieldid.com", PASSWORD, PASSWORD, new Owner(COMPANY), "Full", "User");
		employeeUser.addPermission(EmployeeUser.create);
		employeeUser.addPermission(EmployeeUser.safety);
		employeeUser.addPermission(EmployeeUser.tag);
		manageUsersPage.setFullUserFormFields(employeeUser);
		manageUsersPage.clickSaveUser("employee");
		return employeeUser;
	}
	
	private EmployeeUser addALiteUser(ManageUsersPage manageUsersPage) {
		EmployeeUser user = new EmployeeUser("TestLiteUser", "selenium@fieldid.com", PASSWORD, PASSWORD, new Owner(COMPANY), "Lite", "User");
		user.addPermission(EmployeeUser.create);
		user.addPermission(EmployeeUser.edit);
		manageUsersPage.setLiteUserFormFields(user);
		manageUsersPage.clickSaveUser("lite");
		return user;
	}
	
	private CustomerUser addAReadonlyUser(ManageUsersPage manageUsersPage) {
		CustomerUser user = new CustomerUser("TestReadOnly", "selenium@fieldid.com", PASSWORD, PASSWORD, new Owner(COMPANY), "ReadOnly", "User");
		manageUsersPage.setReadOnlyUserFormFields(user);
		manageUsersPage.clickSaveUser("readOnly");
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
