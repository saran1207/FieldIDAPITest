package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.SubscriptionDetails;

public class NetsuiteSubscriptionDetails implements SubscriptionDetails {

	private Long tenantid;
	private Long subscriptionitem;
	private String accounttype;
	private String startdate;
	private String enddate;
	private String nextbillingdate;
	private String storage;
	private String phonesupport;
	
	public void setTenantid(Long tenantid) {
		this.tenantid = tenantid;
	}

	public void setSubscriptionitem(Long subscriptionitem) {
		this.subscriptionitem = subscriptionitem;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public void setNextbillingdate(String nextbillingdate) {
		this.nextbillingdate = nextbillingdate;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public void setPhonesupport(String phonesupport) {
		this.phonesupport = phonesupport;
	}

	public String getAccountType() {
		return accounttype;
	}

	public Long getContractId() {
		return subscriptionitem;
	}

	public String getEndDate() {
		return enddate;
	}

	public String getNextBillingDate() {
		return nextbillingdate;
	}

	public String getPhoneSupport() {
		return phonesupport;
	}

	public String getStartDate() {
		return startdate;
	}

	public String getStorage() {
		return storage;
	}
	
	public Long getTenantId() {
		return tenantid;
	}

}
