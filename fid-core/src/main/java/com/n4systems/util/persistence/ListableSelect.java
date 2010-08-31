package com.n4systems.util.persistence;


public class ListableSelect extends NewObjectSelect {
	private static final long serialVersionUID = 1L;

	public ListableSelect() {
		this("id", "name");
	}
	
	public ListableSelect(String nameField) {
		this("id", nameField);
	}

	public ListableSelect(String idField, String nameField) {
		super(SimpleListable.class, idField, nameField);
	}
}
