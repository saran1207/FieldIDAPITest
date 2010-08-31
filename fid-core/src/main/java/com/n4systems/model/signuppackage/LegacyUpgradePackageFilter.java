package com.n4systems.model.signuppackage;

import java.util.ArrayList;
import java.util.List;

public class LegacyUpgradePackageFilter extends UpgradePackageFilter {

	
	
	@Override
	public String getPackageName() {
		return "Legacy";
	}

	@Override
	public List<SignUpPackage> reduceToAvailablePackages(List<SignUpPackage> allFullPackages) {
		return new ArrayList<SignUpPackage>();
	}

	@Override 
	public boolean isLegacy() {
		return true;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public ContractPricing getUpgradeContractForPackage(SignUpPackage upgradePackage) {
		throw new CanNotUpgradeException();
	}

	public SignUpPackage getCurrentPackage(List<SignUpPackage> allFullPackages) {
		return null;
	}

	@Override
	public Float getCurrentCostPerUserPerMonth() {
		return 0F;
	}
	
	public ContractPricing getCurrentContract() {
		return null;
	}
	
	


}
