package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class InspectionFormSection {
	String sectionName;
	List<InspectionFormCriteria> criteria = new ArrayList<InspectionFormCriteria>();

	public InspectionFormSection(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public List<InspectionFormCriteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<InspectionFormCriteria> criteria) {
		this.criteria = criteria;
	}

	public InspectionFormCriteria getCriteria(int index) {
		if(criteria.size() < index) {
			return null;
		}
		return criteria.get(index);
	}
	
	public void setInspectionFormCriteria(int index, InspectionFormCriteria element) {
		criteria.add(index, element);
	}
}
