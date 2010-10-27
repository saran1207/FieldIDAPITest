package com.n4systems.ejb.impl;

import java.util.Date;

import com.n4systems.model.Asset;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.n4systems.model.InspectionType;
import com.n4systems.model.Project;

public class InspectionScheduleBundle {	
	private final Asset asset;
	private final InspectionType type;
	private final Project job;
	private final Date scheduledDate;
	
	public InspectionScheduleBundle(Asset asset, InspectionType type, Project job, Date scheduledDate) {
		this.asset = asset;
		this.type = type;
		this.job = job;
		this.scheduledDate = scheduledDate;
		
		guard();
	}

	private void guard() {
		if (asset == null) {
			throw new NullPointerException("asset");
		}
		
		if (type == null) {
			throw new NullPointerException("type");
		}
		
		if (scheduledDate == null) {
			throw new NullPointerException("scheduleDate");
		}
	}
	
	public Asset getAsset() {
		return asset;
	}

	public InspectionType getType() {
		return type;
	}

	public Project getJob() {
		return job;
	}

	public Date getScheduledDate() {
		return scheduledDate;
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
