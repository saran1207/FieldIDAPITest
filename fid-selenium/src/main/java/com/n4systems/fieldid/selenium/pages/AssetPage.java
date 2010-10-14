package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.thoughtworks.selenium.Selenium;

// TODO: Merge the two AssetPage classes.
public class AssetPage extends FieldIDPage {

	MiscDriver misc;

	// Locators
	private String editAssetSerialNumberTextFieldLocator = "xpath=//INPUT[@id='serialNumberText']";
	private String editAssetRFIDNumberTextFieldLocator = "xpath=//INPUT[@id='rfidNumber']";
	private String editAssetReferenceNumberTextFieldLocator = "xpath=//INPUT[@id='customerRefNumber']";
	private String editAssetLocationTextFieldLocator = "xpath=//INPUT[@id='location_freeformLocation']";
	private String editAssetProductStatusSelectListLocator = "xpath=//SELECT[@id='productUpdate_productStatus']";
	private String editAssetPurchaseOrderTextFieldLocator = "xpath=//INPUT[@id='productUpdate_purchaseOrder']";
	private String editAssetIdentifiedTextFieldLocator = "xpath=//INPUT[@id='identified']";
	private String editAssetProductTypeTextFieldLocator = "xpath=//SELECT[@id='productType']";
	private String editAssetPublishOverSafetyNetworkSelectListLocator = "xpath=//SELECT[@id='productUpdate_publishedState']";
	private String editAssetCommentTextFieldLocator = "xpath=//TEXTAREA[@id='comments']";

	public AssetPage(Selenium selenium) {
		super(selenium);
		if (!checkOnAssetPage()) {
			fail("Expected to be on asset page!");
		}
	}

	public boolean checkOnAssetPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Asset')]");
	}

	public boolean checkHeader(String serialNumber) {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'" + serialNumber + "')]");
	}

	public void clickSchedulesTab() {
		clickNavOption("Schedules");
	}

	public void clickSaveSchedule() {
		selenium.click("//input[@id='newSchedule_label_save']");
		waitForAjax();
	}

	public void setSchedule(String date, String inspectionType, String job) {
		if (date != null) {
			selenium.type("//input[@id='newSchedule_nextDate']", date);
		}
		if (inspectionType != null) {
			selenium.select("//select[@id='newSchedule_type']", inspectionType);
		}
		if (job != null) {
			selenium.select("//select[@id='newSchedule_project']", job);
		}
	}

	public boolean checkScheduleExists(String date, String inspectionType, String job) {
		return selenium.isElementPresent("//tbody[@id='schedules']/tr/td[text()='" + inspectionType + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job) + "']");
	}

	public void clickRemoveSchdeule(String date, String inspectionType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job)
				+ "']/..//a[text()='Remove']");
		waitForAjax();
	}

	public void clickEditSchedule(String date, String inspectionType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job)
				+ "']/..//a[text()='Edit']");
		waitForAjax();
	}

	public void editScheduleDate(String oldDate, String inspectionType, String newDate) {
		selenium.type("//tbody[@id='schedules']/tr/td[text()='" + inspectionType + "']/..//input[@value='" + oldDate + "']", newDate);
	}

	public void clickEditSaveSchedule(String inspectionType) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType + "']/..//a[text()='Save']");
		waitForAjax();
	}

	public String getProductStatus() {
		return selenium.getText("//label[.='Product Status']/../span");
	}

	public String getSerialNumber() {
		return selenium.getText("//label[.='Serial Number']/../span");
	}

	public String getPurchaseOrder() {
		return selenium.getText("//label[.='Purchase Order']/../span");
	}

	public InspectPage clickInpectNow(String date, String inspectionType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job)
				+ "']/..//a[text()='inspect now']");
		return new InspectPage(selenium);
	}

	public void clickStopProgress(String date, String inspectionType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job)
				+ "']/..//a[text()='Stop Progress']");
	}

	public InspectionsPerformedPage clickInspectionsTab() {
		selenium.click("//a[contains(.,'Inspections')]");
		return new InspectionsPerformedPage(selenium);
	}

	public AssetPage clickEditTab() {
		selenium.click("//a[contains(.,'Edit')]");
		return new AssetPage(selenium);
	}

	public IdentifyPage clickDelete() {
		selenium.click("//a[contains(.,'Delete')]");
		waitForPageToLoad();
		selenium.click("//input[@id='label_delete']");
		return new IdentifyPage(selenium);
	}

	public void clickSubComponentsTab() {
		selenium.click("//a[contains(.,'Sub-Components')]");
		waitForPageToLoad();
	}

	public void addNewSubcomponent(String serialNumber) {
		selenium.click("//a[contains(.,'Add New')]");
		waitForAjax();
		selenium.type("//input[@name='serialNumber']", serialNumber);
		selenium.click("//input[@value='Save']");
		waitForAjax();
	}

	public void attachExistingSubcomponent(String serialNumber) {
		selenium.click("//a[contains(.,'Find Existing')]");
		// waitForElementToBePresent("//iframe[@id='lightviewContent']");
		selenium.type("//input[@id='subProductSearchForm_search']", serialNumber);
		selenium.click("//input[@id='subProductSearchForm_load']");
	}

	public void clickRemoveSubComponent() {
		selenium.click("//div[@class='subProductAction removeSubProduct']/a");
		waitForAjax();
	}

	public void clickViewTab() {
		selenium.click("//a[contains(.,'View')]");
		waitForPageToLoad();
	}

	public void setAssetForm(Product p) {

		if (p.getPublished() == true) {
			selenium.select(editAssetPublishOverSafetyNetworkSelectListLocator, "Publish");
		} else {
			selenium.select(editAssetPublishOverSafetyNetworkSelectListLocator, "Do Not Publish");
		}
		if (p.getSerialNumber() != null) {
			selenium.type(editAssetSerialNumberTextFieldLocator, p.getSerialNumber());
		}
		if (p.getRFIDNumber() != null) {
			selenium.type(editAssetRFIDNumberTextFieldLocator, p.getRFIDNumber());
		}
		if (p.getReferenceNumber() != null) {
			selenium.type(editAssetReferenceNumberTextFieldLocator, p.getReferenceNumber());
		}
		if (p.getOwner() != null) {
			misc.gotoChooseOwner();
			misc.setOwner(p.getOwner());
			misc.gotoSelectOwner();
		}
		if (p.getLocation() != null) {
			selenium.type(this.editAssetLocationTextFieldLocator, p.getLocation());
		}
		if (p.getProductStatus() != null) {
			if (misc.isOptionPresent(editAssetProductStatusSelectListLocator, p.getProductStatus())) {
				selenium.select(this.editAssetProductStatusSelectListLocator, p.getProductStatus());
			} else {
				fail("Could not find the product status '" + p.getProductStatus() + "'");
			}
		}
		if (p.getPurchaseOrder() != null) {
			selenium.type(this.editAssetPurchaseOrderTextFieldLocator, p.getPurchaseOrder());
		}
		if (p.getIdentified() != null) {
			selenium.type(this.editAssetIdentifiedTextFieldLocator, p.getIdentified());
		}
		if (p.getProductType() != null) {
			selenium.type(this.editAssetProductTypeTextFieldLocator, p.getProductType());
		}
		if (p.getComments() != null) {
			selenium.type(editAssetCommentTextFieldLocator, p.getComments());
		}
	}

	public void clickSave() {
		selenium.click("//input[@id='saveButton']");
		waitForPageToLoad();
	}

	public void setMiscDriver(MiscDriver misc) {
		this.misc = misc;
	}
}
