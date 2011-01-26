package com.n4systems.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WsUploadModel {
	private String id;

	@XmlElement(name="Id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
