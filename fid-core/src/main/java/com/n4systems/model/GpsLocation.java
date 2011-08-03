package com.n4systems.model;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class GpsLocation {

	private BigDecimal latitude;    
	private BigDecimal longitude;

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

	
}
