package com.n4systems.subscription.local;

import com.n4systems.subscription.ExternalIdResponse;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;

public class LocalSignUpTenantResponse implements SignUpTenantResponse {

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
		return "SUCCESS";
	}
}
