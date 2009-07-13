package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.Home;
import com.n4systems.fieldid.Login;
import junit.framework.TestCase;

public class ValidateHomePage extends TestCase {

	IE ie = new IE();
	FieldIDMisc helper = new FieldIDMisc(ie);
	Login login = new Login(ie);
	Home home = new Home(ie);

	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://localhost.localdomain/fieldid/";

	protected void setUp() throws Exception {
		super.setUp();
		super.setUp();
		helper.start();
		login.gotoLoginPage(loginURL);
		if(once) {
			once = false;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	public void testTenantWithJobs() throws Exception {
		String method = getName();
		String company = "unirope";
		String username = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(username);
			login.setPassword(password);
			login.login();
			home.gotoHome();
			home.validateHomePage(true);
			home.getJobsOnHomePage();
			home.gotoChangeYourPassword();
			home.gotoHome();
			home.gotoFindAProduct();
			home.gotoHome();
			home.gotoInstructionalVideos();
			home.gotoHome();
			home.gotoNewFeatures();
			home.gotoHome();
			home.gotoViewTheInspectionHistoryForAProduct();
			home.gotoHome();
			home.gotoViewUpcomingInspections();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testTenantWithoutJobs() throws Exception {
		String method = getName();
		String company = "swwr";
		String username = "n4systems";
		String password = "makemore$";

		try {
			login.setCompany(company);
			login.setUserName(username);
			login.setPassword(password);
			login.login();
			home.gotoHome();
			home.validateHomePage(false);
			home.gotoChangeYourPassword();
			home.gotoHome();
			home.gotoFindAProduct();
			home.gotoHome();
			home.gotoInstructionalVideos();
			home.gotoHome();
			home.gotoNewFeatures();
			home.gotoHome();
			home.gotoViewTheInspectionHistoryForAProduct();
			home.gotoHome();
			home.gotoViewUpcomingInspections();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		helper.myWindowCapture(timestamp + "/tearDown-" + getName() + ".png");
		login.close();
	}
}
