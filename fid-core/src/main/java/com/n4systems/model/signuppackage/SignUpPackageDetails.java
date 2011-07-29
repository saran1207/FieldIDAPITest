package com.n4systems.model.signuppackage;

import java.util.Arrays;
import java.util.List;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.api.Listable;

public enum SignUpPackageDetails implements Listable<String> {

	Free("FIDFREE", "Free"),

	Basic("FIDBASIC", "Basic", ExtendedFeature.EmailAlerts),

	Plus("FIDPLUS", "Plus", ExtendedFeature.Branding, ExtendedFeature.EmailAlerts, ExtendedFeature.Projects),

	Enterprise("FIDENTERPRISE", "Enterprise", ExtendedFeature.EmailAlerts, ExtendedFeature.Projects, ExtendedFeature.Branding),

	Unlimited("FIDUNLIMITED", "Unlimited", ExtendedFeature.EmailAlerts, ExtendedFeature.Projects, ExtendedFeature.Branding);

	public static SignUpPackageDetails retrieveBySyncId(String syncId) {
		if (syncId == null) {
			return getLegacyPackage();
		}

		for (SignUpPackageDetails signUpPackageDetails : SignUpPackageDetails.values()) {
			if (signUpPackageDetails.getSyncId().equals(syncId)) {
				return signUpPackageDetails;
			}
		}

		return null;
	}

	public static SignUpPackageDetails getLegacyPackage() {
		return null;
	}

	private static final SignUpPackageDetails PREFERRED_PACKAGE = SignUpPackageDetails.Enterprise;
	private String syncId;
	private String name;

	private ExtendedFeature[] extendedFeatures;

	private SignUpPackageDetails(String syncId, String name, ExtendedFeature... extendedFeatures) {
		this.syncId = syncId;
		this.name = name;
		this.extendedFeatures = extendedFeatures;
	}

	public String getSyncId() {
		return syncId;
	}

	public String getName() {
		return name;
	}

	public ExtendedFeature[] getExtendedFeatures() {
		return extendedFeatures;
	}

	public List<ExtendedFeature> getFeatureList() {
		return Arrays.asList(extendedFeatures);
	}

	public boolean isPreferred() {
		return this == PREFERRED_PACKAGE;
	}

	public boolean includesFeature(ExtendedFeature feature) {
		for (ExtendedFeature includedFeature : extendedFeatures) {
			if (feature == includedFeature) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getId() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}
}
