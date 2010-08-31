package com.n4systems.subscription.local;

import com.n4systems.subscription.ContractPrice;
import com.n4systems.subscription.PaymentOption;

public class LocalContractPrice implements ContractPrice {

	private Long externalId;
	private PaymentOption paymentOption;
	private Float price;
	private String syncId;

	public Long getExternalId() {
		return externalId;
	}
	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}
	public PaymentOption getPaymentOption() {
		return paymentOption;
	}
	public void setPaymentOption(PaymentOption paymentOption) {
		this.paymentOption = paymentOption;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getSyncId() {
		return syncId;
	}
	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}
}
