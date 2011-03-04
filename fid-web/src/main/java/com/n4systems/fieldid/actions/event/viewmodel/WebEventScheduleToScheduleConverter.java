package com.n4systems.fieldid.actions.event.viewmodel;

import java.util.Date;

import com.n4systems.fieldid.actions.event.WebEventSchedule;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.Project;
import com.n4systems.persistence.loaders.LoaderFactory;

public class WebEventScheduleToScheduleConverter {

	private final LoaderFactory loaderFactory;
	private final SessionUserDateConverter dateConverter;

	public WebEventScheduleToScheduleConverter(LoaderFactory loaderFactory, SessionUserDateConverter dateConverter) {
		this.loaderFactory = loaderFactory;
		this.dateConverter = dateConverter;
	}
	
	public EventSchedule convert(WebEventSchedule webSchedule, Asset asset) {
		Date scheduledDate = dateConverter.convertDate(webSchedule.getDate());
		EventType eventType = loaderFactory.createFilteredIdLoader(EventType.class).setId(webSchedule.getType()).load();

		EventSchedule schedule = new EventSchedule(asset, eventType, scheduledDate);		
		
		if (webSchedule.getJob() != null) {
			Project scheduleJob = loaderFactory.createFilteredIdLoader(Project.class).setId(webSchedule.getJob()).load();
			schedule.setProject(scheduleJob);
		} 
		return schedule;
	}
}
