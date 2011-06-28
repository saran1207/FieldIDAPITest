package com.n4systems.ws.model.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WsDateFieldCriteria extends WsCriteria {
	
	private boolean includeTime = false;
	
	public WsDateFieldCriteria() {
		setCriteriaType("DATEFIELD");
	}

	public void setIncludeTime(boolean includeTime) {
		this.includeTime = includeTime;
	}

	@XmlElement(name="IncludeTime")
	public boolean isIncludeTime() {
		return includeTime;
	}
}
