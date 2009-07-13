package com.n4systems.fieldid.testcase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Criteria {

	String label = "Default Criteria";
	ButtonGroup buttonGroup = new ButtonGroup("Pass, Fail", true);
	ArrayList<String> recs = new ArrayList<String>();
	ArrayList<String> defs = new ArrayList<String>();
	
	public Criteria() {
		super();
	}
	
	public Criteria(String label) {
		super();
		this.label = label;
	}
	
	public Criteria(String label, ButtonGroup bg) {
		this(label);
		buttonGroup = bg;
	}
	
	public Criteria(String label, ButtonGroup bg, List<String> recommendations, List<String> deficiencies) {
		this(label, bg);
		recs.addAll(recommendations);
		defs.addAll(deficiencies);
	}
	
	public String getCriteriaLabel() {
		return label;
	}
	
	public void setCriteriaLabel(String label) {
		this.label = label;
	}
	
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}
	
	public void setButtonGroup(ButtonGroup bg) {
		buttonGroup = bg;
	}
	
	public void addRecommendation(String r) {
		recs.add(r);
	}
	
	public String getRecommendation(int index) {
		return recs.get(index);
	}
	
	public int getNumberRecomendations() {
		return recs.size();
	}
	
	public void addDeficiency(String d) {
		defs.add(d);
	}
	
	public String getDeficiency(int index) {
		return defs.get(index);
	}
	
	public int getNumberDeficiencies() {
		return defs.size();
	}
	
	public Iterator<String> recommendationsIterator() {
		return recs.iterator();
	}
	
	public Iterator<String> deficienciesIterator() {
		return defs.iterator();
	}
}
