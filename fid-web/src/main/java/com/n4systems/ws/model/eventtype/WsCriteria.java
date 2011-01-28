package com.n4systems.ws.model.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public abstract class WsCriteria extends WsModel {
	private String criteriaType;
	private String displayText;
	private boolean retired;
	private List<String> recommendations = new ArrayList<String>();
	private List<String> deficiencies = new ArrayList<String>();

	@XmlElement(name="CriteriaType")
	public String getCriteriaType() {
		return criteriaType;
	}

	public void setCriteriaType(String type) {
		this.criteriaType = type;
	}

	@XmlElement(name="DisplayText")
	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	@XmlElement(name="Retired")
	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
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
}
