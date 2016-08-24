package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class WsSelectCriteria extends WsCriteria {
	private List<String> options = new ArrayList<String>();

	public WsSelectCriteria() {
		setCriteriaType("SELECTBOX");
	}
	
	@XmlElement(name="Options")
	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}
}
