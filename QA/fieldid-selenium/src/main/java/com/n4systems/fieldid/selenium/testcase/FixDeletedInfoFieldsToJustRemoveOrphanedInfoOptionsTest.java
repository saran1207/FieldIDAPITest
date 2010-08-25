package com.n4systems.fieldid.selenium.testcase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageProductCodeMappings;
import com.n4systems.fieldid.selenium.administration.page.ManageProductTypesDriver;
import com.n4systems.fieldid.selenium.datatypes.Attribute;
import com.n4systems.fieldid.selenium.datatypes.ComboBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.ProductCodeMapping;
import com.n4systems.fieldid.selenium.datatypes.ProductType;
import com.n4systems.fieldid.selenium.datatypes.SelectBoxAttribute;
import com.n4systems.fieldid.selenium.datatypes.TextFieldAttribute;
import com.n4systems.fieldid.selenium.datatypes.UnitOfMeasureAttribute;
import com.n4systems.fieldid.selenium.identify.page.IdentifyPageDriver;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

/**
 * WEB-1526
 * 
 * @author dgrainge
 * 
 */
public class FixDeletedInfoFieldsToJustRemoveOrphanedInfoOptionsTest extends FieldIDTestCase {

	Login login;
	IdentifyPageDriver identify;
	Admin admin;
	ManageProductTypesDriver mpts;
	ManageProductCodeMappings mpcms;
	private String companyWithIntegration = "unirope";

	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		identify = new IdentifyPageDriver(selenium, misc);
		admin = new Admin(selenium, misc);
		mpts = new ManageProductTypesDriver(selenium, misc);
		mpcms = new ManageProductCodeMappings(selenium, misc);
	}

	@Test
	public void attributesCanBeDeletedFromUnusedProductType() throws Exception {
		startAsCompany(companyWithIntegration);
		login.signInWithSystemAccount();
		ProductType productType = createAProductTypeWithAttributes();
		verifyEditProductTypeHasDeleteAttribute(productType);
	}

	private void verifyEditProductTypeHasDeleteAttribute(ProductType pt) throws InterruptedException {
		// we assume create product type left us on the View tab of that product
		// type
		mpts.gotoEditProductType();
		mpts.deleteProductTypeAttributes(pt.getAttributes());
	}

	private ProductType createAProductTypeWithAttributes() {
		String productType = MiscDriver.getRandomString(10);
		String textFieldAttributeName = MiscDriver.getRandomString(10);
		String unitOfMeasureAttributeName = MiscDriver.getRandomString(10);
		String selectBoxAttributeName = MiscDriver.getRandomString(10);
		String comboBoxAttributeName = MiscDriver.getRandomString(10);
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
		startAsCompany(companyWithIntegration);
		login.signInWithSystemAccount();
		ProductType productType = createAProductTypeWithAttributes();
		useProductTypeInProductCodeMapping(productType);
		verifyEditProductTypeHasRetireAttribute(productType);
	}

	private void verifyEditProductTypeHasRetireAttribute(ProductType pt) throws InterruptedException {
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
		String productCode = MiscDriver.getRandomString(3);
		Map<Attribute, String> productAttributes = createRandomProductCodeMappings(productType.getAttributes());
		ProductCodeMapping pcm = new ProductCodeMapping(productCode, productType.getName(), productAttributes);
		mpcms.setProductCodeMapping(pcm);
		mpcms.gotoSaveProductCodeMapping();
	}

	private <T extends Attribute> Map<T, String> createRandomProductCodeMappings(List<T> attributes) {
		Map<T, String> result = new HashMap<T, String>();

		for (T a : attributes) {
			String value = null;
			if (a instanceof SelectBoxAttribute || a instanceof ComboBoxAttribute) {
				String id = mpcms.getInputIDForProductTypeSelectComboBoxAttributes(a.getName());
				String locator = "xpath=//SELECT[@id='" + id + "']";
				String[] options = selenium.getSelectOptions(locator);
				value = options[1];
			} else {
				value = MiscDriver.getRandomString(6);
			}
			result.put(a, value);
		}
		return result;
	}
	
}
