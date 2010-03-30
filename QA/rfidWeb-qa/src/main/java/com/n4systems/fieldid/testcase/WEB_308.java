package com.n4systems.fieldid.testcase;

import java.util.Random;

import junit.framework.TestCase;
import watij.runtime.ie.IE;

/**
 * Ability to create two inspection books with the same name for different customers.
 * 
 * @author Darrell
 *
 */
public class WEB_308 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	Random n = new Random();
	final String tenant = "hysafe";
	final String user = "n4systems";
	final String password = "Xk43g8!@";

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setTenant(tenant);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.start(ie, helper.getLoginURL());	// login screen
		ie.maximize();					// maximize window for screen shots
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);	// login
	}
	
	public void testCreateTwoInspectionBooksForDifferentCustomers() throws Exception {
		String customer1 = "darrell" + n.nextInt();
		String customer2 = "darrell" + n.nextInt();
		String title = "1" + n.nextInt();	// 1 so it will be first on the list and found quickly.
		boolean open = true;
		helper.addCustomer(ie, customer1, customer1, null, null, null, null, null, null, null, null, null, null);
		helper.addCustomer(ie, customer2, customer2, null, null, null, null, null, null, null, null, null, null);
		helper.addInspectionBook(ie, customer1, title, open);
		helper.addInspectionBook(ie, customer2, title, open);
		if(!helper.isInspectionBook(ie, customer1, title)) {
			throw new TestCaseFailedException();
		}
		if(!helper.isInspectionBook(ie, customer2, title)) {
			throw new TestCaseFailedException();
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
