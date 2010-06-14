package com.n4systems.servicedto.converts;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.persistence.loaders.LoaderFactory;

public class AssignedUserConverterFactory {

	private SystemSecurityGuard systemSecurityGuard;
	private LoaderFactory loaderFactory;
	
	public AssignedUserConverterFactory(SystemSecurityGuard systemSecurityGuard, LoaderFactory loaderFactory) {
		this.systemSecurityGuard = systemSecurityGuard;
		this.loaderFactory = loaderFactory;
	}

	public AssignedUserConverter getAssignedUserConverter() {
		if (systemSecurityGuard.isAssignedToEnabled()) {
			return new PopulateAssignedUserConverter(loaderFactory);
		} else {
			return new NullAssignedUserConverter();
		}
	}
	
}
