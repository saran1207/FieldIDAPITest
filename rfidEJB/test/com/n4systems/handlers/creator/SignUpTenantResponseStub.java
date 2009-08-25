package com.n4systems.handlers.creator;

import com.n4systems.subscription.ExternalIdResponse;
import com.n4systems.subscription.SignUpTenantResponse;

public class SignUpTenantResponseStub implements SignUpTenantResponse {

	public SignUpTenantResponseStub() {
	}

	public ExternalIdResponse getClient() {
		return null;
	}

	public ExternalIdResponse getSubscription() {
		return null;
	}

	public ExternalIdResponse getTenant() {
		return null;
	}

	public String getResult() {
		return null;
	}

}
