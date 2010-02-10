package com.n4systems.fieldid.testcase;

import java.util.ArrayList;
import java.util.List;

import watij.elements.Link;

import com.n4systems.fieldid.Admin;
import com.n4systems.fieldid.Assets;
import com.n4systems.fieldid.Home;
import com.n4systems.fieldid.Identify;
import com.n4systems.fieldid.Inspect;
import com.n4systems.fieldid.admin.ManageInspectionTypes;
import com.n4systems.fieldid.admin.ManageProductTypes;
import com.n4systems.fieldid.datatypes.InspectionType;
import com.n4systems.fieldid.datatypes.Product;
import com.n4systems.fieldid.datatypes.ProductAttribute;
import com.n4systems.fieldid.datatypes.ProductAttributeType;
import com.n4systems.fieldid.datatypes.ProductType;

public class Validate_2009_6_2 extends FieldIDTestCase {

	private String company;
	private String userid;
	private String password;

	protected void setUp() throws Exception {
		super.setUp();
		company = prop.getProperty("company", "NOT SET");
		userid = prop.getProperty("userid", "NOT SET");
		password = prop.getProperty("password", "NOT SET");
	}
	
	public void testWeb1095() throws Exception {
		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageProductTypes mpts = new ManageProductTypes(ie);
			mpts.gotoManageProductTypes();
			mpts.gotoAddProductType();
			String name = "WEB-1095-" + misc.getRandomString(10);
			ProductType npt = new ProductType(name);
			ProductAttribute[] attributes = new ProductAttribute[1];
			attributes[0] = new ProductAttribute(name);
			ProductAttributeType pat = new ProductAttributeType();
			attributes[0].setProductAttributeType(pat);
			npt.setAttributes(attributes);
			mpts.setAddProductTypeForm(npt);
			mpts.addProductType(name);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1023() throws Exception {
		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Identify identify = new Identify(ie);
			identify.gotoIdentify();
			identify.gotoAddMultipleAssets();
			identify.handleRequiredFieldsOnAddProduct();
			identify.addMultipleAssetsContinueToStep2();
			identify.addMultipleAssetsContinueToStep3();
			if(ie.html().contains("Do no generate serial numbers")) {
				fail("Manual section for step 3 should have Do not generate serial numbers");
			}
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1093() throws Exception {
		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Identify identify = new Identify(ie);
			identify.gotoIdentify();
			identify.gotoAddProduct();
			String identified = misc.getDateString();
			Product p = new Product(identified);
			identify.setProduct(p, true);
			String serialNumber = p.getSerialNumber();
			identify.addProductSave();
			Home home = new Home(ie);
			home.gotoProductInformationViaSmartSearch(serialNumber);
			Assets assets = new Assets(ie);
			assets.gotoEditProduct(serialNumber);
			assets.deleteProduct(serialNumber, "0", null, "0", "0");
			assets.confirmDeleteProduct();
			boolean jobsites = Boolean.parseBoolean(prop.getProperty("jobsites", "NOT SET"));
			boolean integration = Boolean.parseBoolean(prop.getProperty("integration", "NOT SET"));
			identify.validateAddProductPage(jobsites, integration);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1113() throws Exception {
		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			String inspectionTypeName = "web-1113";
			String attributeName = "web-1113-attribute";
			String attributeValue = "web-1113-passed";
			String productTypeName = "web-1113-prod";
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageInspectionTypes mits = new ManageInspectionTypes(ie);
			mits.gotoManageInspectionTypes();
			List<String> inspectionAttributes = new ArrayList<String>();
			inspectionAttributes.add(attributeName);
			if(!mits.isInspectionType(inspectionTypeName)) {
				mits.gotoAddInspectionType();
				InspectionType it = new InspectionType(inspectionTypeName);
				it.setInspectionAttributes(inspectionAttributes);
				mits.addInspectionType(it);
			}
			admin.gotoAdministration();
			ManageProductTypes mpts = new ManageProductTypes(ie);
			mpts.gotoManageProductTypes();
			ProductType npt = new ProductType(productTypeName);
			if(!mpts.isProductType(productTypeName)) {
				mpts.gotoAddProductType();
				mpts.setAddProductTypeForm(npt);
				mpts.addProductType(productTypeName);
			} else {
				mpts.gotoProductType(productTypeName);
			}
			mpts.gotoInspectionTypes(productTypeName);
			mpts.setInspectionType(inspectionTypeName);
			mpts.saveInspectionTypes(productTypeName);
			Identify identify = new Identify(ie);
			identify.gotoAddProduct();
			String identified = misc.getDateString();
			Product p = new Product(identified);
			p.setProductType(productTypeName);
			identify.setProduct(p, true);
			identify.addProductSaveAndInspect();
			String serialNumber = p.getSerialNumber();
			Inspect inspect = new Inspect(ie);
			inspect.gotoSaveStandardInspection(serialNumber);
			Home home = new Home(ie);
			home.gotoProductInformationViaSmartSearch(serialNumber);
			Assets assets = new Assets(ie);
			assets.gotoManageInspections(serialNumber);
			List<Link> inspections = inspect.getInspectionsFromManageInspections(inspectionTypeName);
			assertTrue("Could not find any inspections", inspections.size() > 0);
			inspections.get(0).click();
			inspect.gotoEdit(serialNumber);
			inspect.setEditInspectionAttribute(attributeName, attributeValue);
			inspect.gotoSaveEditStandardInspection(serialNumber);
			String s = inspect.getInspectionAttribute(attributeName);
			assertEquals(attributeValue, s);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
