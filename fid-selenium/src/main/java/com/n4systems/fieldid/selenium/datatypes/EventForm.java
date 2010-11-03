package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class EventForm {
	List<EventFormSection> sections = new ArrayList<EventFormSection>();

	public List<EventFormSection> getSections() {
		return sections;
	}

	public void setSections(List<EventFormSection> sections) {
		this.sections = sections;
	}
	
	public EventFormSection getSection(int index) {
		if(sections.size() < index) {
			return null;
		}
		return sections.get(index);
	}
	
	public void addSection(int index, EventFormSection section) {
		sections.add(index, section);
	}
}
