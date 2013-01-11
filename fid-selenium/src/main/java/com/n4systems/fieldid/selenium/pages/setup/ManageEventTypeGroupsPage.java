package com.n4systems.fieldid.selenium.pages.setup;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

import java.util.List;

import static org.junit.Assert.*;

public class ManageEventTypeGroupsPage extends FieldIDPage {
	
	private static String FIRST_LIST_ITEM = "//table[@class='list']//tr[contains(@id, 'group_')][1]/td[1]/a";

	public ManageEventTypeGroupsPage(Selenium selenium) {
		super(selenium);
		if(!checkOnManageEventTypeGroupsPage()){
			fail("Expected to be on Manage Event Type Group page!");
		}
	}
	
	public boolean checkOnManageEventTypeGroupsPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Manage Event Type Groups')]");
	}
	
	public void clickViewAllTab() {
		clickNavOption("View All");
	}

	public void clickEditTab() {
		clickNavOption("Edit");
	}
	
	public void clickAddTab() {
		clickNavOption("Add");
	}
	
	public String getFirstListItemName(){
		return selenium.getText(FIRST_LIST_ITEM);
	}

	public String clickFirstListItem(){
		String eventName = getFirstListItemName();
		selenium.click(FIRST_LIST_ITEM);
		waitForPageToLoad();
		return eventName;
	}
		
	public String clickFirstListItemEdit(){
		String eventName = getFirstListItemName();		
		selenium.click("//table[@class='list']//tr[contains(@id, 'group_')][1]//a[contains(@id, 'edit_')]");
		waitForPageToLoad();
		return eventName;
	}

	public void clickEditFromViewTab() {
		selenium.click("//div[contains(@class, 'bigForm')]/h2/a");
		waitForPageToLoad();
	}
	
	public void clickListItem(String eventName) {
		selenium.click("//table[@class='list']//tr[contains(@id, 'group_')]/td[1]/a[contains(., '" + eventName + "')]");
		waitForPageToLoad();		
	}

	public void clickCancelButton() {
		selenium.click("//a[contains(.,'Cancel')]");
		waitForPageToLoad();		
	}

	public void clickSaveButton() {
		selenium.click("//input[@name='label.save']");
		waitForPageToLoad();		
	}
	
	public void setName(String name) {
		selenium.type("//input[@id='eventTypeGroupCreate_name']", name);
	}

	public void setReportName(String reportName) {
		selenium.type("//input[@id='eventTypeGroupCreate_reportTitle']", reportName);
	}

	public void setPdfReportStyle(String pdfReportStyle) {
		selenium.check("//ul[@class='printOutSelection']//div[@class='printOutDetails' and contains(., '"+ pdfReportStyle +"')]/input");
	}

	public void setObservationGroupStyle(String observationReportStyle) {
		selenium.check("//ul[@class='printOutSelection']//div[@class='printOutDetails' and contains(., '"+ observationReportStyle +"')]/input");
	}
	
    public void editListItem(String name) {
        selenium.click("//table[@class='list']/tbody//tr//td/a[contains(., '"+name+"')]/../../td/a[.='Edit']");
        waitForPageToLoad();
    }
    
	public void archiveListItem(String name, boolean confirm) {
        selenium.click("//table[@class='list']/tbody//tr//td/a[contains(., '"+name+"')]/../../td/a[.='Archive']");
        waitForPageToLoad();
	}
	
	public boolean listItemExists(String name) {
		return selenium.isElementPresent("//table[@class='list']//a[text()='" + name + "']");
	}

	public void verifyEventTypeSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Type Group saved.", actionMessages.get(0).trim());
	}

}
