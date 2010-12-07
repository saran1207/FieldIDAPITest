package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsEventTypeFormVersion extends WsModel {
	private long version;

	@XmlElement(name="Version")
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
