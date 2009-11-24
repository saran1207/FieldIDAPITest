package com.n4systems.handlers.creator.signup;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.util.DataUnit;

public class UpgradeHandlerImpl implements UpgradeHandler {

	private final PrimaryOrg primaryOrg;
	private final OrgSaver orgSaver;
	private final SubscriptionAgent subscriptionAgent;
	
	public UpgradeHandlerImpl(PrimaryOrg primaryOrg, OrgSaver orgSaver, SubscriptionAgent subscriptionAgent) {
		this.primaryOrg = primaryOrg;
		this.orgSaver = orgSaver;
		this.subscriptionAgent = subscriptionAgent;
	}


	public boolean upgradeTo(UpgradeRequest upgradeRequest, Transaction transaction) {
		if (confirmUpgrade(upgradeRequest)) {
			enableFeatures(upgradeRequest.getUpgradePackage(), transaction);
			adjustLimits(upgradeRequest.getUpgradePackage());
			saveOrg(transaction);
			return true;
		}
		return false;
	}


	private boolean confirmUpgrade(UpgradeRequest upgradeRequest) {
		
		try {
			return subscriptionAgent.upgrade(upgradeRequest);
		} catch (CommunicationException e) {
			return false;
		}
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
	
	
	private void adjustLimits(SignUpPackageDetails upgradePackage) {
		TenantLimit currentLimits = primaryOrg.getLimits();
		
		adjustAssetLimit(upgradePackage, currentLimits);
		adjustDiskSpaceLimit(upgradePackage, currentLimits);
	}

	
	private void adjustDiskSpaceLimit(SignUpPackageDetails upgradePackage, TenantLimit currentLimits) {
		Long packageDiskLimitInBytes = DataUnit.MEBIBYTES.convertTo(upgradePackage.getDiskSpaceInMB(), DataUnit.BYTES);
		
		if (!currentLimits.isDiskLimitInBytesGreaterThan(packageDiskLimitInBytes)) {
			currentLimits.setDiskSpaceInBytes(packageDiskLimitInBytes);
		}
	}

	
	private void adjustAssetLimit(SignUpPackageDetails upgradePackage, TenantLimit currentLimits) {
		Long upgradeAssetLimit = upgradePackage.getAssets();
		if (!currentLimits.isAssetLimitGreaterThan(upgradeAssetLimit))
			currentLimits.setAssets(upgradeAssetLimit);
	}


	private void saveOrg(Transaction transaction) {
		orgSaver.update(transaction, primaryOrg);
	}
	

	protected PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}


	@Override
	public UpgradeCost priceForUpgrade(UpgradeRequest upgradeRequest) throws CommunicationException {
		return subscriptionAgent.costToUpgradeTo(upgradeRequest);
	}


	
	

}
