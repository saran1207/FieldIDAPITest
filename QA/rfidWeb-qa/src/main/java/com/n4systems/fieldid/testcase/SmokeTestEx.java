package com.n4systems.fieldid.testcase;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import watij.runtime.ie.IE;
import com.n4systems.fieldid.*;
import com.n4systems.fieldid.admin.*;
import com.n4systems.fieldid.datatypes.ButtonGroup;
import com.n4systems.fieldid.datatypes.Customer;
import com.n4systems.fieldid.datatypes.CustomerUser;
import com.n4systems.fieldid.datatypes.InspectionForm;
import com.n4systems.fieldid.datatypes.InspectionType;
import com.n4systems.fieldid.datatypes.Product;
import com.n4systems.fieldid.datatypes.ProductSearchSelectColumns;
import com.n4systems.fieldid.datatypes.ProductType;
import com.n4systems.fieldid.datatypes.Section;
import com.n4systems.fieldid.datatypes.Criteria;

import junit.framework.TestCase;

public class SmokeTestEx extends TestCase {

	IE ie = new IE();
	FieldIDMisc misc = new FieldIDMisc(ie);
	Login login = new Login(ie);
	Admin admin = new Admin(ie);
	ManageCustomers mcs = new ManageCustomers(ie); 
	ManageUsers mus = new ManageUsers(ie);
	ManageProductTypes mpts = new ManageProductTypes(ie);
	ManageInspectionTypes mits = new ManageInspectionTypes(ie);
	Identify identify = new Identify(ie);
	Home home = new Home(ie);
	Assets assets = new Assets(ie);
	MyAccount myAccount = new MyAccount(ie);
	Inspect inspect = new Inspect(ie);
	Reporting reporting = new Reporting(ie);
	Schedule schedule = new Schedule(ie);

	static String timestamp = null;
	static boolean once = true;
	final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ");
	static Random r = new Random();

	String loginURL = "https://localhost.localdomain/fieldid/";
	String company = "unirope";
	boolean jobs = false;			// end users do not have Jobs
	String password = "makemore$";
	
	// Smoke Test products
	// Everything is static so it can be used across test cases 
	static String adminSubProductSerialNumber;
	static String adminMasterProductSerialNumber;
	static String createSubProductSerialNumber;
	static String createMasterProductSerialNumber;
	static String editorSubProductSerialNumber;
	static String editorMasterProductSerialNumber;
	static String bothSubProductSerialNumber;
	static String bothMasterProductSerialNumber;
	static String neitherSubProductSerialNumber;
	static String neitherMasterProductSerialNumber;
	
	// Smoke Test customer and users
	// Everything is static so it can be used across test cases 
	static Customer customer = new Customer("smoke", "Smoke Test");
	static String customerID = "smoke";
	static String customerName = "Smoke Test";
	static String customerContact = "Smoke Test";
	static String customerEmail = "darrell.grainger@n4systems.com";
	static String customerStreet = "179 John St.";
	static String customerCity = "Toronto";
	static String customerState = "ON";
	static String customerZipCode = "M5T 1X4";
	static String customerCountry = "Canada";
	static String customerPhone1 = "(416) 599-6464";
	static String customerPhone2 = "(416) 599-6466";
	static String customerFax = "(416) 599-6463";
	static String createUser = new String("create");
	static String editorUser = new String("editor");
	static String bothUser = new String("both");
	static String neitherUser = new String("neither");
	static String adminUser = new String("admin");
	static String createDivision = new String("create-div");
	static String editorDivision = new String("editor-div");
	static String bothDivision = new String("both-div");
	static String neitherDivision = new String("neither-div");
	static String adminDivision = "";
	static String userEmail = "darrell.grainger@n4systems.com";
	static String createFirstName = "Create";
	static String editorFirstName = "Editor";
	static String bothFirstName = "Both";
	static String neitherFirstName = "Neither";
	static String adminFirstName = "Admin";
	static String createLastName = "User";
	static String editorLastName = "User";
	static String bothLastName = "User";
	static String neitherLastName = "User";
	static String adminLastName = "User";

