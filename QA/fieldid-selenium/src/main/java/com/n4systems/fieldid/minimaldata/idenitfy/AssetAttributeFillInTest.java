package com.n4systems.fieldid.minimaldata.idenitfy;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.assets.page.AssetPage;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.identify.page.IdentifyPageDriver;
import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;

public class AssetAttributeFillInTest extends LoggedInTestCase {

	private AssetPage assetPage;
	private IdentifyPageDriver identifyPage;

	public AssetAttributeFillInTest() {
		setInitialCompany("write-company");
	}
	
	@Before
	public void init() {
		identifyPage = new IdentifyPageDriver(selenium, misc);
		assetPage = new AssetPage(selenium, misc);
		identifyPage.gotoAddSingleAsset();
	}
	
	@Test
	public void should_store_a_value_in_a_text_field_during_asset_identify() throws Exception {
		Product asset = new Product();
		asset.setSerialNumber("TEST-" + new Date().getTime());
		asset.setProductType("Simple Multi Attribute Asset");
		
		identifyPage.setAddAssetForm(asset, false);
		
		identifyPage.fillInTextAttribute("Text field", "Filling in text field value");
		identifyPage.saveNewAsset();
	
		goToAsset(asset);
		
		assetPage.verifyAssetViewPageDynamicContents(asset);
		
		assetPage.verifyAttribute("Text field", "Filling in text field value");
	}
	
	@Test
	public void should_store_a_value_in_a_select_field_during_asset_identify() throws Exception {
		Product asset = new Product();
		asset.setSerialNumber("TEST-" + new Date().getTime());
		asset.setProductType("Simple Multi Attribute Asset");
		
		identifyPage.setAddAssetForm(asset, false);
		
		identifyPage.fillInSelectAttribute("select field", "option 1");
		
		identifyPage.saveNewAsset();
	
		goToAsset(asset);
		
		assetPage.verifyAssetViewPageDynamicContents(asset);
		
		assetPage.verifyAttribute("select field", "option 1");
	}
	
	@Test
	public void should_store_a_value_in_a_combo_field_with_a_static_value_during_asset_identify() throws Exception {
		Product asset = new Product();
		asset.setSerialNumber("TEST-" + new Date().getTime());
		asset.setProductType("Simple Multi Attribute Asset");
		
		identifyPage.setAddAssetForm(asset, false);
		
		identifyPage.fillInComboAttribute("combo field", "combo option 1");
		
		identifyPage.saveNewAsset();
	
		goToAsset(asset);
		
		assetPage.verifyAssetViewPageDynamicContents(asset);
		
		assetPage.verifyAttribute("select field", "option 1");
	}

	private void goToAsset(Product asset) {
		misc.setSmartSearch(asset.getSerialNumber());
		misc.submitSmartSearch();
	}
	
}
