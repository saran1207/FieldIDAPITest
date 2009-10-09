package com.n4systems.fieldid.testcase;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import watij.elements.Link;

import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.datatypes.ButtonGroup;
import com.n4systems.fieldid.datatypes.Customer;
import com.n4systems.fieldid.datatypes.CustomerDivision;
import com.n4systems.fieldid.datatypes.CustomerUser;
import com.n4systems.fieldid.datatypes.Inspection;
import com.n4systems.fieldid.datatypes.InspectionForm;
import com.n4systems.fieldid.datatypes.InspectionType;
import com.n4systems.fieldid.datatypes.MassUpdateScheduleForm;
import com.n4systems.fieldid.datatypes.Owner;
import com.n4systems.fieldid.datatypes.Product;
import com.n4systems.fieldid.datatypes.ProductSearchSelectColumns;
import com.n4systems.fieldid.datatypes.ProductType;
import com.n4systems.fieldid.datatypes.ReportSearchSelectColumns;
import com.n4systems.fieldid.datatypes.ScheduleSearchCriteria;
import com.n4systems.fieldid.datatypes.ScheduleSearchSelectColumns;
import com.n4systems.fieldid.datatypes.Section;
import com.n4systems.fieldid.datatypes.Criteria;

public class SmokeTestEx extends FieldIDTestCase {

