package com.n4systems.fieldid.testcase;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import watij.runtime.ie.IE;
import com.n4systems.fieldid.*;
import com.n4systems.fieldid.admin.*;
import com.n4systems.fieldid.datatypes.*;

//
// WEB_186 refactor
//
public class Stub extends TestCase {
	IE ie = new IE();
//	MyAccount myAccount = new MyAccount(ie);
	FieldIDMisc misc = new FieldIDMisc(ie);
	Login login = new Login(ie);
	Home home = new Home(ie);
	Admin admin = new Admin(ie);
	ManageProductTypes mpts = new ManageProductTypes(ie);
	Identify identify = new Identify(ie);
//	commonFieldIDMethods helper = new commonFieldIDMethods();
//	ManageSystemSettings mss = new ManageSystemSettings(ie);
//	Reporting reporting = new Reporting(ie);
	ManageCustomers mcs = new ManageCustomers(ie);
	ManageInspectionTypes mits = new ManageInspectionTypes(ie);
	ManageUsers mus = new ManageUsers(ie);
	Assets assets = new Assets(ie);
	ManageSystemSettings mss = new ManageSystemSettings(ie);

	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://team.n4systems.com/fieldid/";
	String company = "unirope";
	String customer = "ABB Inc.";
	String userid = "n4systems";
	String password = "makemore$";

	protected void setUp() throws Exception {
		super.setUp();
		misc.start();
		login.gotoLoginPage(loginURL);
		if (once) {
			once = false;
			timestamp = misc.createTimestampDirectory();
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
		}
	}

