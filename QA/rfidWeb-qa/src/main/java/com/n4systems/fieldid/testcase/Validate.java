package com.n4systems.fieldid.testcase;

import java.util.List;

import com.n4systems.fieldid.datatypes.EmployeeUser;
import com.n4systems.fieldid.datatypes.Owner;

public class Validate extends FieldIDTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testLogin() throws Exception {
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

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
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

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
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

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
		String company = prop.getProperty("company", "NOT SET");	// tenant with more than 20 customers
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mcs.validate(false);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testAdministrationManageUsers() throws Exception {
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mcs.gotoManageCustomers(false);
			List<String> customers = mcs.getCustomerNamesFromCurrentPage();
			assertTrue("There should be at least one customer to test Manage Users", customers.size() > 0);
			int index = misc.getRandomInteger(customers.size());
			admin.gotoAdministration();
			mus.validate(customers.get(index));
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
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

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
		String company = prop.getProperty("company", "NOT SET");	// someone with more than 20 product types but not much more
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

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
		String company = prop.getProperty("company", "NOT SET");	// more than 20 inspection types but not many more
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

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
		String company = prop.getProperty("company", "NOT SET");	// more than 20 inspection types but not many more
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

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
		String company = prop.getProperty("company", "NOT SET");	// Needs to be a manufacturer tenant
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		String linkedCompany = prop.getProperty("linkedcompany", "NOT SET");
		String linkedUserid = prop.getProperty("linkeduserid", "NOT SET");
		String linkedPassword = prop.getProperty("linkedpassword", "NOT SET");

		try {
			// Log into a company and get its Field ID Access Code
			login.setCompany(linkedCompany);
			login.setUserName(linkedUserid);
			login.setPassword(linkedPassword);
			login.login();
			admin.gotoAdministration();
			mysn.gotoManageYourSafetyNetwork();
//			String FIDAC = mysn.getFIDAC();
			misc.logout();

			// Test publishing your catalog
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
//			mysn.validateManufacturer(FIDAC);
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
		String company = prop.getProperty("company", "NOT SET");	// tenant with jobs
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		boolean jobs = Boolean.parseBoolean(prop.getProperty("jobs", "NOT SET"));
		boolean newFeaturesPDF = Boolean.parseBoolean(prop.getProperty("newfeaturespdf", "NOT SET"));

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			home.validate(jobs, newFeaturesPDF);	// second parameter is true of new features PDF ready.
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testIdentify() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// tenant with integration and known order number
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		String orderNumber = prop.getProperty("ordernumber", "NOT SET");

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
		String company = prop.getProperty("company", "NOT SET");	// tenant with 10000+ assets
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		String serialtext = prop.getProperty("serialtext", "NOT SET");
		String orgUnit = prop.getProperty("orgunit", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Owner owner = new Owner(orgUnit);
			assets.validate(serialtext, owner);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void testReporting() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// tenant with 10000+ inspections
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

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
		String n4systems = prop.getProperty("n4systems", "NOT SET");
		String n4password = prop.getProperty("n4password", "NOT SET");
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		String email = prop.getProperty("email", "NOT SET");
		String firstName = prop.getProperty("firstname", "NOT SET");
		String lastName = prop.getProperty("lastname", "NOT SET");

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
				u.addPermission(EmployeeUser.safety);
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
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

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
