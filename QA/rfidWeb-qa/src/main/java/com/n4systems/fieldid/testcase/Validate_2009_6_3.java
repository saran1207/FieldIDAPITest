package com.n4systems.fieldid.testcase;

import java.util.List;

import com.n4systems.fieldid.Admin;
import com.n4systems.fieldid.Assets;
import com.n4systems.fieldid.Home;
import com.n4systems.fieldid.Identify;
import com.n4systems.fieldid.Inspect;
import com.n4systems.fieldid.admin.ManageInspectionTypes;
import com.n4systems.fieldid.admin.ManageProductTypes;
import com.n4systems.fieldid.datatypes.ButtonGroup;
import com.n4systems.fieldid.datatypes.Criteria;
import com.n4systems.fieldid.datatypes.InspectionForm;
import com.n4systems.fieldid.datatypes.InspectionType;
import com.n4systems.fieldid.datatypes.Product;
import com.n4systems.fieldid.datatypes.ProductType;
import com.n4systems.fieldid.datatypes.Section;

public class Validate_2009_6_3 extends FieldIDTestCase {

	private String company;
	private String userid;
	private String password;
	private long n = System.currentTimeMillis();
	private String masterInspectionType = "n4master-inspection-" + n;
	private String masterProductType = "n4master-" + n;
	private String subProductType = "n4sub-" + n;
	private Product master;
	private Product sub;

	protected void setUp() throws Exception {
		super.setUp();
		company = prop.getProperty("company", "NOT SET");
		userid = prop.getProperty("userid", "NOT SET");
		password = prop.getProperty("password", "NOT SET");
	}
	
	/**
	 * When you try to attach the same instance of a subproduct to
	 * a master product it should pop up a dialog telling you the
	 * subproduct is already attached to the master product.
	 * 
	 * @throws Exception
	 */
	public void test_attempts_to_add_the_same_product_as_a_sub_product_twice_and_check_for_a_warning() throws Exception {
		String dialogMsg = "That product is already attached.";
		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			
			createMasterInspectionType();
			createProductTypes();
			createProducts();
			configureProducts();
						
			// try to add the sub product to the master product a second time
			String subProductSerialNumber = sub.getSerialNumber();
			Assets assets = new Assets(ie);
			assets.addSubProductToMasterProduct(subProductType, subProductSerialNumber);
			
			// confirm the second attempt fails
			String dialogText = misc.getDialogText();
			assertTrue(dialogText.contains(dialogMsg));
			misc.okDialog();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		} finally {
			cleanup();
		}
	}
	
	private void cleanup() throws Exception {
		// delete the sub product type
		Admin admin = new Admin(ie);
		admin.gotoAdministration();
		ManageProductTypes mpts = new ManageProductTypes(ie);
		mpts.gotoManageProductTypes();
		mpts.gotoEditProductType(subProductType);
		mpts.deleteProductType(subProductType);
		mpts.confirmDeleteProductType();

		// delete the master product type
		mpts.gotoEditProductType(masterProductType);
		mpts.deleteProductType(masterProductType);
		mpts.confirmDeleteProductType();
	}
	
	private void configureProducts() throws Exception {
		// add the sub product to the master product
		String masterSerialNumber = master.getSerialNumber();
		String subProductSerialNumber = sub.getSerialNumber();
		Home home = new Home(ie);
		home.gotoProductInformationViaSmartSearch(masterSerialNumber);
		Assets assets = new Assets(ie);
		assets.gotoProductConfiguration(masterSerialNumber);
		assets.addSubProductToMasterProduct(subProductType, subProductSerialNumber);
	}

	private void createProducts() throws Exception {
		// create a sub product
		Identify identify = new Identify(ie);
		identify.gotoAddProduct();
		sub = new Product(misc.getDateString());
		sub.setProductType(subProductType);
		identify.setProduct(sub, true);
		identify.addProductSave();
		
		// create a master product
		master = new Product(misc.getDateString());
		master.setProductType(masterProductType);
		identify.setProduct(master, true);
		identify.addProductSave();
	}
	
	private void createMasterInspectionType() throws Exception {
		InspectionType it;
		// create a master inspection type
		Admin admin = new Admin(ie);
		admin.gotoAdministration();
		ManageInspectionTypes mits = new ManageInspectionTypes(ie);
		mits.gotoManageInspectionTypes();
		
		// if the inspection type already exists, just return
		if(mits.isInspectionType(masterInspectionType)) {
			return;
		}
		
		mits.gotoAddInspectionType();
		it = new InspectionType(masterInspectionType);
		it.setMasterInspection(true);
		mits.addInspectionType(it);
		mits.gotoInspectionForm(masterInspectionType);
		mits.gotoManageButtonGroups();
		List<String> buttonGroups = mits.getButtonGroupNames();
		assertTrue("There are no button groups defined for this tenant.", buttonGroups.size() > 0);
		mits.gotoImDoneFromManageButtonGroups();
		InspectionForm form = new InspectionForm();
		Section s = new Section("Pass/Fail");
		ButtonGroup bg = new ButtonGroup(buttonGroups.get(0));
		bg.setSetsResult(true);
		Criteria c = new Criteria("Pass/Fail", bg);
		s.addCriteria(c);
		form.addSection(s);
		mits.addInspectionForm(form);
		mits.saveInspectionForm();
	}

	private void createProductTypes() throws Exception {
		ProductType npt;
		
		// create a sub product type
		Admin admin = new Admin(ie);
		admin.gotoAdministration();
		ManageProductTypes mpts = new ManageProductTypes(ie);
		mpts.gotoManageProductTypes();
		mpts.gotoAddProductType();
		npt = new ProductType(subProductType);
		mpts.setAddProductTypeForm(npt);
		mpts.addProductType(subProductType);
		
		// create a master product type
		mpts.gotoAddProductType();
		npt = new ProductType(masterProductType);
		mpts.setAddProductTypeForm(npt);
		mpts.addProductType(masterProductType);
		
		// add a sub component to the master product type
		mpts.gotoSubComponents(masterProductType);
		mpts.addSubComponent(subProductType);
		mpts.saveSubComponents(masterProductType);
		
		// add a master inspection type to the master product
		mpts.gotoInspectionTypes(masterProductType);
		mpts.setInspectionType(masterInspectionType);
		mpts.saveInspectionTypes(masterProductType);
	}
	
	public void test_removing_a_sub_product_from_a_master_during_the_system_() throws Exception {
		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			createMasterInspectionType();
			createProductTypes();
			createProducts();
			configureProducts();
			
			// inspect the master product
			Inspect inspect = new Inspect(ie);
			inspect.gotoInspect();
			String serialNumber = master.getSerialNumber();
			inspect.loadAssetViaSmartSearch(serialNumber);
			inspect.gotoStartNewInspection(masterInspectionType, true);
			String label = "";
			inspect.deleteSubProductDuringMasterInspection(subProductType, label);
			// confirm the removal went okay
			Home home = new Home(ie);
			home.gotoProductInformationViaSmartSearch(serialNumber);
			Assets assets = new Assets(ie);
			List<String> subProducts = assets.getSubProducts();
			assertFalse("The master product still has a sub-product", subProducts.contains(subProductType));
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		} finally {
			cleanup();
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
