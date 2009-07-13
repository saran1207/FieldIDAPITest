package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.TenantOrganization;

public abstract class ExtendedFeatureSwitch {
	
	protected final TenantOrganization tenant;
	protected final PersistenceManager persistenceManager;
	protected final ExtendedFeature feature;
	
	protected ExtendedFeatureSwitch(TenantOrganization tenant, PersistenceManager persistenceManager, ExtendedFeature feature) {
		this.tenant = tenant;
		this.persistenceManager = persistenceManager;
		this.feature = feature;
	}
	
	
	public final void enableFeature() {
		if (!tenant.hasExtendedFeature(feature)) {
			featureSetup();
			addFeatureToTenant(feature);
			updateTenant();
		}
	}
	
	protected abstract void featureSetup();
	
	protected void addFeatureToTenant(ExtendedFeature feature) {
		tenant.getExtendedFeatures().add(feature);
	}
	
	
	public final void disableFeature() {
		if (tenant.hasExtendedFeature(feature)) {
			featureTearDown();
			removeFeatureFromTenant(feature);
			updateTenant();
		}
	}

	protected abstract void featureTearDown();
	
	
	protected void removeFeatureFromTenant(ExtendedFeature feature) {
		tenant.getExtendedFeatures().remove(feature);
	}
	
	protected void updateTenant() {
		persistenceManager.update(tenant);
	}
	
}
