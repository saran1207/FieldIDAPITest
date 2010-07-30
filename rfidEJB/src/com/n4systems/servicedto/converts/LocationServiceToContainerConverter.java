package com.n4systems.servicedto.converts;

import com.n4systems.model.location.Location;
import com.n4systems.model.location.LocationContainer;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.dto.LocationServiceDTO;

public class LocationServiceToContainerConverter implements LocationConverter {
	private final LoaderFactory loaderFactory;
	
	public LocationServiceToContainerConverter(LoaderFactory loaderFactory) {
		this.loaderFactory = loaderFactory;
	}
	
	@Override
	public void convert(LocationServiceDTO locationDTO, LocationContainer container) {
		Location location;
		if (locationDTO.getPredefinedLocationId() != null) {
			PredefinedLocation predefinedLocation = loadPredefinedLocation(locationDTO.getPredefinedLocationId());
			location = new Location(predefinedLocation, locationDTO.getLocation());
		} else {
			location = Location.onlyFreeformLocation(locationDTO.getLocation());
		}
		container.setAdvancedLocation(location);
	}
	
	protected PredefinedLocation loadPredefinedLocation(Long id) { 
		return loaderFactory.createPredefinedLocationByIdLoader().setId(id).setShowArchived(true).load();
	}
	
}
