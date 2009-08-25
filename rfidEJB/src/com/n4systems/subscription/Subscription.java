package com.n4systems.subscription;

import com.n4systems.subscription.netsuite.model.PaymentFrequency;

public interface Subscription {
	
	public Long getExternalId();
	public int getMonths();
	public int getUsers();
	public PaymentFrequency getFrequency();
	public String getReferralCode();
	public boolean isPurchasingPhoneSupport();
}
