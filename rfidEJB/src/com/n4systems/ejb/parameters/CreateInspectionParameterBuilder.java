package com.n4systems.ejb.parameters;

import java.util.Date;
import java.util.List;

import com.n4systems.ejb.impl.CreateInspectionParameter;
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

	public CreateInspectionParameterBuilder(Inspection inspection, long userId) {
		this.inspection = inspection;
		this.userId = userId;
	}

	public CreateInspectionParameter build() {
		return new CreateInspectionParameter(inspection, nextInspectionDate, userId, proofTestData, uploadedImages, calculateInspectionResult);
	}
	
	public CreateInspectionParameterBuilder doNotCalculateInspectionResult() {
		calculateInspectionResult  = false;
		return this;
	}
	
	public CreateInspectionParameterBuilder withANextInspectionDate(Date nextInspectionDate) {
		
		this.nextInspectionDate = nextInspectionDate;
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

	

}
