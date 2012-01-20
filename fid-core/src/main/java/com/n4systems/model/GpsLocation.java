package com.n4systems.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class GpsLocation implements Serializable{

	private BigDecimal latitude = null;    
	private BigDecimal longitude = null;
	
	public GpsLocation(){}
	
	public GpsLocation(Number latitude, Number longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	// CAVEAT : be careful about changing this...javascript code makes assumptions.
	@Override
	public String toString() {
		return toPointString();
	}
	
    public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	
	public void setLatitude(Number latitude) { 
		setLatitude(new BigDecimal(latitude.floatValue()));
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public void setLongitude(Number longitude) { 
		setLongitude(new BigDecimal(longitude.floatValue()));
	}
	
	public boolean isValid() {
		return latitude != null && 
			   longitude != null &&
			   latitude.floatValue() != 0 &&
			   longitude.floatValue() != 0;
	}

	public String toPointString() {
		return latitude + "," + longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GpsLocation other = (GpsLocation) obj;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		return true;
	}


}
