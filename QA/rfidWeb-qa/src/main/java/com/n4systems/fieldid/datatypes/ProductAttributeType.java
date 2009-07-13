package com.n4systems.fieldid.datatypes;

import junit.framework.TestCase;

/**
 * This is a list of the options for Attribute Types and the default possibilities for Unit Of Measure
 *
 */
public final class ProductAttributeType extends TestCase {
	// Types
	public static String TextField = "Text Field";
	public static String SelectBox = "Select Box";
	public static String ComboBox = "Combo Box";
	public static String UnitOfMeasure = "Unit Of Measure";
	
	// Defaults
	public static String cm = "Centimeters";
	public static String ft = "Feet";
	public static String in = "Inches";
	public static String kg = "Kilograms";
	public static String kN = "kiloNewtons";
	public static String m = "Meters";
	public static String mm = "Millimetres";
	public static String lbs = "Pounds";
	public static String ton = "Ton";
	public static String tonne = "Tonne";
	
	private String type = TextField;
	private String def = null;			// only used with UnitOfMeasure
	private String[] dropDowns = null;	// used for SelectBox and ComboBox
	
	public ProductAttributeType() {
		super();
	}
	
	public void setType(String type) {
		boolean valid = false;
		if(type.equals(TextField)) {
			valid = true;
		} else if(type.equals(SelectBox)) {
			valid = true;
		} else if(type.equals(ComboBox)) {
			valid = true;
		} else if(type.equals(UnitOfMeasure)) {
			valid = true;
		}
		assertTrue("Input '" + type + "' was not a valid attribute type", valid);
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setDefaultUnitOfMeasure(String def) {
		boolean valid = false;
		if(def.equals(cm)) {
			valid = true;
		} else if(def.equals(ft)) {
			valid = true;
		} else if(def.equals(in)) {
			valid = true;
		} else if(def.equals(kg)) {
			valid = true;
		} else if(def.equals(kN)) {
			valid = true;
		} else if(def.equals(m)) {
			valid = true;
		} else if(def.equals(mm)) {
			valid = true;
		} else if(def.equals(lbs)) {
			valid = true;
		} else if(def.equals(ton)) {
			valid = true;
		} else if(def.equals(tonne)) {
			valid = true;
		}
		assertTrue("Input '" + def + "' was not a valid attribute default", valid);
		this.def = def;
	}
	
	public String getDefaultUnitOfMeasure() {
		return def;
	}
	
	public void setDropDowns(String[] dropDowns) {
		this.dropDowns = dropDowns;
	}
	
	public String[] getDropDowns() {
		return dropDowns;
	}
}
