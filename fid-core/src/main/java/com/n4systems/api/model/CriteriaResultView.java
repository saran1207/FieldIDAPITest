package com.n4systems.api.model;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Objects;


public class CriteriaResultView {
	
	private String displayText;
	private String section;
	private Object result; 	
	private String recommendation;	// NOTE : only support single free form recommendation & deficiency.
	private String deficiency;
	
	@Deprecated // for testing purposes only.
	public CriteriaResultView(String displayText, String section, Object result, String recommendation, String deficiency) {
		this.displayText = displayText;
		this.section = section;
		this.result = result; 	
		this.recommendation = recommendation;
		this.deficiency = deficiency;
	}
	
	public CriteriaResultView() {
	}

	public String getResultString() {
		return result==null ? "" : StringUtils.trimToEmpty(result.toString());
	}
	public void setResultString(String result) {
		this.result = result;
	}
	public void setResult(Object result) {
		this.result = result;		
	}	
	public Object getResult() { 
		return result;
	}
	public String getRecommendationString() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	public String getDeficiencyString() {
		return deficiency;
	}
	public void setDeficiencyString(String deficiency) {
		this.deficiency = deficiency;
	}
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	public String getDisplayText() {
		return displayText;
	}
	@Override
	public String toString() {		
		return Objects.toStringHelper(getClass()).toString();
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getSection() {
		return section;
	}

	
}
