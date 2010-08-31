package com.n4systems.webservice.dto;

public class InspectionImageServiceDTO {

	private String inspectionMobileGuid;
	private boolean fromSubInspection;
	private long performedById;
	private ImageServiceDTO image;

	public long getPerformedById() {
		return performedById;
	}

	public void setPerformedById(long performedById) {
		this.performedById = performedById;
	}
	
	
	
	@Deprecated
	public void setInspectorId(long performedById) {
		this.performedById = performedById;
	}
	
	@Deprecated
	public long getInspectorId() {
		return performedById;
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
