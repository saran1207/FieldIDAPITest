package com.n4systems.model.location;

import static com.n4systems.model.location.EmptyPredefinedLocationTreeMatcher.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;


public class PredefinedLocationTreeTest {

	
	@Test
	public void should_report_as_an_empty_tree_when_there_were_no_locations_added() throws Exception {
		PredefinedLocationTree sut = new PredefinedLocationTree();
		assertThat(sut, anEmptyLocationTree());
	}
	
	@Test
	public void should_report_as_not_an_empty_tree_when_there_were_at_least_one_location_is_added() throws Exception {
		PredefinedLocationTree sut = new PredefinedLocationTree();
		sut.addNode(new PredefinedLocationTreeNode(new PredefinedLocation()));
		
		assertThat(sut, not(anEmptyLocationTree()));
	}
	
}
