package com.n4systems.util;

public enum UserType {
	ALL( "All" ),
	EMPLOYEES( "Employee" ),
	READONLY( "Read-Only" );
	
	
	String label;
	
	UserType( String label ) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
}
