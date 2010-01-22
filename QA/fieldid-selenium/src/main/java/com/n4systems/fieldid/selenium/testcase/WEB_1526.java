package com.n4systems.fieldid.selenium.testcase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.Admin;
import com.n4systems.fieldid.selenium.administration.ManageProductCodeMappings;
import com.n4systems.fieldid.selenium.administration.ManageProductTypes;
import com.n4systems.fieldid.selenium.datatypes.Attribute;
import com.n4systems.fieldid.selenium.datatypes.ComboBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.ProductCodeMapping;
import com.n4systems.fieldid.selenium.datatypes.ProductType;
import com.n4systems.fieldid.selenium.datatypes.SelectBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.TextFieldAttribute;
import com.n4systems.fieldid.selenium.datatypes.UnitOfMeasureAttribute;
import com.n4systems.fieldid.selenium.identify.Identify;
import com.n4systems.fieldid.selenium.login.Login;

public class WEB_1526 extends FieldIDTestCase {

	Login login;
	Identify identify;
	Admin admin;
	ManageProductTypes mpts;
	ManageProductCodeMappings mpcms;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		identify = new Identify(selenium, misc);
		admin = new Admin(selenium, misc);
		mpts = new ManageProductTypes(selenium, misc);
		mpcms = new ManageProductCodeMappings(selenium, misc);
	}

	@Test
	public void attributesCanBeDeletedFromUnusedProductType() throws Exception {
		String companyID = getStringProperty("company");
		String password = getStringProperty("password");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			loginAcceptingEULAIfNecessary(username, password);
			ProductType productType = createAProductTypeWithAttributes();
			verifyEditProductTypeHasDeleteAttribute(productType);
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void verifyEditProductTypeHasDeleteAttribute(ProductType pt) {
		// we assume create product type left us on the View tab of that product type
		mpts.gotoEditProductType();
		mpts.deleteProductTypeAttributes(pt.getAttributes());
	}

	private ProductType createAProductTypeWithAttributes() {
		String productType = misc.getRandomString(10);
		String textFieldAttributeName = misc.getRandomString(10);
		String unitOfMeasureAttributeName = misc.getRandomString(10);
		String selectBoxAttributeName = misc.getRandomString(10);
		String comboBoxAttributeName = misc.getRandomString(10);
		misc.gotoAdministration();
		admin.gotoManageProductTypes();
		mpts.gotoAddProductType();
		ProductType pt = new ProductType(productType);
		TextFieldAttribute tfa = new TextFieldAttribute(textFieldAttributeName, true);
		pt.addAttributes(tfa);
		UnitOfMeasureAttribute uoma = new UnitOfMeasureAttribute(unitOfMeasureAttributeName, true);
		uoma.setDefault(UnitOfMeasureAttribute.DEFAULT_KILOGRAMS);
		pt.addAttributes(uoma);
		SelectBoxAttribute sba = new SelectBoxAttribute(selectBoxAttributeName, true);
		sba.addDropDown("one");
		pt.addAttributes(sba);
		ComboBoxAttribute cba = new ComboBoxAttribute(comboBoxAttributeName, true);
		cba.addDropDown("two");
		pt.addAttributes(cba);
		mpts.setProductType(pt);
		mpts.gotoSaveProductType();
		return pt;
	}

	@Test
	public void attributesCanOnlyBeRetiredIfUsedInProductCodeMapping() throws Exception {
		String companyID = getStringProperty("company");
		String password = getStringProperty("password");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			loginAcceptingEULAIfNecessary(username, password);
			ProductType productType = createAProductTypeWithAttributes();
			useProductTypeInProductCodeMapping(productType);
			verifyEditProductTypeHasRetireAttribute(productType);
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void verifyEditProductTypeHasRetireAttribute(ProductType pt) {
		misc.gotoAdministration();
		admin.gotoManageProductTypes();
		mpts.gotoEditProductType(pt.getName());
		mpts.retireProductTypeAttributes(pt.getAttributes());
	}

	private void useProductTypeInProductCodeMapping(ProductType productType) {
		misc.gotoAdministration();
		admin.gotoManageProductCodeMappings();
		mpcms.gotoAddProductCodeMapping();
		mpcms.setProductTypeInProductCodeMapping(productType.getName());
		String productCode = misc.getRandomString(3);
		Map<Attribute,String> productAttributes = createRandomProductCodeMappings(productType.getAttributes());
		ProductCodeMapping pcm = new ProductCodeMapping(productCode, productType.getName(), productAttributes);
		mpcms.setProductCodeMapping(pcm);
		mpcms.gotoSaveProductCodeMapping();
	}

	private <T extends Attribute> Map<T, String> createRandomProductCodeMappings(List<T> attributes) {
		Map<T,String> result = new HashMap<T,String>();
		
		for (T a: attributes) {
			String value = null;
			if(a instanceof SelectBoxAttribute || a instanceof ComboBoxAttribute) {
				String id = mpcms.getInputIDForProductTypeSelectComboBoxAttributes(a.getName());
				String locator = "xpath=//SELECT[@id='" + id + "']";
				String[] options = selenium.getSelectOptions(locator);
				value = options[1];
			} else {
				value = misc.getRandomString(6);
			}
			result.put(a, value);
		}
		return result;
	}

	@Test
	public void attributesCanOnlyBeRetiredIfUsedInAutoAttribute() throws Exception {
		String companyID = getStringProperty("company");
		String password = getStringProperty("password");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			loginAcceptingEULAIfNecessary(username, password);
			// create a product type with select box attributes
			// save it
			// use the product type in an auto attribute
			// edit product type and retire the select box
			// fail if I can 'Delete' the attribute
		} catch(Exception e) {
			throw e;
		}
	}
	
	@Test
	public void attributesCanOnlyBeRetiredIfUsedInExistingProduct() throws Exception {
		String companyID = getStringProperty("company");
		String password = getStringProperty("password");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			loginAcceptingEULAIfNecessary(username, password);
			// create a product type with select box attributes
			// save it
			// use the product type to create a product
			// edit product type and retire the select box
			// fail if I can 'Delete' the attribute
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void loginAcceptingEULAIfNecessary(String username, String password) {
		login.setUserName(username);
		login.setPassword(password);
		login.gotoSignIn();
		if(misc.isEULA()) {
			misc.scrollToBottomOfEULA();
			misc.gotoAcceptEULA();
		}
		login.verifySignedIn();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
