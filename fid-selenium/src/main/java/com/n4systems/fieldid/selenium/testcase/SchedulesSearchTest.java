package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.SchedulesSearchPage;


public class SchedulesSearchTest extends PageNavigatingTestCase<SchedulesSearchPage> {

	@Override
	protected SchedulesSearchPage navigateToPage() {
		return start().login().clickSchedulesLink();
	}

	@Test
	public void search_with_any_criteria() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
	}
	
	private List<String> getDefaultColumnHeaders() {
		return Arrays.asList("Serial Number", "Reference Number", "Product Type", 
				"Inspection Type", "Last Inspection Date", "Job Site Name", 
				"Scheduled Date", "Status", "Days Past Due", "Links");
	}
}
