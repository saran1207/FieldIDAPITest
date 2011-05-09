package com.n4systems.fieldid.viewhelpers.navigation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.viewhelpers.navigation.NavOption;
import com.n4systems.fieldid.viewhelpers.navigation.NavOptionsController;



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
		
		options.add(new NavOption("some_label", "show", null, 1, null, null, null, null, null, false, false));
		options.add(new NavOption("some_label", "list", null, 1, null, null, null, null, null, false, false));
		options.add(new NavOption("some_label", "add", null, 1, null, null, null, null, null, false, false));
		
		NavOptionsController sut = new NavOptionsController(null, null, "test_options", "list");
		
		assertEquals(options, sut.getOptions());
	}
	
	
}
