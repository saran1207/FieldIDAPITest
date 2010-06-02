package com.n4systems.reporting;



import java.io.File;
import java.util.Date;

import com.n4systems.model.AbstractInspection;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;

public class InspectionReportMapProducer extends AbsractInspectionReportMapProducer {
	
	private final Inspection inspection;
	
	public InspectionReportMapProducer(Inspection inspection, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
		this.inspection = inspection;
	}
	

	@Override
	protected void inspectionParameter() {
		Inspection inspection = (Inspection) this.getInspection();
		add("productLabel", null);
		add("location", inspection.getLocation());
		add("inspectionBook", (inspection.getBook() != null) ? inspection.getBook().getName() : null);
		add("inspectionResult", inspection.getStatus().getDisplayName());
		add("proofTestInfo", addProofTestInfoParams(inspection));

		fillInDate(inspection);
	}


	private void fillInDate(Inspection inspection) {
		String performedDateAsString = formatDate(inspection.getDate(), true);
		Date performedDateTimeZoneShifted = DateHelper.convertToUserTimeZone(inspection.getDate(), dateTimeDefinition.getTimeZone());
		
		add("inspectionDate", performedDateAsString);
		add("datePerformed", performedDateAsString);
		
		add("datePerformed_date", performedDateTimeZoneShifted);
		add("inspectionDate_date", performedDateTimeZoneShifted);
	}

	@Override
	protected File imagePath(FileAttachment imageAttachment) {
		return PathHandler.getInspectionAttachmentFile((Inspection)getInspection(), imageAttachment);
	}


	@Override
	protected AbstractInspection getInspection() {
		return inspection;
	}


	
	
	

}
