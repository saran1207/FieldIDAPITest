package com.n4systems.ejb.parameters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.ejb.impl.CreateEventParameter;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.tools.FileDataContainer;

public class CreateEventParameterBuilder {

	private final Event event;
	private final long userId;
	private Date nextEventDate = null;
	private FileDataContainer proofTestData = null;
	private List<FileAttachment> uploadedImages = null;
	private ArrayList<EventScheduleBundle> schedules = new ArrayList<EventScheduleBundle>();
    private long scheduleId;

    public CreateEventParameterBuilder(Event event, long userId) {
		this.event = event;
		this.userId = userId;
	}

	public CreateEventParameter build() {
		return new CreateEventParameter(event, nextEventDate, userId, proofTestData, uploadedImages, schedules, scheduleId);
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

	public CreateEventParameterBuilder withProofTestFile(FileDataContainer proofTestData) {
		this.proofTestData = proofTestData;
		return this;
	}

	public CreateEventParameterBuilder withUploadedImages(List<FileAttachment> uploadedImages) {
		this.uploadedImages = uploadedImages;
		return this;
	}

    public CreateEventParameterBuilder withScheduleId(long scheduleId) {
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
