package com.n4systems.fieldid.testcase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import watij.runtime.ie.IE;
import static watij.finders.FinderFactory.*;

/**
 * This smoke test should be run after each new deployment. It will log in using
 * the n4systems user, create users with varying permissions, divisions, etc.
 * for a fictitious customer. It will then add products for the different users.
 * 
 * The next test cases will log in as the different users and make sure they can
 * only do the activities for their permission levels plus that they cab only
 * see the products and inspections created for them.
 * 
 * This code assumes you are using a customer WITHOUT job sites. When written
 * the only customer using job sites did not have end users and therefore what
 * this is testing for was not an issue.
 * 
 * It was originally written for a tenant with integration but it should work
 * for non-integration customers as well.
 * 
 * The test suite must be run as a whole. The first few test cases will create
 * products and save the product information into static class variables. The
 * subsequent test cases will use that product information to look up, edit and
 * inspect the products.
 * 
 * Most test cases requires you to manually check the screen shot in the timestamp
 * directory. If the test case fails there will be a file starting with the text
 * FAILURE- followed by the name of the method which failed. For example, if the
 * test case 'testCreateListAssets' fails the file named 'FAILURE-testCreateListAssets.png"
 * will exist.
 * 
 * @author Darrell Grainger
 * 
 */
public class SmokeTest extends TestCase {

	static boolean debug = true;	// set to true if you want progress to print out
	final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ");
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	// If you change the tenant you might have to update the proofType array below.
	final String tenant = "lcrane";
	// we don't want too many manufacturer's certificates. This variable will
	// limit which product we search for thus reducing the number of products.
	final String limitingProductType = "";
	final String masterProductType = "master";
	final String subProductType = "sub";
	final String user = "n4systems";
	final String password = "Xk43g8!@";
	static boolean initialized = false;
	static String timestamp = null;
	static long dayInMilliseconds = 86400000;	// used for scheduling inspections

	/**
	 * I have edit the C:\Windows\system32\drivers\etc\hosts file so
	 * localhost.localdomain equals the IP address of the machine you want to test
	 * against. The SSL certificate being sent by localhost.localdomain and team have been signed
	 * as localhost.localdomain so this trick will allow you to accept the SSL
	 * certificate. Without this, Internet Explorer will complain the site is not
	 * a trusted site.
	 * 
	 * The timestamp variable is used to create a directory. In this directory will
	 * be all the screen shots for the current run. If you run the test cases twice
	 * you will have all the screen shots for both runs.
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("http://team.n4systems.net/fieldid/");

		// create a new directory for each test run but not for each test case
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
			if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: createTimestampDirectory");
			if(createUser.length() > limit)
				createUser = createUser.substring(0, limit);
			if(editorUser.length() > limit)
				editorUser = editorUser.substring(0, limit);
			if(bothUser.length() > limit)
				bothUser = bothUser.substring(0, limit);
			if(neitherUser.length() > limit)
				neitherUser = neitherUser.substring(0, limit);
			if(adminUser.length() > limit)
				adminUser = adminUser.substring(0, limit);
			if(createDivision.length() > limit)
				createDivision = createDivision.substring(0, limit);
			if(editorDivision.length() > limit)
				editorDivision = editorDivision.substring(0, limit);
			if(bothDivision.length() > limit)
				bothDivision = bothDivision.substring(0, limit);
			if(neitherDivision.length() > limit)
				neitherDivision = neitherDivision.substring(0, limit);
		}
	}

	// fake customer and users
	static Random r = new Random();
	static int limit = 15; // limit for field size on things like user name
	static String n = new String(Integer.toString(Math.abs(r.nextInt())));
	static String customerID = "smoke";
	static String customerName = "Smoke Test";
	static String createUser = new String("create-" + n);
	static String editorUser = new String("editor-" + n);
	static String bothUser = new String("both-" + n);
	static String neitherUser = new String("neither-" + n);
	static String adminUser = new String("admin-" + n);
	static String createDivision = new String("create-div-" + n);
	static String editorDivision = new String("editor-div" + n);
	static String bothDivision = new String("both-div-" + n);
	static String neitherDivision = new String("neither-div-" + n);
	static String adminDivision = "";
	static String userEmail = "dev@n4systems.com";
	static String createFirstName = "Create-" + n;
	static String editorFirstName = "Editor-" + n;
	static String bothFirstName = "Both-" + n;
	static String neitherFirstName = "Neither-" + n;
	static String adminFirstName = "Admin-" + n;
	static String createLastName = "User";
	static String editorLastName = "User";
	static String bothLastName = "User";
	static String neitherLastName = "User";
	static String adminLastName = "User";

	/**
	 * Creates the customer Smoke Test, four divisions and five users. The five users are
	 * create, editor, both, neither and admin. Their divisions are create-division,
	 * editor-division, both-division, neither-division and admin has no division assigned.
	 * Each user has different permissions. The create user has Create Inspection, editor
	 * has Edit Inspection, both & admin have Create and Edit Inspection and neither has
	 * no permissions.
	 * 
	 * @throws Exception
	 */
	private void createCustomerAndUsers() throws Exception {
		// Create customer if they don't already exist
		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + customerName + "' customer.");
		if (!helper.isCustomer(ie, customerID, customerName))
			helper.addCustomer(ie, customerID, customerName, null, null, null, null, null, null, null, null, null, null);

