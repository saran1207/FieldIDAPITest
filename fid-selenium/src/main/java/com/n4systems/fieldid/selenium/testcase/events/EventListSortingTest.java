package com.n4systems.fieldid.selenium.testcase.events;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.TreeSet;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.misc.DateUtil;
import com.n4systems.fieldid.selenium.pages.EventsPerformedPage;
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

public class EventListSortingTest extends
		PageNavigatingTestCase<EventsPerformedPage> {

	private static final String TEST_EVENT_TYPE3 = "TestEventType3";
	private static final String TEST_EVENT_TYPE2 = "TestEventType2";
	private static final String TEST_EVENT_TYPE1 = "TestEventType1";
	private static final String TEST_TENANT = "test1";
	private static final String ASSET_IDENTIFIER1 = "12345";
	private static final String USERID1 = "userid1";
	private static final String USERID2 = "userid3";

	@Override
	public void setupScenario(Scenario scenario) {
		
		Tenant tenant = scenario.tenant(TEST_TENANT);
		
		PrimaryOrg primaryOrg = scenario.primaryOrgFor(TEST_TENANT);
		
		User user1 = scenario.defaultUser();
		User user2 = scenario.aUser()
							 .withUserId(USERID1)
							 .withFirstName("a")
							 .build();
		User user3 = scenario.aUser()
							 .withUserId(USERID2)
							 .withFirstName("b")
							 .build();
		
		AssetType type = scenario.anAssetType().named("TestAssetType")
				                               .build();

		AssetStatus assetStatus1 = scenario.anAssetStatus().named("AssetStatus1").build();
		AssetStatus assetStatus2 = scenario.anAssetStatus().named("AssetStatus2").build();
		AssetStatus assetStatus3 = scenario.anAssetStatus().named("AssetStatus3").build();
		
		Asset asset1 = scenario.anAsset()
				              .withOwner(primaryOrg)
				              .withIdentifier(ASSET_IDENTIFIER1)
				              .havingStatus(assetStatus1)
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
				                       .named(TEST_EVENT_TYPE2)
				                       .build();

		EventForm eventForm3 = scenario.anEventForm().build();

		EventType eventType3 = scenario.anEventType()
		                               .withEventForm(eventForm3)
				                       .named(TEST_EVENT_TYPE3)
				                       .build();

		EventGroup group = scenario.anEventGroup()
		                           .forTenant(tenant)
		                           .build();

		Event event1 = scenario.anEvent()
		                      .on(asset1)
		                      .ofType(eventType1)
		                      .withPerformedBy(user1)
		                      .performedOn(DateUtil.addDays(new Date(), -2))
		                      .withOwner(primaryOrg)
		                      .withTenant(tenant)
		                      .withGroup(group)		                      
		                      .withResult(Status.PASS)
		                      .withAssetStatus(assetStatus1)
		                      .build();

		Event event2 = scenario.anEvent()
	  				           .on(asset1)
					           .ofType(eventType2)
					           .withPerformedBy(user2)
    		                   .performedOn(DateUtil.addDays(new Date(), -1))
					           .withOwner(primaryOrg)
					           .withTenant(tenant)
					           .withGroup(group)		                      
					           .withResult(Status.FAIL)
		                       .withAssetStatus(assetStatus2)
					           .build();
		
		Event event3 = scenario.anEvent()
					           .on(asset1)
					           .ofType(eventType3)
					           .withPerformedBy(user3)
					           .withOwner(primaryOrg)
					           .withTenant(tenant)
					           .withGroup(group)		                      
					           .withResult(Status.NA)
		                       .withAssetStatus(assetStatus3)
					           .build();
		
		TreeSet<Event> events = new TreeSet<Event>();
		events.add(event1);
		events.add(event2);
		events.add(event3);
		group.setEvents(events);

		scenario.save(group);
	}

	@Override
	protected EventsPerformedPage navigateToPage() {
		return startAsCompany(TEST_TENANT).login().search(ASSET_IDENTIFIER1).clickEventHistoryTab();
	}
	
	@Test
	public void sort_by_date_performed() throws Exception {
		assertEquals(TEST_EVENT_TYPE3, page.getEventTypes().get(0));
		page.clickSortColumn("Date Performed");
		assertEquals(TEST_EVENT_TYPE1, page.getEventTypes().get(0));
	}
	
	@Test
	public void sort_by_event_type() throws Exception {
		assertEquals(TEST_EVENT_TYPE3, page.getEventTypes().get(0));
		page.clickSortColumn("Event Type");
		assertEquals(TEST_EVENT_TYPE1, page.getEventTypes().get(0));
	}
	
	@Test
	public void sort_by_performed_by() throws Exception {
		assertEquals(TEST_EVENT_TYPE3, page.getEventTypes().get(0));
		page.clickSortColumn("Performed By");
		assertEquals(TEST_EVENT_TYPE2, page.getEventTypes().get(0));
		page.clickSortColumn("Performed By");
		assertEquals(TEST_EVENT_TYPE1, page.getEventTypes().get(0));
	}
	
	@Test
	public void sort_by_result() throws Exception {
		assertEquals(TEST_EVENT_TYPE3, page.getEventTypes().get(0));
		page.clickSortColumn("Result");
		assertEquals(TEST_EVENT_TYPE2, page.getEventTypes().get(0));
		page.clickSortColumn("Result");
		assertEquals(TEST_EVENT_TYPE1, page.getEventTypes().get(0));
	}

	@Test
	public void sort_by_asset_status() throws Exception {
		assertEquals(TEST_EVENT_TYPE3, page.getEventTypes().get(0));
		page.clickSortColumn("Asset Status");
		assertEquals(TEST_EVENT_TYPE1, page.getEventTypes().get(0));
		page.clickSortColumn("Asset Status");
		assertEquals(TEST_EVENT_TYPE3, page.getEventTypes().get(0));
	}
	
}
