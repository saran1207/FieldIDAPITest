package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public class ExendedFeatureToggler {

	private final ExtendedFeature feature;
	private final boolean onOff;

	public ExendedFeatureToggler(ExtendedFeature feature, boolean onOff) {
		this.feature = feature;
		this.onOff = onOff;
	}

	public void applyTo(PrimaryOrg primaryOrg, Transaction transaction) {
		if (onOff) {
			primaryOrg.getExtendedFeatures().add(feature);
		} else {
			primaryOrg.getExtendedFeatures().remove(feature);
			
			ExtendedFeatureDisabler featureDisabler = getDisabler(primaryOrg);
			if (featureDisabler != null) {
				featureDisabler.disableFeature(transaction);
			}
		}
	}

	protected ExtendedFeatureDisabler getDisabler(PrimaryOrg primaryOrg) {
		switch (feature) {
			case EmailAlerts:
				return new EmailAlertsDisabler(primaryOrg);
			case AssignedTo:
				return new AssignedToDisabler(primaryOrg);
			default:
				return null;
		}
	}

}
