package com.n4systems.persistence.loaders;

import com.n4systems.model.signuppackage.ContractPricingByExternalIdLoader;
import com.n4systems.model.signuppackage.SignUpPackageListLoader;
import com.n4systems.model.signuppackage.SignUpPackageLoader;
import com.n4systems.model.tenant.TenantUniqueAvailableNameLoader;

public class NonSecureLoaderFactory {

	/* 
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods to this factory.
	 */

	
	public  ContractPricingByExternalIdLoader createContractPricingByNsRecordIdLoader() {
		return new ContractPricingByExternalIdLoader();
	}
	
	public  TenantUniqueAvailableNameLoader createTenantUniqueAvailableNameLoader() {
		return new TenantUniqueAvailableNameLoader();
	}
	
	public  SignUpPackageLoader createSignUpPackageLoader() {
		return new SignUpPackageLoader();
	}
	public  SignUpPackageListLoader createSignUpPackageListLoader() {
		return new SignUpPackageListLoader();
	}
}
