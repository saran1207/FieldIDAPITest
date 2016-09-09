package com.n4systems.fieldid.viewhelpers.navigation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;



public class NavOptionsControllerTest {
	
	
	@Test
	public void should_beable_to_be_created_with_list_set_as_current_action() {
		NavOptionsController sut = new NavOptionsController(null, null, "test_options", "list");
		assertEquals("list", sut.getCurrentAction());
		assertEquals("list", sut.getCurrentActionOption().getName());
	}
	
	@Test
	public void should_beable_to_be_created_with_missing_file() {
		NavOptionsController sut = new NavOptionsController(null, null, "some_file", "list");
		assertEquals("list", sut.getCurrentAction());
		assertEquals(0, sut.getFilteredOptions().size());
	}
	
	
	
	//TODO make this readable.
	@Test
	public void should_have_list_of_nav_options() {
		List<NavOption> options = new ArrayList<NavOption>();
		
		options.add(new NavOption("some_label", "show", null, 1, null, null, null, null, null, false, ""));
		options.add(new NavOption("some_label", "list", null, 1, null, null, null, null, null, false, ""));
		options.add(new NavOption("some_label", "add", null, 1, null, null, null, null, null, false, ""));
		
		NavOptionsController sut = new NavOptionsController(null, null, "test_options", "list");
		
		assertEquals(options, sut.getOptions());
	}
	
	
}
