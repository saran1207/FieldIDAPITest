package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.netsuite.model.GetPricingDetailsResponse;

public class PricingDetailsClient extends AbstractNetsuiteClient<GetPricingDetailsResponse> {

	private Subscription subscription;
	
	
	public PricingDetailsClient() {
		super(GetPricingDetailsResponse.class, "getpricingdetails");
	}
	
	@Override
	protected void addRequestParameters() {
		addRequestParameter("itemid", subscription.getExternalId().toString());
		addRequestParameter("contractlength", subscription.getMonths().toString());
		addRequestParameter("frequency", subscription.getFrequency().getCode());
		addRequestParameter("numusers", subscription.getUsers().toString());
		addRequestParameter("phonesupport", subscription.isPurchasingPhoneSupport() ? "T" : "F");
		
		if (subscription.getPromoCode() != null) {
			addRequestParameter("promocode", subscription.getPromoCode());
		}
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
}
