package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WsTextFieldCriteria extends WsCriteria {
	
	public WsTextFieldCriteria() {
		setCriteriaType("TEXTFIELD");
	}
	
}
