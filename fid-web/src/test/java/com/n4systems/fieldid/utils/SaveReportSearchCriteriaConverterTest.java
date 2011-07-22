package com.n4systems.fieldid.utils;

import static com.n4systems.model.builders.OrgBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.n4systems.fieldid.permissions.AlwaysOffSystemSecurityGuardTestDouble;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import org.junit.Test;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.EntityByIdIncludingArchivedLoader;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;

import java.util.Arrays;
import java.util.List;


public class SaveReportSearchCriteriaConverterTest {

    private static final List<String> ALL_COLUMN_IDS = Arrays.asList("column1", "column2", "column3");

	@SuppressWarnings("unchecked")
	@Test
	public void should_load_owner_from_saved_report_with_owner_id() {
		SavedReport savedReport = new SavedReport();
		savedReport.getCriteria().put(SavedReport.OWNER_ID, "1");
		
		EntityByIdIncludingArchivedLoader<BaseOrg> mockLoader = createMock(EntityByIdIncludingArchivedLoader.class);
		expect(mockLoader.setId(1L)).andReturn(mockLoader);
		expect(mockLoader.load()).andReturn(aCustomerOrg().build());
		replay(mockLoader);
		
		LoaderFactory loaderFactory = createMock(LoaderFactory.class);
		expect(loaderFactory.createEntityByIdLoader(BaseOrg.class)).andReturn(mockLoader);
		replay(loaderFactory);
		
		SavedReportSearchCriteriaConverter sut = new SavedReportSearchCriteriaConverter(loaderFactory, null, new AlwaysOffSystemSecurityGuardTestDouble()) {
            @Override
            protected List<String> getListOfAllColumnIds() {
                return ALL_COLUMN_IDS;
            }
        };
		
		EventSearchContainer actualContainer = sut.convert(savedReport);
		
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
		
		SavedReportSearchCriteriaConverter sut = new SavedReportSearchCriteriaConverter(loaderFactory, null, new AlwaysOffSystemSecurityGuardTestDouble()) {
            @Override
            protected List<String> getListOfAllColumnIds() {
                return ALL_COLUMN_IDS;
            }
        };
		
		EventSearchContainer actualContainer = sut.convert(savedReport);
		
		assertNull(actualContainer.getOwner());
	}
	
}
