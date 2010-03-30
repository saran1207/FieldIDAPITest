package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;

import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.Identify;
import com.n4systems.fieldid.Login;

import junit.framework.TestCase;

public class validateAddProductPage extends TestCase {

	IE ie = new IE();
	FieldIDMisc helper = new FieldIDMisc(ie);
	Login login = new Login(ie);
	Identify identify = new Identify(ie);

	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://localhost.localdomain/fieldid/";

	protected void setUp() throws Exception {
		super.setUp();
		super.setUp();
		helper.start();
		login.gotoLoginPage(loginURL);
		if(once) {
			once = false;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	public void testAddProductPageTenantWithNoExtendedFeatures() throws Exception {
		String method = getName();
		String company = "swwr";
		String username = "n4systems";
		String password = "Xk43g8!@";

		try {
			login.setCompany(company);
			login.setUserName(username);
			login.setPassword(password);
			login.login();
			identify.gotoAddProduct();
			boolean jobsites = false;
			boolean integration = false;
			identify.validateAddProductPage(jobsites, integration);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testAddProductPageTenantWithIntegration() throws Exception {
		String method = getName();
		String company = "unirope";
		String username = "n4systems";
		String password = "Xk43g8!@";

		try {
			login.setCompany(company);
			login.setUserName(username);
			login.setPassword(password);
			login.login();
			identify.gotoAddProduct();
			boolean jobsites = false;
			boolean integration = true;
			identify.validateAddProductPage(jobsites, integration);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testAddProductPageTenantWithJobsites() throws Exception {
		String method = getName();
		String company = "key";
		String username = "n4systems";
		String password = "Xk43g8!@";

		try {
			login.setCompany(company);
			login.setUserName(username);
			login.setPassword(password);
			login.login();
			identify.gotoAddProduct();
			boolean jobsites = true;
			boolean integration = false;
			identify.validateAddProductPage(jobsites, integration);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		helper.myWindowCapture(timestamp + "/tearDown-" + getName() + ".png");
		login.close();
	}
}
