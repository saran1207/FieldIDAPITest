package com.n4systems.webservice.server.handlers;

import static org.junit.Assert.*;
import static com.n4systems.model.builders.EventBuilder.*;

import java.util.Date;
import java.util.List;

import com.n4systems.model.Event;
import com.n4systems.model.event.NewestEventsForAssetIdLoader;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.test.helpers.DateHelper;
import com.n4systems.test.helpers.FluentArrayList;

public class RealTimeInspectionLookupHandlerTest {
	
	private final long anyTenantId = 1L;
	private final long anyProductId = 1L;
	
	private List<Event> multipleEvents;
	private Date olderDate;
	private Date recentDate;
	private Date mostRecentDate;
	
	@Before
	public void setup() {
		olderDate = DateHelper.createDate(2009, 0, 1);
		recentDate = DateHelper.createDate(2009, 8, 20);
		mostRecentDate = DateHelper.createDate(2009, 9, 20);
		
		multipleEvents = new FluentArrayList<Event>(
				anEvent().performedOn(recentDate).build(),
				anEvent().performedOn(recentDate).build());
	}
	
	@Test
	public void inspections_found_and_no_modified_date_returns_inspections() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(multipleEvents, lookupHandler.setProductId(anyProductId).setLastEventDate(null).lookup());
	}
	
	@Test
	public void inspections_found_and_older_modified_date_returns_inspections() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(multipleEvents, lookupHandler.setProductId(anyProductId).setLastEventDate(olderDate).lookup());
	}
	
	@Test
	public void inspections_found_and_same_modified_date_as_inspections_returns_empty_list() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(0, lookupHandler.setProductId(anyProductId).setLastEventDate(recentDate).lookup().size());
	}
	
	@Test
	public void inspections_found_and_newer_modified_date_than_inspections_returns_empty_list() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(0, lookupHandler.setProductId(anyProductId).setLastEventDate(mostRecentDate).lookup().size());
	}
	
	private NewestEventsForAssetIdLoader getInspectionsLoaderThatReturnsMultipleInspections() {
		NewestEventsForAssetIdLoader loader = new NewestEventsForAssetIdLoader(new TenantOnlySecurityFilter(anyTenantId)) {
			@Override
			public List<Event> load() {
				return multipleEvents;
			}
		};
		
		return loader;
	}
}
