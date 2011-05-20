package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.ImportPage;

public class SearchBlankSlateTest extends PageNavigatingTestCase<AssetsSearchPage> {

	@Override
	protected AssetsSearchPage navigateToPage() {
		return startAsCompany("test1").login().clickAssetsLink();
	}
	
	@Test
	public void blank_slate_present() throws Exception {
		assertTrue(page.isBlankSlate());
	}
	
	@Test
	public void choose_identify_first_asset() throws Exception {
		assertTrue(page.isBlankSlate());
		IdentifyPage identifyPage = page.clickIdentifyFirstAsset();
		assertEquals("Add", identifyPage.getCurrentTab());
	}

	@Test
	public void choose_identify_up_to_25_assets() throws Exception {
		assertTrue(page.isBlankSlate());
		IdentifyPage identifyPage = page.clickIdentifyUpTo250Assets();
		assertEquals("Multi Add", identifyPage.getCurrentTab());
	}
	
	@Test
	public void choose_import_from_excel() throws Exception {
		assertTrue(page.isBlankSlate());
		ImportPage importPage = page.clickImportFromExcel();
		assertEquals("Import", importPage.getCurrentTab());
	}
}
