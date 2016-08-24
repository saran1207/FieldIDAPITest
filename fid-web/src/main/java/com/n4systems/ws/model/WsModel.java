package com.n4systems.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public abstract class WsModel implements Serializable {
	private long id;

	@XmlElement(name="Id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	
}
