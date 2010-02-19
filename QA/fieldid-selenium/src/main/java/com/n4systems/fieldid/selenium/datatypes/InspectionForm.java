package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class InspectionForm {
	List<InspectionFormSection> sections = new ArrayList<InspectionFormSection>();

	public List<InspectionFormSection> getSections() {
		return sections;
	}

	public void setSections(List<InspectionFormSection> sections) {
		this.sections = sections;
	}
	
	public InspectionFormSection getSection(int index) {
		if(sections.size() < index) {
			return null;
		}
		return sections.get(index);
	}
	
	public void addSection(int index, InspectionFormSection section) {
		sections.add(index, section);
	}
}
