package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;

public class UnitOfMeasure implements ProductTypeAtrribute {

	String unitOfMeasureValue = null;
	String unitOfMeasure = null;
	UnitEnum type = UnitEnum.undefined;
	
	public String getType() {
		return "UnitOfMeasure";
	}

	public String getValue() {
		return unitOfMeasureValue;
	}

	public void setValue(String s) {
		unitOfMeasureValue = s;
		if(s.indexOf("cm") != -1 && s.indexOf("m") == -1)		// cm
			type = UnitEnum.Centimeter;
		else if(s.indexOf("ft") != -1 && s.indexOf("in") != -1)	// ft in
			type = UnitEnum.Feet;
		else if(s.indexOf("in") != -1 && s.indexOf("ft") == -1)	// in
			type = UnitEnum.Inches;
		else if(s.indexOf("kg") != -1)							// kg
			type = UnitEnum.Kilograms;
		else if(s.indexOf("kN") != -1)							// kN
			type = UnitEnum.kiloNewtons;
		else if(s.indexOf("m") != -1 && s.indexOf("cm") != -1)	// m cm
			type = UnitEnum.Meters;
		else if(s.indexOf("mm") != -1)							// mm
			type = UnitEnum.Millimetres;
		else if(s.indexOf("lbs") != -1)							// lbs
			type = UnitEnum.Pounds;
		else if(s.indexOf("ton") != -1)							// ton
			type = UnitEnum.Ton;
		else if(s.indexOf("t") != -1 && s.indexOf("ton") == -1)	// t
			type = UnitEnum.Tonne;
	}

	public void setType(IE ie, String ID) throws Exception {
		// find the text field with the ID
		// find the associated anchor and click it
		// find the table
		// set the select in the table to the correct option
		// fill in the text field(s)
		// Submit
	}
}
