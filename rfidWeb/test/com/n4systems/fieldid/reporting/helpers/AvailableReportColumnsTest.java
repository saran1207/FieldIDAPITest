package com.n4systems.fieldid.reporting.helpers;

import static com.n4systems.fieldid.reporting.helpers.ColumnMappingBuilder.*;
import static com.n4systems.fieldid.reporting.helpers.ColumnMappingGroupBuilder.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.common.collect.ImmutableSortedSet;
import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;


public class AvailableReportColumnsTest {
	private static SortedSet<ColumnMappingGroup> emptySet = ImmutableSortedSet.of();
	

	
	


	


	@Test
	public void should_retrive_column_mappings_from_the_static_column_provider() throws Exception {
		StaticColumnProvider staticColumnProvider = createMock(StaticColumnProvider.class);
		expect(staticColumnProvider.getMappings()).andReturn(new TreeSet<ColumnMappingGroup>());
		replay(staticColumnProvider);
		
		AvailableReportColumns sut = new AvailableReportColumns(staticColumnProvider,  anEmptyDynamicColumnProvider());
		
		sut.getMappingGroups();
		
		
		verify(staticColumnProvider);
	}

	
	
	
	@Test
	public void should_return_the_column_mappings_that_were_retrieved_from_the_providers() throws Exception {
		ColumnMappingGroup mappingGroup = aColumnMappingGroup().build();
		
		SortedSet<ColumnMappingGroup> staticColumnMappings = ImmutableSortedSet.of(mappingGroup);
		
		StaticColumnProvider staticColumnProvider = aStaticColumnProviderForColumns(staticColumnMappings);
		
		AvailableReportColumns sut = new AvailableReportColumns(staticColumnProvider,  anEmptyDynamicColumnProvider());
		
		SortedSet<ColumnMappingGroup> actualMappings = sut.getMappingGroups();
		
		assertThat(actualMappings, equalTo(staticColumnMappings));
	}

	@Test
	public void should_only_use_the_provider_once_even_after_multiple_calls_groups() throws Exception {
		StaticColumnProvider staticColumnProvider = createMock(StaticColumnProvider.class);
		expect(staticColumnProvider.getMappings()).andReturn(new TreeSet<ColumnMappingGroup>());
		replay(staticColumnProvider);
		
		AvailableReportColumns sut = new AvailableReportColumns(staticColumnProvider, anEmptyDynamicColumnProvider());
		
		sut.getMappingGroups();
		sut.getMappingGroups();
		
		verify(staticColumnProvider);
	}
	
	
	@Test
	public void should_retrieve_dynamic_columns_from_the_dynamic_column_provider() throws Exception {
		DynamicColumnProvider dynamicColumnProvider = createMock(DynamicColumnProvider.class);
		expect(dynamicColumnProvider.getDynamicGroups()).andReturn(new TreeSet<ColumnMappingGroup>());
		replay(dynamicColumnProvider);
		
		AvailableReportColumns sut = new AvailableReportColumns(anEmptyStaticColumnProvider(), dynamicColumnProvider);
		
		sut.getMappingGroups();
		
		verify(dynamicColumnProvider);
	}

	
	
	@Test
	public void should_use_provided_filter_to_reduce_the_entire_set_of_columns() throws Exception {
		ColumnMappingGroup staticMappingGroup = aStaticColumnMappingGroup().withMappings(aColumnMapping().build()).build();
		ColumnMappingGroup dynamicColumnMappingGroup = aDynamicColumnMappingGroup().withMappings(aColumnMapping().build()).build();
		int numberOfColumnMappings = 2;
		
		ReportColumnFilter reportColumnFilter = createMock(ReportColumnFilter.class);
		expect(reportColumnFilter.available((ColumnMapping)anyObject())).andReturn(true).times(numberOfColumnMappings);
		replay(reportColumnFilter);
		
		AvailableReportColumns sut = new AvailableReportColumns(
											aStaticColumnProviderForColumns(ImmutableSortedSet.of(staticMappingGroup)), 
											aDynamicColumnProviderFor(ImmutableSortedSet.of(dynamicColumnMappingGroup)));
		sut.setFilter(reportColumnFilter);
		
		sut.getMappingGroups();
		
		verify(reportColumnFilter);
	}
	
	
	@Test
	public void should_return_the_filtered_reduced_set_of_columns() throws Exception {
		ReportColumnFilter reportColumnFilter = new ReportColumnFilter() {
			public boolean available(ColumnMapping columnMapping) {
				return false;
			}
		};
		ColumnMappingGroup staticMappingGroup = aStaticColumnMappingGroup().withMappings(aColumnMapping().build()).build();
		ColumnMappingGroup dynamicColumnMappingGroup = aDynamicColumnMappingGroup().withMappings(aColumnMapping().build()).build();
		
		AvailableReportColumns sut = new AvailableReportColumns(
				aStaticColumnProviderForColumns(ImmutableSortedSet.of(staticMappingGroup)), 
				aDynamicColumnProviderFor(ImmutableSortedSet.of(dynamicColumnMappingGroup)));
		
		sut.setFilter(reportColumnFilter);
		
		SortedSet<ColumnMappingGroup> actualMappings = sut.getMappingGroups();	
		
		assertThat(actualMappings, new ColumnMappingGroupsWithEmptyMappings());
	}


	private DynamicColumnProvider anEmptyDynamicColumnProvider() {
		return aDynamicColumnProviderFor(emptySet);
	}
	
	
	private DynamicColumnProvider aDynamicColumnProviderFor(final SortedSet<ColumnMappingGroup> dynamicGroups) {
		return new DynamicColumnProvider() {
			public SortedSet<ColumnMappingGroup> getDynamicGroups() {
				return dynamicGroups;
			}
		};
	}
	
	private StaticColumnProvider anEmptyStaticColumnProvider() {
		return aStaticColumnProviderForColumns(emptySet);
	}
	

	private StaticColumnProvider aStaticColumnProviderForColumns(final SortedSet<ColumnMappingGroup> staticColumnMappings) {
		StaticColumnProvider staticColumnProvider = new StaticColumnProvider() {
			public SortedSet<ColumnMappingGroup> getMappings() { return staticColumnMappings; }
		};
		return staticColumnProvider;
	}
	
	
	private class ColumnMappingGroupsWithEmptyMappings extends TypeSafeMatcher<Collection<ColumnMappingGroup>> {

		@Override
		public boolean matchesSafely(Collection<ColumnMappingGroup> item) {
			for (ColumnMappingGroup columnMappingGroup : item) {
				if (!columnMappingGroup.getMappings().isEmpty()) {
					return false;
				}
			}
			return true;
		}

		public void describeTo(Description description) {
			description.appendText("empty mappings on all of the mapping groups");
		}
		
	}
}
