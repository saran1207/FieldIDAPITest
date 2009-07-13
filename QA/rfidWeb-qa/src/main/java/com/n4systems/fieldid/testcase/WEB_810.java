package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;

import com.n4systems.fieldid.Assets;
import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.Login;
import com.n4systems.fieldid.Reporting;
import com.n4systems.fieldid.Schedule;

import junit.framework.TestCase;

public class WEB_810 extends TestCase {
	IE ie = new IE();
	FieldIDMisc helper = new FieldIDMisc(ie);
	Login login = new Login(ie);
	Assets assets = new Assets(ie);
	Reporting reporting = new Reporting(ie);
	Schedule schedule = new Schedule(ie);

	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://localhost.localdomain/fieldid/";
	String company = "unirope";
	String userid = "n4systems";
	String password = "makemore$";
	String expectedResult = "Select Display Columns";

	protected void setUp() throws Exception {
		super.setUp();
		helper.start();
		login.gotoLoginPage(loginURL);
		if(once) {
			once = false;
			timestamp = helper.createTimestampDirectory() + "/";
		}
		login.setCompany(company);
		login.setUserName(userid);
		login.setPassword(password);
		login.login();
	}
	
	public void testSelectDisplayColumnsOnAssets() throws Exception {
		String method = getName();

		try {
			assets.gotoAssets();
			String s = assets.getSelectDisplayColumnsHeader();
			assertEquals("Expected '" + expectedResult + "' but got '" + s + "'", s, expectedResult);
			assets.gotoProductSearchResults();
			assets.expandProductSearchResultsSearchCriteria();
			s = assets.getSelectDisplayColumnsHeader();
			assertEquals("Expected '" + expectedResult + "' but got '" + s + "'", s, expectedResult);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testSelectDisplayColumnsOnReporting() throws Exception {
		String method = getName();

		try {
			reporting.gotoReporting();
			String s = reporting.getSelectDisplayColumnsHeader();
			assertEquals("Expected '" + expectedResult + "' but got '" + s + "'", s, expectedResult);
			reporting.gotoReportSearchResults();
			reporting.expandReportSearchResultsSearchCriteria();
			s = reporting.getSelectDisplayColumnsHeader();
			assertEquals("Expected '" + expectedResult + "' but got '" + s + "'", s, expectedResult);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testSelectDisplayColumnsOnSchedule() throws Exception {
		String method = getName();

		try {
			schedule.gotoSchedule();
			String s = schedule.getSelectDisplayColumnsHeader();
			assertEquals("Expected '" + expectedResult + "' but got '" + s + "'", s, expectedResult);
			schedule.gotoScheduleSearchResults();
			schedule.expandScheduleSearchResultsSearchCriteria();
			s = schedule.getSelectDisplayColumnsHeader();
			assertEquals("Expected '" + expectedResult + "' but got '" + s + "'", s, expectedResult);
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
