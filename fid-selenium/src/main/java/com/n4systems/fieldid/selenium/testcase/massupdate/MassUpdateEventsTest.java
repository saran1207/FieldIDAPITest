package com.n4systems.fieldid.selenium.testcase.massupdate;

import org.junit.Test;
import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.pages.event.EventMassUpdatePage;
import com.n4systems.fieldid.selenium.pages.reporting.ReportingSearchResultsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventType;

public class MassUpdateEventsTest extends PageNavigatingTestCase<ReportingPage>{
	
	private static final String SERIAL_NUMBER = "9671111";
	private static String COMPANY = "test1";
	
	@Override
	public void setupScenario(Scenario scenario) {

      AssetType type = scenario.anAssetType()
	        .named("Test Asset Type")
	        .build();
	
      Asset asset = scenario.anAsset()
	        .withOwner(scenario.defaultPrimaryOrg())
	        .withSerialNumber(SERIAL_NUMBER)
	        .ofType(type)
	        .build();
      
      Asset asset2 = scenario.anAsset()
	      .withOwner(scenario.defaultPrimaryOrg())
	      .withSerialNumber(SERIAL_NUMBER)
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
		
		for (int i=0; i< 50; i++){
			scenario.anEvent().on(asset)
		        .ofType(eventType)
		        .withPerformedBy(scenario.defaultUser())
		        .withOwner(scenario.defaultPrimaryOrg())
		        .withTenant(scenario.defaultTenant())
		        .withGroup(group)
		        .build();
			
			scenario.anEvent().on(asset2)
		        .ofType(eventType)
		        .withPerformedBy(scenario.defaultUser())
		        .withOwner(scenario.defaultPrimaryOrg())
		        .withTenant(scenario.defaultTenant())
		        .withGroup(group)
		        .build();
		}
     }
	
	@Test
	public void test_remove_all_events_for_multiple_assets(){
		   page.enterSerialNumber(SERIAL_NUMBER);
		   ReportingSearchResultsPage resultsPage = page.clickRunSearchButton();
		   resultsPage.selectAllItemsOnPage();
		   EventMassUpdatePage massUpdatePage = resultsPage.clickEventMassUpdate();
		   massUpdatePage.checkMassDelete();
		   massUpdatePage.clickSaveButtonAndConfirmMassDelete();
	}
	
	  @Override
	    protected ReportingPage navigateToPage() {
		return startAsCompany(COMPANY).login().clickReportingLink();
	}
}
