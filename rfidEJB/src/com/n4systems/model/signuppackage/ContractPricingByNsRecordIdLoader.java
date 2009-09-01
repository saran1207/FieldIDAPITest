package com.n4systems.model.signuppackage;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.subscription.PaymentOption;
import com.n4systems.util.persistence.QueryBuilder;

public class ContractPricingByNsRecordIdLoader extends Loader<ContractPricing> {

	private Long netsuiteRecordId;
	private SignUpPackageDetails signUpPackage;
	private PaymentOption paymentOption;
	
	@Override
	protected ContractPricing load(EntityManager em) {
		QueryBuilder<ContractPricing> builder = new QueryBuilder<ContractPricing>(ContractPricing.class);
		builder.addSimpleWhere("netsuiteRecordId", netsuiteRecordId);
		builder.addSimpleWhere("signUpPackage", signUpPackage);
		builder.addSimpleWhere("paymentOption", paymentOption);
		
		ContractPricing contractPricing = builder.getSingleResult(em);
		
		return contractPricing;
	}

	public void setNetsuiteRecordId(Long netsuiteRecordId) {
		this.netsuiteRecordId = netsuiteRecordId;
	}

	public void setSignUpPackage(SignUpPackageDetails signUpPackage) {
		this.signUpPackage = signUpPackage;
	}

	public void setPaymentOption(PaymentOption paymentOption) {
		this.paymentOption = paymentOption;
	}
}
