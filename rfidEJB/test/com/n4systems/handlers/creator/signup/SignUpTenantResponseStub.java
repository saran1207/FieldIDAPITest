package com.n4systems.handlers.creator.signup;

import com.n4systems.subscription.ExternalIdResponse;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;

public class SignUpTenantResponseStub implements SignUpTenantResponse {

	public SignUpTenantResponseStub() {
	}

	public ExternalIdResponse getClient() {
		return null;
	}

	public Subscription getSubscription() {
		return null;
	}

	public ExternalIdResponse getTenant() {
		return null;
	}

	public String getResult() {
		return null;
	}

}
