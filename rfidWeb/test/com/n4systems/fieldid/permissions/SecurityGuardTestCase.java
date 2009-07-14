package com.n4systems.fieldid.permissions;

import com.n4systems.model.ExtendedFeature;

public abstract class SecurityGuardTestCase {

	public SecurityGuardTestCase() {
		super();
	}

	protected String getFeatureMethodName(ExtendedFeature feature) {
		String featureName = feature.name();
		return "is" + featureName + "Enabled";
	}

}