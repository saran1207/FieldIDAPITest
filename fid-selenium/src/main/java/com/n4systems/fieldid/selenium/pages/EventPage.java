package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class EventPage extends FieldIDPage {

	public EventPage(Selenium selenium) {
		super(selenium);
		if (!checkOnEventPage() && !checkOnQuickEventPage() && !checkOnStartEventPage()) {
			fail("Expected to be on event page!");
		}
	}

	public boolean checkOnQuickEventPage(){
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Perform an Event on ')]");
	}

	public boolean checkOnStartEventPage(){
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Start Event')]");
	}
	
	public boolean checkOnEventPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Event -')]") 
			|| selenium.isElementPresent("//form[@id='eventCreate']") 
			|| selenium.isElementPresent("//form[@id='eventUpdate']");
	}

	public AssetPage clickAssetInformationTab() {
		clickNavOption("Asset Information", false);
        return new AssetPage(selenium);
	}

	public void clickMandatoryEventToPerformLink() {
		selenium.click("//a[@class='exitLink']");
		waitForPageToLoad();
	}

	public void clickStore() {
		selenium.click("//input[@value='Store']");
		waitForPageToLoad();
	}

	public void clickSaveMasterEvent() {
		selenium.click("//input[@id='subAssetForm_label_save']");
		waitForPageToLoad();
	}

	public void clickSaveNormalEvent() {
		selenium.click("//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}

	public void clickSubStartEventLink(String eventType) {
		selenium.click("//a[contains(.,'"+ eventType+"')]");
		waitForPageToLoad();
	}
	  
    public EventPage clickFirstAdHocEventType(){
    	selenium.click("//fieldset[2]/p/a");
		return new EventPage(selenium);
    }
	
	public boolean confirmMasterEventSaved() {
		return selenium.isElementPresent("//span[contains(.,'Master Event Saved.')]");
	}

    public void enterTextCriteria(String criteriaName, String criteriaValue) {
        selenium.type("//label[contains(@class,'eventFormLabel') and .='"+criteriaName+"']/..//input[@class='criteriaTextField']", criteriaValue);
    }

    public void enterPrimaryUnitOfMeasureValue(String criteriaName, String value) {
        selenium.type("//label[contains(@class,'eventFormLabel') and .='"+criteriaName+"']/..//input[contains(@id,'textValue')]", value);
    }

    public void enterSecondaryUnitOfMeasureValue(String criteriaName, String value) {
        selenium.type("//label[contains(@class,'eventFormLabel') and .='"+criteriaName+"']/..//input[contains(@id,'secondaryTextValue')]", value);
    }
    
  
}
