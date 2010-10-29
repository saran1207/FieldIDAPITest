package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage.InspectionFrequencyOverride;

public class ManageAssetTypes_EventFrequenciesTest extends ManageAssetTypesTestCase {
	
	private static final String TEST_EVENT_TYPE = "Pull Test";
	
	@Test
	public void test_add_and_remove_inspection_schedule() throws Exception {
		addTestAssetType();
		addEventTypeToTestAsset(TEST_EVENT_TYPE);
		
		page.clickEventFrequenciesTab();
		page.scheduleInspectionFrequencyForType(TEST_EVENT_TYPE, 14);
		assertEquals(14, page.getScheduledFrequencyForInspectionType(TEST_EVENT_TYPE));
		
		page.removeEventFrequencyForType(TEST_EVENT_TYPE);
		assertFalse("Should have removed scheduled event for type: " + TEST_EVENT_TYPE,
				page.isInspectionFrequencyScheduledForType(TEST_EVENT_TYPE));
	}

	@Test
	public void test_add_schedule_frequency_override() throws Exception {
		addTestAssetType();
		addEventTypeToTestAsset(TEST_EVENT_TYPE);
		page.clickEventFrequenciesTab();
		page.scheduleInspectionFrequencyForType(TEST_EVENT_TYPE, 14);

		Owner owner = new Owner("All American Crane Maintenance", "AACM-TEST");
		page.addOverrideForOwner(TEST_EVENT_TYPE, owner, 5);
		List<InspectionFrequencyOverride> overrides = page.getInspectionFrequencyOverrides(TEST_EVENT_TYPE);
		
		assertEquals(1, overrides.size());
		assertEquals(5, overrides.get(0).frequency);
		assertEquals("AACM-TEST", overrides.get(0).customer);
	}
	
	private void addEventTypeToTestAsset(String inspectionType) {
		page.clickAssetType(TEST_ASSET_TYPE_NAME);
		page.clickEventTypesTab();
		page.selectInspectionType(inspectionType);
		page.saveInspectionTypes();
	}
	
}
