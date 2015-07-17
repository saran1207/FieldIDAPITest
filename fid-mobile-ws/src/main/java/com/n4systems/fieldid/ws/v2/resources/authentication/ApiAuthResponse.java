package com.n4systems.fieldid.ws.v2.resources.authentication;

import com.n4systems.fieldid.ws.v2.resources.setupdata.user.ApiUser;

public class ApiAuthResponse {
	private ApiUser user;
	private ApiTenant tenant;

	public ApiUser getUser() {
		return user;
	}

	public void setUser(ApiUser user) {
		this.user = user;
	}

	public ApiTenant getTenant() {
		return tenant;
	}

	public void setTenant(ApiTenant tenant) {
		this.tenant = tenant;
	}
}
