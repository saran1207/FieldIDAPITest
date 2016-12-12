package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class WsOneClickCriteria extends WsCriteria {
	private boolean principal;
	private List<WsState> states = new ArrayList<WsState>();
	
	public WsOneClickCriteria() {
		setCriteriaType("ONECLICK");
	}
	
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
