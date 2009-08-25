package com.n4systems.subscription.netsuite.model;

public class PricingDetails {
	
	private Float std_price;
	private String frequency;
	private Float disc_price;
	private Integer disc_months;
	private Float contract_value;
	private Float discount_total;

	
	public Float getStd_price() {
		return std_price;
	}
	public void setStd_price(Float std_price) {
		this.std_price = std_price;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public Float getDisc_price() {
		return disc_price;
	}
	public void setDisc_price(Float disc_price) {
		this.disc_price = disc_price;
	}
	public Integer getDisc_months() {
		return disc_months;
	}
	public void setDisc_months(Integer disc_months) {
		this.disc_months = disc_months;
	}
	public Float getContract_value() {
		return contract_value;
	}
	public void setContract_value(Float contract_value) {
		this.contract_value = contract_value;
	}
	public Float getDiscount_total() {
		return discount_total;
	}
	public void setDiscount_total(Float discount_total) {
		this.discount_total = discount_total;
	}
	
}
