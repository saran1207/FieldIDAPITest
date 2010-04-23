package com.n4systems.ejb.impl;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;

public class InspectionScheduleBundle {

	
	public final Product product;
	public final InspectionType type;
	public final Date scheduledDate;
	
	public InspectionScheduleBundle(Product product, InspectionType type, Date scheduledDate) {
		super();
		this.product = product;
		this.type = type;
		this.scheduledDate = scheduledDate;
		
		guard();
	}

	private void guard() {
		if (product == null) {
			throw new IllegalArgumentException("product can not be null");
		}
		
		if (type == null) {
			throw new IllegalArgumentException("inspection type can not be null");
		}
		
		if (scheduledDate == null) {
			throw new IllegalArgumentException("schedule date can not be null");
		}
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	
	
	
}
