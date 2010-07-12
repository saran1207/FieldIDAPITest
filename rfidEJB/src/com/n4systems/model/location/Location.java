package com.n4systems.model.location;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

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
		this.freeformLocation = freeformLocation == null ? "" : freeformLocation;
	}
	
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=true)
	public PredefinedLocation getPredefinedLocation() {
		return predefinedLocation;
	}
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=true)
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

	public Location createForAdjustedPredefinedLocation(PredefinedLocation predefinedLocation) {
		return new Location(predefinedLocation, freeformLocation);
	}

	public boolean hasFreeform() {
		return freeformLocation != null && !freeformLocation.isEmpty();
	}

	public boolean hasPredefinedLocation() {
		return predefinedLocation != null;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	


}
