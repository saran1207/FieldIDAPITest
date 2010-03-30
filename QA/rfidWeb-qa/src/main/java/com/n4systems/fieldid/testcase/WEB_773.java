package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_773 extends TestCase {
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
	
	public void testErrorMessageWhenConnectingAssetTwice() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "Xk43g8!@";
			helper.loginJobsTenant(ie, user, password);
			String jobID = helper.generateRandomString(15);
			String title = helper.generateRandomString(15);
			String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "Save");
			helper.addJob(ie, "Asset", jobID, title, null, null, null, true, null, null, null, null, null, null, null);
			helper.addAssetToJob(ie, title, serialNumber);
			helper.addAssetToJob(ie, title, serialNumber);
			if(!ie.containsText("The asset is already attached to this job.")) {
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
