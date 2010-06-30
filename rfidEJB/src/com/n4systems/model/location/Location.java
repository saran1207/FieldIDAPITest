package com.n4systems.model.location;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class Location {
	@ManyToOne
	private PredefinedLocation predefinedLocation;
	
	public Location() {
		this(null);
	}
	
	public Location(PredefinedLocation predefinedLocation) {
		super();
		this.predefinedLocation = predefinedLocation;
	}


	public PredefinedLocation getPredefinedLocation() {
		return predefinedLocation;
	}
	
	
	
}
