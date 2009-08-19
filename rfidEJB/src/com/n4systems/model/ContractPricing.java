package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.AbstractEntity;

@Entity
@Table(name = "contractpricings")
public class ContractPricing extends AbstractEntity implements Saveable {
	private static final long serialVersionUID = 1L;
	
	private String netsuiteRecordId;
	private String contractLength; // in months
	private String price; // per user per month
	
	@ManyToOne(optional=false)
	private SignupPackage signupPackage;

	public String getNetsuiteRecordId() {
		return netsuiteRecordId;
	}
	public void setNetsuiteRecordId(String netsuiteRecordId) {
		this.netsuiteRecordId = netsuiteRecordId;
	}
	public String getContractLength() {
		return contractLength;
	}
	public void setContractLength(String contractLength) {
		this.contractLength = contractLength;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public SignupPackage getSignupPackage() {
		return signupPackage;
	}
	public void setSignupPackage(SignupPackage signupPackage) {
		this.signupPackage = signupPackage;
	}
}
