package com.n4systems.fieldid.testcase;

/**
 * When you create an inspection type you have the option of adding
 * an inspection form to the inspection type. An inspection form
 * contains sections. Within each section there are zero or more
 * criteria sections. A criteria will have a label, a button group
 * a flag to indicate a failure on this criteria is a failure for
 * the inspection and zero or more observations. The observations
 * can be recommendations and/or deficiencies.
 * 
 * A section must have a name. If you do not provide one, it will
 * default to "Default".
 * 
 * If the criteria is not provided or null then we create a section
 * with no criteria (not really useful but possible).
 * 
 * For each Criteria, it must have a label, a button group and zero
 * or more recommendations/deficiencies. If the label is not provided
 * it will default to "Default Criteria". If no ButtonGroup is provided
 * it will default to a ButtonGroup name "Pass, Fail" and sets result
 * will be true.
 * 
 */
public class Section {
	String sectionName = "Default";
	Criteria[] criterias = null;
	
	public Section() {
		super();
	}
	
	public Section(String name) {
		super();
		setSectionName(name);
	}
	
	public Section(String name, Criteria[] c) {
		this(name);
		criterias = c;
	}
	
	public String getSectionName() {
		return sectionName;
	}
	
	public void setSectionName(String name) {
		sectionName = name;
	}
	
	public Criteria getCriteria(int index) {
		if(criterias != null && index < criterias.length)
			return criterias[index];
		return null;
	}
	
	public int getNumberCriteria() {
		if(criterias != null)
			return criterias.length;
		return 0;
	}
	
	public void setCriteria(Criteria[] c) {
		criterias = c;
	}
	
	public void setCriteria(Criteria c, int index) {
		if(criterias != null)
			criterias[index] = c;
	}
}
