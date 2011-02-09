package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.SetupPage;
import com.n4systems.fieldid.selenium.pages.setup.AutoAttributeWizardPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Test;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AutoAttributeTest extends FieldIDTestCase {

    private static final String TEST_ASSET_TYPE = "Anchorage Connector";

    @Override
    public void setupScenario(Scenario scenario) {
        InfoOptionBean otherBelt = scenario.anInfoOption().withName("Other Belt").build();
        InfoOptionBean boomBelt = scenario.anInfoOption().withName("Boom Belt").build();

        InfoOptionBean opt1 = scenario.anInfoOption().withName("Opt 1").build();
        InfoOptionBean opt2 = scenario.anInfoOption().withName("Opt 2").build();
        InfoOptionBean opt3 = scenario.anInfoOption().withName("Opt 3").build();

        InfoFieldBean descField = scenario.anInfoField().type(InfoFieldBean.TEXTFIELD_FIELD_TYPE).withName("Description").build();
        InfoFieldBean typeField = scenario.anInfoField().type(InfoFieldBean.SELECTBOX_FIELD_TYPE).withName("Type")
                .withOptions(otherBelt, boomBelt).build();
        InfoFieldBean optField = scenario.anInfoField().type(InfoFieldBean.SELECTBOX_FIELD_TYPE).withName("Hardware Covers")
                .withOptions(opt1, opt2, opt3).build();

        scenario.anAssetType().named(TEST_ASSET_TYPE)
                .withFields(descField, typeField, optField)
                .build();
    }

    @Test
	public void testAutoAttributesTextOutput() throws Exception {
		SetupPage setupPage = startAsCompany("test1").login().clickSetupLink();
		AutoAttributeWizardPage attrWizard = setupPage.clickAutoAttributeWizard();
		
		attrWizard.clickAssetType(TEST_ASSET_TYPE);

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
		idPage.selectAssetType(TEST_ASSET_TYPE);
		idPage.selectAttributeValue("Type", "Boom Belt");
		assertEquals("KABOOM BABY", idPage.getAttributeValue("Description"));
	}
	
	@Test
	public void testAutoAttributesSelectOutput() throws Exception {
		SetupPage setupPage = startAsCompany("test1").login().clickSetupLink();
		AutoAttributeWizardPage attrWizard = setupPage.clickAutoAttributeWizard();
		
		attrWizard.clickAssetType(TEST_ASSET_TYPE);

		attrWizard.dragAvailableFieldToInputFields("Type");
		attrWizard.dragAvailableFieldToOutputFields("Hardware Covers");
		
		attrWizard.clickSave();
		attrWizard.clickAddDefinition();
		
		attrWizard.selectChoiceInputField("Type", "Boom Belt");
		attrWizard.selectChoiceOutputField("Hardware Covers", "Opt 1");
		attrWizard.clickSave();
		
		IdentifyPage idPage = attrWizard.clickIdentifyLink();
		idPage.selectAssetType(TEST_ASSET_TYPE);
		idPage.selectAttributeValue("Type", "Boom Belt");
		assertEquals("Opt 1", idPage.getAttributeSelectValue("Hardware Covers"));
	}
	
}
