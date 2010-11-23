package com.n4systems.model.builders;

import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.subscription.PaymentOption;

public class ContractPricingBuilder extends BaseBuilder<ContractPricing> {

	private final Long externalId;
	private final SignUpPackageDetails signUpPackage;
	private final PaymentOption paymentOption;
	private final Float pricePerUserPerMonth;

	public static ContractPricingBuilder aContractPricing() {
		return new ContractPricingBuilder(-1L, SignUpPackageDetails.Basic, PaymentOption.preferredOption(), 100F);
	}

    public ContractPricingBuilder(Long externalId,
			SignUpPackageDetails signUpPackage, PaymentOption paymentOption, Float pricePerUserPerMonth) {
		this.externalId = externalId;
		this.signUpPackage = signUpPackage;
		this.paymentOption = paymentOption;
		this.pricePerUserPerMonth = pricePerUserPerMonth;
	}
	
	public ContractPricingBuilder withExternalId(Long externalId) {
		return new ContractPricingBuilder(externalId, signUpPackage, paymentOption, pricePerUserPerMonth);
	}
	
	public ContractPricingBuilder withPackage(SignUpPackageDetails signUpPackage) {
		return new ContractPricingBuilder(externalId, signUpPackage, paymentOption, pricePerUserPerMonth);
	}

	public ContractPricingBuilder withPaymentOption(PaymentOption paymentOption) {
		return new ContractPricingBuilder(externalId, signUpPackage, paymentOption, pricePerUserPerMonth);
	}
	
	public ContractPricingBuilder withPricePerUserPerMonth(Float pricePerUserPerMonth) {
		return new ContractPricingBuilder(externalId, signUpPackage, paymentOption, pricePerUserPerMonth);
	}
	
	@Override
	public ContractPricing createObject() {
		ContractPricing contract = new ContractPricing();
		
		contract.setId(getId());
		contract.setExternalId(externalId);
		contract.setSignUpPackage(signUpPackage);
		contract.setPaymentOption(paymentOption);
		contract.setPricePerUserPerMonth(pricePerUserPerMonth);
		return contract;
	}


	


	

}
