package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class AssetPage extends FieldIDPage {

	public AssetPage(Selenium selenium) {
		super(selenium);
		if(!checkOnAssetPage()){
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
		return selenium.isElementPresent("//tbody[@id='schedules']/tr/td[text()='" + inspectionType
				+ "']/..//span[text()='" + date 
				+ "']/..//span[text()='" + (job.isEmpty() ? "no job" : job) + "']");
	}

	public void clickRemoveSchdeule(String date, String inspectionType,	String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType
				+ "']/..//span[text()='" + date 
				+ "']/..//span[text()='" + (job.isEmpty() ? "no job" : job) 
				+ "']/..//a[text()='Remove']");
		waitForAjax();
	}

	public void clickEditSchedule(String date, String inspectionType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType
				+ "']/..//span[text()='" + date 
				+ "']/..//span[text()='" + (job.isEmpty() ? "no job" : job) 
				+ "']/..//a[text()='Edit']");
		waitForAjax();
	}

	public void editScheduleDate(String oldDate, String inspectionType, String newDate) {
		selenium.type("//tbody[@id='schedules']/tr/td[text()='" + inspectionType
				+ "']/..//input[@value='" + oldDate 
				+ "']", newDate);
	}

	public void clickEditSaveSchedule(String inspectionType) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType
				+ "']/..//a[text()='Save']");
		waitForAjax();
	}

    public String getProductStatus() {
        return selenium.getText("//label[.='Product Status']/../span");
    }

    public String getPurchaseOrder() {
        return selenium.getText("//label[.='Purchase Order']/../span");
    }

	public InspectPage clickInpectNow(String date, String inspectionType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType
				+ "']/..//span[text()='" + date 
				+ "']/..//span[text()='" + (job.isEmpty() ? "no job" : job) 
				+ "']/..//a[text()='inspect now']");
		return new InspectPage(selenium);
	}

	public void clickStopProgress(String date, String inspectionType, String job) {
		selenium.click("//tbody[@id='schedules']/tr/td[text()='" + inspectionType
				+ "']/..//span[text()='" + date 
				+ "']/..//span[text()='" + (job.isEmpty() ? "no job" : job) 
				+ "']/..//a[text()='Stop Progress']");
	}

	public InspectionsPerformedPage clickInspectionsTab() {
		selenium.click("//a[contains(.,'Inspections')]");
		return new InspectionsPerformedPage(selenium);
	}
}
