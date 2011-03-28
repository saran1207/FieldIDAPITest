package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Test;

import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage.EventFrequencyOverride;

public class ManageAssetTypes_EventFrequenciesTest extends ManageAssetTypesTestCase {
	
	private static final String TEST_EVENT_TYPE = "Pull Test";
    private static final String TEST_CUSTOMER_ORG = "TestCustomerOrg";

    @Override
    public void setupScenario(Scenario scenario) {
        super.setupScenario(scenario);

        scenario.anEventType()
                .withEventForm(scenario.anEventForm().build())
                .named(TEST_EVENT_TYPE)
                .build();

        scenario.aCustomerOrg()
                .withParent(scenario.defaultPrimaryOrg())
                .withName(TEST_CUSTOMER_ORG)
                .build();
    }

    @Test
	public void test_add_and_remove_event_schedule() throws Exception {
		addEventTypeToTestAsset(TEST_EVENT_TYPE);
		
		page.clickEventFrequenciesTab();
		page.scheduleEventFrequencyForType(TEST_EVENT_TYPE, 14);
		assertEquals(14, page.getScheduledFrequencyForEventType(TEST_EVENT_TYPE));
		
		page.removeEventFrequencyForType(TEST_EVENT_TYPE, true);
		assertFalse("Should have removed scheduled event for type: " + TEST_EVENT_TYPE,
				page.isEventFrequencyScheduledForType(TEST_EVENT_TYPE));
	}

	@Test
	public void test_add_schedule_frequency_override() throws Exception {
		addEventTypeToTestAsset(TEST_EVENT_TYPE);
		page.clickEventFrequenciesTab();
		page.scheduleEventFrequencyForType(TEST_EVENT_TYPE, 14);

		Owner owner = new Owner("test1", TEST_CUSTOMER_ORG);
		page.addOverrideForOwner(TEST_EVENT_TYPE, owner, 5);
		List<EventFrequencyOverride> overrides = page.getEventFrequencyOverrides(TEST_EVENT_TYPE);
		
		assertEquals(1, overrides.size());
		assertEquals(5, overrides.get(0).frequency);
		assertEquals(TEST_CUSTOMER_ORG, overrides.get(0).customer);
		
		page.removeEventFrequencyForType(TEST_EVENT_TYPE, true);
		assertFalse("Should have removed scheduled event for type: " + TEST_EVENT_TYPE,
				page.isEventFrequencyScheduledForType(TEST_EVENT_TYPE));
	}
	
	@Test
	public void test_auto_schedule_on_identify(){
		addEventTypeToTestAsset(TEST_EVENT_TYPE);
		
		page.clickEventFrequenciesTab();
		page.scheduleEventFrequencyForType(TEST_EVENT_TYPE, 14);
		
		IdentifyPage identPage = page.clickIdentifyLink();
		identPage.selectAssetType(TEST_ASSET_TYPE_NAME);
		
		assertTrue("Auto schedule for :"+TEST_EVENT_TYPE+" wasn't successfully added.", selenium.isElementPresent("//div[@id='schedule_0']/label[contains(.,'"+TEST_EVENT_TYPE+"')]"));
		
		page = identPage.clickSetupLink().clickAssetTypes().clickAssetType(TEST_ASSET_TYPE_NAME).clickEventFrequenciesTab();
		page.removeEventFrequencyForType(TEST_EVENT_TYPE, true);
		assertFalse("Should have removed scheduled event for type: " + TEST_EVENT_TYPE,
				page.isEventFrequencyScheduledForType(TEST_EVENT_TYPE));
	}
	
	@Test
	public void test_auto_schedule_on_multi_add(){
		addEventTypeToTestAsset(TEST_EVENT_TYPE);
		
		page.clickEventFrequenciesTab();
		page.scheduleEventFrequencyForType(TEST_EVENT_TYPE, 14);
		
		IdentifyPage massIdentPage = page.clickIdentifyLink().clickMultiAdd();
		
		massIdentPage.selectAssetType(TEST_ASSET_TYPE_NAME);
		
		assertTrue("Auto schedule for :"+TEST_EVENT_TYPE+" wasn't successfully added.", selenium.isElementPresent("//div[@id='schedule_0']/label[contains(.,'"+TEST_EVENT_TYPE+"')]"));
		
		page = massIdentPage.clickSetupLink().clickAssetTypes().clickAssetType(TEST_ASSET_TYPE_NAME).clickEventFrequenciesTab();
		page.removeEventFrequencyForType(TEST_EVENT_TYPE, true);
		assertFalse("Should have removed scheduled event for type: " + TEST_EVENT_TYPE,
				page.isEventFrequencyScheduledForType(TEST_EVENT_TYPE));

	}
	
	private void addEventTypeToTestAsset(String eventType) {
		page.clickAssetType(TEST_ASSET_TYPE_NAME);
		page.clickEventTypeAssociationsTab();
		page.selectEventType(eventType);
		page.saveEventTypes();
	}
	
}
