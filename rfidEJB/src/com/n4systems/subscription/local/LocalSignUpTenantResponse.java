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
			clientExternalIdResponse = new LocalExternalIdResponse(randomPositiveLong());
		
		return clientExternalIdResponse;
	}

	private long randomPositiveLong() {
		return Math.abs(new Random().nextLong());
	}

	public Subscription getSubscription() {
		return null;
	}

	public ExternalIdResponse getTenant() {
		if (tenantExternalIdResponse == null)
			tenantExternalIdResponse = new LocalExternalIdResponse(randomPositiveLong());
		
		return tenantExternalIdResponse;
	}

	public String getResult() {
		return "SUCCESS";
	}
}
