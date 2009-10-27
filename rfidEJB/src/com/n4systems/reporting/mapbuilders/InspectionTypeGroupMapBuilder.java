package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.persistence.Transaction;

public class InspectionTypeGroupMapBuilder extends AbstractMapBuilder<InspectionTypeGroup> {
	
	public InspectionTypeGroupMapBuilder() {
		super(ReportField.REPORT_TITLE);
	}
	
	@Override
	protected void setAllFields(InspectionTypeGroup entity, Transaction transaction) {
		setField(ReportField.REPORT_TITLE, entity.getReportTitle());	
	}
	
}
