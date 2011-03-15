package com.n4systems.fieldid.actions.helpers;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.ImmutableSortedSet;
import com.n4systems.test.helpers.FluentArrayList;


public class InfoFieldDynamicGroupGeneratorTest {

	private static final CommonAssetAttributeFinder NOT_TO_BE_USED_FINDER = null;
	
	
	private final class CommonAssetAttributeFinderReturning implements CommonAssetAttributeFinder {
		SortedSet<String> commonInfoFields;

		public CommonAssetAttributeFinderReturning(SortedSet<String> commonInfoFields) {
			super();
			this.commonInfoFields = commonInfoFields;
		}

		public SortedSet<String> findAllCommonInfoFieldNames(List<Long> assetTypeIds) {
			return commonInfoFields;
		}
	}


	@Test
	public void should_call_the_attribute_finder_once_only_once_for_info_fields_when_called_multiple_times() throws Exception {
		CommonAssetAttributeFinder commonAttributeFinder = createMock(CommonAssetAttributeFinder.class);
		expect(commonAttributeFinder.findAllCommonInfoFieldNames(new FluentArrayList<Long>(1L))).andReturn(new TreeSet<String>());
		replay(commonAttributeFinder);
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		sut.getDynamicGroups(null, new FluentArrayList<Long>(1L));
		sut.getDynamicGroups(null, new FluentArrayList<Long>(1L));
		sut.getDynamicGroups(null, new FluentArrayList<Long>(1L));
		
		verify(commonAttributeFinder);
	}
	
	
	@Test
	public void should_call_the_attribute_finder_with_list_of_asset_type_ids_passed_in_when_the_selected_asset_type_id_is_null() throws Exception {
		List<Long> assetTypeIds = new FluentArrayList<Long>(1L);

		CommonAssetAttributeFinder commonAttributeFinder = createMock(CommonAssetAttributeFinder.class);
		expect(commonAttributeFinder.findAllCommonInfoFieldNames(assetTypeIds)).andReturn(new TreeSet<String>());
		replay(commonAttributeFinder);
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		sut.getDynamicGroups(null, assetTypeIds);
		
		verify(commonAttributeFinder);
	}
	
	
	@Test
	public void should_call_the_attribute_finder_with_list_of_only_the_selected_asset_type_id_selected_asset_type_id_is_not_null() throws Exception {
		List<Long> assetTypeIds = new FluentArrayList<Long>(10L, 4L, 1L, 5040L);

		CommonAssetAttributeFinder commonAttributeFinder = createMock(CommonAssetAttributeFinder.class);
		expect(commonAttributeFinder.findAllCommonInfoFieldNames(new FluentArrayList<Long>(1L))).andReturn(new TreeSet<String>());
		replay(commonAttributeFinder);
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		sut.getDynamicGroups(1L, assetTypeIds);
		
		verify(commonAttributeFinder);
	}
	
	@Test
	public void should_not_call_find_common_info_field_finder_if_there_are_no_asset_types_in_the_list() {
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(NOT_TO_BE_USED_FINDER , "pre");
		sut.getDynamicGroups(null, new ArrayList<Long>());
	}
	
	@Test
	public void should_return_an_empty_set_of_column_mappings_when_there_are_no_asset_types_to_select() {
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(NOT_TO_BE_USED_FINDER , "pre");
		
		
		List<ColumnMappingGroupView> actualDynamicGroup = sut.getDynamicGroups(null, new ArrayList<Long>());
		
		
		assertThat(actualDynamicGroup.size(), equalTo(1));
		assertTrue(actualDynamicGroup.get(0).getMappings().isEmpty());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_create_generate_on_column_mapping_per_info_field_found() throws Exception {
		ImmutableSortedSet<String> commonInfoFields = ImmutableSortedSet.of("field1", "field3", "field4");
		CommonAssetAttributeFinder commonAttributeFinder = new CommonAssetAttributeFinderReturning(commonInfoFields);
		
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		List<ColumnMappingGroupView> actualColumnMappings = sut.getDynamicGroups(null, new ArrayList<Long>());
		
		for (ColumnMappingGroupView columnMappingGroup : actualColumnMappings) {
			for (ColumnMappingView columnMapping : columnMappingGroup.getMappings()) {
				assertThat(columnMapping, 
						hasProperty("label", anyOf(equalTo("field1"), equalTo("field3"), equalTo("field4"))));
			}
		}
	}
	
	
	@Test
	public void should_create_generate() throws Exception {
		CommonAssetAttributeFinder commonAttributeFinder = new CommonAssetAttributeFinderReturning(ImmutableSortedSet.of("field1", "field3", "field4"));
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		List<ColumnMappingGroupView> actualColumnMappings = sut.getDynamicGroups(null, new ArrayList<Long>());
		
		for (ColumnMappingGroupView columnMappingGroup : actualColumnMappings) {
			for (ColumnMappingView columnMapping : columnMappingGroup.getMappings()) {
				assertThat(columnMapping, 
						hasProperty("id", Matchers.startsWith("pre" + "_infooption_")));
			}
		}
	}
}
