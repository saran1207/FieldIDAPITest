package com.n4systems.fieldid.actions.inspection.viewmodel;

import java.util.Date;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.fieldid.actions.inspection.WebEventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;
import com.n4systems.persistence.loaders.LoaderFactory;

public class WebEventScheduleToEventScheduleBundleConverter {

	private final LoaderFactory loaderFactory;
	private final SessionUserDateConverter dateConverter;

	public WebEventScheduleToEventScheduleBundleConverter(LoaderFactory loaderFactory, SessionUserDateConverter createUserDateConverter) {
		this.loaderFactory = loaderFactory;
		this.dateConverter = createUserDateConverter;
	}

	public EventScheduleBundle convert(WebEventSchedule nextSchedule, Asset asset) {
		Date scheduleDate;
		EventType scheduleType;
		Project scheduleJob;
		
		scheduleDate = dateConverter.convertDate(nextSchedule.getDate());
		scheduleType = loaderFactory.createFilteredIdLoader(EventType.class).setId(nextSchedule.getType()).load();
		
		if (nextSchedule.getJob() != null) {
			scheduleJob = loaderFactory.createFilteredIdLoader(Project.class).setId(nextSchedule.getJob()).load();
		} else {
			scheduleJob = null;
		}
		
		return new EventScheduleBundle(asset, scheduleType, scheduleJob, scheduleDate);
		
	}

}
