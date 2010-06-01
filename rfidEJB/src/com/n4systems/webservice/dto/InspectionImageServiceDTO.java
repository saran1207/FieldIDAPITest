package com.n4systems.webservice.dto;

public class InspectionImageServiceDTO {

	private String inspectionMobileGuid;
	private boolean fromSubInspection;
	private long inspectorId;
	private ImageServiceDTO image;

	public long getInspectorId() {
		return inspectorId;
	}

	
	//FIXME this needs to be renamed but mobile will depend on it.
	public void setInspectorId(long inspectorId) {
		this.inspectorId = inspectorId;
	}

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
