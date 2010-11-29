package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.thoughtworks.selenium.Selenium;

public class AssetPage extends FieldIDPage {

	// Locators
	private String editAssetSerialNumberTextFieldLocator = "xpath=//INPUT[@id='serialNumberText']";
	private String editAssetRFIDNumberTextFieldLocator = "xpath=//INPUT[@id='rfidNumber']";
	private String editAssetReferenceNumberTextFieldLocator = "xpath=//INPUT[@id='customerRefNumber']";
	private String editAssetLocationTextFieldLocator = "xpath=//INPUT[@id='location_freeformLocation']";
	private String editAssetAssetStatusSelectListLocator = "xpath=//SELECT[@id='assetUpdate_assetStatus']";
	private String editAssetPurchaseOrderTextFieldLocator = "xpath=//INPUT[@id='assetUpdate_purchaseOrder']";
	private String editAssetIdentifiedTextFieldLocator = "xpath=//INPUT[@id='identified']";
	private String editAssetAssetTypeTextFieldLocator = "xpath=//SELECT[@id='assetType']";
	private String editAssetPublishOverSafetyNetworkSelectListLocator = "xpath=//SELECT[@id='assetUpdate_publishedState']";
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

	public void setSchedule(String date, String eventType, String job) {
		if (date != null) {
			selenium.type("//input[@id='newSchedule_nextDate']", date);
		}
		if (eventType != null) {
			selenium.select("//select[@id='newSchedule_type']", eventType);
		}
		if (job != null) {
			selenium.select("//select[@id='newSchedule_project']", job);
		}
	}

	public boolean checkScheduleExists(String date, String eventType, String job) {
		return selenium.isElementPresent("//tbody[@id='schedules']/tr/td[text()='" + eventType + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job) + "']");
	}

	public void clickRemoveSchdeule(String date, String eventType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + eventType + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job)
				+ "']/..//a[text()='Remove']");
		waitForAjax();
	}

	public void clickEditSchedule(String date, String eventType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + eventType + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job)
				+ "']/..//a[text()='Edit']");
		waitForAjax();
	}

	public void editScheduleDate(String oldDate, String eventType, String newDate) {
		selenium.type("//tbody[@id='schedules']/tr/td[text()='" + eventType + "']/..//input[@value='" + oldDate + "']", newDate);
	}

	public void clickEditSaveSchedule(String eventType) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + eventType + "']/..//a[text()='Save']");
		waitForAjax();
	}

	public String getAssetStatus() {
		return selenium.getText("//label[.='Asset Status']/../span");
	}

	public String getSerialNumber() {
		return selenium.getText("//label[.='Serial Number']/../span");
	}

	public String getPurchaseOrder() {
		return selenium.getText("//label[.='Purchase Order']/../span");
	}

	public EventPage clickInpectNow(String date, String eventType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + eventType + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job)
				+ "']/..//a[text()='Start event now']");
		return new EventPage(selenium);
	}

	public void clickStopProgress(String date, String event, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + event + "']/..//span[text()='" + date + "']/..//span[text()='" + (job.isEmpty() ? "no job" : job)
				+ "']/..//a[text()='Stop Progress']");
	}

	public EventsPerformedPage clickEventsTab() {
		selenium.click("//a[contains(.,'Events')]");
		return new EventsPerformedPage(selenium);
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
		waitForElementToBePresent("//form[@id='subAssetSearchForm']");
		selenium.type("//input[@id='subAssetSearchForm_search']", serialNumber);
		selenium.click("//input[@id='subAssetSearchForm_load']");
		waitForElementToBePresent("//div[@id='resultsTable']");
		selenium.click("//button[@class='assetLink']");
		waitForAjax();

	}

	public void clickRemoveSubComponent() {
		selenium.click("//div[@class='subAssetAction removeSubAsset']/a");
		waitForAjax();
	}

	public void clickViewTab() {
		selenium.click("//a[contains(.,'View')]");
		waitForPageToLoad();
	}

	public void setAssetForm(Asset p) {
        MiscDriver misc = new MiscDriver((FieldIdSelenium) selenium);

		if (p.getPublished() == true) {
			selenium.select(editAssetPublishOverSafetyNetworkSelectListLocator, "Publish Over Safety Network");
		} else {
			selenium.select(editAssetPublishOverSafetyNetworkSelectListLocator, "Do Not Publish Over Safety Network");
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
		if (p.getAssetStatus() != null) {
			if (misc.isOptionPresent(editAssetAssetStatusSelectListLocator, p.getAssetStatus())) {
				selenium.select(this.editAssetAssetStatusSelectListLocator, p.getAssetStatus());
			} else {
				fail("Could not find the asset status '" + p.getAssetStatus() + "'");
			}
		}
		if (p.getPurchaseOrder() != null) {
			selenium.type(this.editAssetPurchaseOrderTextFieldLocator, p.getPurchaseOrder());
		}
		if (p.getIdentified() != null) {
			selenium.type(this.editAssetIdentifiedTextFieldLocator, p.getIdentified());
		}
		if (p.getAssetType() != null) {
			selenium.type(this.editAssetAssetTypeTextFieldLocator, p.getAssetType());
		}
		if (p.getComments() != null) {
			selenium.type(editAssetCommentTextFieldLocator, p.getComments());
		}
	}

	public void clickSave() {
		selenium.click("//input[@id='saveButton']");
		waitForPageToLoad();
	}

    public String getValueForAttribute(String attributeName) {
        return selenium.getText("//span[@infofieldname='"+attributeName+"']");
    }

	public void clickMerge() {
		selenium.click("//a[contains(.,'Merge')]");
		waitForPageToLoad();
		selenium.click("//input[@id='label_confirm_as_losing_asset']");
	}
	
	public void loadAssetToMergeIntoAndSubmit(String serialNumber){
		selenium.type("//input[@id='mergeSmartSearch_search']", serialNumber);
		selenium.click("//input[@id='mergeSmartSearch_load']");
		waitForAjax();
		selenium.click("//button[@class='assetLink']");
		selenium.click("//input[@id='merge']");
		waitForPageToLoad();
	}

    public void enterAttributeValue(String attrName, String value) {
        selenium.type("//div[@infofieldname='"+attrName+"']//input[@type='text']", value);
    }

    public void selectAttributeValue(String attrName, String value) {
        selenium.select("//div[@infofieldname='"+attrName+"']//select", value);
    }

    public boolean wasMergeSuccessful(String firstSerialNumber, String mergeSerialNumber) {
        return selenium.isElementPresent("//h1[contains(.,'Merge Assets - " + firstSerialNumber + " into " + mergeSerialNumber + "')]");
    }
}
