package com.n4systems.api.conversion.event;

import com.n4systems.api.model.CriteriaResultView;
import com.n4systems.model.builders.BaseBuilder;

public class CriteriaResultViewBuilder extends BaseBuilder<CriteriaResultView> {

	private String displayText;
	private String section;
	private String result = ""; 	
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

	@Override
	public CriteriaResultView createObject() {		
		return new CriteriaResultView(displayText, section, result, recommendation, deficiency);
	}
	
	
}

