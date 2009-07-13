package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_709 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	public void testClickingOnViewUpcomingInspections() throws Exception {
		String method = helper.getMethodName();

		try {
			String tenant = "hercules";
			String user = "n4systems";
			String password = "makemore$";

			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);
			helper.validateHomePage(ie, timestamp);
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
