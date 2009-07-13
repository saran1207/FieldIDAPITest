package com.n4systems.fieldid.testcase;

import java.util.Map;

import junit.framework.TestCase;
import watij.runtime.ie.IE;

/**
 * Add support for Projects.
 * 
 * @author dgrainge
 *
 */
public class WEB_400 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String tenant = "hysafe";
	final String user = "n4systems";
	final String password = "makemore$";
	final String emailAddress = "darrell.grainger@n4stystems.com";
	static Map<String,String>data = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
			data = helper.setupForEndUser(ie, emailAddress, password);
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
		
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
