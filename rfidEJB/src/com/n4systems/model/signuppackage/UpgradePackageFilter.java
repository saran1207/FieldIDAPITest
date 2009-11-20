package com.n4systems.model.signuppackage;

import java.util.List;




public abstract class UpgradePackageFilter {
	
	public static UpgradePackageFilter createUpgradePackageFilter(SignUpPackage currentPackage) {
		return createUpgradePackageFilter(currentPackage.getSignPackageDetails());
	}
	
	public static UpgradePackageFilter createUpgradePackageFilter(SignUpPackageDetails currentPackage) {
		
		if (currentPackage == SignUpPackageDetails.getLegacyPackage() ) {
			return new LegacyUpgradePackageFilter();
		}
		
		return new UpgradePackageFilterSignUpBacked(currentPackage);
	}
	
	
	public abstract List<SignUpPackage> reduceToAvailablePackages(List<SignUpPackage> allFullPackages);

	public abstract String getPackageName();
	
	public abstract boolean isUpgradable();
	
}
