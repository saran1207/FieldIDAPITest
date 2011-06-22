package com.n4systems.api.model;



public class CriteriaResultView {

	private String displayText;
	private String section;
	private String result = ""; 	
	private String recommendation;	// NOTE : only support single free form recommendation & deficiency.
	private String deficiency;
	
	public String getResultString() {
		return result;
	}
	public void setResultString(String result) {
		this.result = result;
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
		//FIXME DD: need proper implementation. 
		return displayText; 
		//return Objects.toStringHelper(getClass()).toString();
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getSection() {
		return section;
	}
	
}
