package com.n4systems.fieldid.selenium.pages.safetynetwork;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.EventPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
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
	
	public EventPage clickPerformFirstEvent() {
		selenium.click("//a[@id='performFirstEvent']");
		return new EventPage(selenium);
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

	public void setAsset(Asset asset) {
		if (asset.getRFIDNumber() != null) {
			selenium.type("//input[contains(@id, 'rfidNumber')]", asset.getRFIDNumber());
		}
		if (asset.getRFIDNumber() != null) {
			selenium.type("//input[contains(@id, 'customerRefNumber')]", asset.getReferenceNumber());
		} 
		if (asset.getLocation() != null) {
			selenium.type("//input[contains(@id, 'freeformLocation')]", asset.getLocation());
		}
		openDetailedForm();
		if (asset.getAssetStatus() != null) {
			selenium.select("//select[@name='assetStatus']", asset.getAssetStatus());
		}
		if (asset.getOwner() != null) {
			OrgPicker orgPicker = getOrgPicker();
			orgPicker.clickChooseOwner();
			orgPicker.setOwner(asset.getOwner());
			orgPicker.clickSelectOwner();
		}
		if (asset.getPurchaseOrder() != null) {
			selenium.type("//input[contains(@id, 'purchaseOrder')]", asset.getPurchaseOrder());
		}
		if (asset.getNonIntegrationOrderNumber() != null) {
			selenium.type("//input[contains(@id, 'nonIntegrationOrderNumber')]", asset.getNonIntegrationOrderNumber());
		}
		if (asset.getComments() != null) {
			selenium.select("//select[@id='commentTemplateSelection']", asset.getComments());
		}
	}
}
