package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class EventFormSection {
	String sectionName;
	List<OneClickEventFormCriteria> criteria = new ArrayList<OneClickEventFormCriteria>();

	public EventFormSection(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public List<OneClickEventFormCriteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<OneClickEventFormCriteria> criteria) {
		this.criteria = criteria;
	}

	public OneClickEventFormCriteria getCriteria(int index) {
		if(criteria.size() < index) {
			return null;
		}
		return criteria.get(index);
	}
	
	public void setEventFormCriteria(int index, OneClickEventFormCriteria element) {
		criteria.add(index, element);
	}
}
