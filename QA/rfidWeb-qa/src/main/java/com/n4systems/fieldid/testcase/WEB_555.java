package com.n4systems.fieldid.testcase;

import java.util.Random;

import junit.framework.TestCase;
import watij.runtime.ie.IE;

/**
 * Cannot create customer user, throws null pointer exception.
 * 
 * @author Darrell
 *
 */
public class WEB_555 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setUserName("n4systems");
		helper.setPassword("Xk43g8!@");
		helper.setTenant("brs");
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		ie.maximize();
	}
	
	public void testCreatingACustomerUser() throws Exception {
		Random n = new Random();
		// add a customer or select a customer
		String customerID = "BRS";
		String customerName = "Boise Rigging Supply";
		if(!helper.isCustomer(ie, customerID, customerName))
			helper.addCustomer(ie, customerID, customerName, null, null, null, null, null, null, null, null, null, null);
		else
			helper.gotoShowCustomer(ie, customerID, customerName);
		
		String userID = "deleteme";
		String userEmail = "dev@n4systems.com";
		String password = "Xk43g8!@";
		String verifyPassword = password;

		// if the user exists, pick a different name.
		while(helper.isCustomerUser(ie, customerID, customerName, userID)) {
			userID = userID + n.nextInt();
			userID = userID.substring(0, 15);
		}
		helper.addCustomerUser(ie, customerID, customerName, userID, userEmail, "First", "Last", null, null, null, null, null, password, verifyPassword, true, true);
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
