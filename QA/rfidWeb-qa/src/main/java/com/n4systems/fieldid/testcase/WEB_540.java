package com.n4systems.fieldid.testcase;

import java.util.Random;

import junit.framework.TestCase;
import watij.runtime.ie.IE;

/**
 * Grammatical mistake on message given to user after they submit a request for an account.
 * 
 * @author Darrell
 *
 */
public class WEB_540 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testRequestAnAccount() throws Exception {
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setUserName("n4systems");
		helper.setPassword("makemore$");
		helper.setTenant("key");
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		Random n = new Random();
		String userID = "test" + n.nextInt();
		String emailAddress = "darrell.grainger@n4systems.com";
		String firstName = "Darrell";
		String lastName = "Grainger";
		String position = null;
		String timeZone = null;
		String company = "N4 Systems";
		String phone = "416-599-6464";
		String password = "makemore$";
		String verifyPassword = password;
		String comments = null;
		helper.requestAnAccount(ie, userID, emailAddress, firstName, lastName, position, timeZone, company, phone, password, verifyPassword, comments, false, false);
		helper.validateRegisterNewUserPage(ie, userID, emailAddress);
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
