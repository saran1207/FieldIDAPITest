package com.n4systems.ws.model.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="ONECLICK")
public class WsOneClickCriteria extends WsCriteria {
	private boolean principal;
	private List<WsState> states = new ArrayList<WsState>();
	
	@XmlElement(name="Principal")
	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
	@XmlElement(name="States")
	public List<WsState> getStates() {
		return states;
	}

	public void setStates(List<WsState> states) {
		this.states = states;
	}
}
