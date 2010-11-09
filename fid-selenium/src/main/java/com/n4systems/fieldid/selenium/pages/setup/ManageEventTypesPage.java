package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.*;

import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.EventForm;
import com.n4systems.fieldid.selenium.datatypes.EventFormCriteria;
import com.n4systems.fieldid.selenium.datatypes.EventFormObservations;
import com.n4systems.fieldid.selenium.datatypes.EventFormSection;
import com.n4systems.fieldid.selenium.datatypes.EventType;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageEventTypesPage extends FieldIDPage {

	final static String FIRST_LIST_ITEM = "//table[@class='list']//tr[3]/td[1]/a";
	
	public ManageEventTypesPage(Selenium selenium) {
		super(selenium);
		if(!checkOnManageEventTypesPage()){
			fail("Expected to be on Manage Event Types page!");
		}
	}
	
	public boolean checkOnManageEventTypesPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Event Types')]");
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

	public void clickEventFormTab() {
		clickNavOption("Event Form");
	}

	public void clickImportTab() {
		clickNavOption("Import");
	}

	public void clickSave(){
		selenium.click("//input[@name='save']");
		waitForPageToLoad();
	}
	
	public void clickSaveAndAdd(){
		selenium.click("//input[@name='saveAndAdd']");
		waitForPageToLoad();
	}
	
	public void clickCancel(){
		selenium.click("//a[text()='Cancel']");
		waitForPageToLoad();
	}
	
	public void clickAddAttribute() {
		selenium.click("//button[text()='Add Attribute']");
		waitForAjax();
	}
	
	public String getFirstListItemName() {
		return selenium.getText(FIRST_LIST_ITEM);
	}

	public String clickFirstListItem() {
		String eventName = getFirstListItemName();
		selenium.click(FIRST_LIST_ITEM);
		waitForPageToLoad();
		return eventName;
	}
		
	public String clickFirstListItemEdit() {
		String eventName = getFirstListItemName();		
		selenium.click("//table[@class='list']//tr[3]//a[text()='Edit']");
		waitForPageToLoad();
		return eventName;
	}
	
	public String clickFirstListItemCopy() {
		String eventName = getFirstListItemName();		
		selenium.click("//table[@class='list']//tr[3]//a[text()='Copy']");
		waitForPageToLoad();
		return eventName;
	}
	
	public boolean checkPageHeaderText(String eventName) {
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'" + eventName + "')]");
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

	public boolean listItemExists(String name) {
		return selenium.isElementPresent("//table[@class='list']//a[text()='" + name + "']");
	}
	
	public void clickListItem(String name) {
		selenium.click("//table[@class='list']//a[text()='" + name + "']");
		waitForPageToLoad();
	}

	public void clickDelete() {
		selenium.click("//input[@name='delete']");
		waitForPageToLoad();
	}
	
	public void clickConfirmDelete() {
		selenium.click("//input[@name='label.delete']");
		waitForPageToLoad();
	}
	
	public void clickAddSection() {
		selenium.click("//button[text()='Add Section']");
		waitForAjax();
	}
	
	public void clickAddCriteria(int section) {
		selenium.click("//div[@id='criteriaSection_" + section + "']//div[1]//button");
		waitForAjax();
	}

	public void setEventFormFields(EventForm eventForm) {
		int sectionCount = 0;
		if (!eventForm.getSections().isEmpty()) {
			for (EventFormSection section : eventForm.getSections()) {
				clickAddSection();
				selenium.type("//input[@name='criteriaSections[" + sectionCount + "].title']", section.getSectionName());
				if(!section.getCriteria().isEmpty()) {
					addCriteria(sectionCount, section.getCriteria());
				}
				sectionCount++;
			}
		}
	}

	private void addCriteria(int sectionCount, List<EventFormCriteria> criterion) {
		int criteriaCount = 0;
		for (EventFormCriteria criteria : criterion) {
			clickAddCriteria(sectionCount);
			if(criteria.getCriteriaLabel() != null) {
				selenium.type("//input[@name='criteriaSections[" + sectionCount + "].criteria[" + criteriaCount + "].displayText']", criteria.getCriteriaLabel());
			}
			if(criteria.getButtonGroup() != null) {
				selenium.select("//select[@name='criteriaSections[" + sectionCount + "].criteria[" + criteriaCount + "].states.iD']", criteria.getButtonGroup());
			}
			if(criteria.isSetsResult()) {
				selenium.check("//input[@name='criteriaSections[" + sectionCount + "].criteria[" + criteriaCount + "].principal']");
			}
			if(criteria.getObservations() != null) {
				selenium.click("//a[@id='obs_open_" + sectionCount + "_" + criteriaCount + "']");
				waitForAjax();
				addObservations(sectionCount, criteriaCount, criteria.getObservations());
			}
			criteriaCount++;
		}
	}

	private void addObservations(int sectionCount, int criteriaCount, EventFormObservations observations) {
		if(!observations.getRecommendations().isEmpty()) {
			int recCount = 0;
			for(String rec : observations.getRecommendations()) {
				selenium.click("//button[@id='addRecommendation_" + sectionCount + "_" + criteriaCount + "']");
				waitForAjax();
				selenium.type("//input[@name='criteriaSections[" + sectionCount + "].criteria[" + criteriaCount + "].recommendations[" + recCount + "]']", rec);
				recCount++;
			}
		}
		if(!observations.getDeficiencies().isEmpty()) {
			int defCount = 0;
			for(String def : observations.getRecommendations()) {
				selenium.click("//button[@id='addDeficiencies_" + sectionCount + "_" + criteriaCount + "']");
				waitForAjax();
				selenium.type("//input[@name='criteriaSections[" + sectionCount + "].criteria[" + criteriaCount + "].deficiencies[" + defCount + "]']", def);
				defCount++;
			}
		}
	}

	public void clickSaveEventForm() {
		selenium.click("//input[@name='hbutton.save']");
		waitForPageToLoad();
	}

	public void verifyEventTypeSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Type Saved", actionMessages.get(0).trim());
	}

	public void verifyEventFormSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Form saved.", actionMessages.get(0).trim());
	}
	
	public void verifyEventFormDeleted() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Type is currently being deleted for you. This may take some time for the process to complete.", actionMessages.get(0).trim());
	}

	public void validateCopiedEvent(String eventName) {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Your Event Type has been copied and will appear below with the name - " + eventName, actionMessages.get(0).trim());
	}
	
}
