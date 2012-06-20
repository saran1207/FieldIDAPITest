package com.n4systems.model.location;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.n4systems.model.security.AllowSafetyNetworkAccess;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

@Embeddable
public class Location implements Serializable {

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
		setFreeformLocation(freeformLocation);
	}
	
	@AllowSafetyNetworkAccess
	public PredefinedLocation getPredefinedLocation() {
		return predefinedLocation;
	}

    public void setPredefinedLocation(PredefinedLocation predefinedLocation) {
        this.predefinedLocation = predefinedLocation;
    }
	
	@AllowSafetyNetworkAccess
	public String getFreeformLocation() {
		return freeformLocation;
	}

	public void setFreeformLocation(String freeformLocation) {
		this.freeformLocation = (freeformLocation == null) ? "" : freeformLocation;
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
		return !freeformLocation.isEmpty();
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
	
	public String getFullName() {
		String fullName = "";
        if (hasPredefinedLocation()) {
            fullName += predefinedLocation.getFullName();

            if (hasFreeform())
                fullName += ": ";
        }

        fullName += freeformLocation;
        return fullName;
	}

    public boolean isBlank() {
        return !hasPredefinedLocation() && !hasFreeform();
    }

}
