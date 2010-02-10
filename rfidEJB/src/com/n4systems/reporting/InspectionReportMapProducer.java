package com.n4systems.reporting;



import java.io.File;

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
		add("inspectionDate", formatDate(inspection.getDate(), true));
		add("inspectionDate_date", DateHelper.convertToUserTimeZone(inspection.getDate(), dateTimeDefinition.getTimeZone()));
		add("location", inspection.getLocation());
		add("inspectionBook", (inspection.getBook() != null) ? inspection.getBook().getName() : null);
		add("inspectionResult", inspection.getStatus().getDisplayName());
		add("proofTestInfo", addProofTestInfoParams(inspection));
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
