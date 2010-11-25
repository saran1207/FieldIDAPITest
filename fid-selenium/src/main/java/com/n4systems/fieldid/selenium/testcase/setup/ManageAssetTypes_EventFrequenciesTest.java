package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.EventType;
import org.junit.Test;

import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage.EventFrequencyOverride;

public class ManageAssetTypes_EventFrequenciesTest extends ManageAssetTypesTestCase {
	
	private static final String TEST_EVENT_TYPE = "Pull Test";
    private static final String TEST_CUSTOMER_ORG = "TestCustomerOrg";

    @Override
    public void setupScenario(Scenario scenario) {
        super.setupScenario(scenario);

        scenario.anEventType()
                .named(TEST_EVENT_TYPE)
                .withTenant(scenario.defaultTenant())
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
		
		page.removeEventFrequencyForType(TEST_EVENT_TYPE);
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
	}
	
	private void addEventTypeToTestAsset(String eventType) {
		page.clickAssetType(TEST_ASSET_TYPE_NAME);
		page.clickEventTypesTab();
		page.selectEventType(eventType);
		page.saveEventTypes();
	}
	
}
