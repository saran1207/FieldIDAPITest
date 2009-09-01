package com.n4systems.model.signuppackage;

import java.util.List;

public class SignUpPackage {
	private SignUpPackageDetails signPackageDetails;
	private List<ContractPricing> paymentOptions;

	
	public SignUpPackage(SignUpPackageDetails signPackageDetails, List<ContractPricing> paymentOptions) {
		super();
		this.signPackageDetails = signPackageDetails;
		this.paymentOptions = paymentOptions;
	}
	
	public SignUpPackageDetails getSignPackageDetails() {
		return signPackageDetails;
	}

	public List<ContractPricing> getPaymentOptions() {
		return paymentOptions;
	}
	
}
