package com.n4systems.ws.model.eventbook;

import com.n4systems.ws.model.WsUploadModel;
import com.n4systems.ws.model.org.WsOrg;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WsEventBook extends WsUploadModel {
	private String name;
	private boolean open;
	private WsOrg owner;
	private boolean active;

	@XmlElement(name="Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="Open")
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	@XmlElement(name="Owner")
	public WsOrg getOwner() {
		return owner;
	}

	public void setOwner(WsOrg owner) {
		this.owner = owner;
	}	

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
}
