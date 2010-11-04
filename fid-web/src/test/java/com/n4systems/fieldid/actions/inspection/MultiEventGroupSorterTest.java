package com.n4systems.fieldid.actions.inspection;

import static com.n4systems.model.builders.EventTypeBuilder.*;
import static com.n4systems.model.builders.EventTypeGroupBuilder.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.test.helpers.FluentHashSet;


public class MultiEventGroupSorterTest {

	
	@SuppressWarnings("unchecked")
	@Test
	public void should_return_an_empty_list_of_groups_when_the_event_types_are_empty() throws Exception {
		MultiEventGroupSorter sut = new MultiEventGroupSorter(Collections.EMPTY_SET);
		assertThat(sut.getGroups(), Matchers.equalTo(Collections.EMPTY_LIST));
	}
	
	@Test
	public void should_return_an_list_with_a_single_event_type_group_when_there_is_only_one_event_type_given() {
		EventTypeGroup group = anEventTypeGroup().build();
		FluentHashSet<EventType> eventTypes = new FluentHashSet<EventType>(anEventType().withGroup(group).build());
		List<EventTypeGroup> eventTypeGroupNames = new FluentArrayList<EventTypeGroup>(group);
		
		
		MultiEventGroupSorter sut = new MultiEventGroupSorter(eventTypes);
		
		assertThat(sut.getGroups(), Matchers.equalTo(eventTypeGroupNames));
	}
	
	
	@Test
	public void should_return_an_list_with_a_single_event_type_group_when_all_event_types_have_the_same_group() {
		EventTypeGroup group = anEventTypeGroup().build();
		FluentHashSet<EventType> eventTypes = new FluentHashSet<EventType>(
				anEventType().withGroup(group).build(),
				anEventType().withGroup(group).build());
		List<EventTypeGroup> eventTypeGroupNames = new FluentArrayList<EventTypeGroup>(group);
		
		
		MultiEventGroupSorter sut = new MultiEventGroupSorter(eventTypes);
		
		assertThat(sut.getGroups(), Matchers.equalTo(eventTypeGroupNames));
	}
	
	
	@Test
	public void should_return_an_list_of_group_name_in_alphabeticable_order_when_there_are_multiple_groups() {
		EventTypeGroup zGroup = anEventTypeGroup().withName("Z group").build();
		EventTypeGroup gGroup = anEventTypeGroup().withName("G group").build();
		EventTypeGroup qGroup = anEventTypeGroup().withName("q group").build();
		EventTypeGroup aGroup = anEventTypeGroup().withName("a group").build();
		
		FluentHashSet<EventType> eventTypes = new FluentHashSet<EventType>(
				anEventType().withGroup(zGroup).build(),
				anEventType().withGroup(aGroup).build(),
				anEventType().withGroup(qGroup).build(),
				anEventType().withGroup(gGroup).build()
				);
		List<EventTypeGroup> eventTypeGroupNames = new FluentArrayList<EventTypeGroup>(aGroup, gGroup, qGroup, zGroup);
		
		MultiEventGroupSorter sut = new MultiEventGroupSorter(eventTypes);
		
		assertThat(sut.getGroups(), Matchers.equalTo(eventTypeGroupNames));
	}
	
	
	
	
	
	@Test
	public void should_return_a_list_of_event_types_in_alphabeticable_for_the_given_group() {
		EventTypeGroup group = anEventTypeGroup().withName("group 1").build();
		EventType typeB = anEventType().named("eventType b").withGroup(group).build();
		EventType typeA = anEventType().named("eventType a").withGroup(group).build();
		EventType typeZ = anEventType().named("eventType z").withGroup(group).build();
		
		FluentHashSet<EventType> eventTypes = new FluentHashSet<EventType>(typeB, typeA, typeZ);
		List<EventType> expectedList = ImmutableList.of(typeA, typeB, typeZ);
		
		MultiEventGroupSorter sut = new MultiEventGroupSorter(eventTypes);
		
		assertThat(sut.getEventTypesForGroup(group), Matchers.equalTo(expectedList));
	}
	
	@Test
	public void should_split_list_of_event_types_by_group() {
		EventTypeGroup group1 = anEventTypeGroup().withName("group 1").build();
		EventTypeGroup group2 = anEventTypeGroup().withName("group 2").build();
		
		EventType typeA = anEventType().named("eventType a").withGroup(group2).build();
		EventType typeB = anEventType().named("eventType b").withGroup(group1).build();
		EventType typeZ = anEventType().named("eventType z").withGroup(group1).build();
		
		FluentHashSet<EventType> eventTypes = new FluentHashSet<EventType>(typeB, typeA, typeZ);
		List<EventType> expectedGroup1List = ImmutableList.of(typeB, typeZ);
		List<EventType> expectedGroup2List = ImmutableList.of(typeA);
		
		MultiEventGroupSorter sut = new MultiEventGroupSorter(eventTypes);
		
		assertThat(sut.getEventTypesForGroup(group1), Matchers.equalTo(expectedGroup1List));
		assertThat(sut.getEventTypesForGroup(group2), Matchers.equalTo(expectedGroup2List));
	}
	
	
	
	
	
}
