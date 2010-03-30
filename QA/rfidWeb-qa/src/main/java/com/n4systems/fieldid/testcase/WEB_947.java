package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.Home;
import com.n4systems.fieldid.Login;
import junit.framework.TestCase;

public class WEB_947 extends TestCase {
	IE ie = new IE();
	Home home = new Home(ie);
	Login login = new Login(ie);
	FieldIDMisc misc = new FieldIDMisc(ie);

	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://localhost.localdomain/fieldid/";
	String company = "unirope";
	String userid = "n4systems";
	String password = "Xk43g8!@";

	protected void setUp() throws Exception {
		super.setUp();
		misc.start();
		login.gotoLoginPage(loginURL);
		if(once) {
			once = false;
			timestamp = misc.createTimestampDirectory() + "/";
		}
		login.setCompany(company);
		login.setUserName(userid);
		login.setPassword(password);
		login.login();
	}
	
	public void testInstructionalVideos() throws Exception {
		String method = getName();

		try {
			home.gotoHome();
			home.gotoInstructionalVideos();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		misc.myWindowCapture(timestamp + "/tearDown-" + getName() + ".png");
		login.close();
	}
}
