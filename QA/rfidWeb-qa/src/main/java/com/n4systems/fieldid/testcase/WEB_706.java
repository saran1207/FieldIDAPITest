package com.n4systems.fieldid.testcase;

import java.text.SimpleDateFormat;
import java.util.Date;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * Emailed inspection schedules are not respecting tenant date format.
 * This test will set up a notification for tomorrow. Therefore you need
 * to run this test case today then check tomorrow morning to see what
 * the results are.
 * 
 * The format for hercules is MM/dd/yy.
 * The format for unilift is dd/MM/yy.
 * 
 * There is one test case for each of these tenants. We want to make sure
 * they are both receiving the correct formatting for date in the emails.
 * 
 * The email will go out tpo dev@n4systems.com.
 * 
 * @author dgrainge
 *
 */
public class WEB_706 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String[] emails = { "dev@n4systems.com" };

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}

	private Date setNotification(String user, String password, String tenant) throws Exception {
		helper.setEndUser(false);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
		boolean notify = true;
		String when = "Weekly";
		long oneDay = 24 * 60 * 60 * 1000;
		Date tomorrow = new Date(System.currentTimeMillis() + oneDay);
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		String on = sdf.format(tomorrow);
		int starting = 0;
		int next = 28;
		helper.setNotification(ie, notify, when, on, starting, next, emails);
		return tomorrow;
	}
	public void testEditNotificationSettingsMMDDYY() throws Exception {
		String method = helper.getMethodName();

		try {
			String tenant = "hercules";
			String user = "n4systems";
			String password = "makemore$";

			Date tomorrow = setNotification(user, password, tenant);
			System.out.println("This test case will set up an email notification for " + tomorrow);
			System.out.println("Please check emails to see if the date format in the email is MM/dd/yy");
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testEditNotificationSettingsDDMMYY() throws Exception {
		String method = helper.getMethodName();

		try {
			String tenant = "unilift";
			String user = "n4systems";
			String password = "makemore$";

			Date tomorrow = setNotification(user, password, tenant);
			System.out.println("This test case will set up an email notification for " + tomorrow);
			System.out.println("Please check emails to see if the date format in the email is dd/MM/yy");
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
