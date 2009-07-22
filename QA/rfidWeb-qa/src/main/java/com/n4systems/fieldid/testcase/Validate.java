package com.n4systems.fieldid.testcase;

import com.n4systems.fieldid.datatypes.EmployeeUser;

public class Validate extends FieldIDTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testAdministration() throws Exception {
		String company = p.getProperty("company");
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testAdministrationManageOrganizations() throws Exception {
		String company = p.getProperty("company");
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mos.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testAdministrationManageCustomers() throws Exception {
		String company = p.getProperty("company");	// tenant with more than 20 customers
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mcs.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testAdministrationManageUsers() throws Exception {
		String company = p.getProperty("company");
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mus.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	// Manage User Registrations
	
	public void testAdministrationManageSystemSettings() throws Exception {
		String company = p.getProperty("company");
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mss.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testAdministrationManageProductTypes() throws Exception {
		String company = p.getProperty("company");	// someone with more than 20 product types but not much more
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mpts.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	// Manage Product Type Groups
	
	// Manage Product Code Mappings
	
	// Manage Product Statuses
		
	public void testAdministrationManageInspectionTypes() throws Exception {
		String company = p.getProperty("company");	// more than 20 inspection types but not many more
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mits.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testAdministrationManageEventTypeGroups() throws Exception {
		String company = p.getProperty("company");	// more than 20 inspection types but not many more
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			metgs.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	// Manage Inspection Books
	
	// Auto Attribute Wizard
	
	// Manage Comment Templates
	
	// Data Log
	
	public void testAdministrationManageYourSafetyNetwork() throws Exception {
		String company = p.getProperty("company");	// Needs to be a manufacturer tenant
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");
		String linkedCompany = p.getProperty("linkedcompany");
		String linkedUserid = p.getProperty("linkeduserid");
		String linkedPassword = p.getProperty("linkedpassword");

		try {
			// Log into a company and get its Field ID Access Code
			login.setCompany(linkedCompany);
			login.setUserName(linkedUserid);
			login.setPassword(linkedPassword);
			login.login();
			admin.gotoAdministration();
			mysn.gotoManageYourSafetyNetwork();
			String FIDAC = mysn.getFIDAC();
			misc.logout();

			// Test publishing your catalog
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mysn.validateManufacturer(FIDAC);
			String companyName = admin.getCompanyName();
			misc.logout();

			// Test importing a manufacturer's catalog
			login.setCompany(linkedCompany);
			login.setUserName(linkedUserid);
			login.setPassword(linkedPassword);
			login.login();
			admin.gotoAdministration();
			mysn.validateTenant(companyName);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testHome() throws Exception {
		String company = p.getProperty("company");	// tenant with jobs
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");
		boolean jobs = Boolean.parseBoolean(p.getProperty("jobs"));

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			home.validate(jobs);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testIdentify() throws Exception {
		String company = p.getProperty("company");	// tenant with integration and known order number
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");
		String orderNumber = p.getProperty("ordernumber");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			identify.validate(orderNumber);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	// Inspect
	
	public void testAssets() throws Exception {
		String company = p.getProperty("company");	// tenant with 10000+ assets
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			assets.validate("Reel/ID");
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testReporting() throws Exception {
		String company = p.getProperty("company");	// tenant with 10000+ inspections
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			reporting.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	// need a validate for Schedule

	public void testJobs() throws Exception {
		String n4systems = p.getProperty("n4systems");
		String n4password = p.getProperty("n4password");
		String company = p.getProperty("company");
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");
		String email = p.getProperty("email");
		String firstName = p.getProperty("firstname");
		String lastName = p.getProperty("lastname");

		try {
			login.setCompany(company);
			login.setUserName(n4systems);
			login.setPassword(n4password);
			admin.gotoAdministration();
			mus.gotoManageUsers();
			mus.setListUsersNameFilter(userid);
			mus.gotoListUsersSearch();
			if(mus.isUser(userid)) {
				mus.gotoAddEmployeeUser();
				EmployeeUser u = new EmployeeUser(userid, email, firstName, lastName, password);
				u.addPermission(EmployeeUser.tag);
				u.addPermission(EmployeeUser.sysconfig);
				u.addPermission(EmployeeUser.sysusers);
				u.addPermission(EmployeeUser.endusers);
				u.addPermission(EmployeeUser.create);
				u.addPermission(EmployeeUser.edit);
				u.addPermission(EmployeeUser.jobs);
// TODO:				mus.addEmployeeUser(u);
			}
			misc.logout();

			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			jobs.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testMyAccount() throws Exception {
		String company = p.getProperty("company");
		String userid = p.getProperty("userid");
		String password = p.getProperty("password");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			myAccount.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
