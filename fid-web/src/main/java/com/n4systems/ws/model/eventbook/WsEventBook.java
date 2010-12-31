package com.n4systems.ws.model.eventbook;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;
import com.n4systems.ws.model.org.WsOrg;

@XmlRootElement
public class WsEventBook extends WsModel {
	private String name;
	private boolean open;
	private WsOrg owner;

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

}
