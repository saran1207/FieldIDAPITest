package com.n4systems.fieldid.selenium.datatypes;

public class UnitOfMeasureAttribute extends Attribute {
	public static final String DEFAULT_CENTIMETERS = "Centimeters";
	public static final String DEFAULT_FEET = "Feet";
	public static final String DEFAULT_INCHES = "Inches";
	public static final String DEFAULT_KILOGRAMS = "Kilograms";
	public static final String DEFAULT_KILONEWTONS = "kiloNewtons";
	public static final String DEFAULT_METERS = "Meters";
	public static final String DEFAULT_MILLIMETERS = "Millimetres";
	public static final String DEFAULT_POUNDS = "Pounds";
	public static final String DEFAULT_TON = "Ton";
	public static final String DEFAULT_TONNE = "Tonne";
	
	String defaultMeasure;
	
	public UnitOfMeasureAttribute(String name, boolean required) {
		super(name, Attribute.TYPE_UNITOFMEASURE, required);
	}
	
	public UnitOfMeasureAttribute() {
		super();
	}
	
	public String getDefault() {
		return defaultMeasure;
	}
	
	public void setDefault(String s) {
		defaultMeasure = s;
	}
}
