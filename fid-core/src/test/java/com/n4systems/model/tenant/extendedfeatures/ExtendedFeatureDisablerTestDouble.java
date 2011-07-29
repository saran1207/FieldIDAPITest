package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public class ExtendedFeatureDisablerTestDouble extends ExtendedFeatureDisabler {
	public int callsToFeatureTearDown = 0;
	
	protected ExtendedFeatureDisablerTestDouble(PrimaryOrg primaryOrg, ExtendedFeature feature) {
		super(primaryOrg, feature);
	}

	@Override
	protected void disableFeature(Transaction transaction) {
		if (transaction == null) throw new RuntimeException("transaction can not be null");
		
		callsToFeatureTearDown++;
	}

}
