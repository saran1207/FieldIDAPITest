package com.n4systems.webservice.dto;

import java.util.List;

public class InspectionImageServiceDTO {

	private String inspectionMobileGuid;
	private boolean fromSubInspection;
	private long inspectorId;
	
	public long getInspectorId() {
		return inspectorId;
	}
	public void setInspectorId(long inspectorId) {
		this.inspectorId = inspectorId;
	}
	private ImageServiceDTO image;

	public boolean isFromSubInspection() {
		return fromSubInspection;
	}
	public void setFromSubInspection(boolean fromSubInspection) {
		this.fromSubInspection = fromSubInspection;
	}
	public String getInspectionMobileGuid() {
		return inspectionMobileGuid;
	}
	public void setInspectionMobileGuid(String inspectionMobileGuid) {
		this.inspectionMobileGuid = inspectionMobileGuid;
	}
	public ImageServiceDTO getImage() {
		return image;
	}
	public void setImage(ImageServiceDTO image) {
		this.image = image;
	}

}
