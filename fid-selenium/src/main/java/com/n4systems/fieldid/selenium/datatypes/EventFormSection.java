package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class EventFormSection {
	String sectionName;
	List<EventFormCriteria> criteria = new ArrayList<EventFormCriteria>();

	public EventFormSection(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public List<EventFormCriteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<EventFormCriteria> criteria) {
		this.criteria = criteria;
	}

	public EventFormCriteria getCriteria(int index) {
		if(criteria.size() < index) {
			return null;
		}
		return criteria.get(index);
	}
	
	public void setInspectionFormCriteria(int index, EventFormCriteria element) {
		criteria.add(index, element);
	}
}
