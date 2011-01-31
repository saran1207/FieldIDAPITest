package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import com.n4systems.fieldid.selenium.pages.QuickEventPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.ExtendedFeature;
import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;

public class AssetSearchTest extends PageNavigatingTestCase<AssetsSearchPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.primaryOrgFor("test1").setExtendedFeatures(setOf(ExtendedFeature.AssignedTo));

        AssetType type = scenario.anAssetType()
                .named("Chain Sling")
                .build();

        scenario.anAsset()
                .ofType(type)
                .rfidNumber("123456")
                .withSerialNumber("10105")
                .build();
    }

    @Override
	protected AssetsSearchPage navigateToPage() {
		return startAsCompany("test1").login().clickAssetsLink();
	}
	
	@Test
	public void search_with_any_criteria() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
	}
	
	@Test
	public void wildcard_search_by_rfid_with_results() throws Exception {
		AssetSearchCriteria criteria = new AssetSearchCriteria();
		criteria.setRFIDNumber("1*");
		page.setSearchCriteria(criteria);
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
	}

	@Test
	public void search_and_modify_search_criteria_with_results() throws Exception {
		AssetSearchCriteria criteria = new AssetSearchCriteria();
		criteria.setRFIDNumber("1*");
		page.setSearchCriteria(criteria);
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		
		page.expandSearchCriteria();
		criteria.setSerialNumber("10*");
		page.setSearchCriteria(criteria);
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());		
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
	}
	
	@Test
	public void search_by_rfid_with_no_results() throws Exception {
		AssetSearchCriteria criteria = new AssetSearchCriteria();
		criteria.setRFIDNumber("123ABCXXXYYYZZZ");
		page.setSearchCriteria(criteria);
		page.clickRunSearchButton();
		assertFalse(page.hasSearchResults());
	}	
	
	@Test
	public void search_with_no_display_columns_selected() throws Exception {
		page.setDisplayColumns(new SearchDisplayColumns());
		page.clickRunSearchButton();
		assertEquals(1, page.getFormErrorMessages().size());
	}
	
	@Test
	public void search_with_custom_display_columns_selected() throws Exception {
		SearchDisplayColumns displayColumns = new SearchDisplayColumns();
		displayColumns.setAssetType(true);
		displayColumns.setAssignedTo(true);
		displayColumns.setSafetyNetwork(true);
		
		page.setDisplayColumns(displayColumns);
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());		
		List<String> expectedColumns = Arrays.asList("Assigned To", "Asset Type", "Safety Network", "");
		assertEquals(expectedColumns, page.getResultColumnHeaders());
	}
	
	@Test
	public void search_with_all_display_columns_selected() throws Exception {
		SearchDisplayColumns displayColumns = new SearchDisplayColumns();
		displayColumns.selectAllColumns();
		
		page.setDisplayColumns(displayColumns);
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());		
		List<String> expectedColumns = Arrays.asList("Serial Number", "RFID Number", "Reference Number", 
				"Assigned To", "Customer Name", "Division", "Location", "Organization", "Order Description", "Order Number", "Purchase Order", "Asset Type Group",
				"Asset Type", "Asset Status", "Last Event Date", "Network Last Event Date", "Identified By", "Modified By", "Comments",
				"Description", "Safety Network","Date Identified", "");
		assertEquals(expectedColumns, page.getResultColumnHeaders());
	}
	
	@Test
	public void search_results_serial_number_link() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String serialNumber = page.getResultSerialNumbers().get(0);
		
		AssetPage assetPage = page.clickResultSerialNumber(serialNumber);
		assertTrue(assetPage.checkHeader(serialNumber));
	}
	
	@Test
	public void search_results_info_link() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String serialNumber = page.getResultSerialNumbers().get(0);
		
		AssetPage assetPage = page.clickResultInfo(serialNumber);
		assertTrue(assetPage.checkHeader(serialNumber));
	}
	
	@Test
	public void search_results_start_event_link_quick_event() throws Exception {
        AssetSearchCriteria criteria = new AssetSearchCriteria();
        criteria.setAssetType("Chain Sling");
        page.setSearchCriteria(criteria);

		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String serialNumber = page.getResultSerialNumbers().get(0);

        QuickEventPage quickEventPage = page.clickResultStartEvent(serialNumber);
    }
	
	private List<String> getDefaultColumnHeaders() {
		return Arrays.asList("Serial Number", "Reference Number", "Customer Name", "Location", 
				"Asset Type", "Asset Status", "Last Event Date", "Date Identified", "");
	}

}
