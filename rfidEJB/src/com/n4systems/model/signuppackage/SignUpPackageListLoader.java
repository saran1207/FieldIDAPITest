package com.n4systems.model.signuppackage;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.NonSecuredListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SignUpPackageListLoader extends NonSecuredListLoader<SignUpPackage>{

	@Override
	protected List<SignUpPackage> load(EntityManager em) {
		List<ContractPricing> contracts = getContracts(em);
		return produceSignUpPackages(contracts);
	}

	private List<ContractPricing> getContracts(EntityManager em) {
		QueryBuilder<ContractPricing> query = new QueryBuilder<ContractPricing>(ContractPricing.class);
		query.addOrder("signUpPackage");
		
		List<ContractPricing> contracts = query.getResultList(em);
		return contracts;
	}

	private List<SignUpPackage> produceSignUpPackages(List<ContractPricing> contracts) {
		List<SignUpPackage> packages = new ArrayList<SignUpPackage>();
		for (SignUpPackageDetails signUpPackageDetails : SignUpPackageDetails.values()) {
			List<ContractPricing> paymentOptionsForCurrentPackage = new ArrayList<ContractPricing>();
			for (ContractPricing contractPricing : contracts) {
				if (contractPricing.getSignUpPackage() == signUpPackageDetails) {
					paymentOptionsForCurrentPackage.add(contractPricing);
				}
			}
			packages.add(new SignUpPackage(signUpPackageDetails, paymentOptionsForCurrentPackage));
			
		}
		
		return packages;
	}

}
