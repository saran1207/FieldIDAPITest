package com.n4systems.model.signuppackage;

import java.util.List;




public abstract class UpgradePackageFilter {
	
	public static UpgradePackageFilter createUpgradePackageFilter(ContractPricing contractPricing) {
		if (contractPricing == ContractPricing.getLegacyContractPricing() ) {
			return new LegacyUpgradePackageFilter();
		}
		
		return new UpgradePackageFilterSignUpBacked(contractPricing);
	}
	
	
	public static UpgradePackageFilter createNullUpgradePackageFilter() {
		return new LegacyUpgradePackageFilter();
	}
	
	public boolean isLegacy() {
		return false;
	}
	
	
	public abstract List<SignUpPackage> reduceToAvailablePackages(List<SignUpPackage> allFullPackages);

	public abstract String getPackageName();
	
	public abstract boolean isUpgradable();

	public abstract ContractPricing getUpgradeContractForPackage(SignUpPackage upgradePackage);

	public abstract SignUpPackage getCurrentPackage(List<SignUpPackage> allFullPackages);

	public abstract ContractPricing getCurrentContract();

	public abstract Float getCurrentCostPerUserPerMonth();
		
	
}
