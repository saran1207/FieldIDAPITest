package com.n4systems.webservice.dto;

public class ObservationServiceDTO {
	
	public static String RECOMMENDATION = "recommendation";
	public static String DEFICIENCY = "deficiency";
	
	private String typeDiscriminator;
	private String observationText;
	private long orderIndex;
	private long criteriaId;
	
	public String getTypeDiscriminator() {
		return typeDiscriminator;
	}
	public void setTypeDiscriminator(String typeDiscriminator) {
		this.typeDiscriminator = typeDiscriminator;
	}
	public String getObservationText() {
		return observationText;
	}
	public void setObservationText(String observationText) {
		this.observationText = observationText;
	}
	public long getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(long orderIndex) {
		this.orderIndex = orderIndex;
	}
	public long getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(long criteriaId) {
		this.criteriaId = criteriaId;
	}		
}
