package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.netsuite.model.GetSubscriptionDetailsResponse;

public class SubscriptionDetailsClient extends AbstractNetsuiteClient<GetSubscriptionDetailsResponse> {

	private Long tenantExternalId;
	
	public SubscriptionDetailsClient() {
		super(GetSubscriptionDetailsResponse.class, "getsubscriptiondetails");
	}
	
	@Override
	protected void addRequestParameters() {
		addRequestParameter("tenantid", tenantExternalId.toString());		
	}

	public void setTenantExternalId(Long tenantExternalId) {
		this.tenantExternalId = tenantExternalId;
	}

}
