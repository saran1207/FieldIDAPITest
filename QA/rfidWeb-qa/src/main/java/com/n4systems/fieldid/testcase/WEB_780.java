package com.n4systems.fieldid.testcase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_780 extends TestCase {
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
	
	public void testAddJobBadDateStarted() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "makemore$";
			helper.loginJobsTenant(ie, user, password);
			Random r = new Random();
			int n = Math.abs(r.nextInt());
			String jobID = "" + n;
			String title = "" + n;
			String expectedDateFormat = "MM/DD/YY HH:MM AM/PM";
			String badDateFormat = "MM/dd/yy HH:mm";	// HH = 24 hour clock
			SimpleDateFormat now = new SimpleDateFormat(badDateFormat);
			String badDate = now.format(new Date());
			
			helper.addJob(ie, "Event", jobID, title, null, null, null, true, null, badDate, null, null, null, null, null);
			if(!ie.containsText(expectedDateFormat)) {
				throw new TestCaseFailedException();
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testAddJobBadEstimatedCompletionDate() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "makemore$";
			helper.loginJobsTenant(ie, user, password);
			Random r = new Random();
			int n = Math.abs(r.nextInt());
			String jobID = "" + n;
			String title = "" + n;
			String expectedDateFormat = "MM/DD/YY HH:MM AM/PM";
			String badDateFormat = "MM/dd/yy HH:mm";	// HH = 24 hour clock
			SimpleDateFormat now = new SimpleDateFormat(badDateFormat);
			String badDate = now.format(new Date());
			
			helper.addJob(ie, "Event", jobID, title, null, null, null, true, null, null, badDate, null, null, null, null);
			if(!ie.containsText(expectedDateFormat)) {
				throw new TestCaseFailedException();
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testAddJobBadActualCompletedDate() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "makemore$";
			helper.loginJobsTenant(ie, user, password);
			Random r = new Random();
			int n = Math.abs(r.nextInt());
			String jobID = "" + n;
			String title = "" + n;
			String expectedDateFormat = "MM/DD/YY HH:MM AM/PM";
			String badDateFormat = "MM/dd/yy HH:mm";	// HH = 24 hour clock
			SimpleDateFormat now = new SimpleDateFormat(badDateFormat);
			String badDate = now.format(new Date());
			
			helper.addJob(ie, "Event", jobID, title, null, null, null, true, null, null, null, badDate, null, null, null);
			if(!ie.containsText(expectedDateFormat)) {
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
