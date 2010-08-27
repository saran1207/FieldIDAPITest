package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ManageProductTypes_InspectionFrequenciesTest extends ManageProductTypesTestCase {
	
	@Test
	public void test_add_and_remove_inspection_schedule() throws Exception {
		addTestProductType();
		page.clickProductType(TEST_PRODUCT_TYPE_NAME);
		page.clickInspectionTypesTab();
		String testInspectionType = "Pull Test";
		page.selectInspectionType(testInspectionType);
		page.saveInspectionTypes();
		
		page.clickInspectionFrequenciesTab();
		page.scheduleInspectionFrequencyForType(testInspectionType, 14);
		assertEquals(14, page.getScheduledFrequencyForInspectionType(testInspectionType));
		
		page.removeInspectionFrequencyForType(testInspectionType);
		assertFalse("Should have removed scheduled inspection for type: " + testInspectionType,
				page.isInspectionFrequencyScheduledForType(testInspectionType));
	}
	
}
