package com.n4systems.netsuite.client;

import com.n4systems.netsuite.model.ContractLength;
import com.n4systems.netsuite.model.GetItemDetailsResponse;
import com.n4systems.netsuite.model.ProductInformation;

public class ProductDetailsClient extends AbstractNetsuiteClient<GetItemDetailsResponse> {

	public ProductDetailsClient() {
		super(GetItemDetailsResponse.class, "getitemdetails");
	}
	
	@Override
	protected void setupClassMap() {
		addToClassMap("itemlist", ProductInformation.class);
		addToClassMap("contractlengths", ContractLength.class);
	}
	
	@Override
	protected void addRequestParameters() {
		// No additional parameters
	}

}
