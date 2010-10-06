package com.n4systems.fieldid.selenium.safetynetwork.page;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.pages.InspectPage;
import com.thoughtworks.selenium.Selenium;

public class SafetyNetworkRegisterAssetForm extends FieldIDPage{

	public SafetyNetworkRegisterAssetForm(Selenium selenium) {
		super(selenium);
	}

	public void clickRegisterAsset() {
		selenium.click("//input[@id='saveButton']");
		waitForPageToLoad();
	}
	
	public boolean isConfirmPage() {
		return selenium.isElementPresent("//button[.='OK']");
	}
	
	public SafetyNetworkVendorAssetListPage clickOk() {
		selenium.click("//button[.='OK']");
		return new SafetyNetworkVendorAssetListPage(selenium);
	}
	
	public InspectPage clickPerformFirstEvent() {
		selenium.click("//a[@id='performFirstEvent']");
		return new InspectPage(selenium);
	}
	
	public AssetPage clickViewAsset() {
		selenium.click("//a[@id='viewAsset']");
		return new AssetPage(selenium);
	}

	public void enterSerialNumber(String value) {
		selenium.type("//input[contains(@id, 'serialNumber')]", value);
	}

	public String getSerialNumber() {
		return selenium.getValue("//input[contains(@id, 'serialNumber')]");
	}
	
	public void openDetailedForm() {
		selenium.click("//a[@id='expand_details']");
		waitForElementToBePresent("//div[@id='extraDetails']");
	}

	public void setProduct(Product product) {
		if (product.getRFIDNumber() != null) {
			selenium.type("//input[contains(@id, 'rfidNumber')]", product.getRFIDNumber());
		}
		if (product.getRFIDNumber() != null) {
			selenium.type("//input[contains(@id, 'customerRefNumber')]", product.getReferenceNumber());
		} 
		if (product.getLocation() != null) {
			selenium.type("//input[contains(@id, 'freeformLocation')]", product.getLocation());
		}
		openDetailedForm();
		if (product.getProductStatus() != null) {
			selenium.select("//select[@name='productStatus']", product.getProductStatus());
		}
		if (product.getOwner() != null) {
			OrgPicker orgPicker = getOrgPicker();
			orgPicker.clickChooseOwner();
			orgPicker.setOwner(product.getOwner());
			orgPicker.clickSelectOwner();
		}
		if (product.getPurchaseOrder() != null) {
			selenium.type("//input[contains(@id, 'purchaseOrder')]", product.getPurchaseOrder());
		}
		if (product.getNonIntegrationOrderNumber() != null) {
			selenium.type("//input[contains(@id, 'nonIntegrationOrderNumber')]", product.getNonIntegrationOrderNumber());
		}
		if (product.getComments() != null) {
			selenium.select("//select[@id='commentTemplateSelection']", product.getComments());
		}
	}
}
