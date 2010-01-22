package com.n4systems.fieldid.selenium.datatypes;

public class TextFieldAttribute extends Attribute {
	public TextFieldAttribute(String name, boolean required) {
		super(name, Attribute.TYPE_TEXTFIELD, required);
	}
	
	public TextFieldAttribute() {
		super();
	}
}
