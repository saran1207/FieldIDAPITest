package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class InspectionFormObservations {
	private List<String> recommendations = new ArrayList<String>();
	private List<String> deficiencies = new ArrayList<String>();
	
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
