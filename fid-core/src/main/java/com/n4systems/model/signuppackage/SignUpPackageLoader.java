package com.n4systems.model.signuppackage;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class SignUpPackageLoader extends Loader<SignUpPackage> {

	
	private SignUpPackageDetails signUpPackageTarget;
	
	@Override
	public SignUpPackage load(EntityManager em) {
		QueryBuilder<ContractPricing> query = new QueryBuilder<ContractPricing>(ContractPricing.class, new OpenSecurityFilter());
		query.addSimpleWhere("signUpPackage", signUpPackageTarget);
		List<ContractPricing> contracts = query.getResultList(em);
		return new SignUpPackage(signUpPackageTarget, contracts);
	}

	public SignUpPackageLoader setSignUpPackageTarget(SignUpPackageDetails signUpPackageTarget) {
		this.signUpPackageTarget = signUpPackageTarget;
		return this;
	}
	
	

}