	protected void setUp() throws Exception {
		super.setUp();
		misc.start();
		login.gotoLoginPage(loginURL);
		if(once) {
			once = false;
			timestamp = misc.createTimestampDirectory() + "/";
			customer.setContactName(customerContact);
			customer.setContactEmail(customerEmail);
			customer.setStreetAddress(customerStreet);
			customer.setCity("Toronto");
			customer.setState(customerState);
			customer.setZip(customerZipCode);
			customer.setCountry(customerCountry);
			customer.setPhone1(customerPhone1);
			customer.setPhone2(customerPhone2);
			customer.setFax(customerFax);
		}
		login.setCompany(company);
	}
	
	private void loginN4Systems() throws Exception {
		login.setUserName("n4systems");
		login.setPassword(password);
		login.login();
	}

	private void loginAdminUser() throws Exception {
		login.setUserName(adminUser);
		login.setPassword(password);
		login.login();
	}

	private void loginCreateUser() throws Exception {
		login.setUserName(createUser);
		login.setPassword(password);
		login.login();
	}

	private void loginEditorUser() throws Exception {
		login.setUserName(editorUser);
		login.setPassword(password);
		login.login();
	}

	private void loginBothUser() throws Exception {
		login.setUserName(bothUser);
		login.setPassword(password);
		login.login();
	}

	private void loginNeitherUser() throws Exception {
		login.setUserName(neitherUser);
		login.setPassword(password);
		login.login();
	}

