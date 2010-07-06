package com.n4systems.fieldid.viewhelpers;

import static com.n4systems.model.builders.PredefinedLocationBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.persistence.loaders.LoaderFactory;


public class ProductCrudHelperTest {

	
	private static final LoaderFactory UNUSED_LOADER_FACTORY = null;

	@Test
	public void should_give_an_emtpy_string_as_the_name_of_a_null_location() throws Exception {
		String generatedName = new ProductCrudHelper(UNUSED_LOADER_FACTORY).getFullNameOfLocation(null);
		assertThat(generatedName, equalTo(""));
	}
	
	
	@Test
	public void should_return_just_the_name_of_the_single_location_when_the_predefined_location_is_a_root_node() throws Exception {
		PredefinedLocation predefinedLocation = aRootPredefinedLocation().withName("root").build();
		
		String generatedName = new ProductCrudHelper(UNUSED_LOADER_FACTORY).getFullNameOfLocation(locationWithOnlyPredefined(predefinedLocation));
		assertThat(generatedName, equalTo("root"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_return_the_name_starting_with_parent_on_the_left() throws Exception {
		PredefinedLocation predefinedLocationParent = new PredefinedLocation();
		predefinedLocationParent.setName("root");
		
		PredefinedLocation predefinedLocationChild = aPredefinedLocation().withName("leaf").withParent(aRootPredefinedLocation().withName("root").build()).build();
		
		
		String generatedName = new ProductCrudHelper(UNUSED_LOADER_FACTORY).getFullNameOfLocation(locationWithOnlyPredefined(predefinedLocationChild));
		assertThat(generatedName, allOf(startsWith("root"), endsWith("leaf")));
	}
	
	
	
	
	@Test
	public void should_separate_names_by_a_space() throws Exception {
		PredefinedLocation predefinedLocationParent = new PredefinedLocation();
		predefinedLocationParent.setName("root");
		
		PredefinedLocation predefinedLocationChild = aPredefinedLocation().withName("leaf").withParent(aRootPredefinedLocation().withName("root").build()).build();
		
		
		String generatedName = new ProductCrudHelper(UNUSED_LOADER_FACTORY).getFullNameOfLocation(locationWithOnlyPredefined(predefinedLocationChild));
		assertThat(generatedName, equalTo("root leaf"));
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
		
		String generatedName = new ProductCrudHelper(UNUSED_LOADER_FACTORY).getFullNameOfLocation(new Location(predefinedLocationChild, freeformLocation));
		assertThat(generatedName, allOf(containsString("root"), containsString("leaf"), endsWith(" " +  freeformLocation)));
	}


	
}
