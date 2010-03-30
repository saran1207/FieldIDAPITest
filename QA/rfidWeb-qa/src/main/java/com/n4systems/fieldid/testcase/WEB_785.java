package com.n4systems.fieldid.testcase;

import java.util.List;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_785 extends TestCase {
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
	
	public void testDeletedJobAppearingInInspectionScheduleDropDown() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "Xk43g8!@";
			helper.loginJobsTenant(ie, user, password);
			helper.gotoJobs(ie);
			List<String> jobs = helper.getEventJobs(ie);
			String serialNumber = helper.getProduct(ie);
			boolean fail = !helper.validateJobsOnInspectionSchedule(ie, serialNumber, jobs);
			if(fail) {
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
