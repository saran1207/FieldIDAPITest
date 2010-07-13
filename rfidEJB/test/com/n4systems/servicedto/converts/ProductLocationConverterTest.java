package com.n4systems.servicedto.converts;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.n4systems.model.Product;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.webservice.dto.LocationServiceDTO;

public class ProductLocationConverterTest {

	@Test
	public void creates_freeform_only_when_predefined_location_is_null() {
		ProductLocationConverter converter = new ProductLocationConverter(null);
		
		final String freeForm = "free_form"; 
		
		LocationServiceDTO location = new LocationServiceDTO() {
			@Override
			public Long getPredefinedLocationId() { return null; }
			
			@Override
			public String getLocation() { return freeForm; }
		};
		
		Product product = new Product();
		converter.convert(location, product);
		
		assertEquals(freeForm, product.getAdvancedLocation().getFreeformLocation());
		assertNull(product.getAdvancedLocation().getPredefinedLocation());
	}
	
	@Test
	public void loads_and_populates_full_location_when_predefined_is_set() {
		final PredefinedLocation predefinedLocation = new PredefinedLocation();
		predefinedLocation.setId(10L);
		predefinedLocation.setName("asdas");
		
		final String freeForm = "free_form";
		
		ProductLocationConverter converter = new ProductLocationConverter(null) {
			protected PredefinedLocation loadPredefinedLocation(Long id) { 
				return predefinedLocation;
			}
		};
		
		LocationServiceDTO location = new LocationServiceDTO() {
			@Override
			public Long getPredefinedLocationId() { return 10L; }
			
			@Override
			public String getLocation() { return freeForm; }
		};
		
		Product product = new Product();
		converter.convert(location, product);
		
		assertEquals(freeForm, product.getAdvancedLocation().getFreeformLocation());
		assertEquals(predefinedLocation, product.getAdvancedLocation().getPredefinedLocation());
	}
}