	String organization = null;
	String company = null;
	String password = null;
	boolean jobs = false;			// end users do not have Jobs
	String masterInspectionEventTypeGroup = null;
	
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
	static Customer customer = new Customer("smoke", "Smoke Test", null);
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
	static String adminDivision = null;
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
		if(once) {
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
		organization = prop.getProperty("organization");
		company = prop.getProperty("company");
		password = prop.getProperty("password");
		jobs = Boolean.parseBoolean(prop.getProperty("jobs"));
		masterInspectionEventTypeGroup = prop.getProperty("masterinspectioneventtypegroup");
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
		loginN4Systems();
		try {
			createCustomerAndUsers();
			createInspectionTypesAndProductTypes();
			createProducts();
			configureProducts();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
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
			helperAssetsSearch();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private void helperAssetsSearch() throws Exception {
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
			assets.printAllManufacturerCertificates();
			assets.exportToExcel();
			assertFalse("Mass Update is available to end users", assets.isMassUpdate());
	}
	
	public void testCreateAssetsSearch() throws Exception {
		String method = getName();
		loginCreateUser();
		try {
			helperAssetsSearch();
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
			helperAssetsSearch();
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
			helperAssetsSearch();
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
			helperAssetsSearch();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testAdminPrintingACertificate() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			helperPrintingACertificate(adminMasterProductSerialNumber);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatePrintingACertificate() throws Exception {
		String method = getName();
		loginCreateUser();
		try {
			helperPrintingACertificate(createMasterProductSerialNumber);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testEditorPrintingACertificate() throws Exception {
		String method = getName();
		loginEditorUser();
		try {
			helperPrintingACertificate(editorMasterProductSerialNumber);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testBothPrintingACertificate() throws Exception {
		String method = getName();
		loginBothUser();
		try {
			helperPrintingACertificate(bothMasterProductSerialNumber);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testNeitherPrintingACertificate() throws Exception {
		String method = getName();
		loginNeitherUser();
		try {
			helperPrintingACertificate(neitherMasterProductSerialNumber);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private void helperPrintingACertificate(String serialNumber) throws Exception {
		home.gotoProductInformationViaSmartSearch(serialNumber);
		assets.downloadManufactureCertificate();
	}
	
	public void testAdminEditingAProduct() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			helperEditingAProduct(adminMasterProductSerialNumber, false);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreateEditingAProduct() throws Exception {
		String method = getName();
		loginCreateUser();
		try {
			helperEditingAProduct(createMasterProductSerialNumber, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testEditorEditingAProduct() throws Exception {
		String method = getName();
		loginEditorUser();
		try {
			helperEditingAProduct(editorMasterProductSerialNumber, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testBothEditingAProduct() throws Exception {
		String method = getName();
		loginBothUser();
		try {
			helperEditingAProduct(bothMasterProductSerialNumber, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testNeitherEditingAProduct() throws Exception {
		String method = getName();
		loginNeitherUser();
		try {
			helperEditingAProduct(neitherMasterProductSerialNumber, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private void helperEditingAProduct(String serialNumber, boolean divisional) throws Exception {
		home.gotoProductInformationViaSmartSearch(serialNumber);
		assets.gotoEditProduct(serialNumber);
		assets.checkEndUserEditProduct(divisional);
		assets.saveCustomerProduct(serialNumber);
	}
	
	public void testAdminInspectingAProduct() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			helperInspectingAProduct(adminMasterProductSerialNumber, masterInspectionType, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreateInspectingAProduct() throws Exception {
		String method = getName();
		loginCreateUser();
		try {
			helperInspectingAProduct(createMasterProductSerialNumber, masterInspectionType, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testEditorFailingToInspectAProduct() throws Exception {
		String method = getName();
		loginEditorUser();
		try {
			inspect.gotoInspect();
			inspect.loadAssetViaSmartSearch(editorMasterProductSerialNumber);
			assertFalse("The ability to start a new inspection exists for a user without permission", inspect.isStartNewInspectionAvailable());
			
			// Logout, login as admin and create an inspect for editor to edit later
			misc.logout();
			loginAdminUser();
			helperInspectingAProduct(editorMasterProductSerialNumber, masterInspectionType, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testBothInspectingAProduct() throws Exception {
		String method = getName();
		loginBothUser();
		try {
			helperInspectingAProduct(bothMasterProductSerialNumber, masterInspectionType, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testNeitherFailingToInspectAProduct() throws Exception {
		String method = getName();
		loginNeitherUser();
		try {
			inspect.gotoInspect();
			inspect.loadAssetViaSmartSearch(neitherMasterProductSerialNumber);
			assertFalse("The ability to start a new inspection exists for a user without permission", inspect.isStartNewInspectionAvailable());
			
			// Logout, login as admin and create an inspect for neither to fail editing later
			misc.logout();
			loginAdminUser();
			helperInspectingAProduct(neitherMasterProductSerialNumber, masterInspectionType, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private void helperInspectingAProduct(String serialNumber, String inspectionType, boolean master) throws Exception {
		home.gotoProductInformationViaSmartSearch(serialNumber);
		assets.gotoInspectionGroups(serialNumber);
		inspect.gotoStartNewInspection(inspectionType, true);
		Inspection inspection = new Inspection();
		inspect.gotoStartMasterInspection(inspectionType);
		inspect.setMasterInspection(inspection, null);
		inspect.gotoStoreMasterInspection();
		inspect.gotoSaveMasterInspection(serialNumber);
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
		
		String organization;
		Owner owner = new Owner(this.organization, customerName);
		p.setOwner(owner);
//		p.setCustomer(customerName);
//		p.setDivision(division);
		p.setReferenceNumber(userid + "-ref");
		p.setPurchaseOrder(userid + "-po");
		p.setLocation(userid + "-location");
		p.setProductType(productType);
		identify.setProduct(p, true);
		identify.addProductSave();
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
		FieldIDMisc.stopMonitor();
		mits.gotoInspectionForm(masterInspectionType);
		masterInspectionForm = mits.getInspectionForm();
		if(masterInspectionForm == null) {
			mits.gotoManageButtonGroups();
			List<String> buttonGroupNames = mits.getButtonGroupNames();
//			if(buttonGroupNames.size() < 1) {
//				mits.gotoAddButtonGroup();
//				ButtonGroupType bgt = new ButtonGroupType("Pass/Fail");
//				bgt.setLabel(0, "PASS");
//				bgt.setLabel(1, "FAIL", ButtonGroupType.FAIL, ButtonGroupType.FAIL_IMAGE);
//				// TODO: add the button group
//			}
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
					int numRecommendations = 21; // misc.getRandomInteger(0, 3);
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
		FieldIDMisc.startMonitor();
	}

	private void createMasterInspectonType() throws Exception {
		InspectionType it = new InspectionType(masterInspectionType);
		it.setGroup(masterInspectionEventTypeGroup);
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
		CustomerDivision d = new CustomerDivision(null,null);
		if(!mcs.isCustomerDivision(createDivision)) {
			mcs.gotoAddCustomerDivision();
			d.setDivisionID(createDivision);
			d.setDivisionName(createDivision);
			mcs.setCustomerDivision(d);
			mcs.addCustomerDivision();
		}
		if(!mcs.isCustomerDivision(editorDivision)) {
			mcs.gotoAddCustomerDivision();
			d.setDivisionID(editorDivision);
			d.setDivisionName(editorDivision);
			mcs.setCustomerDivision(d);
			mcs.addCustomerDivision();
		}
		if(!mcs.isCustomerDivision(bothDivision)) {
			mcs.gotoAddCustomerDivision();
			d.setDivisionID(bothDivision);
			d.setDivisionName(bothDivision);
			mcs.setCustomerDivision(d);
			mcs.addCustomerDivision();
		}
		if(!mcs.isCustomerDivision(neitherDivision)) {
			mcs.gotoAddCustomerDivision();
			d.setDivisionID(neitherDivision);
			d.setDivisionName(neitherDivision);
			mcs.setCustomerDivision(d);
			mcs.addCustomerDivision();
		}
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
		mcs.gotoManageCustomers();
		mcs.setAddCustomerFilter(customer.getCustomerName());
		mcs.gotoAddCustomerFilter();
		mcs.gotoCustomer(customer);
		mcs.gotoUsers();

		// Create User
		CustomerUser u = new CustomerUser(createUser, userEmail, createFirstName, createLastName, password);
		u.setDivision(createDivision);
		u.setCustomer(customerName);
		u.setPosition("seated");
		u.setInitials("cu");
		u.setCountry("Canada");
		u.setTimeZone("Toronto");
		List<String> permissions = new ArrayList<String>();
		permissions.add(CustomerUser.create);
		u.setPermissions(permissions);
		addUser(u);
		
		// Edit User
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
		u.setUserID(neitherUser);
		u.setFirstName(neitherFirstName);
		u.setLastName(neitherLastName);
		u.setDivision(neitherDivision);
		u.setInitials("nu");
		permissions = new ArrayList<String>();
		u.setPermissions(permissions);
		addUser(u);
		
		// Admin User
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
		 if(!mcs.isUser(u.getUserID())) {
			mcs.gotoAddUser();
			mcs.addCustomerUser(u);
		} else {
			mcs.gotoEditUser(u.getUserID());
			mcs.editCustomerUser(u);
		}
	}
	
	public void testAdminReportingSearch() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			helperReportingSearch(neitherDivision, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreateReportingSearch() throws Exception {
		String method = getName();
		loginCreateUser();
		try {
			helperReportingSearch(null, false);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testEditorReportingSearch() throws Exception {
		String method = getName();
		loginEditorUser();
		try {
			helperReportingSearch(null, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testBothReportingSearch() throws Exception {
		String method = getName();
		loginBothUser();
		try {
			helperReportingSearch(null, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testNeitherReportingSearch() throws Exception {
		String method = getName();
		loginNeitherUser();
		try {
			helperReportingSearch(null, false);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private void helperReportingSearch(String division, boolean massUpdate) throws Exception {
		reporting.gotoReporting();
		reporting.expandReportSelectColumns();
		ReportSearchSelectColumns r = new ReportSearchSelectColumns();
		r.setAllOn();
		reporting.setReportSelectColumns(r);
		reporting.gotoReportSearchResults();
		List<String> customers = reporting.getReportingSearchResultsColumn("Customer Name");
		Iterator<String> i = customers.iterator();
		while(i.hasNext()) {
			String cus = i.next();
			assertEquals(customer.getCustomerName(), cus);
		}
		reporting.printThisReport();
		reporting.printAllPDFReports();
		reporting.printAllObservationReports();
		reporting.exportToExcel();
		if(massUpdate) {
			reporting.gotoMassUpdate();
			reporting.checkEndUserMassUpdate();
			Inspection inspection = new Inspection();
			inspection.setCustomer(customer.getCustomerName());
			inspection.setLocation("massupdate");
			inspection.setPrintable(true);
			if(division != null) {
				inspection.setDivision(division);
			}
			reporting.setMassUpdate(inspection);
			reporting.gotoSaveMassUpdate();
		} else {
			assertFalse("Mass update is available for a user without edit permission.", reporting.isMassUpdateAvailable());
		}
		reporting.gotoSummaryReport();
		reporting.expandSummaryReport();
	}
	
	public void testAdminEditingAnInspection() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			helperEditInspection(adminMasterProductSerialNumber, masterInspectionType, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testEditorEditingAnInspection() throws Exception {
		String method = getName();
		loginEditorUser();
		try {
			helperEditInspection(editorMasterProductSerialNumber, masterInspectionType, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testBothEditingAnInspection() throws Exception {
		String method = getName();
		loginBothUser();
		try {
			helperEditInspection(bothMasterProductSerialNumber, masterInspectionType, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreateFailingToEditingAnInspection() throws Exception {
		String method = getName();
		loginCreateUser();
		try {
			inspect.gotoInspect();
			inspect.loadAssetViaSmartSearch(createMasterProductSerialNumber);
			assertFalse("I can edit an inspection with a user without Edit permission", inspect.isInspectionEditable());
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testNeitherFailingToEditingAnInspection() throws Exception {
		String method = getName();
		loginNeitherUser();
		try {
			inspect.gotoInspect();
			inspect.loadAssetViaSmartSearch(neitherMasterProductSerialNumber);
			assertFalse("I can edit an inspection with a user without Edit permission", inspect.isInspectionEditable());
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private void helperEditInspection(String serialNumber, String inspectionType, boolean master) throws Exception {
		home.gotoProductInformationViaSmartSearch(serialNumber);
		assets.gotoInspectionGroups(serialNumber);
		List<Link> inspections = inspect.getInspectionsFromManageInspections(inspectionType);
		assertTrue("Could not find an inspection of type '" + inspectionType + "' to edit.", inspections.size() > 0);
		Link inspection = inspections.get(0);
		inspection.click();
		inspect.gotoEdit(serialNumber);
		if(master) {
			inspect.gotoEditMasterInspection(serialNumber, inspectionType);
			inspect.gotoStoreEditMasterInspection(serialNumber);
			inspect.gotoSaveMasterInspection(serialNumber);
		} else {
			inspect.gotoSaveStandardInspection(serialNumber);
		}
	}

	public void testAdminAddingASchedule() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			helperAddASchedule(adminMasterProductSerialNumber, masterInspectionType);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private void helperAddASchedule(String serialNumber, String inspectionType) throws Exception {
		String scheduleDate = misc.getDateStringNextMonth();
		home.gotoProductInformationViaSmartSearch(serialNumber);
		assets.gotoInspectionSchedule(serialNumber);
		assets.addScheduleFor(scheduleDate, inspectionType, null);
		scheduleDate = misc.getDateStringLastMonth();
		assets.addScheduleFor(scheduleDate, inspectionType, null);
		schedule.gotoSchedule();
		schedule.gotoScheduleSearchResults();
	}
	
	public void testCreateAddingASchedule() throws Exception {
		String method = getName();
		loginCreateUser();
		try {
			helperAddASchedule(createMasterProductSerialNumber, masterInspectionType);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testBothAddingASchedule() throws Exception {
		String method = getName();
		loginBothUser();
		try {
			helperAddASchedule(bothMasterProductSerialNumber, masterInspectionType);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testEditorFailingToAddASchedule() throws Exception {
		String method = getName();
		loginEditorUser();
		try {
			home.gotoProductInformationViaSmartSearch(editorMasterProductSerialNumber);
			assertFalse("I can get to Edit Schedules on a user without Create permission", assets.isSchedulesAvailable());
			misc.logout();
			loginAdminUser();
			helperAddASchedule(editorMasterProductSerialNumber, masterInspectionType);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testNeitherFailingToAddASchedule() throws Exception {
		String method = getName();
		loginNeitherUser();
		try {
			home.gotoProductInformationViaSmartSearch(neitherMasterProductSerialNumber);
			assertFalse("I can get to Edit Schedules on a user without Create permission", assets.isSchedulesAvailable());
			misc.logout();
			loginAdminUser();
			helperAddASchedule(neitherMasterProductSerialNumber, masterInspectionType);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testAdminViewingScheduleSearchResults() throws Exception {
		String method = getName();
		loginAdminUser();
		try {
			helperViewingScheduleSearchResults(masterInspectionType, adminMasterProductSerialNumber, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreateViewingScheduleSearchResults() throws Exception {
		String method = getName();
		loginCreateUser();
		try {
			helperViewingScheduleSearchResults(masterInspectionType, createMasterProductSerialNumber, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testEditorViewingScheduleSearchResults() throws Exception {
		String method = getName();
		loginEditorUser();
		try {
			helperViewingScheduleSearchResults(masterInspectionType, editorMasterProductSerialNumber, false);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testBothViewingScheduleSearchResults() throws Exception {
		String method = getName();
		loginBothUser();
		try {
			helperViewingScheduleSearchResults(masterInspectionType, bothMasterProductSerialNumber, true);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testNeitherViewingScheduleSearchResults() throws Exception {
		String method = getName();
		loginNeitherUser();
		try {
			helperViewingScheduleSearchResults(masterInspectionType, neitherMasterProductSerialNumber, false);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	/**
	 * Assumes we are inspecting a masterProductType
	 * 
	 * @param inspectionType
	 * @param serialNumber
	 * @throws Exception
	 */
	private void helperViewingScheduleSearchResults(String inspectionType, String serialNumber, boolean create) throws Exception {
		String nextInspectionDate = misc.getDateString();
		schedule.gotoSchedule();
		ScheduleSearchCriteria s = new ScheduleSearchCriteria();
		s.setEventTypeGroup(masterInspectionEventTypeGroup);
		s.setProductType(masterProductType);
		schedule.setScheduleSearchCriteria(s);
		schedule.expandProductSearchSelectColumns();
		ScheduleSearchSelectColumns c = new ScheduleSearchSelectColumns();
		c.setAllOn();
		schedule.setScheduleSearchColumns(c);
		schedule.gotoScheduleSearchResults();
		schedule.exportToExcel();
		if(create) {
			schedule.gotoMassUpdate();
			MassUpdateScheduleForm m = new MassUpdateScheduleForm(nextInspectionDate);
			schedule.setMassUpdate(m);
			schedule.gotoSaveMassUpdate();
		} else {
			assertFalse("Someone without create permission can mass update schedules", schedule.isMassUpdateAvailable());
		}
	}

	// test Schedule
	//		edit a schedule

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
