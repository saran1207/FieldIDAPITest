package com.n4systems.subscription.local;

import com.n4systems.subscription.PaymentFrequency;
import com.n4systems.subscription.Pricing;

public class LocalPricing implements Pricing {

	public Float getContractValue() {
		return 1185.00F;
	}

	public Integer getDiscountMonths() {
		return 3;
	}

	public Float getDiscountPrice() {
		return 95.00F;
	}

	public Float getDiscountTotal() {
		return 15.00F;
	}

	public PaymentFrequency getFrequency() {
		return PaymentFrequency.Monthly;
	}

	public Float getPhoneSupportValue() {
		return 185.00F;
	}

	public Float getStandardPrice() {
		return 100.00F;
	}

	public Float getStoragePrice() {
		return 10.00F;
	}

}
