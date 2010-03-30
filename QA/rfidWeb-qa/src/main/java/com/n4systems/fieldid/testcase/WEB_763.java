package com.n4systems.fieldid.testcase;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static watij.finders.FinderFactory.*;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_763 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String tenant = "marine";
	final String user = "n4systems";
	final String password = "Xk43g8!@";

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setTenant(tenant);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.start(ie, helper.getLoginURL());	// login screen
		ie.maximize();					// maximize window for screen shots
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);	// login
	}
	
	public void testProductAttibutesAfterClearForm() throws Exception {
		String method = helper.getMethodName();

		try {
			List<String> productTypes = helper.getProductTypes(ie);
			helper.gotoAssets(ie);
			helper.expandSelectColumns(ie);
			for(String productType : productTypes) {
				helper.selectProductTypeOnSearchForm(ie, productType);
				helper.myWindowCapture(timestamp + "/" + method + "-" + helper.filenameFixer(productType) + ".png", ie);
			}
			ie.button(value("Clear Form")).click();
			helper.myWindowCapture(timestamp + "/" + method + "-Clear-Form" + ".png", ie);
			Set<String> attributes = new TreeSet<String>();
			if(!helper.validateProductAttributesOnSearchForm(ie, attributes)) {	// I'm expecting no attributes
				throw new TestCaseFailedException();
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
