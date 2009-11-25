package com.n4systems.model.builders;

import java.util.Arrays;
import java.util.List;

import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;

public class SignUpPackageBuilder extends BaseBuilder<SignUpPackage> {

	private SignUpPackageDetails signUpPackageDetail;
	
	private List<ContractPricing> contracts;
	
	public static SignUpPackageBuilder createSignUpPackage(SignUpPackageDetails signUpPackageDetail) {
		return new SignUpPackageBuilder(signUpPackageDetail, new ContractPricing[]{});
	}

	
	public static SignUpPackageBuilder createSignUpPackageWithSimpleContract(SignUpPackageDetails signUpPackageDetail, long contractId) {
		ContractPricing contract = new ContractPricing();
		contract.setExternalId(contractId);
		contract.setSignUpPackage(signUpPackageDetail);
		return new SignUpPackageBuilder(signUpPackageDetail, contract);
	}
	
	private SignUpPackageBuilder(SignUpPackageDetails signUpPackageDetail, ContractPricing... contracts) {
		super();
		this.signUpPackageDetail = signUpPackageDetail;
		this.contracts = Arrays.asList(contracts);
		
	}
	
	public SignUpPackageBuilder withContracts(ContractPricing... contracts) {
		return new SignUpPackageBuilder(signUpPackageDetail, contracts);
	}


	@Override
	public SignUpPackage build() {
		return new SignUpPackage(signUpPackageDetail, contracts);
	}
	
	
	
}
