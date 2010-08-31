package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.setup.ManageProductTypesPage.InspectionFrequencyOverride;

public class ManageProductTypes_InspectionFrequenciesTest extends ManageProductTypesTestCase {
	
	private static final String TEST_INSPECTION_TYPE = "Pull Test";
	
	@Test
	public void test_add_and_remove_inspection_schedule() throws Exception {
		addTestProductType();
		addInspectionTypeToTestProduct(TEST_INSPECTION_TYPE);
		
		page.clickInspectionFrequenciesTab();
		page.scheduleInspectionFrequencyForType(TEST_INSPECTION_TYPE, 14);
		assertEquals(14, page.getScheduledFrequencyForInspectionType(TEST_INSPECTION_TYPE));
		
		page.removeInspectionFrequencyForType(TEST_INSPECTION_TYPE);
		assertFalse("Should have removed scheduled inspection for type: " + TEST_INSPECTION_TYPE,
				page.isInspectionFrequencyScheduledForType(TEST_INSPECTION_TYPE));
	}

	@Test
	public void test_add_schedule_frequency_override() throws Exception {
		addTestProductType();
		addInspectionTypeToTestProduct(TEST_INSPECTION_TYPE);
		page.clickInspectionFrequenciesTab();
		page.scheduleInspectionFrequencyForType(TEST_INSPECTION_TYPE, 14);

		Owner owner = new Owner("All American Crane Maintenance", "AACM-TEST");
		page.addOverrideForOwner(TEST_INSPECTION_TYPE, owner, 5);
		List<InspectionFrequencyOverride> overrides = page.getInspectionFrequencyOverrides(TEST_INSPECTION_TYPE);
		
		assertEquals(1, overrides.size());
		assertEquals(5, overrides.get(0).frequency);
		assertEquals("AACM-TEST", overrides.get(0).customer);
	}
	
	private void addInspectionTypeToTestProduct(String inspectionType) {
		page.clickProductType(TEST_PRODUCT_TYPE_NAME);
		page.clickInspectionTypesTab();
		page.selectInspectionType(inspectionType);
		page.saveInspectionTypes();
	}
	
}
