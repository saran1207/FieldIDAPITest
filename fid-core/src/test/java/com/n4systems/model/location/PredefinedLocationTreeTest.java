package com.n4systems.model.location;

import org.junit.Test;

import static com.n4systems.model.location.EmptyPredefinedLocationTreeMatcher.anEmptyLocationTree;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;


public class PredefinedLocationTreeTest {

	
	@Test
	public void should_report_as_an_empty_tree_when_there_were_no_locations_added() throws Exception {
		PredefinedLocationTree sut = new PredefinedLocationTree();
		assertThat(sut, anEmptyLocationTree());
	}
	
	@Test
	public void should_report_as_not_an_empty_tree_when_there_were_at_least_one_location_is_added() throws Exception {
		PredefinedLocationTree sut = new PredefinedLocationTree();
		PredefinedLocation pl = new PredefinedLocation();
		pl.setId(1L);
		sut.addNode(new PredefinedLocationTreeNode(pl));
		
		assertThat(sut, not(anEmptyLocationTree()));
	}
	
}
