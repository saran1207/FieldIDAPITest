package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class WsComboBoxCriteria extends WsCriteria {
	private List<String> options = new ArrayList<String>();
	
	public WsComboBoxCriteria() {
		setCriteriaType("COMBOBOX");
	}
	
	@XmlElement(name="Options")
	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}
	
}
