package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;

import com.n4systems.fieldid.Admin;
import com.n4systems.fieldid.Assets;
import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.Home;
import com.n4systems.fieldid.Identify;
import com.n4systems.fieldid.Login;
import com.n4systems.fieldid.MyAccount;
import com.n4systems.fieldid.Reporting;
import com.n4systems.fieldid.admin.ManageCustomers;
import com.n4systems.fieldid.admin.ManageEventTypeGroups;
import com.n4systems.fieldid.admin.ManageInspectionTypes;
import com.n4systems.fieldid.admin.ManageOrganizations;
import com.n4systems.fieldid.admin.ManageProductTypes;
import com.n4systems.fieldid.admin.ManageSystemSettings;
import com.n4systems.fieldid.admin.ManageUsers;
import com.n4systems.fieldid.admin.ManageYourSafetyNetwork;

import junit.framework.TestCase;

public class Validate extends TestCase {
	IE ie = new IE();
	FieldIDMisc misc = new FieldIDMisc(ie);
	Login login = new Login(ie);
	Home home = new Home(ie);
	Identify identify = new Identify(ie);
	Assets assets = new Assets(ie);
	Reporting reporting = new Reporting(ie);
	MyAccount myAccount = new MyAccount(ie);
	Admin admin = new Admin(ie);
	ManageOrganizations mos = new ManageOrganizations(ie);
	ManageCustomers mcs = new ManageCustomers(ie);
	ManageUsers mus = new ManageUsers(ie);
	ManageSystemSettings mss = new ManageSystemSettings(ie);
	ManageProductTypes mpts = new ManageProductTypes(ie);
	ManageInspectionTypes mits = new ManageInspectionTypes(ie);
	ManageEventTypeGroups metgs = new ManageEventTypeGroups(ie);
	ManageYourSafetyNetwork mysn = new ManageYourSafetyNetwork(ie);
	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://localhost.localdomain/fieldid/";

	protected void setUp() throws Exception {
		super.setUp();
		misc.start();
		login.gotoLoginPage(loginURL);
		if (once) {
			once = false;
			timestamp = misc.createTimestampDirectory();
		}
	}

	public void testAdministration() throws Exception {
		String method = getName();
		String company = "unirope";
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testAdministrationManageOrganizations() throws Exception {
		String method = getName();
		String company = "unirope";
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mos.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testAdministrationManageCustomers() throws Exception {
		String method = getName();
		String company = "halo";		// someone with more than 20 customers but not too many
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mcs.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testAdministrationManageUsers() throws Exception {
		String method = getName();
		String company = "unirope";
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mus.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	// Manage User Registrations
	
	public void testAdministrationManageSystemSettings() throws Exception {
		String method = getName();
		String company = "unirope";
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mss.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testAdministrationManageProductTypes() throws Exception {
		String method = getName();
		String company = "unirope";		// someone with more than 20 product types but not much more
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mpts.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	// Manage Product Type Groups
	
	// Manage Product Code Mappings
	
	// Manage Product Statuses
		
	public void testAdministrationManageInspectionTypes() throws Exception {
		String method = getName();
		String company = "halo";	// more than 20 inspection types but not many more
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mits.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testAdministrationManageEventTypeGroups() throws Exception {
		String method = getName();
		String company = "halo";	// more than 20 inspection types but not many more
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			metgs.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	// Manage Inspection Books
	
	// Auto Attribute Wizard
	
	// Manage Comment Templates
	
	// Data Log
	
	public void testAdministrationManageYourSafetyNetwork() throws Exception {
		String method = getName();
		String company = "jergens";	// Need to log in as a manufacturer
		String userid = "n4systems";
		String password = "makemore$";
		String linkedCompany = "swwr";
		String linkedUserid = "n4systems";
		String linkedPassword = "makemore$";

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
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testHome() throws Exception {
		String method = getName();
		String company = "unirope";		// someone with jobs
		String userid = "n4systems";
		String password = "makemore$";
		boolean jobs = true;

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			home.validate(jobs);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testIdentify() throws Exception {
		String method = getName();
		String company = "unirope";		// someone with integration and a known order number
		String userid = "n4systems";
		String password = "makemore$";
		String orderNumber = "100203";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			identify.validate(orderNumber);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	// Inspect
	
	public void testAssets() throws Exception {
		String method = getName();
		String company = "cglift";	// need a tenant with 10000+ assets
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			assets.validate("Reel/ID");
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testReporting() throws Exception {
		String method = getName();
		String company = "hercules";	// need a tenant with 10000+ inspections
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			reporting.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	// need a validate for Schedule

	public void testMyAccount() throws Exception {
		String method = getName();
		String company = "uts";
		String userid = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			myAccount.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		login.close();
	}
}
