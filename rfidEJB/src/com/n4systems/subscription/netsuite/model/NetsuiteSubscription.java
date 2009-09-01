package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.PaymentFrequency;
import com.n4systems.subscription.Subscription;

public class NetsuiteSubscription implements Subscription {
	private Long contractId;
	private Integer months;
	private Integer users;
	private PaymentFrequency frequency;
	private String promoCode;
	private boolean purchasingPhoneSupport;

	public Long getContractId() {
		return contractId;
	}
	
	public void setNsrecordid(Long nsrecordid) {
		this.contractId = nsrecordid;
	}

	public PaymentFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(PaymentFrequency frequency) {
		this.frequency = frequency;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public boolean isPurchasingPhoneSupport() {
		return purchasingPhoneSupport;
	}

	public void setPurchasingPhoneSupport(boolean purchasingPhoneSupport) {
		this.purchasingPhoneSupport = purchasingPhoneSupport;
	}

	public Integer getMonths() {
		return months;
	}

	public void setMonths(Integer months) {
		this.months = months;
	}

	public Integer getUsers() {
		return users;
	}

	public void setUsers(Integer users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return getContractId() + " for "+getMonths()+" months "+" with "+getUsers()+" users. "+getFrequency()+". Phone support? "+isPurchasingPhoneSupport();
	}
	
	
	
}
