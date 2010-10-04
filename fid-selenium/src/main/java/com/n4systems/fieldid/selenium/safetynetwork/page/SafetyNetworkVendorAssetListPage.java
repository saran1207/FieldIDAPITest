package com.n4systems.fieldid.selenium.safetynetwork.page;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.model.Product;
import com.thoughtworks.selenium.Selenium;

public class SafetyNetworkVendorAssetListPage extends FieldIDPage {

	public SafetyNetworkVendorAssetListPage(Selenium selenium) {
		super(selenium);
	}

	public SafetyNetworkVendorPage clickAsset(String serialNumber){
		selenium.click("//a[.='" + serialNumber + "']");
		return new SafetyNetworkVendorPage(selenium);
	}
	
	public boolean hasAssetList() {
		return selenium.isElementPresent("//table[@id='productTable']");
	}
	
	public List<Product> getAssetList() {
		List<Product> results = new ArrayList<Product>();
		
		List<String> serialNumbers = collectTableValuesUnderCellForCurrentPage(2, 1, "a");
		
		for(String serialNumber: serialNumbers) {
			Product product = new Product();
			product.setSerialNumber(serialNumber);
			results.add(product);
		}
		
		return results;
	}
	
	public SafetyNetworkRegisterAssetForm clickRegister(int line) {
		selenium.click("//tr[" + ++line + "]//a[.='Register']");
		waitForElementToBePresent("//iframe[@id='lightviewContent']");
		return new SafetyNetworkRegisterAssetForm(selenium);
	}
}
