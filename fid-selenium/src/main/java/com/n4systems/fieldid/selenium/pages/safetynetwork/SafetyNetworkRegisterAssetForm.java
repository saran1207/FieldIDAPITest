package com.n4systems.fieldid.selenium.pages.safetynetwork;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.datatypes.Owner;
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

	public void enterIdentifier(String value) {
		selenium.type("//input[contains(@id, 'identifier')]", value);
	}

	public String getIdentifier() {
		return selenium.getValue("//input[contains(@id, 'identifier')]");
	}
	
	public void openDetailedForm() {
		selenium.click("//a[@id='expand_details']");
		waitForElementToBePresent("//div[@id='extraDetails']");
	}
	
	
	public void setRFIDNumber(String value) {
		selenium.type("//input[contains(@id, 'rfidNumber')]", value);
	}
	
	public void setReferenceNumber(String value) {
		selenium.type("//input[contains(@id, 'customerRefNumber')]", value);
	}
	
	public void setLocation(String value) {
		selenium.type("//input[contains(@id, 'freeformLocation')]", value);
	}
	
	public void setAssetStatus(String value) {
		if(!selenium.isVisible("//div[@id='extraDetails']")) {
			openDetailedForm();
		}
		selenium.select("//select[@name='assetStatus']", value);
	}
	
	public void setOwner(Owner owner) {
		if(!selenium.isVisible("//div[@id='extraDetails']")) {
			openDetailedForm();
		}
		OrgPicker orgPicker = getOrgPicker();
		orgPicker.clickChooseOwner();
		orgPicker.setOwner(owner);
		orgPicker.clickSelectOwner();	
	}
	
	public void setPurchaseOrder(String value) {
		if(!selenium.isVisible("//div[@id='extraDetails']")) {
			openDetailedForm();
		}
		selenium.type("//input[contains(@id, 'purchaseOrder')]", value);
	}

	public void setNonIntegrationOrderNumber(String value) {
		if(!selenium.isVisible("//div[@id='extraDetails']")) {
			openDetailedForm();
		}
		selenium.type("//input[contains(@id, 'nonIntegrationOrderNumber')]", value);
	}
	
	public void selectComments(String value) {
		if(!selenium.isVisible("//div[@id='extraDetails']")) {
			openDetailedForm();
		}
		selenium.select("//select[@id='commentTemplateSelection']", value);
	}
	
	public void enterComments(String value) {
		if(!selenium.isVisible("//div[@id='extraDetails']")) {
			openDetailedForm();
		}
		selenium.type("//textarea[@id='comments']", value);
	}
	
	public void setAsset(Asset asset) {
		if (asset.getRFIDNumber() != null) {
			selenium.type("//input[contains(@id, 'rfidNumber')]", asset.getRFIDNumber());
		}
		if (asset.getReferenceNumber() != null) {
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
