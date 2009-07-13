package com.n4systems.services;

import java.util.Date;
import java.util.List;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.util.DateHelper;

public class NextInspectionScheduleService {

	private final Product product;
	private final InspectionType inspectionType;
	private final Date nextDate;	
	private final InspectionScheduleManager inspectionScheduleManager;
	
	public NextInspectionScheduleService(Product product, InspectionType inspectionType, Date nextDate, InspectionScheduleManager inspectionScheduleManager) {
		this.product = product;
		this.inspectionType = inspectionType;
		this.nextDate = nextDate;
		this.inspectionScheduleManager = inspectionScheduleManager;
	}
	
	/**
	 * Creates the next inspection schedule for the product.  If there is already a schedule
	 * for the contained product and inspection type it will simply return that one.
	 * @return The newly created schedule, or the already existing one.
	 */
	public InspectionSchedule createNextSchedule() {
		
		InspectionSchedule inspectionSchedule = null;
		
		List<InspectionSchedule> upcomingSchedules = inspectionScheduleManager.getAvailableSchedulesFor(product);
		
		for (InspectionSchedule upcomingSchedule : upcomingSchedules) {
			if (DateHelper.isEqualIgnoringTime(upcomingSchedule.getNextDate(), nextDate) && upcomingSchedule.getInspectionType().equals(inspectionType)) {
				inspectionSchedule = upcomingSchedule;
				break;
			}
		}
		
		if (inspectionSchedule == null) {
			inspectionSchedule = new InspectionSchedule(product, inspectionType);
			inspectionSchedule.setNextDate(nextDate);		
			inspectionSchedule = inspectionScheduleManager.update(inspectionSchedule);
		}
		
		return inspectionSchedule;
	}
	
}
