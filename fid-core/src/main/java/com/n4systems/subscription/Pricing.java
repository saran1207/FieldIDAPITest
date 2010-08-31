package com.n4systems.subscription;

public interface Pricing {

	public Float getStandardPrice();
	public PaymentFrequency getFrequency();
	public Float getStoragePrice();
	public Float getDiscountPrice();
	public Float getPrice();
	public Integer getDiscountMonths();
	public Float getContractValue();
	public Float getDiscountTotal();
	public Float getPhoneSupportValue();
	
	public Float getFirstPaymentTotal();
	
}
