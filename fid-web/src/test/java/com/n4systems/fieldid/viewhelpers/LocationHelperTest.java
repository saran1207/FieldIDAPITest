package com.n4systems.fieldid.viewhelpers;

import static com.n4systems.model.builders.PredefinedLocationBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.n4systems.fieldid.viewhelpers.LocationHelper;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.persistence.Transactor;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HierarchicalNode;


public class LocationHelperTest {
	
	private static final LoaderFactory UNUSED_LOADER_FACTORY = null;
	private static final Transactor UNUSED_TRANSACTOR = null;
	private static final Location NULL_LOCATION = null;
	private static final Long ID_TO_FIND = new Long(1);
	private final Long SOME_ID = new Long(999999);
	private final Long ANOTHER_ID = new Long(55555);
	
	
	@Test
	public void should_give_an_emtpy_string_as_the_name_of_a_null_location() throws Exception {
		String generatedName = new LocationHelper(UNUSED_LOADER_FACTORY, UNUSED_TRANSACTOR).getFullNameOfLocation(NULL_LOCATION);
		assertThat(generatedName, equalTo(""));
	}
	
	
	@Test
	public void should_return_just_the_name_of_the_single_location_when_the_predefined_location_is_a_root_node() throws Exception {
		PredefinedLocation predefinedLocation = aRootPredefinedLocation().withName("root").build();
		
		String generatedName = new LocationHelper(UNUSED_LOADER_FACTORY, UNUSED_TRANSACTOR).getFullNameOfLocation(locationWithOnlyPredefined(predefinedLocation));
		assertThat(generatedName, equalTo("root"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_return_the_name_starting_with_parent_on_the_left() throws Exception {
		PredefinedLocation predefinedLocationParent = new PredefinedLocation();
		predefinedLocationParent.setName("root");
		
		PredefinedLocation predefinedLocationChild = aPredefinedLocation().withName("leaf").withParent(aRootPredefinedLocation().withName("root").build()).build();
		
		
		String generatedName = new LocationHelper(UNUSED_LOADER_FACTORY, UNUSED_TRANSACTOR).getFullNameOfLocation(locationWithOnlyPredefined(predefinedLocationChild));
		assertThat(generatedName, allOf(startsWith("root"), endsWith("leaf")));
	}
	
	
	
	
	@Test
	public void should_separate_names_by_a_greater_than_sign() throws Exception {
		PredefinedLocation predefinedLocationParent = new PredefinedLocation();
		predefinedLocationParent.setName("root");
		
		PredefinedLocation predefinedLocationChild = aPredefinedLocation().withName("leaf").withParent(aRootPredefinedLocation().withName("root").build()).build();
		
		
		String generatedName = new LocationHelper(UNUSED_LOADER_FACTORY, UNUSED_TRANSACTOR).getFullNameOfLocation(locationWithOnlyPredefined(predefinedLocationChild));
		assertThat(generatedName, equalTo("root > leaf"));
	}


	private Location locationWithOnlyPredefined(PredefinedLocation predefinedLocationChild) {
		return new Location(predefinedLocationChild, "");
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_put_the_free_from_field_in_the_name_as_the_last_element() throws Exception {
		PredefinedLocation predefinedLocationParent = new PredefinedLocation();
		predefinedLocationParent.setName("root");
		
		PredefinedLocation predefinedLocationChild = aPredefinedLocation().withName("leaf").withParent(aRootPredefinedLocation().withName("root").build()).build();
		
		
		String freeformLocation = "freeform";
		
		String generatedName = new LocationHelper(UNUSED_LOADER_FACTORY, UNUSED_TRANSACTOR).getFullNameOfLocation(new Location(predefinedLocationChild, freeformLocation));
		assertThat(generatedName, allOf(containsString("root"), containsString("leaf"), endsWith(" " +  freeformLocation)));
	}

	@Test
	public void should_have_name_that_is_only_the_freeform_location_when_there_is_no_predefined_location() throws Exception {
		String freeformLocation = "freeform";
		
		String generatedName = new LocationHelper(UNUSED_LOADER_FACTORY, UNUSED_TRANSACTOR).getFullNameOfLocation(Location.onlyFreeformLocation(freeformLocation));
		assertThat(generatedName,  equalTo(freeformLocation));
	}
	
	@Test
	public void should_find_node_by_id(){
		LocationHelper helper = new LocationHelper(null, null);
	
		HierarchicalNode nodeToFind = new HierarchicalNode();
		HierarchicalNode parent = new HierarchicalNode();
		HierarchicalNode parentOfParent = new HierarchicalNode();
		HierarchicalNode someNode = new HierarchicalNode();
		List<HierarchicalNode> nodes = new ArrayList<HierarchicalNode>();
		
		nodeToFind.setId(ID_TO_FIND);
		
		someNode.setId(SOME_ID);
		parent.setId(SOME_ID);
		parentOfParent.setId(SOME_ID);
		
		parent.addChild(nodeToFind);
		parentOfParent.addChild(someNode);
		parentOfParent.addChild(someNode);
		parentOfParent.addChild(someNode);
		parentOfParent.addChild(parent);
		nodes.add(parentOfParent);
		
		assertThat(helper.findNodeById(ID_TO_FIND, nodes).getId(), equalTo(ID_TO_FIND));
	}
	
	@Test
	public void should_return_null_if_node_not_found(){
		LocationHelper helper = new LocationHelper(null, null);
	
		HierarchicalNode nodeToFind = new HierarchicalNode();
		HierarchicalNode parent = new HierarchicalNode();
		HierarchicalNode parentOfParent = new HierarchicalNode();
		HierarchicalNode someNode = new HierarchicalNode();
		List<HierarchicalNode> nodes = new ArrayList<HierarchicalNode>();
		
		nodeToFind.setId(ID_TO_FIND);
		
		someNode.setId(SOME_ID);
		parent.setId(SOME_ID);
		parentOfParent.setId(SOME_ID);
		
		parent.addChild(nodeToFind);
		parentOfParent.addChild(someNode);
		parentOfParent.addChild(someNode);
		parentOfParent.addChild(someNode);
		parentOfParent.addChild(parent);
		nodes.add(parentOfParent);
		
		assertThat(helper.findNodeById(new Long(ANOTHER_ID), nodes), equalTo(null));
	}
	
}
