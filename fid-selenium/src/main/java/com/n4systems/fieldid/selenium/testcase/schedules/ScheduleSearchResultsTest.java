package com.n4systems.fieldid.selenium.testcase.schedules;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.schedules.SchedulesSearchResultsPage;

public class ScheduleSearchResultsTest extends FieldIDTestCase {

	private String serialNumberToVerify;
	private static String FIRST_ELEMENT_LOCATOR = "//tr[2]/td/a";
	private SchedulesSearchResultsPage schedulesSearchResultsPage;

	@Before
	public void setUp() {
		schedulesSearchResultsPage = startAsCompany("illinois").login().clickSchedulesLink().clickRunSearchButton();
	}

	@Test
	public void start_event_link() {
		serialNumberToVerify = selenium.getText(FIRST_ELEMENT_LOCATOR);
		schedulesSearchResultsPage.clickStartEventLink();
		assertEquals("Inspect - " + serialNumberToVerify, selenium.getText("//div[@id='contentTitle']/h1"));
	}

	@Test
	public void view_schedules_test() {
		serialNumberToVerify = selenium.getText(FIRST_ELEMENT_LOCATOR);
		schedulesSearchResultsPage.clickViewSchedulesLink();
		assertEquals("Asset - " + serialNumberToVerify, selenium.getText("//div[@id='contentTitle']/h1"));
	}

	@Test
	public void edit_schedules(){
		serialNumberToVerify = selenium.getText(FIRST_ELEMENT_LOCATOR);
		schedulesSearchResultsPage.clickEditSchedulesLink();
		assertEquals("Asset - " + serialNumberToVerify, selenium.getText("//div[@id='contentTitle']/h1"));
	}
	
	@Test
	public void view_asset_link(){
		serialNumberToVerify = selenium.getText(FIRST_ELEMENT_LOCATOR);
		schedulesSearchResultsPage.clickViewAssetLink();
		assertEquals("Asset - " + serialNumberToVerify, selenium.getText("//div[@id='contentTitle']/h1"));
	}
	
	@Test
	public void edit_asset_link(){
		serialNumberToVerify = selenium.getText(FIRST_ELEMENT_LOCATOR);
		schedulesSearchResultsPage.clickEditAssetLink();
		assertEquals("Asset - " + serialNumberToVerify, selenium.getText("//div[@id='contentTitle']/h1"));
	}
	
}
