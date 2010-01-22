package com.n4systems.fieldid.selenium.testcase;

import java.util.List;

import org.junit.*;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.Admin;
import com.n4systems.fieldid.selenium.administration.ManageUsers;
import com.n4systems.fieldid.selenium.login.Login;

public class WEB_1466 extends FieldIDTestCase {

	Login login;
	Admin admin;
	ManageUsers mus;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		admin = new Admin(selenium, misc);
		mus = new ManageUsers(selenium, misc);
	}

	@Test
	public void addEmployeePermissionTagProductsShouldBeChangedToIdentifyAssetsOnCustomerTenants() throws Exception {
		String username = getStringProperty("username1");
		String password = getStringProperty("password1");
		String companyID = getStringProperty("company1");

		try {
			setCompany(companyID);
				login.loginAcceptingEULAIfNecessary(username, password);
				gotoAddEmployee();
				verifyTagProductsHasChangedToIdentifyAssets();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void editEmployeePermissionTagProductsShouldBeChangedToIdentifyAssetsOnCustomerTenants() throws Exception {
		String username = getStringProperty("username1");
		String password = getStringProperty("password1");
		String companyID = getStringProperty("company1");

		try {
				setCompany(companyID);
				login.loginAcceptingEULAIfNecessary(username, password);
				gotoEditEmployee();
				verifyTagProductsHasChangedToIdentifyAssets();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void addEmployeePermissionTagProductsShouldBeChangedToIdentifyAssetsOnJobSiteTenants() throws Exception {
		String username = getStringProperty("username2");
		String password = getStringProperty("password2");
		String companyID = getStringProperty("company2");

		try {
				setCompany(companyID);
				login.loginAcceptingEULAIfNecessary(username, password);
				gotoAddEmployee();
				verifyTagProductsHasChangedToIdentifyAssets();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void editEmployeePermissionTagProductsShouldBeChangedToIdentifyAssetsOnJobsiteTenants() throws Exception {
		String username = getStringProperty("username2");
		String password = getStringProperty("password2");
		String companyID = getStringProperty("company2");

		try {
				setCompany(companyID);
				login.loginAcceptingEULAIfNecessary(username, password);
				gotoEditEmployee();
				verifyTagProductsHasChangedToIdentifyAssets();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void addEmployeePermissionManageEndUsersShouldBeChangedToManageCustomersOnCustomerTenants() throws Exception {
		String username = getStringProperty("username1");
		String password = getStringProperty("password1");
		String companyID = getStringProperty("company1");

		try {
				setCompany(companyID);
				login.loginAcceptingEULAIfNecessary(username, password);
				gotoAddEmployee();
				verifyManageEndUsersHasChangedToManageCustomers();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void editEmployeePermissionManageEndUsersShouldBeChangedToManageCustomersOnCustomerTenants() throws Exception {
		String username = getStringProperty("username1");
		String password = getStringProperty("password1");
		String companyID = getStringProperty("company1");

		try {
				setCompany(companyID);
				login.loginAcceptingEULAIfNecessary(username, password);
				gotoEditEmployee();
				verifyManageEndUsersHasChangedToManageCustomers();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void addEmployeePermissionManageEndUsersShouldBeChangedToManageJobSiteOnJobSitesTenants() throws Exception {
		String username = getStringProperty("username2");
		String password = getStringProperty("password2");
		String companyID = getStringProperty("company2");

		try {
				setCompany(companyID);
				login.loginAcceptingEULAIfNecessary(username, password);
				gotoAddEmployee();
				verifyManageEndUsersHasChangedToManageJobSites();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void editEmployeePermissionManageEndUsersShouldBeChangedToManageJobSiteOnJobSitesTenants() throws Exception {
		String username = getStringProperty("username2");
		String password = getStringProperty("password2");
		String companyID = getStringProperty("company2");

		try {
				setCompany(companyID);
				login.loginAcceptingEULAIfNecessary(username, password);
				gotoEditEmployee();
				verifyManageEndUsersHasChangedToManageJobSites();
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void verifyManageEndUsersHasChangedToManageJobSites() {
		List<String> permissions = mus.getListOfPermissionsFromAddEmployeeUser();
		assertTrue(permissions.contains("Manage Job Sites"));
	}

	private void verifyManageEndUsersHasChangedToManageCustomers() {
		List<String> permissions = mus.getListOfPermissionsFromAddEmployeeUser();
		assertTrue(permissions.contains("Manage Customers"));
	}

	private void gotoEditEmployee() {
		misc.gotoAdministration();
		admin.gotoManageUsers();
		mus.setUserType("Employee");
		mus.gotoFilterSearchButton();
		// pick the first employee user
		List<String> userIDs = mus.getListOfUserIDsOnCurrentPage();
		mus.gotoEditUser(userIDs.get(0));
	}

	/**
	 * Other tests:
	 * check on add employee user for customer tenant manage end user changed to manage customer
	 * check on edit employee user for customer tenant manage end user changed to manage customer
	 * check on add employee user for job site tenant manage end user changed to manage job sites
	 * check on edit employee user for job site tenant manage end user changed to manage job sites
	 */
	
	private void gotoAddEmployee() {
		misc.gotoAdministration();
		admin.gotoManageUsers();
		mus.gotoAddEmployeeUser();
	}

	private void verifyTagProductsHasChangedToIdentifyAssets() {
		List<String> permissions = mus.getListOfPermissionsFromAddEmployeeUser();
		assertTrue(permissions.contains("Identify Assets"));
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
