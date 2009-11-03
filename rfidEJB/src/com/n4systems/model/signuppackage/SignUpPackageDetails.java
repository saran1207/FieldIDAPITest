package com.n4systems.model.signuppackage;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.tenant.LimitAdjuster;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.util.DataUnit;


public enum SignUpPackageDetails {
	
	Free("FIDFREE", "Free", 5L, 1L, 25L, 0L),
	
	Basic("FIDBASIC", "Basic", 50L, 5L, 250L, 0L, ExtendedFeature.EmailAlerts),
	
	Plus("FIDPLUS", "Plus", 1000L, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, ExtendedFeature.EmailAlerts, 
				ExtendedFeature.Projects),
																	
	Enterprise("FIDENTERPRISE", "Enterprise", 1000L, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, ExtendedFeature.EmailAlerts, 
				ExtendedFeature.Projects, ExtendedFeature.Branding, ExtendedFeature.PartnerCenter, ExtendedFeature.MultiLocation,
				ExtendedFeature.AllowIntegration),
				
	Unlimited("FIDUNLIMITED", "Unlimited", 1000L, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, ExtendedFeature.EmailAlerts, 
				ExtendedFeature.Projects, ExtendedFeature.Branding, ExtendedFeature.PartnerCenter,
				ExtendedFeature.AllowIntegration, ExtendedFeature.MultiLocation, ExtendedFeature.CustomCert, 
				ExtendedFeature.DedicatedProgramManager);
	
	
	public static SignUpPackageDetails retrieveBySyncId(String syncId) {
		for (SignUpPackageDetails signUpPackageDetails : SignUpPackageDetails.values()) {
			if (signUpPackageDetails.getSyncId().equals(syncId)) {
				return signUpPackageDetails;
			}
		}
		
		return null;
	}
	
	private static final SignUpPackageDetails PREFERRED_PACKAGE = SignUpPackageDetails.Enterprise;
	private String syncId;
	private String name;
	private Long assets;
	private Long diskSpace;
	private Long users;
	private Long secondaryOrgs;
	
	private ExtendedFeature[] extendedFeatures;
	
	private SignUpPackageDetails(String syncId, String name, Long diskSpace, Long users, Long assets, Long secondaryOrgs, ExtendedFeature...extendedFeatures) {
		this.syncId = syncId;
		this.name = name;
		this.diskSpace = diskSpace;
		this.users = users;
		this.assets = assets;
		this.secondaryOrgs = secondaryOrgs;
		this.extendedFeatures = extendedFeatures;	
	}
	
	public String getSyncId() {
		return syncId;
	}
	public String getName() {
		return name;
	}
	public Long getDiskSpaceInMB() {
		return diskSpace;
	}
	public Long getUsers() {
		return users;
	}
	public Long getAssets() {
		return assets;
	}
	public Long getSecondaryOrgs() {
		return secondaryOrgs;
	}
	public ExtendedFeature[] getExtendedFeatures() {
		return extendedFeatures;
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
	
	public LimitAdjuster getLimitAdjuster() {
		return new LimitAdjuster(assets, diskSpace, DataUnit.MEGABYTES);
	}
	
	

}
