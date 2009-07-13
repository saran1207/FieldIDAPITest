package com.n4systems.fieldid.testcase;

import java.util.List;
import java.util.Random;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_819 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String tenant = "navfac";
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
	
	public void testCheckComplianceProductWithNoScheduledInspections() throws Exception {
		String method = helper.getMethodName();

		try {
			Random r = new Random();
			List<String> productTypes = helper.getProductTypes(ie);
			int n = r.nextInt(productTypes.size());
			String productType = productTypes.get(n);
			String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, productType, null, null, null, "Save");
			helper.deleteAllScheduleFromProduct(ie, serialNumber);
			helper.gotoComplianceCheck(ie, serialNumber);
			if(!helper.validateComplianceCheckNoSchedules(ie)) {
				throw new TestCaseFailedException();
			}
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
