package com.n4systems.fieldid.selenium.testcase.schedules;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.SchedulesSearchPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SchedulesSearchTest extends PageNavigatingTestCase<SchedulesSearchPage> {

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
        
     }
	
	@Override
	protected SchedulesSearchPage navigateToPage() {
		return startAsCompany("test1").login().clickSchedulesLink();
	}

	@Test
	public void search_with_any_criteria() throws Exception {
		page.clickRunSearchButton();
		assertTrue(page.hasSearchResults());
		assertEquals(getDefaultColumnHeaders(), page.getResultColumnHeaders());
	}
	
	private List<String> getDefaultColumnHeaders() {
		return Arrays.asList("Scheduled Date", "Status", "Days Past Due", "ID Number", "Asset Type", "Customer Name", "Location", 
				"Event Type", "Last Event Date", "");
	}
}
