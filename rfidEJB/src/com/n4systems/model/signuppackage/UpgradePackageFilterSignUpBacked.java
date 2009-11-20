package com.n4systems.model.signuppackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpgradePackageFilterSignUpBacked extends UpgradePackageFilter {

	private final SignUpPackageDetails currentPackage;

	protected UpgradePackageFilterSignUpBacked(SignUpPackageDetails currentPackage) {
		this.currentPackage = currentPackage;
	}

	protected UpgradePackageFilterSignUpBacked(SignUpPackage currentPackage) {
		this(currentPackage.getSignPackageDetails());
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

	
}
