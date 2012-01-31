package com.n4systems.fieldid.ws.v1.resources.tenant;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiTenant extends ApiReadonlyModel {
	private boolean usingAssignedTo;

	public void setUsingAssignedTo(boolean usingAssignedTo) {
		this.usingAssignedTo = usingAssignedTo;
	}

	public boolean isUsingAssignedTo() {
		return usingAssignedTo;
	}
}
