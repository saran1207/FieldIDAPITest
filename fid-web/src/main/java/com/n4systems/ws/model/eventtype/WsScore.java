package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsScore extends WsModel {
	
	private String name;
	private Double value;
	private boolean notApplicable = false;
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="Name")
	public String getName() {
		return name;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	@XmlElement(name="Value")
	public Double getValue() {
		return value;
	}

	public void setNotApplicable(boolean notApplicable) {
		this.notApplicable = notApplicable;
	}

	@XmlElement(name="NotApplicable")
	public boolean isNotApplicable() {
		return notApplicable;
	}
}
