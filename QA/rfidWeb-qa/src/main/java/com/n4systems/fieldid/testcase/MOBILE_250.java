package com.n4systems.fieldid.testcase;

import java.util.Map;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class MOBILE_250 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String tenant = "hysafe";
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
	
	public void testSetup() throws Exception {
		String method = helper.getMethodName();

		try {
			String emailAddress = "dev@n4systems.com";
			String password = "makemore$";
			String filename = timestamp + "/end-user-data.txt";
			Map<String, String> data = helper.setupForEndUser(ie, emailAddress, password);
			helper.dumpSetupForEndUser(data, filename);
			String random = data.get("random");
			//String customerID = data.get("customerID");
			String customerName = data.get("customerName");
			String employee = data.get("employee0");
			String employeeName = helper.getUserNameFromUserID(ie, employee);
			String division = data.get("division2");
			
			String eventJobID = "event" + random;
			String eventTitle = "eventTitle" + random;
			helper.addJob(ie, "Event", eventJobID, eventTitle, customerName, division, null, true, null, null, null, null, null, null, null);
			
			String assetJobID = "event" + random;
			String assetTitle = "eventTitle" + random;
			helper.addJob(ie, "Asset", assetJobID, assetTitle, customerName, division, null, true, null, null, null, null, null, null, null);
			
			helper.addJob(ie, "Event", "no-customer", "no-customer", null, null, null, true, null, null, null, null, null, null, null);
			helper.addJob(ie, "Asset", "no-customer2", "no-customer2", null, null, null, true, null, null, null, null, null, null, null);

			helper.addResourceToJob(ie, "no-customer", employeeName);
			helper.addEventToEventJob(ie, eventTitle, null, null, customerName, null, null, null, null, null, null, -1);
			helper.addResourceToJob(ie, eventTitle, employeeName);
			helper.addEventToEventJob(ie, eventTitle, null, null, customerName, division, null, null, null, null, null, -1);
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
