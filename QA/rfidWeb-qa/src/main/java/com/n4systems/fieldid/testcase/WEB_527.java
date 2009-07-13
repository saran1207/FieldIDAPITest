package com.n4systems.fieldid.testcase;

import java.util.Random;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * Customer reporting, Phase II, Save a report.
 * 
 * User Story:
 * 1. User Generates a report
 * 2. Picks columns and regenerates the report
 * 3. Saves the settings to a report name.
 * 4. Under the My Account screen the saved report is now listed.
 * 
 * Requirements
 * 1. Reports are per user
 * 2. My Accounts lists reports and has Edit/Delete options
 * 3. Edit report takes you to a place to edit the columns
 * 4. User can save an unlimited number of reports.
 * 5. Reports will have a report name and a description, description is optional
 * 6. "Save" will save report and take you to list of reports under My Account
 * 7. "Save & Return to Report" will save and return to report results.
 *
 */
public class WEB_527 extends TestCase {	// TODO: the whole saved reports area is changing

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	private void loginTenant(String tenant) throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "makemore$";

			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);

		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Log in as a tenant with Projects feature.
	 * No Integration, no Compliance, no JobSites.
	 * 
	 * @throws Exception
	 */
	private void loginTenantWithProjects() throws Exception {
		loginTenant("hysafe");
	}
	
	/**
	 * Log in as a tenant with Integration feature.
	 * No Compliance, no JobSites, no Projects.
	 * 
	 * @throws Exception
	 */
	private void loginTenantWithIntegration() throws Exception {
		loginTenant("unirope");
	}
	
	/**
	 * Log in as a tenant with JobSites. Also has Compliance.
	 * No Integration, no Projects.
	 * 
	 * @throws Exception
	 */
	private void loginTenantWithJobSites() throws Exception {
		loginTenant("key");
	}
	
	/**
	 * Log in as a tenant with no extended features.
	 * No Integration, no Compliance, no JobSites, no Projects.
	 * 
	 * @throws Exception
	 */
	private void loginTenant() throws Exception {
		loginTenant("UTS");
	}
	
	public void testCustomReportingWithProjects() throws Exception {
		String method = helper.getMethodName();

		try {
			loginTenantWithProjects();
			Random r = new Random();
			int n = r.nextInt();
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			String name = "test-" + Math.abs(n);
			String newname = "delete-" + Math.abs(n);
			helper.addSavedReport(ie, name);
			helper.myWindowCapture(timestamp + method + "-Saved-First-Report-Default-Columns.png", ie);
			helper.renameSavedReport(ie, name, newname);
			helper.myWindowCapture(timestamp + method + "-Renamed-First-Report-Results.png", ie);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + method + "-Renamed-First-Report-My-Account.png", ie);
			helper.gotoSavedReport(ie, newname);
			helper.setAllColumnsOnSearchResults(ie);
			helper.addSavedReportAs(ie, name);
			helper.myWindowCapture(timestamp + method + "-Saved-Second-Report-All-Columns.png", ie);
			helper.deleteSavedReport(ie, name);
			helper.myWindowCapture(timestamp + method + "-Deleted-First-Report.png", ie);
			helper.deleteSavedReport(ie, newname);
			helper.myWindowCapture(timestamp + method + "-Deleted-Second-Report.png", ie);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + method + "-Reports-Deleted-My-Account-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testGoingFromReportingResultsToMyAccount() throws Exception {
		String method = helper.getMethodName();

		try {
			loginTenantWithProjects();
			helper.gotoReporting(ie);
			helper.gotoSavedReportFromReportPage(ie);
			helper.myWindowCapture(timestamp + method + "-My-Account-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testCustomReportingWithIntegration() throws Exception {
		String method = helper.getMethodName();

		try {
			loginTenantWithIntegration();
			Random r = new Random();
			int n = r.nextInt();
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			String name = "test-" + Math.abs(n);
			String newname = "delete-" + Math.abs(n);
			helper.addSavedReport(ie, name);
			helper.myWindowCapture(timestamp + method + "-Saved-First-Report-Default-Columns.png", ie);
			helper.renameSavedReport(ie, name, newname);
			helper.myWindowCapture(timestamp + method + "-Renamed-First-Report-Results.png", ie);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + method + "-Renamed-First-Report-My-Account.png", ie);
			helper.gotoSavedReport(ie, newname);
			helper.setAllColumnsOnSearchResults(ie);
			helper.addSavedReportAs(ie, name);
			helper.myWindowCapture(timestamp + method + "-Saved-Second-Report-All-Columns.png", ie);
			helper.deleteSavedReport(ie, name);
			helper.myWindowCapture(timestamp + method + "-Deleted-First-Report.png", ie);
			helper.deleteSavedReport(ie, newname);
			helper.myWindowCapture(timestamp + method + "-Deleted-Second-Report.png", ie);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + method + "-Reports-Deleted-My-Account-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testGoingFromReportingResultsToMyAccountIntegrationTenant() throws Exception {
		String method = helper.getMethodName();

		try {
			loginTenantWithIntegration();
			helper.gotoReporting(ie);
			helper.gotoSavedReportFromReportPage(ie);
			helper.myWindowCapture(timestamp + method + "-My-Account-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testCustomReportingWithJobSites() throws Exception {
		String method = helper.getMethodName();

		try {
			loginTenantWithJobSites();
			Random r = new Random();
			int n = r.nextInt();
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			String name = "test-" + Math.abs(n);
			String newname = "delete-" + Math.abs(n);
			helper.addSavedReport(ie, name);
			helper.myWindowCapture(timestamp + method + "-Saved-First-Report-Default-Columns.png", ie);
			helper.renameSavedReport(ie, name, newname);
			helper.myWindowCapture(timestamp + method + "-Renamed-First-Report-Results.png", ie);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + method + "-Renamed-First-Report-My-Account.png", ie);
			helper.gotoSavedReport(ie, newname);
			helper.setAllColumnsOnSearchResults(ie);
			helper.addSavedReportAs(ie, name);
			helper.myWindowCapture(timestamp + method + "-Saved-Second-Report-All-Columns.png", ie);
			helper.deleteSavedReport(ie, name);
			helper.myWindowCapture(timestamp + method + "-Deleted-First-Report.png", ie);
			helper.deleteSavedReport(ie, newname);
			helper.myWindowCapture(timestamp + method + "-Deleted-Second-Report.png", ie);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + method + "-Reports-Deleted-My-Account-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testGoingFromReportingResultsToMyAccountJobSitesTenant() throws Exception {
		String method = helper.getMethodName();

		try {
			loginTenantWithJobSites();
			helper.gotoReporting(ie);
			helper.gotoSavedReportFromReportPage(ie);
			helper.myWindowCapture(timestamp + method + "-My-Account-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testCustomReportingWithBasicTenant() throws Exception {
		String method = helper.getMethodName();

		try {
			loginTenant();
			Random r = new Random();
			int n = r.nextInt();
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			String name = "test-" + Math.abs(n);
			String newname = "delete-" + Math.abs(n);
			helper.addSavedReport(ie, name);
			helper.myWindowCapture(timestamp + method + "-Saved-First-Report-Default-Columns.png", ie);
			helper.renameSavedReport(ie, name, newname);
			helper.myWindowCapture(timestamp + method + "-Renamed-First-Report-Results.png", ie);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + method + "-Renamed-First-Report-My-Account.png", ie);
			helper.gotoSavedReport(ie, newname);
			helper.setAllColumnsOnSearchResults(ie);
			helper.addSavedReportAs(ie, name);
			helper.myWindowCapture(timestamp + method + "-Saved-Second-Report-All-Columns.png", ie);
			helper.deleteSavedReport(ie, name);
			helper.myWindowCapture(timestamp + method + "-Deleted-First-Report.png", ie);
			helper.deleteSavedReport(ie, newname);
			helper.myWindowCapture(timestamp + method + "-Deleted-Second-Report.png", ie);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + method + "-Reports-Deleted-My-Account-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testGoingFromReportingResultsToMyAccountBasicTenant() throws Exception {
		String method = helper.getMethodName();

		try {
			loginTenant();
			helper.gotoReporting(ie);
			helper.gotoSavedReportFromReportPage(ie);
			helper.myWindowCapture(timestamp + method + "-My-Account-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
