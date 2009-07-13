package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.TenantOrganization;

public class ExtendedFeatureFactory {

	public static ExtendedFeatureSwitch getSwitchFor(ExtendedFeature feature, TenantOrganization tenant, PersistenceManager persistenceManager) {
		switch (feature) {
			case PartnerCenter:
				return new PartnerCenterSwitch(tenant, persistenceManager);
			case JobSites:
				return new JobSiteSwitch(tenant, persistenceManager);
			case EmailAlerts:
				return new EmailAlertsSwitch(tenant, persistenceManager);
			default:
				return new DefaultExtendedFeatureSwitch(tenant, persistenceManager, feature);
		}
	}
}
