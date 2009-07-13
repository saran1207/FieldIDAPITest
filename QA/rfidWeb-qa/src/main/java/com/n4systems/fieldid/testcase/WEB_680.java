package com.n4systems.fieldid.testcase;

import java.util.ArrayList;
import java.util.Map;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_680 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	static Map<String, String>setupData = null;
	final String tenant = "cglift";
	final String password = "makemore$";	// default password for everyone
	final String emailAddress = "darrell.grainger@n4systems.com";
	ArrayList<String> reportNames = new ArrayList<String>();

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setTenant(tenant);
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	
	/**
	 * log in as n4systems
	 * 		generate default report for setupData.get("customerName"); no division
	 *  	generate report for setupData.get("customerName"); division0, all columns
	 *  	generate report for setupData.get("customerName"); division1, even columns
	 *  	generate report for setupData.get("customerName"); division2, odd columns
	 *  	generate report for setupData.get("customerName"); division3, all columns
	 *  
	 */
	
	/**
	 * Log in a user.
	 * 
	 * @throws Exception
	 */
	private void loginUser(String userName, boolean enduser) throws Exception {
		String method = helper.getMethodName();

		try {
			helper.setEndUser(enduser);
			helper.setUserName(userName);
			helper.setPassword(password);
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Create all the test data for subsequent test cases.
	 * If this test case fails, all the other test cases will fail as well.
	 * We know if this test case failed because setupData will equal null.
	 * 
	 * It will also dump the setupData to a file called ${timestamp}/data.txt
	 * where ${timestamp} is the date/time the test data was created.
	 * 
	 * @throws Exception
	 */
	public void testSetup() throws Exception {
		String method = helper.getMethodName();

		try {
			loginUser("n4systems", false);
			setupData = helper.setupForEndUser(ie, emailAddress, password);
			helper.dumpSetupForEndUser(setupData, timestamp + "data.txt");
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Create a number of reports and share them with all the end users.
	 * 
	 * @throws Exception
	 */
	public void testN4SystemsSavingReports() throws Exception {
		String method = helper.getMethodName();

		try {
			int i, numUsers = Integer.parseInt(setupData.get("numUsers"));
			loginUser("n4systems", false);

			// report #1, default, filtered by customer
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, setupData.get("customerName"), "", null, null, null, null);
			String reportName = "default-" + setupData.get("random");
			helper.addSavedReport(ie, reportName);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + "/" + method + "My-Account-One-Saved-Reports.png", ie);
			helper.gotoSavedReport(ie, reportName);
			helper.myWindowCapture(timestamp + "/" + method + "-" + reportName + "-Saved-Reports.png", ie);
			for(i = 0; i < numUsers; i++) {
				String userID = setupData.get("userName"+i);
				String userName = helper.getUserNameFromUserID(ie, userID);
				helper.shareSavedReport(ie, reportName, userName);
			}
			reportNames.add(reportName);
			
			// report #2, all columns, filtered by customer
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, setupData.get("customerName"), "", null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			reportName = "all-columns-" + setupData.get("random");
			helper.addSavedReport(ie, reportName);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + "/" + method + "My-Account-Two-Saved-Reports.png", ie);
			helper.gotoSavedReport(ie, reportName);
			helper.myWindowCapture(timestamp + "/" + method + "-" + reportName + "-Saved-Reports.png", ie);
			for(i = 0; i < numUsers; i++) {
				String userID = setupData.get("userName"+i);
				String userName = helper.getUserNameFromUserID(ie, userID);
				helper.shareSavedReport(ie, reportName, userName);
			}
			reportNames.add(reportName);
			
			// report #3, all columns, no filtering
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, "", "", null, null, null, null);
			reportName = "all";
			helper.addSavedReport(ie, reportName);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + "/" + method + "My-Account-Three-Saved-Reports.png", ie);
			helper.gotoSavedReport(ie, reportName);
			helper.myWindowCapture(timestamp + "/" + method + "-" + reportName + "-Saved-Reports.png", ie);
			for(i = 0; i < numUsers; i++) {
				String userID = setupData.get("userName"+i);
				String userName = helper.getUserNameFromUserID(ie, userID);
				helper.shareSavedReport(ie, reportName, userName);
			}
			reportNames.add(reportName);

		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Delete the reports from the n4systems user. They should still be available
	 * to the end users in the next test case.
	 * 
	 * @throws Exception
	 */
	public void testN4SystemDeletingSavedReports() throws Exception {
		String method = helper.getMethodName();

		try {
			loginUser("n4systems", false);

			for(int i = 0; i < reportNames.size(); i++) {
				String reportName = reportNames.get(i);
				helper.gotoMyAccount(ie);
				helper.deleteSavedReport(ie, reportName);
				helper.myWindowCapture(timestamp + "/" + method + "-" + reportName + ".png", ie);
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	private void loginEndUser(String userName) throws Exception {
		helper.setEndUser(false);
		helper.setUserName(userName);
		helper.setPassword(password);
		helper.loginBrandedDefaultRegular(ie, false);
	}

	/**
	 * Confirm the reports shared by n4systems are still here. Run the
	 * reports and check that there is no security breach. Export the
	 * reports to Excel as well and confirm there is no security breach
	 * on the Excel spreadsheets as well.
	 * 
	 * @throws Exception
	 */
	public void testCheckSharedReports() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			int numUsers = Integer.parseInt(setupData.get("numUsers"));
			for(int i = 0; i < numUsers; i++) {
				String userName = setupData.get("userName"+i);
				loginEndUser(userName);
				helper.gotoMyAccount(ie);
				helper.myWindowCapture(timestamp + "/" + userName + "-Saved-Reports.png", ie);
				for(int j = 0; j < reportNames.size(); j++) {
					String reportName = reportNames.get(j);
					helper.gotoSavedReport(ie, reportName);
					helper.myWindowCapture(timestamp + "/" + userName + "-Saved-Report-" + reportName + ".png", ie);
					helper.exportToExcel(ie);
				}
				helper.logout(ie);
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testEndUsersDeleteSharedReport() throws Exception {
		String method = helper.getMethodName();

		try {
			loginUser("n4systems", false);

			// create the report
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, "", "", null, null, null, null);
			
			// save the report
			String reportName = "shared";
			helper.addSavedReport(ie, reportName);
			
			// confirm it saved okay
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + "/" + method + "My-Account-Saved-Reports-Before-Delete.png", ie);
			
			// run the saved report and confirm it
			helper.gotoSavedReport(ie, reportName);
			helper.myWindowCapture(timestamp + "/" + method + "-" + reportName + "-Saved-Reports.png", ie);
			
			// share the report with all the end users
			int numUsers = Integer.parseInt(setupData.get("numUsers"));
			for(int i = 0; i < numUsers; i++) {
				String userID = setupData.get("userName"+i);
				String userName = helper.getUserNameFromUserID(ie, userID);
				helper.shareSavedReport(ie, reportName, userName);
			}
			
			helper.logout(ie);
			
			// delete the shared report from each end users
			for(int i = 0; i < numUsers; i++) {
				String userName = setupData.get("userName"+i);
				loginEndUser(userName);
				helper.gotoMyAccount(ie);
				helper.myWindowCapture(timestamp + "/" + userName + "-Saved-Reports.png", ie);
				for(int j = 0; j < reportNames.size(); j++) {
					helper.deleteSavedReport(ie, reportName);
					helper.myWindowCapture(timestamp + "/" + userName + "-Saved-Report-" + reportName + ".png", ie);
				}
				helper.logout(ie);
			}

			// log back in as n4systems and confirm all those reports are still there
			helper.setUserName("n4systems");
			helper.setEndUser(false);
			helper.loginBrandedDefaultRegular(ie, false);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + "/" + method + "My-Account-Saved-Reports-After-Delete.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testShareReportNameAlreadyExists() throws Exception {
		String method = helper.getMethodName();

		try {
			loginUser("n4systems", false);
			String employeeID = setupData.get("employee0");
			String employeeUser = helper.getUserNameFromUserID(ie, employeeID);

			// add a report
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, "", "", null, null, null, null);
			String reportName = "all";
			helper.addSavedReport(ie, reportName);
			helper.shareSavedReport(ie, reportName, employeeUser);
			String customer = setupData.get("customerName");
			
			// add a second report with the same name
			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, customer, "", null, null, null, null);
			helper.addSavedReport(ie, reportName);
			helper.shareSavedReport(ie, reportName, employeeUser);
			helper.logout(ie);
			
			loginEndUser(employeeID);
			helper.gotoMyAccount(ie);
			helper.myWindowCapture(timestamp + "/Two-Reports-With-Same-Name-" + method + ".png", ie);
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
