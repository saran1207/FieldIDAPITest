package com.n4systems.fieldid.actions.asset;

import com.n4systems.fieldid.actions.api.LoaderFactoryProvider;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;

import java.io.Serializable;

public class LocationWebModel implements Serializable {

	private String freeformLocation;
	private PredefinedLocation predefinedLocation;

	private final LoaderFactoryProvider factoryProvider;

	public LocationWebModel(LoaderFactoryProvider factoryProvider) {
		this.factoryProvider = factoryProvider;
	}
	
	
	public LocationWebModel matchLocation(Location location) {
		this.predefinedLocation = location.getPredefinedLocation();
		this.freeformLocation = location.getFreeformLocation();
		return this;
	}

	public Long getPredefinedLocationId() {
		return predefinedLocation != null ? predefinedLocation.getId() : null;
	}

	public void setPredefinedLocationId(Long predefinedLocationId) {
		if (predefinedLocationId == null) {
			predefinedLocation = null;
		} else if (!predefinedLocationId.equals(getPredefinedLocationId())) {
			predefinedLocation =  factoryProvider.getLoaderFactory().createOwnerAndDownWithPrimaryFilteredIdLoader(PredefinedLocation.class).setId(predefinedLocationId).load();
		}
		
	}

    public void setPredefinedLocation(PredefinedLocation location) {
        this.predefinedLocation = location;
    }

	public String getFreeformLocation() {
		return freeformLocation;
	}

	public void setFreeformLocation(String freeformLocation) {
		this.freeformLocation = freeformLocation;
	}

	public PredefinedLocation getPredefinedLocation() {
		return predefinedLocation;
	}
	
	
	
	public Location createLocation() {
		return new Location(predefinedLocation, freeformLocation);
	}
	
	
}
