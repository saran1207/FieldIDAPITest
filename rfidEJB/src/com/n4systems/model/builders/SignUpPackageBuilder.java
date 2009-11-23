package com.n4systems.model.builders;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;

public class SignUpPackageBuilder extends BaseBuilder<SignUpPackage> {

	private SignUpPackageDetails signUpPackageDetail;
	
	private ContractPricing contract;
	
	public static SignUpPackageBuilder createSignUpPackage(SignUpPackageDetails signUpPackageDetail) {
		return new SignUpPackageBuilder(signUpPackageDetail, null);
	}

	
	public static SignUpPackageBuilder createSignUpPackageWithSimpleContract(SignUpPackageDetails signUpPackageDetail, long contractId) {
		ContractPricing contract = new ContractPricing();
		contract.setExternalId(contractId);
		contract.setSignUpPackage(signUpPackageDetail);
		return new SignUpPackageBuilder(signUpPackageDetail, contract);
	}
	
	private SignUpPackageBuilder(SignUpPackageDetails signUpPackageDetail, ContractPricing contract) {
		super();
		this.signUpPackageDetail = signUpPackageDetail;
		this.contract = contract;
		
	}
	
	public SignUpPackageBuilder withContract(ContractPricing contract) {
		return new SignUpPackageBuilder(signUpPackageDetail, contract);
	}


	@Override
	public SignUpPackage build() {
		
		
		List<ContractPricing> contracts = new ArrayList<ContractPricing>();
		contracts.add(contract);
		
		return new SignUpPackage(signUpPackageDetail, contracts);
	}
	
	
	
}
