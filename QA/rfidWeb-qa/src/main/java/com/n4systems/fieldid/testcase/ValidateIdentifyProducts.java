package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;

import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.Identify;
import com.n4systems.fieldid.Login;

import junit.framework.TestCase;

public class ValidateIdentifyProducts extends TestCase {

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
	
	public void testIdentifyProductsPage() throws Exception {
		String method = getName();
		String company = "unirope";		// has to be a tenant with Integration
		String username = "n4systems";
		String password = "makemore$";
		String orderNumber = "100203";	// known order number for Unirope

		try {
			login.setCompany(company);
			login.setUserName(username);
			login.setPassword(password);
			login.login();
			identify.gotoIdentifyProducts();
			identify.validateIdentifyProductsPage();
			identify.gotoOrderNumber(orderNumber);
			identify.validateIdentifyProductsPageWithOrderDetails(orderNumber);
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
