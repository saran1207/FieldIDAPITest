package com.n4systems.model.location;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.ToStringBuilder;

@Embeddable
public class Location {

	private static final PredefinedLocation noPredefinedLocation = null;

	public static Location onlyFreeformLocation(String freeformLocation) {
		return new Location(noPredefinedLocation, freeformLocation);
	}

	@ManyToOne
	private PredefinedLocation predefinedLocation;

	@Column(name = "location")
	private String freeformLocation;

	public Location() {
		
		this(null, null);
	}

	public Location(PredefinedLocation predefinedLocation, String freeformLocation) {
		super();
		this.predefinedLocation = predefinedLocation;
		this.freeformLocation = freeformLocation;
	}

	public PredefinedLocation getPredefinedLocation() {
		return predefinedLocation;
	}

	public String getFreeformLocation() {
		return freeformLocation;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Location createForAdjustedFreeformLocation(String freeformLocation) {
		return new Location(predefinedLocation, freeformLocation);
	}

	public Location createForAdjustedPredefinedLocation(
			PredefinedLocation predefinedLocation) {
		return new Location(predefinedLocation, freeformLocation);
	}

}
