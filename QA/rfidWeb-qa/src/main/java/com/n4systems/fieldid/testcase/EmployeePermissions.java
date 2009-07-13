package com.n4systems.fieldid.testcase;

import junit.framework.TestCase;
import watij.runtime.ie.IE;

public class EmployeePermissions extends TestCase {

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
	
	public void test() throws Exception {
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

		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	// TODO: test the user permissions for employee users (BIG)
	//
	// Test Identify, Inspect, Assets, Reporting, Schedule, Administration
	// Create users with each permission plus n4systems (all permissions)
	// Test with customers with job sites, integration, non-integration
	//
	//		Tag Products      
	//		Manage System Configuration      
	//		Manage System Users      
	//		Manage End Users      
	//		Create Inspections      
	//		Edit Inspections


	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}

}
