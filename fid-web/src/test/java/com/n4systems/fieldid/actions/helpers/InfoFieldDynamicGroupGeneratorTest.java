package com.n4systems.fieldid.actions.helpers;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.ImmutableSortedSet;
import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.test.helpers.FluentArrayList;


public class InfoFieldDynamicGroupGeneratorTest {

	private static final CommonProductAttributeFinder NOT_TO_BE_USED_FINDER = null;
	
	
	private final class CommonProductAttributeFinderReturning implements CommonProductAttributeFinder {
		SortedSet<String> commonInfoFields;

		public CommonProductAttributeFinderReturning(SortedSet<String> commonInfoFields) {
			super();
			this.commonInfoFields = commonInfoFields;
		}

		public SortedSet<String> findAllCommonInfoFieldNames(List<Long> assetTypeIds) {
			return commonInfoFields;
		}
	}


	@Test
	public void should_call_the_attribute_finder_once_only_once_for_info_fields_when_called_multiple_times() throws Exception {
		CommonProductAttributeFinder commonAttributeFinder = createMock(CommonProductAttributeFinder.class);
		expect(commonAttributeFinder.findAllCommonInfoFieldNames(new FluentArrayList<Long>(1L))).andReturn(new TreeSet<String>());
		replay(commonAttributeFinder);
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		sut.getDynamicGroups(null, new FluentArrayList<Long>(1L));
		sut.getDynamicGroups(null, new FluentArrayList<Long>(1L));
		sut.getDynamicGroups(null, new FluentArrayList<Long>(1L));
		
		verify(commonAttributeFinder);
	}
	
	
	@Test
	public void should_call_the_attribute_finder_with_list_of_product_type_ids_passed_in_when_the_selected_product_type_id_is_null() throws Exception {
		List<Long> productTypeIds = new FluentArrayList<Long>(1L);

		CommonProductAttributeFinder commonAttributeFinder = createMock(CommonProductAttributeFinder.class);
		expect(commonAttributeFinder.findAllCommonInfoFieldNames(productTypeIds)).andReturn(new TreeSet<String>());
		replay(commonAttributeFinder);
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		sut.getDynamicGroups(null, productTypeIds);
		
		verify(commonAttributeFinder);
	}
	
	
	@Test
	public void should_call_the_attribute_finder_with_list_of_only_the_selected_product_type_id_selected_product_type_id_is_not_null() throws Exception {
		List<Long> productTypeIds = new FluentArrayList<Long>(10L, 4L, 1L, 5040L);

		CommonProductAttributeFinder commonAttributeFinder = createMock(CommonProductAttributeFinder.class);
		expect(commonAttributeFinder.findAllCommonInfoFieldNames(new FluentArrayList<Long>(1L))).andReturn(new TreeSet<String>());
		replay(commonAttributeFinder);
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		sut.getDynamicGroups(1L, productTypeIds);
		
		verify(commonAttributeFinder);
	}
	
	@Test
	public void should_not_call_find_common_info_field_finder_if_there_are_no_product_types_in_the_list() {
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(NOT_TO_BE_USED_FINDER , "pre");
		sut.getDynamicGroups(null, new ArrayList<Long>());
	}
	
	@Test
	public void should_return_an_empty_set_of_column_mappings_when_there_are_no_product_types_to_select() {
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(NOT_TO_BE_USED_FINDER , "pre");
		
		
		List<ColumnMappingGroup> actualDynamicGroup = sut.getDynamicGroups(null, new ArrayList<Long>());
		
		
		assertThat(actualDynamicGroup.size(), equalTo(1));
		assertTrue(actualDynamicGroup.get(0).getMappings().isEmpty());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_create_generate_on_column_mapping_per_info_field_found() throws Exception {
		ImmutableSortedSet<String> commonInfoFields = ImmutableSortedSet.of("field1", "field3", "field4");
		CommonProductAttributeFinder commonAttributeFinder = new CommonProductAttributeFinderReturning(commonInfoFields);
		
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		List<ColumnMappingGroup> actualColumnMappings = sut.getDynamicGroups(null, new ArrayList<Long>());
		
		for (ColumnMappingGroup columnMappingGroup : actualColumnMappings) {
			for (ColumnMapping columnMapping : columnMappingGroup.getMappings()) {
				assertThat(columnMapping, 
						hasProperty("label", anyOf(equalTo("field1"), equalTo("field3"), equalTo("field4"))));
			}
		}
	}
	
	
	@Test
	public void should_create_generate() throws Exception {
		CommonProductAttributeFinder commonAttributeFinder = new CommonProductAttributeFinderReturning(ImmutableSortedSet.of("field1", "field3", "field4"));
		
		InfoFieldDynamicGroupGenerator sut = new InfoFieldDynamicGroupGenerator(commonAttributeFinder, "pre");
		
		List<ColumnMappingGroup> actualColumnMappings = sut.getDynamicGroups(null, new ArrayList<Long>());
		
		for (ColumnMappingGroup columnMappingGroup : actualColumnMappings) {
			for (ColumnMapping columnMapping : columnMappingGroup.getMappings()) {
				assertThat(columnMapping, 
						hasProperty("id", Matchers.startsWith("pre" + "_infooption_")));
			}
		}
	}
}
