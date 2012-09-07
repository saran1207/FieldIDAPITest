package com.n4systems.fieldid.selenium.testcase.massupdate;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.pages.event.EventMassUpdatePage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.*;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MassUpdateEventsTest extends PageNavigatingTestCase<ReportingPage>{
	
	private static final String IDENTIFIER = "9671111";
	private static String COMPANY = "test1";
	
	@Override
	public void setupScenario(Scenario scenario) {

      AssetType type = scenario.anAssetType()
	        .named("Test Asset Type")
	        .build();
	
      Asset asset = scenario.anAsset()
	        .withOwner(scenario.defaultPrimaryOrg())
	        .withIdentifier(IDENTIFIER)
	        .ofType(type)
	        .build();
      
      Asset asset2 = scenario.anAsset()
	      .withOwner(scenario.defaultPrimaryOrg())
	      .withIdentifier(IDENTIFIER)
	      .ofType(type)
	      .build();
	
		EventForm eventForm = scenario.anEventForm().build();
		
		EventType eventType = scenario.anEventType()
	        .withEventForm(eventForm)
	        .withMaster(true)
	        .named("Test Event Type")
	        .build();
			
		for (int i=0; i< 5; i++){
			
			Event event1 = scenario.anEvent().on(asset)
		        .ofType(eventType)
		        .withPerformedBy(scenario.defaultUser())
		        .withOwner(scenario.defaultPrimaryOrg())
		        .withTenant(scenario.defaultTenant())
		        .build();

			Event event2 = scenario.anEvent().on(asset2)
		        .ofType(eventType)
		        .withPerformedBy(scenario.defaultUser())
		        .withOwner(scenario.defaultPrimaryOrg())
		        .withTenant(scenario.defaultTenant())
		        .build();
		}
     }
	
	@Test
	public void test_remove_all_events_for_multiple_assets(){
        page.enterIdentifier(IDENTIFIER);
        page.clickRunSearchButton();
        page.selectAllItemsOnPage();
        EventMassUpdatePage massUpdatePage = page.clickEventMassUpdate();
        massUpdatePage.selectDelete();
        massUpdatePage.saveDeleteDetails();
        massUpdatePage.clickConfirmDelete();
        assertTrue("Not all Events were properly removed", verifyAllEventsAreRemoved());
	}
	
	@Test
	public void test_remove_all_schedules_for_multiple_assets(){
        page.enterIdentifier(IDENTIFIER);
        page.clickRunSearchButton();
        page.selectAllItemsOnPage();
        EventMassUpdatePage massUpdatePage = page.clickEventMassUpdate();
        massUpdatePage.selectDelete();
        massUpdatePage.saveDeleteDetails();
        massUpdatePage.clickConfirmDelete();
        assertTrue("Not all Schedules were properly removed", verifyAllSchedulesAreRemoved());
	}
	
	private boolean verifyAllSchedulesAreRemoved() {
		page.clickSchedulesLink();
		return selenium.isElementPresent("//div[@class='initialMessage']");
	}

	private boolean verifyAllEventsAreRemoved(){
		return selenium.isElementPresent("//div[@class='message']");
	}
	
    @Override
    protected ReportingPage navigateToPage() {
		return startAsCompany(COMPANY).login().clickReportingLink();
	}
}
