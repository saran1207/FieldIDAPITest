package com.n4systems.fieldid.datatypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Section {

	String name;
	List<Criteria> criteria = new ArrayList<Criteria>();
	
	public Section() {
		super();
	}

	public Section(String name) {
		super();
		this.name = name;
	}

	public void setSectionName(String name) {
		this.name = name;
	}
	public String getSectionName() {
		return name;
	}
	
	public void addCriteria(Criteria c) {
		criteria.add(c);
	}

	public Criteria getCriteria(int index) {
		return criteria.get(index);
	}
	
	public Criteria getCriteria(String name) {
		Iterator<Criteria> i = criteria.iterator();
		while(i.hasNext()) {
			Criteria c = i.next();
			if(c.getCriteriaName().equals(name)) {
				return c;
			}
		}
		
		return null;
	}
	
	public boolean deleteCriteria(Criteria c) {
		return criteria.remove(c);
	}

	public int getNumberOfCriteria() {
		return criteria.size();
	}
}
