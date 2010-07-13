package com.n4systems.servicedto.converts;

import com.n4systems.model.Product;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.dto.LocationServiceDTO;

public class ProductLocationConverter implements LocationConverter {
	private final LoaderFactory loaderFactory;
	
	public ProductLocationConverter(LoaderFactory loaderFactory) {
		this.loaderFactory = loaderFactory;
	}
	
	@Override
	public void convert(LocationServiceDTO locationDTO, Product product) {
		Location location;
		if (locationDTO.getPredefinedLocationId() != null) {
			PredefinedLocation predefinedLocation = loadPredefinedLocation(locationDTO.getPredefinedLocationId());
			location = new Location(predefinedLocation, locationDTO.getLocation());
		} else {
			location = Location.onlyFreeformLocation(locationDTO.getLocation());
		}
		product.setAdvancedLocation(location);
	}
	
	protected PredefinedLocation loadPredefinedLocation(Long id) { 
		return loaderFactory.createFilteredIdLoader(PredefinedLocation.class).setId(id).load();
	}
}
