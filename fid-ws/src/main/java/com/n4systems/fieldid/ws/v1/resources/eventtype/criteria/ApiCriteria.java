package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.event.ApiCriteriaResult;
import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiCriteria extends ApiReadonlyModel {
	private String criteriaType;
	private String displayText;
	private List<String> recommendations = new ArrayList<String>();
	private List<String> deficiencies = new ArrayList<String>();
	private ApiCriteriaResult result;

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
}
