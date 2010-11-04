package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.EventType;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ManageEventTypes {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String manageEventTypesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Event Types')]";
	private String eventTypeTableXpath = "//div[@id='pageContent']/table";
	private String eventTypeTableRowCountXpath = eventTypeTableXpath + "/tbody/tr/td[1]/a[1]";
	private String editEventTypeNameTextFieldLocator = "css=#eventTypeUpdate_name";
	private String editEventTypeGroupSelectListLocator = "css=#eventTypeUpdate_group";
	private String editEventTypePrintableCheckboxLocator = "css=#eventTypeUpdate_printable";
	private String editEventTypeMasterEventCheckboxLocator = "css=#eventTypeUpdate_master";
	private String editEventTypeRobertLogFileOnRadioButtonLocator = "xpath=//input[contains(@id,'ROBERTS') and contains(@id,'true')]";
	private String editEventTypeRobertLogFileOffRadioButtonLocator = "xpath=//input[contains(@id,'ROBERTS') and contains(@id,'false')]";
	private String editEventTypeNAExcelFileOnRadioButtonLocator = "xpath=//input[contains(@id,'NATIONALAUTOMATION') and contains(@id,'true')]";
	private String editEventTypeNAExcelFileOffRadioButtonLocator = "xpath=//input[contains(@id,'NATIONALAUTOMATION') and contains(@id,'false')]";
	private String editEventTypeChantLogFileOnRadioButtonLocator = "xpath=//input[contains(@id,'CHANT') and contains(@id,'true')]";
	private String editEventTypeChantLogFileOffRadioButtonLocator = "xpath=//input[contains(@id,'CHANT') and contains(@id,'false')]";
	private String editEventTypeWiropLogFileOnRadioButtonLocator = "xpath=//input[contains(@id,'WIROP') and contains(@id,'true')]";
	private String editEventTypeWiropLogFileOffRadioButtonLocator = "xpath=//input[contains(@id,'WIROP') and contains(@id,'false')]";
	private String editEventTypeOtherOnRadioButtonLocator = "xpath=//input[contains(@id,'OTHER') and contains(@id,'true')]";
	private String editEventTypeOtherOffRadioButtonLocator = "xpath=//input[contains(@id,'OTHER') and contains(@id,'false')]";
	private String editEventTypeAddAttributeButtonLocator = "xpath=//button[contains(text(),'Add Attribute')]";
	private String editEventTypeSaveButtonLocator = "css=#eventTypeUpdate_save";
	private String editEventTypeDeleteButtonLocator = "css=#eventTypeUpdate_delete";
	private String editEventTypeCancelLinkLocator = "xpath=//A[contains(text(),'Cancel')]";
	private String editEventTypeSupportedProofTestTypeTableXpath = "//FORM[@id='eventTypeUpdate']/table";
	private String editEventTypeEventAttributesListXpath = "//h2[contains(text(),'Event Attributes')]/../div[@id='infoFields']";
	private String editEventTypeEventAttributesXpath = editEventTypeEventAttributesListXpath + "/p[contains(@id,'attribute_')]";
	
	public ManageEventTypes(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageEventTypesPage() {
		misc.checkForErrorMessages("verifyManageEventTypesPage");
		if(!selenium.isElementPresent(manageEventTypesPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Event Types'.");
		}
	}

	public List<String> getAllEventTypeNames() {
		return getFilteredEventTypeNames(null);
	}
	
	private List<String> getFilteredEventTypeNames(String filter) {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(eventTypeTableRowCountXpath);
		int num = n.intValue();
		int row = 3;
		int column = 1;
		String locator = "xpath=" + eventTypeTableXpath + "/tbody/tr[" + row + "]/td[" + column + "]/a[1]";
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

	public List<String> getMasterEventTypeNames() {
		return getFilteredEventTypeNames("Master");
	}

	public List<String> getStandardEventTypeNames() {
		return getFilteredEventTypeNames("Standard");
	}

	public void gotoEditEventType(String eventType) {
		String locator = "xpath="  + eventTypeTableXpath + "/tbody/tr/td/a[contains(text(),'" + eventType + "')]/../../td[4]/A[contains(text(),'Edit')]";
		if(selenium.isElementPresent(locator)) {
			selenium.click(locator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find an edit link for event type '" + eventType + "'");
		}
	}
	
	private void assertEditEventTypePage() {
		assertTrue(selenium.isElementPresent(editEventTypeNameTextFieldLocator));
		assertTrue(selenium.isElementPresent(editEventTypeGroupSelectListLocator));
		assertTrue(selenium.isElementPresent(editEventTypePrintableCheckboxLocator));
		assertTrue(selenium.isElementPresent(editEventTypeMasterEventCheckboxLocator));
		assertTrue(selenium.isElementPresent(editEventTypeRobertLogFileOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeRobertLogFileOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeNAExcelFileOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeNAExcelFileOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeChantLogFileOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeChantLogFileOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeWiropLogFileOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeWiropLogFileOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeOtherOnRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeOtherOffRadioButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeAddAttributeButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeSaveButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeDeleteButtonLocator));
		assertTrue(selenium.isElementPresent(editEventTypeCancelLinkLocator));
	}

	public EventType getEventType() {
		assertEditEventTypePage();
		EventType result = new EventType(null);
		result.setName(selenium.getValue(editEventTypeNameTextFieldLocator));
		result.setGroup(selenium.getSelectedLabel(editEventTypeGroupSelectListLocator));
		result.setPrintable(selenium.isChecked(editEventTypePrintableCheckboxLocator));
		result.setMasterEvent(selenium.isChecked(editEventTypeMasterEventCheckboxLocator));
		result.setSupportedProofTestTypes(getSupportedProofTestTypes());
		result.setEventAttributes(getEventAttributes());
		return result;
	}

	public List<String> getEventAttributes() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(editEventTypeEventAttributesXpath);
		int num = n.intValue();
		int row = 1;
		String eventAttributesLocator = "xpath=" + editEventTypeEventAttributesListXpath + "/p[" + row + "]/input";
		for(int i = 0; i < num; i++) {
			result.add(selenium.getValue(eventAttributesLocator));
			eventAttributesLocator = eventAttributesLocator.replaceFirst("p\\[" + (row+i), "p[" + (row+i+1));
		}
		return result;
	}

	private List<String> getSupportedProofTestTypes() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(editEventTypeSupportedProofTestTypeTableXpath);
		int num = n.intValue();
		int row = 2;
		String supportedProofTestTypeLocator = "xpath=" + editEventTypeSupportedProofTestTypeTableXpath + "/tbody/tr[" + row + "]/td/input[@value='true']";
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
		if(selenium.isElementPresent(editEventTypeAddAttributeButtonLocator)) {
			selenium.click(editEventTypeAddAttributeButtonLocator);
			selenium.waitForAjax(MiscDriver.DEFAULT_TIMEOUT);
		} else {
			fail("Could not find the Add Attribute button");
		}
	}

	public void setLastAttribute(String string) {
		String lastEventAttributesLocator = "xpath=" + editEventTypeEventAttributesListXpath + "/p[last()]/input";
		selenium.type(lastEventAttributesLocator, string);
	}

	public void clickSaveButton() {
		if(selenium.isElementPresent(editEventTypeSaveButtonLocator)) {
			selenium.click(editEventTypeSaveButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Save button");
		}
	}
}
