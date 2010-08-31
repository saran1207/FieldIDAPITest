package com.n4systems.webservice.dto;

public class InspectionInfoOptionServiceDTO {
	
	private long inspectionId;
	private String infoFieldName;
	private String infoOptionValue;

	public long getInspectionId() {
		return inspectionId;
	}
	public void setInspectionId(long inspectionId) {
		this.inspectionId = inspectionId;
	}
	public String getInfoFieldName() {
		return infoFieldName;
	}
	public void setInfoFieldName(String infoFieldName) {
		this.infoFieldName = infoFieldName;
	}
	public String getInfoOptionValue() {
		return infoOptionValue;
	}
	public void setInfoOptionValue(String infoOptionValue) {
		this.infoOptionValue = infoOptionValue;
	}
}
