package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * The division drop down is not populated when you attempt to accept a user request for an account.
 * 
 * @author Darrell
 *
 */
public class WEB_584 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	final String tenant = "nischain";
	final String user = "n4systems";
	final String password = "Xk43g8!@";
	final String customer = "Ford Motor Co. (Dearborn Engine)";	// has a division
	final String division = "Ford Dearborn Engine & Fuel Tank: 3001 Miller Road";
	final int maxUserIDLength  = 15;
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.setUserName(user);
		helper.start(ie, helper.getLoginURL());
	}
	
	public void testAcceptingRequestForAccountCustomerWithDivision() throws Exception {
		// request an account
		String userID = helper.generateRandomUserID(maxUserIDLength);
		String emailAddress = "dev@n4systems.com";
		String firstName = "Darrell";
		String lastName = "Grainger";
		String phone = "416-599-6464";
		helper.requestAnAccount(ie, userID, emailAddress, firstName, lastName, null, null, customer, phone, password, password, null, false, false);
		// log in a n4systems
		boolean createInspection = true;
		boolean editInspection = false;
		// TODO: create methods to get organization, customers with divisions and divisions
		String organization = "NIS Chain";
		ie.goTo(helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		helper.acceptAccountRequest(ie, organization, userID, customer, division, createInspection, editInspection);
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}

}
