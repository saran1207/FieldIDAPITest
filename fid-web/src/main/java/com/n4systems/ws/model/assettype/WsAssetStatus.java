package com.n4systems.ws.model.assettype;

import com.n4systems.ws.model.WsModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WsAssetStatus extends WsModel {
	private String name;
	private boolean active;
	
	@XmlElement(name="Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="Active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
