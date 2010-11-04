package com.n4systems.fieldid.selenium.testcase;

import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.Asset;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageInspectionTypes;
import com.n4systems.fieldid.selenium.administration.page.ManageAssetTypesDriver;
import com.n4systems.fieldid.selenium.identify.page.IdentifyPageDriver;
import com.n4systems.fieldid.selenium.login.page.Login;

public class EventAttributesOnSubComponentsTest extends FieldIDTestCase {

	private Login login;
	private Admin admin;
	private ManageInspectionTypes mits;
	private ManageAssetTypesDriver mpts;
	private IdentifyPageDriver identify;
	
	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		admin = new Admin(selenium, misc);
		mits = new ManageInspectionTypes(selenium, misc);
		mpts = new ManageAssetTypesDriver(selenium, misc);
		identify = new IdentifyPageDriver(selenium, misc);
		String company = getStringProperty("company");
		String username = getStringProperty("username");
		String password = getStringProperty("password");
		startAsCompany(company);
		login.signInAllTheWayToHome(username, password);
	}
	
	@Test
	public void updated_event_attributes_on_new_sub_component_should_be_saved() throws Exception {
		String masterInspectionType = getMasterInspectionType();
		String masterAssetType = getMasterAssetType(masterInspectionType);
		addInspectionAttributeIfNecessary(masterInspectionType);
		identifyAMasterAssetToInspection(masterAssetType);
		// save and inspect
	}
	
	private String identifyAMasterAssetToInspection(String assetType) throws InterruptedException {
		misc.gotoIdentify();
		identify.isAdd();
		identify.gotoAdd();
		Asset asset = new Asset();
		asset.setAssetType(assetType);
		asset = identify.setAddAssetForm(asset, true);
		identify.saveNewAsset();
		return asset.getSerialNumber();
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

	private String getMasterAssetType(String masterInspectionType) {
		misc.gotoAdministration();
		admin.gotoManageAssetTypes();
		List<String> masterAssetTypes = mpts.getAssetTypes();
		for(String assetType : masterAssetTypes) {
			mpts.gotoViewAssetType(assetType);
			mpts.gotoInspectionTypesAssetType();
			List<String> inspectionTypesOnAssetType = mpts.getInspectionTypes();
			if(inspectionTypesOnAssetType.contains(masterInspectionType)) {
				return assetType;
			}
			mpts.gotoViewAllAssetType();
		}
		return null;
	}

}
