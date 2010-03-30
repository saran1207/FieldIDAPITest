package com.n4systems.fieldid.testcase;

import java.util.Arrays;
import java.util.List;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_826 extends TestCase {
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
	
	public void testCannotAddNonEmployeeUser() throws Exception {
		String method = helper.getMethodName();

		try {
			String jobID = helper.generateRandomString(15);
			String title = jobID;
			helper.addJob(ie, "Event", jobID, title, null, null, null, true, null, null, null, null, null, null, null);
			String[] emps = helper.getListOfUserNames(ie, null, "Employee", false);
			List<String> resources = helper.getListOfResourcesAssignable(ie, title);
			List<String> employees = Arrays.asList(emps);
			if(!resources.equals(employees)) {
				System.out.println(resources);
				System.out.println(employees);
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
