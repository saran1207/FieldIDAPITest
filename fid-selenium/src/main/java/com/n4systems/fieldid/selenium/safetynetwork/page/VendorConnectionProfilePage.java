package com.n4systems.fieldid.selenium.safetynetwork.page;

import com.n4systems.fieldid.selenium.pages.safetynetwork.AssetPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class VendorConnectionProfilePage extends FieldIDPage {

	public VendorConnectionProfilePage(Selenium selenium) {
		super(selenium);
	}

	public SafetyNetworkVendorAssetListPage clickViewPreassignedAssets() {
		selenium.click("//div[@id='preAssignedAssets']/p/a");
		return new SafetyNetworkVendorAssetListPage(selenium);
	}

	public void setAssetToSearchFor(String assetName) {
		selenium.type("//input[@id='assetSearchBox']", assetName);
	}

	public SafetyNetworkVendorAssetListPage clickSearchWithListResult() {
		selenium.click("//input[@id='searchForAssetButton']");
		return new SafetyNetworkVendorAssetListPage(selenium);
	}

	public AssetPage clickSearchWithSingleResult() {
		selenium.click("//input[@id='searchForAssetButton']");
		return new AssetPage(selenium);
	}
	
}
