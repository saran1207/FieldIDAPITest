package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public abstract class ExtendedFeatureSwitch {
	
	protected final PrimaryOrg primaryOrg;
	protected final ExtendedFeature feature;
	
	protected ExtendedFeatureSwitch(PrimaryOrg primaryOrg, ExtendedFeature feature) {
		this.primaryOrg = primaryOrg;
		this.feature = feature;
	}
	
	protected abstract void featureSetup(Transaction transaction);
	protected abstract void featureTearDown(Transaction transaction);
	
	public final void enableFeature(Transaction transaction) {
		if (!primaryOrg.hasExtendedFeature(feature)) {
			featureSetup(transaction);
			addFeatureToTenant(feature);
		}
	}
	
	protected void addFeatureToTenant(ExtendedFeature feature) {
		primaryOrg.getExtendedFeatures().add(feature);
	}
	
	public final void disableFeature(Transaction transaction) {
		if (primaryOrg.hasExtendedFeature(feature)) {
			featureTearDown(transaction);
			removeFeatureFromTenant(feature);
		}
	}

	protected void removeFeatureFromTenant(ExtendedFeature feature) {
		primaryOrg.getExtendedFeatures().remove(feature);
	}
	
}
