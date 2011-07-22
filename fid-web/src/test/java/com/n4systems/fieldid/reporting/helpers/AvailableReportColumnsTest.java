package com.n4systems.fieldid.reporting.helpers;

import com.google.common.collect.ImmutableSortedSet;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.n4systems.fieldid.reporting.helpers.ColumnMappingBuilder.aColumnMapping;
import static com.n4systems.fieldid.reporting.helpers.ColumnMappingGroupBuilder.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class AvailableReportColumnsTest {
	private static SortedSet<ColumnMappingGroupView> emptySet = ImmutableSortedSet.of();

	@Test
	public void should_retrive_column_mappings_from_the_static_column_provider() throws Exception {
		StaticColumnProvider staticColumnProvider = createMock(StaticColumnProvider.class);
		expect(staticColumnProvider.getMappings()).andReturn(new TreeSet<ColumnMappingGroupView>());
		replay(staticColumnProvider);
		
		AvailableReportColumns sut = new AvailableReportColumns(staticColumnProvider,  anEmptyDynamicColumnProvider());
		
		sut.getMappingGroups();
		
		verify(staticColumnProvider);
	}

	@Test
	public void should_return_the_column_mappings_that_were_retrieved_from_the_providers() throws Exception {
		ColumnMappingGroupView mappingGroup = aColumnMappingGroup().build();
		
		SortedSet<ColumnMappingGroupView> staticColumnMappings = ImmutableSortedSet.of(mappingGroup);
		
		StaticColumnProvider staticColumnProvider = aStaticColumnProviderForColumns(staticColumnMappings);
		
		AvailableReportColumns sut = new AvailableReportColumns(staticColumnProvider,  anEmptyDynamicColumnProvider());
		
		SortedSet<ColumnMappingGroupView> actualMappings = sut.getMappingGroups();
		
		assertThat(actualMappings, equalTo(staticColumnMappings));
	}

	@Test
	public void should_only_use_the_provider_once_even_after_multiple_calls_groups() throws Exception {
		StaticColumnProvider staticColumnProvider = createMock(StaticColumnProvider.class);
		expect(staticColumnProvider.getMappings()).andReturn(new TreeSet<ColumnMappingGroupView>());
		replay(staticColumnProvider);
		
		AvailableReportColumns sut = new AvailableReportColumns(staticColumnProvider, anEmptyDynamicColumnProvider());
		
		sut.getMappingGroups();
		sut.getMappingGroups();
		
		verify(staticColumnProvider);
	}
	
	@Test
	public void should_retrieve_dynamic_columns_from_the_dynamic_column_provider() throws Exception {
		DynamicColumnProvider dynamicColumnProvider = createMock(DynamicColumnProvider.class);
		expect(dynamicColumnProvider.getDynamicGroups()).andReturn(new TreeSet<ColumnMappingGroupView>());
		replay(dynamicColumnProvider);
		
		AvailableReportColumns sut = new AvailableReportColumns(anEmptyStaticColumnProvider(), dynamicColumnProvider);
		
		sut.getMappingGroups();
		
		verify(dynamicColumnProvider);
	}

	@Test
	public void should_use_provided_filter_to_reduce_the_entire_set_of_columns() throws Exception {
		ColumnMappingGroupView staticMappingGroup = aStaticColumnMappingGroup().withMappings(aColumnMapping().build()).build();
		ColumnMappingGroupView dynamicColumnMappingGroup = aDynamicColumnMappingGroup().withMappings(aColumnMapping().build()).build();
		int numberOfColumnMappings = 2;
		
		ReportColumnFilter reportColumnFilter = createMock(ReportColumnFilter.class);
		expect(reportColumnFilter.available((ColumnMappingView)anyObject())).andReturn(true).times(numberOfColumnMappings);
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
			public boolean available(ColumnMappingView columnMapping) {
				return false;
			}
		};
		ColumnMappingGroupView staticMappingGroup = aStaticColumnMappingGroup().withMappings(aColumnMapping().build()).build();
		ColumnMappingGroupView dynamicColumnMappingGroup = aDynamicColumnMappingGroup().withMappings(aColumnMapping().build()).build();
		
		AvailableReportColumns sut = new AvailableReportColumns(
				aStaticColumnProviderForColumns(ImmutableSortedSet.of(staticMappingGroup)), 
				aDynamicColumnProviderFor(ImmutableSortedSet.of(dynamicColumnMappingGroup)));
		
		sut.setFilter(reportColumnFilter);
		
		SortedSet<ColumnMappingGroupView> actualMappings = sut.getMappingGroups();
		
		assertThat(actualMappings, new ColumnMappingGroupsWithEmptyMappings());
	}

	private DynamicColumnProvider anEmptyDynamicColumnProvider() {
		return aDynamicColumnProviderFor(emptySet);
	}

	private DynamicColumnProvider aDynamicColumnProviderFor(final SortedSet<ColumnMappingGroupView> dynamicGroups) {
		return new DynamicColumnProvider() {
			public SortedSet<ColumnMappingGroupView> getDynamicGroups() {
				return dynamicGroups;
			}
		};
	}
	
	private StaticColumnProvider anEmptyStaticColumnProvider() {
		return aStaticColumnProviderForColumns(emptySet);
	}

	private StaticColumnProvider aStaticColumnProviderForColumns(final SortedSet<ColumnMappingGroupView> staticColumnMappings) {
		StaticColumnProvider staticColumnProvider = new StaticColumnProvider() {
			public SortedSet<ColumnMappingGroupView> getMappings() { return staticColumnMappings; }
		};
		return staticColumnProvider;
	}

	private class ColumnMappingGroupsWithEmptyMappings extends TypeSafeMatcher<Collection<ColumnMappingGroupView>> {

		@Override
		public boolean matchesSafely(Collection<ColumnMappingGroupView> item) {
			for (ColumnMappingGroupView columnMappingGroup : item) {
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
