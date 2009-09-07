package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class ExtendedFeatureFactory {

	public static ExtendedFeatureSwitch getSwitchFor(ExtendedFeature feature, PrimaryOrg primaryOrg) {
		switch (feature) {
			case PartnerCenter:
				return new PartnerCenterSwitch(primaryOrg);
			case EmailAlerts:
				return new EmailAlertsSwitch(primaryOrg);
			default:
				return new DefaultExtendedFeatureSwitch(primaryOrg, feature);
		}
	}
}
