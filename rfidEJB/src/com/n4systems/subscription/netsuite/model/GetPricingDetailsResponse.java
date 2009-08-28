package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.Pricing;

public class GetPricingDetailsResponse extends AbstractResponse implements PriceCheckResponse {
	
	private PricingDetails pricing;

	public Pricing getPricing() {
		return pricing;
	}

	public void setPricing(PricingDetails pricing) {
		this.pricing = pricing;
	}

}
