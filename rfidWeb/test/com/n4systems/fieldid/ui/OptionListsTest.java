package com.n4systems.fieldid.ui;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.model.api.Listable;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.util.persistence.SimpleListable;

public class OptionListsTest {

	
	@Test
	public void should_not_add_element_to_list_when_it_is_already_included() throws Exception {
		List<Listable<Long>> existingList = ImmutableList.of(createListable(1L));
		Listable<Long> elementToEnsureIsInList = createListable(1L);
		
		OptionLists.includeInList(existingList, elementToEnsureIsInList);
		
		assertThat(existingList, equalTo((List<Listable<Long>>)ImmutableList.of(createListable(1L))));
	}
	
	@Test
	public void should_add_element_to_list_when_it_is_included() throws Exception {
		List<Listable<Long>> existingList = new FluentArrayList<Listable<Long>>();
		Listable<Long> elementToEnsureIsInList = createListable(1L);
		
		OptionLists.includeInList(existingList, elementToEnsureIsInList);
		
		assertThat(existingList, equalTo((List<Listable<Long>>)ImmutableList.of(createListable(1L))));
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_place_new_element_on_the_end_of_the_list() throws Exception {
		List<Listable<Long>> existingList = new FluentArrayList<Listable<Long>>(createListable(2L), createListable(3L));
		Listable<Long> elementToEnsureIsInList = createListable(1L);
		
		OptionLists.includeInList(existingList, elementToEnsureIsInList);
		
		assertThat(existingList, equalTo((List<Listable<Long>>)ImmutableList.of(createListable(2L), createListable(3L), createListable(1L))));
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_not_add_the_element_when_it_is_null() throws Exception {
		List<Listable<Long>> existingList = new FluentArrayList<Listable<Long>>(createListable(2L), createListable(3L));
		Listable<Long> elementToEnsureIsInList = null;
		
		OptionLists.includeInList(existingList, elementToEnsureIsInList);
		
		assertThat(existingList, equalTo((List<Listable<Long>>)ImmutableList.of(createListable(2L), createListable(3L))));
	}
	

	@Test(expected=RuntimeException.class)
	public void should_throw_exception_when_a_null_list_is_given() throws Exception {
		List<Listable<Long>> existingList = null;
		Listable<Long> elementToEnsureIsInList = createListable(1L);
		
		OptionLists.includeInList(existingList, elementToEnsureIsInList);
		
	}
	
	private Listable<Long> createListable(Long id) {
		return (Listable<Long>)new SimpleListable<Long>(id, "name");
	}
}
