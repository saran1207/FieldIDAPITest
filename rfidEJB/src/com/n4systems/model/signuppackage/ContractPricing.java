package com.n4systems.model.signuppackage;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.subscription.PaymentOption;

@Entity
@Table(name = "contractpricings")
public class ContractPricing extends AbstractEntity implements Saveable, Comparable<ContractPricing> {
	private static final long serialVersionUID = 1L;
	
	private Long externalId;
	private Float pricePerUserPerMonth;
	
	@Enumerated(EnumType.STRING)
	private PaymentOption paymentOption;
	
	@Enumerated(EnumType.STRING)
	private SignUpPackageDetails signUpPackage;

	
	public static ContractPricing getLegacyContractPricing() {
		return null;
	}
	
	public Long getExternalId() {
		return externalId;
	}
	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}
	public Float getPricePerUserPerMonth() {
		return pricePerUserPerMonth;
	}
	public void setPricePerUserPerMonth(Float pricePerUserPerMonth) {
		this.pricePerUserPerMonth = pricePerUserPerMonth;
	}
	public PaymentOption getPaymentOption() {
		return paymentOption;
	}
	public void setPaymentOption(PaymentOption paymentOption) {
		this.paymentOption = paymentOption;
	}
	public SignUpPackageDetails getSignUpPackage() {
		return signUpPackage;
	}
	public void setSignUpPackage(SignUpPackageDetails signUpPackage) {
		this.signUpPackage = signUpPackage;
	}
	public int compareTo(ContractPricing o) {
		return paymentOption.compareTo(o.getPaymentOption());
	}
	
	
	@Override
	public String toString() {
		return "ContractPricing [externalId=" + externalId + ", paymentOption="
				+ paymentOption + ", pricePerUserPerMonth="
				+ pricePerUserPerMonth + ", signUpPackage=" + signUpPackage
				+ "]";
	}

}
