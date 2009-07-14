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
import com.n4systems.fieldid.admin.ManageInspectionTypes;
import com.n4systems.fieldid.admin.ManageProductTypes;
import com.n4systems.fieldid.admin.ManageSystemSettings;
import com.n4systems.fieldid.admin.ManageUsers;

import junit.framework.TestCase;

public class Validate extends TestCase {
	IE ie = new IE();
	Admin admin = new Admin(ie);
	Assets assets = new Assets(ie);
	FieldIDMisc misc = new FieldIDMisc(ie);
	Home home = new Home(ie);
	Identify identify = new Identify(ie);
	Login login = new Login(ie);
	MyAccount myAccount = new MyAccount(ie);
	Reporting reporting = new Reporting(ie);

	ManageCustomers mcs = new ManageCustomers(ie);
	ManageInspectionTypes mits = new ManageInspectionTypes(ie);
	ManageProductTypes mpts = new ManageProductTypes(ie);
	ManageSystemSettings mss = new ManageSystemSettings(ie);
	ManageUsers mus = new ManageUsers(ie);

	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://localhost.localdomain/fieldid/";
	String company = "hercules";
	String customer = "ABB Inc.";
	String userid = "n4systems";
	String password = "makemore$";

	protected void setUp() throws Exception {
		super.setUp();
		misc.start();
		login.gotoLoginPage(loginURL);
		if (once) {
			once = false;
			timestamp = misc.createTimestampDirectory();
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
		}
	}

	public void testLibraries() throws Exception {
		String method = getName();

		try {
//			admin.validate();
			assets.validate();
//			misc.validate();
//			home.validate();
			identify.validate();
//			login.validate();
			myAccount.validate();
			reporting.validate();
			admin.gotoAdministration();
			mcs.validate();
			admin.gotoAdministration();
			mits.validate();
			admin.gotoAdministration();
			mpts.validate();
			admin.gotoAdministration();
			mss.validate();
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
	protected void tearDown() throws Exception {
		super.tearDown();
		login.close();
	}

}
