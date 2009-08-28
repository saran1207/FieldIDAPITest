package com.n4systems.subscription.local;

import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.Pricing;

public class LocalPriceCheckResponse implements PriceCheckResponse {

	public Pricing getPricing() {
		return new LocalPricing();
	}

	public String getResult() {
		return "OK";
	}

}
