package com.n4systems.fieldid.testcase;

import java.util.List;
import java.util.Random;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_796 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	final String tenant = "hysafe";
	final String user = "n4systems";
	final String password = "makemore$";

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
	
	public void testRemovingAllInspectionTypesFromAProductType() throws Exception {
		String method = helper.getMethodName();

		try {
			Random r = new Random();
			List<String> pts = helper.getProductTypeWithInspectionTypes(ie);
			String productType = null;
			String productID = null;
			String[] productParameters = { "" };
			if(pts != null && pts.size() != 0) {	// if there is something existing use it
				int n = r.nextInt(pts.size());
				productType = pts.get(n);
				productID = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, productType, productParameters, null, null, "Save");
			} else {								// create what I need
				String it = helper.generateRandomString(15);
				String pt = helper.generateRandomString(15);
				helper.addInspectionType(ie, it, "Visual Inspection", false, false, null, null);
				helper.addProductType(ie, pt, null, null, null, false, null, null, null, null, null);
				helper.addInspectionTypeToProductType(ie, it, pt);
				productID = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, productType, productParameters, null, null, "Save");
			}
			helper.deleteAllInspectionTypesFromProductType(ie, productType);
			String[] its = helper.getInspectionTypesForProduct(ie, productID);
			if(its != null && its.length != 0) {
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
