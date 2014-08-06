package com.n4systems.util;

import com.n4systems.model.api.DisplayEnum;

public enum UserBelongsToFilter implements DisplayEnum{
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
