package com.n4systems.fieldid.selenium.pages.setup.eventtypes;

import static org.junit.Assert.*;

import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.EventType;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class EventTypeAddEditPage extends FieldIDPage {

	public EventTypeAddEditPage(Selenium selenium) {
		super(selenium);
	}
	
	public String getNameFieldValue() {
		return selenium.getValue("//input[@name='name']");
	}

	public void setFormFields(EventType eventType) {
		if(eventType.getName() != null) {
			selenium.type("//input[@name='name']", eventType.getName());
		}
		if(eventType.getGroup() != null) {
			selenium.select("//select[@name='group']", eventType.getGroup());
		}
		if (eventType.isPrintable()) {
			selenium.check("//input[@name='printable']");
		}
		if(eventType.isMasterEvent()) {
			selenium.check("//input[@name='master']");
		}
		if (eventType.isAssignedToAvailable()) {
			selenium.check("//input[@name='assignedToAvailable']");
		}
		if(eventType.getSupportedProofTestTypes() != null) {
			for (String proofType : eventType.getSupportedProofTestTypes()) {
				selenium.check("//input[contains(@name,'" + proofType + "') and @value='true']");
			}
		}
		if(eventType.getEventAttributes() != null) {
			int attributeCount = 0;
			for (String attribute : eventType.getEventAttributes()) {
				clickAddAttribute();
				selenium.type("//input[@name='infoFields[" + attributeCount + "]']", attribute);
				attributeCount++;
			}
		}
	}
	
	public EventTypeAddEditPage clickAddAttribute() {
		selenium.click("//button[text()='Add Attribute']");
		waitForAjax();
		return this;
	}
	
	public EventTypeAddEditPage clickDelete() {
		selenium.click("//a[contains(.,'Delete')]");
		waitForPageToLoad();
		return this;
	}
	
	public EventTypeViewPage clickSave(){
		selenium.click("//input[@name='save']");
		throwExceptionOnFormError(true);
		return new EventTypeViewPage(selenium, false);
	}

	public EventTypeFormPage clickSaveAndAdd(){
		selenium.click("//input[@name='saveAndAdd']");
		throwExceptionOnFormError(true);
        return new EventTypeFormPage(selenium, false);
	}
	
	public EventTypeViewAllPage clickCancel(){
		selenium.click("//a[text()='Cancel']");
		return new EventTypeViewAllPage(selenium);
	}

	public EventTypeViewAllPage clickConfirmDelete() {
		selenium.click("//input[@name='label.delete']");
		return new EventTypeViewAllPage(selenium);
	}
	
	public void verifyEventTypeSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Type Saved", actionMessages.get(0).trim());
	}
}
