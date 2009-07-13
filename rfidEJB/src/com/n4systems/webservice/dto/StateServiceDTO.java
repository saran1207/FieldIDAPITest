package com.n4systems.webservice.dto;

public class StateServiceDTO extends AbstractBaseServiceDTO {

	private String displayText;
	private String status;
	private String buttonName;
	private Long stateSetId;
	
	public String getDisplayText() {
		return displayText;
	}
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public Long getStateSetId() {
		return stateSetId;
	}
	public void setStateSetId(Long stateSetId) {
		this.stateSetId = stateSetId;
	}
	
}
