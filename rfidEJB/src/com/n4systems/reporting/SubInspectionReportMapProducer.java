package com.n4systems.reporting;

import java.io.File;

import com.n4systems.model.AbstractInspection;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.SubInspection;
import com.n4systems.util.DateTimeDefinition;

public class SubInspectionReportMapProducer extends AbsractInspectionReportMapProducer {

	private final SubInspection inspection;
	private final Inspection masterInspection;
	
	public SubInspectionReportMapProducer(SubInspection inspection, Inspection masterInspection, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
		this.inspection = inspection;
		this.masterInspection = masterInspection;
	}
	
	@Override
	public void inspectionParameter() {
		SubInspection subInspection = (SubInspection) getInspection();
		add("productLabel", subInspection.getName());
	}

	@Override
	protected File imagePath(FileAttachment imageAttachment) {
		return PathHandler.getInspectionAttachmentFile(masterInspection, inspection, imageAttachment);
	}

	@Override
	protected AbstractInspection getInspection() {
		return inspection;
	}



}
