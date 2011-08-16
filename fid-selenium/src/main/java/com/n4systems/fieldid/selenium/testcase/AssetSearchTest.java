package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.SearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.ExtendedFeature;

public class AssetSearchTest extends PageNavigatingTestCase<AssetsSearchPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.primaryOrgFor("test1").setExtendedFeatures(setOf(ExtendedFeature.AssignedTo, ExtendedFeature.Integration));

        AssetType type = scenario.anAssetType()
                .named("Chain Sling")
                .build();

        scenario.anAsset()
                .ofType(type)
                .rfidNumber("123456")
                .withIdentifier("10105")
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
		criteria.setIdentifier("10*");
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
		List<String> expectedColumns = Arrays.asList("Asset Type", "Assigned To", "Safety Network", "");
		assertEquals(expectedColumns, page.getResultColumnHeaders());
	}
	
	@Test
	public void search_with_all_display_columns_selected() throws Exception {
		SearchDisplayColumns displayColumns = new SearchDisplayColumns();
		displayColumns.selectAllColumns();
		
		page.setDisplayColumns(displayColumns);
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());		
		List<String> expectedColumns = Arrays.asList("ID Number",  "Asset Type",  "Asset Status",  "Customer Name",  "Location",
				"Date Identified",  "Last Event Date",  "RFID Number",  "Reference Number",  "Assigned To",  "Division",  "Organization",  
				"Order Description",  "Order Number",  "Purchase Order",  "Asset Type Group",  "Network Last Event Date",  "Identified By",  
				"Modified By",  "Comments",  "Description",  "Safety Network", "");
		assertEquals(expectedColumns, page.getResultColumnHeaders());
	}
	
	@Test
	public void search_results_identifier_link() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String identifier = page.getResultIdentifiers().get(0);
		
		AssetPage assetPage = page.clickResultIdentifier(identifier);
		assertTrue(assetPage.checkHeader(identifier));
	}
	
	@Test
	public void search_results_info_link() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String identifier = page.getResultIdentifiers().get(0);
		
		AssetPage assetPage = page.clickResultInfo(identifier);
		assertTrue(assetPage.checkHeader(identifier));
	}
	
	@Test
	public void search_results_start_event_link_quick_event() throws Exception {
        AssetSearchCriteria criteria = new AssetSearchCriteria();
        criteria.setAssetType("Chain Sling");
        page.setSearchCriteria(criteria);

		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String identifier = page.getResultIdentifiers().get(0);

        page.clickResultStartEvent(identifier);
    }
	
	private List<String> getDefaultColumnHeaders() {
		return Arrays.asList("ID Number", "Asset Type", "Asset Status", "Customer Name", "Location",  "Date Identified", "Last Event Date", "Next Scheduled Date", "");
	}

}
