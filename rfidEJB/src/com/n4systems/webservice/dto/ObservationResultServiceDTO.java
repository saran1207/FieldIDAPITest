package com.n4systems.webservice.dto;



public class ObservationResultServiceDTO {

	public enum ObservationType { RECOMMENDATION, DEFICIENCY };
	
	public enum ObservationState { OUTSTANDING, REPAIREDONSITE, REPAIRED, COMMENT };
	
	private ObservationType type;	
	private String text;
	private ObservationState state;

	private long criteriaResultId;
	private long orderIndex;
	
	public long getCriteriaResultId() {
		return criteriaResultId;
	}
	public void setCriteriaResultId(long criteriaResultId) {
		this.criteriaResultId = criteriaResultId;
	}
	public long getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(long orderIndex) {
		this.orderIndex = orderIndex;
	}
	public ObservationType getType() {
		return type;
	}
	public void setType(ObservationType type) {
		this.type = type;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ObservationState getState() {
		return state;
	}
	public void setState(ObservationState state) {
		this.state = state;
	}
	
}