		// Create divisions if they don't already exist
		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + createDivision + "' division.");
		if (!helper.isCustomerDivision(ie, customerID, customerName, createDivision))
			helper.addDivison(ie, customerID, customerName, createDivision);

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + editorDivision + "' division.");
		if (!helper.isCustomerDivision(ie, customerID, customerName, editorDivision))
			helper.addDivison(ie, customerID, customerName, editorDivision);
		
		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + bothDivision + "' division.");
		if (!helper.isCustomerDivision(ie, customerID, customerName, bothDivision))
			helper.addDivison(ie, customerID, customerName, bothDivision);
		
		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + neitherDivision + "' division.");
		if (!helper.isCustomerDivision(ie, customerID, customerName, neitherDivision))
			helper.addDivison(ie, customerID, customerName, neitherDivision);

		// Create users if they don't exist, edit them if they do exist
		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + createUser + "' user.");
		if (!helper.isCustomerUser(ie, customerID, customerName, createUser)) {
			helper.addCustomerUser(ie, customerID, customerName, createUser, userEmail, createFirstName, createLastName, "", "", "", null, createDivision, password, password, true, false);
		} else {
			helper.editCustomerUser(ie, customerID, customerName, createUser, userEmail, createFirstName, createLastName, "", "", "", null, createDivision, true, false);
		}

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + editorUser + "' user.");
		if (!helper.isCustomerUser(ie, customerID, customerName, editorUser)) {
			helper.addCustomerUser(ie, customerID, customerName, editorUser, userEmail, editorFirstName, editorLastName, "", "", "", null, editorDivision, password, password, false, true);
		} else {
			helper.editCustomerUser(ie, customerID, customerName, editorUser, userEmail, editorFirstName, editorLastName, "", "", "", null, editorDivision, false, true);
		}

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + bothUser + "' user.");
		if (!helper.isCustomerUser(ie, customerID, customerName, bothUser)) {
			helper.addCustomerUser(ie, customerID, customerName, bothUser, userEmail, bothFirstName, bothLastName, "", "", "", null, bothDivision, password, password, true, true);
		} else {
			helper.editCustomerUser(ie, customerID, customerName, bothUser,
					userEmail, bothFirstName, bothLastName, "", "", "", null, bothDivision, true, true);
		}

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + neitherUser + "' user.");
		if (!helper.isCustomerUser(ie, customerID, customerName, neitherUser)) {
			helper.addCustomerUser(ie, customerID, customerName, neitherUser, userEmail, neitherFirstName, neitherLastName, "", "", "", null, neitherDivision, password, password, false, false);
		} else {
			helper.editCustomerUser(ie, customerID, customerName, neitherUser, userEmail, neitherFirstName, neitherLastName, "", "", "", null, neitherDivision, false, false);
		}

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + adminUser + "' user.");
		if (!helper.isCustomerUser(ie, customerID, customerName, adminUser)) {
			helper.addCustomerUser(ie, customerID, customerName, adminUser, userEmail, adminFirstName, adminLastName, "", "", "", null, "", password, password, true, true);
		} else {
			helper.editCustomerUser(ie, customerID, customerName, adminUser, userEmail, adminFirstName, adminLastName, "", "", "", null, "", true, true);
		}
	}

	// fake product types and products
	final String masterInspection = "Master Visual Inspection";
	final String inspectionGroup = "Visual Inspection";
	final boolean printable = true;
	final boolean master = true;
	final String[] proofTestType = { "ROBERTS", "NATIONALAUTOMATION", "CHANT", "WIROP", "OTHER" };
	final String[] attributes = null;

	/**
	 * Creates inspection types, master products and sub-products for the different
	 * end users to use. Assigns existing proof test types to the inspections.
	 * 
	 * @throws Exception
	 */
	private void createProductTypesAndProducts() throws Exception {
		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating '" + masterInspection + "' inspection type.");
		if (!helper.isInspectionType(ie, masterInspection))
			helper.addInspectionType(ie, masterInspection, inspectionGroup, printable, master, proofTestType, attributes);
		else
			helper.editInspectionType(ie, masterInspection, inspectionGroup, printable, master, proofTestType, attributes);

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: linking master and sub product types.");
		helper.setupMasterAndSubProductTypes(ie, masterInspection);
	}

	static String createProductMasterSerialNumber = null;
	static String createProductSubSerialNumber = null;
	static String editorProductMasterSerialNumber = null;
	static String editorProductSubSerialNumber = null;
	static String bothProductMasterSerialNumber = null;
	static String bothProductSubSerialNumber = null;
	static String neitherProductMasterSerialNumber = null;
	static String neitherProductSubSerialNumber = null;
	static String adminProductMasterSerialNumber = null;
	static String adminProductSubSerialNumber = null;

	/**
	 * If a test case goes wrong it is possible products created in previous test cases
	 * were needed for the current test case. This method will dump the various serial
	 * numbers once they have been created. If a subsequent test case assumes the serial
	 * number has been assigned, you can edit the current test case. For example, if the
	 * current test case is going to attempt an inspection on the bothProductMasterSerialNumber
	 * product, rather than run the createProducts and the current test case, you can edit
	 * the current test case by adding:
	 * 
	 * 		bothProductMasterSerialNumber = <whatever number this method spits out>;
	 * 
	 * then just run the current test case again.
	 * 
	 * @throws Exception
	 */
	private void dumpSerialNumbers() throws Exception {
		System.out.println(" create Master: " + createProductMasterSerialNumber);
		System.out.println("    create Sub: " + createProductSubSerialNumber);
		System.out.println(" editor Master: " + editorProductMasterSerialNumber);
		System.out.println("    editor Sub: " + editorProductSubSerialNumber);
		System.out.println("   both Master: " + bothProductMasterSerialNumber);
		System.out.println("      both Sub: " + bothProductSubSerialNumber);
		System.out.println("neither Master: " + neitherProductMasterSerialNumber);
		System.out.println("   neither Sub: " + neitherProductSubSerialNumber);
		System.out.println("  admin Master: " + adminProductMasterSerialNumber);
		System.out.println("     admin Sub: " + adminProductSubSerialNumber);
	}
	
	/**
	 * Creates a set of products for the different end users to identify, inspection, edit, etc.
	 * 
	 * @throws Exception
	 */
	private void createProducts() throws Exception {
		// create a master product, a sub product and link them
		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating master product, sub product and linking them for '" + createDivision + "'.");
		createProductMasterSerialNumber = helper.addProduct(ie, null, true, "", "", customerName, createDivision, null, null, "", null, "", null, null, masterProductType, null, null, null, "Save");
		createProductSubSerialNumber = helper.addProduct(ie, null, true, null, null, customerName, createDivision, null, null, "", null, "", null, null, subProductType, null, null, null, "Save");
		helper.addSubProductToMasterProduct(ie, createProductSubSerialNumber, subProductType, createProductMasterSerialNumber, "createSub");

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating master product, sub product and linking them for '" + editorDivision + "'.");
		editorProductMasterSerialNumber = helper.addProduct(ie, null, true, "", "", customerName, editorDivision, null, null, "", null, "", null, null, masterProductType, null, null, null, "Save");
		editorProductSubSerialNumber = helper.addProduct(ie, null, true, null, null, customerName, editorDivision, null, null, "", null, "", null, null, subProductType, null, null, null, "Save");
		helper.addSubProductToMasterProduct(ie, editorProductSubSerialNumber, subProductType, editorProductMasterSerialNumber, "editorSub");

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating master product, sub product and linking them for '" + bothDivision + "'.");
		bothProductMasterSerialNumber = helper.addProduct(ie, null, true, "", "", customerName, bothDivision, null, null, "", null, "", null, null, masterProductType, null, null, null, "Save");
		bothProductSubSerialNumber = helper.addProduct(ie, null, true, null, null, customerName, bothDivision, null, null, "", null, "", null, null, subProductType, null, null, null, "Save");
		helper.addSubProductToMasterProduct(ie, bothProductSubSerialNumber, subProductType, bothProductMasterSerialNumber, "bothSub");

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating master product, sub product and linking them for '" + neitherDivision + "'.");
		neitherProductMasterSerialNumber = helper.addProduct(ie, null, true, "", "", customerName, neitherDivision, null, null, "", null, "", null, null, masterProductType, null, null, null, "Save");
		neitherProductSubSerialNumber = helper.addProduct(ie, null, true, null, null, customerName, neitherDivision, null, null, "", null, "", null, null, subProductType, null, null, null, "Save");
		helper.addSubProductToMasterProduct(ie, neitherProductSubSerialNumber, subProductType, neitherProductMasterSerialNumber, "neitherSub");

		if(debug)	System.err.println(sdf.format(new Date()) + "DEBUG: creating master product, sub product and linking them for '" + adminDivision + "'.");
		adminProductMasterSerialNumber = helper.addProduct(ie, null, true, "", "", customerName, adminDivision, null, null, "", null, "", null, null, masterProductType, null, null, null, "Save");
		adminProductSubSerialNumber = helper.addProduct(ie, null, true, null, null, customerName, adminDivision, null, null, "", null, "", null, null, subProductType, null, null, null, "Save");
		helper.addSubProductToMasterProduct(ie, adminProductSubSerialNumber, subProductType, adminProductMasterSerialNumber, "adminSub");
		
		dumpSerialNumbers();	// for debug purposes
	}
	
	private void loginCreate() throws Exception {
		helper.setEndUser(false);
		helper.setUserName(createUser);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
		ie.waitUntilReady();
	}

	private void loginEditor() throws Exception {
		helper.setEndUser(false);
		helper.setUserName(editorUser);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
		ie.waitUntilReady();
	}

	private void loginBoth() throws Exception {
		helper.setEndUser(false);
		helper.setUserName(bothUser);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
		ie.waitUntilReady();
	}

	private void loginNeither() throws Exception {
		helper.setEndUser(false);
		helper.setUserName(neitherUser);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
		ie.waitUntilReady();
	}

	private void loginAdmin() throws Exception {
		helper.setEndUser(false);
		helper.setUserName(adminUser);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
		ie.waitUntilReady();
	}

	private void loginN4Systems() throws Exception {
		helper.setEndUser(false);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
		ie.waitUntilReady();
	}

	/**
	 * This test case must be run first. If it fails the other test cases will
	 * fail as well. This test case will exercise the following:
	 * 
	 * Logging in
	 * Creating Customer
	 * Creating Divisions
	 * Creating Customer Users
	 * Creating/Editing Product Types
	 * Creating/Editing Inspection Types
	 * Add Product
	 * Product Configuration (linking sub-products to master products)
	 * 
	 * @throws Exception
	 */
	public void testSystemUserLogin() throws Exception {
		String method = helper.getMethodName();
		helper.setEndUser(false);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.myWindowCapture(timestamp + "/" + method + "-LoginPage.png", ie);
		helper.loginBrandedDefaultRegular(ie, false);
		ie.waitUntilReady();

		// Check that the Administration icon is available
		helper.myWindowCapture(timestamp + "/" + method + "-LoggedIn.png", ie);
		assertTrue(helper.isAdministration(ie));

		try {
			createCustomerAndUsers();
			createProductTypesAndProducts();
			createProducts();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Log in as n4systems and make sure the links on the home page work okay.
	 * Create snapshots of the pages linked to the Home page. Should be checked
	 * manually to ensure we are not seeing too much or not enough.
	 * 
	 * @throws Exception
	 */
	public void testN4SystemsUserHomePage() throws Exception {
		String method = helper.getMethodName();
		loginN4Systems();

		try {
			helper.myWindowCapture(timestamp + "/" + method + "-Home-Page.png", ie);
			helper.validateHomePage(ie, timestamp + "/" + helper.getMethodName());
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Log in as create, an end user with only create inspection permission. We
	 * make sure the links on the home page work okay.
	 * Create snapshots of the pages linked to the Home page. Should be checked
	 * manually to ensure we are not seeing too much or not enough.
	 * 
	 * @throws Exception
	 */
	public void testCreateUserHomePage() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();
	
			helper.myWindowCapture(timestamp + "/" + method + "-Home-Page.png", ie);
			helper.validateHomePage(ie, timestamp + "/" + helper.getMethodName());
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Log in as editor, an end user with only edit inspection permission. We
	 * make sure the links on the home page work okay.
	 * Create snapshots of the pages linked to the Home page. Should be checked
	 * manually to ensure we are not seeing too much or not enough.
	 * 
	 * @throws Exception
	 */
	public void testEditorUserHomePage() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();
	
			helper.myWindowCapture(timestamp + "/" + method + "-Home-Page.png", ie);
			helper.validateHomePage(ie, timestamp + "/" + helper.getMethodName());
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Log in as both, an end user with create and edit inspection permission. We
	 * make sure the links on the home page work okay.
	 * Create snapshots of the pages linked to the Home page. Should be checked
	 * manually to ensure we are not seeing too much or not enough.
	 * 
	 * @throws Exception
	 */
	public void testBothUserHomePage() throws Exception {
		String method = helper.getMethodName();
		try {
			loginBoth();
	
			helper.myWindowCapture(timestamp + "/" + method + "-Home-Page.png", ie);
			helper.validateHomePage(ie, timestamp + "/" + helper.getMethodName());
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Log in as neither, an end user with no inspection permission. We
	 * make sure the links on the home page work okay.
	 * Create snapshots of the pages linked to the Home page. Should be checked
	 * manually to ensure we are not seeing too much or not enough.
	 * 
	 * @throws Exception
	 */
	public void testNeitherUserHomePage() throws Exception {
		String method = helper.getMethodName();
		try {
			loginNeither();
	
			helper.myWindowCapture(timestamp + "/" + method + "-Home-Page.png", ie);
			helper.validateHomePage(ie, timestamp + "/" + helper.getMethodName());
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Login as create and list assets. Should only see the assets for this user.
	 * 
	 * @throws Exception
	 */
	public void testCreateListAssets() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();

			helper.gotoProductSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setProductSearchResultsColumns(ie, true, false, true, true, true, false, true, false, true, true, true, false, false, true, false, false);
			helper.myWindowCapture(timestamp + "/" + method + "-ProductSearchResultsPage.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Login as editor and list assets. Should only see the assets for this user.
	 * 
	 * @throws Exception
	 */
	public void testEditorListAssets() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();
	
			helper.gotoProductSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setProductSearchResultsColumns(ie, true, false, true, true, true, false, true, false, true, true, true, false, false, true, false, false);
			helper.myWindowCapture(timestamp + "/" + method + "-ProductSearchResultsPage.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Login as both and list assets. Should only see the assets for this user.
	 * 
	 * @throws Exception
	 */
	public void testBothListAssets() throws Exception {
		String method = helper.getMethodName();
		try {
			loginBoth();
	
			helper.gotoProductSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setProductSearchResultsColumns(ie, true, false, true, true, true, false, true, false, true, true, true, false, false, true, false, false);
			helper.myWindowCapture(timestamp + "/" + method + "-ProductSearchResultsPage.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Login as neither and list assets. Should only see the assets for this user.
	 * 
	 * @throws Exception
	 */
	public void testNeitherListAssets() throws Exception {
		String method = helper.getMethodName();
		try {
			loginNeither();
	
			helper.gotoProductSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setProductSearchResultsColumns(ie, true, false, true, true, true, false, true, false, true, true, true, false, false, true, false, false);
			helper.myWindowCapture(timestamp + "/" + method + "-ProductSearchResultsPage.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Login as admin and list assets. Should see the assets for this customer.
	 * Will include the assets for create, editor, both and neither.
	 * 
	 * @throws Exception
	 */
	public void testAdminListAssets() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();
	
			helper.gotoProductSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setProductSearchResultsColumns(ie, true, false, true, true, true, false, true, false, true, true, true, false, false, true, false, false);
			helper.myWindowCapture(timestamp + "/" + method + "-ProductSearchResultsPage.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * The users are all end users. They should not have the ability to edit a product.
	 * This confirms that the end user create cannot edit the product.
	 *  
	 * @throws Exception
	 */
	public void testCreateFailingToEditProduct() throws Exception {
		String method = helper.getMethodName();
		boolean result;
		
		try {
			loginCreate();
	
			result = helper.isEditProduct(ie, createProductSubSerialNumber); 
			helper.myWindowCapture(timestamp + "/" + method + "-No-Edit-Product-Link.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
		
		if(result) {
			throw new TestCaseFailedException();
		}
	}
	
	/**
	 * The users are all end users. They should not have the ability to edit a product.
	 * This confirms that the end user editor cannot edit the product.
	 *  
	 * @throws Exception
	 */
	public void testEditorFailingToEditProduct() throws Exception {
		String method = helper.getMethodName();
		boolean result;
		try {
			loginEditor();
	
			result = helper.isEditProduct(ie, editorProductSubSerialNumber); 
			helper.myWindowCapture(timestamp + "/" + method + "-No-Edit-Product-Link.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}

		if(result) {
			throw new TestCaseFailedException();
		}
	}
	
	/**
	 * The users are all end users. They should not have the ability to edit a product.
	 * This confirms that the end user both cannot edit the product.
	 *  
	 * @throws Exception
	 */
	public void testBothFailingToEditProduct() throws Exception {
		String method = helper.getMethodName();
		boolean result;
		try {
			loginBoth();
	
			result = helper.isEditProduct(ie, bothProductSubSerialNumber); 
			helper.myWindowCapture(timestamp + "/" + method + "-No-Edit-Product-Link.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}

		if(result) {
			throw new TestCaseFailedException();
		}
	}
	
	/**
	 * The users are all end users. They should not have the ability to edit a product.
	 * This confirms that the end user neither cannot edit the product.
	 *  
	 * @throws Exception
	 */
	public void testNeitherFailingToEditProduct() throws Exception {
		String method = helper.getMethodName();
		boolean result;
		try {
			loginNeither();
	
			result = helper.isEditProduct(ie, neitherProductSubSerialNumber); 
			helper.myWindowCapture(timestamp + "/" + method + "-No-Edit-Product-Link.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}

		if(result) {
			throw new TestCaseFailedException();
		}
	}
	
	/**
	 * The users are all end users. They should not have the ability to edit a product.
	 * This confirms that the end user admin cannot edit the product.
	 *  
	 * @throws Exception
	 */
	public void testAdminFailingToEditProduct() throws Exception {
		String method = helper.getMethodName();
		boolean result;
		try {
			loginAdmin();
	
			result = helper.isEditProduct(ie, adminProductSubSerialNumber); 
			helper.myWindowCapture(timestamp + "/" + method + "-No-Edit-Product-Link.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}

		if(result) {
			throw new TestCaseFailedException();
		}
	}
	
	/**
	 * Have create find and inspect the product identified for their customer and
	 * division.
	 * 
	 * @throws Exception
	 */
	public void testCreateInspectingAProduct() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();
			helper.addInspectionToProduct(ie, masterInspection, createProductMasterSerialNumber, master);
			ie.waitUntilReady();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
		helper.myWindowCapture(timestamp + "/" + method + "-Saved-Inspection-Page.png", ie);
	}
	
	/**
	 * Have both find and inspect the product identified for their customer and
	 * division.
	 * 
	 * @throws Exception
	 */
	public void testBothInspectingAProduct() throws Exception {
		String method = helper.getMethodName();
		try {
			loginBoth();
			
			helper.addInspectionToProduct(ie, masterInspection, bothProductMasterSerialNumber, master);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Saved-Inspection-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Have admin find and inspect the product identified for their customer and
	 * editor's division. Needed for when editor attempts to modify its inspection.
	 * 
	 * @throws Exception
	 */
	public void testAdminInspectingEditorProduct() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();
			
			helper.addInspectionToProduct(ie, masterInspection, editorProductMasterSerialNumber, master);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Saved-Inspection-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Have admin find and inspect the product identified for their customer and
	 * neither's division. Needed for when neither attempts to modify its inspection.
	 * 
	 * @throws Exception
	 */
	public void testAdminInspectingNeitherProduct() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();
			
			helper.addInspectionToProduct(ie, masterInspection, neitherProductMasterSerialNumber, master);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Saved-Inspection-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Editor attempts to create an inspection. This should fail since editor does
	 * not have create inspection permission.
	 * Check the screen shot to confirm there is no option to start an inspection.
	 * 
	 * @throws Exception
	 */
	public void testEditorFailingtoInspectAProduct() throws Exception {
		String method = helper.getMethodName();
		boolean result;
		try {
			loginEditor();
			
			result = helper.isInspectionProduct(ie, editorProductMasterSerialNumber);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Cannot-Inspect-Inspection-Group-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
		
		if(result) {
			throw new TestCaseFailedException();
		}
	}
	
	/**
	 * Neither attempts to create an inspection. This should fail since neither does
	 * not have create inspection permission.
	 * Check the screen shot to confirm there is no option to start an inspection.
	 * 
	 * @throws Exception
	 */
	public void testNeitherFailingtoInspectAProduct() throws Exception {
		String method = helper.getMethodName();
		boolean result;
		try {
			loginNeither();
			
			result = helper.isInspectionProduct(ie, neitherProductMasterSerialNumber);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Cannot-Inspect-Inspection-Group-Page.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}

		if(result) {
			throw new TestCaseFailedException();
		}
	}
	
	/**
	 * Editor edits an inspection created by admin in a previous test case.
	 * This assumes that test case testAdminInspectingEditorProduct passed.
	 * 
	 * @throws Exception
	 */
	public void testEditorEditingAnInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();
			
			String inspectionDate = null;
			String inspector = editorFirstName + " " + editorLastName;
			String jobSite = null;
			String customer = customerName;
			String division = editorDivision;
			String location = null;
			String inspectionBook = null;
			String result = null;
			String comments = "This inspection was edited by " + editorUser;
			String charge = "20.00";
			boolean print = false;
			int whichInspection = 0;
	
			helper.editInspection(ie, editorProductMasterSerialNumber, whichInspection, inspectionDate, inspector, jobSite, customer, division, location, inspectionBook, result, comments, print, charge);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Saved-Edited-Inspection.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Both edits their inspection.
	 * 
	 * @throws Exception
	 */
	public void testBothEditingAnInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginBoth();
			
			String inspectionDate = null;
			String inspector = bothFirstName + " " + bothLastName;
			String jobSite = null;
			String customer = customerName;
			String division = bothDivision;
			String location = null;
			String inspectionBook = null;
			String result = null;
			String comments = "This inspection was edited by " + bothUser;
			String charge = "20.00";
			boolean print = false;
			int whichInspection = 0;
	
			helper.editInspection(ie, bothProductMasterSerialNumber, whichInspection, inspectionDate, inspector, jobSite, customer, division, location, inspectionBook, result, comments, print, charge);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Saved-Edited-Inspection.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Create attempts to edit an inspection. This should not be possible.
	 * Check the screen shot to confirm it was not possible.
	 * 
	 * @throws Exception
	 */
	public void testCreateFailingToEditAnInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();
			
			if(helper.isInspectionGroupEditable(ie, createProductMasterSerialNumber))
				throw new TestCaseFailedException();
			
			helper.myWindowCapture(timestamp + "/" + method + "-Saved-Edited-Inspection.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Neither attempts to edit an inspection. This should not be possible.
	 * Check the screen shot to confirm it was not possible. This assumes
	 * that admin has created an inspection for neither to attempt to inspect.
	 * See the test case testAdminInspectingNeitherProduct.
	 * 
	 * @throws Exception
	 */
	public void testNeitherFailingToEditAnInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginNeither();
			
			if(helper.isInspectionGroupEditable(ie, neitherProductMasterSerialNumber))
				throw new TestCaseFailedException();
			
			helper.myWindowCapture(timestamp + "/" + method + "-Saved-Edited-Inspection.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Go to Reporting, search for all products, select all columns and update
	 * the results. Check the screen capture to make sure it was successful.
	 * 
	 * @throws Exception
	 */
	public void testCreateReportingWithAllColumnsSelected() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Go to Reporting, search for all products, select all columns and update
	 * the results. Check the screen capture to make sure it was successful.
	 * 
	 * @throws Exception
	 */
	public void testEditorReportingWithAllColumnsSelected() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Go to Reporting, search for all products, select all columns and update
	 * the results. Check the screen capture to make sure it was successful.
	 * 
	 * @throws Exception
	 */
	public void testBothReportingWithAllColumnsSelected() throws Exception {
		String method = helper.getMethodName();
		try {
			loginBoth();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Go to Reporting, search for all products, select all columns and update
	 * the results. Check the screen capture to make sure it was successful.
	 * 
	 * @throws Exception
	 */
	public void testNeitherReportingWithAllColumnsSelected() throws Exception {
		String method = helper.getMethodName();
		try {
			loginNeither();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Go to Reporting, search for all products, select all columns and update
	 * the results. Check the screen capture to make sure it was successful.
	 * 
	 * @throws Exception
	 */
	public void testAdminReportingWithAllColumnsSelected() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * The create user edits the schedule for inspecting their product.
	 * 
	 * @throws Exception
	 */
	public void testCreateEditScheduleForInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();

			SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
			Date d = new Date(System.currentTimeMillis() + (7*dayInMilliseconds));	// one week from today
			String date = now.format(d);
			helper.addInspectionSchedule(ie, createProductMasterSerialNumber, masterInspection, date, null);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Schedule-Inspections-For-" + date.replaceAll("/", "-") + ".png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * The editor user attempts to edits the schedule for inspecting their product.
	 * You need Create Inspection permission to edit the schedule so this should not
	 * work. Check the screen shot to confirm.
	 * 
	 * @throws Exception
	 */
	public void testEditorEditScheduleForInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();

			SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
			Date d = new Date(System.currentTimeMillis() + (7*dayInMilliseconds));	// one week from today
			String date = now.format(d);
			// If you do not have Create Inspection permission, you cannot edit schedule
			if(helper.isScheduleEditable(ie, editorProductMasterSerialNumber, masterInspection)) {
				throw new TestCaseFailedException();
			}
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Schedule-Inspections-For-" + date.replaceAll("/", "-") + ".png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * The both user edits the schedule for inspecting their product.
	 * 
	 * @throws Exception
	 */
	public void testBothEditScheduleForInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginBoth();

			SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
			Date d = new Date(System.currentTimeMillis() + (7*dayInMilliseconds));	// one week from today
			String date = now.format(d);
			helper.addInspectionSchedule(ie, bothProductMasterSerialNumber, masterInspection, date, null);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Schedule-Inspections-For-" + date.replaceAll("/", "-") + ".png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * The neither user attempts to edits the schedule for inspecting their product.
	 * You need Create Inspection permission to edit the schedule so this should not
	 * work. Check the screen shot to confirm.
	 * 
	 * @throws Exception
	 */
	public void testNeitherEditScheduleForInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginNeither();

			SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
			Date d = new Date(System.currentTimeMillis() + (7*dayInMilliseconds));	// one week from today
			String date = now.format(d);
			// If you do not have Create Inspection permission, you cannot edit schedule
			if(helper.isScheduleEditable(ie, neitherProductMasterSerialNumber, masterInspection)) {
				throw new TestCaseFailedException();
			}
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Schedule-Inspections-For-" + date.replaceAll("/", "-") + ".png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * The admin user edits the schedule for inspecting their product.
	 * 
	 * @throws Exception
	 */
	public void testAdminEditScheduleForInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
			Date d = new Date(System.currentTimeMillis() + (7*dayInMilliseconds));	// one week from today
			String date = now.format(d);
			helper.addInspectionSchedule(ie, adminProductMasterSerialNumber, masterInspection, date, null);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Schedule-Inspections-For-" + date.replaceAll("/", "-") + ".png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * List all the schedules for the create user products.
	 * 
	 * @throws Exception
	 */
	public void testCreateListingScheduledInspections() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * The admin user edits the schedule for inspecting editor product.
	 * 
	 * @throws Exception
	 */
	public void testAdminEditScheduleEditorForInspection() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
			Date d = new Date(System.currentTimeMillis() + (7*dayInMilliseconds));	// one week from today
			String date = now.format(d);
			helper.addInspectionSchedule(ie, editorProductMasterSerialNumber, masterInspection, date, null);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Schedule-Inspections-For-" + date.replaceAll("/", "-") + ".png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * List all the schedules for the editor user products.
	 * 
	 * @throws Exception
	 */
	public void testEditorListingScheduledInspections() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * List all the schedules for the both user products.
	 * 
	 * @throws Exception
	 */
	public void testBothListingScheduledInspections() throws Exception {
		String method = helper.getMethodName();
		try {
			loginBoth();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * List all the schedules for the neither user products.
	 * 
	 * @throws Exception
	 */
	public void testNeitherListingScheduledInspections() throws Exception {
		String method = helper.getMethodName();
		try {
			loginNeither();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * List all the schedules for the admin user products.
	 * 
	 * @throws Exception
	 */
	public void testAdminListingScheduledInspections() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/" + method + "-Scheduled-Inspections.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * print all the manufacturer certificates for the user neither. Since neither
	 * can only see the products we created in this smoke test and this smoke test
	 * does not create manufacturer certificates, the email should be a message
	 * telling you there were no certificates to print.
	 * 
	 * @throws Exception
	 */
	public void testNeitherPrintingAllManufacturerCertificates() throws Exception {
		String method = helper.getMethodName();
		try {
			loginNeither();

			helper.productSearch(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			helper.printAllManufacturerCertificates(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-Print-Manufacturer-Certificates.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * print all the manufacturer certificates for the tenant. Because we are logging
	 * in as n4systems, we have access to all products and therefore all products with
	 * manufacturer certificates. There is a variable, limitingProductType, defined
	 * with the tenant variable which is used to limit the number of manufacturer
	 * certificates this test case will generate. If you set it to null it will print
	 * out all the certificates.
	 * 
	 * @throws Exception
	 */
	public void testN4SystemsPrintingAllManufacturerCertificates() throws Exception {
		String method = helper.getMethodName();
		try {
			loginN4Systems();

			helper.productSearch(ie, null, null, null, null, null, null, null, null, null, null, null, limitingProductType, null, null);
			ie.waitUntilReady();
			helper.printAllManufacturerCertificates(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-Print-Manufacturer-Certificates.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Do a search for all products visible to create, turn on all the columns and
	 * export the results to an Excel spreadsheet. The capture result for this test
	 * will go to the bottom of the page before it does the capture. This will allow
	 * you to see the number of products in the result. The number of rows in the
	 * spreadsheet should match (plus one for the header).
	 * 
	 * @throws Exception
	 */
	public void testEditorAssetsExportToExcel() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();

			helper.productSearch(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.exportToExcel(ie);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Export-to-Excel.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Do a search for all products visible to admin, turn on all the columns and
	 * export the results to an Excel spreadsheet. The capture result for this test
	 * will go to the bottom of the page before it does the capture. This will allow
	 * you to see the number of products in the result. The number of rows in the
	 * spreadsheet should match (plus one for the header).
	 * 
	 * @throws Exception
	 */
	public void testAdminAssetsExportToExcel() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.productSearch(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.exportToExcel(ie);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Export-to-Excel.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Do a search for all products visible to both, turn on all the columns and
	 * export the results to an Excel spreadsheet. The capture result for this test
	 * will go to the bottom of the page before it does the capture. This will allow
	 * you to see the number of products in the result. The number of rows in the
	 * spreadsheet should match (plus one for the header).
	 * 
	 * @throws Exception
	 */
	public void testBothReportingExportToExcel() throws Exception {
		String method = helper.getMethodName();
		try {
			loginBoth();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.exportToExcel(ie);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Export-to-Excel.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Do a search for all products visible to admin, turn on all the columns and
	 * export the results to an Excel spreadsheet. The capture result for this test
	 * will go to the bottom of the page before it does the capture. This will allow
	 * you to see the number of products in the result. The number of rows in the
	 * spreadsheet should match (plus one for the header).
	 * 
	 * @throws Exception
	 */
	public void testAdminReportingExportToExcel() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.exportToExcel(ie);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Export-to-Excel.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Do a search for all products visible to create, turn on all the columns and
	 * export the results to an Excel spreadsheet. The capture result for this test
	 * will go to the bottom of the page before it does the capture. This will allow
	 * you to see the number of products in the result. The number of rows in the
	 * spreadsheet should match (plus one for the header).
	 * 
	 * @throws Exception
	 */
	public void testCreateScheduleExportToExcel() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.exportToExcel(ie);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Export-to-Excel.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Do a search for all products visible to admin, turn on all the columns and
	 * export the results to an Excel spreadsheet. The capture result for this test
	 * will go to the bottom of the page before it does the capture. This will allow
	 * you to see the number of products in the result. The number of rows in the
	 * spreadsheet should match (plus one for the header).
	 * 
	 * @throws Exception
	 */
	public void testAdminScheduleExportToExcel() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.setAllColumnsOnSearchResults(ie);
			ie.waitUntilReady();
			helper.exportToExcel(ie);
			helper.gotoBottomOfPage(ie, true);
			helper.myWindowCapture(timestamp + "/" + method + "-Export-to-Excel.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Confirm that Customer users with Create Inspection permission cannot mass update
	 * an asset. The test case, if successful will grab a screen shot of the bottom
	 * of the display.
	 * 
	 * @throws Exception
	 */
	public void testCreateCannotMassUpdateAssets() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();

			helper.productSearch(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			if(helper.isMassUpdateAvailable(ie)) {
				throw new TestCaseFailedException();
			}
			helper.myWindowCapture(timestamp + "/" + method + "-Mass-Update.png", ie);
		} catch (Exception e) {
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Confirm that Customer users with Edit Inspection permission cannot mass update
	 * an asset. The test case, if successful will grab a screen shot of the bottom
	 * of the display. You should check the screen shot to make sure there is no link
	 * to the Mass Update. This is testing for a false positive.
	 * 
	 * @throws Exception
	 */
	public void testEditorCannotMassUpdateAssets() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();

			helper.productSearch(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			helper.gotoBottomOfPage(ie, true);
			if(helper.isMassUpdateAvailable(ie)) {
				throw new TestCaseFailedException();
			}
			helper.myWindowCapture(timestamp + "/" + method + "-Mass-Update.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Confirm that Customer users with All Inspection permission cannot mass update
	 * an asset. The test case, if successful will grab a screen shot of the bottom
	 * of the display. You should check the screen shot to make sure there is no link
	 * to the Mass Update. This is testing for a false positive.
	 * 
	 * @throws Exception
	 */
	public void testAdminCannotMassUpdateAssets() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.productSearch(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			helper.gotoBottomOfPage(ie, true);
			if(helper.isMassUpdateAvailable(ie)) {
				throw new TestCaseFailedException();
			}
			helper.myWindowCapture(timestamp + "/" + method + "-Mass-Update.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Select all products visible to this user then from the search result page select
	 * the Print->This Report. To finish the test case you need to check email for a link
	 * to the PDF created. Once you download the PDF, you want to compare the screen shot
	 * to the PDF. Are all the same products in the PDF as in the screen shot? Obviously,
	 * if there is more than one page of products on the screen you will not be able to
	 * validate all products. Check the count of products. Additionally, the report should
	 * be filtering on the following fields:
	 * 
	 * 		Product Type: 			master
	 * 		Inspector Name:			Create User
	 * 		Inspection Type Group:	Visual Inspection
	 * 		Customer:				Smoke Test
	 * 		Division:				create-inspection
	 * 
	 * Make sure the top of each page in the PDF indicates these are filtered fields.
	 * 
	 * @throws Exception
	 */
	public void testCreateReportingPrintThisReport() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();

			String createName = createFirstName + " " + createLastName;
			helper.gotoReportSearchResults(ie, null, null, null, inspectionGroup, createName, null, null, null, null, null, null, masterProductType, null, null, null);
			ie.waitUntilReady();
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.printReportingThisReport(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-Reporting-This-Report.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Select all products visible to this user then from the search result page select
	 * the Print->This Report. To finish the test case you need to check email for a link
	 * to the PDF created. Once you download the PDF, you want to compare the screen shot
	 * to the PDF. Are all the same products in the PDF as in the screen shot? Obviously,
	 * if there is more than one page of products on the screen you will not be able to
	 * validate all products. Check the count of products. Additionally, the report should
	 * be filtering on the following fields:
	 * 
	 * 		Product Type: 			master
	 * 		Inspector Name:			Admin User
	 * 		Inspection Type Group:	Visual Inspection
	 * 		Customer:				Smoke Test
	 * 		Division:				All
	 * 
	 * Make sure the top of each page in the PDF indicates these are filtered fields.
	 * 
	 * @throws Exception
	 */
	public void testAdminReportingPrintThisReport() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			String adminName = adminFirstName + " " + adminLastName;
			helper.gotoReportSearchResults(ie, null, null, null, inspectionGroup, adminName, null, null, null, null, null, null, masterProductType, null, null, null);
			ie.waitUntilReady();
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.printReportingThisReport(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-Reporting-This-Report.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Confirm that Customer users with Create Inspection permission cannot mass update
	 * reports. The test case, if successful will grab a screen shot of the bottom
	 * of the display. You should check the screen shot to make sure there is no link
	 * to the Mass Update. This is testing for a false positive.
	 * 
	 * @throws Exception
	 */
	public void testCreateCannotMassUpdateReporting() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			if(helper.isMassUpdateAvailable(ie)) {
				throw new TestCaseFailedException();
			}
			helper.myWindowCapture(timestamp + "/" + method + "-Mass-Update.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Confirm that Customer users with Edit Inspection permission can mass update
	 * reports. The test case, if successful will grab a screen shot of the bottom
	 * of the display. You should check the screen shot to make sure there is a link
	 * to the Mass Update. This is testing for a false positive.
	 * 
	 * @throws Exception
	 */
	public void testEditorCanMassUpdateReporting() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			if(!helper.isMassUpdateAvailable(ie)) {
				throw new TestCaseFailedException();
			}
			helper.gotoMassUpdate(ie);
			boolean jobsite = false;
			helper.validateReportingMassUpdate(ie, jobsite);
			helper.myWindowCapture(timestamp + "/" + method + "-Mass-Update.png", ie);
		} catch (Exception e) {
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Confirm that Customer users with All Inspection permission can mass update
	 * reports. The test case, if successful will grab a screen shot of the bottom
	 * of the display.
	 * 
	 * @throws Exception
	 */
	public void testAdminCanMassUpdateReporting() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			if(!helper.isMassUpdateAvailable(ie)) {
				throw new TestCaseFailedException();
			}
			helper.gotoMassUpdate(ie);
			boolean jobsite = false;
			helper.validateReportingMassUpdate(ie, jobsite);
			helper.myWindowCapture(timestamp + "/" + method + "-Mass-Update.png", ie);
		} catch (Exception e) {
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * Confirm that Customer users with Create Inspection permission can mass update
	 * schedules. The test case, if successful will grab a screen shot of the bottom
	 * of the display. You should check the screen shot to make sure there is no link
	 * to the Mass Update. This is testing for a false positive.
	 * 
	 * @throws Exception
	 */
	public void testCreateCanMassUpdateSchedule() throws Exception {
		String method = helper.getMethodName();
		try {
			loginCreate();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			if(!helper.isMassUpdateAvailable(ie)) {
				throw new TestCaseFailedException();
			}
			helper.gotoMassUpdate(ie);
			helper.validateScheduleMassUpdate(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-Mass-Update.png", ie);
		} catch (Exception e) {
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Confirm that Customer users with Edit Inspection permission cannot mass update
	 * schedules. The test case, if successful will grab a screen shot of the bottom
	 * of the display. You should check the screen shot to make sure there is a link
	 * to the Mass Update. This is testing for a false positive.
	 * 
	 * This test case assumes that admin has updated the schedule at least one of
	 * editor's products. This is done in testAdminEditScheduleEditorForInspection.
	 * If the test case testAdminEditScheduleEditorForInspection is not run successfully
	 * this test case will most likely fail because there is nothing scheduled to
	 * mass update. 
	 * 
	 * @throws Exception
	 */
	public void testEditorCannotMassUpdateSchedule() throws Exception {
		String method = helper.getMethodName();
		try {
			loginEditor();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			if(helper.isMassUpdateAvailable(ie)) {
				throw new TestCaseFailedException();
			}
			helper.myWindowCapture(timestamp + "/" + method + "-Mass-Update.png", ie);
		} catch (Exception e) {
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Confirm that Customer users with All Inspection permission can mass update
	 * schedules. The test case, if successful will grab a screen shot of the bottom
	 * of the display. You should check the screen shot to make sure there is a link
	 * to the Mass Update. This is testing for a false positive.
	 * 
	 * @throws Exception
	 */
	public void testAdminCanMassUpdateSchedule() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			ie.waitUntilReady();
			if(!helper.isMassUpdateAvailable(ie)) {
				throw new TestCaseFailedException();
			}
			helper.gotoMassUpdate(ie);
			helper.validateScheduleMassUpdate(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-Mass-Update.png", ie);
		} catch (Exception e) {
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Test that the admin user can print the inspection certificates for every division.
	 * After this completes an email with a link to a download zip file will be sent out.
	 * You need to look at the contents of the download (a PDF file) and confirm it looks
	 * okay.
	 * 
	 * @throws Exception
	 */
	public void testAdminPrintAllInspectionCertificatesFromReporting() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.printReportingAllInspectionCertificates(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-All-Inspect-Certs.png", ie);
		} catch (Exception e) {
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	/**
	 * Test that the admin user can print the observtion reports for every division.
	 * After this completes an email with a link to a download zip file will be sent out.
	 * You need to look at the contents of the download (a PDF file) and confirm it looks
	 * okay.
	 * 
	 * @throws Exception
	 */
	public void testAdminPrintAllObservationReportsFromReporting() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.printReportingAllObservationReports(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-All-Observation-Reports.png", ie);
		} catch (Exception e) {
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testAdminReportingSummaryReport() throws Exception {
		String method = helper.getMethodName();
		try {
			loginAdmin();

			helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			helper.gotoReportingSummaryReport(ie);
			helper.myWindowCapture(timestamp + "/" + method + "-All-Observation-Reports.png", ie);
		} catch (Exception e) {
			helper.gotoBottomOfPage(ie, true);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testAddProductSave() throws Exception {
		String method = helper.getMethodName();
		try {
			loginN4Systems();

			String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, subProductType, null, null, null, "Save");
			helper.myWindowCapture(timestamp + "/" + method + "-" + serialNumber + "-Should-Be-On-Add-Product-Without-Errors.png", ie);
			if(!ie.span(text("Product created.")).exists())
				throw new TestCaseFailedException();
			if(!ie.htmlElement(text("Add Product")).exists())
				throw new TestCaseFailedException();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testAddProductSaveAndInspect() throws Exception {
		String method = helper.getMethodName();
		try {
			loginN4Systems();

			// TODO: the Save and Inspect should go to Quick Inspect but if sub product type
			// has an a single inspection type it will go directly to that inspection. The
			// setup code tries to remove all inspection types from sub but WEB-796 makes it
			// fail. So this part of the code fails as well.
			
			String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, subProductType, null, null, null, "Save And Inspect");
			helper.myWindowCapture(timestamp + "/" + method + "-" + serialNumber + "-Should-Be-On-Quick-Inspect-Without-Errors.png", ie);
			if(!ie.span(text("Product created.")).exists())
				throw new TestCaseFailedException();
			if(!ie.htmlElement(text("Quick Inspect")).exists())
				throw new TestCaseFailedException();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testAddProductSaveAndPrint() throws Exception {
		String method = helper.getMethodName();
		try {
			loginN4Systems();

			List<String> pts = helper.getProductTypesWithManufacturersCert(ie);
			if(pts.size() < 1) {
				throw new TestCaseFailedException();	// no products with manufacturer cert
			}
			String productType = pts.get(pts.size()-1);	// get the last product type on the list
			String[] params = { "" };
			String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, productType, params, null, null, "Save And Print");
			helper.myWindowCapture(timestamp + "/" + method + "-" + serialNumber + "-Should-Be-On-Add-Product-Without-Errors.png", ie);
			IE child = ie.childBrowser(0);
			if(!child.exists())
				throw new TestCaseFailedException();
			else
				child.close();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testAddProductSaveAndSchedule() throws Exception {
		String method = helper.getMethodName();
		try {
			loginN4Systems();

			String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, subProductType, null, null, null, "Save And Schedule");
			helper.myWindowCapture(timestamp + "/" + method + "-" + serialNumber + "-Should-Be-On-Schedule-For-Without-Errors.png", ie);
			if(!ie.span(text("Product created.")).exists())
				throw new TestCaseFailedException();
			if(!ie.containsText("Schedules For"))
				throw new TestCaseFailedException();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		System.out.println("Web Browser status changed " + helper.getNumberOfResets() + " times.");
		System.out.println("But we had to refresh the web browser " + helper.getNumberOfRefreshes() + " times.");
		ie.close();
	}
}
