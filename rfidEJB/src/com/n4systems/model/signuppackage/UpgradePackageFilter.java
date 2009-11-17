package com.n4systems.model.signuppackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpgradePackageFilter {

	private final SignUpPackageDetails currentPackage;
	
	public UpgradePackageFilter(SignUpPackageDetails currentPackage) {
		this.currentPackage = currentPackage;
	}
	
	public UpgradePackageFilter(SignUpPackage currentPackage) {
		this.currentPackage = currentPackage.getSignPackageDetails();
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
	

}
