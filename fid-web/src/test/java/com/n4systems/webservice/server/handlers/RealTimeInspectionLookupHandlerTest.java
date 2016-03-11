package com.n4systems.webservice.server.handlers;

import com.n4systems.model.ThingEvent;
import com.n4systems.model.event.NewestEventsForAssetIdLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.test.helpers.DateHelper;
import com.n4systems.test.helpers.FluentArrayList;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static org.junit.Assert.assertEquals;

public class RealTimeInspectionLookupHandlerTest {
	
	private final long anyTenantId = 1L;
	private final long anyAssetId = 1L;
	
	private List<ThingEvent> multipleEvents;
	private Date olderDate;
	private Date recentDate;
	private Date mostRecentDate;
	
	@Before
	public void setup() {
		olderDate = DateHelper.createDate(2009, 0, 1);
		recentDate = DateHelper.createDate(2009, 8, 20);
		mostRecentDate = DateHelper.createDate(2009, 9, 20);
		
		multipleEvents = new FluentArrayList<ThingEvent>(
				anEvent().performedOn(recentDate).build(),
				anEvent().performedOn(recentDate).build());
	}
	
	@Test
	public void inspections_found_and_no_modified_date_returns_inspections() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(multipleEvents, lookupHandler.setAssetId(anyAssetId).setLastEventDate(null).lookup());
	}
	
	@Test
	public void inspections_found_and_older_modified_date_returns_inspections() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(multipleEvents, lookupHandler.setAssetId(anyAssetId).setLastEventDate(olderDate).lookup());
	}
	
	@Test
	public void inspections_found_and_same_modified_date_as_inspections_returns_empty_list() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(0, lookupHandler.setAssetId(anyAssetId).setLastEventDate(recentDate).lookup().size());
	}
	
	@Test
	public void inspections_found_and_newer_modified_date_than_inspections_returns_empty_list() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(0, lookupHandler.setAssetId(anyAssetId).setLastEventDate(mostRecentDate).lookup().size());
	}
	
	private NewestEventsForAssetIdLoader getInspectionsLoaderThatReturnsMultipleInspections() {
		NewestEventsForAssetIdLoader loader = new NewestEventsForAssetIdLoader(new TenantOnlySecurityFilter(anyTenantId)) {
			@Override
			public List<ThingEvent> load() {
				return multipleEvents;
			}
		};
		
		return loader;
	}
}
