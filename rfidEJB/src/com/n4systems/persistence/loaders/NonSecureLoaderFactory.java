package com.n4systems.persistence.loaders;

import com.n4systems.model.signuppackage.ContractPricingByNsRecordIdLoader;
import com.n4systems.model.signuppackage.SignUpPackageListLoader;
import com.n4systems.model.signuppackage.SignUpPackageLoader;
import com.n4systems.model.tenant.TenantUniqueAvailableNameLoader;

public class NonSecureLoaderFactory {

	/* 
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods to this factory.
	 */

	
	public static ContractPricingByNsRecordIdLoader createContractPricingByNsRecordIdLoader() {
		return new ContractPricingByNsRecordIdLoader();
	}
	
	public static TenantUniqueAvailableNameLoader createTenantUniqueAvailableNameLoader() {
		return new TenantUniqueAvailableNameLoader();
	}
	
	public static SignUpPackageLoader createSignUpPackageLoader() {
		return new SignUpPackageLoader();
	}
	public static SignUpPackageListLoader createSignUpPackageListLoader() {
		return new SignUpPackageListLoader();
	}
}
