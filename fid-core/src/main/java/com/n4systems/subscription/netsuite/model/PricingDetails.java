package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.PaymentFrequency;
import com.n4systems.subscription.Pricing;

public class PricingDetails implements Pricing {
	
	private Float std_price;
	private String frequency;
	private Float disc_price;
	private Integer disc_months;
	private Float contract_value;
	private Float discount_total;
	private Float phonesupport_value;
	private Float storage_pricing;
	private Float first_payment;

	public void setPhonesupport_value(Float phonesupportValue) {
		phonesupport_value = phonesupportValue;
	}
	
	public void setStorage_pricing(Float storagePricing) {
		storage_pricing = storagePricing;
	}

	public void setStd_price(Float std_price) {
		this.std_price = std_price;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public void setDisc_price(Float disc_price) {
		this.disc_price = disc_price;
	}
	public void setDisc_months(Integer disc_months) {
		this.disc_months = disc_months;
	}
	public void setContract_value(Float contract_value) {
		this.contract_value = contract_value;
	}
	public void setDiscount_total(Float discount_total) {
		this.discount_total = discount_total;
	}	
	public void setFirst_payment(Float firstPayment) {
		first_payment = firstPayment;
	}

	public Integer getDiscountMonths() {
		return disc_months;
	}
	public Float getDiscountPrice() {
		return disc_price;
	}
	public Float getPrice() {
		return disc_price != null ? disc_price : std_price;
	}
	public Float getDiscountTotal() {
		return discount_total;
	}
	public Float getPhoneSupportValue() {
		return phonesupport_value;
	}
	public Float getStandardPrice() {
		return std_price;
	}
	public Float getStoragePrice() {
		return storage_pricing;
	}
	public PaymentFrequency getFrequency() {
		return PaymentFrequency.valueOf(frequency);
	}
	public Float getContractValue() {
		return contract_value;
	}
	public Float getFirstPaymentTotal() {
		return first_payment;
	}
	
}
