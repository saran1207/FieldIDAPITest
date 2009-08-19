package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public abstract class ExtendedFeatureSwitch {
	
	protected final PrimaryOrg primaryOrg;
	protected final ExtendedFeature feature;
	
	protected ExtendedFeatureSwitch(PrimaryOrg primaryOrg, ExtendedFeature feature) {
		this.primaryOrg = primaryOrg;
		this.feature = feature;
	}
	
	protected abstract void featureSetup();
	protected abstract void featureTearDown();
	
	public final void enableFeature() {
		if (!primaryOrg.hasExtendedFeature(feature)) {
			featureSetup();
			addFeatureToTenant(feature);
		}
	}
	
	protected void addFeatureToTenant(ExtendedFeature feature) {
		primaryOrg.getExtendedFeatures().add(feature);
	}
	
	public final void disableFeature() {
		if (primaryOrg.hasExtendedFeature(feature)) {
			featureTearDown();
			removeFeatureFromTenant(feature);
		}
	}

	protected void removeFeatureFromTenant(ExtendedFeature feature) {
		primaryOrg.getExtendedFeatures().remove(feature);
	}
	
}
