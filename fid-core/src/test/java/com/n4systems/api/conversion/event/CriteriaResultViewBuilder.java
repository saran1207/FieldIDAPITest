package com.n4systems.api.conversion.event;

import com.n4systems.api.model.CriteriaResultView;
import com.n4systems.model.builders.BaseBuilder;

public class CriteriaResultViewBuilder extends BaseBuilder<CriteriaResultView> {

	private String displayText;
	private String section;
	private Object result = ""; 	
	private String recommendation;
	private String deficiency;
		
	
	
	public CriteriaResultViewBuilder() { 		
	}
	
	public CriteriaResultViewBuilder aCriteriaResultView() {
		this.displayText = "text";
		this.section = "aSection";
		this.result = "Pass"; 	
		this.recommendation = "recommendation";
		this.deficiency = "deficiency";
		return this;
	}	
	
	public CriteriaResultViewBuilder withSection(String section) { 
		this.section = section;
		return this;
	}
	
	public CriteriaResultViewBuilder withDisplayText(String text) { 
		this.displayText = text;
		return this;
	}
	
	@Override
	public CriteriaResultView createObject() {		
		CriteriaResultView crv = new CriteriaResultView(displayText, section, result, recommendation, deficiency);
		return crv;
	}

	public CriteriaResultViewBuilder withResult(Object result) {
		this.result = result; 
		return this;
	}
	
	
}

