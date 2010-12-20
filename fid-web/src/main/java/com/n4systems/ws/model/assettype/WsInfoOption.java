package com.n4systems.ws.model.assettype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsInfoOption extends WsModel {
	private String name;
	private long infoFieldId;
	private boolean staticData;

	@XmlElement(name="Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="InfoFieldId")
	public long getInfoFieldId() {
		return infoFieldId;
	}

	public void setInfoFieldId(long infoFieldId) {
		this.infoFieldId = infoFieldId;
	}

	@XmlElement(name="StaticData")
	public boolean isStaticData() {
		return staticData;
	}

	public void setStaticData(boolean staticData) {
		this.staticData = staticData;
	}

}
