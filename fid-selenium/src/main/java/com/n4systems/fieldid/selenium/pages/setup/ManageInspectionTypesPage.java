package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.*;

import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.InspectionForm;
import com.n4systems.fieldid.selenium.datatypes.InspectionFormCriteria;
import com.n4systems.fieldid.selenium.datatypes.InspectionFormObservations;
import com.n4systems.fieldid.selenium.datatypes.InspectionFormSection;
import com.n4systems.fieldid.selenium.datatypes.InspectionType;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageInspectionTypesPage extends FieldIDPage {

	final static String FIRST_LIST_ITEM = "//table[@class='list']//tr[3]/td[1]/a";
	
	public ManageInspectionTypesPage(Selenium selenium) {
		super(selenium);
		if(!checkOnManageInspectionTypesPage()){
			fail("Expected to be on Manage Inspection Types page!");
		}
	}
	
	public boolean checkOnManageInspectionTypesPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Manage Inspection Types')]");
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

	public void clickInspectionFormTab() {
		clickNavOption("Inspection Form");
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
	
	public boolean checkPageHeaderText(String inspectionName) {
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'" + inspectionName + "')]");
	}

	public void setFormFields(InspectionType inspectionType) {
		if(inspectionType.getName() != null) {
			selenium.type("//input[@name='name']", inspectionType.getName());
		}
		if(inspectionType.getGroup() != null) {
			selenium.select("//select[@name='group']", inspectionType.getGroup());
		}
		if (inspectionType.isPrintable()) {
			selenium.check("//input[@name='printable']");
		}
		if(inspectionType.isMasterInspection()) {
			selenium.check("//input[@name='master']");
		}
		if (inspectionType.isAssignedToAvailable()) {
			selenium.check("//input[@name='assignedToAvailable']");
		}
		if(inspectionType.getSupportedProofTestTypes() != null) {
			for (String proofType : inspectionType.getSupportedProofTestTypes()) {
				selenium.check("//input[contains(@name,'" + proofType + "') and @value='true']");
			}
		}
		if(inspectionType.getInspectionAttributes() != null) {
			int attributeCount = 0;
			for (String attribute : inspectionType.getInspectionAttributes()) {
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

	public void setInpectionFormFields(InspectionForm inspectionForm) {
		int sectionCount = 0;
		if (!inspectionForm.getSections().isEmpty()) {
			for (InspectionFormSection section : inspectionForm.getSections()) {
				clickAddSection();
				selenium.type("//input[@name='criteriaSections[" + sectionCount + "].title']", section.getSectionName());
				if(!section.getCriteria().isEmpty()) {
					addCriteria(sectionCount, section.getCriteria());
				}
				sectionCount++;
			}
		}
	}

	private void addCriteria(int sectionCount, List<InspectionFormCriteria> criterion) {
		int criteriaCount = 0;
		for (InspectionFormCriteria criteria : criterion) {			
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

	private void addObservations(int sectionCount, int criteriaCount, InspectionFormObservations observations) {		
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

	public void clickSaveInspectionForm() {
		selenium.click("//input[@name='hbutton.save']");
		waitForPageToLoad();
	}

	public void verifyInspectionTypeSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Inspection Type Saved", actionMessages.get(0).trim());
	}

	public void verifyInspectionFormSaved() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Inspection Form saved.", actionMessages.get(0).trim());
	}
	
	public void verifyInspectionFormDeleted() {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Inspection Type is currently being deleted for you. This may take some time for the process to complete.", actionMessages.get(0).trim());
	}

	public void validateCopiedInspection(String inspectionName) {
		List <String> actionMessages = getActionMessages();
		assertFalse(actionMessages.isEmpty());
		assertEquals("Your Inspection Type has been copied and will appear below with the name - " + inspectionName, actionMessages.get(0).trim());
	}
	
}
