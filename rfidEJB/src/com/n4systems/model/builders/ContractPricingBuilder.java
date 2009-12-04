package com.n4systems.model.builders;

import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.subscription.PaymentOption;

public class ContractPricingBuilder extends BaseBuilder<ContractPricing> {

	private final Long externalId;
	private final SignUpPackageDetails signUpPackage;
	private final PaymentOption paymentOption;
	



	public static ContractPricingBuilder aContractPricing() {
		return new ContractPricingBuilder(-1L, SignUpPackageDetails.Basic, PaymentOption.preferredOption());
	}
	
	
	public ContractPricingBuilder(Long externalId,
			SignUpPackageDetails signUpPackage, PaymentOption paymentOption) {
		super();
		this.externalId = externalId;
		this.signUpPackage = signUpPackage;
		this.paymentOption = paymentOption;
	}
	
	public ContractPricingBuilder withExternalId(Long externalId) {
		return new ContractPricingBuilder(externalId, signUpPackage, paymentOption);
	}
	
	public ContractPricingBuilder withPackage(SignUpPackageDetails signUpPackage) {
		return new ContractPricingBuilder(externalId, signUpPackage, paymentOption);
	}


	public ContractPricingBuilder withPaymentOption(PaymentOption paymentOption) {
		return new ContractPricingBuilder(externalId, signUpPackage, paymentOption);
	}
	
	@Override
	public ContractPricing build() {
		ContractPricing contract = new ContractPricing();
		
		contract.setId(id);
		contract.setExternalId(externalId);
		contract.setSignUpPackage(signUpPackage);
		contract.setPaymentOption(paymentOption);
		
		return contract;
	}


	


	

}
