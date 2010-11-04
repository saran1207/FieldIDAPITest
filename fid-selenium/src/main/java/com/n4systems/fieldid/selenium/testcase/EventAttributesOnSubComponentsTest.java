package com.n4systems.fieldid.selenium.testcase;

import java.util.List;

import com.n4systems.fieldid.selenium.administration.page.ManageEventTypes;
import com.n4systems.fieldid.selenium.datatypes.Asset;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.administration.page.Admin;
import com.n4systems.fieldid.selenium.administration.page.ManageAssetTypesDriver;
import com.n4systems.fieldid.selenium.identify.page.IdentifyPageDriver;
import com.n4systems.fieldid.selenium.login.page.Login;

public class EventAttributesOnSubComponentsTest extends FieldIDTestCase {

	private Login login;
	private Admin admin;
	private ManageEventTypes mits;
	private ManageAssetTypesDriver mpts;
	private IdentifyPageDriver identify;
	
	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		admin = new Admin(selenium, misc);
		mits = new ManageEventTypes(selenium, misc);
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
		String masterEventType = getMasterEventType();
		String masterAssetType = getMasterAssetType(masterEventType);
		addEventAttributeIfNecessary(masterEventType);
		identifyAMasterAssetToEvent(masterAssetType);
		// save and start event
	}
	
	private String identifyAMasterAssetToEvent(String assetType) throws InterruptedException {
		misc.gotoIdentify();
		identify.isAdd();
		identify.gotoAdd();
		Asset asset = new Asset();
		asset.setAssetType(assetType);
		asset = identify.setAddAssetForm(asset, true);
		identify.saveNewAsset();
		return asset.getSerialNumber();
	}

	private void addEventAttributeIfNecessary(String masterEventType) throws InterruptedException {
		misc.gotoAdministration();
		admin.gotoManageEventTypes();
		mits.gotoEditEventType(masterEventType);
		List<String> attributes = mits.getEventAttributes();
		if(attributes.size() == 0) {
			mits.clickAddAttributeButton();
			mits.setLastAttribute("Selenium");
			mits.clickSaveButton();
		}
	}

	private String getMasterEventType() {
		misc.gotoAdministration();
		admin.gotoManageEventTypes();
		List<String> masterEventTypes = mits.getMasterEventTypeNames();
		if(masterEventTypes.size() > 0) {
			return masterEventTypes.get(0);
		}
		return null;
	}

	private String getMasterAssetType(String masterEventType) {
		misc.gotoAdministration();
		admin.gotoManageAssetTypes();
		List<String> masterAssetTypes = mpts.getAssetTypes();
		for(String assetType : masterAssetTypes) {
			mpts.gotoViewAssetType(assetType);
			mpts.gotoEventTypesAssetType();
			List<String> eventTypesOnAssetType = mpts.getEventTypes();
			if(eventTypesOnAssetType.contains(masterEventType)) {
				return assetType;
			}
			mpts.gotoViewAllAssetType();
		}
		return null;
	}

}
