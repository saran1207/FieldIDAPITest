package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public class ToggleExendedFeatureMethod {

	private final ExtendedFeature feature;
	private final boolean onOff;

	public ToggleExendedFeatureMethod(ExtendedFeature feature, boolean onOff) {
		this.feature = feature;
		this.onOff = onOff;
	}

	public void applyTo(PrimaryOrg primaryOrg, Transaction transaction) {
		ExtendedFeatureSwitch featureSwitchAssignedTo = switchFor(primaryOrg);
		
		if (onOff) {
			featureSwitchAssignedTo.enableFeature(transaction);
		} else {
			featureSwitchAssignedTo.disableFeature(transaction);
		}
	}

	protected ExtendedFeatureSwitch switchFor(PrimaryOrg primaryOrg) {
		return ExtendedFeatureFactory.getSwitchFor(feature, primaryOrg);
	}

}
