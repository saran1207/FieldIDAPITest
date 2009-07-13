package com.n4systems.fieldid.datatypes;

import java.util.ArrayList;
import java.util.List;

public class Criteria {

	String name;
	ButtonGroup bg;
	List<String> recommendations = new ArrayList<String>();
	List<String> deficiencies = new ArrayList<String>();
	
	public Criteria(String name, ButtonGroup bg) {
		this.name = name;
		this.bg = bg;
	}

	public String getCriteriaName() {
		return name;
	}

	public void setCriteriaName(String name) {
		this.name = name;
	}
	
	public ButtonGroup getButtonGroup() {
		return bg;
	}
	
	public void setButtonGroup(ButtonGroup bg) {
		this.bg = bg;
	}
	
	public String getRecommendation(int index) {
		return recommendations.get(index);
	}
	
	public void addRecommendation(String s) {
		recommendations.add(s);
	}
	
	public boolean deleteRecommendation(String s) {
		return recommendations.remove(s);
	}

	public String getDeficiency(int index) {
		return deficiencies.get(index);
	}
	
	public void addDeficiency(String s) {
		deficiencies.add(s);
	}
	
	public boolean deleteDeficiency(String s) {
		return deficiencies.remove(s);
	}

	public int getNumberOfRecommendations() {
		return recommendations.size();
	}

	public int getNumberOfDeficiencies() {
		return deficiencies.size();
	}
}
