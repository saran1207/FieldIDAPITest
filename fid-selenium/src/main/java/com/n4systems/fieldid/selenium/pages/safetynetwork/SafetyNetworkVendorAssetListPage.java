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

	public SafetyNetworkVendorPage clickAsset(String serialNumber){
		selenium.click("//a[.='" + serialNumber + "']");
		return new SafetyNetworkVendorPage(selenium);
	}
	
	public boolean hasAssetList() {
		return selenium.isElementPresent("//table[@id='assetTable']");
	}
	
	public List<Asset> getAssetList() {
		List<Asset> results = new ArrayList<Asset>();
		
		List<String> serialNumbers = collectTableValuesUnderCellForCurrentPage(2, 1, "a");
		
		for(String serialNumber: serialNumbers) {
			Asset asset = new Asset();
			asset.setSerialNumber(serialNumber);
			results.add(asset);
		}
		
		return results;
	}
	
	public SafetyNetworkRegisterAssetForm clickRegister(int line) {
		selenium.click("//tr[" + ++line + "]//a[.='Register']");
		waitForElementToBePresent("//iframe[@id='lightviewContent']");
		return new SafetyNetworkRegisterAssetForm(selenium);
	}

	public void clickLastPage() {
		selenium.click("//a[text()='Last']");
		waitForPageToLoad();
	}
}
