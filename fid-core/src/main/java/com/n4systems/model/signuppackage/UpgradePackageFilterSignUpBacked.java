package com.n4systems.model.signuppackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpgradePackageFilterSignUpBacked extends UpgradePackageFilter {

	private final SignUpPackageDetails currentPackage;
	private final ContractPricing currentContract;

	protected UpgradePackageFilterSignUpBacked(ContractPricing currentContract) {
		this.currentContract = currentContract;
		this.currentPackage = currentContract.getSignUpPackage();
	} 
	
	protected List<SignUpPackageDetails> availablePackages() {
		List<SignUpPackageDetails> listOfPackageDetails = Arrays.asList(SignUpPackageDetails.values());
		int currentPackageIndex = listOfPackageDetails.indexOf(currentPackage);
		return listOfPackageDetails.subList(currentPackageIndex + 1, listOfPackageDetails.size()); 
	}

	public List<SignUpPackage> reduceToAvailablePackages(List<SignUpPackage> allFullPackages) {
		List<SignUpPackage> filteredPackages = new ArrayList<SignUpPackage>();
		for (SignUpPackage signUpPackage : allFullPackages) {
			if (availablePackages().contains(signUpPackage.getSignPackageDetails())) {
				filteredPackages.add(signUpPackage);
			}
		}
		return filteredPackages;
	}

	public String getPackageName() {
		return currentPackage.getName();
	}

	public boolean isUpgradable() {
		return !availablePackages().isEmpty();
	}

	@Override
	public ContractPricing getUpgradeContractForPackage(SignUpPackage upgradePackage) {
		return upgradePackage.getContract(currentContract.getPaymentOption());
	}

	public SignUpPackage getCurrentPackage(List<SignUpPackage> allFullPackages) {
		
		for (SignUpPackage signUpPackage : allFullPackages) {
			if (signUpPackage.hasContract(currentContract)) {
				return signUpPackage;
			}
		}
		return null;
	}
	
	public ContractPricing getCurrentContract() {
		return currentContract;
	}

	@Override
	public Float getCurrentCostPerUserPerMonth() {
		return currentContract.getPricePerUserPerMonth();
	}
	
	
	

	
}
