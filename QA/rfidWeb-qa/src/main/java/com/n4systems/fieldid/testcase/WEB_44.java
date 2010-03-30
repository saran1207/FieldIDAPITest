package com.n4systems.fieldid.testcase;

import static watij.finders.FinderFactory.text;
import junit.framework.TestCase;
import watij.runtime.ie.IE;

/**
 * Ability to print multiple certificates of conformance (manufacturer certificates)
 * 
 * @author Darrell
 *
 */
public class WEB_44 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	
	protected void setUp() throws Exception {
		super.setUp();
 		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.start(ie);
	}
	
	public void login(IE ie, String tenant, String user, String password) throws Exception {
		helper.setUserName(user);
		helper.setPassword(password);
		helper.setTenant(tenant);
		ie.goTo(helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
		ie.waitUntilReady();
	}
	
	public void testPrintMultipleManufacturerCertificates() throws Exception {
		String[] tenant = { 	// How many manufacturer certs in our system on 12-Feb-2009
				"brs",			// 11
				"elko",			// 11
				"halo",			// 2144
				"hercules",		// 27841
				"hysafe",		// 0
				"johnsakach",	// 29
				"marine",		// 120
				"nischain",		// 4
				"PeakWorks",	// 2032
				"swwr",			// 226
				"unilift",		// 10669
				};
		String user = "n4systems";
		String password = "Xk43g8!@";
		for(int t = 0; t < tenant.length; t++) {
			login(ie, tenant[t], user, password);
			helper.productSearch(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			if(ie.link(text("Print all manufacturer certificates")).exists())
				helper.printAllManufacturerCertificates(ie);
			helper.logout(ie);
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}

}
