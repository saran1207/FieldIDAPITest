package com.n4systems.webservice.server.handlers;

import static org.junit.Assert.*;
import static com.n4systems.model.builders.InspectionBuilder.*;

import java.util.Date;
import java.util.List;

import com.n4systems.model.inspection.NewestInspectionsForAssetIdLoader;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Inspection;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.test.helpers.DateHelper;
import com.n4systems.test.helpers.FluentArrayList;

public class RealTimeInspectionLookupHandlerTest {
	
	private final long anyTenantId = 1L;
	private final long anyProductId = 1L;
	
	private List<Inspection> multipleInspections;
	private Date olderDate;
	private Date recentDate;
	private Date mostRecentDate;
	
	@Before
	public void setup() {
		olderDate = DateHelper.createDate(2009, 0, 1);
		recentDate = DateHelper.createDate(2009, 8, 20);
		mostRecentDate = DateHelper.createDate(2009, 9, 20);
		
		multipleInspections = new FluentArrayList<Inspection>(
				anInspection().performedOn(recentDate).build(), 
				anInspection().performedOn(recentDate).build());
	}
	
	@Test
	public void inspections_found_and_no_modified_date_returns_inspections() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(multipleInspections, lookupHandler.setProductId(anyProductId).setLastInspectionDate(null).lookup());
	}
	
	@Test
	public void inspections_found_and_older_modified_date_returns_inspections() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(multipleInspections, lookupHandler.setProductId(anyProductId).setLastInspectionDate(olderDate).lookup());		
	}
	
	@Test
	public void inspections_found_and_same_modified_date_as_inspections_returns_empty_list() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(0, lookupHandler.setProductId(anyProductId).setLastInspectionDate(recentDate).lookup().size());				
	}
	
	@Test
	public void inspections_found_and_newer_modified_date_than_inspections_returns_empty_list() {
		RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(getInspectionsLoaderThatReturnsMultipleInspections());
		assertEquals(0, lookupHandler.setProductId(anyProductId).setLastInspectionDate(mostRecentDate).lookup().size());						
	}
	
	private NewestInspectionsForAssetIdLoader getInspectionsLoaderThatReturnsMultipleInspections() {
		NewestInspectionsForAssetIdLoader loader = new NewestInspectionsForAssetIdLoader(new TenantOnlySecurityFilter(anyTenantId)) {
			@Override
			public List<Inspection> load() {
				return multipleInspections;
			}
		};
		
		return loader;
	}
}
