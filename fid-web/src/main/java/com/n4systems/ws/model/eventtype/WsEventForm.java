package com.n4systems.ws.model.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsEventForm extends WsModel {
	private List<WsCriteriaSection> sections = new ArrayList<WsCriteriaSection>();
	
	@XmlElement(name="Sections")
	public List<WsCriteriaSection> getSections() {
		return sections;
	}

	public void setSections(List<WsCriteriaSection> sections) {
		this.sections = sections;
	}
}
