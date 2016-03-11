package com.n4systems.ws.model.lastmod;

import com.n4systems.ws.model.WsModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class WsLastModified extends WsModel {
	private Date modified;

	@XmlElement(name="Modified")
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

}
