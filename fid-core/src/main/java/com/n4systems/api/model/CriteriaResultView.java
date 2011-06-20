package com.n4systems.api.model;


public class CriteriaResultView {

	private String displayText;
	private String section;
	private String result;  //TODO DD : hmmm...result can't be generic 'cause of discrepencies among criteria.   need to interface a getRawValue() method for all criteria???	
	private String recommendation;	// NOTE : only support single free form recommendation & deficiency.
	private String deficiency;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	public String getDeficiency() {
		return deficiency;
	}
	public void setDeficiency(String deficiency) {
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
