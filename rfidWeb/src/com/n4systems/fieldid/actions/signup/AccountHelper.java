package com.n4systems.fieldid.actions.signup;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageListLoader;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.SubscriptionAgent;

public class AccountHelper {

	private SubscriptionAgent subscriptionAgent;
	private UpgradePackageFilter currentPackageFilter;
	private PrimaryOrg primaryOrg;
	private SignUpPackageListLoader packageLoader;
	
	public AccountHelper(SubscriptionAgent subscriptionAgent, PrimaryOrg primaryOrg, SignUpPackageListLoader packageLoader) {
		this.subscriptionAgent = subscriptionAgent;
		this.primaryOrg = primaryOrg;
		this.packageLoader = packageLoader;
	}


	public UpgradePackageFilter currentPackageFilter() {
		if (currentPackageFilter == null) {
			try {
				currentPackageFilter = UpgradePackageFilter.createUpgradePackageFilter(getCurrentContact());
			} catch (Exception e) {
				return UpgradePackageFilter.createNullUpgradePackageFilter();
			}
		}	
		
		return currentPackageFilter;
	}


	private ContractPricing getCurrentContact() throws CommunicationException {
		Long contractId = subscriptionAgent.contractIdFor(primaryOrg.getExternalId());
		for (SignUpPackage signUpPackage : packageLoader.load()) {
			for (ContractPricing contract : signUpPackage.getPaymentOptions()) {
				if (contract.getExternalId().equals(contractId)) {
					return contract;
				}
			}
			
		}
		return ContractPricing.getLegacyContractPricing();
	}
	
}
