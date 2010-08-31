package com.n4systems.handlers.creator.signup;

import com.n4systems.subscription.ExternalIdResponse;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.local.LocalExternalIdResponse;

public class SignUpTenantResponseStub implements SignUpTenantResponse {

	public SignUpTenantResponseStub() {
	}

	public ExternalIdResponse getClient() {
		return new LocalExternalIdResponse(1L);
	}

	public Subscription getSubscription() {
		return null;
	}

	public ExternalIdResponse getTenant() {
		return new LocalExternalIdResponse(1L);
	}

	public String getResult() {
		return null;
	}

}
