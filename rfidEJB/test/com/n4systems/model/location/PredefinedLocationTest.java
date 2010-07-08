package com.n4systems.model.location;

import static com.n4systems.model.builders.PredefinedLocationBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;


public class PredefinedLocationTest {

	
	@Test
	public void should_find_that_location_with_no_parent_is_at_level_1_of_the_tree() throws Exception {
		PredefinedLocation location = aRootPredefinedLocation().build();
		
		assertThat(location.levelNumber(), equalTo(1));
	}
	
	@Test
	public void should_find_that_location_with_one_parent_is_at_level_2_of_the_tree() throws Exception {
		PredefinedLocation location = aPredefinedLocation().withParent(aRootPredefinedLocation().build()).build();
		
		assertThat(location.levelNumber(), equalTo(2));
	}
	
	@Test
	public void should_find_that_location_with_2_parent_is_at_level_3_of_the_tree() throws Exception {
		PredefinedLocation level2Location = aPredefinedLocation().withParent(aRootPredefinedLocation().build()).build();
		PredefinedLocation location =  aPredefinedLocation().withParent(level2Location).build();
		
		assertThat(location.levelNumber(), equalTo(3));
	}
	
}
