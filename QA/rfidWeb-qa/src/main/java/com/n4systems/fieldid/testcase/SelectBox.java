package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;
import static watij.finders.SymbolFactory.*;

public class SelectBox implements ProductTypeAtrribute {

	String textOption = null;
	
	public String getType() {
		return "SelectBox";
	}

	public String getValue() {
		return textOption;
	}

	public void setValue(String s) {
		textOption = s;
	}

	public void setType(IE ie, String ID) throws Exception {
		ie.selectList(id, ID).option(text, textOption).select();
	}

}
