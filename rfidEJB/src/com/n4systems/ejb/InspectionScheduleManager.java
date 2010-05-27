package com.n4systems.ejb;

import java.util.List;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;

public interface InspectionScheduleManager {
	
	public List<InspectionSchedule> autoSchedule(Product product);
	
	public InspectionSchedule update(InspectionSchedule schedule);
	public void restoreScheduleForInspection(Inspection inspection);
	

	
	public void removeAllSchedulesFor(Product product);
	
	
	
	public List<InspectionSchedule> getAvailableSchedulesFor(Product product);
	
	public boolean schedulePastDue(Long scheduleId);
	public Long getProductIdForSchedule(Long scheduleId);
	public Long getInspectionTypeIdForSchedule(Long scheduleId);
	
	public Long getInspectionIdForSchedule(Long scheduleId);
}
