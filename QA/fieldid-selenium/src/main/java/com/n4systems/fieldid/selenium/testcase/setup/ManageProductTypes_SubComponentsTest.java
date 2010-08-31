package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ManageProductTypes_SubComponentsTest extends ManageProductTypesTestCase {
	
	@Test
	public void add_subcomponents_to_product_type() {
		addTestProductType();
		page.clickProductType(TEST_PRODUCT_TYPE_NAME);
		page.clickSubComponentsTab();
		
		List<String> subComponents = page.getSubComponents();
		assertEquals("New product type should have no subcomponents", 0, subComponents.size());
		
		page.addSubComponent("Mobile Crane");
		page.addSubComponent("Shackle");
		page.saveSubComponents();
		
		subComponents = page.getSubComponents();
		assertEquals(2, subComponents.size());
		System.out.println(subComponents);
		assertTrue(subComponents.contains("Mobile Crane"));
		assertTrue(subComponents.contains("Shackle"));
	}
	
	@Test
	public void remove_subcomponents_from_product_type() throws Exception {
		addTestProductType();
		page.clickProductType(TEST_PRODUCT_TYPE_NAME);
		page.clickSubComponentsTab();
		
		page.addSubComponent("Mobile Crane");
		page.addSubComponent("Shackle");
		page.saveSubComponents();
		
		List<String> subComponents = page.getSubComponents();
		assertEquals(2, subComponents.size());
		
		page.removeSubComponent("Mobile Crane");
		page.saveSubComponents();
		
		subComponents = page.getSubComponents();
		assertEquals("Should have removed subcomponent", 1, subComponents.size());
		assertEquals("Shackle", subComponents.get(0));
	}

}
