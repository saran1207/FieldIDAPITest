package com.n4systems.subscription.netsuite.model;

public class ContractLength {

	private Long nsrecordid;
	private Integer months;
	private String price;

	public Long getNsrecordid() {
		return nsrecordid;
	}
	public void setNsrecordid(Long nsrecordid) {
		this.nsrecordid = nsrecordid;
	}
	public Integer getMonths() {
		return months;
	}
	public void setMonths(Integer months) {
		this.months = months;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
