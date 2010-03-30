package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * Test logging into the various different kinds of tenants.
 * There was a problem were by I couldn't even log in to a
 * tenant with Jobs (Projects) enabled.
 * 
 * @author dgrainge
 *
 */
public class WEB_813 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String[] tenants = {
			"UTS",			// nothing
			"navfac",		// Compliance
			"unirope",		// Integration
			"hysafe",		// Jobs
			"oceaneering"	// JobSites
		};

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
		helper.start(ie);
		ie.maximize();					// maximize window for screen shots
	}
	
	private void login(String tenant) throws Exception {
		helper.setTenant(tenant);
		helper.setUserName(user);
		helper.setPassword(password);
		ie.goTo(helper.getLoginURL());
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);	// login
	}
	
	public void testLogin() throws Exception {
		String method = helper.getMethodName();

		try {
			for(int i = 0; i < tenants.length; i++) {
				login(tenants[i]);
				helper.myWindowCapture(timestamp + "/" + method + "-" + tenants[i] + ".png", ie);
				if(!helper.validateHomePage(ie))
					throw new TestCaseFailedException();
				helper.logout(ie);
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
