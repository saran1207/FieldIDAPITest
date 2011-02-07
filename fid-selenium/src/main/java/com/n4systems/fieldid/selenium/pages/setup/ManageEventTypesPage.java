package com.n4systems.fieldid.selenium.pages.setup;

import com.n4systems.fieldid.selenium.datatypes.EventForm;
import com.n4systems.fieldid.selenium.datatypes.OneClickEventFormCriteria;
import com.n4systems.fieldid.selenium.datatypes.EventFormObservations;
import com.n4systems.fieldid.selenium.datatypes.EventFormSection;
import com.n4systems.fieldid.selenium.datatypes.EventType;
import com.n4systems.fieldid.selenium.pages.ButtonGroupPage;
import com.n4systems.fieldid.selenium.pages.WicketFieldIDPage;
import com.thoughtworks.selenium.Selenium;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ManageEventTypesPage extends WicketFieldIDPage {

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
        disableTheWarningThatPromptsWhenYouLeavePage();
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
        disableTheWarningThatPromptsWhenYouLeavePage();
	}

    private void disableTheWarningThatPromptsWhenYouLeavePage() {
        selenium.runScript("promptBeforeLeaving = false;");
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

	public void clickEditEventType(String eventTypeName) {
        selenium.click("//table[@class='list']//tr//td[position()=1]//a[text()='"+eventTypeName+"']/../../td[6]//a[.='Edit']");
		waitForPageToLoad();
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
		selenium.click("//a[contains(.,'Delete')]");
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
	
	public void setEventFormFields(EventForm eventForm) {
		if (!eventForm.getSections().isEmpty()) {
			for (EventFormSection section : eventForm.getSections()) {
                addCriteriaSection(section.getSectionName());
				if(!section.getCriteria().isEmpty()) {
					addCriteria(section.getCriteria());
				}
			}
		}
	}

    public void addCriteriaSection(String name) {
        selenium.selectFrame("//iframe");
        selenium.type("//div[@id='criteriaSectionsPanel']//input[@name='sectionNameField']", name);
        selenium.click("//div[@id='criteriaSectionsPanel']//button[.='Add']");
        waitForWicketAjax();
        selenium.selectFrame("relative=up");
    }

    public List<String> getCriteriaNames() {
        selenium.selectFrame("//iframe");

        int numSections = selenium.getXpathCount("//div[@id='criteriaPanel']//div[contains(@class,'criteria')]").intValue();
        List<String> sectionNames = new ArrayList<String>();
        for (int i = 1; i <= numSections; i++) {
            sectionNames.add(selenium.getText("//div[@id='criteriaPanel']//div[contains(@class,'criteria')]["+i+"]//div[@class='itemLinkTitle']//a//span"));
        }

        selenium.selectFrame("relative=up");
        return sectionNames;
    }

    public List<String> getCriteriaSectionNames() {
        selenium.selectFrame("//iframe");

        int numSections = selenium.getXpathCount("//div[@id='criteriaSectionsPanel']//div[contains(@class,'criteriaSections')]").intValue();
        List<String> sectionNames = new ArrayList<String>();
        for (int i = 1; i <= numSections; i++) {
            sectionNames.add(selenium.getText("//div[@id='criteriaSectionsPanel']//div[contains(@class,'criteriaSections')]["+i+"]//div[@class='itemLinkTitle']//a//span"));
        }

        selenium.selectFrame("relative=up");
        return sectionNames;
    }

    public void clickCriteriaSection(String criteriaSectionName) {
        selenium.selectFrame("//iframe");

        selenium.click("//div[@id='criteriaSectionsPanel']//div[contains(@class,'criteriaSections')]//div[@class='itemLinkTitle']//a//span[text()='"+criteriaSectionName+"']");
        waitForWicketAjax();

        selenium.selectFrame("relative=up");
    }

	private void addCriteria(List<OneClickEventFormCriteria> criterion) {
		for (OneClickEventFormCriteria criteria : criterion) {
            addCriteriaNamed(criteria.getCriteriaLabel());
			if(criteria.getButtonGroup() != null) {
				selenium.select("//div[@id='criteriaEditor']//select[@name='stateSetSelect']", criteria.getButtonGroup());
			}
			if(criteria.isSetsResult()) {
                selenium.check("//div[@id='criteriaEditor']//input[@name='setsResultCheckbox']");
			} else {
                selenium.uncheck("//div[@id='criteriaEditor']//input[@name='setsResultCheckbox']");
            }
			if(criteria.getObservations() != null) {
				addObservations(criteria.getObservations());
			}
		}
	}

    private void addCriteriaNamed(String criteriaLabel) {
        addCriteriaNamed(criteriaLabel, "One-Click Button");
    }

    private void addCriteriaNamed(String criteriaLabel, String type) {
        selenium.selectFrame("//iframe");
        selenium.type("//div[@id='criteriaPanel']//input[@name='criteriaName']", criteriaLabel);
        selenium.select("//div[@id='criteriaPanel']//select[@name='criteriaType']", type);
        selenium.click("//div[@id='criteriaPanel']//button[.='Add']");
        waitForWicketAjax();
        selenium.selectFrame("relative=up");
    }

    private void addObservations(EventFormObservations observations) {
        selenium.selectFrame("//iframe");
		if(!observations.getRecommendations().isEmpty()) {
			for(String rec : observations.getRecommendations()) {
                selenium.type("//div[.='Pre-Configured Recommendations']/following-sibling::div[1]//input[@name='string']", rec);
                selenium.click("//div[.='Pre-Configured Recommendations']/following-sibling::div[1]//button");
                waitForWicketAjax();
			}
		}
		if(!observations.getDeficiencies().isEmpty()) {
			for(String def : observations.getRecommendations()) {
                selenium.type("//div[.='Pre-Configured Deficiencies']/following-sibling::div[1]//input[@name='string']", def);
                selenium.click("//div[.='Pre-Configured Deficiencies']/following-sibling::div[1]//button");
                waitForWicketAjax();
			}
		}
        selenium.selectFrame("relative=up");
	}

	public void clickSaveEventForm() {
        selenium.selectFrame("//iframe");
        selenium.click("//a[contains(text(), 'Done, Save and Finish')]");
        waitForWicketAjax();
        selenium.selectFrame("relative=up");
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
	
	public ButtonGroupPage clickButtonGroups(){
		selenium.click("//div[@id='contentHeader']/ul/li[2]/a");
		return new ButtonGroupPage(selenium);
	}

    public void clickReorderSections() {
        selenium.selectFrame("//iframe");
        selenium.click("//a[contains(.,'Reorder Sections')]");
        waitForWicketAjax();
        selenium.selectFrame("relative=up");
    }

    public void dragSectionToPosition(String sectionName, int position) {
        dragSortableItem(sectionName, position, "criteriaSectionsPanel", "criteriaSections");
    }

    public void dragCriteriaToPosition(String criteriaName, int position) {
        dragSortableItem(criteriaName, position, "criteriaPanel", "criteria");
    }

    public void clickReorderCriteria() {
        selenium.selectFrame("//iframe");
        selenium.click("//a[contains(.,'Reorder Criteria')]");
        waitForWicketAjax();
        selenium.selectFrame("relative=up");
    }

    private void dragSortableItem(String sectionName, int position, String containerXpath, String itemClass) {
        selenium.selectFrame("//iframe");
        String sectionXpath = "//div[@id='"+containerXpath+"']//div[contains(@class,'"+itemClass+"')]//div[@class='itemLinkTitle']//a//span[text()='"+sectionName+"']//ancestor::div[@class='"+itemClass+"']";
        String toXpath = "//div[contains(@class,'sortableSectionContainer')]";
        if (position > 1) {
            toXpath += "//div[contains(@class,'"+itemClass+"')]["+position+"]";
        }
        dragAndDropFromTo(sectionXpath, toXpath);
        waitForWicketAjax();
        selenium.selectFrame("relative=up");
    }

    public String getTypeForCriteria(String criteriaName) {
        selenium.selectFrame("//iframe");
        String type = selenium.getText("//div[@id='criteriaPanel']//div[contains(@class,'criteria')]//div[@class='itemLinkTitle']//span[.='"+criteriaName+"']/../div[@class='subTitle']");
        selenium.selectFrame("relative=up");
        return type;
    }
}
