package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.n4systems.fieldid.selenium.persistence.Scenario;

public class ManageAssetTypes_SubComponentsTest extends ManageAssetTypesTestCase {
	private static final Logger logger = Logger.getLogger(ManageAssetTypes_SubComponentsTest.class);
	
    @Override
    public void setupScenario(Scenario scenario) {
        super.setupScenario(scenario);

        scenario.anAssetType()
                .named("Mobile Crane")
                .build();

        scenario.anAssetType()
                .named("Shackle")
                .build();
    }

    @Test
	public void add_subcomponents_to_asset_type() {
		page.clickAssetType(TEST_ASSET_TYPE_NAME);
		page.clickSubComponentsTab();
		
		List<String> subComponents = page.getSubComponents();
		assertEquals("New asset type should have no subcomponents", 0, subComponents.size());
		
		page.addSubComponent("Mobile Crane");
		page.addSubComponent("Shackle");
		page.saveSubComponents();
		
		subComponents = page.getSubComponents();
		assertEquals(2, subComponents.size());
		logger.info(subComponents);
		assertTrue(subComponents.contains("Mobile Crane"));
		assertTrue(subComponents.contains("Shackle"));
	}
	
	@Test
	public void remove_subcomponents_from_asset_type() throws Exception {
		page.clickAssetType(TEST_ASSET_TYPE_NAME);
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
