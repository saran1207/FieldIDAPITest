package com.n4systems.handlers.creator.signup;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.persistence.Transaction;

public class UpgradeHandlerImpl implements UpgradeHandler {

	private final PrimaryOrg primaryOrg;
	private final OrgSaver orgSaver;
	
	public UpgradeHandlerImpl(PrimaryOrg primaryOrg, OrgSaver orgSaver) {
		this.primaryOrg = primaryOrg;
		this.orgSaver = orgSaver;
	}

	public void upgradeTo(SignUpPackageDetails upgradePackage, Transaction transaction) {
		enableFeatures(upgradePackage, transaction);
		adjustLimits(upgradePackage);
		saveOrg(transaction);
	}

	private void adjustLimits(SignUpPackageDetails upgradePackage) {
		Long upgradeAssetLimit = upgradePackage.getAssets();
		TenantLimit currentLimits = primaryOrg.getLimits();
		
		if (!currentLimits.isAssetLimitGreaterThan(upgradeAssetLimit))
			currentLimits.setAssets(upgradeAssetLimit);
	}

	private void enableFeatures(SignUpPackageDetails upgradePackage, Transaction transaction) {
		for (ExtendedFeature feature : upgradePackage.getExtendedFeatures()) {
			ExtendedFeatureSwitch featureSwitch = getSwitchFor(feature);
			featureSwitch.enableFeature(transaction);
		}
	}
	
	protected ExtendedFeatureSwitch getSwitchFor(ExtendedFeature feature) {
		return ExtendedFeatureFactory.getSwitchFor(feature, getPrimaryOrg());
	}

	private void saveOrg(Transaction transaction) {
		orgSaver.update(transaction, primaryOrg);
	}
	
	

	protected PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}
	

}
