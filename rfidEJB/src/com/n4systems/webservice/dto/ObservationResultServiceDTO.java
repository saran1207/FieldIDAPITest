package com.n4systems.webservice.dto;


public class ObservationResultServiceDTO {

	public enum ObservationType { RECOMENDATION, DEFICIENCY };
	
	public enum ObservationState { OUTSTANDING, REPAIREDONSITE, REPAIRED, COMMENT };
	
	private ObservationType type;	
	private String text;
	private ObservationState state;

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
