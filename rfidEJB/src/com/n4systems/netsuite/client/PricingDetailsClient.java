package com.n4systems.netsuite.client;

import com.n4systems.netsuite.model.GetPricingDetailsResponse;
import com.n4systems.netsuite.model.PricingDetails;

public class PricingDetailsClient extends AbstractNetsuiteClient<GetPricingDetailsResponse> {

	private String referralCode;
	private String itemId;
	private String contractLength;
	private String frequency;
	private String users;
	
	public PricingDetailsClient() {
		super(GetPricingDetailsResponse.class, "getpricingdetails");
	}
	
	@Override
	protected void addRequestParameters() {
		addRequestParameter("itemid", itemId);
		addRequestParameter("contractlength", contractLength);
		addRequestParameter("frequency", frequency);
		addRequestParameter("users", users);

		if (referralCode != null) {
			addRequestParameter("referralcode", referralCode);
		}
	}
	
	@Override
	protected void setupClassMap() {
		addToClassMap("pricing", PricingDetails.class);
	}

	
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public void setContractLength(int contractLength) {
		this.contractLength = String.valueOf(contractLength);
	}
	
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public void setUsers(int users) {
		this.users = String.valueOf(users);
	}	
	
	
}
