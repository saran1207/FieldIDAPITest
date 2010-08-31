package com.n4systems.subscription;

public interface ContractPrice {
	
	public Long getExternalId();
	public String getSyncId();
	public PaymentOption getPaymentOption();
	public Float getPrice();

}
