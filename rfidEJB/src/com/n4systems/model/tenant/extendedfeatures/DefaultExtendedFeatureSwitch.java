package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.TenantOrganization;

public class DefaultExtendedFeatureSwitch extends ExtendedFeatureSwitch {

	
	protected DefaultExtendedFeatureSwitch(TenantOrganization tenant, PersistenceManager persistenceManager, ExtendedFeature feature) {
		super(tenant, persistenceManager, feature);
	}

	@Override
	protected void featureSetup() {
	}

	@Override
	protected void featureTearDown() {
	}
}
