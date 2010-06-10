package com.n4systems.servicedto.converts;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.persistence.loaders.LoaderFactory;

public class PopulateAssignedUserFactory {

	private SystemSecurityGuard systemSecurityGuard;
	private LoaderFactory loaderFactory;
	
	public PopulateAssignedUserFactory(SystemSecurityGuard systemSecurityGuard, LoaderFactory loaderFactory) {
		this.systemSecurityGuard = systemSecurityGuard;
		this.loaderFactory = loaderFactory;
	}

	public PopulateAssignedUser getPopulateAssignedUser() {
		if (systemSecurityGuard.isAssignedToEnabled()) {
			return new PopulateAssignedUserImpl(loaderFactory);
		} else {
			return new DoNotPopulateAssignedUserImpl();
		}
	}
	
	
}
