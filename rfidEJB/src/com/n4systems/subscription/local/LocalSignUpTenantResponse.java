package com.n4systems.subscription.local;

import java.util.Random;

import com.n4systems.subscription.ExternalIdResponse;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;

public class LocalSignUpTenantResponse implements SignUpTenantResponse {

	private LocalExternalIdResponse tenantExternalIdResponse;
	private LocalExternalIdResponse clientExternalIdResponse;

	public ExternalIdResponse getClient() {
		if (clientExternalIdResponse == null)
			clientExternalIdResponse = new LocalExternalIdResponse(new Random().nextLong());
		
		return clientExternalIdResponse;
	}

	public Subscription getSubscription() {
		return null;
	}

	public ExternalIdResponse getTenant() {
		if (tenantExternalIdResponse == null)
			tenantExternalIdResponse = new LocalExternalIdResponse(new Random().nextLong());
		
		return tenantExternalIdResponse;
	}

	public String getResult() {
		return "SUCCESS";
	}
}
