package com.n4systems.subscription.local;

import com.n4systems.subscription.ExternalIdResponse;
import com.n4systems.subscription.SignUpTenantResponse;

public class LocalSignUpTenantResponse implements SignUpTenantResponse {

	public ExternalIdResponse getClient() {
		return new LocalExternalIdResponse(1L);
	}

	public ExternalIdResponse getSubscription() {
		return new LocalExternalIdResponse(1L);
	}

	public ExternalIdResponse getTenant() {
		return new LocalExternalIdResponse(1L);
	}

	public String getResult() {
		return "SUCCESS";
	}
}
