package com.n4systems.model.signuppackage;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.tenant.TenantLimit;


public enum SignUpPackageDetails {
	
	Free("FIDFREE", "Free", 5L, 1L, 5L),
	
	Basic("FIDBASIC", "Basic", 50L, 5L, 250L, ExtendedFeature.EmailAlerts),
	
	Plus("FIDPLUS", "Plus", 1000L, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, ExtendedFeature.EmailAlerts, 
																	ExtendedFeature.Projects),
																	
	Enterprise("FIDENTERPRISE", "Enterprise", 1000L, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, ExtendedFeature.EmailAlerts, 
				ExtendedFeature.Projects, ExtendedFeature.Branding, ExtendedFeature.Compliance, ExtendedFeature.PartnerCenter),
				
	Unlimited("FIDUNLIMITED", "Unlimited", 1000L, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, ExtendedFeature.EmailAlerts, 
				ExtendedFeature.Projects, ExtendedFeature.Branding, ExtendedFeature.Compliance, ExtendedFeature.PartnerCenter);
	
	private String syncId;
	private String name;
	private Long diskSpace;
	private Long users;
	private Long assets;
	private ExtendedFeature[] extendedFeatures;
	
	private SignUpPackageDetails(String syncId, String name, Long diskSpace, Long users, Long assets, ExtendedFeature...extendedFeatures) {
		this.syncId = syncId;
		this.name = name;
		this.diskSpace = diskSpace;
		this.users = users;
		this.assets = assets;
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
	public ExtendedFeature[] getExtendedFeatures() {
		return extendedFeatures;
	}
	
	
}
