package com.n4systems.fieldid.testcase;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_580 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String tenant = "hysafe";
	final String user = "n4systems";
	final String password = "makemore$";
	static String assetJobID = null;
	static String assetJobTitle = null;
	static String eventJobID = null;
	static String eventJobTitle = null;
	static String[] users = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setTenant(tenant);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.start(ie, helper.getLoginURL());	// login screen
		ie.maximize();					// maximize window for screen shots
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);	// login
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
			assetJobTitle = helper.generateRandomString(15);
			assetJobID = assetJobTitle;
			helper.addJob(ie, "Asset", assetJobID, assetJobTitle, null, null, null, true, null, null, null, null, null, null, null);
			eventJobTitle = helper.generateRandomString(15);
			eventJobID = eventJobTitle;
			helper.addJob(ie, "Event", eventJobID, eventJobTitle, null, null, null, true, null, null, null, null, null, null, null);
			// must be employee users only getting added to jobs
			users = helper.getListOfUserNames(ie, null, "Employee", false);
		}
	}
	
	public void testAddTwoEmployeesToAnAssetJob() throws Exception {
		String method = helper.getMethodName();

		try {
			Set<String> employees = new TreeSet<String>();
			Random r = new Random();
			int n = r.nextInt(users.length);
			employees.add(users[n]);
			helper.addResourceToJob(ie, assetJobTitle, users[n]);
			n = r.nextInt(users.length);
			employees.add(users[n]);
			helper.addResourceToJob(ie, assetJobTitle, users[n]);
			if(!helper.validateJobAssignedResources(ie, assetJobTitle, employees)) {
				throw new TestCaseFailedException();
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testRemovingEmployeeFromAnAssetJob() throws Exception {
		String method = helper.getMethodName();

		try {
			Random r = new Random();
			Set<String> resources = helper.getResourcesAssignedToJob(ie, assetJobTitle);
			int n = r.nextInt(resources.size());
			if(n > -1) {
				String resource = (String) resources.toArray()[n];
				helper.deleteResourceAssignedToJob(ie, assetJobTitle, resource);
				helper.myWindowCapture(timestamp + "/" + method + "-" + resource + ".png", ie);
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testCannotAddSameEmployeeTwiceToAssetJob() throws Exception {
		String method = helper.getMethodName();

		try {
			if(!helper.validateJobAssignedResourcesDropDown(ie, assetJobTitle)) {
				throw new TestCaseFailedException();
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testAddTwoEmployeesToAnEventJob() throws Exception {
		String method = helper.getMethodName();

		try {
			Set<String> employees = new TreeSet<String>();
			Random r = new Random();
			int n = r.nextInt(users.length);
			employees.add(users[n]);
			helper.addResourceToJob(ie, eventJobTitle, users[n]);
			n = r.nextInt(users.length);
			employees.add(users[n]);
			helper.addResourceToJob(ie, eventJobTitle, users[n]);
			if(!helper.validateJobAssignedResources(ie, eventJobTitle, employees)) {
				throw new TestCaseFailedException();
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testRemovingEmployeeFromAnEventJob() throws Exception {
		String method = helper.getMethodName();

		try {
			Random r = new Random();
			Set<String> resources = helper.getResourcesAssignedToJob(ie, eventJobTitle);
			int n = r.nextInt(resources.size());
			if(n > -1) {
				String resource = (String) resources.toArray()[n];
				helper.deleteResourceAssignedToJob(ie, eventJobTitle, resource);
				helper.myWindowCapture(timestamp + "/" + method + "-" + resource + ".png", ie);
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testCannotAddSameEmployeeTwiceToEventJob() throws Exception {
		String method = helper.getMethodName();

		try {
			if(!helper.validateJobAssignedResourcesDropDown(ie, eventJobTitle)) {
				throw new TestCaseFailedException();
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
