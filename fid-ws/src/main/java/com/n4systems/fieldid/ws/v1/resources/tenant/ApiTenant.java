package com.n4systems.fieldid.ws.v1.resources.tenant;

public class ApiTenant {
	private boolean usingAssignedTo;

	public void setUsingAssignedTo(boolean usingAssignedTo) {
		this.usingAssignedTo = usingAssignedTo;
	}

	public boolean isUsingAssignedTo() {
		return usingAssignedTo;
	}
}
