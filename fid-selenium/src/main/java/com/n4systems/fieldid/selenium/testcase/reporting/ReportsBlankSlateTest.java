package com.n4systems.fieldid.selenium.testcase.reporting;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.EventPage;
import com.n4systems.fieldid.selenium.pages.ImportPage;
import com.n4systems.fieldid.selenium.pages.ManageEventsPage;
import com.n4systems.fieldid.selenium.pages.ReportingPage;

public class ReportsBlankSlateTest extends PageNavigatingTestCase<ReportingPage> {

	@Override
	protected ReportingPage navigateToPage() {
		return startAsCompany("test1").login().clickReportingLink();
	}
	
	@Test
	public void blank_slate_is_present() throws Exception {
		assertTrue(page.isBlankSlate());
	}

	@Test
	public void choose_perform_first_event() throws Exception {
		assertTrue(page.isBlankSlate());
		EventPage eventPage = page.clickPerformFirstEventPage();
		assertTrue(eventPage.checkOnStartEventPage());
	}
	
	@Test
	public void choose_perform_multi_events() throws Exception {
		assertTrue(page.isBlankSlate());
		ManageEventsPage eventPage = page.clickPerformMultiEventPage();
		assertTrue(eventPage.checkOnMassEventPage());
	}

	@Test
	public void choose_import_from_excel() throws Exception {
		assertTrue(page.isBlankSlate());
		ImportPage importPage = page.clickImportFromExcel();
		assertEquals("Import", importPage.getCurrentTab());
	}

}
