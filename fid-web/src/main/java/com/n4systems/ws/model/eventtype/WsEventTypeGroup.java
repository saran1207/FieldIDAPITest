package com.n4systems.ws.model.eventtype;

import com.n4systems.ws.model.WsModel;

import javax.xml.bind.annotation.XmlElement;

public class WsEventTypeGroup extends WsModel {
	private String name;

	@XmlElement(name="Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
