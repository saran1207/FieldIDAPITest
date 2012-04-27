package com.n4systems.fieldid.selenium.testcase.massupdate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.pages.SchedulesSearchPage;
import com.n4systems.fieldid.selenium.pages.assets.AssetsMassUpdatePage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fieldid.selenium.persistence.builder.SimpleEventBuilder;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.builders.EventScheduleBuilder;
import com.n4systems.model.builders.SubAssetBuilder;
import com.n4systems.model.orgs.PrimaryOrg;

public class MassUpdateAssetsTest extends FieldIDTestCase {

	private HomePage page;

	@Override
	public void setupScenario(Scenario scenario) {
		PrimaryOrg primaryOrg = scenario.primaryOrgFor("test1");
		primaryOrg.setExtendedFeatures(setOf(ExtendedFeature.OrderDetails));
		scenario.save(primaryOrg);
		
		
		AssetType type = scenario.anAssetType().named("Workman Harness").build();

		AssetStatus status1 = scenario.anAssetStatus().named("In Service").build();

		scenario.anAssetStatus().named("Out of Service").build();
		
		AssetBuilder anAsset = scenario.anAsset()
			.withIdentifier("123456")
			.ofType(type)
			.havingStatus(status1);

		SimpleEventBuilder.aSimpleEvent(scenario).build();

		EventScheduleBuilder.aScheduledEventSchedule().asset(anAsset.build());
		anAsset.purchaseOrder("PO 3").build();
		Asset masterAsset = anAsset.purchaseOrder("PO 4").build();
		
		
		AssetBuilder aSubAsset = scenario.anAsset()
			.withIdentifier("123456sub")
			.ofType(type)
			.havingStatus(status1);
			
		SubAssetBuilder subBuilder = new SubAssetBuilder(aSubAsset.build(), masterAsset);
		subBuilder.build();
	}

	@Before
	public void setUp() {
		page = startAsCompany("test1").login();
	}

	@Test
	public void test_mass_update_asset() throws Exception {
		AssetsSearchPage assetsSearchPage = page.clickAssetsLink();

		assetsSearchPage.enterIdentifier("123456");
		assetsSearchPage.clickRunSearchButton();

		assertEquals(3, assetsSearchPage.getTotalResultsCount());

		assetsSearchPage.selectAllItemsOnPage();

		AssetsMassUpdatePage massUpdatePage = assetsSearchPage.clickMassUpdate();
		massUpdatePage.selectEdit();
		massUpdatePage.setAssetStatus("Out of Service");
		massUpdatePage.setPurchaseOrder("PO 5");
		
		massUpdatePage.saveEditDetails();

		assetsSearchPage = massUpdatePage.clickConfirmEdit();

		AssetPage assetPage = assetsSearchPage.clickAssetLinkForResult(1);

		assertEquals("PO 5", assetPage.getPurchaseOrder());
		assertEquals("Out of Service", assetPage.getAssetStatus());

		assetPage.goBack();

		assetsSearchPage.clickAssetLinkForResult(2);

		assertEquals("PO 5", assetPage.getPurchaseOrder());
		assertEquals("Out of Service", assetPage.getAssetStatus());
	}
	
	@Test
	public void test_mass_update_asset_required_fields() throws Exception {
		AssetsSearchPage assetsSearchPage = page.clickAssetsLink();

		assetsSearchPage.enterIdentifier("123456");
		assetsSearchPage.clickRunSearchButton();

		assertEquals(3, assetsSearchPage.getTotalResultsCount());

		assetsSearchPage.selectAllItemsOnPage();

		AssetsMassUpdatePage massUpdatePage = assetsSearchPage.clickMassUpdate();
		massUpdatePage.selectEdit();
		
		massUpdatePage.checkOwner();
		massUpdatePage.checkIdentified();
		
		massUpdatePage.saveEditDetailsWithError();
		
		assertFalse(massUpdatePage.getFormErrorMessages().isEmpty());
	}

	@Test
	public void test_mass_delete_assets_with_schedule() {
		
		doDelete("123456");

		assertTrue("Assets weren't successfully deleted", selenium.isElementPresent("//span[contains(.,'Mass Delete Successful. 3 assets removed.')]"));

		SchedulesSearchPage scheduleSearch = page.clickSchedulesLink();
		scheduleSearch.clickRunSearchButton();

		assertTrue("Schedule wasn't successfully deleted", verifyAllSchedulesAreRemoved());
	}

	@Test
	public void test_mass_delete_assets_with_events() {
		
		doDelete("9671111");
		
		assertTrue("Asset wasn't successfully deleted", selenium.isElementPresent("//span[contains(.,'Mass Delete Successful. 1 assets removed.')]"));
	
		ReportingPage reportingPage = page.clickReportingLink();
		reportingPage.clickRunSearchButton();
		assertTrue("Event wasn't successfully deleted", selenium.isElementPresent("//div[@class='no-results']"));
	}

	@Test
	public void test_mass_delete_sub_assets_attached_to_master_assets_still_persist_after_removing_master() {
		
		doDelete("123456");
		
		AssetsSearchPage assetsSearchPage = page.clickAssetsLink();

		assetsSearchPage.enterIdentifier("123456sub");
		assetsSearchPage.clickRunSearchButton();
		
		assertTrue("Sub asset was incorrectly removed!", !verifyAllSchedulesAreRemoved());
	}
	
	@Test
	public void test_mass_delete_sub_assets_attached_to_master_dont_remove_master(){
	
		doDelete("123456sub");
		
		AssetsSearchPage assetsSearchPage = page.clickAssetsLink();

		assetsSearchPage.enterIdentifier("123456");
		assetsSearchPage.clickRunSearchButton();
		
		assertTrue("Master asset was incorrectly removed!", !verifyAllSchedulesAreRemoved());
	}
	
	public void doDelete(String identifier){
		AssetsSearchPage assetsSearchPage = page.clickAssetsLink();

		assetsSearchPage.enterIdentifier(identifier);
		assetsSearchPage.clickRunSearchButton();

		assetsSearchPage.selectAllItemsOnPage();

		AssetsMassUpdatePage massUpdatePage = assetsSearchPage.clickMassUpdate();
		
		massUpdatePage.selectDelete();
		
		massUpdatePage.saveDeleteDetails();
		
		massUpdatePage.clickConfirmDelete();
	
	}
	
	private boolean verifyAllSchedulesAreRemoved() {
		return selenium.isElementPresent("//div[@class='emptyList']");
	}
	
}
