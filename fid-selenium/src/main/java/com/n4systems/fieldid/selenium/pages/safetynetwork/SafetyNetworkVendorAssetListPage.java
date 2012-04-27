package com.n4systems.fieldid.selenium.pages.safetynetwork;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.model.Asset;
import com.thoughtworks.selenium.Selenium;

public class SafetyNetworkVendorAssetListPage extends FieldIDPage {

	public SafetyNetworkVendorAssetListPage(Selenium selenium) {
		super(selenium);
		if(!checkOnAssetListPage()){
			fail("Expected to be on asset list page!");
		}
	}

	private boolean checkOnAssetListPage() {
		return (selenium.isElementPresent("//table[@id='assetTable']") ||
				selenium.isElementPresent("//div[@class='emptyList']"));
	}

	public SafetyNetworkVendorPage clickAsset(String identifier){
		selenium.click("//a[.='" + identifier + "']");
		return new SafetyNetworkVendorPage(selenium);
	}
	
	public boolean hasAssetList() {
		return selenium.isElementPresent("//table[@id='assetTable']");
	}
	
	public List<Asset> getAssetList() {
		List<Asset> results = new ArrayList<Asset>();
		
		List<String> identifiers = collectTableValuesUnderCellForCurrentPage(2, 1, "a");
		
		for(String identifier : identifiers) {
			Asset asset = new Asset();
			asset.setIdentifier(identifier);
			results.add(asset);
		}
		
		return results;
	}
	
	public SafetyNetworkRegisterAssetForm clickRegister(int line) {
		selenium.click("//tr[" + ++line + "]//a[.='Register']");
		waitForElementToBePresent("//iframe[@class='cboxIframe']");
		return new SafetyNetworkRegisterAssetForm(selenium);
	}

	public void clickLastPage() {
		selenium.click("//a[text()='Last']");
		waitForPageToLoad();
	}
}
