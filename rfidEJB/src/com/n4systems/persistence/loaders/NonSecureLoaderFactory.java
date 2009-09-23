package com.n4systems.persistence.loaders;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.safetynetwork.OrgConnectionByTenantListLoader;
import com.n4systems.model.signuppackage.ContractPricingByExternalIdLoader;
import com.n4systems.model.signuppackage.SignUpPackageListLoader;
import com.n4systems.model.signuppackage.SignUpPackageLoader;
import com.n4systems.model.tenant.TenantUniqueAvailableNameLoader;

public class NonSecureLoaderFactory {

	/*
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods
	 * to this factory.
	 */

	public ContractPricingByExternalIdLoader createContractPricingByNsRecordIdLoader() {
		return new ContractPricingByExternalIdLoader();
	}

	public <T extends BaseEntity> NonSecureIdLoader<T> createNonSecureIdLoader(Class<T> clazz) {
		return new NonSecureIdLoader<T>(clazz);
	}

	public OrgConnectionByTenantListLoader createOrgConnectionByTenantListLoader() {
		return new OrgConnectionByTenantListLoader();
	}

	public SignUpPackageListLoader createSignUpPackageListLoader() {
		return new SignUpPackageListLoader();
	}

	public SignUpPackageLoader createSignUpPackageLoader() {
		return new SignUpPackageLoader();
	}

	public TenantUniqueAvailableNameLoader createTenantUniqueAvailableNameLoader() {
		return new TenantUniqueAvailableNameLoader();
	}
}
