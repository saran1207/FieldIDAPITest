package com.n4systems.fieldid.testcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_703 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String tenant = "hysafe";
	final String user = "n4systems";
	final String password = "Xk43g8!@";
	static int numJobs = 7;
	static int maxNumJobs = 5;				// Just what Alex has set the number of jobs to be.
	static String userName = "N4 Systems";
	static Set<String> jobTitles = new TreeSet<String>();

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
		userName = helper.getUserNameFromUserID(ie, user);
		for(int i = 0; i < numJobs; i++) {
			String jobTitle = helper.generateRandomString(15);
			String jobID = jobTitle;
			helper.addJob(ie, "Event", jobID, jobTitle, null, null, null, true, null, null, null, null, null, null, null);
			jobTitles.add(jobTitle);
			helper.addResourceToJob(ie, jobTitle, userName);
		}
	}
	
	public void testJobsAssignedToMeOnHomePage() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoHome(ie);
			Set<String> jobs = helper.getJobsOnHomePage(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-Home-Page.png", ie);
			if(jobs.size() != maxNumJobs) {
				throw new TestCaseFailedException();
			}
			for(String job : jobs) {
				helper.gotoJob(ie, job);
				helper.myWindowCapture(timestamp + "/" + method + "-Job-" + job + "-Page.png", ie);
				if(!helper.validateJobAssignedToEmployee(ie, job, userName)) {
					throw new TestCaseFailedException();
				}
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testFilterJobListToJustAssignedToMe() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoJobsAssignedToMe(ie);
			List<String> myJobs = helper.getJobs(ie);
			helper.gotoJobs(ie);
			List<String> allJobs = helper.getJobs(ie);
			List<String> notMyJobs = new ArrayList<String>();
			notMyJobs.addAll(allJobs);
			notMyJobs.removeAll(myJobs);
			// confirm I am not on any of the other jobs
			for(String jobTitle : notMyJobs) {
				if(helper.isUserAssignedToJob(ie, jobTitle, userName)) {
					throw new TestCaseFailedException();
				}
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
