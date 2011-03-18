package com.n4systems.fieldid.selenium.pages.setup.eventtypes;

import static org.junit.Assert.*;

import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class EventTypeViewPage extends FieldIDPage {

	public EventTypeViewPage(Selenium selenium) {
		this(selenium, true);
	}
	
	public EventTypeViewPage(Selenium selenium, boolean waitForLoad) {
		super(selenium, waitForLoad);
		assertTitleAndTab("Manage Event Type", "View");
	}
	
	public String getEventTypeName() {
		return selenium.getText("//p[label[text()='Name']]/span");
	}
	
	public EventTypeAddEditPage clickEditTab() {
		clickNavOption("Edit", false);
		return new EventTypeAddEditPage(selenium);
	}
	
	public EventTypeFormPage clickEventFormTab() {
		clickNavOption("Event Form", false);
		return new EventTypeFormPage(selenium);
	}
	
	public EventTypeImportPage clickImportTab() {
		clickNavOption("Import", false);
		return new EventTypeImportPage(selenium);
	}
	
	public EventTypeAssetTypeAssociationsPage clickAssetTypeAssociationsTab(){
		clickNavOption("Asset Type Associations", false);
		return new EventTypeAssetTypeAssociationsPage(selenium, true);
		
	}
	
	public void verifyEventTypeSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Type Saved", actionMessages.get(0).trim());
	}
}
