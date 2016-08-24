package com.n4systems.fieldid.utils;

import com.n4systems.test.helpers.FluentArrayList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class StrutsListHelperTest {

	
	@Test
	public void should_do_nothing_to_a_with_no_values_with_the_prefix() throws Exception {
		List<String> list = new FluentArrayList<String>("element_1", "element_2");
		
		StrutsListHelper.removeMarkedEntries("--delete--", list);
		
		assertEquals(2, list.size());
	}
	
	@Test
	public void should_remove_the_first_element_that_has_the_prefix() throws Exception {
		List<String> list = new FluentArrayList<String>("--delete--element_1", "element_2");
		
		StrutsListHelper.removeMarkedEntries("--delete--", list);
		
		assertEquals(1, list.size());
		assertEquals("element_2", list.get(0));
	}
	
	@Test
	public void should_remove_the_second_element_that_has_the_prefix() throws Exception {
		List<String> list = new FluentArrayList<String>("element_1", "--delete--element_2");
		
		StrutsListHelper.removeMarkedEntries("--delete--", list);
		
		assertEquals(1, list.size());
		assertEquals("element_1", list.get(0));
	}
	
	@Test
	public void should_remove_multiple_elements_that_has_the_prefix() throws Exception {
		List<String> list = new FluentArrayList<String>("--delete--element_1", "--delete--element_2");
		
		StrutsListHelper.removeMarkedEntries("--delete--", list);
		
		assertEquals(new ArrayList<String>(), list);
	}
	
	@Test
	public void should_ignore_null_values() throws Exception {
		List<String> list = new FluentArrayList<String>("element_1", null, "element_2");
		
		StrutsListHelper.removeMarkedEntries("--delete--", list);
		
		assertEquals(new FluentArrayList<String>("element_1", null, "element_2"), list);
	}
	
}
