package com.n4systems.fieldid.selenium.testcase;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageInspectionTypes;
import com.n4systems.fieldid.selenium.administration.page.ManageProductTypesDriver;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.identify.page.IdentifyPageDriver;
import com.n4systems.fieldid.selenium.login.page.Login;

public class InspectionAttributesOnSubComponentsTest extends FieldIDTestCase {

	private Login login;
	private Admin admin;
	private ManageInspectionTypes mits;
	private ManageProductTypesDriver mpts;
	private IdentifyPageDriver identify;
	
	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		admin = new Admin(selenium, misc);
		mits = new ManageInspectionTypes(selenium, misc);
		mpts = new ManageProductTypesDriver(selenium, misc);
		identify = new IdentifyPageDriver(selenium, misc);
		String company = getStringProperty("company");
		String username = getStringProperty("username");
		String password = getStringProperty("password");
		startAsCompany(company);
		login.signInAllTheWayToHome(username, password);
	}
	
	@Test
	public void updated_inspection_attributes_on_new_sub_component_should_be_saved() throws Exception {
		String masterInspectionType = getMasterInspectionType();
		String masterProductType = getMasterProductType(masterInspectionType);
		addInspectionAttributeIfNecessary(masterInspectionType);
		identifyAMasterProductToInspection(masterProductType);
		// save and inspect
	}
	
	private String identifyAMasterProductToInspection(String productType) throws InterruptedException {
		misc.gotoIdentify();
		identify.isAdd();
		identify.gotoAdd();
		Product product = new Product();
		product.setProductType(productType);
		product = identify.setAddAssetForm(product, true);
		identify.saveNewAsset();
		return product.getSerialNumber();
	}

	private void addInspectionAttributeIfNecessary(String masterInspectionType) throws InterruptedException {
		misc.gotoAdministration();
		admin.gotoManageInspectionTypes();
		mits.gotoEditInspectionType(masterInspectionType);
		List<String> attributes = mits.getInspectionAttributes();
		if(attributes.size() == 0) {
			mits.clickAddAttributeButton();
			mits.setLastAttribute("Selenium");
			mits.clickSaveButton();
		}
	}

	private String getMasterInspectionType() {
		misc.gotoAdministration();
		admin.gotoManageInspectionTypes();
		List<String> masterInspectionTypes = mits.getMasterInspectionTypeNames();
		if(masterInspectionTypes.size() > 0) {
			return masterInspectionTypes.get(0);
		}
		return null;
	}

	private String getMasterProductType(String masterInspectionType) {
		misc.gotoAdministration();
		admin.gotoManageProductTypes();
		List<String> masterProductTypes = mpts.getProductTypes();
		for(String productType : masterProductTypes) {
			mpts.gotoViewProductType(productType);
			mpts.gotoInspectionTypesProductType();
			List<String> inspectionTypesOnProductType = mpts.getInspectionTypes();
			if(inspectionTypesOnProductType.contains(masterInspectionType)) {
				return productType;
			}
			mpts.gotoViewAllProductType();
		}
		return null;
	}

}
