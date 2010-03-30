package com.n4systems.fieldid.testcase;

import java.util.Random;

import junit.framework.TestCase;
import watij.runtime.ie.IE;

/**
 * Exception when deleting customer.
 * 
 * @author Darrell
 *
 */
public class WEB_554 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	Random r = new Random();
	int n = r.nextInt();
	String userID = "test" + n;
	String firstName = "First" + n;
	String lastName = "Last" + n;
	String userName = firstName + " " + lastName;
	String userEmail = "dev@n4systems.com";
	String password = "Xk43g8!@";
	String customerID = "bob";
	String customerName = "Bob Newhart";
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setUserName("n4systems");
		helper.setPassword("Xk43g8!@");
		helper.setTenant("unirope");
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		ie.maximize();
		if(!helper.isCustomer(ie, customerID, customerName)) {
			helper.addCustomer(ie, customerID, customerName, null, null, null, null, null, null, null, null, null, null);
		}
		helper.gotoShowCustomer(ie, customerID, customerName);
		helper.addCustomerUser(ie, customerID, customerName, userID, userEmail, firstName, lastName, null, null, null, null, null, password, password, true, true);
	}
	
	public void testDeletingCustomerUser() throws Exception {
		helper.deleteCustomerUser(ie, customerID, customerName, userID);
	}
	
	public void testDeletingCustomerWithUsers() throws Exception {
		helper.deleteCustomer(ie, customerID, customerName);
	}
	
	public void testDeletingCustomerUserWithInspections() throws Exception {
		// TODO: Assumes these product type and inspection type exists.
		String productType = "Standard Wire Rope";
		String inspectionTypeName = "Proof Test-150K Test Bed";
		String serialNumber = helper.addProduct(ie, null, true, null, null, customerName, null, null, null, null, null, null, null, null, productType, null, null, null, "Save");
		helper.addInspectionToProduct(ie, inspectionTypeName, serialNumber, false);
		helper.deleteCustomerUser(ie, customerID, customerName, userID);
	}
	
	public void testDeletingCustomerWithUsersWithInspections() throws Exception {
		String productType = "Standard Wire Rope";
		String inspectionTypeName = "Proof Test-150K Test Bed";
		String serialNumber = helper.addProduct(ie, null, true, null, null, customerName, null, null, null, null, null, null, null, null, productType, null, null, null, "Save");
		helper.addInspectionToProduct(ie, inspectionTypeName, serialNumber, false);
		helper.deleteCustomer(ie, customerID, customerName);
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
