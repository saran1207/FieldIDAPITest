package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsState extends WsModel {
	private String displayText;
	private String buttonName;
	private WsStatus status;
	private boolean active;
	
	@XmlElement(name="DisplayText")
	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	@XmlElement(name="ButtonName")
	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	@XmlElement(name="Status")
	public WsStatus getStatus() {
		return status;
	}

	public void setStatus(WsStatus status) {
		this.status = status;
	}

	@XmlElement(name="Active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
