package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_722 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String tenant = "hysafe";
	final String user = "n4systems";
	final String password = "makemore$";

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
	
	public void testAssetLinkOnAssetJob() throws Exception {
		String method = helper.getMethodName();

		try {
			String jobID = helper.generateRandomString(15);
			String title = jobID;
			helper.addJob(ie, "Asset", jobID, title, null, null, null, true, null, null, null, null, null, null, null);
			helper.gotoJobAssets(ie, title);
			if(!helper.validateJobAssets(ie)) {
				throw new TestCaseFailedException();
			}
			helper.myWindowCapture(timestamp + "/" + method + ".png", ie);
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
