package com.n4systems.ejb.impl;

import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ThingEvent;
import com.n4systems.tools.FileDataContainer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;

public class CreateEventParameter {
	public ThingEvent event;
	public final Date nextEventDate;
	public final Long userId;
	public final FileDataContainer fileData;
	public final List<FileAttachment> uploadedFiles;

	public final List<EventScheduleBundle> schedules;


	public CreateEventParameter(ThingEvent event, Date nextEventDate, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles, List<EventScheduleBundle> schedules) {
		this.event = event;
		this.nextEventDate = nextEventDate;
		this.userId = userId;
		this.fileData = fileData;
		this.uploadedFiles = uploadedFiles;
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