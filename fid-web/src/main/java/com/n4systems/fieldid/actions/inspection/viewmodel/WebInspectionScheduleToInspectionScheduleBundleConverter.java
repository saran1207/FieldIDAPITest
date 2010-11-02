package com.n4systems.fieldid.actions.inspection.viewmodel;

import java.util.Date;

import com.n4systems.ejb.impl.InspectionScheduleBundle;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.fieldid.actions.inspection.WebInspectionSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;
import com.n4systems.persistence.loaders.LoaderFactory;

public class WebInspectionScheduleToInspectionScheduleBundleConverter {

	private final LoaderFactory loaderFactory;
	private final SessionUserDateConverter dateConverter;

	public WebInspectionScheduleToInspectionScheduleBundleConverter(LoaderFactory loaderFactory, SessionUserDateConverter createUserDateConverter) {
		this.loaderFactory = loaderFactory;
		this.dateConverter = createUserDateConverter;
	}

	public InspectionScheduleBundle convert(WebInspectionSchedule nextSchedule, Asset asset) {
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
		
		return new InspectionScheduleBundle(asset, scheduleType, scheduleJob, scheduleDate);
		
	}

}
