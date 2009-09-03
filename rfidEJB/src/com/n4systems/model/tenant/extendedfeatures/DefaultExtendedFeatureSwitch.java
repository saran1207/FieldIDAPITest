package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public class DefaultExtendedFeatureSwitch extends ExtendedFeatureSwitch {

	protected DefaultExtendedFeatureSwitch(PrimaryOrg primaryOrg, ExtendedFeature feature) {
		super(primaryOrg, feature);
	}

	@Override
	protected void featureSetup(Transaction transaction) {}

	@Override
	protected void featureTearDown(Transaction transaction) {}
}
