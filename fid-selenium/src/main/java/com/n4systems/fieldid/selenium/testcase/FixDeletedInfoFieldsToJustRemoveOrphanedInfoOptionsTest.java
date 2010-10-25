package com.n4systems.fieldid.selenium.testcase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.fieldid.selenium.administration.page.ManageAssetTypesDriver;
import com.n4systems.fieldid.selenium.datatypes.AssetCodeMapping;
import com.n4systems.fieldid.selenium.datatypes.AssetType;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageAssetCodeMappings;
import com.n4systems.fieldid.selenium.datatypes.Attribute;
import com.n4systems.fieldid.selenium.datatypes.ComboBoxAttribute;
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
	ManageAssetTypesDriver mpts;
	ManageAssetCodeMappings mpcms;
	private String companyWithIntegration = "unirope";

	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		identify = new IdentifyPageDriver(selenium, misc);
		admin = new Admin(selenium, misc);
		mpts = new ManageAssetTypesDriver(selenium, misc);
		mpcms = new ManageAssetCodeMappings(selenium, misc);
	}

	@Test
	public void attributesCanBeDeletedFromUnusedAssetType() throws Exception {
		startAsCompany(companyWithIntegration);
		login.signInWithSystemAccount();
		AssetType assetType = createAnAssetTypeWithAttributes();
		verifyEditAssetTypeHasDeleteAttribute(assetType);
	}

	private void verifyEditAssetTypeHasDeleteAttribute(AssetType pt) throws InterruptedException {
		// we assume create asset type left us on the View tab of that asset
		// type
		mpts.gotoEditAssetType();
		mpts.deleteAssetTypeAttributes(pt.getAttributes());
	}

	private AssetType createAnAssetTypeWithAttributes() {
		String assetType = MiscDriver.getRandomString(10);
		String textFieldAttributeName = MiscDriver.getRandomString(10);
		String unitOfMeasureAttributeName = MiscDriver.getRandomString(10);
		String selectBoxAttributeName = MiscDriver.getRandomString(10);
		String comboBoxAttributeName = MiscDriver.getRandomString(10);
		misc.gotoAdministration();
		admin.gotoManageAssetTypes();
		mpts.gotoAddAssetType();
		AssetType pt = new AssetType(assetType);
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
		mpts.setAssetType(pt);
		mpts.gotoSaveAssetType();
		return pt;
	}

	@Test
	public void attributesCanOnlyBeRetiredIfUsedInAssetCodeMapping() throws Exception {
		startAsCompany(companyWithIntegration);
		login.signInWithSystemAccount();
		AssetType assetType = createAnAssetTypeWithAttributes();
		useAssetTypeInAssetCodeMapping(assetType);
		verifyEditAssetTypeHasRetireAttribute(assetType);
	}

	private void verifyEditAssetTypeHasRetireAttribute(AssetType at) throws InterruptedException {
		misc.gotoAdministration();
		admin.gotoManageAssetTypes();
		mpts.gotoEditAssetType(at.getName());
		mpts.retireAssetTypeAttributes(at.getAttributes());
	}

	private void useAssetTypeInAssetCodeMapping(AssetType assetType) {
		misc.gotoAdministration();
		admin.gotoManageAssetCodeMappings();
		mpcms.gotoAddAssetCodeMapping();
		mpcms.setAssetTypeInAssetCodeMapping(assetType.getName());
		String assetCode = MiscDriver.getRandomString(3);
		Map<Attribute, String> assetAttributes = createRandomAssetCodeMappings(assetType.getAttributes());
		AssetCodeMapping pcm = new AssetCodeMapping(assetCode, assetType.getName(), assetAttributes);
		mpcms.setAssetCodeMapping(pcm);
		mpcms.gotoSaveAssetCodeMapping();
	}

	private <T extends Attribute> Map<T, String> createRandomAssetCodeMappings(List<T> attributes) {
		Map<T, String> result = new HashMap<T, String>();

		for (T a : attributes) {
			String value = null;
			if (a instanceof SelectBoxAttribute || a instanceof ComboBoxAttribute) {
				String id = mpcms.getInputIDForAssetTypeSelectComboBoxAttributes(a.getName());
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
