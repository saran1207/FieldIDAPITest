package com.n4systems.ws.model.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsCriteria extends WsModel {
	private String displayText;
	private boolean principal;
	private List<String> recommendations = new ArrayList<String>();
	private List<String> deficiencies = new ArrayList<String>();
	private List<WsState> states = new ArrayList<WsState>();
	
	@XmlElement(name="DisplayText")
	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	@XmlElement(name="Principal")
	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	@XmlElement(name="Recommendations")
	public List<String> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<String> recommendations) {
		this.recommendations = recommendations;
	}

	@XmlElement(name="Deficiencies")
	public List<String> getDeficiencies() {
		return deficiencies;
	}

	public void setDeficiencies(List<String> deficiencies) {
		this.deficiencies = deficiencies;
	}

	@XmlElement(name="States")
	public List<WsState> getStates() {
		return states;
	}

	public void setStates(List<WsState> states) {
		this.states = states;
	}
}
