package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.SmartSearchResultsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;

public class SmartSearchTest extends FieldIDTestCase {

	private SmartSearchResultsPage resultsPage;
	private String SERIAL_NUMBER = "Chocolate";
	private String RFID = "rfid-49";
	private int numberOfPages = 5;

	@Override
	public void setupScenario(Scenario scenario) {

		AssetType type = scenario.anAssetType().named("Cup Cake").build();
		for (int i = 0; i < 50; i++) {
			scenario.anAsset().ofType(type).rfidNumber("rfid-" + i).withIdentifier(SERIAL_NUMBER).build();
		}
	}

	@Test
	public void search_by_rfid_should_return_a_single_result() {
		AssetPage assetPage = startAsCompany("test1").systemLogin().search(RFID);
		assertTrue("Rfid number not found", assetPage.checkRfidNumber(RFID));
	}

	@Test
	public void search_by_identifier_should_return_five_pages_of_results() {
		resultsPage = startAsCompany("test1").systemLogin().searchWithMultipleResults(SERIAL_NUMBER);
		assertTrue("Did not return 5 pages of results", resultsPage.checkNumberOfPages(numberOfPages));
	}

	@Test
	public void test_pagination_works() {
		resultsPage = startAsCompany("test1").systemLogin().searchWithMultipleResults(SERIAL_NUMBER);
		SmartSearchResultsPage nextPage = resultsPage.clickNextLink();
		assertTrue("Next page was not returned", nextPage.checkOnSmartSearchResultsPage());
	}
}
