package com.n4systems.ejb.parameters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.ejb.impl.CreateEventParameter;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ThingEvent;
import com.n4systems.tools.FileDataContainer;

public class CreateEventParameterBuilder {

	private final ThingEvent event;
	private final long userId;
	private Date nextEventDate = null;
	private FileDataContainer proofTestDataContainer = null;
	private List<FileAttachment> uploadedFiles = null;
	private ArrayList<EventScheduleBundle<Asset>> schedules = new ArrayList<EventScheduleBundle<Asset>>();
    private Long scheduleId;

    public CreateEventParameterBuilder(ThingEvent event, long userId) {
		this.event = event;
		this.userId = userId;
	}

	public CreateEventParameter build() {
		return new CreateEventParameter(event, nextEventDate, userId, proofTestDataContainer, uploadedFiles, schedules);
	}
	
	public CreateEventParameterBuilder withANextEventDate(Date nextEventDate) {
		this.nextEventDate = nextEventDate;
		
		if (nextEventDate != null) {
			addSchedule(new EventScheduleBundle(event.getAsset(), event.getType(), null, nextEventDate));
		}
		return this;
	}

	public CreateEventParameterBuilder addSchedule(EventScheduleBundle eventSchedule) {
		schedules.add(eventSchedule);
		return this;
	}

	public CreateEventParameterBuilder withProofTestFile(FileDataContainer proofTestDataContainer) {
		this.proofTestDataContainer = proofTestDataContainer;
		return this;
	}

	public CreateEventParameterBuilder withUploadedFiles(List<FileAttachment> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
		return this;
	}

    public CreateEventParameterBuilder withScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
		return this;
	}

	public CreateEventParameterBuilder addSchedules(List<EventScheduleBundle> schedules) {
		for (EventScheduleBundle eventScheduleBundle : schedules) {
			addSchedule(eventScheduleBundle);
		}
		return this;
	}

}
