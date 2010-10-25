package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.SetupPage;
import com.n4systems.fieldid.selenium.pages.setup.AutoAttributeWizardPage;

public class AutoAttributeTest extends FieldIDTestCase {
	
	@Test
	public void testAutoAttributesTextOutput() throws Exception {
		SetupPage setupPage = startAsCompany("msa").login().clickSetupLink();
		AutoAttributeWizardPage attrWizard = setupPage.clickAutoAttributeWizard();
		
		String assetType = "Anchorage Connector";
		
		attrWizard.clickAssetType(assetType);
		
		//TODO: Remove when data setup is working.
		deleteCriteriaIfExisting(attrWizard, assetType);
		
		assertEquals(3, attrWizard.getAvailableFields().size());
		assertEquals(0, attrWizard.getInputFields().size());
		assertEquals(0, attrWizard.getOutputFields().size());
		
		attrWizard.dragAvailableFieldToInputFields("Type");
		
		assertEquals(2, attrWizard.getAvailableFields().size());
		assertEquals(1, attrWizard.getInputFields().size());
		
		assertTrue(attrWizard.getInputFields().containsKey("Type"));
		
		attrWizard.dragAvailableFieldToOutputFields("Description");
		assertEquals(1, attrWizard.getOutputFields().size());
		assertTrue(attrWizard.getOutputFields().containsKey("Description"));
		
		attrWizard.clickSave();
		attrWizard.clickAddDefinition();
		
		attrWizard.selectChoiceInputField("Type", "Boom Belt");
		attrWizard.enterOutputTextField("Description", "KABOOM BABY");
		attrWizard.clickSave();
		
		IdentifyPage idPage = attrWizard.clickIdentifyLink();
		idPage.selectAssetType(assetType);
		idPage.selectAttributeValue("Type", "Boom Belt");
		assertEquals("KABOOM BABY", idPage.getAttributeValue("Description"));
	}
	
	@Test
	public void testAutoAttributesSelectOutput() throws Exception {
		SetupPage setupPage = startAsCompany("msa").login().clickSetupLink();
		AutoAttributeWizardPage attrWizard = setupPage.clickAutoAttributeWizard();
		
		String assetType = "ArcSafe Harness";
		
		attrWizard.clickAssetType(assetType);
		
		//TODO: Remove when data setup is working.
		deleteCriteriaIfExisting(attrWizard, assetType);
		
		attrWizard.dragAvailableFieldToInputFields("Belt Loops");
		attrWizard.dragAvailableFieldToOutputFields("Hardware Covers");
		
		attrWizard.clickSave();
		attrWizard.clickAddDefinition();
		
		attrWizard.selectChoiceInputField("Belt Loops", "No");
		attrWizard.selectChoiceOutputField("Hardware Covers", "No");
		attrWizard.clickSave();
		
		IdentifyPage idPage = attrWizard.clickIdentifyLink();
		idPage.selectAssetType(assetType);
		idPage.selectAttributeValue("Belt Loops", "No");
		assertEquals("No", idPage.getAttributeSelectValue("Hardware Covers"));
	}
	
	private void deleteCriteriaIfExisting(AutoAttributeWizardPage attrWizard, String assetType) {
		if (attrWizard.isOnDefinitionsTab()) {
			attrWizard.clickEditTab();
			attrWizard.clickDeleteButtonAndConfirm();
			attrWizard.clickAssetType(assetType);
		}
	}

}
