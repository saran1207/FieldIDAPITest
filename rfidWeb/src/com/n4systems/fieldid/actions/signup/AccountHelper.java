package com.n4systems.fieldid.actions.signup;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.subscription.SubscriptionAgent;

public class AccountHelper {

	private SubscriptionAgent subscriptionAgent;
	private UpgradePackageFilter currentPackageFilter;
	private PrimaryOrg primaryOrg;
	
	public AccountHelper(SubscriptionAgent subscriptionAgent, PrimaryOrg primaryOrg) {
		this.subscriptionAgent = subscriptionAgent;
		this.primaryOrg = primaryOrg;
	}


	public UpgradePackageFilter currentPackageFilter() {
		if (currentPackageFilter == null) {
			try {
				SignUpPackageDetails currentPackage = SignUpPackageDetails.retrieveBySyncId(subscriptionAgent.currentPackageFor(primaryOrg.getExternalId()));
				currentPackageFilter = UpgradePackageFilter.createUpgradePackageFilter(currentPackage);
			} catch (Exception e) {
				return null;
			}
		}	
		
		return currentPackageFilter;
	}
	
}