	public void test() throws Exception {
		String method = getName();

		try {
			
			Thread.sleep(1);
//			FileInputStream in = new FileInputStream("charge-2009.4.2.csv");
//			BufferedInputStream csv = new BufferedInputStream(in);
//			csv.close();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testDeletingRegularProductType() throws Exception {
		String method = getName();

		try {
			admin.gotoAdministration();
			mpts.gotoManageProductTypes();
			mpts.gotoAddProductType();
			String name = method + misc.getRandomInteger();
			ProductType npt = new ProductType(name);
			mpts.setAddProductTypeForm(npt);
			mpts.addProductType(name);
			mpts.gotoEditProductType2(name);
			mpts.deleteProductType(name);
			misc.myWindowCapture(timestamp + "/DEBUG-Confirm-Product-Type-Delete-" + method + "-all-rows-zero.png");
			mpts.confirmDeleteProductType();
			// TODO: Assumes product delete will happen quickly. Might need to add some sleep time
			// here. Better still would be checking for the email that gets sent when product type
			// delete has completed.
			assertFalse("The product type '" + name + "' still exists.", mpts.isProductType(name));
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testDeletingFullyDefinedProductType() throws Exception {
		String method = getName();

		try {
			admin.gotoAdministration();
			mpts.gotoManageProductTypes();
			mpts.gotoAddProductType();
			String name = method + misc.getRandomInteger();
			
			// Create a fully populated product type
			ProductType npt = new ProductType(name);
			String warnings = "delete product type warnings";
			npt.setWarnings(warnings);
			String instructions = "delete product type instructions";
			npt.setInstructions(instructions);
			String cautionsURL = "http://www.google.com";
			npt.setCautionsURL(cautionsURL);
			boolean hasManufacturerCertificate = true;
			npt.setHasManufacturerCertificate(hasManufacturerCertificate);
			String manufacturerCertificateText = "delete product type manufacturer certificate text";
			npt.setManufacturerCertificateText(manufacturerCertificateText);
			String productDescriptionTemplate = "delete product type product description template";
			npt.setProductDescriptionTemplate(productDescriptionTemplate);
			
			ProductAttribute[] attributes = new ProductAttribute[8];
			for(int i = 0; i < attributes.length; i++) {
				attributes[i] = new ProductAttribute("attrib-" + i);
				ProductAttributeType p = new ProductAttributeType();
				String[] s = new String[2];
				if(i % 2 == 0) {
					attributes[i].setRequired(true);
				}
				switch(i) {
					case 0:		// TextField, required
					case 1:		// TextField, not required
						attributes[i].setType(ProductAttributeType.TextField);
						break;
					case 2:		// Select Box, required
					case 3:		// Select Box, not required
						s[0] = "select-dropdown-0" + i;
						s[1] = "select-dropdown-1" + i;
						p.setType(ProductAttributeType.SelectBox);
						p.setDropDowns(s);
						attributes[i].setProductAttributeType(p);
						break;
					case 4:		// Combo box, required
					case 5:		// Combo box, not required
						s[0] = "combobox-dropdown-0" + i;
						s[1] = "combobox-dropdown-1" + i;
						p.setType(ProductAttributeType.ComboBox);
						p.setDropDowns(s);
						attributes[i].setProductAttributeType(p);
						break;
					case 6:		// Unit Of Measure, required
					case 7:		// Unit Of Measure, not required
						p.setType(ProductAttributeType.UnitOfMeasure);
						p.setDefaultUnitOfMeasure(ProductAttributeType.lbs);
						attributes[i].setProductAttributeType(p);
						break;
					default:
						break;
				}
			}
			npt.setAttributes(attributes);
			mpts.setAddProductTypeForm(npt);
			mpts.addProductType(name);
			mpts.gotoEditProductType2(name);
			mpts.deleteProductType(name);
			misc.myWindowCapture(timestamp + "/DEBUG-Confirm-Product-Type-Delete-" + method + "-all-rows-zero.png");
			mpts.confirmDeleteProductType();
			// TODO: Assumes product delete will happen quickly. Might need to add some sleep time
			// here. Better still would be checking for the email that gets sent when product type
			// delete has completed.
			assertFalse("The product type '" + name + "' still exists.", mpts.isProductType(name));
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testDeletingRegularProductTypeWithProducts() throws Exception {
		String method = getName();

		try {
			admin.gotoAdministration();
			mpts.gotoManageProductTypes();
			mpts.gotoAddProductType();
			String name = method + misc.getRandomInteger();
			ProductType npt = new ProductType(name);
			mpts.setAddProductTypeForm(npt);
			mpts.addProductType(name);
			identify.gotoIdentify();
			identify.gotoAdd();
			String today = misc.getDateString();
			Product p = new Product(today);
			p.setProductType(name);
			identify.addProduct(p, true, "Save");
			admin.gotoAdministration();
			mpts.gotoManageProductTypes();
			mpts.gotoEditProductType(name);
			mpts.deleteProductType(name);
			misc.myWindowCapture(timestamp + "/DEBUG-Confirm-Product-Type-Delete-" + method + "-all-rows-zero.png");
			ConfirmProductTypeDelete details = mpts.getProductTypeRemovalDetails();
			assertTrue("The number of products being deleted is not 1.", details.getProducts() == 1);
			assertTrue("The number of inspections being deleted is not 0.", details.getInspections() == 0);
			assertTrue("The number of schedules being deleted is not 0.", details.getSchedules() == 0);
			assertTrue("The number of product code mappings being deleted is not 0.", details.getProductCodeMappings() == 0);
			assertTrue("The number of product types removed as sub-product types being deleted is not 0.", details.getMasterProductTypes() == 0);
			assertTrue("The number of products being detached from master products is not 0.", details.getSubProducts() == 0);
			assertTrue("The number of subproduct, deleted from a master product, being detached is not 0.", details.getMasterProducts() == 0);
			mpts.confirmDeleteProductType();
			// TODO: Assumes product delete will happen quickly. Might need to add some sleep time
			// here. Better still would be checking for the email that gets sent when product type
			// delete has completed.
			assertFalse("The product type '" + name + "' still exists.", mpts.isProductType(name));
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testDeletingRegularProductTypeWithInspections()
			throws Exception {
		String method = getName();

		try {
			admin.gotoAdministration();
			mpts.gotoManageProductTypes();
			mpts.gotoAddProductType();
			String name = method + misc.getRandomInteger();
			ProductType npt = new ProductType(name);
			mpts.setAddProductTypeForm(npt);
			mpts.addProductType(name);
			// TODO: add an inspection type
			// TODO: configure product type with new inspection type
			identify.gotoIdentify();
			identify.gotoAdd();
			String today = misc.getDateString();
			Product p = new Product(today);
			p.setProductType(name);
			identify.addProduct(p, true, "Save");
			// TODO: inspection the new product
			admin.gotoAdministration();
			mpts.gotoManageProductTypes();
			mpts.gotoEditProductType(name);
			mpts.deleteProductType(name);
			misc.myWindowCapture(timestamp + "/DEBUG-Confirm-Product-Type-Delete-" + method + "-all-rows-zero.png");
			ConfirmProductTypeDelete details = mpts.getProductTypeRemovalDetails();
			assertTrue("The number of products being deleted is not 1.", details.getProducts() == 1);
			assertTrue("The number of inspections being deleted is not 1.", details.getInspections() == 1);
			assertTrue("The number of schedules being deleted is not 0.", details.getSchedules() == 0);
			assertTrue("The number of product code mappings being deleted is not 0.", details.getProductCodeMappings() == 0);
			assertTrue("The number of product types removed as sub-product types being deleted is not 0.", details.getMasterProductTypes() == 0);
			assertTrue("The number of products being detached from master products is not 0.", details.getSubProducts() == 0);
			assertTrue("The number of subproduct, deleted from a master product, being detached is not 0.", details.getMasterProducts() == 0);
			mpts.confirmDeleteProductType();
			// TODO: Assumes product delete will happen quickly. Might need to add some sleep time
			// here. Better still would be checking for the email that gets sent when product type
			// delete has completed.
			assertFalse("The product type '" + name + "' still exists.", mpts.isProductType(name));
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testDeletingRegularProductTypeWithInspectionsScheduled()
			throws Exception {
		String method = getName();

		try {
			admin.gotoAdministration();
			mpts.gotoManageProductTypes();
			mpts.gotoAddProductType();
			String name = method + misc.getRandomInteger();
			ProductType npt = new ProductType(name);
			mpts.setAddProductTypeForm(npt);
			mpts.addProductType(name);
			// TODO: add an inspection type
			// TODO: configure product type with new inspection type
			// TODO: add an inspection schedule to the product type
			identify.gotoIdentify();
			identify.gotoAdd();
			String today = misc.getDateString();
			Product p = new Product(today);
			p.setProductType(name);
			identify.addProduct(p, true, "Save");
			// TODO: inspection the new product
			admin.gotoAdministration();
			mpts.gotoManageProductTypes();
			mpts.gotoEditProductType(name);
			mpts.deleteProductType(name);
			misc.myWindowCapture(timestamp + "/DEBUG-Confirm-Product-Type-Delete-" + method + "-all-rows-zero.png");
			ConfirmProductTypeDelete details = mpts.getProductTypeRemovalDetails();
			assertTrue("The number of products being deleted is not 1.", details.getProducts() == 1);
			assertTrue("The number of inspections being deleted is not 1.", details.getInspections() == 1);
			assertTrue("The number of schedules being deleted is not 1.", details.getSchedules() == 1);
			assertTrue("The number of product code mappings being deleted is not 0.", details.getProductCodeMappings() == 0);
			assertTrue("The number of product types removed as sub-product types being deleted is not 0.", details.getMasterProductTypes() == 0);
			assertTrue("The number of products being detached from master products is not 0.", details.getSubProducts() == 0);
			assertTrue("The number of subproduct, deleted from a master product, being detached is not 0.", details.getMasterProducts() == 0);
			mpts.confirmDeleteProductType();
			// TODO: Assumes product delete will happen quickly. Might need to add some sleep time
			// here. Better still would be checking for the email that gets sent when product type
			// delete has completed.
			assertFalse("The product type '" + name + "' still exists.", mpts.isProductType(name));
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void testDeletingMasterProductTypeWithSubProducts() throws Exception {
		String method = getName();

		try {
			// add a regular product type
			// add a master product type, minimum requirements
			// configure subproducts for master product
			// identify an instance of new product type
			// add instances subproducts to the new product
			// delete the master product type
			// check that product type does not exist
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	// TODO: add product code mappings to a product type then delete it
	// TODO: create a master product type, subproduct type, create instances of master, add subproducts, delete master product type
	// TODO: create a master product type, subproduct type, create instances of master, add subproducts, delete subproduct type product type

	protected void tearDown() throws Exception {
		super.tearDown();
//		misc.myWindowCapture(timestamp + "/tearDown-" + getName() + ".png");
		login.close();
	}
}
