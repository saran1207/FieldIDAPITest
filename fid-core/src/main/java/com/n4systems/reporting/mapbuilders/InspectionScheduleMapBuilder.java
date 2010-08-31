package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.inspectionschedule.NextInspectionScheduleLoader;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.DateHelper;

public class InspectionScheduleMapBuilder extends AbstractMapBuilder<Inspection> {
	private final DateTimeDefiner dateDefiner;
	private final NextInspectionScheduleLoader nextInspectionScheduleLoader;
	
	public InspectionScheduleMapBuilder(DateTimeDefiner dateDefiner, NextInspectionScheduleLoader nextInspectionScheduleLoader) {
		super(ReportField.NEXT_DATE, ReportField.NEXT_DATE_STRING);
		
		this.dateDefiner = dateDefiner;
		this.nextInspectionScheduleLoader = nextInspectionScheduleLoader;
	}
	
	public InspectionScheduleMapBuilder(DateTimeDefiner dateDefiner) {
		this(dateDefiner, new NextInspectionScheduleLoader());
	}
	
	@Override
	protected void setAllFields(Inspection entity, Transaction transaction) {
		InspectionSchedule is = loadNextInspectionSchedule(entity, transaction);
		
		if (is != null) {
			setField(ReportField.NEXT_DATE,			is.getNextDate());
			setField(ReportField.NEXT_DATE_STRING,	DateHelper.format(is.getNextDate(), dateDefiner));
		}
	}
	
	private InspectionSchedule loadNextInspectionSchedule(Inspection inspection, Transaction transaction) {
		InspectionSchedule nextInspectionSchedule = nextInspectionScheduleLoader.setFieldsFromInspection(inspection).load(transaction);
		return nextInspectionSchedule;
	}
}
