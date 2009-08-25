package com.n4systems.subscription;

public interface SignUpTenantResponse extends Response {
	
	public ExternalIdResponse getTenant();
	public ExternalIdResponse getClient();
	public ExternalIdResponse getSubscription();
	
}
