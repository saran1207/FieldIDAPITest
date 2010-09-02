package com.n4systems.fieldid.selenium.datatypes;

public class Attribute {
	// different types of attribute
	public static final String TYPE_TEXTFIELD = "Text Field";
	public static final String TYPE_SELECTBOX = "Select Box";
	public static final String TYPE_COMBOBOX = "Combo Box";
	public static final String TYPE_UNITOFMEASURE = "Unit Of Measure";
	
	String name;
	String type;
	boolean required;
	
	public Attribute() {	
	}
	
	public Attribute(String name, String type, boolean required) {
		this.name = name;
		this.type = type;
		this.required = required;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean getRequired() {
		return required;
	}
	
	public void setName(String s) {
		name = s;
	}
	
	public void setType(String s) {
		type = s;
	}
	
	public void setRequired(boolean b) {
		required = b;
	}
}
