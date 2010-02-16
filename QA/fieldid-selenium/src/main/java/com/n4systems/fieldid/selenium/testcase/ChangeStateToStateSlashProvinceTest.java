package com.n4systems.fieldid.selenium.testcase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageCustomers;
import com.n4systems.fieldid.selenium.administration.page.ManageOrganizations;
import com.n4systems.fieldid.selenium.datatypes.Customer;
import com.n4systems.fieldid.selenium.login.page.Login;

/**
 * WEB-1482
 * 
 * @author dgrainge
 *
 */
public class ChangeStateToStateSlashProvinceTest extends FieldIDTestCase {

	Login login;
	Admin admin;
	ManageOrganizations mos;
	ManageCustomers mcs;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		admin = new Admin(selenium, misc);
		mos = new ManageOrganizations(selenium, misc);
		mcs = new ManageCustomers(selenium, misc);
	}

	@Test
	public void addCustomerShouldHaveStateSlashProvince() throws Exception {
		String companyID = getStringProperty("company");
		String password = getStringProperty("password");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			login.loginAcceptingEULAIfNecessary(username, password);
			gotoAddCustomer();
			verifyInputWithStateSlashProvince();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void addDivisionShouldHaveStateSlashProvince() throws Exception {
		String companyID = getStringProperty("company");
		String password = getStringProperty("password");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			login.loginAcceptingEULAIfNecessary(username, password);
			gotoAddDivision();
			verifyInputWithStateSlashProvince();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void addOrganizationalUnitShouldHaveStateSlashProvince() throws Exception {
		String companyID = getStringProperty("company");
		String password = getStringProperty("password");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			login.loginAcceptingEULAIfNecessary(username, password);
			gotoAddOrganization();
			verifyInputWithStateSlashProvince();
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void editPrimaryOrganizationalUnitShouldHaveStateSlashProvince() throws Exception {
		String companyID = getStringProperty("company");
		String password = getStringProperty("password");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			login.loginAcceptingEULAIfNecessary(username, password);
			gotoEditPrimaryOrganization();
			verifyInputWithStateSlashProvince();
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void gotoEditPrimaryOrganization() {
		misc.gotoAdministration();
		admin.gotoManageOrganizations();
		mos.gotoEditPrimaryOrganization();
	}

	private void gotoAddOrganization() {
		misc.gotoAdministration();
		admin.gotoManageOrganizations();
		mos.gotoAddSecondaryOrganization();
	}

	private void gotoAddDivision() {
		misc.gotoAdministration();
		admin.gotoManageCustomers();
		mcs.gotoAddCustomer();
		String customerID = misc.getRandomString(15);
		String customerName = misc.getRandomString(15);
		Customer c = new Customer(customerID, customerName);
		mcs.setAddCustomer(c);
		mcs.gotoSaveCustomer();
		mcs.gotoDivisions();
		mcs.gotoAddDivision();
	}

	private void verifyInputWithStateSlashProvince() {
		assertTrue(selenium.isElementPresent("xpath=//LABEL[contains(text(),'State/Province')]"));
	}

	private void gotoAddCustomer() {
		misc.gotoAdministration();
		admin.gotoManageCustomers();
		mcs.gotoAddCustomer();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
