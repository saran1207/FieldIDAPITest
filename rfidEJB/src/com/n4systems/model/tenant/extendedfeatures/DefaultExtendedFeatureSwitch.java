package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class DefaultExtendedFeatureSwitch extends ExtendedFeatureSwitch {

	protected DefaultExtendedFeatureSwitch(PrimaryOrg primaryOrg, ExtendedFeature feature) {
		super(primaryOrg, feature);
	}

	@Override
	protected void featureSetup() {}

	@Override
	protected void featureTearDown() {}
}
