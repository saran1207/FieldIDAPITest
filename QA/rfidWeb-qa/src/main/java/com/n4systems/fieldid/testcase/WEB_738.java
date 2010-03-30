package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_738 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;

	protected void setUp() throws Exception {
		super.setUp();
		String user = "n4systems";
		String password = "Xk43g8!@";
		String tenant = "swwr";
		helper.setTenant(tenant);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	public void testRejectingRequestForUserAccount() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoLoginPage(ie, helper.getTenant());
			String userID = "dgrainge";
			String emailAddress = "dev@n4systems.com";
			String firstName = "Darrell";
			String lastName = "Grainger";
			String position = "Seated";
			String company = "N4 Systems";
			String phone = "416-599-6464";
			String password = "Xk43g8!@";
			helper.requestAnAccount(ie, userID, emailAddress, firstName, lastName, position, null, company, phone, password, password, null, false, false);
			helper.gotoLoginPage(ie, helper.getTenant());
			helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);
			helper.rejectRequestForUserAccount(ie, userID);
			if(helper.isRequestForUserAccount(ie, userID)) {	// once rejected it shouldn't be there
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
