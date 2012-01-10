package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiOneClickState extends ApiReadonlyModel {
	private String displayText;
	private String buttonName;
	private ApiEventStatus status;
	
	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public ApiEventStatus getStatus() {
		return status;
	}

	public void setStatus(ApiEventStatus status) {
		this.status = status;
	}
	
}
