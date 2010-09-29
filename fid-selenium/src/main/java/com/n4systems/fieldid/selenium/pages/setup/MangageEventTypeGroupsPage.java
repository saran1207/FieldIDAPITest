package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.EventTypeGroup;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class MangageEventTypeGroupsPage extends FieldIDPage {
	
	private static String FIRST_LIST_ITEM = "//table[@class='list']//tr[contains(@id, 'group_')][1]/td[1]/a";

	public MangageEventTypeGroupsPage(Selenium selenium) {
		super(selenium);
		if(!checkOnManageEventTypeGroupsPage()){
			fail("Expected to be on Manage Event Type Group page!");
		}
	}
	
	public boolean checkOnManageEventTypeGroupsPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Manage Event Type Group')]");
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
	
	public boolean checkPageHeaderText(String eventName) {
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'" + eventName + "')]");
	}
	
	public void clickListItem(String eventName) {
		selenium.click("//table[@class='list']//tr[contains(@id, 'group_')]/td[1]/a[contains(., '" + eventName + "')]");
		waitForPageToLoad();		
	}

	public void clickCancelButton() {
		selenium.click("//input[@name='label.cancel']");
		waitForPageToLoad();		
	}

	public void clickSaveButton() {
		selenium.click("//input[@name='label.save']");
		waitForPageToLoad();		
	}

	public void setEventTypeGroupFormFields(EventTypeGroup eventTypeGroup) {
		if (eventTypeGroup.getName() != null) {
			selenium.type("//input[@id='eventTypeGroupCreate_name']", eventTypeGroup.getName());
		}
		if(eventTypeGroup.getReportName() != null){
			selenium.type("//input[@id='eventTypeGroupCreate_reportTitle']", eventTypeGroup.getReportName());
		}
		if(eventTypeGroup.getPdfReportStyle() !=  null) {
			selenium.check("//ul[@class='printOutSelection']//div[@class='printOutDetails' and contains(., '"+ eventTypeGroup.getPdfReportStyle() +"')]/input");
		}
		if(eventTypeGroup.getObservationReportStyle() !=  null) {
			selenium.check("//ul[@class='printOutSelection']//div[@class='printOutDetails' and contains(., '"+ eventTypeGroup.getObservationReportStyle() +"')]/input");
		}
	}
	
	public void deleteListItem(String name) {
		selenium.click("//table[@class='list']//td[text()='" + name + "']/..//a[contains(@id, 'delete_')]");
		waitForPageToLoad();
	}
	
	public boolean listItemExists(String name) {
		return selenium.isElementPresent("//table[@class='list']//td[text()='" + name + "']");
	}

	public void verifyEventTypeSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Type Group saved.", actionMessages.get(0).trim());
	}

	public void verifyEventTypeDeleted() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Type Group deleted.", actionMessages.get(0).trim());
	}

}
