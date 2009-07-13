package com.n4systems.fieldid.testcase;

import java.util.List;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_827 extends TestCase {
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
	
	public void testOnlyMyJobsDeleteJob() throws Exception {
		String method = helper.getMethodName();

		try {
			String jobID1 = "not-assigned-" + helper.generateRandomString(10);
			String title1 = jobID1;
			helper.addJob(ie, "Event", jobID1, title1, null, null, null, true, null, null, null, null, null, null, null);

			String jobID2 = "assigned-" + helper.generateRandomString(10);
			String title2 = jobID2;
			helper.addJob(ie, "Event", jobID2, title2, null, null, null, true, null, null, null, null, null, null, null);
			
			String employee = helper.getUserNameFromUserID(ie, helper.getUserName());
			helper.addResourceToJob(ie, title2, employee);
			
			helper.gotoJobsAssignedToMe(ie);
			List<String> myJobs = helper.getJobs(ie);
			if(myJobs.contains(title1)) {
				throw new TestCaseFailedException();	// title1 should not be in the list
			}
			if(!myJobs.contains(title2)) {
				throw new TestCaseFailedException();	// title2 should be in the list
			}
			
			helper.deleteJob(ie, title2, true);
			helper.validateJobsAssignedToMe(ie);
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
