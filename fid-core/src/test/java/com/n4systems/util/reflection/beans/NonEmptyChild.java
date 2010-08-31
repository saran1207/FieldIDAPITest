package com.n4systems.util.reflection.beans;

public class NonEmptyChild extends EmptyParent {

	private String field;

	protected String getField() {
		return field;
	}

	protected void setField(String field) {
		this.field = field;
	}
	
	
}
