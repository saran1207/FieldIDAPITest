package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import junit.framework.TestCase;
import static watij.finders.SymbolFactory.*;

/**
 * This test suite will go to various points in the Field ID
 * application and wait for the session to timeout. Currently
 * the session timeout is 30 minutes, so I have each test case
 * wait 31 minutes before checking for the session timeout
 * message. When a session times out the expected behaviour
 * is to pop up a lightbox asking the user to log back in.
 * If they enter a bad password it will prompt them again.
 * 
 * The test suite assumes there are no Internet Explorer
 * windows open and there is definitely no one already logged
 * into Field ID. Currently, if you are logged into Field ID
 * and you open another Internet Explorer, it will automatically
 * log you in. This code assumes you are not logged in.
 * 
 * @author dgrainge
 *
 */
public class WEB_252 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	long timeout = 31*60*1000; 	// How long before a session times out; minutes to seconds to milliseconds
	String png = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setPassword("Xk43g8!@");
		helper.setTenant("cglift");
		helper.setUserName("n4systems");
		helper.start(ie, helper.getLoginURL());	// login screen
		ie.maximize();					// maximize window for screen shots
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);	// login
		helper.stopMonitorStatus();	// disable refresh
	}
	
	protected void lightboxLogin(IE ie) throws Exception {
		helper.isSessionTimeoutLightboxOpen(ie);					// Confirm light box appears
		helper.loginSessionTimeLightbox(ie, "badpassword");			// Enter an incorrect password
		assert(ie.containsText(""));								// confirm the lightbox is re-displayed
		helper.loginSessionTimeLightbox(ie, helper.getPassword());	// Enter correct password
	}

	public void testSessionTimeoutHome() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoHome(ie);			// Go to the Home page
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		helper.validateHomePage(ie);	// Confirm you are on the Home page
	}

	public void testSessionTimeoutIdentify() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoAddProduct(ie);			// Go to the Add Product page
		ie.link(text, "generate").click();	// Generate a serial number
		Thread.sleep(timeout);				// Wait at the page for session timeout
		lightboxLogin(ie);					// log back in
		helper.validateAddProductPage(ie, false, true);	// Confirm you are on the Add Product page
	}

	public void testSessionTimeoutInspect() throws Exception {
		png = helper.getMethodName() + ".png";
		String prodID;
		String inspectType;
		
		// get a product id
		prodID = helper.getProductsFirstPage(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null)[0];
		// get an inspection type
		inspectType = helper.getInspectionTypesForProduct(ie, prodID)[0];
		
		helper.gotoInspectionTypeOnProduct(ie, prodID, inspectType);	// Go to the Inspect Product page
		Thread.sleep(timeout);							// Wait at the page for session timeout
		lightboxLogin(ie);								// log back in
		helper.validateInspectionTypeOnProductPage(ie, prodID, inspectType);	// Confirm you are on the InspectionType - Product page
	}

	public void testSessionTimeoutAssets() throws Exception {
		png = helper.getMethodName() + ".png";
		// TODO
		// Login
		// Go to the Assets page
		// Generate a search result
		// Wait at the Product Search - Results page for 30 minutes
		// Click on one of the product serial numbers (or next page)
		// Confirm light box appears
		// Re-enter password
		// Confirm you are on the correct page
	}

	public void testSessionTimeoutReporting() throws Exception {
		png = helper.getMethodName() + ".png";
		// TODO
		// Login
		// Go to the Reporting page
		// Generate a search result
		// Wait at the Report - Results page for 30 minutes
		// Click on one of the product serial numbers (or next page)
		// Confirm light box appears
		// Re-enter password
		// Confirm you are on the correct page
	}

	public void testSessionTimeoutSchedule() throws Exception {
		png = helper.getMethodName() + ".png";
		// TODO
		// Login
		// Go to the Schedule page
		// Generate a search result
		// Wait at the Inspection Schedule - Results page for 30 minutes
		// Click on one of the product serial numbers (or next page)
		// Confirm light box appears
		// Re-enter password
		// Confirm you are on the correct page
	}
	
	public void testSessionTimeoutAdministration() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoAdministration(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageOrganizationalUnits() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageOrganizationalUnits(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageCustomers() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageCustomers(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageSystemUsers() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageSystemUsers(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageUserRegistrationRequests() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageUserRegistrationRequest(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageProductTypes() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageProductTypes(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageProductStatuses() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageProductStatuses(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageInspectionTypes() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageInspectionTypes(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageInspectionBooks() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageInspectionBooks(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutEditNotificationSettings() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoEditNotificationSettings(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutAutoAttributesWizard() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoAutoAttributesWizard(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageCommentTemplates() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageCommentTemplates(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutDataLog() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoDataLog(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	public void testSessionTimeoutManageYourSafetyNetwork() throws Exception {
		png = helper.getMethodName() + ".png";
		helper.gotoManageYourSafetyNetwork(ie);
		Thread.sleep(timeout);			// Wait at Home page for session timeout
		lightboxLogin(ie);				// log back in
		// TODO: Confirm you are on the right page
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		/**
		 * Set the name of the file to be the name of the test case method.
		 * At the end of each test case, this will take a snapshot of the
		 * browser just before it closes it.
		 */
		helper.myWindowCapture(png, ie);
		ie.close();
	}
}
