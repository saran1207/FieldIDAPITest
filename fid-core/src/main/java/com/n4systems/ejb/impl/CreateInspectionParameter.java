package com.n4systems.ejb.impl;

import java.util.Date;
import java.util.List;

import com.n4systems.model.Event;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.n4systems.model.FileAttachment;
import com.n4systems.tools.FileDataContainer;

public class CreateInspectionParameter {
	public final Event event;
	public final Date nextInspectionDate;
	public final Long userId;
	public final FileDataContainer fileData;
	public final List<FileAttachment> uploadedFiles;
	public final boolean calculateInspectionResult;
	
	public final List<InspectionScheduleBundle> schedules;


	public CreateInspectionParameter(Event event, Date nextInspectionDate, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles, boolean calculateInspectionResult, List<InspectionScheduleBundle> schedules) {
		this.event = event;
		this.nextInspectionDate = nextInspectionDate;
		this.userId = userId;
		this.fileData = fileData;
		this.uploadedFiles = uploadedFiles;
		this.calculateInspectionResult = calculateInspectionResult;
		this.schedules = schedules;
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