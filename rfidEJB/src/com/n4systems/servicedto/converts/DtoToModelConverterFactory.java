package com.n4systems.servicedto.converts;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class DtoToModelConverterFactory {

	public static DtoToModelConverterFactory createFactory(SystemSecurityGuard systemSecurityGuard) {
		return new DtoToModelConverterFactory(systemSecurityGuard);
	}

	
	private final SystemSecurityGuard systemSecurityGuard;

	
	private DtoToModelConverterFactory(SystemSecurityGuard systemSecurityGuard) {
		this.systemSecurityGuard = systemSecurityGuard;
	}


	public ProductServiceDTOConverter createProductConverter() {
		return new ProductServiceDTOConverter(systemSecurityGuard,
				new FilteredIdLoader<PredefinedLocation>(new TenantOnlySecurityFilter(systemSecurityGuard.getTenantId()), PredefinedLocation.class));
	}
	
	public InspectionServiceDTOConverter createInspectionConverter() {
		return new InspectionServiceDTOConverter(systemSecurityGuard);
	}
	

}
