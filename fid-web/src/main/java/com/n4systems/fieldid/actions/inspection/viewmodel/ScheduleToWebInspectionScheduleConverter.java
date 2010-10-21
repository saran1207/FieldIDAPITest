package com.n4systems.fieldid.actions.inspection.viewmodel;

import java.util.Date;

import com.n4systems.fieldid.actions.helpers.UserDateConverter;
import com.n4systems.fieldid.actions.inspection.WebInspectionSchedule;
import com.n4systems.model.AssetTypeSchedule;

public class ScheduleToWebInspectionScheduleConverter {
	
	
	private final UserDateConverter dateConverter;

	public ScheduleToWebInspectionScheduleConverter(UserDateConverter dateConverter) {
		this.dateConverter = dateConverter;
		
	}

	public WebInspectionSchedule convert(AssetTypeSchedule schedule, Date currentDatePerformed) {
		if (schedule == null) { return null; }
		
		
		WebInspectionSchedule webInspectionSchedule = new WebInspectionSchedule();
		
		webInspectionSchedule.setType(schedule.getInspectionType().getId());
		webInspectionSchedule.setTypeName(schedule.getInspectionType().getName());
		
		
		webInspectionSchedule.setDate(dateConverter.convertDate(schedule.getNextDate(currentDatePerformed)));
		
		return webInspectionSchedule;
	}

}
