package com.n4systems.model.builders;

import java.util.Date;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;

public class InspectionScheduleBuilder extends BaseBuilder<InspectionSchedule> {

	private final Product product;
	private final InspectionType inspectionType;
	private final Date nextDate;
	private final Inspection inspection;
	
	public static InspectionScheduleBuilder aScheduledInspectionSchedule() {
		return new InspectionScheduleBuilder(null,null,new Date(), null);
	}
	
	public static InspectionScheduleBuilder aCompletedInspectionSchedule() {
		return new InspectionScheduleBuilder(null,null,new Date(), null);
	}
	
	public InspectionScheduleBuilder(Product product, InspectionType inspectionType, Date nextDate, Inspection inspection) {
		super();
		this.product = product;
		this.inspectionType = inspectionType;
		this.nextDate = nextDate;
		this.inspection = inspection;
	}
	
	public InspectionScheduleBuilder product(Product product) {
		return new InspectionScheduleBuilder(product, inspectionType, nextDate, inspection);
	}
	
	public InspectionScheduleBuilder inspectionType(InspectionType inspectionType) {
		return new InspectionScheduleBuilder(product, inspectionType, nextDate, inspection);
	}
	
	public InspectionScheduleBuilder nextDate(Date nextDate) {
		return new InspectionScheduleBuilder(product, inspectionType, nextDate, inspection);
	}
	
	public InspectionScheduleBuilder completedDoing(Inspection inspection) {
		return new InspectionScheduleBuilder(product, inspectionType, nextDate, inspection);
	}
	
	@Override
	public InspectionSchedule build() {
		InspectionSchedule inspectionSchedule = new InspectionSchedule(product, inspectionType);
		inspectionSchedule.setNextDate(nextDate);
		inspectionSchedule.setId(id);
		
		if (inspection != null) {
			inspectionSchedule.completed(inspection);
			try {
				injectField(inspection, "schedule", inspectionSchedule);
			} catch (Exception e) {
				throw new ProcessFailureException("couldn't inject schedule", e);
			}
		}
		return inspectionSchedule;
	}
	
}
