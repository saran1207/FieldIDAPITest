package com.n4systems.fieldid.viewhelpers;

import static com.n4systems.model.builders.PredefinedLocationBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.persistence.Transactor;
import com.n4systems.persistence.loaders.LoaderFactory;


public class LocationHelperTest {
	
	private static final LoaderFactory UNUSED_LOADER_FACTORY = null;
	private static final Transactor UNUSED_TRANSACTOR = null;
	private static final Location NULL_LOCATION = null;

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
	public void should_separate_names_by_a_space() throws Exception {
		PredefinedLocation predefinedLocationParent = new PredefinedLocation();
		predefinedLocationParent.setName("root");
		
		PredefinedLocation predefinedLocationChild = aPredefinedLocation().withName("leaf").withParent(aRootPredefinedLocation().withName("root").build()).build();
		
		
		String generatedName = new LocationHelper(UNUSED_LOADER_FACTORY, UNUSED_TRANSACTOR).getFullNameOfLocation(locationWithOnlyPredefined(predefinedLocationChild));
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
		
		String generatedName = new LocationHelper(UNUSED_LOADER_FACTORY, UNUSED_TRANSACTOR).getFullNameOfLocation(new Location(predefinedLocationChild, freeformLocation));
		assertThat(generatedName, allOf(containsString("root"), containsString("leaf"), endsWith(" " +  freeformLocation)));
	}

	@Test
	public void should_have_name_that_is_only_the_freeform_location_when_there_is_no_predefined_location() throws Exception {
		String freeformLocation = "freeform";
		
		String generatedName = new LocationHelper(UNUSED_LOADER_FACTORY, UNUSED_TRANSACTOR).getFullNameOfLocation(Location.onlyFreeformLocation(freeformLocation));
		assertThat(generatedName,  equalTo(freeformLocation));
	}
	
}
