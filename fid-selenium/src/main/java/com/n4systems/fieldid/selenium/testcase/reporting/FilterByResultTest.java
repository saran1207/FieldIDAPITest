package com.n4systems.fieldid.selenium.testcase.reporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventType;
import com.n4systems.model.Status;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;

@RunWith(Parameterized.class)
public class FilterByResultTest extends PageNavigatingTestCase<ReportingPage> {

	private String resultName;

	public FilterByResultTest(String resultName) {
		this.resultName = resultName;
	}
	
	private static final String TEST_EVENT_TYPE1 = "TestEventType1";
	private static final String TEST_TENANT = "test1";
	private static final String ASSET_IDENTIFIER1 = "12345";

	@Override
	public void setupScenario(Scenario scenario) {
		
		Tenant tenant = scenario.tenant(TEST_TENANT);
		
		PrimaryOrg primaryOrg = scenario.primaryOrgFor(TEST_TENANT);
		
		User user = scenario.defaultUser();
		
		AssetType type = scenario.anAssetType().named("TestAssetType")
				                               .build();

		AssetStatus assetStatus = scenario.anAssetStatus().named("AssetStatus1").build();
		
		Asset asset1 = scenario.anAsset()
				              .withOwner(primaryOrg)
				              .withIdentifier(ASSET_IDENTIFIER1)
				              .havingStatus(assetStatus)
				              .ofType(type)
				              .build();

		EventForm eventForm1 = scenario.anEventForm().build();

		EventType eventType1 = scenario.anEventType()
		                               .withEventForm(eventForm1)
				                       .named(TEST_EVENT_TYPE1)
				                       .build();
		
		EventForm eventForm2 = scenario.anEventForm().build();

		EventType eventType2 = scenario.anEventType()
		                               .withEventForm(eventForm2)
				                       .named(TEST_EVENT_TYPE1)
				                       .build();

		EventForm eventForm3 = scenario.anEventForm().build();

		EventType eventType3 = scenario.anEventType()
		                               .withEventForm(eventForm3)
				                       .named(TEST_EVENT_TYPE1)
				                       .build();

		EventGroup group = scenario.anEventGroup()
		                           .forTenant(tenant)
		                           .build();

		Event event1 = scenario.anEvent()
		                      .on(asset1)
		                      .ofType(eventType1)
		                      .withPerformedBy(user)
		                      .withOwner(primaryOrg)
		                      .withTenant(tenant)
		                      .withGroup(group)		                      
		                      .withResult(Status.PASS)
		                      .withAssetStatus(assetStatus)
		                      .build();

		Event event2 = scenario.anEvent()
	  				           .on(asset1)
					           .ofType(eventType2)
					           .withPerformedBy(user)
					           .withOwner(primaryOrg)
					           .withTenant(tenant)
					           .withGroup(group)		                      
					           .withResult(Status.FAIL)
		                       .withAssetStatus(assetStatus)
					           .build();
		
		Event event3 = scenario.anEvent()
					           .on(asset1)
					           .ofType(eventType3)
					           .withPerformedBy(user)
					           .withOwner(primaryOrg)
					           .withTenant(tenant)
					           .withGroup(group)		                      
					           .withResult(Status.NA)
		                       .withAssetStatus(assetStatus)
					           .build();
		
		TreeSet<Event> events = new TreeSet<Event>();
		events.add(event1);
		events.add(event2);
		events.add(event3);
		group.setEvents(events);

		scenario.save(group);
	}

	
	
    @Override
    protected ReportingPage navigateToPage() {
        return startAsCompany(TEST_TENANT).login().clickReportingLink();
    }

    @Parameters
	public static Collection<String[]> data() {
		ArrayList<String[]> dataList = new ArrayList<String[]>();
		dataList.add(new String[]{"Pass"});
		dataList.add(new String[]{"N/A"});
		dataList.add(new String[]{"Fail"});
		return dataList;
	}
	
	@Test
	public void show_just_events_that_passed_when_pass_selected_in_the_result_filter() throws Exception {
		page.selectResult(resultName);
		
		page.clickRunSearchButton();

		assertTrue(page.hasSearchResults());
		List<String> eventResults = page.getEventResults();
		
		for (String eventResult: eventResults) {
			assertEquals(resultName, eventResult);
		}
	}

}
