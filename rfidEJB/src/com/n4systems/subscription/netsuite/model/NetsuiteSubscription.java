package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.ExternalIdResponse;
import com.n4systems.subscription.Subscription;

public class NetsuiteSubscription implements Subscription, ExternalIdResponse {

	private int months;
	private int users;
	private PaymentFrequency frequency;
	private String referralCode;
	private boolean purchasingPhoneSupport;
	private Long netsuiteRecordId;
	private Long nsrecordid;

	public Long getExternalId() {
		return getNsrecordid();
	}
	
	public void setExternalId(Long externalId) {
		setNetsuiteRecordId(externalId);
	}
	
	public Long getNetsuiteRecordId() {
		return netsuiteRecordId;
	}

	public void setNetsuiteRecordId(Long netsuiteRecordId) {
		this.netsuiteRecordId = netsuiteRecordId;
	}

	public Long getNsrecordid() {
		return nsrecordid;
	}

	public void setNsrecordid(Long nsrecordid) {
		this.nsrecordid = nsrecordid;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public int getUsers() {
		return users;
	}

	public void setUsers(int users) {
		this.users = users;
	}

	public PaymentFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(PaymentFrequency frequency) {
		this.frequency = frequency;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public boolean isPurchasingPhoneSupport() {
		return purchasingPhoneSupport;
	}

	public void setPurchasingPhoneSupport(boolean purchasingPhoneSupport) {
		this.purchasingPhoneSupport = purchasingPhoneSupport;
	}
	
}
