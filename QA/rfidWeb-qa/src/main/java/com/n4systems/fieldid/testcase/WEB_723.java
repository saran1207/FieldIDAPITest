package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * Signatures and company logo was moved out of the database and into
 * the file system. This just goes and checks the images to make sure
 * they are visible on the web pages (e.g. Edit User).
 * 
 * @author dgrainge
 *
 */
public class WEB_723 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		String tenant = "hercules";
		String user = "n4systems";
		String password = "Xk43g8!@";

		helper.setEndUser(false);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	public void testCompanyLogo() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.myWindowCapture(timestamp + "/Can-you-see-the-" + helper.getTenant() + "-logo.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testCertificateLogo() throws Exception {
		String method = helper.getMethodName();

		try {
			String productType = "Wire Mesh Slings";	// not too many Wire Mesh Slings products so we'll use it
			helper.gotoProductSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, productType, null, null);
			helper.printAllManufacturerCertificates(ie);
			System.out.println("Check email for manufacturer certificates. Check they all have company certificate logo.");
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testUserSignatures() throws Exception {
		String method = helper.getMethodName();
		// just check five signatures for now.
		String[] users = { "jgiannou", "jshill", "johns", "barryy", "melanie" };

		try {
			for(int i = 0; i < users.length; i++) {
				helper.gotoEditUser(ie, users[i]);
				helper.myWindowCapture(timestamp + "/" + method + "-" + users[i] + ".png", ie);
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
