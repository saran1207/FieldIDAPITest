package com.n4systems.fieldid.utils;

import static com.n4systems.model.builders.OrgBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;



public class SaveReportSearchCriteriaConverterTest {


	@SuppressWarnings("unchecked")
	@Test
	public void should_load_owner_from_saved_report_with_owner_id() {
		SavedReport savedReport = new SavedReport();
		savedReport.getCriteria().put(SavedReport.OWNER_ID, "1");
		
		FilteredIdLoader<BaseOrg> mockLoader = createMock(FilteredIdLoader.class);
		expect(mockLoader.setId(1L)).andReturn(mockLoader);
		expect(mockLoader.load()).andReturn(aCustomerOrg().build());
		replay(mockLoader);
		
		LoaderFactory loaderFactory = createMock(LoaderFactory.class);
		expect(loaderFactory.createFilteredIdLoader(BaseOrg.class)).andReturn(mockLoader);
		replay(loaderFactory);
		
		SavedReportSearchCriteriaConverter sut = new SavedReportSearchCriteriaConverter(loaderFactory, null);
		
		InspectionSearchContainer actualContainer = sut.convert(savedReport);
		
		assertNotNull(actualContainer.getOwner());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_not_load_owner_from_saved_report_with_no_owner_id() {
		SavedReport savedReport = new SavedReport();
		
		FilteredIdLoader<BaseOrg> mockLoader = createMock(FilteredIdLoader.class);
		replay(mockLoader);
		
		LoaderFactory loaderFactory = createMock(LoaderFactory.class);
		expect(loaderFactory.createFilteredIdLoader(BaseOrg.class)).andReturn(mockLoader);
		replay(loaderFactory);
		
		SavedReportSearchCriteriaConverter sut = new SavedReportSearchCriteriaConverter(loaderFactory, null);
		
		InspectionSearchContainer actualContainer = sut.convert(savedReport);
		
		assertNull(actualContainer.getOwner());
	}
	
}
