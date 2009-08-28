package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.netsuite.model.GetItemDetailsResponse;

public class ProductDetailsClient extends AbstractNetsuiteClient<GetItemDetailsResponse> {

	public ProductDetailsClient() {
		super(GetItemDetailsResponse.class, "getitemdetails");
	}
	
	@Override
	protected void addRequestParameters() {
		// No additional parameters
	}

}
