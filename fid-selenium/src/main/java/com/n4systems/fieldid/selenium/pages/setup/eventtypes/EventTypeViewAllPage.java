package com.n4systems.fieldid.selenium.pages.setup.eventtypes;

import static org.junit.Assert.*;

import java.util.List;

import com.n4systems.fieldid.selenium.pages.ButtonGroupPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class EventTypeViewAllPage extends FieldIDPage {
	
	public EventTypeViewAllPage(Selenium selenium) {
		super(selenium);
		assertTitleAndTab("Event Types", "View All");
	}
	
	public ButtonGroupPage clickButtonGroups() {
		clickNavOption("Button Groups");
		return new ButtonGroupPage(selenium, false);
	}
	
	public EventTypeAddEditPage clickAddTab() {
		clickNavOption("Add", false);
		return new EventTypeAddEditPage(selenium);
	}
	
	private String formatEventTypeRowSelector(String eventTypeName) {
		return String.format("//table[@class='list']//tr[td[1]/a[text()='%s']]", eventTypeName);
	}
	
	public EventTypeViewPage clickEventTypeName(String eventTypeName) {
		selenium.click(formatEventTypeRowSelector(eventTypeName) + "/td[1]/a");
		return new EventTypeViewPage(selenium);
	}
	
	public EventTypeAddEditPage clickEventTypeEdit(String eventTypeName) {
		selenium.click(formatEventTypeRowSelector(eventTypeName) + "//a[text()='Edit']");
		return new EventTypeAddEditPage(selenium);
	}

	public EventTypeViewAllPage clickEventTypeCopy(String eventTypeName) {
		selenium.click(formatEventTypeRowSelector(eventTypeName) + "//a[text()='Copy']");
		return new EventTypeViewAllPage(selenium);
	}
	
	public void validateCopiedEvent(String eventName) {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Your Event Type has been copied and will appear below with the name - " + eventName, actionMessages.get(0).trim());
	}
	
	public void verifyTypeDeleted() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Type is currently being deleted for you. This may take some time for the process to complete.", actionMessages.get(0).trim());
	}
	
}
