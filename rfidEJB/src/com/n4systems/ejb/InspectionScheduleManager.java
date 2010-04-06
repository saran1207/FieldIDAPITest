package com.n4systems.ejb;

import java.util.Date;
import java.util.List;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.inspectionschedulecount.InspectionScheduleCount;
import com.n4systems.model.security.SecurityFilter;

public interface InspectionScheduleManager {
	
	public List<InspectionSchedule> autoSchedule(Product product);
	
	public InspectionSchedule update(InspectionSchedule schedule);
	public void restoreScheduleForInspection(Inspection inspection);
	
	public ProductTypeSchedule update(ProductTypeSchedule schedule);
	
	public void remove(ProductTypeSchedule schedule);
	
	public void removeAllSchedulesFor(Product product);
	public void removeAllSchedulesFor(ProductType productType, InspectionType inspectionType);
	
	public List<InspectionScheduleCount> getInspectionScheduleCount(Date fromDate, Date toDate, Long tenantId);
	public List<InspectionScheduleCount> getInspectionScheduleCount(Date fromDate, Date toDate, SecurityFilter secFilter);
	
	public List<InspectionSchedule> getAvailableSchedulesFor(Product product);
	
	public boolean schedulePastDue(Long scheduleId);
	public Long getProductIdForSchedule(Long scheduleId);
	public Long getInspectionTypeIdForSchedule(Long scheduleId);
	
	public Long getInspectionIdForSchedule(Long scheduleId);
	public List<InspectionSchedule> getSchedulesInTimeFrame(Product product, InspectionType inspectionType, Date inspectionDate);
}
