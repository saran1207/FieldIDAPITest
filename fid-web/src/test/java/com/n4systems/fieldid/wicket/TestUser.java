package com.n4systems.fieldid.wicket;

import java.util.Set;

import com.google.common.collect.Sets;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.security.Permissions;

public enum TestUser {

	NO_PERMISSIONS_USER(Permissions.NO_PERMISSIONS), 
	ALL_PERMISSIONS_USER(Permissions.ALL), 
	ALL_EVENT_PERMISSIONS_USER(Permissions.ALLEVENT), 
	CUSTOMER_PERMISSIONS_USER(Permissions.CUSTOMER), 
	SYSTEM_PERMISSIONS_USER(Permissions.SYSTEM), 
	ADMIN_PERMISSIONS_USER(Permissions.ADMIN), 
	JOBS_USER(Sets.newHashSet(ExtendedFeature.Projects));
	
	private int permission = Permissions.ALL;
	private Set<ExtendedFeature> extendedFeatures = Sets.newHashSet();  // default value has none on.

	private TestUser(Set<ExtendedFeature> extendedFeatures) { 
		this.extendedFeatures = extendedFeatures;
	}
	
	private TestUser(int permission) { 
		this.permission = permission;
	}

	public int getPermission() {
		return permission;
	}
	
	public Set<ExtendedFeature> getExtendedFeatures() { 
		return extendedFeatures;
	}
	
}
