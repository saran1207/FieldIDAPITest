package com.n4systems.fieldid.testcase;

import watij.elements.Radio;
import watij.elements.Radios;
import watij.runtime.ie.IE;
import junit.framework.TestCase;
import static watij.finders.FinderFactory.*;

public class WEB_765 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String tenant = "hysafe";
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
		helper.setTenant(tenant);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.start(ie, helper.getLoginURL());	// login screen
		ie.maximize();					// maximize window for screen shots
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);	// login
	}
	
	public void testPressingEnterDuringAddUserDoesNotEnableAllPermissions() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.gotoAddEmployeeUser(ie);
			ie.textField(0).focus();
			ie.sendKeys("{ENTER}");
			Radios rs = ie.radios(value("true"));
			for(int i = 0; i < rs.length(); i++) {
				Radio r = rs.get(i);
				if(r.checked()) {
					throw new TestCaseFailedException();
				}
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
