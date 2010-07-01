package com.n4systems.model.location;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;


public class LocationTest {

	
	
	@Test
	public void should_create_a_new_location_with_the_same_predefined_location_and_the_new_freeform_location() throws Exception {
		Location originalLocation = new Location(new PredefinedLocation(), "inital freeform location");
		
		String alteredFreeformLocation = "new freefrom location";
		Location alteredLocation = originalLocation.createForAdjustedFreeformLocation(alteredFreeformLocation);
		
		assertThatNewLocationWasCreated(alteredLocation, originalLocation, alteredFreeformLocation, originalLocation.getPredefinedLocation());
	}


	private void assertThatNewLocationWasCreated(Location alteredLocation, Location originalLocation, String freeformLocation, PredefinedLocation predefinedLocation) {
		assertThat(alteredLocation, not(sameInstance(originalLocation)));
		assertThat(alteredLocation, hasProperty("predefinedLocation", is(predefinedLocation)));
		assertThat(alteredLocation, hasProperty("freeformLocation", equalTo(freeformLocation)));
	}
	
	
	@Test
	public void should_create_a_new_location_with_the_same_dynamic_location_and_the_given_predefined_location() throws Exception {
		Location originalLocation = new Location(new PredefinedLocation(), "inital freeform location");
		
		PredefinedLocation alteredPredefinedLocation = new PredefinedLocation();
		
		Location alteredLocation = originalLocation.createForAdjustedPredefinedLocation(alteredPredefinedLocation);
		
		
		
		assertThatNewLocationWasCreated(alteredLocation, originalLocation, originalLocation.getFreeformLocation(), alteredPredefinedLocation);

	}
}
