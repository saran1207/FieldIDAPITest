package com.n4systems.fieldid.testcase;

import java.text.SimpleDateFormat;
import java.util.Date;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_775 extends TestCase {
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
	
	public void testAddEventSearchCriteriaFormContentsMaintained() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "makemore$";
			helper.loginJobsTenant(ie, user, password);
			String s = "WEB-775";
			int length = 15 - s.length();
			String jobID = s + helper.generateRandomString(length);
			String title = s + helper.generateRandomString(length);
			String scheduleStatus = "All";
			String customer = helper.getRandomCustomer(ie);
			String division = helper.getRandomDivision(ie, customer);
			String productStatus = helper.getRandomProductStatus(ie);
			String productType = null;
			String inspectionType = null;
			while(inspectionType == null) {	// pick product types until we find one with an inspection type
				productType = helper.getRandomProductType(ie);
				inspectionType = helper.getRandomInspectionType(ie, productType);
			}
			
			String inspectionTypeGroup = helper.getInspectionTypeGroup(ie, inspectionType);
			String[] productParameters = { "" };
			String serialNumber = helper.addProduct(ie, null, true, null, null, customer, division, null, null, null, productStatus, null, null, null, productType, productParameters, null, null, "Save");

			// add a schedule to the product
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			String date = sdf.format(new Date());
			
			helper.addInspectionSchedule(ie, serialNumber, inspectionType, date, null);
			
			// set the date range so the schedule falls inside the range
			String fromDate = date;
			String toDate = date;
			
			helper.addJob(ie, "Event", jobID, title, null, null, null, true, null, null, null, null, null, null, null);
			helper.runEventJobAddEventSearch(ie, scheduleStatus, serialNumber, customer, division, inspectionTypeGroup, productType, productStatus, fromDate, toDate);
			helper.validateEventJobAddEventSearch(ie, scheduleStatus, serialNumber, customer, division, inspectionTypeGroup, productType, productStatus, fromDate, toDate);
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
