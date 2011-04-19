package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetTypeGroup;

public class MangeAssetTypes_ListFilterTest extends ManageAssetTypesTestCase {

	@Override
	public void setupScenario(Scenario scenario) {
		
		AssetTypeGroup group = scenario.anAssetTypeGroup()
		                               .withTenant(scenario.tenant("test1"))
		                               .withName("Emergency")
		                               .build();
		
		scenario.anAssetType()
		        .named("Ladder")
		        .build();
		
		scenario.anAssetType()
                .named("Fire Extinguisher")
                .withGroup(group)
                .build();

		scenario.anAssetType()
                .named("First Aid Kit")
                .withGroup(group)
                .build();
	}
	
	@Test
	public void filter_asset_type_by_name() throws Exception {
		assertEquals(5, page.getAssetTypes().size());
		page.enterNameFilter("kit");
		page.clickFilter();
		assertEquals(1, page.getAssetTypes().size());
		assertEquals("First Aid Kit", page.getAssetTypes().get(0));
	}
	
	@Test
	public void filter_asset_type_by_group() throws Exception {
		assertEquals(5, page.getAssetTypes().size());
		page.selectFilterGroup("Emergency");
		page.clickFilter();
		assertEquals(2, page.getAssetTypes().size());
	}

	@Test
	public void filter_asset_type_by_not_in_a_group() throws Exception {
		assertEquals(5, page.getAssetTypes().size());
		page.selectFilterGroup("Not in a Group");
		page.clickFilter();
		assertEquals(3, page.getAssetTypes().size());
	}
	
	@Test
	public void filter_by_name_and_clear_filter() throws Exception {
		assertEquals(5, page.getAssetTypes().size());
		page.enterNameFilter("kit");
		page.clickFilter();
		assertEquals(1, page.getAssetTypes().size());
		assertEquals("First Aid Kit", page.getAssetTypes().get(0));
		page.clickClearFilter();
		assertEquals(5, page.getAssetTypes().size());
	}
	
	@Test
	public void filter_by_name_and_group() throws Exception {
		assertEquals(5, page.getAssetTypes().size());
		page.enterNameFilter("der");
		page.selectFilterGroup("Not in a Group");
		page.clickFilter();
		assertEquals(1, page.getAssetTypes().size());
		assertEquals("Ladder", page.getAssetTypes().get(0));
	}
	
}
