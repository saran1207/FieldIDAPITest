package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchCriteria;
import com.n4systems.fieldid.selenium.datatypes.AssetSearchDisplayColumns;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;

public class AssetSearchTest extends PageNavigatingTestCase<AssetsSearchPage> {

	@Override
	protected AssetsSearchPage navigateToPage() {
		return start().login().clickAssetsLink();
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
		page.setDisplayColumns(new AssetSearchDisplayColumns());
		page.clickRunSearchButton();
		assertEquals(1, page.getFormErrorMessages().size());
	}
	
	@Test
	public void search_with_custom_display_columns_selected() throws Exception {
		AssetSearchDisplayColumns displayColumns = new AssetSearchDisplayColumns();
		displayColumns.setProductType(true);
		displayColumns.setAssignedTo(true);
		displayColumns.setSafetyNetwork(true);
		
		page.setDisplayColumns(displayColumns);
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());		
		List<String> expectedColumns = Arrays.asList("Product Type", "Assigned To", "Safety Network", "Links");
		assertEquals(expectedColumns, page.getResultColumnHeaders());
	}
	
	@Test
	public void search_with_all_display_columns_selected() throws Exception {
		AssetSearchDisplayColumns displayColumns = new AssetSearchDisplayColumns();
		displayColumns.selectAllColumns();
		
		page.setDisplayColumns(displayColumns);
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());		
		List<String> expectedColumns = Arrays.asList("Order Description", "Serial Number", "Reference Number", 
				"RFID Number", "Job Site Name", "Division", "Location", "Organization", "Product Type Group", 
				"Product Type", "Product Status", "Date Identified", "Last Inspection Date", "Network Last Inspection Date", 
				"Assigned To", "Identified By", "Modified By", "Comments", "Description", "Safety Network", "Order Number", 
				"Purchase Order", "Links");
		assertEquals(expectedColumns, page.getResultColumnHeaders());
	}
	
	private List<String> getDefaultColumnHeaders() {
		return Arrays.asList("Serial Number", "Reference Number", "Job Site Name", "Location", 
				"Product Type", "Product Status", "Date Identified", "Last Inspection Date", "Links");
	}
}
