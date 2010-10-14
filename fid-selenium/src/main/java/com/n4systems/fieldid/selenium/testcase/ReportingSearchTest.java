package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.ReportSearchCriteria;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.ReportingPage;

public class ReportingSearchTest extends PageNavigatingTestCase<ReportingPage> {

	@Override
	protected ReportingPage navigateToPage() {
		return start().login().clickReportingLink();
	}
	
	@Test
	public void search_with_any_criteria() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
	}
	
	@Test
	public void search_with_report_specific_criteria() throws Exception {
		ReportSearchCriteria criteria = new ReportSearchCriteria();
		criteria.setSerialNumber("1*");
		criteria.setResult("Pass");
		page.setSearchCriteria(criteria);
		
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
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
	
	private List<String> getDefaultColumnHeaders() {
		return Arrays.asList("Serial Number", "Inspection Type", "Job Site Name", "Result", 
				"Product Type", "Product Status", "Links");
	}

}
