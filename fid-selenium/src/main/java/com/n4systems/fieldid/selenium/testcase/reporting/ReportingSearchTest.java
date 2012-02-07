package com.n4systems.fieldid.selenium.testcase.reporting;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.ReportSearchCriteria;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.EventPage;
import com.n4systems.fieldid.selenium.pages.MyAccountPage;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.pages.reporting.SaveReportPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ReportingSearchTest extends PageNavigatingTestCase<ReportingPage> {
	
	@Override
	public void setupScenario(Scenario scenario) {
		scenario.aSimpleEvent().build();
	}

	@Override
	protected ReportingPage navigateToPage() {
		return startAsCompany("test1").login().clickReportingLink();
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
		criteria.setIdentifier("9*");
		criteria.setResult("Pass");
		page.setSearchCriteria(criteria);
		
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
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
	public void search_results_view_event_link() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String identifier = page.getResultIdentifiers().get(0);

		page.clickViewEvent(identifier);
	}
	
	@Test
	public void search_results_edit_event_link() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		String identifier = page.getResultIdentifiers().get(0);

		EventPage eventPage = page.clickEditEvent(identifier);
		assertEquals("Edit", eventPage.getCurrentTab());
	}
	
	//Lightbox issues.
	public void search_results_print_report() {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		
		page.clickPrintReport();
		assertTrue(selenium.isElementPresent("//p[contains(.,'Your download is being generated.')]"));
	}
	
    @Test
	public void search_results_start_event() {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		String identifierToVerify = page.getResultIdentifiers().get(0);

		
		page.clickStartEventLink();
		
		assertEquals("Perform an Event on " + identifierToVerify, selenium.getText("//div[@id='contentTitle']/h1"));
	}
	
	
	@Test
	public void saved_report_create_and_cancel() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		SaveReportPage saveReportForm = page.clickSaveReport();
        final ReportingPage reportingPage = saveReportForm.clickCancel();

        assertTrue(reportingPage.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), reportingPage.getResultColumnHeaders());
	}
	
	//Failing...
	public void saved_report_create_run_and_delete() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
		
		SaveReportPage saveReportPage = page.clickSaveReport();
		String reportName = "Test Save Report";
		saveReportPage.setReportName(reportName);
        final ReportingPage reportingPage = saveReportPage.clickSave();
        assertEquals("Your Report has been saved.", reportingPage.getActionMessages().get(0));
		
		reportingPage.clickStartNewReport();
		
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
		//Empty string for the empty last table column.
        return Arrays.asList("Date Performed", "ID Number", "Customer Name", "Location", "Asset Type", "Asset Status", "Event Type", "Performed By", "Result", "");

	}

}
