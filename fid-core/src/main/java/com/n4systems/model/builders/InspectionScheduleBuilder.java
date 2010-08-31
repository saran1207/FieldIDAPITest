package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionBuilder.anInspection;
import static com.n4systems.model.builders.InspectionTypeBuilder.anInspectionType;
import static com.n4systems.model.builders.ProductBuilder.aProduct;

import java.util.Date;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.Project;

public class InspectionScheduleBuilder extends BaseBuilder<InspectionSchedule> {

	private final Product product;
	private final InspectionType inspectionType;
	private final Date nextDate;
	private final Inspection inspection;
	private final Project job;
	
	public static InspectionScheduleBuilder aScheduledInspectionSchedule() {
		return new InspectionScheduleBuilder(aProduct().build(),anInspectionType().build(),new Date(), null, null);
	}
	
	public static InspectionScheduleBuilder aCompletedInspectionSchedule() {
		InspectionType inspectionType = anInspectionType().build();
		Product product = aProduct().build();
		Inspection inspection = anInspection().ofType(inspectionType).on(product).build();
		return new InspectionScheduleBuilder(product,inspectionType,new Date(), inspection, null);
	}


	

	public InspectionScheduleBuilder(Product product, InspectionType inspectionType, Date nextDate, Inspection inspection, Project job) {
		super();
		this.product = product;
		this.inspectionType = inspectionType;
		this.nextDate = nextDate;
		this.inspection = inspection;
		this.job = job;
	}
	
	public InspectionScheduleBuilder product(Product product) {
		return new InspectionScheduleBuilder(product, inspectionType, nextDate, inspection, job);
	}
	
	public InspectionScheduleBuilder inspectionType(InspectionType inspectionType) {
		return new InspectionScheduleBuilder(product, inspectionType, nextDate, inspection, job);
	}
	
	public InspectionScheduleBuilder nextDate(Date nextDate) {
		return new InspectionScheduleBuilder(product, inspectionType, nextDate, inspection, job);
	}
	
	public InspectionScheduleBuilder completedDoing(Inspection inspection) {
		return new InspectionScheduleBuilder(product, inspectionType, nextDate, inspection, job);
	}
	
	public InspectionScheduleBuilder forJob(Project job) {
		return new InspectionScheduleBuilder(product, inspectionType, nextDate, inspection, job);
	}
	
	@Override
	public InspectionSchedule build() {
		InspectionSchedule inspectionSchedule = new InspectionSchedule(product, inspectionType);
		inspectionSchedule.setNextDate(nextDate);
		inspectionSchedule.setId(id);
		inspectionSchedule.setProject(job);
		
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
