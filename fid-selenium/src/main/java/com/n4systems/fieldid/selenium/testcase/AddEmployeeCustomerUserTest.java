package com.n4systems.fieldid.selenium.testcase;

import static com.n4systems.fieldid.selenium.datatypes.Owner.someOrg;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.ManageUsers;
import com.n4systems.fieldid.selenium.datatypes.CustomerUser;
import com.n4systems.fieldid.selenium.datatypes.EmployeeUser;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.SystemUser;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;

public class AddEmployeeCustomerUserTest extends FieldIDTestCase {

	private HomePage homePage;
	
	@Before
	public void setUp() throws Exception {
		String company = getStringProperty("company");
		LoginPage loginPage = startAsCompany(company);
		homePage = loginPage.systemLogin();
	}
	
	@Test
	public void should_be_able_to_add_an_employee_user() throws Exception {
		ManageUsersPage manageUsersPage = homePage.clickSetupLink().clickManageUsers();
		manageUsersPage.clickAddEmployeeUserTab();
		EmployeeUser eu = addAnEmployeeUser(manageUsersPage);
		verifyUserWasAdded(manageUsersPage, eu);
	}
	
	@Test
	public void should_be_able_to_add_a_customer_user() throws Exception {
		ManageUsersPage manageUsersPage = homePage.clickSetupLink().clickManageUsers();
		manageUsersPage.clickAddCustomerUserTab();
		CustomerUser cu = addACustomerUser(manageUsersPage);
		verifyUserWasAdded(manageUsersPage, cu);
	}
	
	private CustomerUser addACustomerUser(ManageUsersPage manageUsersPage) {
		String email = "selenium@fieldid.com";
		String password = getStringProperty("customer-password");
		Owner owner = someOrg();
		String firstName = MiscDriver.getRandomString(10);
		String lastName = MiscDriver.getRandomString(10);
		String userid = firstName.toLowerCase();
		CustomerUser cu = new CustomerUser(userid, email, password, password, owner, firstName, lastName);
		manageUsersPage.setCustomerFormFields(cu);
		manageUsersPage.clickSaveCustomerUser();
		return cu;
	}

	private void verifyUserWasAdded(ManageUsersPage manageUsersPage, SystemUser user) {
		assertTrue(user != null);
		List<String> success = misc.getActionMessages();
		List<String> errors = misc.getFormErrorMessages();
		assertTrue("There were errors on the page: " + misc.convertListToString(errors), errors.size() == 0);
		String successMessage = "User Saved";
		assertTrue("Did not get the expected '" + successMessage + "'", success.contains(successMessage));
		manageUsersPage.clickViewAllTab();
		manageUsersPage.selectSearchUserType(ManageUsers.USER_TYPE_ALL);
		manageUsersPage.enterSearchNameFilter(user.getUserid());
		manageUsersPage.clickSearchButton();
		List<String> users = manageUsersPage.getListOfUserIDsOnCurrentPage();
		assertTrue("Did not find the newly created user in the list of users: " + user, users.contains(user.getUserid()));
	}

	private EmployeeUser addAnEmployeeUser(ManageUsersPage manageUsersPage) {
		String email = "selenium@fieldid.com";
		String password = getStringProperty("employee-password");
		Owner owner = someOrg();
		String firstName = MiscDriver.getRandomString(10);
		String lastName = MiscDriver.getRandomString(10);
		String userid = firstName.toLowerCase();
		EmployeeUser employeeUser = new EmployeeUser(userid, email, password, password, owner, firstName, lastName);
		employeeUser.addPermission(EmployeeUser.create);
		employeeUser.addPermission(EmployeeUser.safety);
		employeeUser.addPermission(EmployeeUser.tag);
		manageUsersPage.setAddEmployeeUser(employeeUser);
		manageUsersPage.clickSaveEmployeeUser();
		return employeeUser;
	}

}
