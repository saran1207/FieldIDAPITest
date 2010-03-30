package com.n4systems.fieldid.testcase;

import java.util.Random;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * If you have more than one page of Jobs the 2, Next and Last links do not work.
 * 
 * @author dgrainge
 *
 */
public class WEB_782 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	static final int numOfJobs = 21;		// Want to create more than one page of Jobs

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		String user = "n4systems";
		String password = "Xk43g8!@";
		helper.loginJobsTenant(ie, user, password);
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
			try {
				helper.gotoJobs(ie);
				helper.gotoPage(ie, "2");
				helper.validateJobsPage(ie);
			} catch (Exception e) {	// if it cannot get to page 2, try creating enough jobs to make a page 2
				Random r = new Random();
				int n = Math.abs(r.nextInt());
				String typeOfJob = "Event";
				String jobID = "ID-" + n;
				String jobTitle = "WEB-782-" + n;
				for(int i = 0; i < numOfJobs; i++) {
					if(typeOfJob.equals("Event"))
						typeOfJob = "Asset";
					helper.addJob(ie, typeOfJob, jobID+i, jobTitle+i, null, null, null, true, null, null, null, null, null, null, null);
				}
			}
		}
	}
	
	public void testGoToNextPage() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoJobs(ie);
			helper.gotoNextPage(ie);
			helper.validateJobsPage(ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testGoToPageOne() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoJobs(ie);
			String page = "1";
			helper.gotoPage(ie, page);
			helper.validateJobsPage(ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testGoToLastPage() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoJobs(ie);
			helper.gotoLastPage(ie);
			helper.validateJobsPage(ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testGoToFirstPage() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoJobs(ie);
			helper.gotoLastPage(ie);
			helper.gotoFirstPage(ie);
			helper.validateJobsPage(ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testGoToPageTwo() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoJobs(ie);
			String page = "2";
			helper.gotoPage(ie, page);
			helper.validateJobsPage(ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testGoToPreviousPage() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoJobs(ie);
			helper.gotoPage(ie, "2");
			helper.gotoPreviousPage(ie);
			helper.validateJobsPage(ie);
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
