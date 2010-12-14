package com.n4systems.util;

public enum UserType {
	ALL( "All" ),
	ADMIN( "Admin" ),
	SYSTEM( "System" ),
	FULL( "Full" ),
	READONLY( "Read-Only" ),
	LITE( "Lite" );
	
	String label;
	
	UserType( String label ) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
}
