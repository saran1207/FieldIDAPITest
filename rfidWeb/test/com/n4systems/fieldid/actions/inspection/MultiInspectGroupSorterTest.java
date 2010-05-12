package com.n4systems.fieldid.actions.inspection;

import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.InspectionTypeGroupBuilder.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.test.helpers.FluentHashSet;


public class MultiInspectGroupSorterTest {

	
	@SuppressWarnings("unchecked")
	@Test
	public void should_return_an_empty_list_of_groups_when_the_inspection_types_are_empty() throws Exception {
		MultiInspectGroupSorter sut = new MultiInspectGroupSorter(Collections.EMPTY_SET);
		assertThat(sut.getGroups(), Matchers.equalTo(Collections.EMPTY_LIST));
	}
	
	@Test
	public void should_return_an_list_with_a_single_inspection_type_group_when_there_is_only_one_inspection_type_given() {
		InspectionTypeGroup group = anInspectionTypeGroup().build();
		FluentHashSet<InspectionType> inspectionTypes = new FluentHashSet<InspectionType>(anInspectionType().withGroup(group).build());
		List<InspectionTypeGroup> inspectionTypeGroupNames = new FluentArrayList<InspectionTypeGroup>(group);
		
		
		MultiInspectGroupSorter sut = new MultiInspectGroupSorter(inspectionTypes);
		
		assertThat(sut.getGroups(), Matchers.equalTo(inspectionTypeGroupNames));
	}
	
	
	@Test
	public void should_return_an_list_with_a_single_inspection_type_group_when_all_inspection_types_have_the_same_group() {
		InspectionTypeGroup group = anInspectionTypeGroup().build();
		FluentHashSet<InspectionType> inspectionTypes = new FluentHashSet<InspectionType>(
				anInspectionType().withGroup(group).build(),
				anInspectionType().withGroup(group).build());
		List<InspectionTypeGroup> inspectionTypeGroupNames = new FluentArrayList<InspectionTypeGroup>(group);
		
		
		MultiInspectGroupSorter sut = new MultiInspectGroupSorter(inspectionTypes);
		
		assertThat(sut.getGroups(), Matchers.equalTo(inspectionTypeGroupNames));
	}
	
	
	@Test
	public void should_return_an_list_of_group_name_in_alphabeticable_order_when_there_are_multiple_groups() {
		InspectionTypeGroup zGroup = anInspectionTypeGroup().withName("Z group").build();
		InspectionTypeGroup gGroup = anInspectionTypeGroup().withName("G group").build();
		InspectionTypeGroup qGroup = anInspectionTypeGroup().withName("q group").build();
		InspectionTypeGroup aGroup = anInspectionTypeGroup().withName("a group").build();
		
		FluentHashSet<InspectionType> inspectionTypes = new FluentHashSet<InspectionType>(
				anInspectionType().withGroup(zGroup).build(),
				anInspectionType().withGroup(aGroup).build(),
				anInspectionType().withGroup(qGroup).build(),
				anInspectionType().withGroup(gGroup).build()
				);
		List<InspectionTypeGroup> inspectionTypeGroupNames = new FluentArrayList<InspectionTypeGroup>(aGroup, gGroup, qGroup, zGroup);
		
		MultiInspectGroupSorter sut = new MultiInspectGroupSorter(inspectionTypes);
		
		assertThat(sut.getGroups(), Matchers.equalTo(inspectionTypeGroupNames));
	}
	
	
	
	
	
	@Test
	public void should_return_a_list_of_inspection_types_in_alphabeticable_for_the_given_group() {
		InspectionTypeGroup group = anInspectionTypeGroup().withName("group 1").build();
		InspectionType typeB = anInspectionType().named("inspectionType b").withGroup(group).build();
		InspectionType typeA = anInspectionType().named("inspectionType a").withGroup(group).build();
		InspectionType typeZ = anInspectionType().named("inspectionType z").withGroup(group).build();
		
		FluentHashSet<InspectionType> inspectionTypes = new FluentHashSet<InspectionType>(typeB, typeA, typeZ);
		List<InspectionType> expectedList = ImmutableList.of(typeA, typeB, typeZ);
		
		MultiInspectGroupSorter sut = new MultiInspectGroupSorter(inspectionTypes);
		
		assertThat(sut.getInspectionTypesForGroup(group), Matchers.equalTo(expectedList));
	}
	
	@Test
	public void should_split_list_of_inspection_types_by_group() {
		InspectionTypeGroup group1 = anInspectionTypeGroup().withName("group 1").build();
		InspectionTypeGroup group2 = anInspectionTypeGroup().withName("group 2").build();
		
		InspectionType typeA = anInspectionType().named("inspectionType a").withGroup(group2).build();
		InspectionType typeB = anInspectionType().named("inspectionType b").withGroup(group1).build();
		InspectionType typeZ = anInspectionType().named("inspectionType z").withGroup(group1).build();
		
		FluentHashSet<InspectionType> inspectionTypes = new FluentHashSet<InspectionType>(typeB, typeA, typeZ);
		List<InspectionType> expectedGroup1List = ImmutableList.of(typeB, typeZ);
		List<InspectionType> expectedGroup2List = ImmutableList.of(typeA);
		
		MultiInspectGroupSorter sut = new MultiInspectGroupSorter(inspectionTypes);
		
		assertThat(sut.getInspectionTypesForGroup(group1), Matchers.equalTo(expectedGroup1List));
		assertThat(sut.getInspectionTypesForGroup(group2), Matchers.equalTo(expectedGroup2List));
	}
	
	
	
	
	
}
