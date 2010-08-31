package com.n4systems.subscription.local;

import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.Pricing;

public class LocalPriceCheckResponse implements PriceCheckResponse {
	private Pricing pricing;
	
	public LocalPriceCheckResponse() {
		this(new LocalPricing());
	}
	
	public LocalPriceCheckResponse(Pricing pricing) {
		super();
		this.pricing = pricing;
	}
	
	public Pricing getPricing() {
		return pricing;
	}

	public String getResult() {
		return "OK";
	}

}
