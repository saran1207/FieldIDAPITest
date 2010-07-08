package com.n4systems.servicedto.converts;

import com.n4systems.model.Product;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.dto.LocationServiceDTO;

public class LocationServiceDTOConverter {
	
	private FilteredIdLoader<PredefinedLocation> filteredPredefinedLocationIdLoader;
	
	public LocationServiceDTOConverter(FilteredIdLoader<PredefinedLocation> filteredPredefinedLocationIdLoader) {
		this.filteredPredefinedLocationIdLoader = filteredPredefinedLocationIdLoader;
	}
	
	public void convert(LocationServiceDTO locationServiceDTO, Product targetProduct) {
		
		Location location = null;
		PredefinedLocation predefinedLocation = null;
		if (locationServiceDTO.predefinedLocationIdExists()) {
			predefinedLocation = filteredPredefinedLocationIdLoader.setId(locationServiceDTO.getPredefinedLocationId()).load();
			location = new Location(predefinedLocation, locationServiceDTO.getFreeformLocation());
		} else {
			location = Location.onlyFreeformLocation(locationServiceDTO.getFreeformLocation());
		}
		
		targetProduct.setAdvancedLocation(location);
		
	}
	
}
