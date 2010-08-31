package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public class ExtendedFeatureSwitchTestDouble extends ExtendedFeatureSwitch {
	
	
	public int callsToFeatureSetup = 0;
	public int callsToFeatureTearDown = 0;
	
	protected ExtendedFeatureSwitchTestDouble(PrimaryOrg primaryOrg, ExtendedFeature feature) {
		super(primaryOrg, feature);
	}

	@Override
	protected void featureSetup(Transaction transaction) {
		if (transaction == null) throw new RuntimeException("transaction can not be null");
		
		callsToFeatureSetup++;
	}

	@Override
	protected void featureTearDown(Transaction transaction) {
		if (transaction == null) throw new RuntimeException("transaction can not be null");
		
		callsToFeatureTearDown++;
	}

}
