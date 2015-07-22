package com.n4systems.fieldid.ws.v2.resources.setupdata.eventform.criteria;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel2;

import java.util.ArrayList;
import java.util.List;

public class ApiCriteria extends ApiReadOnlyModel2 {
	private String criteriaType;
	private String displayText;
	private String instructions;
	private List<String> recommendations = new ArrayList<>();
	private List<String> deficiencies = new ArrayList<>();

	protected ApiCriteria(String criteriaType) {
		this.criteriaType = criteriaType;
	}
	
	public String getCriteriaType() {
		return criteriaType;
	}

	public void setCriteriaType(String criteriaType) {
		this.criteriaType = criteriaType;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	
	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public List<String> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<String> recommendations) {
		this.recommendations = recommendations;
	}

	public List<String> getDeficiencies() {
		return deficiencies;
	}

	public void setDeficiencies(List<String> deficiencies) {
		this.deficiencies = deficiencies;
	}
}
