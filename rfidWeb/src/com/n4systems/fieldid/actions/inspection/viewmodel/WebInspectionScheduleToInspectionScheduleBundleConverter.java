package com.n4systems.fieldid.actions.inspection.viewmodel;

import java.util.Date;

import com.n4systems.ejb.impl.InspectionScheduleBundle;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.fieldid.actions.inspection.WebInspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.Project;
import com.n4systems.persistence.loaders.LoaderFactory;

public class WebInspectionScheduleToInspectionScheduleBundleConverter {

	private final LoaderFactory loaderFactory;
	private final SessionUserDateConverter dateConverter;

	public WebInspectionScheduleToInspectionScheduleBundleConverter(LoaderFactory loaderFactory, SessionUserDateConverter createUserDateConverter) {
		this.loaderFactory = loaderFactory;
		this.dateConverter = createUserDateConverter;
	}

	public InspectionScheduleBundle convert(WebInspectionSchedule nextSchedule, Product product) {
		Date scheduleDate;
		InspectionType scheduleType;
		Project scheduleJob;
		
		scheduleDate = dateConverter.convertDate(nextSchedule.getDate());
		scheduleType = loaderFactory.createFilteredIdLoader(InspectionType.class).setId(nextSchedule.getType()).load();
		
		if (nextSchedule.getJob() != null) {
			scheduleJob = loaderFactory.createFilteredIdLoader(Project.class).setId(nextSchedule.getJob()).load();
		} else {
			scheduleJob = null;
		}
		
		return new InspectionScheduleBundle(product, scheduleType, scheduleJob, scheduleDate);
		
	}

}
