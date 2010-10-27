package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionBuilder.anInspection;
import static com.n4systems.model.builders.InspectionTypeBuilder.anInspectionType;
import static com.n4systems.model.builders.AssetBuilder.anAsset;

import java.util.Date;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;

public class InspectionScheduleBuilder extends BaseBuilder<InspectionSchedule> {

	private final Asset asset;
	private final InspectionType inspectionType;
	private final Date nextDate;
	private final Inspection inspection;
	private final Project job;
	
	public static InspectionScheduleBuilder aScheduledInspectionSchedule() {
		return new InspectionScheduleBuilder(anAsset().build(),anInspectionType().build(),new Date(), null, null);
	}
	
	public static InspectionScheduleBuilder aCompletedInspectionSchedule() {
		InspectionType inspectionType = anInspectionType().build();
		Asset asset = anAsset().build();
		Inspection inspection = anInspection().ofType(inspectionType).on(asset).build();
		return new InspectionScheduleBuilder(asset,inspectionType,new Date(), inspection, null);
	}

	public InspectionScheduleBuilder(Asset asset, InspectionType inspectionType, Date nextDate, Inspection inspection, Project job) {
		this.asset = asset;
		this.inspectionType = inspectionType;
		this.nextDate = nextDate;
		this.inspection = inspection;
		this.job = job;
	}
	
	public InspectionScheduleBuilder asset(Asset asset) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, inspection, job);
	}
	
	public InspectionScheduleBuilder inspectionType(InspectionType inspectionType) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, inspection, job);
	}
	
	public InspectionScheduleBuilder nextDate(Date nextDate) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, inspection, job);
	}
	
	public InspectionScheduleBuilder completedDoing(Inspection inspection) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, inspection, job);
	}
	
	public InspectionScheduleBuilder forJob(Project job) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, inspection, job);
	}
	
	@Override
	public InspectionSchedule createObject() {
		InspectionSchedule inspectionSchedule = new InspectionSchedule(asset, inspectionType);
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
