package com.n4systems.ejb.impl;

import java.util.Date;
import java.util.List;

import com.n4systems.model.Event;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.n4systems.model.FileAttachment;
import com.n4systems.tools.FileDataContainer;

public class CreateEventParameter {
	public final Event event;
	public final Date nextEventDate;
	public final Long userId;
	public final FileDataContainer fileData;
	public final List<FileAttachment> uploadedFiles;
	public final boolean calculateEventResult;
	
	public final List<EventScheduleBundle> schedules;


	public CreateEventParameter(Event event, Date nextEventDate, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles, boolean calculateEventResult, List<EventScheduleBundle> schedules) {
		this.event = event;
		this.nextEventDate = nextEventDate;
		this.userId = userId;
		this.fileData = fileData;
		this.uploadedFiles = uploadedFiles;
		this.calculateEventResult = calculateEventResult;
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