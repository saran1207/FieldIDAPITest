package com.n4systems.fieldid.testcase;

public enum InspectPermissions {
	TAG(1), CREATE(2), SYSUSERS(3), ENDUSERS(4), UNDEFINED(5), EDIT(6), SYSCONFIG(7);
	
	int value;
	
	InspectPermissions(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
