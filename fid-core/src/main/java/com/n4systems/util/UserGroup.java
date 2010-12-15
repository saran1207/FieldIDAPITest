package com.n4systems.util;

public enum UserGroup {
	ALL( "All" ),
	EMPLOYEE( "Employee" ),
	CUSTOMER( "Customer" );
	
	public String label;
	
	UserGroup( String label ) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
