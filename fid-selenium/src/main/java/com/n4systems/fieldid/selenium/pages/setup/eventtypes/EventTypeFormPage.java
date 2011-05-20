package com.n4systems.fieldid.selenium.pages.setup.eventtypes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.EventForm;
import com.n4systems.fieldid.selenium.datatypes.EventFormObservations;
import com.n4systems.fieldid.selenium.datatypes.EventFormSection;
import com.n4systems.fieldid.selenium.datatypes.OneClickEventFormCriteria;
import com.n4systems.fieldid.selenium.pages.ButtonGroupPage;
import com.n4systems.fieldid.selenium.pages.WicketFieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class EventTypeFormPage extends WicketFieldIDPage {

    public EventTypeFormPage(Selenium selenium) {
		this(selenium, true);
	}
    
    public EventTypeFormPage(Selenium selenium, boolean waitForLoad) {
		super(selenium, waitForLoad);
		
		assertTitleAndTab("Manage Event Type", "Event Form");
		disableTheWarningThatPromptsWhenYouLeavePage();
	}
    
    private void disableTheWarningThatPromptsWhenYouLeavePage() {
        selenium.runScript("promptBeforeLeaving = false;");
    }
	
	public boolean checkOnManageEventTypesPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Event Types')]");
	}

	public void clickEditEventType(String eventTypeName) {
        selenium.click("//table[@class='list']//tr//td[position()=1]//a[text()='"+eventTypeName+"']/../../td[6]//a[.='Edit']");
		waitForPageToLoad();
	}
	
	public boolean checkPageHeaderText(String eventName) {
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'" + eventName + "')]");
	}

	public boolean listItemExists(String name) {
		return selenium.isElementPresent("//table[@class='list']//a[text()='" + name + "']");
	}
	
	public void clickListItem(String name) {
		selenium.click("//table[@class='list']//a[text()='" + name + "']");
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
        selenium.type("//div[@id='criteriaSectionsPanel']//input[@name='sectionNameField']", name);
        selenium.click("//div[@id='criteriaSectionsPanel']//button[.='Add']");
        waitForWicketAjax();
    }

    public List<String> getCriteriaNames() {
        int numSections = selenium.getXpathCount("//div[@id='criteriaPanel']//div[contains(@class,'criteria')]").intValue();
        List<String> sectionNames = new ArrayList<String>();
        for (int i = 1; i <= numSections; i++) {
            sectionNames.add(selenium.getText("//div[@id='criteriaPanel']//div[contains(@class,'criteria')]["+i+"]//div[@class='itemLinkTitle']//a//span"));
        }

        return sectionNames;
    }

    public List<String> getCriteriaSectionNames() {
        int numSections = selenium.getXpathCount("//div[@id='criteriaSectionsPanel']//div[contains(@class,'criteriaSections')]").intValue();
        List<String> sectionNames = new ArrayList<String>();
        for (int i = 1; i <= numSections; i++) {
            sectionNames.add(selenium.getText("//div[@id='criteriaSectionsPanel']//div[contains(@class,'criteriaSections')]["+i+"]//div[@class='itemLinkTitle']//a//span"));
        }

        return sectionNames;
    }

    public void clickCriteriaSection(String criteriaSectionName) {
        selenium.click("//div[@id='criteriaSectionsPanel']//div[contains(@class,'criteriaSections')]//div[@class='itemLinkTitle']//a//span[text()='"+criteriaSectionName+"']");
        waitForWicketAjax();
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

    public void addCriteriaNamed(String criteriaLabel) {
        addCriteriaNamed(criteriaLabel, "One-Click Button");
    }

    public void addCriteriaNamed(String criteriaLabel, String type) {
        selenium.type("//div[@id='criteriaPanel']//input[@name='criteriaName']", criteriaLabel);
        selenium.select("//div[@id='criteriaPanel']//select[@name='criteriaType']", type);
        selenium.click("//div[@id='criteriaPanel']//button[.='Add']");
        waitForWicketAjax();
    }

    private void addObservations(EventFormObservations observations) {
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
	}

	public void clickSaveEventForm() {
        selenium.click("//a[contains(text(), 'Done, Save and Finish')]");
        waitForWicketAjax();
        waitForPageToLoad();
	}

	public void verifyEventFormSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Form saved.", actionMessages.get(0).trim());
	}
	
	public ButtonGroupPage clickButtonGroups(){
		clickNavOption("Button Groups");
		return new ButtonGroupPage(selenium, false);
	}

    public void clickReorderSections() {
        selenium.click("//a[contains(.,'Reorder Sections')]");
        waitForWicketAjax();
    }

    public void dragSectionToPosition(String sectionName, int position) {
        dragSortableItem(sectionName, position, "criteriaSectionsPanel", "criteriaSections");
    }

    public void dragCriteriaToPosition(String criteriaName, int position) {
        dragSortableItem(criteriaName, position, "criteriaPanel", "criteria");
    }

    public void clickReorderCriteria() {
        selenium.click("//a[contains(.,'Reorder Criteria')]");
        waitForWicketAjax();
    }

    private void dragSortableItem(String sectionName, int position, String containerXpath, String itemClass) {
        String sectionXpath = "//div[@id='"+containerXpath+"']//div[contains(@class,'"+itemClass+"')]//div[@class='itemLinkTitle']//a//span[text()='"+sectionName+"']//ancestor::div[@class='"+itemClass+"']";
        String toXpath = "//div[contains(@class,'sortableSectionContainer')]";
        if (position > 1) {
            toXpath += "//div[contains(@class,'"+itemClass+"')]["+position+"]";
        }
        dragAndDropFromTo(sectionXpath, toXpath);
        waitForWicketAjax();
    }

    public String getTypeForCriteria(String criteriaName) {
        return selenium.getText("//div[@id='criteriaPanel']//div[contains(@class,'criteria')]//div[@class='itemLinkTitle']//span[.='"+criteriaName+"']/../div[@class='subTitle']");
    }

    public void clickCriteria(String criteriaName) {
        selenium.click("//div[@id='criteriaPanel']//div[contains(@class,'criteria')]//div[@class='itemLinkTitle']//a//span[text()='"+criteriaName+"']");
        waitForWicketAjax();
    }

    public List<String> getDropDownOptions() {
        List<String> options = new ArrayList<String>();

        int numItems = selenium.getXpathCount("//div[@id='criteriaEditor']//div[contains(@class,'stringListItem')]//div[@class='itemLinkTitle']//a//span").intValue();
        for (int i = 1; i<= numItems; i++) {
            options.add(selenium.getText("//div[@id='criteriaEditor']//div[contains(@class,'stringListItem')]//div[@class='itemLinkTitle']//a//span["+i+"]"));
        }

        return options;
    }

    public void addDropDownOption(String optionName) {
        selenium.type("//div[@id='criteriaEditor']//div[contains(@class,'addItemTitle') and text() = 'Drop Down Options']/..//input[@type='text']", optionName);
        selenium.click("//div[@id='criteriaEditor']//div[contains(@class,'addItemTitle') and text() = 'Drop Down Options']/..//button[@name='addButton']");
        waitForWicketAjax();
    }
    
    public void addComboBoxOption(String optionName) {
        selenium.type("//div[@id='criteriaEditor']//div[contains(@class,'addItemTitle') and text() = 'Combo Box Options']/..//input[@type='text']", optionName);
        selenium.click("//div[@id='criteriaEditor']//div[contains(@class,'addItemTitle') and text() = 'Combo Box Options']/..//button[@name='addButton']");
        waitForWicketAjax();
    }

    public EventTypeViewPage clickSaveAndFinishEventForm() {
        selenium.click("//div[@class='savePanel']//a[contains(., 'Save and Finish')]");
        return new EventTypeViewPage(selenium);
    }

    public void deleteCriteriaSectionNamed(String sectionName) {
        selenium.click("//div[@id='criteriaSectionsPanel']//div[contains(@class,'criteriaSections')]//div[@class='itemLinkTitle']//a//span[text()='"+sectionName+"']//ancestor::div[contains(@class, 'criteriaSections')]//div[@class='deleteLinkSection']//img");
        waitForWicketAjax();
    }

    public void renameCriteriaSectionFromTo(String oldSectionName, String newSectionName) {
        selenium.click("//div[@id='criteriaSectionsPanel']//div[contains(@class,'criteriaSections')]//div[@class='itemLinkTitle']//a//span[text()='"+oldSectionName+"']//ancestor::div[contains(@class, 'criteriaSections')]//div[@class='editCopyLinks']//a[.='Edit']");
        waitForWicketAjax();

        selenium.type("//div[@id='criteriaSectionsPanel']//input[@name='newText']", newSectionName);
        selenium.click("//div[@id='criteriaSectionsPanel']//input[@name='newText']//ancestor::div[contains(@class, 'criteriaSections')]//div[@class='editCopyLinks']//a[.='Store']");
        waitForWicketAjax();
    }

    public void copyCriteriaSectionNamed(String sectionNameToCopy) {
        selenium.click("//div[@id='criteriaSectionsPanel']//div[contains(@class,'criteriaSections')]//div[@class='itemLinkTitle']//a//span[text()='"+sectionNameToCopy+"']//ancestor::div[contains(@class, 'criteriaSections')]//div[@class='editCopyLinks']//a[.='Copy']");
        waitForWicketAjax();
    }

    public boolean isSelectedCriteriaResultSetting() {
        return selenium.isChecked("//div[@id='criteriaEditor']//div[contains(@class,'buttonGroupConfiguration')]//input[@type='checkbox']");
    }

    public void setSelectedCriteriaResultSetting(boolean resultSetting) {
        if (resultSetting) {
            selenium.check("//div[@id='criteriaEditor']//div[contains(@class,'buttonGroupConfiguration')]//input[@type='checkbox']");
        } else {
            selenium.uncheck("//div[@id='criteriaEditor']//div[contains(@class,'buttonGroupConfiguration')]//input[@type='checkbox']");
        }
        selenium.fireEvent("//div[@id='criteriaEditor']//div[contains(@class,'buttonGroupConfiguration')]//input[@type='checkbox']", "click");
        waitForWicketAjax();
    }

    public String getSelectedButtonGroup() {
        return selenium.getSelectedLabel("//div[@id='criteriaEditor']//select[@name='stateSetSelect']");
    }

    public void selectButtonGroup(String buttonGroupName) {
        selenium.select("//div[@id='criteriaEditor']//select[@name='stateSetSelect']", buttonGroupName);
        waitForWicketAjax();
    }

    public void selectPrimaryUnitOfMeasure(String unitOfMeasureName) {
        selenium.select("//div[@id='criteriaEditor']//select[@name='primaryUnitSelect']", unitOfMeasureName);
        waitForWicketAjax();
    }

    public void selectSecondaryUnitOfMeasure(String unitOfMeasureName) {
        selenium.select("//div[@id='criteriaEditor']//select[@name='secondaryUnitSelect']", unitOfMeasureName);
        waitForWicketAjax();
    }

    public String getSelectedPrimaryUnitOfMeasure() {
        return selenium.getSelectedLabel("//div[@id='criteriaEditor']//select[@name='primaryUnitSelect']");
    }

    public String getSelectedSecondaryUnitOfMeasure() {
        return selenium.getSelectedLabel("//div[@id='criteriaEditor']//select[@name='secondaryUnitSelect']");
    }

    public void verifyEventTypeSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Event Type Saved", actionMessages.get(0).trim());
	}
}
