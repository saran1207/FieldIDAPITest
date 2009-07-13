package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import static watij.finders.SymbolFactory.*;

public class TextFieldAttribute implements ProductTypeAtrribute {

	String textField = null;
	
	public String getType() {
		return "TextField";
	}

	public String getValue() {
		return textField;
	}

	public void setValue(String s) {
		textField = s;
	}

	public void setType(IE ie, String ID) throws Exception {
		ie.textField(id, ID).set(textField);
	}
}
