package com.n4systems.fieldid.testcase;

import java.util.List;
import java.util.Random;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_772 extends TestCase {
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
	
	public void test() throws Exception {
		String method = helper.getMethodName();

		try {
			Random r = new Random();
			int n = 0;
			String jobID = helper.generateRandomString(15);
			String title = jobID;
			List<String> customers = helper.getCustomerWithDivisionsList(ie);
			String customer = null;
			String customerID = null;
			if(customers == null || customers.size() == 0) {
				customers = helper.getCustomerList(ie);
				n = r.nextInt(customers.size());
				customer = customers.get(n);
				customerID = helper.getCustomerID(ie, customer);
				helper.addDivison(ie, customerID, customer, "web-772");
				// create some divisions for a customer
			} else {
				n = r.nextInt(customers.size());
				customer = customers.get(n);
				customerID = helper.getCustomerID(ie, customer);
			}
			List<String> divisions = helper.getDivisionList(ie, customer);
			n = r.nextInt(divisions.size());
			String division = divisions.get(n);
			String status = null, description = null, startDate = null, estimateDate = null, actualDate = null, duration = null, poNumber = null, workPerformed = null;
			boolean open = true;
			helper.addJob(ie, "Event", jobID, title, customer, division, status, open, description, startDate, estimateDate, actualDate, duration, poNumber, workPerformed);
			helper.validateEventJobDetails(ie, jobID, title, customer, division, status, open, description, startDate, estimateDate, actualDate, duration, poNumber, workPerformed);
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
