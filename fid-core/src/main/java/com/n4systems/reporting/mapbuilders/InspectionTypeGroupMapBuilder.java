package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.EventTypeGroup;
import com.n4systems.persistence.Transaction;

public class InspectionTypeGroupMapBuilder extends AbstractMapBuilder<EventTypeGroup> {
	
	public InspectionTypeGroupMapBuilder() {
		super(ReportField.REPORT_TITLE);
	}
	
	@Override
	protected void setAllFields(EventTypeGroup entity, Transaction transaction) {
		setField(ReportField.REPORT_TITLE, entity.getReportTitle());	
	}
	
}
