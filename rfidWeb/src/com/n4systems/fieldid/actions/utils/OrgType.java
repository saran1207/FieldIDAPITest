package com.n4systems.fieldid.actions.utils;

public enum OrgType {
	ALL, INTERNAL, NON_PRIMARY, SECONDARY, PRIMARY, CUSTOMER, EXTERNAL;
	
	
	public static OrgType getByName(String name) {
		return OrgType.valueOf(name.toUpperCase());
	}
}
