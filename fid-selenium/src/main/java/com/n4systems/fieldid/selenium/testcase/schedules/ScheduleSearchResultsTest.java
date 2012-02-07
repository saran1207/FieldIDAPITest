package com.n4systems.fieldid.selenium.testcase.schedules;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.SchedulesSearchPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ScheduleSearchResultsTest extends FieldIDTestCase {

	private SchedulesSearchPage resultsPage;

	private static String COMPANY = "test1";
	private static final String TEST_EVENT_TYPE1 = "Event Type 1";
	private static final String SERIAL_NUMBER = "11111111";

	@Override
	public void setupScenario(Scenario scenario) {

		EventTypeGroup group = scenario.anEventTypeGroup()
									   .forTenant(scenario.tenant(COMPANY))
								       .withName("Test Event Type Group")
								 	   .build();

        EventType eventType1 = scenario.anEventType()
                                      .named(TEST_EVENT_TYPE1)
                                      .withGroup(group)
                                      .build();
        
        AssetType assetType = scenario.assetType(COMPANY, TEST_ASSET_TYPE_1);
                
        scenario.save(new AssociatedEventType(eventType1, assetType));    
        
        Asset asset = scenario.anAsset()
                              .withOwner(scenario.primaryOrgFor(COMPANY))
                              .withIdentifier(SERIAL_NUMBER)
                              .ofType(assetType)
                              .build();      
        
        scenario.save(new EventSchedule(asset, eventType1, new Date()));
     }

	@Before
	public void setUp() {
        resultsPage = startAsCompany(COMPANY).login().clickSchedulesLink();
        resultsPage.clickRunSearchButton();
	}

	@Test
	public void start_event_link() {
		resultsPage.clickStartEventLink();
		assertEquals(TEST_EVENT_TYPE1 + " on " + SERIAL_NUMBER, selenium.getText("//div[@id='contentTitle']/h1"));
	}

	@Test
	public void view_schedules_test() {
		resultsPage.clickViewSchedulesLink();
		assertEquals("Asset - " + SERIAL_NUMBER, selenium.getText("//div[@id='contentTitle']/h1"));
	}

	@Test
	public void edit_schedules(){
		resultsPage.clickEditSchedulesLink();
		assertEquals("Asset - " + SERIAL_NUMBER, selenium.getText("//div[@id='contentTitle']/h1"));
	}
	
	@Test
	public void view_asset_link(){
		resultsPage.clickViewAssetLink();
		assertEquals("Asset - " + SERIAL_NUMBER, selenium.getText("//div[@id='contentTitle']/h1"));
	}
	
	@Test
	public void edit_asset_link(){
		resultsPage.clickEditAssetLink();
		assertEquals("Asset - " + SERIAL_NUMBER, selenium.getText("//div[@id='contentTitle']/h1"));
	}
	
}
