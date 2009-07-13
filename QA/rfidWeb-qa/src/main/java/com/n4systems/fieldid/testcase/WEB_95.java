package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import com.n4systems.fieldid.Admin;
import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.Home;
import com.n4systems.fieldid.Login;
import com.n4systems.fieldid.admin.ManageSystemSettings;

import junit.framework.TestCase;

public class WEB_95 extends TestCase {
	IE ie = new IE();
	FieldIDMisc misc = new FieldIDMisc(ie);
	Login login = new Login(ie);
	Home home = new Home(ie);
	Admin admin = new Admin(ie);
	ManageSystemSettings mss = new ManageSystemSettings(ie);

	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://localhost.localdomain/fieldid/";
	String company = "swwr";
	String userid = "n4systems";
	String password = "makemore$";

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
	
	public void testSettingTheWebSiteField() throws Exception {
		String method = getName();
		String url="http://www.unirope.com/";

		try {
			admin.gotoAdministration();
			mss.gotoManageSystemSettings();
			mss.setWebSiteAddress(url);
			mss.saveChangesToSystemSettings();
			home.gotoHome();
			home.checkCompanyWebSiteLink();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testClearingTheWebSiteField() throws Exception {
		String method = getName();
		String url="";

		try {
			// TODO: WEB-964 needs to be fixed
			admin.gotoAdministration();
			mss.gotoManageSystemSettings();
			mss.setWebSiteAddress(url);
			mss.saveChangesToSystemSettings();
			home.gotoHome();
			assertFalse("Wasn't expecting to find a link to the company website", home.isCompanyWebsite());
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testUploadingImages() throws Exception {
		String method = getName();

		try {
			admin.gotoAdministration();
			mss.gotoManageSystemSettings();
			mss.setWebSiteAddress("");
			mss.saveChangesToSystemSettings();
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
