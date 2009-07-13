package com.n4systems.fieldid.testcase;

import java.util.Random;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * Custom Event (Inspection) Type Groups
 * 
 * @author dgrainge
 *
 */
public class WEB_562 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	String tenant = "hysafe";
	String user = "n4systems";
	String password = "makemore$";
	static String name = null;
	static String reportTitle = null;


	protected void setUp() throws Exception {
		super.setUp();
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
	}
	
	/**
	 * Add an event type group
	 * 
	 * @throws Exception
	 */
	public void testAddAnEventTypeGroup() throws Exception {
		String method = helper.getMethodName();

		try {
			Random n = new Random();
			name = "WEB-562-" + Math.abs(n.nextInt());
			reportTitle = "WEB-562-" + Math.abs(n.nextInt());
			helper.addEventTypeGroup(ie, name, reportTitle);
			if(!helper.isEventTypeGroup(ie, name)) {
				throw new TestCaseFailedException();
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Edit an event type group. Go to the Group Information page then click 'Edit'.
	 * 
	 * @throws Exception
	 */
	public void testEditAnEventTypeGroup() throws Exception {
		String method = helper.getMethodName();

		try {
			String newName = "new-" + name;
			String newReportTitle = "new-" + reportTitle;
			helper.editEventTypeGroup(ie, name, newName, newReportTitle);
			name = newName;
			reportTitle = newReportTitle;
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Edit an event type group. Go to the Group Information page then click 'edit'.
	 * 
	 * @throws Exception
	 */
	public void testEditAnEventTypeGroup2() throws Exception {
		String method = helper.getMethodName();

		try {
			String newName = "new-" + name;
			String newReportTitle = "new-" + reportTitle;
			helper.editEventTypeGroup2(ie, name, newName, newReportTitle);
			name = newName;
			reportTitle = newReportTitle;
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Edit an event type group. Go to the Event Type Group list and click 'Edit'.
	 * 
	 * @throws Exception
	 */
	public void testEditAnEventTypeGroup3() throws Exception {
		String method = helper.getMethodName();

		try {
			String newName = "new-" + name;
			String newReportTitle = "new-" + reportTitle;
			helper.editEventTypeGroup3(ie, name, newName, newReportTitle);
			name = newName;
			reportTitle = newReportTitle;
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Delete an event type group. Go to the Event Type Group list and click 'Delete'.
	 * 
	 * @throws Exception
	 */
	public void testDeleteAnEventTypeGroup() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.deleteEventTypeGroup(ie, name);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Delete an event type group. Go to the Group Information page then click 'Delete' button.
	 * Uses the testAddAnEventTypeGroup method to add the group before deleting it.
	 * 
	 * @throws Exception
	 */
	public void testDeleteAnEventTypeGroup2() throws Exception {
		String method = helper.getMethodName();

		try {
			testAddAnEventTypeGroup();
			helper.deleteEventTypeGroup2(ie, name);
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
