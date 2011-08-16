package com.n4systems.fieldid.selenium.testcase.massupdate;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.pages.event.EventMassUpdatePage;
import com.n4systems.fieldid.selenium.pages.reporting.ReportingSearchResultsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventType;

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
			
		EventGroup group = scenario.anEventGroup()
			.forTenant(scenario.defaultTenant())
	        .build();
		
		for (int i=0; i< 5; i++){
			
			Event event1 = scenario.anEvent().on(asset)
		        .ofType(eventType)
		        .withPerformedBy(scenario.defaultUser())
		        .withOwner(scenario.defaultPrimaryOrg())
		        .withTenant(scenario.defaultTenant())
		        .withGroup(group)
		        .build();
			
			scenario.anEventSchedule().completedDoing(event1).asset(asset).build();
			
			Event event2 = scenario.anEvent().on(asset2)
		        .ofType(eventType)
		        .withPerformedBy(scenario.defaultUser())
		        .withOwner(scenario.defaultPrimaryOrg())
		        .withTenant(scenario.defaultTenant())
		        .withGroup(group)
		        .build();
			
			scenario.anEventSchedule().completedDoing(event2).asset(asset2).build();
		}
     }
	
	@Test
	public void test_remove_all_events_for_multiple_assets(){
		   page.enterIdentifier(IDENTIFIER);
		   ReportingSearchResultsPage resultsPage = page.clickRunSearchButton();
		   resultsPage.selectAllItemsOnPage();
		   EventMassUpdatePage massUpdatePage = resultsPage.clickEventMassUpdate();
		   massUpdatePage.checkMassDelete();
		   massUpdatePage.clickSaveButtonAndConfirmMassDelete();
		   assertTrue("Not all Events were properly removed", verifyAllEventsAreRemoved());
	}
	
	@Test
	public void test_remove_all_schedules_for_multiple_assets(){
		   page.enterIdentifier(IDENTIFIER);
		   ReportingSearchResultsPage resultsPage = page.clickRunSearchButton();
		   resultsPage.selectAllItemsOnPage();
		   EventMassUpdatePage massUpdatePage = resultsPage.clickEventMassUpdate();
		   massUpdatePage.checkMassDelete();
		   massUpdatePage.clickSaveButtonAndConfirmMassDelete();
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
