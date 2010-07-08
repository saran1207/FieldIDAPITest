package com.n4systems.servicedto.converts;

import com.n4systems.model.location.Location;
import com.n4systems.webservice.dto.LocationServiceDTO;

public class LocationToLocationServiceDTOConverter {

	public LocationServiceDTO convert(Location location) {
		
		LocationServiceDTO locationServiceDTO = new LocationServiceDTO();
		
		locationServiceDTO.setFreeformLocation(location.getFreeformLocation());
		locationServiceDTO.setPredefinedLocationId(location.getPredefinedLocation().getId());
		
		return locationServiceDTO;
	}


}
