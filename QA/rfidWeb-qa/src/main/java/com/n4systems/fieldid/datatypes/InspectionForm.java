package com.n4systems.fieldid.datatypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InspectionForm {

	List<Section> sections = new ArrayList<Section>();
	
	public InspectionForm() {
		super();
	}

	public void addSection(Section s) {
		sections.add(s);
	}
	
	public Section getSection(int index) {
		return sections.get(index);
	}
	
	public Section getSection(String name) {
		Iterator<Section> i = sections.iterator();
		while(i.hasNext()) {
			Section tmp = i.next();
			String s = tmp.getSectionName();
			if(s.equals(name)) {
				return tmp;
			}
		}
		
		return null;
	}
	
	public int getNumberOfSections() {
		return sections.size();
	}
	
	public boolean deleteSection(Section s) {
		return sections.remove(s);
	}
}
