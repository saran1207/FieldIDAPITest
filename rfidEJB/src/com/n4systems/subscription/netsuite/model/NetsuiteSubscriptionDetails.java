package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.SubscriptionDetails;

public class NetsuiteSubscriptionDetails implements SubscriptionDetails {

	private Long tenantid;
	private Long itemid;
	private String accounttype;
	private String startdate;
	private String enddate;
	private String nextbillingdate;
	private String storage;
	private String phonesupport;
	private String fieldid;
	private Boolean payviacc;
	private Boolean cconfile;
	private Long contractlength;
	
	public void setTenantid(Long tenantid) {
		this.tenantid = tenantid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
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
		return itemid;
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
	public boolean getPhoneSupportAsBoolean() {
		return  (phonesupport != null && phonesupport.equalsIgnoreCase("yes"));
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

	public String getFieldid() {
		return fieldid;
	}

	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
	}

	public Boolean getPayviacc() {
		return payviacc;
	}

	public void setPayviacc(Boolean payviacc) {
		this.payviacc = payviacc;
	}

	public Boolean getCconfile() {
		return cconfile;
	}

	public void setCconfile(Boolean cconfile) {
		this.cconfile = cconfile;
	}

	public Long getContractlength() {
		return contractlength;
	}

	public void setContractlength(Long contractlength) {
		this.contractlength = contractlength;
	}
}