	public void testSmokeTestSetup() throws Exception {
		String method = getName();
		loginN4Systems();
		try {
			createCustomerAndUsers();
			createInspectionTypesAndProductTypes();
			createProducts();
			configureProducts();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testAdminValidateHome() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			home.validateHomePage(jobs);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testAdminAssetsSearch() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			assets.gotoAssets();
			assets.expandProductSearchSelectColumns();
			ProductSearchSelectColumns c = new ProductSearchSelectColumns();
			c.setAllOn();
			assets.setProductSearchColumns(c);
			assets.gotoProductSearchResults();
			List<String> customers = assets.getProductSearchResultsColumn("Customer Name");
			Iterator<String> i = customers.iterator();
			while(i.hasNext()) {
				String cus = i.next();
				assertEquals(customer.getCustomerName(), cus);
			}
			misc.myWindowCapture(timestamp + "/" + method + ".png");
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreateAssetsSearch() throws Exception {
		String method = getName();
		loginCreateUser();
		try {
			assets.gotoAssets();
			assets.expandProductSearchSelectColumns();
			ProductSearchSelectColumns c = new ProductSearchSelectColumns();
			c.setAllOn();
			assets.setProductSearchColumns(c);
			assets.gotoProductSearchResults();
			List<String> customers = assets.getProductSearchResultsColumn("Customer Name");
			Iterator<String> i = customers.iterator();
			while(i.hasNext()) {
				String cus = i.next();
				assertEquals(customer.getCustomerName(), cus);
			}
			misc.myWindowCapture(timestamp + "/" + method + ".png");
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testEditorAssetsSearch() throws Exception {
		String method = getName();
		loginEditorUser();
		try {
			assets.gotoAssets();
			assets.expandProductSearchSelectColumns();
			ProductSearchSelectColumns c = new ProductSearchSelectColumns();
			c.setAllOn();
			assets.setProductSearchColumns(c);
			assets.gotoProductSearchResults();
			List<String> customers = assets.getProductSearchResultsColumn("Customer Name");
			Iterator<String> i = customers.iterator();
			while(i.hasNext()) {
				String cus = i.next();
				assertEquals(customer.getCustomerName(), cus);
			}
			misc.myWindowCapture(timestamp + "/" + method + ".png");
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testBothAssetsSearch() throws Exception {
		String method = getName();
		loginBothUser();
		try {
			assets.gotoAssets();
			assets.expandProductSearchSelectColumns();
			ProductSearchSelectColumns c = new ProductSearchSelectColumns();
			c.setAllOn();
			assets.setProductSearchColumns(c);
			assets.gotoProductSearchResults();
			List<String> customers = assets.getProductSearchResultsColumn("Customer Name");
			Iterator<String> i = customers.iterator();
			while(i.hasNext()) {
				String cus = i.next();
				assertEquals(customer.getCustomerName(), cus);
			}
			misc.myWindowCapture(timestamp + "/" + method + ".png");
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testNeitherAssetsSearch() throws Exception {
		String method = getName();
		loginNeitherUser();
		try {
			assets.gotoAssets();
			assets.expandProductSearchSelectColumns();
			ProductSearchSelectColumns c = new ProductSearchSelectColumns();
			c.setAllOn();
			assets.setProductSearchColumns(c);
			assets.gotoProductSearchResults();
			List<String> customers = assets.getProductSearchResultsColumn("Customer Name");
			Iterator<String> i = customers.iterator();
			while(i.hasNext()) {
				String cus = i.next();
				assertEquals(customer.getCustomerName(), cus);
			}
			misc.myWindowCapture(timestamp + "/" + method + ".png");
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testAdminEditingAProduct() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			home.gotoProductInformationViaSmartSearch(adminMasterProductSerialNumber);
			assets.gotoEditProduct(adminMasterProductSerialNumber);
			assets.checkEndUserEditProduct(false);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private void configureProducts() throws Exception {
		home.gotoProductInformationViaSmartSearch(adminMasterProductSerialNumber);
		assets.gotoProductConfiguration(adminMasterProductSerialNumber);
		assets.addSubProductToMasterProduct(subProductType, adminSubProductSerialNumber);
		
		home.gotoProductInformationViaSmartSearch(createMasterProductSerialNumber);
		assets.gotoProductConfiguration(createMasterProductSerialNumber);
		assets.addSubProductToMasterProduct(subProductType, createSubProductSerialNumber);
		
		home.gotoProductInformationViaSmartSearch(editorMasterProductSerialNumber);
		assets.gotoProductConfiguration(editorMasterProductSerialNumber);
		assets.addSubProductToMasterProduct(subProductType, editorSubProductSerialNumber);
		
		home.gotoProductInformationViaSmartSearch(bothMasterProductSerialNumber);
		assets.gotoProductConfiguration(bothMasterProductSerialNumber);
		assets.addSubProductToMasterProduct(subProductType, bothSubProductSerialNumber);
		
		home.gotoProductInformationViaSmartSearch(neitherMasterProductSerialNumber);
		assets.gotoProductConfiguration(neitherMasterProductSerialNumber);
		assets.addSubProductToMasterProduct(subProductType, neitherSubProductSerialNumber);
	}

	private void createProducts() throws Exception {
		identify.gotoAddProduct();
		adminSubProductSerialNumber = createMasterSubProduct(adminUser, null, subProductType);
		adminMasterProductSerialNumber = createMasterSubProduct(adminUser, null, masterProductType);
		createSubProductSerialNumber = createMasterSubProduct(createUser, createDivision, subProductType);
		createMasterProductSerialNumber = createMasterSubProduct(createUser, createDivision, masterProductType);
		editorSubProductSerialNumber = createMasterSubProduct(editorUser, editorDivision, subProductType);
		editorMasterProductSerialNumber = createMasterSubProduct(editorUser, editorDivision, masterProductType);
		bothSubProductSerialNumber = createMasterSubProduct(bothUser, bothDivision, subProductType);
		bothMasterProductSerialNumber = createMasterSubProduct(bothUser, bothDivision, masterProductType);
		neitherSubProductSerialNumber = createMasterSubProduct(neitherUser, neitherDivision, subProductType);
		neitherMasterProductSerialNumber = createMasterSubProduct(neitherUser, neitherDivision, masterProductType);
		dumpSerialNumbers(timestamp + "serial-numbers.txt");
	}
	
	private void dumpSerialNumbers(String filename) throws Exception {
		PrintStream stream = new PrintStream(filename);

		stream.println("     admin sub product: " + adminSubProductSerialNumber);
		stream.println("  admin master product: " + adminMasterProductSerialNumber);
		stream.println("    create sub product: " + createSubProductSerialNumber);
		stream.println(" create master product: " + createMasterProductSerialNumber);
		stream.println("    editor sub product: " + editorSubProductSerialNumber);
		stream.println(" editor master product: " + editorMasterProductSerialNumber);
		stream.println("      both sub product: " + bothSubProductSerialNumber);
		stream.println("   both master product: " + bothMasterProductSerialNumber);
		stream.println("   neither sub product: " + neitherSubProductSerialNumber);
		stream.println("neither master product: " + neitherMasterProductSerialNumber);

		stream.close();
	}

	private String createMasterSubProduct(String userid, String division, String productType) throws Exception {
		String today = misc.getDateString();
		Product p = new Product(today);
		
		// Create a sub product for admin
		p.setCustomer(customerName);
		p.setDivision(division);
		p.setReferenceNumber(userid + "-ref");
		p.setPurchaseOrder(userid + "-po");
		p.setLocation(userid + "-location");
		p.setProductType(productType);
		identify.addProduct(p, true, "Save");
		return p.getSerialNumber();
	}


	/**
	 * Create the inspection types and product types.
	 * 
	 * @throws Exception
	 */
	private void createInspectionTypesAndProductTypes() throws Exception {
		admin.gotoAdministration();
		createInspectionTypes();
		createProductTypes();
	}
	
	static String masterInspectionType = "N4 Master Visual Inspection";
	static InspectionForm masterInspectionForm = null;
	static String masterProductType = "n4master";
	static String subInspectionType = null;	// This gets filled in later
	static String subProductType = "n4subproduct";

	private void createProductTypes() throws Exception {
		createSubProductType();
		addSubInspectionTypeToSubProductType();
		createMasterProductType();
		addSubProductToMasterProductType();
		addMasterInspectionTypeToMasterProductType();
	}

	private void addMasterInspectionTypeToMasterProductType() throws Exception {
		mpts.gotoInspectionTypes(masterProductType);
		mpts.setInspectionType(masterInspectionType);
		mpts.saveInspectionTypes(masterProductType);
	}

	private void addSubProductToMasterProductType() throws Exception {
		mpts.gotoSubComponents(masterProductType);
		List<String> subcomponents = mpts.getSubComponents();
		if(!subcomponents.contains(subProductType)) {
			mpts.addSubComponent(subProductType);
			mpts.saveSubComponents(masterProductType);
		}
		
	}

	private void createMasterProductType() throws Exception {
		ProductType npt = new ProductType(masterProductType);
		npt.setWarnings("Master Product Type Warning");
		npt.setInstructions("Master Product Type Instructions");
		npt.setCautionsURL("http://www.n4systems.com/");
		npt.setHasManufacturerCertificate(true);
		npt.setManufacturerCertificateText("Master Product Type Manufacturer Certificate Text");

		admin.gotoAdministration();
		mpts.gotoManageProductTypes();
		if(!mpts.isProductType(masterProductType)) {
			mpts.gotoAddProductType();
			mpts.setAddProductTypeForm(npt);
		} else {
			mpts.gotoEditProductType(masterProductType);
			mpts.setAddProductTypeForm(npt);
		}
		mpts.addProductType(masterProductType);
	}

	private void addSubInspectionTypeToSubProductType() throws Exception {
		mpts.gotoInspectionTypes(subProductType);
		mpts.setInspectionType(subInspectionType);
		mpts.saveInspectionTypes(subProductType);
	}

	private void createSubProductType() throws Exception {
		ProductType npt = new ProductType(subProductType);
		npt.setWarnings("Sub Product Type Warning");
		npt.setInstructions("Sub Product Type Instructions");
		npt.setCautionsURL("http://www.n4systems.com/");
		npt.setHasManufacturerCertificate(true);
		npt.setManufacturerCertificateText("Sub Product Type Manufacturer Certificate Text");

		admin.gotoAdministration();
		mpts.gotoManageProductTypes();
		if(!mpts.isProductType(subProductType)) {
			mpts.gotoAddProductType();
			mpts.setAddProductTypeForm(npt);
		} else {
			mpts.gotoEditProductType(subProductType);
			mpts.setAddProductTypeForm(npt);
		}
		mpts.addProductType(subProductType);
	}

	/**
	 * Create the necessary Inspection Types.
	 * 
	 * @throws Exception
	 */
	private void createInspectionTypes() throws Exception {
		mits.gotoManageInspectionTypes();
		createMasterInspectonType();
		addMasterInspectionForm();
		createSubProductInspectionType();
	}

	private void createSubProductInspectionType() throws Exception {
		mits.gotoViewAll();
		List<String> its = mits.getStandardInspectionTypes();
		if(its.size() > 0) {
			int n = misc.getRandomInteger(its.size());
			subInspectionType = its.get(n);
		} else {
			subInspectionType = "Sub Product Visual Inspect";
			InspectionType it = new InspectionType(subInspectionType);
			it.setPrintable(true);
			it.setMasterInspection(false);
			List<String> proofTestTypes = new ArrayList<String>();
			proofTestTypes.add(InspectionType.robert);
			it.setProofTestTypes(proofTestTypes);
			List<String> inspectionAttributes = new ArrayList<String>();
			inspectionAttributes.add("Attrib");
			it.setInspectionAttributes(inspectionAttributes);
			mits.gotoAddInspectionType();
			mits.addInspectionType(it);
		}
	}

	private void addMasterInspectionForm() throws Exception {
		misc.stopMonitorStatus();
		mits.gotoInspectionForm(masterInspectionType);
		masterInspectionForm = mits.getInspectionForm();
		if(masterInspectionForm == null) {
			mits.gotoManageButtonGroups();
			List<String> buttonGroupNames = mits.getButtonGroupNames();
			mits.gotoImDoneFromManageButtonGroups();
			masterInspectionForm = new InspectionForm();
			int numSections = 2;
			for(int section = 0; section < numSections; section++) {
				Section s = new Section("Section #" + section);
				int numCriteria = misc.getRandomInteger(1,3);
				for(int criteria = 0; criteria < numCriteria; criteria++) {
					ButtonGroup bg = new ButtonGroup(buttonGroupNames.get(0));
					if(criteria == 0) {
						bg.setSetsResult(true);
					}
					Criteria c = new Criteria("Criteria #" + criteria, bg);
					int numRecommendations = misc.getRandomInteger(0, 3);
					for(int recommendation = 0; recommendation < numRecommendations; recommendation++) {
						c.addRecommendation("Recommendation #" + recommendation);
					}
					int numDeficiencies = misc.getRandomInteger(0, 3);
					for(int deficiency = 0; deficiency < numDeficiencies; deficiency++) {
						c.addDeficiency("Deficiency #" + deficiency);
					}
					s.addCriteria(c);
				}
				masterInspectionForm.addSection(s);
			}
			mits.addInspectionForm(masterInspectionForm);
			mits.saveInspectionForm();
		}
		misc.startMonitorStatus();
	}

	private void createMasterInspectonType() throws Exception {
		InspectionType it = new InspectionType(masterInspectionType);
		it.setPrintable(true);
		it.setMasterInspection(true);
		List<String> proofTestTypes = new ArrayList<String>();
		proofTestTypes.add(InspectionType.robert);
		proofTestTypes.add(InspectionType.naexcel);
		proofTestTypes.add(InspectionType.chant);
		proofTestTypes.add(InspectionType.wirop);
		proofTestTypes.add(InspectionType.other);
		it.setProofTestTypes(proofTestTypes);
		if(!mits.isInspectionType(masterInspectionType)) {
			mits.gotoAddInspectionType();
			mits.addInspectionType(it);
		} else {
			mits.gotoEditInspectionTypeFromInspectionTypesPage(masterInspectionType);
			mits.editInspectionType(it);
		}
	}

	/**
	 * Create the Customer, divisions and users.
	 * 
	 * @throws Exception
	 */
	private void createCustomerAndUsers() throws Exception {
		createCustomer();
		createDivisions();
		createUsers();
	}

	/**
	 * Create the 'Smoke Test' customer if they do not exist.
	 * Otherwise, edit the 'Smoke Test' customer.
	 * 
	 * @throws Exception
	 */
	private void createCustomer() throws Exception {
		admin.gotoAdministration();
		mcs.gotoManageCustomers();
		mcs.setAddCustomerFilter(customer.getCustomerName());
		mcs.gotoAddCustomerFilter();
		if(mcs.isCustomer(customer)) {
			mcs.gotoEditCustomer(customer);
			mcs.editCustomer(customer);
		} else {
			mcs.gotoAddCustomer();
			mcs.addCustomer(customer);
		}
	}

	/**
	 * See if a division already exists. If it does not create it.
	 * Assumes we run this after createCustomer. So we are on the 
	 * 'Manage Customer - Smoke Test' page.
	 * 
	 * @throws Exception
	 */
	private void createDivisions() throws Exception {
		mcs.gotoCustomerDivisions();
		if(!mcs.isCustomerDivision(createDivision))
			mcs.addCustomerDivision(createDivision);
		if(!mcs.isCustomerDivision(editorDivision))
			mcs.addCustomerDivision(editorDivision);
		if(!mcs.isCustomerDivision(bothDivision))
			mcs.addCustomerDivision(bothDivision);
		if(!mcs.isCustomerDivision(neitherDivision))
			mcs.addCustomerDivision(neitherDivision);
	}

	/**
	 * Create the various end users for testing security.
	 * Currently there are two permissions, Create and Edit
	 * Inspection. There are also users in a division and
	 * not in a division. Admin is a non-divisional user
	 * with both edit and create inspection permission.
	 * The other users are in separate divisions and their
	 * name reflects the permission they have, e.g. Both
	 * would have both permissions.
	 * 
	 * @throws Exception
	 */
	private void createUsers() throws Exception {
		admin.gotoAdministration();
		mus.gotoManageUsers();
		
		// Create User
		CustomerUser u = new CustomerUser(createUser, userEmail, createFirstName, createLastName, password);
		u.setDivision(createDivision);
		u.setCustomer(customerName);
		u.setPosition("seated");
		u.setInitials("cu");
		u.setTimeZone("Toronto");
		List<String> permissions = new ArrayList<String>();
		permissions.add(CustomerUser.create);
		u.setPermissions(permissions);
		addUser(u);
		
		// Edit User
		mus.gotoViewAll();
		u.setUserID(editorUser);
		u.setFirstName(editorFirstName);
		u.setLastName(editorLastName);
		u.setDivision(editorDivision);
		u.setInitials("eu");
		permissions = new ArrayList<String>();
		permissions.add(CustomerUser.edit);
		u.setPermissions(permissions);
		addUser(u);
		
		// Both User
		mus.gotoViewAll();
		u.setUserID(bothUser);
		u.setFirstName(bothFirstName);
		u.setLastName(bothLastName);
		u.setDivision(bothDivision);
		u.setInitials("bu");
		permissions = new ArrayList<String>();
		permissions.add(CustomerUser.edit);
		permissions.add(CustomerUser.create);
		u.setPermissions(permissions);
		addUser(u);
		
		// Neither User
		mus.gotoViewAll();
		u.setUserID(neitherUser);
		u.setFirstName(neitherFirstName);
		u.setLastName(neitherLastName);
		u.setDivision(neitherDivision);
		u.setInitials("nu");
		permissions = new ArrayList<String>();
		u.setPermissions(permissions);
		addUser(u);
		
		// Admin User
		mus.gotoViewAll();
		u.setUserID(adminUser);
		u.setFirstName(adminFirstName);
		u.setLastName(adminLastName);
		u.setDivision(adminDivision);
		u.setInitials("au");
		permissions = new ArrayList<String>();
		permissions.add(CustomerUser.edit);
		permissions.add(CustomerUser.create);
		u.setPermissions(permissions);
		addUser(u);
	}
	
	/**
	 * Checks to see if the given CustomerUser exists.
	 * If it does not we add the user to the system.
	 * If it does exist we edit the existing user.
	 * 
	 * @param u
	 * @throws Exception
	 */
	private void addUser(CustomerUser u) throws Exception {
		mus.setListUsersNameFilter(u.getUserID());
		mus.setListUsersUserType("Customers");
		mus.gotoListUsersSearch();
		if(!mus.isUser(u.getUserID())) {
			mus.gotoAddCustomerUser();
			mus.addCustomerUser(u);
		} else {
			mus.gotoEditCustomerUser(u);
			mus.editCustomerUser(u);
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		ie.close();
	}

}
