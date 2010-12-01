package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class ExtendedFeatureFactory {

	public static ExtendedFeatureSwitch getSwitchFor(ExtendedFeature feature, PrimaryOrg primaryOrg) {
		switch (feature) {
			case ReadOnlyUser:
				return new ReadOnlyUserSwitch(primaryOrg);
			case EmailAlerts:
				return new EmailAlertsSwitch(primaryOrg);
			case AssignedTo:
				return new AssignedToSwitch(primaryOrg);
			default:
				return new DefaultExtendedFeatureSwitch(primaryOrg, feature);
		}
	}
}
