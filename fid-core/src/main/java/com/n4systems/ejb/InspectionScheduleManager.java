package com.n4systems.ejb;

import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;

public interface InspectionScheduleManager {
	
	public List<InspectionSchedule> autoSchedule(Asset asset);
	
	public InspectionSchedule update(InspectionSchedule schedule);
	public void restoreScheduleForInspection(Inspection inspection);
	

	
	public void removeAllSchedulesFor(Asset asset);
	
	
	
	public List<InspectionSchedule> getAvailableSchedulesFor(Asset asset);
	
	public boolean schedulePastDue(Long scheduleId);
	public Long getAssetIdForSchedule(Long scheduleId);
	public Long getInspectionTypeIdForSchedule(Long scheduleId);
	
	public Long getInspectionIdForSchedule(Long scheduleId);
}
