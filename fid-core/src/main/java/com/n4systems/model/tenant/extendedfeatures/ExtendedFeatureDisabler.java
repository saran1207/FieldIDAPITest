package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public abstract class ExtendedFeatureDisabler {
	
	protected final PrimaryOrg primaryOrg;
	protected final ExtendedFeature feature;
	
	protected ExtendedFeatureDisabler(PrimaryOrg primaryOrg, ExtendedFeature feature) {
		this.primaryOrg = primaryOrg;
		this.feature = feature;
	}

	protected abstract void disableFeature(Transaction transaction);
	
}
