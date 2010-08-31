package com.n4systems.ejb.parameters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.ejb.impl.CreateInspectionParameter;
import com.n4systems.ejb.impl.InspectionScheduleBundle;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.tools.FileDataContainer;

public class CreateInspectionParameterBuilder {

	private final Inspection inspection;
	private final long userId;
	private boolean calculateInspectionResult = true;
	private Date nextInspectionDate = null;
	private FileDataContainer proofTestData = null;
	private List<FileAttachment> uploadedImages = null;
	private ArrayList<InspectionScheduleBundle> schedules = new ArrayList<InspectionScheduleBundle>();
	

	public CreateInspectionParameterBuilder(Inspection inspection, long userId) {
		this.inspection = inspection;
		this.userId = userId;
	}

	public CreateInspectionParameter build() {
		return new CreateInspectionParameter(inspection, nextInspectionDate, userId, proofTestData, uploadedImages, calculateInspectionResult, schedules);
	}
	
	public CreateInspectionParameterBuilder doNotCalculateInspectionResult() {
		calculateInspectionResult  = false;
		return this;
	}
	
	public CreateInspectionParameterBuilder withANextInspectionDate(Date nextInspectionDate) {
		this.nextInspectionDate = nextInspectionDate;
		
		if (nextInspectionDate != null) {
			addSchedule(new InspectionScheduleBundle(inspection.getProduct(), inspection.getType(), null, nextInspectionDate));
		}
		return this;
	}

	public CreateInspectionParameterBuilder addSchedule(InspectionScheduleBundle inspectionSchedule) {
		schedules.add(inspectionSchedule);
		return this;
	}

	public CreateInspectionParameterBuilder withProofTestFile(FileDataContainer proofTestData) {
		this.proofTestData = proofTestData;
		return this;
	}

	public CreateInspectionParameterBuilder withUploadedImages(List<FileAttachment> uploadedImages) {
		this.uploadedImages = uploadedImages;
		return this;
		
	}

	public CreateInspectionParameterBuilder addSchedules(List<InspectionScheduleBundle> schedules) {
		for (InspectionScheduleBundle inspectionScheduleBundle : schedules) {
			addSchedule(inspectionScheduleBundle);
		}
		return this;
	}

}
