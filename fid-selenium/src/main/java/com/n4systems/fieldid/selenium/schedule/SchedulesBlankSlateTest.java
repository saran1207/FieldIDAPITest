package com.n4systems.fieldid.selenium.schedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.SchedulesSearchPage;

public class SchedulesBlankSlateTest extends PageNavigatingTestCase<SchedulesSearchPage> {

	@Override
	protected SchedulesSearchPage navigateToPage() {
		return startAsCompany("test1").login().clickSchedulesLink();
	}

	@Test
	public void blank_slate_is_present() throws Exception {
		assertTrue(page.isBlankSlate());
	}
	
	@Test
	public void choose_identify_an_asset_now() throws Exception {
		assertTrue(page.isBlankSlate());
		IdentifyPage identifyPage = page.clickIdentifyAnAssetNow();
		assertEquals("Add", identifyPage.getCurrentTab());
	}
}
