package com.n4systems.netsuite.model;

public class GetPricingDetailsResponse extends AbstractResponse {
	
	private PricingDetails pricing;

	public PricingDetails getPricing() {
		return pricing;
	}

	public void setPricing(PricingDetails pricing) {
		this.pricing = pricing;
	}

}
