package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * Check to see if duplicate RFID shows a bad lightbox. In a previous release,
 * a lightbox appeared to tell the user the RFID number used was already assigned
 * to another product. The bug was that the lightbox was empty. To verify this
 * look at the screen shot created.
 * 
 * @author dgrainge
 *
 */
public class WEB_715 extends TestCase {

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
	
	public void testCheckingRFIDNumberLightbox() throws Exception {
		String method = helper.getMethodName();

		try {
			String rfidNumber = helper.getRandomRFID();
			if(!helper.isProduct(ie, rfidNumber)) {
				helper.addProduct(ie, null, true, rfidNumber, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "Save");
			}
			helper.addProduct(ie, null, true, rfidNumber, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "Save");
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
