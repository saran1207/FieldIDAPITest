package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetStatusPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.api.Archivable.EntityState;

public class ManageAssetStatusTest extends FieldIDTestCase {

	private static final String STATUS1 = "Status1";
	private static final String STATUS2 = "Status2";
	private static final String TEST_NAME = "Name";
	private ManageAssetStatusPage page;

	@Before
	public void setUp() {
		page = startAsCompany("test1").systemLogin().clickSetupLink().clickManageAssetStatuses();
	}
	
	@Override
	public void setupScenario(Scenario scenario) {
		scenario.anAssetStatus()
		        .forTenant(scenario.defaultTenant())
		        .named(STATUS1)
		        .build();
		
		scenario.anAssetStatus()
        .forTenant(scenario.defaultTenant())
        .named(STATUS2)
        .withState(EntityState.ARCHIVED)
        .build();
	}

	@Test
	public void test_add_asset_status() throws Exception {
		page.clickAddTab();
		page.enterAssetStatusName(TEST_NAME);
		page.clickSave();
		
		assertTrue(page.getFormErrorMessages().isEmpty());
		assertTrue(page.getAssetStatuses().contains(TEST_NAME));
	}
	
	@Test
	public void test_add_asset_status_with_error() throws Exception {
		page.clickAddTab();
		page.clickSave();
		
		assertFalse(page.getFormErrorMessages().isEmpty());
		assertFalse(page.getAssetStatuses().contains(TEST_NAME));
	}
	
	@Test
	public void test_archive_asset_status() throws Exception {
		page.archive(STATUS1);

		assertTrue(page.getFormErrorMessages().isEmpty());
		assertFalse(page.getAssetStatuses().contains(STATUS1));
	}

	
	@Test
	public void test_unarchive_asset_status() throws Exception {
		page.clickViewArchivedTab();
		page.unarchive(STATUS2);
		
		assertTrue(page.getFormErrorMessages().isEmpty());
		assertTrue(page.getAssetStatuses().contains(STATUS2));
	}

}
