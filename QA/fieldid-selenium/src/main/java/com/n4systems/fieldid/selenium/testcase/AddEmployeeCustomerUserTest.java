package com.n4systems.fieldid.selenium.testcase;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageUsers;
import com.n4systems.fieldid.selenium.datatypes.CustomerUser;
import com.n4systems.fieldid.selenium.datatypes.EmployeeUser;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.SystemUser;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class AddEmployeeCustomerUserTest extends FieldIDTestCase {

	private Login login;
	private Admin admin;
	private ManageUsers mus;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		admin = new Admin(selenium, misc);
		mus = new ManageUsers(selenium, misc);
		String company = getStringProperty("company");
		setCompany(company);
		login.signInWithSystemAccount();
	}
	
	@Test
	public void should_be_able_to_add_an_employee_user() throws Exception {
		gotoAddAnEmployeeUser();
		EmployeeUser eu = addAnEmployeeUser();
		assertUserWasAdded(eu);
	}
	
	@Test
	public void should_be_able_to_add_a_customer_user() throws Exception {
		gotoAddACustomerUser();
		CustomerUser cu = addACustomerUser();
		assertUserWasAdded(cu);
	}
	
	private CustomerUser addACustomerUser() {
		String email = "selenium@fieldid.com";
		String password = getStringProperty("customer-password");
		Owner owner = new Owner();
		String firstName = MiscDriver.getRandomString(10);
		String lastName = MiscDriver.getRandomString(10);
		String userid = firstName.toLowerCase();
		CustomerUser cu = new CustomerUser(userid, email, password, password, owner, firstName, lastName);
		mus.setAddCustomerUser(cu);
		mus.gotoSaveCustomerUser();
		return cu;
	}

	private void gotoAddACustomerUser() {
		gotoManageUsers();
		mus.gotoAddCustomerUser();
	}

	private void gotoManageUsers() {
		misc.gotoSetup();
		admin.gotoManageUsers();
	}

	private void assertUserWasAdded(SystemUser user) {
		assertTrue(user != null);
		List<String> success = misc.getActionMessages();
		List<String> errors = misc.getFormErrorMessages();
		assertTrue("There were errors on the page: " + misc.convertListToString(errors), errors.size() == 0);
		String successMessage = "User Saved";
		assertTrue("Did not get the expected '" + successMessage + "'", success.contains(successMessage));
		mus.gotoViewAll();
		mus.setUserType(ManageUsers.UserTypeAll);
		mus.setNameFilter(user.getUserid());
		mus.gotoSearchFilter();
		List<String> users = mus.getListOfUserIDsOnCurrentPage();
		assertTrue("Did not find the newly created user in the list of users: " + user, users.contains(user.getUserid()));
	}

	private EmployeeUser addAnEmployeeUser() {
		String email = "selenium@fieldid.com";
		String password = getStringProperty("employee-password");
		Owner owner = new Owner();
		String firstName = MiscDriver.getRandomString(10);
		String lastName = MiscDriver.getRandomString(10);
		String userid = firstName.toLowerCase();
		EmployeeUser employeeUser = new EmployeeUser(userid, email, password, password, owner, firstName, lastName);
		employeeUser.addPermission(EmployeeUser.create);
		employeeUser.addPermission(EmployeeUser.safety);
		employeeUser.addPermission(EmployeeUser.tag);
		mus.setAddEmployeeUser(employeeUser);
		mus.gotoSaveEmployeeUser();
		return employeeUser;
	}

	private void gotoAddAnEmployeeUser() {
		gotoManageUsers();
		mus.gotoAddEmployeeUser();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
