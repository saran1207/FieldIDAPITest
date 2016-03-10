package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import com.n4systems.fieldid.ws.v1.resources.event.ApiCriteriaResult;
import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiCriteria extends ApiReadonlyModel {
	private String criteriaType;
	private String displayText;
	private String instructions;
	private List<String> recommendations = new ArrayList<String>();
	private List<String> deficiencies = new ArrayList<String>();
	private ApiCriteriaResult result;
	private Boolean required;
	private List<ApiCriteriaRule> criteriaRules;

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
	
	public ApiCriteriaResult getResult() {
		return result;
	}

	public void setResult(ApiCriteriaResult result) {
		this.result = result;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public List<ApiCriteriaRule> getCriteriaRules() {
		return criteriaRules;
	}

	public ApiCriteria setCriteriaRules(List<ApiCriteriaRule> criteriaRules) {
		this.criteriaRules = criteriaRules;
		return this;
	}
}
