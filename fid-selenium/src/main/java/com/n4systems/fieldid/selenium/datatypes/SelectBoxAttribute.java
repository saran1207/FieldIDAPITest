package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class SelectBoxAttribute extends Attribute {
	List<String> dropDowns = new ArrayList<String>();
	
	public SelectBoxAttribute(String name, boolean required) {
		super(name, Attribute.TYPE_SELECTBOX, required);
	}
	
	public SelectBoxAttribute() {
		super();
	}
	
	public List<String> getDropDowns() {
		return dropDowns;
	}
	
	public String getDropDown(int index) {
		return dropDowns.get(index);
	}
	
	public void addDropDown(String s) {
		dropDowns.add(s);
	}
	
	public void removeDropDown(String s) {
		dropDowns.remove(s);
	}
	
	public void setDropDowns(List<String> dropDowns) {
		this.dropDowns = dropDowns;
	}
}
