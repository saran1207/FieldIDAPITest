package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.EventType;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ManageInspectionTypes {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String manageInspectionTypesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Inspection Types')]";
	private String inspectionTypeTableXpath = "//div[@id='pageContent']/table";
	private String inspectionTypeTableRowCountXpath = inspectionTypeTableXpath + "/tbody/tr/td[1]/a[1]";
	private String editInspectionTypeNameTextFieldLocator = "css=#inspectionTypeUpdate_name";
	private String editInspectionTypeGroupSelectListLocator = "css=#inspectionTypeUpdate_group";
	private String editInspectionTypePrintableCheckboxLocator = "css=#inspectionTypeUpdate_printable";
	private String editInspectionTypeMasterInspectionCheckboxLocator = "css=#inspectionTypeUpdate_master";
	private String editInspectionTypeRobertLogFileOnRadioButtonLocator = "xpath=//input[contains(@id,'ROBERTS') and contains(@id,'true')]";
	private String editInspectionTypeRobertLogFileOffRadioButtonLocator = "xpath=//input[contains(@id,'ROBERTS') and contains(@id,'false')]";
	private String editInspectionTypeNAExcelFileOnRadioButtonLocator = "xpath=//input[contains(@id,'NATIONALAUTOMATION') and contains(@id,'true')]";
	private String editInspectionTypeNAExcelFileOffRadioButtonLocator = "xpath=//input[contains(@id,'NATIONALAUTOMATION') and contains(@id,'false')]";
	private String editInspectionTypeChantLogFileOnRadioButtonLocator = "xpath=//input[contains(@id,'CHANT') and contains(@id,'true')]";
	private String editInspectionTypeChantLogFileOffRadioButtonLocator = "xpath=//input[contains(@id,'CHANT') and contains(@id,'false')]";
	private String editInspectionTypeWiropLogFileOnRadioButtonLocator = "xpath=//input[contains(@id,'WIROP') and contains(@id,'true')]";
	private String editInspectionTypeWiropLogFileOffRadioButtonLocator = "xpath=//input[contains(@id,'WIROP') and contains(@id,'false')]";
	private String editInspectionTypeOtherOnRadioButtonLocator = "xpath=//input[contains(@id,'OTHER') and contains(@id,'true')]";
	private String editInspectionTypeOtherOffRadioButtonLocator = "xpath=//input[contains(@id,'OTHER') and contains(@id,'false')]";
	private String editInspectionTypeAddAttributeButtonLocator = "xpath=//button[contains(text(),'Add Attribute')]";
	private String editInspectionTypeSaveButtonLocator = "css=#inspectionTypeUpdate_save";
	private String editInspectionTypeDeleteButtonLocator = "css=#inspectionTypeUpdate_delete";
	private String editInspectionTypeCancelLinkLocator = "xpath=//A[contains(text(),'Cancel')]";
	private String editInspectionTypeSupportedProofTestTypeTableXpath = "//FORM[@id='inspectionTypeUpdate']/table";
	private String editInspectionTypeInspectionAttributesListXpath = "//h2[contains(text(),'Inspection Attributes')]/../div[@id='infoFields']";
	private String editInspectionTypeInspectionAttributesXpath = editInspectionTypeInspectionAttributesListXpath + "/p[contains(@id,'attribute_')]";
	
	public ManageInspectionTypes(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageInspectionTypesPage() {
		misc.checkForErrorMessages("verifyManageInspectionTypesPage");
		if(!selenium.isElementPresent(manageInspectionTypesPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Inspection Types'.");
		}
	}

	public List<String> getAllInspectionTypeNames() {
		return getFilteredInspectionTypeNames(null);
	}
	
	private List<String> getFilteredInspectionTypeNames(String filter) {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(inspectionTypeTableRowCountXpath);
		int num = n.intValue();
		int row = 3;
		int column = 1;
		String locator = "xpath=" + inspectionTypeTableXpath + "/tbody/tr[" + row + "]/td[" + column + "]/a[1]";
		for(int i = 0; i < num; i++) {
			String s = selenium.getText(locator);
			if(filter == null) {
				result.add(s);
			} else {
				String typeLocator = locator + "/../../td[2]";
				String type = selenium.getText(typeLocator);
				if(type.contains(filter)) {
					result.add(s);
				}
			}
			locator = locator.replaceFirst("tr\\[" + (i+row), "tr[" + (row+i+1));
		}
		return result;
	}

	public List<String> getMasterInspectionTypeNames() {
		return getFilteredInspectionTypeNames("Master");
	}

	public List<String> getStandardInspectionTypeNames() {
		return getFilteredInspectionTypeNames("Standard");
	}

	public void gotoEditInspectionType(String inspectionType) {
		String locator = "xpath="  +inspectionTypeTableXpath + "/tbody/tr/td/a[contains(text(),'" + inspectionType + "')]/../../td[4]/A[contains(text(),'Edit')]";
		if(selenium.isElementPresent(locator)) {
			selenium.click(locator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find an edit link for inspection type '" + inspectionType + "'");
		}
	}
	
	private void assertEditInspectionTypePage() {
		assertTrue(selenium.isElementPresent(editInspectionTypeNameTextFieldLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeGroupSelectListLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypePrintableCheckboxLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeMasterInspectionCheckboxLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeRobertLogFileOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeRobertLogFileOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeNAExcelFileOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeNAExcelFileOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeChantLogFileOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeChantLogFileOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeWiropLogFileOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeWiropLogFileOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeOtherOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeOtherOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeAddAttributeButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeSaveButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeDeleteButtonLocator));
		assertTrue(selenium.isElementPresent(editInspectionTypeCancelLinkLocator));
	}

	public EventType getInspectionType() {
		assertEditInspectionTypePage();
		EventType result = new EventType(null);
		result.setName(selenium.getValue(editInspectionTypeNameTextFieldLocator));
		result.setGroup(selenium.getSelectedLabel(editInspectionTypeGroupSelectListLocator));
		result.setPrintable(selenium.isChecked(editInspectionTypePrintableCheckboxLocator));
		result.setMasterInspection(selenium.isChecked(editInspectionTypeMasterInspectionCheckboxLocator));
		result.setSupportedProofTestTypes(getSupportedProofTestTypes());
		result.setInspectionAttributes(getInspectionAttributes());
		return result;
	}

	public List<String> getInspectionAttributes() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(editInspectionTypeInspectionAttributesXpath);
		int num = n.intValue();
		int row = 1;
		String inspectionAttributesLocator = "xpath=" + editInspectionTypeInspectionAttributesListXpath + "/p[" + row + "]/input";
		for(int i = 0; i < num; i++) {
			result.add(selenium.getValue(inspectionAttributesLocator));
			inspectionAttributesLocator = inspectionAttributesLocator.replaceFirst("p\\[" + (row+i), "p[" + (row+i+1));
		}
		return result;
	}

	private List<String> getSupportedProofTestTypes() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(editInspectionTypeSupportedProofTestTypeTableXpath);
		int num = n.intValue();
		int row = 2;
		String supportedProofTestTypeLocator = "xpath=" + editInspectionTypeSupportedProofTestTypeTableXpath + "/tbody/tr[" + row + "]/td/input[@value='true']";
		for(int i = 0; i < num; i++) {
			if(selenium.isChecked(supportedProofTestTypeLocator)) {
				String proofTestTypeNameLocator = supportedProofTestTypeLocator + "/../../TD[1]";
				result.add(selenium.getText(proofTestTypeNameLocator));
			}
			supportedProofTestTypeLocator = supportedProofTestTypeLocator.replaceFirst("tr\\[" + (row+i), "tr[" + (row+i+1));
		}
		return result;
	}

	public void clickAddAttributeButton() throws InterruptedException {
		if(selenium.isElementPresent(editInspectionTypeAddAttributeButtonLocator)) {
			selenium.click(editInspectionTypeAddAttributeButtonLocator);
			selenium.waitForAjax(MiscDriver.DEFAULT_TIMEOUT);
		} else {
			fail("Could not find the Add Attribute button");
		}
	}

	public void setLastAttribute(String string) {
		String lastInspectionAttributesLocator = "xpath=" + editInspectionTypeInspectionAttributesListXpath + "/p[last()]/input";
		selenium.type(lastInspectionAttributesLocator, string);
	}

	public void clickSaveButton() {
		if(selenium.isElementPresent(editInspectionTypeSaveButtonLocator)) {
			selenium.click(editInspectionTypeSaveButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Save button");
		}
	}
}
