package com.n4systems.util;

public enum UserBelongsToFilter {
	ALL( "All" ),
	EMPLOYEE( "Employee" ),
	CUSTOMER( "Customer" );
	
	public String label;
	
	UserBelongsToFilter(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
