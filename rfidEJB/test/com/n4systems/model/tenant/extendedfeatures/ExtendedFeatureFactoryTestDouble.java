package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class ExtendedFeatureFactoryTestDouble {

	public static ExtendedFeatureSwitchTestDouble getSwitchFor(ExtendedFeature feature, PrimaryOrg primaryOrg) {
		return new ExtendedFeatureSwitchTestDouble(primaryOrg, feature);
	}
	
}
