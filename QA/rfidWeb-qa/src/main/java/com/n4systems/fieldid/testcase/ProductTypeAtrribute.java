package com.n4systems.fieldid.testcase;

import watij.runtime.ie.IE;

public interface ProductTypeAtrribute {
	public String getType();
	public String getValue();
	public void setType(IE ie, String ID) throws Exception;
	public void setValue(String s);
}
