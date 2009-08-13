package com.n4systems.fieldid.testcase;

import com.n4systems.fieldid.datatypes.EmployeeUser;

public class Validate extends FieldIDTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testLogin() throws Exception {
		String company = prop.getProperty("company");
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

		try {
			login.validate(company, userid, password);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testAdministration() throws Exception {
		String company = prop.getProperty("company");
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
		String company = prop.getProperty("company");
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
		String company = prop.getProperty("company");	// tenant with more than 20 customers
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
		String company = prop.getProperty("company");
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
		String company = prop.getProperty("company");
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
		String company = prop.getProperty("company");	// someone with more than 20 product types but not much more
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
		String company = prop.getProperty("company");	// more than 20 inspection types but not many more
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
		String company = prop.getProperty("company");	// more than 20 inspection types but not many more
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
		String company = prop.getProperty("company");	// Needs to be a manufacturer tenant
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");
		String linkedCompany = prop.getProperty("linkedcompany");
		String linkedUserid = prop.getProperty("linkeduserid");
		String linkedPassword = prop.getProperty("linkedpassword");

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
		String company = prop.getProperty("company");	// tenant with jobs
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");
		boolean jobs = Boolean.parseBoolean(prop.getProperty("jobs"));

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
		String company = prop.getProperty("company");	// tenant with integration and known order number
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");
		String orderNumber = prop.getProperty("ordernumber");

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
		String company = prop.getProperty("company");	// tenant with 10000+ assets
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");
		String serialtext = prop.getProperty("serialtext");
		String customer = prop.getProperty("customer");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			assets.validate(serialtext, customer);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testReporting() throws Exception {
		String company = prop.getProperty("company");	// tenant with 10000+ inspections
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
		String n4systems = prop.getProperty("n4systems");
		String n4password = prop.getProperty("n4password");
		String company = prop.getProperty("company");
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");
		String email = prop.getProperty("email");
		String firstName = prop.getProperty("firstname");
		String lastName = prop.getProperty("lastname");

		try {
			login.setCompany(company);
			login.setUserName(n4systems);
			login.setPassword(n4password);
			login.login();
			admin.gotoAdministration();
			mus.gotoManageUsers();
			mus.setListUsersNameFilter(userid);
			mus.gotoListUsersSearch();
			EmployeeUser u = new EmployeeUser(userid, email, firstName, lastName, password);
			if(!mus.isUser(userid)) {
				mus.gotoAddEmployeeUser();
				u.addPermission(EmployeeUser.tag);
				u.addPermission(EmployeeUser.sysconfig);
				u.addPermission(EmployeeUser.sysusers);
				u.addPermission(EmployeeUser.endusers);
				u.addPermission(EmployeeUser.create);
				u.addPermission(EmployeeUser.edit);
				u.addPermission(EmployeeUser.jobs);
				mus.addEmployeeUser(u);
			}
			misc.logout();

			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			jobs.validate(u);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testMyAccount() throws Exception {
		String company = prop.getProperty("company");
		String userid = prop.getProperty("userid");
		String password = prop.getProperty("password");

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
