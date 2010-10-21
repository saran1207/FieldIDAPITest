package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.ReportSearchCriteria;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.InspectPage;
import com.n4systems.fieldid.selenium.pages.MyAccountPage;
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
	
	@Test
	public void search_results_view_inspection_link() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String serialNumber = page.getResultSerialNumbers().get(0);

		page.clickViewInspection(serialNumber);
	}
	
	@Test
	public void search_results_edit_inspection_link() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String serialNumber = page.getResultSerialNumbers().get(0);

		InspectPage inpectPage = page.clickEditInspection(serialNumber);
		assertEquals("Edit", inpectPage.getCurrentTab());
	}
	
	@Test
	public void saved_report_create_and_cancel() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		SaveReportForm saveReportForm = page.clickSaveReport();
		page = saveReportForm.clickCancel();
		
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
	}
	
	@Test
	public void saved_report_create_run_and_delete() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		SaveReportForm saveReportForm = page.clickSaveReport();
		String reportName = "Test Save Report";
		saveReportForm.setReportName(reportName);
		page = saveReportForm.clickSave();
		assertEquals("Your Report has been saved.", page.getActionMessages().get(0));
		
		page.clickStartNewReport();
		
		assertTrue(page.getSaveReportList().contains(reportName));
		
		page.clickRunSavedReport(reportName);
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());		
		
		page.clickStartNewReport();
		
		MyAccountPage myAccountPage = page.clickSaveReportsMore();
		
		myAccountPage.deleteReport(reportName);
		assertEquals("Your Report has been deleted.", myAccountPage.getActionMessages().get(0));
		
		page = myAccountPage.clickReportingLink();
		
		assertFalse(page.getSaveReportList().contains(reportName));
	}
	
	private List<String> getDefaultColumnHeaders() {
		return Arrays.asList("Serial Number", "Inspection Type", "Job Site Name", "Result", 
				"Asset Type", "Asset Status", "Links");
	}

}
