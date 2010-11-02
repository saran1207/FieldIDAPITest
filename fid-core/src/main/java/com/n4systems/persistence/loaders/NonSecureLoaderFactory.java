package com.n4systems.persistence.loaders;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.downloadlink.AllDownloadLinksByDateLoader;
import com.n4systems.model.inspection.EventBySubEventLoader;
import com.n4systems.model.asset.LastInspectionDateLoader;
import com.n4systems.model.safetynetwork.OrgConnectionExistsLoader;
import com.n4systems.model.signuppackage.ContractPricingByExternalIdLoader;
import com.n4systems.model.signuppackage.SignUpPackageListLoader;
import com.n4systems.model.signuppackage.SignUpPackageLoader;

public class NonSecureLoaderFactory {

	/*
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods
	 * to this factory.
	 */

	public AllDownloadLinksByDateLoader createAllDownloadLinksByDateLoader() {
		return new AllDownloadLinksByDateLoader();
	}

	public ContractPricingByExternalIdLoader createContractPricingByNsRecordIdLoader() {
		return new ContractPricingByExternalIdLoader();
	}

	public EventBySubEventLoader createInspectionBySubInspectionLoader() {
		return new EventBySubEventLoader();
	}

	public LastInspectionDateLoader createLastInspectionDateLoader(Long networkId) {
		return new LastInspectionDateLoader();
	}

	public <T extends BaseEntity> NonSecureIdLoader<T> createNonSecureIdLoader(Class<T> clazz) {
		return new NonSecureIdLoader<T>(clazz);
	}
	
	public OrgConnectionExistsLoader createOrgConnectionExistsLoader() {
		return new OrgConnectionExistsLoader();
	}
	
	public SignUpPackageListLoader createSignUpPackageListLoader() {
		return new SignUpPackageListLoader();
	}
	
	public SignUpPackageLoader createSignUpPackageLoader() {
		return new SignUpPackageLoader();
	}
}
